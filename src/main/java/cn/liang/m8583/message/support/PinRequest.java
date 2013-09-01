package cn.liang.m8583.message.support;

import cn.liang.m8583.field.Password;
import cn.liang.m8583.field.basic.CardNumber;
import cn.liang.m8583.message.MessageType;
import cn.liang.m8583.message.Request;



/**
 * 修改个人密码请求
 * @author  Liang Yabao
 * 2012-3-22
 */
public class PinRequest extends Request{
	public PinRequest(MessageType mt) {
		super(mt);
	}

	private Password oldPassword;
	private Password newPassword;
	
	private CardNumber cardNumber;
	
	public Password getOldPassword() {
		return oldPassword;
	}

	public Password getNewPassword() {
		return newPassword;
	}


	
	public CardNumber getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(CardNumber cardNumber) {
		this.cardNumber = cardNumber;
	}

}
