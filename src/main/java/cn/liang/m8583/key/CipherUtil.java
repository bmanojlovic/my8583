package cn.liang.m8583.key;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import cn.liang.m8583.transcoder.Message8583;
import cn.liang.m8583.transcoder.exception.CipherException;


/**
 * 加密工具，依赖于JDK中的标准实现。
 * 因为使用CBC连接方式，PKCS填充方式，不适合于8583报文。
 * 在8583报文中不建议使用。
 * @deprecated
 * @author 325336, Liang Yabao
 * 2012-3-13
 */
public  class CipherUtil {

	private KeyFinder finder;
	private static final String algorithm ="DESede";
	//private static final String algorithm ="DESede/CBC/Zeros";
	//private static final String algorithm ="DESede/CBC/PKCS5Padding";
	//private static final String algorithm ="DESede/ECB/NoPadding";
	//private static final String algorithm ="DESede/CBC/PKCS5";
	//private static final String algorithm ="DESede/ECB/PKCS7";
	
	
	private SecretKey masterKey = null;
	Cipher workingKeyDecryptor;
	Cipher workingKeyEncryptor;

	/**
	 * 设置主密钥 algorithm ="DESede"
	 * @param masterKey 
	 * @throws CipherException 
	 */
	public void setMasterKey(SecretKey masterKey) throws CipherException {
		this.masterKey = masterKey;
		try {
			workingKeyDecryptor = Cipher.getInstance(this.algorithm);
			workingKeyDecryptor.init(Cipher.DECRYPT_MODE, masterKey);
			workingKeyEncryptor = Cipher.getInstance(this.algorithm);
			workingKeyEncryptor.init(Cipher.ENCRYPT_MODE, masterKey);
		} catch (NoSuchAlgorithmException e) {
			throw new CipherException(e);
		} catch (NoSuchPaddingException e) {
			throw new CipherException(e);
		} catch (InvalidKeyException e) {
			throw new CipherException(e);
		}
	}
	
	/**
	 * 设置主密钥
	 * @param bytes	24 Byte for triple DES algorithan
	 * @throws CipherException 
	 */
	public void setMasterKey(byte[] bytes) throws CipherException{
		try {
			this.masterKey = this.generateKey(bytes);
			this.setMasterKey(masterKey);
		} catch (InvalidKeyException e) {
			throw new CipherException(e);
		} catch (InvalidKeySpecException e) {
			throw new CipherException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new CipherException(e);
		}
	}
	
	public SecretKey getMasterKey() {
		return masterKey;
	}

	/**
	 * 加密工作依赖于工作密钥查找器，每一个巴枪对应于一个工作密钥
	 * @param finder	工作密钥查找器
	 */
	public CipherUtil(KeyFinder finder){
		this.finder = finder;
	}
	
	/**
	 * 根据字节数组生成密钥对象
	 * @param bytes	密钥
	 * @return	密钥对象
	 * @throws InvalidKeyException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchAlgorithmException	没有对应的加密算法
	 */
	public SecretKey generateKey(byte[] bytes) throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException{
		SecretKeyFactory keyFactory = SecretKeyFactory
				.getInstance(this.algorithm);
		SecretKey securekey = keyFactory.generateSecret(new DESedeKeySpec(bytes));
		return securekey;
	}
	
	protected Cipher decryptor(String  terminalID) throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException{
		byte[] keyBytes = finder.findKey(terminalID);
		Key securekey = this.generateKey(keyBytes);
		Cipher decryptor = Cipher.getInstance(this.algorithm);
		decryptor.init(Cipher.DECRYPT_MODE, securekey);
		System.out.println("算法："+decryptor.getAlgorithm());
		return decryptor;
	}
	protected Cipher encryptor(String  terminalID) throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException{
		byte[] keyBytes = finder.findKey(terminalID);
		Key securekey = this.generateKey(keyBytes);
		Cipher encryptor = Cipher.getInstance(this.algorithm);
		encryptor.init(Cipher.ENCRYPT_MODE, securekey);
		return encryptor;
	}
	
	/**
	 * 解密mac
	 * @param terminalID
	 * @param data
	 * @return
	 * @throws CipherException
	 */
	public byte[] decrypt(String terminalID,byte[] data) throws CipherException {
		try {
			Cipher decryptor = this.decryptor(terminalID);
			return decryptor.doFinal(data);
	
		} catch (NoSuchAlgorithmException e) {
			throw new CipherException(e);
		} catch (InvalidKeySpecException e) {
			throw new CipherException(e);
		} catch (NoSuchPaddingException e) {
			throw new CipherException(e);
		} catch (InvalidKeyException e) {
			throw new CipherException(e);
		} catch (IllegalBlockSizeException e) {
			throw new CipherException(e);
		} catch (BadPaddingException e) {
			throw new CipherException(e);
		}
	}

	/**
	 * 加密mac
	 * @param terminalID
	 * @param data
	 * @return
	 * @throws CipherException
	 */
	public byte[] encrypt(String terminalID,byte[] data) throws CipherException {
		try {
			Cipher encryptor = this.encryptor(terminalID);
			return encryptor.doFinal(data);
	
		} catch (NoSuchAlgorithmException e) {
			throw new CipherException(e);
		} catch (InvalidKeySpecException e) {
			throw new CipherException(e);
		} catch (NoSuchPaddingException e) {
			throw new CipherException(e);
		} catch (InvalidKeyException e) {
			throw new CipherException(e);
		} catch (IllegalBlockSizeException e) {
			throw new CipherException(e);
		} catch (BadPaddingException e) {
			throw new CipherException(e);
		}
	}
	
	/**
	 * 解密个人密码
	 * @param terminalID	终端标识
	 * @param pinData	个人密码密文
	 * @return	个人密码明文
	 * @throws CipherException
	 */
	public String decryptPassword(String terminalID, byte[] pinData) throws CipherException{
		byte[] pin = this.decrypt(terminalID, pinData);
		try {
			return new String(pin, Message8583.CHARSET.name());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 加密个人密码
	 * @param terminalID	终端标识
	 * @param password	个人密码明文
	 * @return	个人密码密文
	 * @throws CipherException
	 */
	public byte[] encryptPassword(String terminalID,String password) throws CipherException{
		byte[] pin = null;
		try {
			pin = this.encrypt(terminalID, password.getBytes( Message8583.CHARSET.name()));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pin;
	}
	
	/**
	 * 解密工作密钥
	 * @param key	工作密钥密文
	 * @return	工作密钥明文
	 * @throws CipherException
	 */
	public byte[] decryptWorkingKey(byte[] key) throws CipherException{
		
		try {
			return workingKeyDecryptor.doFinal(key);
		} catch (IllegalBlockSizeException e) {
			throw new CipherException(e);
		} catch (BadPaddingException e) {
			throw new CipherException(e);
		}
	}
	
	/**
	 * 加密工作密钥
	 * @param key	工作密钥明文
	 * @return	工作密钥密文
	 * @throws CipherException
	 */
	public byte[] encryptWorkingKey(byte[] key) throws CipherException{
		
		try {
			return workingKeyEncryptor.doFinal(key);
		} catch (IllegalBlockSizeException e) {
			throw new CipherException(e);
		} catch (BadPaddingException e) {
			throw new CipherException(e);
		}
	}
}
