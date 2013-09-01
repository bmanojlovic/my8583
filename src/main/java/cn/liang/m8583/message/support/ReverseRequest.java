package cn.liang.m8583.message.support;

import cn.liang.m8583.field.basic.CardNumber;
import cn.liang.m8583.field.basic.ProcessCode;
import cn.liang.m8583.field.basic.ResponseCode;
import cn.liang.m8583.field.basic.TransactionAmount;
import cn.liang.m8583.message.MessageType;
import cn.liang.m8583.message.Request;


/**
 * 冲正交易请求，现在应该只允许自动冲正。
 * @author  Liang Yabao
 * 2012-3-20
 */
public class ReverseRequest extends Request{

	
	public ReverseRequest(MessageType mt) {
		super(mt);
	}

	private CardNumber card = new CardNumber();
	private TransactionAmount transactionAmount = new TransactionAmount();
	private ResponseCode responseCode = new ResponseCode();
	private ProcessCode processCode = new ProcessCode();

	@Override
	public  ProcessCode getProcessCode() {
		return processCode;
	}

	public CardNumber getCard() {
		return card;
	}

	public TransactionAmount getTransactionAmount() {
		return transactionAmount;
	}

	public ResponseCode getResponseCode() {
		return responseCode;
	}

	
	
}
