package cn.liang.m8583.transcoder.exception;

/**
 * 加密机异常
 * 此异常是严重异常，通常情况下不应发生，发生时应及时处理。
 * @author  Liang Yabao
 * 2012-3-22
 */
public class CipherException extends Exception{
	
	public CipherException(Throwable cause) {
		super(cause);
	}
	public CipherException(String mes){
		super(mes);
	}
}
