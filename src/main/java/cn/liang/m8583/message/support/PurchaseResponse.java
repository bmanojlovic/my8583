package cn.liang.m8583.message.support;

import java.util.Collections;
import java.util.List;

import cn.liang.m8583.field.basic.AdditionalAmount;
import cn.liang.m8583.field.basic.CardNumber;
import cn.liang.m8583.field.basic.TransactionAmount;
import cn.liang.m8583.message.MessageType;
import cn.liang.m8583.message.RequestResponse;


/**
 * 消费响应
 * @author  Liang Yabao
 * 2012-3-20
 */
public class PurchaseResponse extends RequestResponse{
	
	public PurchaseResponse(MessageType mt) {
		super(mt);
	}

	//避免null值，在响应码不为“00”时，可以使用默认值
	private CardNumber cardNumber= new CardNumber();

	private TransactionAmount transactionAmount = new TransactionAmount();
	
	private List<AdditionalAmount> aal=Collections.emptyList();
	

	public List<AdditionalAmount> getAal() {
		return aal;
	}
	public void setAal(List<AdditionalAmount> aal) {
		this.aal = aal;
	}
	public CardNumber getCardNumber() {
		return cardNumber;
	}
	public TransactionAmount getTransactionAmount() {
		return transactionAmount;
	}



}
