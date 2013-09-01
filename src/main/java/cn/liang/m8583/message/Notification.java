package cn.liang.m8583.message;


/**
 * @author 325336, Liang Yabao
 * 2012-3-12
 * 此类报文，可以没有响应，通常message type 的第三位是4
 */
public abstract class Notification extends InputMessage{

	public Notification(MessageType mt) {
		super(mt);
		// TODO Auto-generated constructor stub
	}

}
