package cn.liang.m8583.message.support;

import java.util.Arrays;

import cn.liang.m8583.field.Password;
import cn.liang.m8583.field.basic.CardNumber;
import cn.liang.m8583.message.MessageType;
import cn.liang.m8583.message.Request;
import cn.liang.m8583.transcoder.Message8583;


/**
 * 余额请求报文
 * @author 325336, Liang Yabao
 * 2012-3-12
 * 
 */
public class BalanceRequest extends Request{

	public BalanceRequest() {
		super(new MessageType("0200"));

	}

	public CardNumber getCardNumber() {
		return cardNumber;
	}

	public Password getPassword() {
		return password;
	}

	private CardNumber cardNumber =new CardNumber();
	private Password password = new Password();

	
	
	
}
