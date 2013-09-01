package cn.liang.m8583.transcoder;

import cn.liang.m8583.message.Message;
import cn.liang.m8583.transcoder.exception.DecodeException;
import cn.liang.m8583.transcoder.exception.EncodeException;


/**
 * 8583报文转码器（引擎），包括编码与解码
 * 在这个设计体系里，8583报文存在三种形态：
 * 1、字节数组形态（传输形态）
 * 2、字段形态（中间形态），调用者不应该关心此形态。
 * 3、java域模型(domain)的形态
 * @author  Liang Yabao
 * 2012-3-09
 */
public interface Transcoder {
	
	/**
	 * 将字节数组形态的报文解码成java认识的报文。
	 * @param bytes
	 * @return
	 * @throws Exception
	 */
	public Message decode(byte[] bytes) throws DecodeException;
	
	/**
	 * 将报文编码成字节数组
	 * @param mes
	 * @return
	 * @throws Exception
	 */
	public byte[] encode(Message mes) throws EncodeException;
	
	/**
	 * 能接受的报文类型
	 * @param messageType	消息类型
	 * @param processingCode	处理码
	 * @param clazz	报文类
	 */
	public void accept(String messageType,String processingCode,Class<? extends Message> clazz);


}