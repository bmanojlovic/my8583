package cn.liang.m8583.message.support;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import cn.liang.m8583.key.DesEncryptor;
import cn.liang.m8583.key.Encryptor;
import cn.liang.m8583.key.KeyFinder;
import cn.liang.m8583.key.KeyFinderDemo;
import cn.liang.m8583.message.support.PinRequest;
import cn.liang.m8583.message.support.PinResponse;
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
public class PinTest {

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
	public void testRequest() throws CipherException, EncodeException, DecodeException {
		PinRequest pr = new PinRequest();
		
		pr.setCard("125304493142");
		pr.setNodeId("12345678");
		pr.setOperator("999999");
		pr.setPin(enc.encryptPassword(kf.findKey(null), "63524178"));
		pr.setNewPin(enc.encryptPassword(kf.findKey(null), "96385271"));
		pr.setTerminalId("125304493142");
		pr.setTtc("789456");
		
		
		byte[] bytes = coder.encode(pr);
		PinRequest pr2 = (PinRequest) coder.decode(bytes);
		
		System.out.println(JSONUtils.fromObject(pr));
		System.out.println(JSONUtils.fromObject(pr2));
		System.out.println("password:"+ enc.decryptPassword(kf.findKey(null), pr.getPin()));
		assert pr.equals(pr2);

	}
	
	@Test
	public void testResponse() throws EncodeException, DecodeException {
		PinResponse pr = new PinResponse();
		pr.setDateTime(new Date());
		pr.setResponseCode("00");
		pr.setNodeId("12345678");
		pr.setTerminalId("125304493142");
		pr.setTtc("789456");
		
		byte[] bytes = coder.encode(pr);
		PinResponse pr2 = (PinResponse) coder.decode(bytes);
		
		System.out.println(JSONUtils.fromObject(pr));
		System.out.println(JSONUtils.fromObject(pr2));

		assert pr.equals(pr2);

	}

}
