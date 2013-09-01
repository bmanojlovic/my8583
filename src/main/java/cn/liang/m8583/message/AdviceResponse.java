package cn.liang.m8583.message;

/**
 * @author 325336, Liang Yabao
 * 2012-3-12
 * Advice类报文的响应报文，通常message type的第三位是3
 */
public abstract class AdviceResponse extends OutputMessage{

	public AdviceResponse(MessageType mt) {
		super(mt);
		// TODO Auto-generated constructor stub
	}

}
