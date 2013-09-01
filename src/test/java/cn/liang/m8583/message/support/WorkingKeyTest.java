package cn.liang.m8583.message.support;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import cn.liang.m8583.field.basic.Argument;
import cn.liang.m8583.key.DesEncryptor;
import cn.liang.m8583.key.Encryptor;
import cn.liang.m8583.key.KeyFinder;
import cn.liang.m8583.key.KeyFinderDemo;
import cn.liang.m8583.message.support.WorkingKeyRequest;
import cn.liang.m8583.message.support.WorkingKeyResponse;
import cn.liang.m8583.transcoder.DefaultTranscoder;
import cn.liang.m8583.transcoder.Transcoder;
import cn.liang.m8583.transcoder.exception.CipherException;
import cn.liang.m8583.transcoder.exception.DecodeException;
import cn.liang.m8583.transcoder.exception.EncodeException;

import com.sfpay.framework.common.json.JSONUtils;

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
 * @author 325336 <a href="mailto:liangyabao@sf-express.com">梁亚保</a>
 * 
 * CreateDate: 2012-5-21
 */
public class WorkingKeyTest {


	private KeyFinder kf;
	private Encryptor enc;
	private Transcoder coder;
	
	@Before
	public void  initCoder(){
		kf = new KeyFinderDemo();
		enc = new DesEncryptor();
		coder = new DefaultTranscoder(kf,enc);
	}

	@Test
	public void testWorkingKeyRequest() throws CipherException, EncodeException, DecodeException{
		WorkingKeyRequest wkr = new WorkingKeyRequest();

		wkr.setNodeId("12345678");
		wkr.setOperator("999999");
		wkr.setTerminalId("125304493142");
		wkr.setTtc("789456");
		
		
		byte[] bytes = coder.encode(wkr);
		WorkingKeyRequest wkr2 = (WorkingKeyRequest) coder.decode(bytes);
		
		System.out.println(JSONUtils.fromObject(wkr));
		System.out.println(JSONUtils.fromObject(wkr2));

		assert wkr.equals(wkr2);


	}
	@Test
	public void testWorkingKeyResponse() throws UnsupportedEncodingException, DecodeException, EncodeException, CipherException{
		
		WorkingKeyResponse wkr = new WorkingKeyResponse();
		
		wkr.setArgument(new Argument(10000,10000,100000,3600));
		
		wkr.setDateTime(new Date());
		wkr.setNewWorkingKey(enc.encryptWorkingKey(kf.findKey(null), "123456781234567887654321".getBytes("GBK")));
		wkr.setNodeId("12345678");
		wkr.setResponseCode("00");
		wkr.setTerminalId("123456789012");
		wkr.setTtc("654321");
		
		
		WorkingKeyResponse wkr2 = (WorkingKeyResponse) coder.decode(coder.encode(wkr));
		
		assert wkr2.equals(wkr);
		System.out.println(new String(enc.decryptWorkingKey(kf.findKey(null), wkr2.getNewWorkingKey()),"GBK"));
		
	}

}
