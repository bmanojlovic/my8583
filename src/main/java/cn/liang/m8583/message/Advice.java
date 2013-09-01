package cn.liang.m8583.message;


/**
 * @author  Liang Yabao
 * 2012-3-12
 * Advice类报文，此类报文是已成事实的报文通知，只要求响应，不要求批准。
 * 通常可以先响应后处理，在message type 中第三位一般是2
 */
public abstract class Advice extends InputMessage{

	public Advice(MessageType mt) {
		super(mt);
		// TODO Auto-generated constructor stub
	}

}
