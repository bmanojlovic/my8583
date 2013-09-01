package cn.liang.m8583.transcoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.liang.m8583.key.KeyFinder;
import cn.liang.m8583.message.Message;
import cn.liang.m8583.message.support.WorkingKeyRequest;
import cn.liang.m8583.message.support.WorkingKeyResponse;
import cn.liang.m8583.transcoder.exception.DecodeException;
import cn.liang.m8583.transcoder.exception.EncodeException;


/**
 * 8583报文（引擎）转码器的实现类
 * @author 325336, Liang Yabao
 * 2012-3-12
 */
public class TranscoderImpl implements Transcoder {
	
	private static final Logger logger = LoggerFactory.getLogger(TranscoderImpl.class);

	public Message decode(byte[] bytes) throws DecodeException {
		try {
			byte[] data = MessageUtil.removeMessageHead(bytes);
			
			//切分原始报文
			Message8583 m8583 = new Message8583();
			m8583.decode(data);
			
			//转化成java认识的报文对象
			Class<? extends Message> clazz = Message.getMessageClass(
					m8583.getMessageType(), m8583.getProcessCode());
			Message mes = clazz.newInstance();
			mes.decode(m8583);
			
			return mes;
		} catch (Exception e) {
			throw new DecodeException(bytes,e);
		}
	}
	
	public Message decode(byte[] bytes,MessageAuthenticationTool mat,KeyFinder finder) throws DecodeException {
		
		Message mes = this.decode(bytes);
		
		byte[] data = MessageUtil.removeMessageHead(bytes);
		try {
			//找到计算mac的KEY
			byte[] key = null;
			if (mes instanceof WorkingKeyRequest
					|| mes instanceof WorkingKeyResponse) {
				// 请求同步密钥时，使用主密钥计算mac
				key = finder.findMasterKey();

			} else {
				key = finder.findKey(mes.getTerminalId().getAscii());
			}
			
			boolean isAuthentic = mat.authenticate(data, key);
						
			mes.authentic(isAuthentic);
			return mes;
		} catch (Exception e) {
			//因为报文已经被解码成对象形式，在这个过程 中如果发生了未知异常，可以从异常中获得报文对象。
			//如果要知道具体原因，可以从DecodeException中getCause()进行处理。
			throw new DecodeException(bytes, mes, e);
		}
	}
	
	public byte[] encode(Message mes) throws EncodeException {
		try {
			//转换成字段形态
			Message8583 m8583 = mes.encode();
			m8583.setMessageType(mes.getMessageType());
			m8583.setProcessCode(mes.getProcessCode());
			//转换成字节数组形态
			byte[] data =  m8583.encode();
			
			//添加报文头
			byte[] bytes = MessageUtil.addMessageHead(data);
			return bytes;
		} catch (Exception e) {
			throw new EncodeException(mes, e);
		}
	}
	
	public byte[] encode(Message mes,MessageAuthenticationTool mat,KeyFinder finder) throws EncodeException{
		
		byte[] data = this.encode(mes);
		
		//找到计算mac的KEY
		byte[] key;
		if (mes instanceof WorkingKeyRequest
				|| mes instanceof WorkingKeyResponse) {
			// 在同步密钥报文里，通过主密钥计算mac。
			key = finder.findMasterKey();

		} else {
			key = finder.findKey(mes.getTerminalId().getAscii());
		}
		
		MessageUtil.removeMessageHead(data);
		data = mat.sign(data, key);
		return MessageUtil.addMessageHead(data);
	}


	public void accept(String messageType, String processCode,
			Class<? extends Message> clazz) {
		Message.registMessage(messageType, processCode, clazz);
	}

}
