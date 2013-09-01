package cn.liang.m8583.transcoder.exception;

import cn.liang.m8583.message.Message;

/**
 * 编码异常
 * 在编码8583报文时发生，通常是参数设置有误。
 * @author 325336, Liang Yabao
 * 2012-3-16
 */
public class EncodeException extends Exception{

	private Message mes;

	public EncodeException(Message mes, Throwable cause) {
		super(cause);
		this.mes = mes;
	}
	
	/**
	 * 以json字符串形式返回报文
	 * @return
	 */
	public String getMessageInJson(){
		//return JSONUtils.fromObject(mes);
		return mes.toString();
	}
	
	/**
	 * 方法说明：<br>
	 * 获得报文的对象形式。
	 *
	 * @return
	 */
	public Message getMessageObject(){
		return mes;
	}
}
