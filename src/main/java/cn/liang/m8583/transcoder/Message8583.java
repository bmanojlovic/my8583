package cn.liang.m8583.transcoder;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.liang.m8583.field.BitMap;
import cn.liang.m8583.field.DataType;
import cn.liang.m8583.field.Field;
import cn.liang.m8583.field.basic.AdditionalAmount;
import cn.liang.m8583.field.basic.Argument;
import cn.liang.m8583.field.basic.TransactionDetail;
import cn.liang.m8583.field.basic.TransactionQuery;


/**
 * 8583编码的报文，用于切分与组装字节数组形态的报文。
 * @author 325336, Liang Yabao
 * 2012-3-12
 */
public class Message8583 {
	private static final int MAX_WAYBILL_LIST_SIZE = 999/12;

	private static final int WAYBILL_SIZE = 12;

	private static final Logger logger = LoggerFactory.getLogger(Message8583.class);
	
	private static final int WORKING_KEY_SIZE = 24;
	private static final int WORKING_KEY_CHECK_VALUE_SIZE = 4;
	
	public static final Charset CHARSET = Charset.forName("GBK");
	private byte[] messageType={0x00,0x00};
	private BitMap  bitMap = new BitMap();

	//fields中需要放置数据，是报文的一个成员变量
	public Field[] fields = new Field[] {
			/* FLD 1 */
			new Field("BIT MAP ", 8, 0, DataType.BINARY),
			/* FLD 2 */
			new Field("PRIMARY ACCOUNT NUMBER ", 20, 2,
					DataType.NUMBER),
			/* FLD 3 */
			new Field("PROCESSING CODE ", 6, 0, DataType.NUMBER),
			/* FLD 4 */
			new Field("TRANSACTION AMOUNT ", 12, 0, DataType.INTEGER),
			/* FLD 5 */
			Field.NO_USE_12,
			/* FLD 6 */
			Field.NO_USE_12,
			/* FLD 7 */
			new Field("TRANSACTION DATE AND TIME ", 10, 0,
					DataType.STRING),
			/* FLD 8 */
			Field.NO_USE_8,
			/* FLD 9 */
			Field.NO_USE_8,
			/* FLD 10 */
			Field.NO_USE_8,
			/* FLD 11 */
			new Field("Terminal Transaction Code ", 6, 0,
					DataType.NUMBER),
			/* FLD 12 */
			new Field("TIME, LOCAL TRANSACTION ", 6, 0,
					DataType.NUMBER),
			/* FLD 13 */
			new Field("DATE, LOCAL TRANSACTION ", 8, 0,
					DataType.NUMBER),
			/* FLD 14 */
			new Field("DATE, EXPIRATION ", 4, 0, DataType.STRING),
			/* FLD 15 */
			new Field("DATE, SETTLEMENT ", 4, 0, DataType.STRING),
			/* FLD 16 */
			Field.NO_USE,
			/* FLD 17 */
			new Field("DATE, CAPTURE ", 4, 0, DataType.STRING),
			/* FLD 18 */
			new Field("MERCHANT'S TYPE ", 4, 0, DataType.STRING),
			/* FLD 19 */
			Field.NO_USE,
			/* FLD 20 */
			Field.NO_USE,
			/* FLD 21 */
			Field.NO_USE,
			/* FLD 22 */
			new Field("POINT OF SERVICE ENTRY MODE ", 3, 0,
					DataType.STRING),
			/* FLD 23 */
			Field.NO_USE,
			/* FLD 24 */
			new Field("Network International identifier ", 3, 0,
					DataType.INTEGER),
			/* FLD 25 */
			new Field("POINT OF SERVICE CONDITION CODE ", 2, 0,
					DataType.STRING),
			/* FLD 26 */
			Field.NO_USE,
			/* FLD 27 */
			Field.NO_USE,
			/* FLD 28 */
			new Field("field27 ", 6, 0, DataType.STRING),
			/* FLD 29 */
			Field.NO_USE,
			/* FLD 30 */
			Field.NO_USE,
			/* FLD 31 */
			Field.NO_USE,
			/* FLD 32 */
			new Field("ACQUIRER INSTITUTION ID. CODE ", 11, 2,
					DataType.STRING),
			/* FLD 33 */
			//new Field("FORWARDING INSTITUTION ID. CODE ", 11, 2,
			//		Field.DataType.STRING),
			new Field("PHONE NUMBER ", 11, 2,
					DataType.NUMBER),
			/* FLD 34 */
			new Field("TRANSACTION QUERY ", 28, 2, DataType.NUMBER),
			/* FLD 35 */
			new Field("TRACK 2 DATA ", 37, 2, DataType.STRING),
			/* FLD 36 */
			new Field("TRACK 3 DATA ", 104, 3, DataType.STRING),
			/* FLD 37 */
			new Field("RETRIEVAL REFERENCE NUMBER ", 20, 0,
					DataType.STRING),
			/* FLD 38 */
			new Field("AUTH. IDENTIFICATION RESPONSE ", 6, 0,
					DataType.STRING),
			/* FLD 39 */
			new Field("RESPONSE CODE ", 2, 0, DataType.STRING),
			/* FLD 40 */
			Field.NO_USE,
			/* FLD 41 */
			new Field("TERMINAL ID. ", 12, 0,
					DataType.STRING),
			/* FLD 42 */
			new Field("NODE ID. ", 8, 0,
					DataType.STRING),
			/* FLD 43 */
			new Field("CARD ACCEPTOR NAME LOCATION ", 40, 0,
					DataType.STRING),
			/* FLD 44 */
			new Field("ADDITIONAL RESPONSE DATA ", 25, 2, DataType.STRING),
			/* FLD 45 */
			Field.NO_USE,
			/* FLD 46 */
			Field.NO_USE,
			/* FLD 47 */
			new Field("field47 ", 999, 3, DataType.STRING),
			/* FLD 48 */
			new Field("ADDITIONAL DATA --- PRIVATE ", 999, 3,
					DataType.STRING),
			/* FLD 49 */
			new Field("CURRENCY CODE,TRANSACTION ", 3, 0, DataType.STRING),
			/* FLD 50 */
			new Field("CURRENCY CODE,SETTLEMENT ", 3, 0, DataType.STRING),
			/* FLD 51 */
			Field.NO_USE,
			/* FLD 52 */
			new Field("PERSONAL IDENTIFICATION NUMBER DATA ", 8, 0,
					DataType.BINARY),
			/* FLD 53 */
			new Field("SECURITY RELATED CONTROL INformATION", 16, 0,
					DataType.STRING),
			/* FLD 54 */
			new Field("ADDITIONAL AMOUNTS ", 120, 3, DataType.STRING),
			/* FLD 55 */
			Field.NO_USE,
			/* FLD 56 */
			Field.NO_USE,
			/* FLD 57 */
			new Field("PREFERENTIAL INFO. ",999, 3, DataType.STRING),
			/* FLD 58 */
			Field.NO_USE,
			/* FLD 59 */
			new Field("TRANSACTION DETAIL ",999, 3, DataType.STRING),
			/* FLD 60 */
			new Field("WAYBILL NO. ",999, 3, DataType.NUMBER),
			/* FLD 61 */
			new Field("OPERATOR ",6, 3, DataType.STRING),
			/* FLD 62 */
			Field.NO_USE,
			/* FLD 63 */
			new Field("ERROR INFO. ",999, 3, DataType.STRING),
			/* FLD 64 */
			new Field("MESSAGE AUTHENTICATION CODE ", 8, 0,
					DataType.BINARY) };

