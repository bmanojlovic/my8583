package cn.liang.m8583.key;

import cn.liang.m8583.transcoder.exception.CipherException;

/**
 * 加密机
 * 有加密与解密的功能
 * 主要用于mac，个人密码加密，工作密钥加密
 * 
 * 硬件加密机，对传输的数据进行加密的外围硬件设备，用于PIN的加密和解密、验证报文和文件来源的正确性以及存储密钥。
 * 本系统中使用软加密机，功能与硬件加密机相当。
 * 
 * @author 325336, Liang Yabao
 * 2012-3-31
 */
public interface Encryptor {
	
	/**
	 * 单DES加密一个数据块
	 * @param key	密钥
	 * @param data	一个数据块，通常是8个字节。
	 * @return	加密后的一个数据块，无填充
	 */
	public byte[] encrypt(byte[] key, byte[] data) ;
	
	/**
	 * 单DES解密一个数据块
	 * @param key	密钥
	 * @param data	一个数据块，通常是8个字节
	 * @return	解密后的一个数据块
	 */
	public byte[] decrypt(byte[] key, byte[] data) ;
	
	/**
	 * 解密个人密码
	 * @param key	工作密钥
	 * @param pinData	个人密码密文
	 * @return	个人密码明文
	 * @throws CipherException
	 */
	public String decryptPassword(byte[] key, byte[] pinData) throws CipherException;
	
	/**
	 * 加密个人密码
	 * @param key	工作密钥
	 * @param password	个人密码明文
	 * @return	个人密码密文
	 * @throws CipherException
	 */
	public byte[] encryptPassword(byte[] key,String password) throws CipherException;
	
	/**
	 * 解密工作密钥
	 * @param key	主密钥，即密钥加密密钥，用于加密工作密钥的密钥
	 * @param workingKey	工作密钥密文
	 * @return	工作密钥明文
	 * @throws CipherException
	 */
	public byte[] decryptWorkingKey(byte[] key,byte[] workingKey) throws CipherException;
	
	/**
	 * 加密工作密钥
	 * @param key	主密钥，即密钥加密密钥，用于加密工作密钥的密钥
	 * @param workingKey	工作密钥明文
	 * @return	工作密钥密文
	 * @throws CipherException
	 */
	public byte[] encryptWorkingKey(byte[] key,byte[] workingKey) throws CipherException;

	/**
	 * 计算报文认证码
	 * 报文认证算法，不一定是8583所独有；
	 * 所以它不应该放在转码器中，而是应该存在于加密机中
	 * @param key	密钥，在这里用到的是单DES，所key至少应该有8字节，即64位
	 * @param mes	需计算认证码的消息体。
	 * @param len	需计算认证码的消息体的长度
	 * @return	报文认证码
	 */
	public byte[] mac(byte[] key, byte[] mes, int len);
}