package cn.liang.m8583.message.support;

import cn.liang.m8583.field.basic.CardNumber;
import cn.liang.m8583.field.basic.TransactionAmount;
import cn.liang.m8583.message.MessageType;
import cn.liang.m8583.message.Request;


/**
 * 充值请求报文
 * @author  Liang Yabao
 * 2012-3-12
 * 
 */
public class RechargeRequest extends Request {
	

	private CardNumber cardNumber;
	private TransactionAmount transactionAmount = new TransactionAmount();

	
	public RechargeRequest(MessageType mt) {
		super(mt);

	}


	public CardNumber getCardNumber() {
		return cardNumber;
	}


	public TransactionAmount getTransactionAmount() {
		return transactionAmount;
	}


	
}
