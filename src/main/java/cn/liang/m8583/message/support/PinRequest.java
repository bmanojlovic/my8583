package cn.liang.m8583.message.support;

import java.util.Arrays;

import cn.liang.m8583.field.basic.CardNumber;
import cn.liang.m8583.message.MessageType;
import cn.liang.m8583.message.Request;
import cn.liang.m8583.transcoder.Message8583;



/**
 * 修改个人密码请求
 * @author 325336, Liang Yabao
 * 2012-3-22
 */
public class PinRequest extends Request{
	public PinRequest(MessageType mt) {
		super(mt);
		// TODO Auto-generated constructor stub
	}

	private CardNumber cardNumber;
	private byte[] pin;
	private byte[] newPin;

	public byte[] getNewPin() {
		return newPin;
	}

	public void setNewPin(byte[] newPin) {
		this.newPin = newPin;
	}

	public CardNumber getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(CardNumber cardNumber) {
		this.cardNumber = cardNumber;
	}

	public byte[] getPin() {
		return pin;
	}

	public void setPin(byte[] pin) {
		this.pin = pin;
	}

	@Override
	public void decode(Message8583 mes) {
		super.decode(mes);
		
		
	}

	@Override
	public Message8583 encode() {
		Message8583 mes = super.encode();
		
		return mes;
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof PinRequest)) {
			return false;
		}
		PinRequest pr = (PinRequest) obj;
		return pr.getCardNumber().equals(this.getCardNumber()) 
				&& Arrays.equals(pr.getPin(), this.pin)
				&& Arrays.equals(pr.getNewPin(), this.newPin);
	}

}
