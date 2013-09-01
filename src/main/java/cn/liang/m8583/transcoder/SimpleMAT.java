package cn.liang.m8583.transcoder;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.liang.m8583.key.Encryptor;


public class SimpleMAT implements MessageAuthenticationTool{

	private static final Logger logger = LoggerFactory.getLogger(SimpleMAT.class);
	private Encryptor encryptor;
	

	public SimpleMAT(Encryptor encryptor){
		this.encryptor = encryptor;
	}

	public Encryptor getEncryptor() {
		return encryptor;
	}
	public void setEncryptor(Encryptor encryptor) {
		this.encryptor = encryptor;
	}
	
	@Override
	public boolean authenticate(byte[] mes, byte[] mak) {
		if(!existMAC(mes)){
			throw new RuntimeException("the message have no mac!");
		}
		// get MAC
		byte[] oMac = new byte[8];
		System.arraycopy(mes, mes.length - 8, oMac, 0, oMac.length);
		// compute mac
		byte[] rMac = encryptor.mac(mak, mes, mes.length - 8);
		
		if (logger.isDebugEnabled()) {
			logger.debug("decode mac:{}"
					+ new String(MessageUtil.byte2hex(rMac)));
			logger.debug("origin mac:{}"
					+ new String(MessageUtil.byte2hex(oMac)));
		}
		
		// 验证报文有效性
		// compare mac a and mac b
		return Arrays.equals(oMac, rMac);
	}

	/**
	 * 方法说明：<br>
	 * 在对象报文编码成字节报文后，进行签名
	 * 加密机不应该了解8583结构，所以此方法不应该在加密机中。
	 * @param data	8583报文，无报文头，从MessageType开始到63字段。
	 * @param key
	 * @return
	 */
	@Override
	public byte[] sign(byte[] data, byte[] mak) {
		if(logger.isDebugEnabled()){
			logger.debug("before sign:{}",new String(MessageUtil.byte2hex(data)));
		}
		//1.check bytes is  8583 message
		if(existMAC(data)){
			throw new RuntimeException("the message bytes have signed!");
		}
		//2.modify bitmap 置64字段为存在。
		data[9] = (byte) (data[9] | 0x01);
		//3.计算mac
		byte[] mac = encryptor.mac(mak, data, data.length );
		if (logger.isDebugEnabled()) {
			logger.debug("encode mac:{}"
					+ new String(MessageUtil.byte2hex(mac)));
		}
		//4.add mac;
		byte[] newdata = MessageUtil.concat(data,mac);
		
		if(logger.isDebugEnabled()){
			logger.debug("after sign:{}",new String(MessageUtil.byte2hex(newdata)));
		}

		return newdata;
	}

	protected boolean existMAC(byte[] data) {
		return (data[9] & 0x01)==1;
	}

}
