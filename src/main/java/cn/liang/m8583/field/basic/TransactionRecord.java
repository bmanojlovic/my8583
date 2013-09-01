package cn.liang.m8583.field.basic;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.liang.m8583.transcoder.MessageUtil;

/**
 * 交易记录
 * @author 325336, Liang Yabao
 * 2012-3-20
 */
public class TransactionRecord {

	private static final int AMOUNT_LENGTH = 12;
	private static final int RRN_LENGTH = 20;
	private static final int TTC_SIZE = 6;
	private static final int CURRENCY_CODE_SIZE = 3;
	private static final long MAX_AMOUNT = 1000000000000L-1;
	private static final DateFormat dateF = new SimpleDateFormat("yyyyMMddHHmmss");

	private static final Logger logger = LoggerFactory.getLogger(TransactionRecord.class);
	
	
	//卡号
	private String card;
	
	//N2记录类型
	private int type;
	
	//交易流水 号
	private String rrn;
	//POS流水号
	private String ttc;
	
	//交易的日期与时间
	private Date dateTime;
	
	//交易金额 
	private long amount;

	//交易币种
	private String currency;
	
	//运单列表
	private List<String> waybillList = new ArrayList<String>();
	
	//运单号列表的大小，只在decode方法时被使用，作为一个中间量
	private int waybillSize;
	
	//额外信息，key与value都可以是中文
	//巴枪客户端只要显示，不需要作业务处理。
	private Map<String,String> extra = new HashMap<String,String>();
	
	//获取运单列表
	public List<String> getWaybillList() {
		return waybillList;
	}

	/**
	 * 设置运单列表，每个运单应该是12个ASCII字符
	 * @param waybillList
	 */
	public void setWaybillList(List<String> waybillList) {
		this.waybillList = waybillList;
	}

	/**
	 * 设置附加信息说明
	 * @param name	
	 * @param info
	 */
	public void setAdditionalInfo(String name,String info){
		extra.put(name, info);
	}
	
	/**
	 * 获得附加信息
	 * @param name
	 * @return	附加信息内容
	 */
	public String getAdditionalInfo(String name){
		return extra.get(name);
	}
	
	@Deprecated
	public Map<String, String> getExtra() {
		return extra;
	}

	/**
	 * 额外信息，key与value都可以是中文
	 * 巴枪客户端只要显示，不需要作业务处理。
	 * @param extra
	 */
	@Deprecated
	public void setExtra(Map<String, String> extra) {
		this.extra = extra;
	}

	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getRetrievalReferenceNumber() {
		return rrn;
	}
	
