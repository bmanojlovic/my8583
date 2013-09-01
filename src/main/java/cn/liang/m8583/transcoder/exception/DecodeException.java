package cn.liang.m8583.transcoder.exception;

import cn.liang.m8583.message.Message;
import cn.liang.m8583.transcoder.MessageUtil;


/**
 * 解码异常
 * 在解码8583报文时发生。发生此异常，通常可以抛弃报文，不予处理
 * @author 325336, Liang Yabao
 * 2012-3-16
 */
public class DecodeException extends Exception {
	private byte[] bytes;
	private Message message;

	public Message getMessageObject() {
		return message;
	}

	public DecodeException(byte[] bytes, Throwable cause) {
		super(cause);
		this.bytes = bytes;
	}
	
	public DecodeException(byte[] bytes,Message message, Throwable cause) {
		super(cause);
		this.bytes = bytes;
		this.message = message;
	}

	/**
	 * 转换成16进制数字字符串形式报文
	 * @return
	 */
	public String getMessageInHex() {
		char[] chars = MessageUtil.byte2hex(bytes);
		return MessageUtil.multiLine(chars);
	}
	
	/**
	 * 方法说明：<br>
	 * 获得报文的字节形式
	 *
	 * @return
	 */
	public byte[] getMessageBytes(){
		return bytes;
	}
	
}
