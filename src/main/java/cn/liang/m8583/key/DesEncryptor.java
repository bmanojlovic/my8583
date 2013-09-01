package cn.liang.m8583.key;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.liang.m8583.transcoder.MessageUtil;
import cn.liang.m8583.transcoder.exception.CipherException;



/**
 * DES算法的加密机实现，可以加密与解密
 * @author  Liang Yabao
 * 2012-3-31
 */
public class DesEncryptor implements Encryptor {

	private static final int WORKING_KEY_SIZE = 24;
	private static final int PIN_DATA_SIZE = 8;
	private static final String CHECK_VALUE_INIT = "0000000000000000";

	private static final Logger logger = LoggerFactory.getLogger(DesEncryptor.class);
	
	public String decryptPassword(byte[] key, byte[] pinData)
			throws CipherException {
		if (pinData.length != PIN_DATA_SIZE) {
			throw new CipherException("the PIN_DATA_SIZE should be "+PIN_DATA_SIZE);
		}
		if (key.length != WORKING_KEY_SIZE) {
			throw new CipherException("the key's size should be "+WORKING_KEY_SIZE);
		}
		String k = new String(MessageUtil.byte2hex(key),0,32);
		String pin = new String(MessageUtil.byte2hex(pinData));
		
		logger.debug("use key:{}", k);
		pin = Des.des3(pin, k, Des.Mode.DECRYPTION);
		logger.debug("after triple des decryption pin :{}", pin);
		//i指向最后一个字符
		int i = pin.length()-1;
		//直到i指向最后一个数字，而不是其它字符
		while(pin.charAt(i)>'9'){
			i--;
		}
		String pwd = new String(pin.toCharArray(),0,i+1);
		logger.debug("password :{}", pwd);
		return Des.HEX_2_ASC(pwd);
	}
	
	
	public byte[] encryptPassword(byte[] key,String password) throws CipherException{
		if (password.length() >8) {
			throw new CipherException("the password should be less than 8!");
		}
		password = Des.ASC_2_HEX(password);
		while(password.length()<16){
			password+="F";
		}
		if (key.length != WORKING_KEY_SIZE) {
			throw new CipherException("the key's size should be "+WORKING_KEY_SIZE);
		}
		String k = new String(MessageUtil.byte2hex(key),0,32);
		
		String pin = Des.des3(password, k, Des.Mode.ENCRYPTION);
		
		
		return MessageUtil.hex2byte(pin.toLowerCase().toCharArray());
	}
	
	
	public byte[] decryptWorkingKey(byte[] key,byte[] workingKey) throws CipherException{
		if(workingKey.length < WORKING_KEY_SIZE ||key .length!= WORKING_KEY_SIZE){
			throw new CipherException("the key's size should be 24 bytes!");
		}
		try {
			String k = new String(MessageUtil.byte2hex(key),0,32);
			char[] chars = MessageUtil.byte2hex(workingKey);
			String wk1= new String(chars,0,16);
			String wk2= new String(chars,16,16);
			String wk3= new String(chars,32,16);
			String wk;
			wk = Des.des3(wk1, k, Des.Mode.DECRYPTION);
			wk += Des.des3(wk2, k, Des.Mode.DECRYPTION);
			wk += Des.des3(wk3, k, Des.Mode.DECRYPTION);
			byte[] wkbs= MessageUtil.hex2byte(wk.toCharArray());
			if (logger.isDebugEnabled()) {
				logger.debug("after decrypt working key:{}", new String(
						MessageUtil.byte2hex(wkbs)));
				logger.debug("check value:"+new String(MessageUtil.byte2hex(this.checkvalue(wkbs))));
			}
			 return wkbs;
		} catch (Exception e) {
			throw new CipherException(e);
		} 
	}
	
	
	public byte[] encryptWorkingKey(byte[] key,byte[] workingKey) throws CipherException{
		
		if(workingKey.length!= WORKING_KEY_SIZE ||key .length!= WORKING_KEY_SIZE){
			throw new CipherException("the key's size should be 24 bytes!");
		}
		try {
			String k = new String(MessageUtil.byte2hex(key),0,32);
			char[] chars = MessageUtil.byte2hex(workingKey);
			String wk1= new String(chars,0,16);
			String wk2= new String(chars,16,16);
			String wk3= new String(chars,32,16);
			String wk;
			wk = Des.des3(wk1, k, Des.Mode.ENCRYPTION);
			wk += Des.des3(wk2, k, Des.Mode.ENCRYPTION);
			wk += Des.des3(wk3, k, Des.Mode.ENCRYPTION);
			byte[] wkbs= MessageUtil.hex2byte(wk.toCharArray());
			
			byte[] checkvalue = this.checkvalue(workingKey);
			
			byte[] bytes = new byte[56];
			System.arraycopy(wkbs, 0, bytes, 0, WORKING_KEY_SIZE);
			System.arraycopy(checkvalue, 0, bytes, WORKING_KEY_SIZE, 4);
			System.arraycopy(wkbs, 0, bytes, WORKING_KEY_SIZE+4, WORKING_KEY_SIZE);
			System.arraycopy(checkvalue, 0, bytes, WORKING_KEY_SIZE*2+4, 4);
			 
			return bytes;
		} catch (Exception e) {
			throw new CipherException(e);
		} 
	}

