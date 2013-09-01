package cn.liang.m8583.key;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

import cn.liang.m8583.key.DesEncryptor;
import cn.liang.m8583.key.Encryptor;
import cn.liang.m8583.transcoder.exception.CipherException;


/**
 * @author 325336, Liang Yabao
 * 2012-4-5
 */
public class PasswordTest {

	@Test
	public void test() throws UnsupportedEncodingException, CipherException {
		Encryptor enc= new DesEncryptor();
		String password = "987654";
		byte[] key= "123456781234567812345678".getBytes("GBK");
		byte[] bytes = enc.encryptPassword(key, password);
		String pwd = enc.decryptPassword(key, bytes);
		System.out.println(pwd);
		
		assert password.equals(pwd);
	}

}
