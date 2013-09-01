package cn.liang.m8583.message.support;

import java.util.Arrays;
import java.util.List;

import cn.liang.m8583.message.MessageType;
import cn.liang.m8583.message.Request;
import cn.liang.m8583.transcoder.Message8583;


/**
 * 消费请求
 * @author 325336, Liang Yabao
 * 2012-3-20
 */
public class PurchaseRequest extends Request{
	
	public PurchaseRequest(MessageType mt) {
		super(mt);
		// TODO Auto-generated constructor stub
	}

	private String card;
	private long amount;
	
	//验证码是条件选项，并非必填字段。
	private String verificationCode;
	private String currencyCode;
	private byte[] pin;
	private List<String> waybill;
	
	public String getCard() {
		return card;
	}
	public void setCard(String card) {
		this.card = card;
	}
	public long getAmount() {
		return amount;
	}
	public void setAmount(long amount) {
		this.amount = amount;
	}
	public String getVerificationCode() {
		return verificationCode;
	}
	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public byte[] getPin() {
		return pin;
	}
	public void setPin(byte[] pin) {
		this.pin = pin;
	}
	public List<String> getWaybill() {
		return waybill;
	}
	public void setWaybill(List<String> waybill) {
		this.waybill = waybill;
	}


	@Override
	public Message8583 encode() {
		Message8583 mes = super.encode();
		mes.setCardNumber(card);
		mes.setAmount(amount);
		mes.setCurrencyCode(currencyCode);
		mes.setWaybill(waybill);
		mes.setPinData(pin);
		//验证码是条件选项，在金额比较大的时候才会填。
		if(verificationCode!=null){
			mes.setVerificationCode(verificationCode);
		}
		return mes;
	}
	
	@Override
	public void decode(Message8583 mes) {
		super.decode(mes);
		this.card = mes.getCardNumber();
		this.amount = mes.getAmount();
		this.currencyCode = mes.getCurrencyCode();
		this.pin = mes.getPinData();
		this.waybill = mes.getWaybill();
		if (mes.existVerificationCode()) {
			this.verificationCode = mes.getVerificationCode();
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof PurchaseRequest)) {
			return false;
		}
		PurchaseRequest pr = (PurchaseRequest) obj;
		if (pr.getVerificationCode() != null
				&& !pr.getVerificationCode().equals(this.verificationCode)) {
			return false;
		}
		if (this.getVerificationCode() != null
				&& !this.getVerificationCode().equals(pr.getVerificationCode())) {
			return false;
		}
		
		return pr.getCard().equals(this.card) && Arrays.equals(pin, pr.getPin())
				&& pr.getAmount() == this.amount
				&& pr.getCurrencyCode().equals(this.currencyCode)
				&& pr.getWaybill().equals(this.getWaybill());
	}

}