	/**
	 * 把字节数组切分成各个字段。
	 * @param bytes	字节数组形态的8583报文，从消息类型开始，包括mac字段。
	 */
	public void decode(byte[] bytes) {

		int cur=0;

		messageType = new byte[2];
		System.arraycopy(bytes, 0, messageType, 0, 2);
		cur += 2;

		//bitmap
		byte[] bitmapBytes = new byte[8];
		System.arraycopy(bytes, cur, bitmapBytes, 0, 8);
		cur += 8;
		
		logger.debug("bitmap :{}",MessageUtil.byte2hex(bitmapBytes));
		
		bitMap= new BitMap(bitmapBytes);
		
		for (int index : bitMap.exist()) {

			Field field = fields[index];
			
			cur = field.read(bytes, cur);

		}
	}

	/**
	 * 把各个字段组装成一个8583字节报文。
	 * @return
	 */
	public byte[] encode() {

		//initMAC();
		//计算字节报文总长度
		int messageLength = messageType.length+BitMap.LENGTH_IN_BYTE;
		
		logger.debug("bitmap :{}",MessageUtil.byte2hex(bitMap.getBytes()));
		
		for (int index : bitMap.exist()) {
			Field field = fields[index];
			//字段的长度使用BCD压缩表示，0位、2位、3位分别用0字节、1字节、2字节表示。
			int fieldLength = (field.getVarLength()+1)/2;
			messageLength+=(field.getData().length+fieldLength);
		}
		logger.debug("the message length from message type to mac :{}",messageLength);
		
		byte[] bytes = new byte[messageLength];
		
		
		//messageType
		System.arraycopy(messageType, 0, bytes, 0, messageType.length);
		int cur =messageType.length;
		//bitmap
		System.arraycopy(bitMap.getBytes(), 0, bytes, cur, BitMap.LENGTH_IN_BYTE);
		cur+=BitMap.LENGTH_IN_BYTE;
		//other fields
		for (int index : bitMap.exist()) {
			Field field = fields[index];
			cur = field.write(bytes, cur);
		}
		return bytes;
	}
	
