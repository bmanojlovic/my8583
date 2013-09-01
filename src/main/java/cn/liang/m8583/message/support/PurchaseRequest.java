package cn.liang.m8583.message.support;

import cn.liang.m8583.field.Password;
import cn.liang.m8583.field.basic.CardNumber;
import cn.liang.m8583.field.basic.CurrencyCode;
import cn.liang.m8583.field.basic.TransactionAmount;
import cn.liang.m8583.message.MessageType;
import cn.liang.m8583.message.Request;

/**
 * 消费请求
 * 
 * @author  Liang Yabao 2012-3-20
 */
public class PurchaseRequest extends Request {

	private CardNumber cardNumber = new CardNumber();
	private TransactionAmount transactionAmount = new TransactionAmount();
	private CurrencyCode currencyCode = new CurrencyCode();
	private Password password = new Password();
	
	
	public PurchaseRequest(MessageType mt) {
		super(mt);

	}

	public CardNumber getCardNumber() {
		return cardNumber;
	}

	public TransactionAmount getTransactionAmount() {
		return transactionAmount;
	}

	public Password getPassword() {
		return password;
	}

	public CurrencyCode getCurrencyCode() {
		return currencyCode;
	}

}
