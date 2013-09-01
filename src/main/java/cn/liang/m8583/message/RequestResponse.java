package cn.liang.m8583.message;


/**
 * @author 325336, Liang Yabao
 * 2012-3-12
 * Request类报文的响应报文，通常message type的第三位是1
 */
public abstract class RequestResponse extends OutputMessage{

	public RequestResponse(MessageType mt) {
		super(mt);
		// TODO Auto-generated constructor stub
	}


}