	/**
	 * 
	 * @return	4位数字的字符串
	 */
	public String getMessageType(){
		return new String(MessageUtil.bcd2num(messageType,4));
	}
	
	/**
	 * 
	 * @return	6位数字的字符串
	 */
	public String getProcessCode(){
		Field  field = fields[3-1];
		return new String(MessageUtil.bcd2num(field.getData(),field.getActLength()));
	}
	/**
	 * 定长，BCD压缩，2字节=4数字
	 * @param messageType
	 */
	public void setMessageType(String messageType){
		if(messageType.length()!= 4){
			throw new RuntimeException("message type's length should be 4 digit!");
		}
		this.messageType= MessageUtil.num2bcd(messageType.toCharArray());
	}
	/**
	 * 定长，BCD压缩，3字节=6数字
	 * @param processCode
	 */
	public void setProcessCode(String processCode){
		int index = 3-1;
		if(processCode.length()!=fields[index].getMaxLength()){
			throw new RuntimeException("processCode's length not right!");
		}
		byte[] data = MessageUtil.num2bcd(processCode.toCharArray());
		
		fields[index].setActLength(processCode.length());
		fields[index].setData(data);
		bitMap.setExistField(index);
	}


	/**
	 * 卡号
	 * @return primary account number
	 */
	public String getCardNumber() {
		Field field = fields[2 - 1];
		if ((field.getActLength() + 1) / 2 != field.getData().length) {
			throw new RuntimeException("the lengthInByte is not right!");
		}
		return new String(MessageUtil.bcd2num(field.getData(),
				field.getActLength()));
	}
	
	/**
	 * 最大12个数字
	 * @return 交易金额
	 */
	public  long getAmount() {
		//第4个字段，对应数组中第3个索引位置
		long amount =0L;
		Field field = fields[4-1];
		byte[] data = field.getData();
		int base = 1;//个位的基数为一。
		//每个字节表示两个数字，
		//右靠BCD码表示
		//从最后一个字节开始，即低位算起
		for(int i = data.length-1;i>=0;i--){
			byte b = data[i];
			//低四位，表示一个数字，乘以所在的位置基数
			amount +=(b&0x0F)*base;
			base*=10;//十进制
			//高四位
			amount +=((b&0xF0)>>4)*base;
			base*=10;//十进制
		}
		return amount;
	}

