package cn.liang.m8583.handler;

import cn.liang.m8583.message.InputMessage;
import cn.liang.m8583.message.OutputMessage;


/**
 * @author  Liang Yabao
 * 2012-3-12
 * 对应于Request类报文的处理器
 * @see Request
 */
public interface RequestHandler extends Handler {

	public String requestMessageType();

	public String responseMessageType();

	public Class<? extends InputMessage> requestClass();

	public Class<? extends OutputMessage> responseClass();

	public OutputMessage handle(InputMessage reqm) ;
}
