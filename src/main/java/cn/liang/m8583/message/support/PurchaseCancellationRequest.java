package cn.liang.m8583.message.support;

import cn.liang.m8583.field.Password;
import cn.liang.m8583.field.basic.CardNumber;
import cn.liang.m8583.field.basic.RetrievalReferenceNumber;
import cn.liang.m8583.field.basic.TransactionAmount;
import cn.liang.m8583.message.MessageType;
import cn.liang.m8583.message.Request;


/**
 * 消费撤销请求
 * @author  Liang Yabao
 * 2012-3-20
 */
public class PurchaseCancellationRequest extends Request{

	public PurchaseCancellationRequest(MessageType mt) {
		super(mt);
	}

	private CardNumber cardNumber = new CardNumber();
	private TransactionAmount transactionAmount = new TransactionAmount();
	private RetrievalReferenceNumber retrievalReferenceNumber = new RetrievalReferenceNumber();
	private Password password = new Password();

	public CardNumber getCardNumber() {
		return cardNumber;
	}
	public TransactionAmount getTransactionAmount() {
		return transactionAmount;
	}
	public Password getPassword() {
		return password;
	}
	public RetrievalReferenceNumber getRetrievalReferenceNumber() {
		return retrievalReferenceNumber;
	}
}