	/**
	 * 个人密码，明文，最多8个数字。
	 * @return
	 */
	/*
	public  String getPassword() {
		Field field = fields[52 - 1];
		byte[] bytes = field.getData();//应该有64位，即8个字节
		char[] chars = MessageUtil.byte2HexDigit(bytes);
		return new String(chars);
	}
*/
	/**
	 * 个人密码，密文，64bit，如果不存在，则为空。
	 * @return
	 */
	public  byte[] getPinData( ) {
		Field field = fields[52 - 1];
		byte[] bytes = field.getData();//应该有64位，即8个字节
		return bytes;
	}


	/**
	 * 
	 * @return pos终端标识
	 */
	public  String getTerminalID() {
		Field  field= fields[41-1];
		if(field.getMaxLength() != field.getData().length){
			throw new RuntimeException("the lengthInByte is not right!");
		}
		return MessageUtil.asc2str(field.getData());
	}

	/**
	 * 
	 * @return pos所属节点机构标识
	 */
	public  String getNodeID() {
		Field  field= fields[42-1];
		if(field.getMaxLength() != field.getData().length){
			throw new RuntimeException("the lengthInByte is not right!");
		}
		return MessageUtil.asc2str(field.getData());
	}


	/**
	 * 
	 * @return pos操作员
	 */
	public  String getOperator() {
		Field  field=fields[61-1];
		if(field.getActLength() != field.getData().length){
			throw new RuntimeException("the lengthInByte is not right!");
		}
		return MessageUtil.asc2str(field.getData());
	}

	/**
	 * 
	 * @return pos流水号
	 */
	public String getTTC() {
		Field field = fields[11 - 1];
		if ((field.getMaxLength()+1)/2 != field.getData().length) {
			throw new RuntimeException("the lengthInByte is not right!");
		}
		return new String(MessageUtil.bcd2num(field.getData(),
				field.getActLength()));
	}

	public void setTransactionDateTime(Date dateTime) {
		DateFormat dateF = new SimpleDateFormat("yyyyMMdd");
		DateFormat timeF = new SimpleDateFormat("HHmmss");
		byte[] date = MessageUtil.num2bcd(dateF.format(dateTime).toCharArray());
		byte[] time = MessageUtil.num2bcd(timeF.format(dateTime).toCharArray());
		fields[13-1].setData(date);
		fields[12-1].setData(time);
		bitMap.setExistField(13-1);
		bitMap.setExistField(12-1);
	}
	
	/**
	 * 小于 1000000000000L
	 * @param amount
	 */
	public void setAmount(long amount) {
		if (amount >= 1000000000000L) {
			throw new RuntimeException("the amount is too big!");
		}
		int index = 4-1;//amount's index is 3
		Field field = fields[index];
		//字段定长
		int byteLen = (field.getMaxLength()+1)/2;
		byte[] bytes = new byte[byteLen];
		int base = 10;//十进制
		
		for (int i = byteLen-1; amount > 0 && i >= 0; i--) {

			// low bits
			bytes[i] |= (amount % base);//BCD形式表示低四位
			amount /= base;
			// high bits
			bytes[i] |= ((amount % base) << 4);//BCD形式表示高四位
			amount /= base;
		}
		
		field.setData(bytes);
		bitMap.setExistField(index);
		
	}

	/**
	 * 左靠BCD码表示，1字节=2数字
	 * @param card
	 */
	public void setCardNumber(String card){
		int index = 2-1;
		Field field = fields[index];
		//card number is bcd code,2N=1B
		if(card.length()>field.getMaxLength()){
			throw new RuntimeException("the card number is too long!");
		}
		char[] chars = card.toCharArray();
		byte[] bytes = MessageUtil.num2bcd(chars);
		
		field.setData(bytes);
		field.setActLength(card.length());
		bitMap.setExistField(index);
	}
/*
	public void setPinData(String pin) {
		int index = 52 - 1;
		Field field = fields[index];
		if (pin.length() != field.getLength() * 2) {
			throw new RuntimeException(
					"the pin data must be 64 bits,as a word 16 hex char!");
		}
		char[] chars = pin.toCharArray();

		byte[] bytes = new byte[field.getLength()];
		for (int i = 0; i < bytes.length; i++) {
			int hign = MessageUtil.digit4Hex(chars[i * 2]);
			int low = MessageUtil.digit4Hex(chars[i * 2 + 1]);
			bytes[i] |= hign << 4;//字符转换为高四位
			bytes[i] |= low;//字符转换为低四位
		}
		field.setData(bytes);
		bitMap.setExistField(index);
	}
	*/
	
