package cn.liang.m8583.message.support;

import cn.liang.m8583.message.MessageType;
import cn.liang.m8583.message.Request;
import cn.liang.m8583.transcoder.Message8583;


/**
 * 冲正交易请求，现在应该只允许自动冲正。
 * @author  Liang Yabao
 * 2012-3-20
 */
public class ReverseRequest extends Request{

	
	public ReverseRequest(MessageType mt) {
		super(mt);
		// TODO Auto-generated constructor stub
	}

	private String card;
	private long amount;
	private String responseCode;
	private String processCode;

	@Override
	public  String getProcessCode() {
		return processCode;
	}
	/**
	 * 设置处理码，定长6个数字
	 * 冲正交易的处理码与冲正的原始交易相同，所以processCode是实例成员，不是静态成员
	 * @param processCode
	 */
	public void setProcessCode(String processCode){
		this.processCode = processCode;
	}
	
	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getCard() {
		return card;
	}

	public long getAmount() {
		return amount;
	}
	public void setCard(String card) {
		this.card = card;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}


	@Override
	public void decode(Message8583 mes) {
		super.decode(mes);
		this.card = mes.getCardNumber();
		this.amount = mes.getAmount();
		this.responseCode = mes.getResponseCode();
		this.processCode = mes.getProcessCode();
	}	

	@Override
	public Message8583 encode() {
		Message8583 mes = super.encode();
		
		mes.setAmount(this.amount);
		mes.setCardNumber(this.card);
		mes.setResponseCode(responseCode);
		mes.setProcessCode(processCode);
		return mes;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)||!(obj instanceof ReverseRequest)) {
			return false;
		}
		ReverseRequest rq = (ReverseRequest) obj;
		return super.equals(rq) && rq.getCard().equals(card)
				&& rq.getAmount() == amount
				&& rq.getResponseCode().equals(responseCode)
				&& rq.getProcessCode().equals(processCode);

	}

}
