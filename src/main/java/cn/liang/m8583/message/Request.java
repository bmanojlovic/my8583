package cn.liang.m8583.message;


/**
 * @author 325336, Liang Yabao
 * 2012-3-12
 * 请求类报文，通常message type的第三位是0
 */
public abstract class Request extends InputMessage{

	public Request(MessageType mt) {
		super(mt);
		// TODO Auto-generated constructor stub
	}


}