	/**
	 * 定长，8字节，个人密码密文
	 * @param pin
	 */
	public void setPinData(byte[] pin) {
		int index = 52 - 1;
		Field field = fields[index];
		if (pin.length != field.getMaxLength()) {
			throw new RuntimeException(
					"the pin data must be 64 bits,as a word 16 hex char!");
		}
		
		field.setData(pin);
		bitMap.setExistField(index);
	}

	/**
	 * 终端号
	 * @param tid
	 */
	public void setTerminalID(String tid){
		//terminal id is fixed length
		int index = 41-1;
		Field  field= fields[index];
		if (tid.length() != field.getMaxLength()) {
			throw new RuntimeException("the terminal id's length should be "
					+ field.getMaxLength() + "!");
		}
		byte[] data = new byte[field.getMaxLength()];
		byte[] bytes = MessageUtil.str2asc(tid);
		System.arraycopy(bytes, 0, data, 0, bytes.length);
		field.setData(data);
		bitMap.setExistField(index);
	}
	
	/**
	 * 分点部标识
	 * @param nid
	 */
	public void setNodeID(String nid){
		int index = 42-1;
		Field  field= fields[index];
		if (nid.length() != field.getMaxLength()) {
			throw new RuntimeException("the node id's length should be "
					+ field.getMaxLength() + "!");
		}
		byte[] data = new byte[field.getMaxLength()];
		byte[] bytes = MessageUtil.str2asc(nid);
		System.arraycopy(bytes, 0, data, 0, bytes.length);
		field.setData(data);
		bitMap.setExistField(index);
	}
	/**
	 * 操作员
	 * @param operator
	 */
	public void setOperator(String operator){
		int index= 61-1;
		Field  field=fields[index];
		if(operator.length()>field.getMaxLength()){
			throw new RuntimeException("the operator's length is too long!");
		}
		byte[] bytes = MessageUtil.str2asc(operator);
		field.setData(bytes);
		field.setActLength(operator.length());
		bitMap.setExistField(index);
	}
	
	/**
	 * 终端交易号（即流水号）
	 * @param ttc
	 */
	public void setTTC(String ttc){
		//ttc is fixed length
		int index = 11-1;
		Field  field=fields[index];
		char[] chars = ttc.toCharArray();
		//每个字节可以表示两个数字
		if(ttc.length()!=field.getMaxLength()){
			throw new RuntimeException("the ttc's length  is not right!");
		}
		byte[] bytes = MessageUtil.num2bcd(chars);
		field.setData(bytes);
		bitMap.setExistField(index);
	}

	/**
	 * 响应码
	 * @param responseCode
	 */
	public void setResponseCode(String responseCode) {
		int index = 39-1;
		Field field = fields[index];
		if(responseCode.length()!=field.getMaxLength()){
			throw new RuntimeException("the responseCode's length should be "
					+ field.getMaxLength() + "!");
		}
		field.setData(MessageUtil.str2asc(responseCode));
		bitMap.setExistField(index);
	}

