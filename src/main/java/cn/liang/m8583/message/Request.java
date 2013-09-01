package cn.liang.m8583.message;


/**
 * @author  Liang Yabao
 * 2012-3-12
 * 请求类报文，通常message type的第三位是0
 */
public abstract class Request extends InputMessage{

	public Request(MessageType mt) {
		super(mt);
	}


}
