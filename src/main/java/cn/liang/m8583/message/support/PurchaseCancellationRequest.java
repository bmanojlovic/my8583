package cn.liang.m8583.message.support;

import java.util.Arrays;

import cn.liang.m8583.message.MessageType;
import cn.liang.m8583.message.Request;
import cn.liang.m8583.transcoder.Message8583;


/**
 * 消费撤销请求
 * @author 325336, Liang Yabao
 * 2012-3-20
 */
public class PurchaseCancellationRequest extends Request{

	public PurchaseCancellationRequest(MessageType mt) {
		super(mt);
		// TODO Auto-generated constructor stub
	}

	private String card;
	private long amount;
	private String rrn;
	private byte[] pin;
	
	public byte[] getPin() {
		return pin;
	}
	public void setPin(byte[] pin) {
		this.pin = pin;
	}
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
	public String getRetrievalReferenceNumber() {
		return rrn;
	}
	public void setRetrievalReferenceNumber(String rrn) {
		this.rrn = rrn;
	}


	@Override
	public void decode(Message8583 mes) {

		super.decode(mes);
		this.card = mes.getCardNumber();
		this.amount = mes.getAmount();
		this.rrn = mes.getRetrievalReferenceNumber();
		this.pin = mes.getPinData();
	}	

	@Override
	public Message8583 encode() {
		
		Message8583 mes = super.encode();
		mes.setCardNumber(card);
		mes.setAmount(amount);
		mes.setRetrievalReferenceNumber(rrn);
		mes.setPinData(pin);
		return mes;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)||!(obj instanceof PurchaseCancellationRequest)) {
			return false;
		}
		PurchaseCancellationRequest pcr = (PurchaseCancellationRequest) obj;
		return  pcr.getCard().equals(card)
				&& pcr.getAmount() == amount
				&& pcr.getRetrievalReferenceNumber().equals(rrn)
				&& Arrays.equals(pcr.getPin(), pin);

	}
}