	/**
	 * 工作密钥的检查值
	 * @param workingKey
	 * @return
	 */
	public byte[] checkvalue(byte[] workingKey) {
		
		String k = new String(MessageUtil.byte2hex(workingKey), 0, 32);
		String cv = Des.des3(CHECK_VALUE_INIT, k, Des.Mode.ENCRYPTION);
		logger.debug("check value:{}",cv);
		byte[] cvbs = MessageUtil.hex2byte(cv.toCharArray());
		byte[] bytes = new byte[4];
		System.arraycopy(cvbs, 0, bytes, 0, 4);
		return bytes;
	}
	
	public byte[] encrypt(byte[] key, byte[] data) {
		String k = new String(MessageUtil.byte2hex(key),0,16);
		String d = new String(MessageUtil.byte2hex(data));
		logger.debug("des encrypt in : {}", d);
		d = Des.des(d, k, Des.Mode.ENCRYPTION);
		logger.debug("des encrypt out: {}", d);
		return MessageUtil.hex2byte(d.toLowerCase().toCharArray());
	}

	public byte[] decrypt(byte[] key, byte[] data) {
		String k = new String(MessageUtil.byte2hex(key),0,16);
		String d = new String(MessageUtil.byte2hex(data));
		d = Des.des(d, k, Des.Mode.DECRYPTION);
		return MessageUtil.hex2byte(d.toLowerCase().toCharArray());
	}
	
	/**
	 * 计算报文认证码
	 * 报文认证算法，不一定是8583所独有；
	 * 所以它不应该放在转码器中，而是应该存在于加密机中
	 * @param key	密钥，在这里用到的是单DES，所key至少应该有8字节，即64位
	 * @param mes	需计算认证码的消息体。
	 * @param len	需计算认证码的消息体的长度
	 * @return	报文认证码
	 */
	public byte[] mac(byte[] key, byte[] mes, int len) {
		byte[] sOutMAC = new byte[8];
		int offset = 0;
		while (len > offset) {
			if ((len - offset) <= 8) {
				for (int i = 0; i < len - offset; i++) {
					sOutMAC[i] ^= mes[offset + i];
				}
				sOutMAC = encrypt(key, sOutMAC);
				break;
			}
			for (int i = 0; i < 8; i++) {
				sOutMAC[i] ^= mes[offset + i];
			}
			sOutMAC = encrypt(key, sOutMAC);
			offset += 8;
		}

		sOutMAC = encrypt(key, sOutMAC);// 8位密钥使用des
		return sOutMAC;
	}
	
}
