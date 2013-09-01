package cn.liang.m8583.message.support;

import java.util.Arrays;

import cn.liang.m8583.field.basic.TransactionQuery;
import cn.liang.m8583.message.MessageType;
import cn.liang.m8583.message.Request;
import cn.liang.m8583.transcoder.Message8583;


/**
 * 交易明细查询请求
 * @author  Liang Yabao
 * 2012-3-20
 */
public class TransactionDetailRequest extends Request{

	public TransactionDetailRequest(MessageType mt) {
		super(mt);
		// TODO Auto-generated constructor stub
	}

	private TransactionQuery transactionQuery ;

	private String card;
	private byte[] pin;

	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}

	public byte[] getPin() {
		return pin;
	}

	public void setPin(byte[] pin) {
		this.pin = pin;
	}

	public TransactionQuery getTransactionQuery() {
		return transactionQuery;
	}

	public void setTransactionQuery(TransactionQuery transactionQuery) {
		this.transactionQuery = transactionQuery;
	}
	
	@Override
	public void decode(Message8583 mes) {
		super.decode(mes);
		this.transactionQuery = mes.getTransactionQuery();
		this.card = mes.getCardNumber();
		this.pin = mes.getPinData();
	}	

	@Override
	public Message8583 encode() {
		Message8583 mes = super.encode();
		mes.setTransactionQuery(transactionQuery);
		mes.setCardNumber(card);
		mes.setPinData(pin);
		return mes;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)||!(obj instanceof TransactionDetailRequest)) {
			return false;
		}
		TransactionDetailRequest tdr = (TransactionDetailRequest) obj;
		return tdr.getCard().equals(this.card)
				&& Arrays.equals(this.pin, tdr.getPin())
				&& tdr.getTransactionQuery().equals(this.getTransactionQuery());
	}
	
}