	/**
	 * 交易日期与时间
	 * @return
	 */
	public Date getTransactionDateTime() {
		Field dateField = fields[13 - 1];
		Field timeField = fields[12 - 1];
		String d = new String(MessageUtil.bcd2num(dateField.getData(),
				dateField.getActLength()));
		String t = new String(MessageUtil.bcd2num(timeField.getData(),
				timeField.getActLength()));
		DateFormat dateF = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			return dateF.parse(d + t);
		} catch (ParseException e) {
			throw new RuntimeException("DateFormat Exception:", e);
		}
	}

	/**
	 * 响应码
	 * @return
	 */
	public String getResponseCode() {
		return MessageUtil.asc2str(fields[39-1].getData());
	}
	
	/**
	 * 附加金额列表
	 * @param aal
	 */
	public void setAdditionalAmountList(List<AdditionalAmount> aal){
		int index = 54-1;
		Field field = fields[index];
		int len = aal.size()*AdditionalAmount.SIZE;
		byte[] bytes = new byte[len];
		int offset = 0;
		for(AdditionalAmount aa : aal){
			System.arraycopy(aa.encode(), 0, bytes, offset, AdditionalAmount.SIZE);
			offset += AdditionalAmount.SIZE;
		}
		field.setData(bytes);
		field.setActLength(len);
		bitMap.setExistField(index);
	}
	
	/**
	 * 附加金额列表
	 * @return
	 */
	public List<AdditionalAmount> getAdditionalAmountList(){
		int index = 54-1;
		Field field = fields[index];
		int additionalAmountSize = field.getActLength()/AdditionalAmount.SIZE;
		List<AdditionalAmount> list = new ArrayList<AdditionalAmount>(additionalAmountSize);
		byte[] data = field.getData();
		byte[] bytes = new byte[AdditionalAmount.SIZE];
		for(int i =0;i<additionalAmountSize;i++){
			System.arraycopy(data, i*AdditionalAmount.SIZE, bytes, 0, AdditionalAmount.SIZE);
			AdditionalAmount aa = new AdditionalAmount();
			list.add(aa.decode(bytes));
		}
		
		return list;
	}
	
	/**
	 * 优惠信息
	 * @param preferentialInformation
	 */
	public void setPreferentialInformation(String preferentialInformation) {
		int index = 57 - 1;
		Field field = fields[index];
		try {
			byte[] data = preferentialInformation.getBytes(CHARSET.name());
			if (data.length > field.getMaxLength()) {
				throw new RuntimeException(
						"Preferential Information is more than max "
								+ field.getMaxLength());
			}
			field.setData(data);
			field.setActLength(data.length);
			bitMap.setExistField(index);
		} catch (UnsupportedEncodingException uee) {
			throw new RuntimeException(uee);
		}

	}

	/**
	 * 获得优惠信息
	 * 
	 * @return
	 */
	public String getPreferentialInformation() {
		try {
			int index = 57 - 1;
			Field field = fields[index];
			String str = new String(field.getData(), CHARSET.name());
			return str;
		} catch (UnsupportedEncodingException uee) {
			throw new RuntimeException(uee);
		}
	}
	
	/**
	 * 用户名，变长
	 * 
	 * @param userName
	 */
	public void setUserName(String userName) {
		try {
			int index = 44 - 1;
			Field field = fields[index];

			byte[] data = userName.getBytes(CHARSET.name());
			if (data.length > field.getMaxLength()) {
				throw new RuntimeException("user name's size is more than max "
						+ field.getMaxLength());
			}
			field.setData(data);
			field.setActLength(data.length);
			bitMap.setExistField(index);
		} catch (UnsupportedEncodingException uee) {
			throw new RuntimeException(uee);
		}
	}
	
	/**
	 * 用户名
	 * 
	 * @return
	 */
	public String getUserName() {
		try {
			int index = 44 - 1;
			Field field = fields[index];
			String str = new String(field.getData(), CHARSET.name());
			return str;
		} catch (UnsupportedEncodingException uee) {
			throw new RuntimeException(uee);
		}
	}
	
	/**
	 * 系统交易号
	 * @param rrn	定长30字节
	 */
	public void setRetrievalReferenceNumber(String rrn){
		int index = 37-1;
		Field field = fields[index];
		if(rrn.length()!= field.getMaxLength()){
			throw new RuntimeException("Retrieval Reference Number's size is not right!");
		}
		byte[] bytes = MessageUtil.str2asc(rrn);
		field.setData(bytes);
		bitMap.setExistField(index);
	}
	
	/**系统交易号*/
	public String getRetrievalReferenceNumber(){
		int index = 37-1;
		Field field = fields[index];
		return MessageUtil.asc2str(field.getData());
	}
	
	/**交易明细*/
	public TransactionDetail getTransactionDetail() {
		int index = 59-1;
		Field field = fields[index];
		TransactionDetail detail = new TransactionDetail();
		detail.decode(field.getData());
		return detail;
	}

	/**交易明细*/
	public void setTransactionDetail(TransactionDetail transactionDetail) {
		int index = 59-1;
		Field field = fields[index];
		byte[] data = transactionDetail.encode();

		field.setData(data);
		field.setActLength(data.length);
		bitMap.setExistField(index);
	}
	
	/**验证码*/
	public void setVerificationCode(String code){
		int index = 38-1;
		Field field = fields[index];
		if(code.length()!=field.getMaxLength()){
			throw new RuntimeException("verification code must be "+field.getMaxLength());
		}
		field.setData(MessageUtil.str2asc(code));
		field.setActLength(code.length());
		bitMap.setExistField(index);
	}
	
	/**验证码*/
	public String getVerificationCode(){
		int index = 38-1;
		Field field = fields[index];
		//位图标志为0时，data即为null。否则即使没有数据也有byte[0]
		//if (field.getData() == null) {
		//	return null;
		//} else {
			return MessageUtil.asc2str(field.getData());
		//}
	}
	
	public boolean existVerificationCode(){
		int index = 38-1;
		return bitMap.exist(index);
	}
	
	/**
	 * 币种，定长，AN3
	 * @param code
	 */
	public void setCurrencyCode(String code){
		int index = 50-1;
		Field field = fields[index];
		if(code.length()!=field.getMaxLength()){
			throw new RuntimeException("currency code must be "+field.getMaxLength());
		}
		field.setData(MessageUtil.str2asc(code));
		field.setActLength(code.length());
		bitMap.setExistField(index);
	}
	
	/**
	 * 币种，定长，AN3
	 */
	public String getCurrencyCode(){
		int index = 50-1;
		Field field = fields[index];
		return MessageUtil.asc2str(field.getData());
	}
	
	/**设置运单号列表*/
	public void setWaybill(List<String> waybillList){
		
		if(waybillList.size()>MAX_WAYBILL_LIST_SIZE){
			throw new RuntimeException("the most waybill is "+MAX_WAYBILL_LIST_SIZE);
		}
		
		int index = 60-1;
		Field field = fields[index];
		
		byte[] data = new byte[waybillList.size()*WAYBILL_SIZE/2];
		for(int i =0;i<waybillList.size();i++){
			String s = waybillList.get(i);
			if(s.length()!=WAYBILL_SIZE){
				throw new RuntimeException(" the waybill length should be "+WAYBILL_SIZE);
			}
			byte[] bs= MessageUtil.num2bcd(s.toCharArray());
			System.arraycopy(bs, 0, data, i*WAYBILL_SIZE/2, WAYBILL_SIZE/2);
		}
		field.setData(data);
		field.setActLength(waybillList.size()*WAYBILL_SIZE);
		bitMap.setExistField(index);
	}
	
	/**获得运单号列表*/
	public List<String> getWaybill(){
		int index = 60-1;
		Field field = fields[index];
		byte[] data = field.getData();
		//运单号个数
		int size = data.length*2/WAYBILL_SIZE;
		List<String> list = new ArrayList<String>(size);
		char[] chars = MessageUtil.bcd2num(data, field.getActLength());
		for(int i =0;i<size;i++){
			//list.add(MessageUtil.asc2str(data, i*14, 14));
			list.add(new String(chars, i*WAYBILL_SIZE, WAYBILL_SIZE));
		}
		return list;
	}
	
	/**设置交易明细查询*/
	public void setTransactionQuery(TransactionQuery transactionQuery){
		int index = 34-1;
		Field field = fields[index];
		
		//ASCII表示
		byte[] data = transactionQuery.encode();
		//元素个数
		field.setActLength(data.length);
		
		//BCD码表示
		String ds = MessageUtil.asc2str(data);
		field.setData(MessageUtil.num2bcd(ds.toCharArray()));
		
		bitMap.setExistField(index);
	}
	
	/**获得交易明细查询*/
	public TransactionQuery getTransactionQuery(){
		int index = 34-1;
		Field field = fields[index];
		char[] chars = MessageUtil.bcd2num(field.getData(), field.getActLength());
		TransactionQuery transactionQuery = new TransactionQuery();
		transactionQuery.decode(MessageUtil.str2asc(new String(chars)));
		return transactionQuery;
	}
	
	/**
	 * 消息认证码
	 * @return
	 */
	public byte[] getMac(){
		int index = 64-1;
		Field field = fields[index];
		return field.getData();
	}
	
	/**
	 * 个人新密码，密文
	 * @param pin
	 */
	public void setNewPintData(byte[] pin){
		int index = 48 - 1;
		Field field = fields[index];
		if (pin.length != 8) {
			throw new RuntimeException(
					"the pin data must be 64 bits,most 8 digits!");
		}
		
		field.setData(pin);
		field.setActLength(8);
		bitMap.setExistField(index);
	}
	
	/**
	 * 个人新密码，密文，64bit，如果不存在，则为空。
	 * @return
	 */
	public  byte[] getNewPinData() {
		Field field = fields[48 - 1];
		byte[] bytes = field.getData();//应该有64位，即8个字节
		return bytes;
	}
	
	/**
	 * 新工作密钥
	 * （PIK 24字节）+（checkvalue 4字节）+（MIK 24字节）+（checkvalue 4字节）
	 * @param newWorkingKey
	 */
	public void setNewWorkingKeyData(byte[] newWorkingKey){
		int index = 48 - 1;
		Field field = fields[index];
		int len = (WORKING_KEY_SIZE+WORKING_KEY_CHECK_VALUE_SIZE)*2;
		if (newWorkingKey.length != len) {
			throw new RuntimeException(
					"the working key data must be 192 bits,as 24 bytes!");
		}
		
		field.setData(newWorkingKey);
		field.setActLength(len);
		bitMap.setExistField(index);
	}
	
	/**
	 * 终端新密钥，密文，192bit，24个字节。
	 * @return
	 */
	public  byte[] getNewWorkingKeyData() {
		Field field = fields[48 - 1];
		byte[] bytes = field.getData();
		return bytes;
	}

	/**
	 * 终端参数
	 * @param arg
	 */
	public void setArgument(Argument arg){
		int index = 63-1;
		Field field = fields[index];
		field.setData(arg.encode());
		field.setActLength(field.getData().length);
		bitMap.setExistField(index);
	}
	
	/**
	 * 终端参数
	 * @return
	 */
	public Argument getArgument(){
		int index = 63-1;
		Field field = fields[index];
		return new Argument().decode(field.getData());
	}
	
	/**
	 * 方法说明：<br>
	 * 获得快捷支付对应的手机号 
	 *
	 * @return
	 */
	public String getPhoneNumber(){
		Field field = fields[33 - 1];
		if ((field.getActLength() + 1) / 2 != field.getData().length) {
			throw new RuntimeException("the lengthInByte is not right!");
		}
		return new String(MessageUtil.bcd2num(field.getData(),
				field.getActLength()));
	}
	
	/**
	 * 方法说明：<br>
	 * 设置快捷支付对应的手机号 
	 *
	 * @param phoneNumber
	 */
	public void setPhoneNumber(String phoneNumber){
		int index = 33-1;
		Field field = fields[index];
		//phone number is bcd code,2N=1B
		if(phoneNumber.length()>field.getMaxLength()){
			throw new RuntimeException("the phone number is too long!");
		}
		char[] chars = phoneNumber.toCharArray();
		byte[] bytes = MessageUtil.num2bcd(chars);
		
		field.setData(bytes);
		field.setActLength(phoneNumber.length());
		bitMap.setExistField(index);
	}
}