	/**
	 * 设置系统业务流水号，定长AN20
	 * @param rrn
	 */
	public void setRetrievalReferenceNumber(String rrn) {
		this.rrn = rrn;
	}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public long getAmount() {
		return amount;
	}
	public void setAmount(long amount) {
		this.amount = amount;
	}
	
	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}

	public String getTerminalTransactionCode() {
		return ttc;
	}

	/**
	 * 设置终端交易流水号，定长N6
	 * @param ttc
	 */
	public void setTerminalTransactionCode(String ttc) {
		this.ttc = ttc;
	}

	public String getCurrency() {
		return currency;
	}

	/**
	 * 设置交易币种，定长AN3
	 * @param currency
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}


	public byte[] encode() {
		if(type>99||type<0) {
			throw new RuntimeException("the record type should be two digit!");
		}
		if(rrn.length()!=RRN_LENGTH){
			throw new RuntimeException("the record's RRN_LENGTH should be "+RRN_LENGTH);
		}
		if(amount>MAX_AMOUNT){
			throw new RuntimeException("the amount is too big!");
		}
		if(currency.length()>CURRENCY_CODE_SIZE){
			throw new RuntimeException("CURRENCY_CODE_SIZE should be "+CURRENCY_CODE_SIZE);
		}
		if(ttc.length()>TTC_SIZE){
			throw new RuntimeException("terminal trasaction code's size is "+TTC_SIZE);
		}
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			//卡号，2位变长，LLVAR
			baos.write(MessageUtil.int2asc(card.length(), 2));
			baos.write(MessageUtil.str2asc(card));
			//交易类型
			baos.write(MessageUtil.int2asc(type, 2));
			//系统检索号
			baos.write(MessageUtil.str2asc(rrn));
			//终端流水号
			baos.write(MessageUtil.str2asc(ttc));
			//交易日期与时间
			baos.write(MessageUtil.str2asc(dateF.format(dateTime)));
			//交易金额 
			byte[] ambs = new byte[12];
			MessageUtil.long2asc(amount, 12, ambs, 0);
			baos.write(ambs);
			//交易币种
			baos.write(MessageUtil.str2asc(currency));
			//运单号个数
			baos.write(MessageUtil.int2asc(waybillList.size(), 2));
			//附加信息
			byte[] ext = fromObject(extra).getBytes("GBK");
			baos.write(MessageUtil.int2asc(ext.length, 3));
			baos.write(ext);
			byte[] bytes = baos.toByteArray();
			if (logger.isDebugEnabled()) {
				//logger.debug("[json]{}", JSONUtils.fromObject(this));
				logger.debug("[byte]{}", new String(MessageUtil.byte2hex(bytes)));
			}
			return bytes;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}
	//private static ObjectMapper mapper = new ObjectMapper();

	/**
	 * 
	 * Description:
	 * <BR>
	 * convert java Object to JSON String
	 * @param obj       java Object, like List, Map, POJO
	 * @return          JSON format String
	 */
	public static String fromObject(Object obj) {
		String resultStr = null;
		try {
			//resultStr = mapper.writeValueAsString(obj);
		} catch (Exception e) {
		}
		return resultStr;
	}
	public TransactionRecord decode(byte[] bytes){
		//卡号
		int cur = 0;
		int cardlen = (int) MessageUtil.asc2long(bytes, cur, 2);
		cur += 2;
		this.card = MessageUtil.asc2str(bytes, cur, cardlen);
		cur+=cardlen;
		//交易类型
		this.type = (int) MessageUtil.asc2long(bytes, cur, 2);
		cur+=2;
		//系统检索号
		this.rrn = MessageUtil.asc2str(bytes, cur, this.RRN_LENGTH);
		cur+= RRN_LENGTH;
		//终端流水号
		this.ttc = MessageUtil.asc2str(bytes, cur, this.TTC_SIZE);
		cur+=TTC_SIZE;
		//交易日期与时间
		String dt = MessageUtil.asc2str(bytes, cur, 14);//日期时间
		cur+=14;
		try {
			this.dateTime = dateF.parse(dt);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		//交易金额 
		this.amount = MessageUtil.asc2long(bytes, cur, AMOUNT_LENGTH);
		cur+=AMOUNT_LENGTH;
		//交易币种
		this.currency = MessageUtil.asc2str(bytes, cur, CURRENCY_CODE_SIZE);
		cur+=CURRENCY_CODE_SIZE;
		//运单号，后续拆分运单号时需用到。
		this.waybillSize = (int) MessageUtil.asc2long(bytes, cur, 2);
		cur+=2;
		
		//附加信息
		int extralen = (int)MessageUtil.asc2long(bytes, cur, 3);
		cur+= 3;
		
		try {
			String extraStr = new String(bytes, cur, extralen,"GBK");
			cur+= extralen;
			this.extra = mapFormJSONStr(extraStr);
		} catch (UnsupportedEncodingException e) {
			new RuntimeException(e);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("[byte]{}", new String(MessageUtil.byte2hex(bytes)));
			//logger.debug("[json]{}", JSONUtils.fromObject(this));
		}
		return this;
	}
	
	/**
	 * 
	 * Description:
	 * <BR>
	 * convert from JSON String to Map
	 * @param jsonStr      JSON String
	 * @return             java Map Object
	 */
	public static Map mapFormJSONStr(String jsonStr) {
		try {
			//return mapper.readValue(jsonStr, Map.class);
		} catch (Exception e) {
		}
		return null;
	}
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof TransactionRecord)) {
			return false;
		}
		TransactionRecord tr = (TransactionRecord) obj;
		return this.card.equals(tr.getCard())
				&& this.currency.equals(tr.getCurrency())
				&& this.ttc.equals(tr.getTerminalTransactionCode())
				&& this.amount== tr.getAmount()
				&& dateF.format(this.dateTime).equals(dateF.format(tr.getDateTime()))
				&& this.rrn.equals(tr.getRetrievalReferenceNumber())
				&& this.type==tr.getType();

	}

	/**
	 * 在解码后，拆分运单号
	 * @param it
	 */
	void splitWaybill(Iterator<String> it) {
		logger.debug("waybill count:{}",waybillSize);
		for(int i=0;i<waybillSize;i++){
			waybillList.add(it.next());
		}
	}
}
