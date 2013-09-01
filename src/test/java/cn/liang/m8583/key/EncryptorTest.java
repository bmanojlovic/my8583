package cn.liang.m8583.key;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;

import org.junit.Test;

import cn.liang.m8583.field.basic.Argument;
import cn.liang.m8583.key.DesEncryptor;
import cn.liang.m8583.key.Encryptor;
import cn.liang.m8583.message.support.WorkingKeyResponse;
import cn.liang.m8583.transcoder.DefaultTranscoder;
import cn.liang.m8583.transcoder.MessageUtil;
import cn.liang.m8583.transcoder.Transcoder;
import cn.liang.m8583.transcoder.exception.CipherException;
import cn.liang.m8583.transcoder.exception.DecodeException;
import cn.liang.m8583.transcoder.exception.EncodeException;


/**
 * 
 * 类说明：<br>
 * 
 * 
 * <p>
 * 详细描述：<br>
 * 
 * 
 * </p>
 * 
 * @author  <a href="mailto:liangyabao@sf-express.com">梁亚保</a>
 * 
 * CreateDate: 2012-5-21
 */
public class EncryptorTest {

	//@Test
	public void testWorkingKey() {
		try {
			byte[] key="123456781234567812345678".getBytes("GBK");
			Encryptor enc = new DesEncryptor();
			byte[] bytes = enc.encryptWorkingKey(key, key);
			byte[] key1 = enc.decryptWorkingKey(key, bytes);
			System.out.println(MessageUtil.byte2hex(bytes));
			assert Arrays.equals(key, key1):"WorkingKeyTest test wrong!";
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CipherException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
