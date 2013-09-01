package cn.liang.m8583.message.support;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import cn.liang.m8583.key.DesEncryptor;
import cn.liang.m8583.key.Encryptor;
import cn.liang.m8583.key.KeyFinder;
import cn.liang.m8583.key.KeyFinderDemo;
import cn.liang.m8583.message.support.RechargeCancellationRequest;
import cn.liang.m8583.message.support.RechargeCancellationResponse;
import cn.liang.m8583.transcoder.DefaultTranscoder;
import cn.liang.m8583.transcoder.Transcoder;
import cn.liang.m8583.transcoder.exception.CipherException;
import cn.liang.m8583.transcoder.exception.DecodeException;
import cn.liang.m8583.transcoder.exception.EncodeException;

import com.sfpay.framework.common.json.JSONUtils;

/**
 * @author 325336, Liang Yabao
 * 2012-4-5
 */
public class RechargeCancellationTest {
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
		RechargeCancellationRequest rcr = new RechargeCancellationRequest();
		rcr.setAmount(100L);
		rcr.setCard("125304493142");
		rcr.setNodeId("12345678");
		rcr.setOperator("999999");
		rcr.setPin(enc.encryptPassword(kf.findKey(null), "63524178"));
		rcr.setRetrievalReferenceNumber("12345678901234567890");
		rcr.setTerminalId("125304493142");
		rcr.setTtc("789456");
		
		
		byte[] bytes = coder.encode(rcr);
		RechargeCancellationRequest rcr2 = (RechargeCancellationRequest) coder.decode(bytes);
		
		System.out.println(JSONUtils.fromObject(rcr));
		System.out.println(JSONUtils.fromObject(rcr2));
		System.out.println("password:"+ enc.decryptPassword(kf.findKey(null), rcr.getPin()));
		assert rcr.equals(rcr2);

	}
	
	@Test
	public void testResponse() throws CipherException, EncodeException, DecodeException {
		RechargeCancellationResponse rcr = new RechargeCancellationResponse();
		rcr.setDateTime(new Date());
		rcr.setResponseCode("00");
		rcr.setNodeId("12345678");
		rcr.setTerminalId("125304493142");
		rcr.setTtc("789456");
		
		
		byte[] bytes = coder.encode(rcr);
		RechargeCancellationResponse rcr2 = (RechargeCancellationResponse) coder.decode(bytes);
		
		System.out.println(JSONUtils.fromObject(rcr));
		System.out.println(JSONUtils.fromObject(rcr2));

		assert rcr.equals(rcr2);

	}

}
