package cn.liang.m8583.key;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import org.junit.Test;

import cn.liang.m8583.key.CipherUtil;
import cn.liang.m8583.key.KeyFinder;
import cn.liang.m8583.key.KeyFinderDemo;
import cn.liang.m8583.transcoder.MessageUtil;
import cn.liang.m8583.transcoder.exception.CipherException;


/**
 * @author  Liang Yabao
 * 2012-4-5
 */
public class KeyTest {

	private static final String KEY_DES_EDE = "DESede";
	private static final String CIPHER_DES_EDE = "DESede/CBC/PKCS7Padding";
	private static final String PASSWORD_CRYPT_KEY = "012345678901234567891234";
	private static final String npwd= "123456789";
	
	@Test
	public void testDesede() throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException {
		SecureRandom sr = new SecureRandom();
		//Security.addProvider(provider)
		DESedeKeySpec dks = new DESedeKeySpec(PASSWORD_CRYPT_KEY.getBytes());
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_DES_EDE);
		SecretKey securekey = keyFactory.generateSecret(dks);
		//加密
		Cipher encryptor = Cipher.getInstance(KEY_DES_EDE);
		encryptor.init(Cipher.ENCRYPT_MODE, securekey, sr);
		//解密
		SecureRandom sr2 = new SecureRandom(npwd.getBytes());
		Cipher decryptor = Cipher.getInstance(KEY_DES_EDE);
		decryptor.init(Cipher.DECRYPT_MODE, securekey, sr2);
		
		System.out.println("参数："+encryptor.getParameters());
		System.out.println("块大小："+encryptor.getBlockSize());
		byte[] enc = encryptor.doFinal(npwd.getBytes());
		System.out.println("before enc: "+getMessageInHex(npwd.getBytes()));
		System.out.println("after enc : "+getMessageInHex(enc));
		byte[] dec = decryptor.doFinal(enc);
		//System.out.println("before dec: "+getMessageInHex(dec));
		System.out.println("after  dec: "+getMessageInHex(dec));
		String npwd2 = new String(dec);
		System.out.println(npwd2);
		assert npwd2.equals(npwd);
	}
	
	public String getMessageInHex(byte[] bytes) {
		char[] chars = MessageUtil.byte2hex(bytes);
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < chars.length; i++) {
			if (i % 12 == 0) {
				builder.append(System.getProperty("line.separator"));
			} else if (i % 4 == 0) {
				builder.append(' ');
			}
			builder.append(chars[i]);
		}
		return builder.toString();
	}
	
	//@Test
	public void testCipherUtil() throws CipherException{
		KeyFinder finder = new KeyFinderDemo();
		CipherUtil util = new CipherUtil(finder);
		util.setMasterKey(finder.findKey(null));
		String pwd = "12345678";
		System.out.println("password:"+pwd);
		byte[] bytes = util.encryptPassword("123456789012", pwd);
		
		byte[] b2 = new byte[8];
		System.arraycopy(bytes, 0, b2, 0, 8);
		String dpwd = util.decryptPassword("123456789012", b2);
		System.out.println("decr pwd:"+dpwd);
		assert pwd.equals(dpwd):"testCipherUtil";
		
	}

}
