package cn.liang.m8583.handler;

import cn.liang.m8583.message.InputMessage;
import cn.liang.m8583.message.OutputMessage;

/**
 * @author  Liang Yabao
 * 2012-3-12
 * 没有对应的处理器，但要求解码器解码时用此类。
 */
public class NoHandler implements RequestHandler{

	String messageType;
	String processingCode;
	Class<? extends InputMessage> clazz;

	public NoHandler(String messageType, String processingCode,
			Class<? extends InputMessage> clazz) {
		this.messageType = messageType;
		this.processingCode = processingCode;
		this.clazz = clazz;
	}
	
	public String processingCode() {
		
		return processingCode;
	}

	public String requestMessageType() {
		return messageType;
	}

	public String responseMessageType() {
		
		return null;
	}

	public Class<? extends InputMessage> requestClass() {
		return clazz;
	}

	public Class<? extends OutputMessage> responseClass() {
		
		return null;
	}

	public OutputMessage handle(InputMessage reqm) {
		throw new RuntimeException("No handler for this type request: "+ reqm.getClass());
	}

}
