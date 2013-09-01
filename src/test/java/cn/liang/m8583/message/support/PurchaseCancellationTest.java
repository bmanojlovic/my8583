package cn.liang.m8583.message.support;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import cn.liang.m8583.key.DesEncryptor;
import cn.liang.m8583.key.Encryptor;
import cn.liang.m8583.key.KeyFinder;
import cn.liang.m8583.key.KeyFinderDemo;
import cn.liang.m8583.message.support.PurchaseCancellationRequest;
import cn.liang.m8583.message.support.PurchaseCancellationResponse;
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
public class PurchaseCancellationTest {

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
		PurchaseCancellationRequest pcr = new PurchaseCancellationRequest();
		pcr.setAmount(100L);
		pcr.setCard("125304493142");
		pcr.setNodeId("12345678");
		pcr.setOperator("999999");
		pcr.setPin(enc.encryptPassword(kf.findKey(null), "635241"));
		pcr.setRetrievalReferenceNumber("12345678901234567890");
		pcr.setTerminalId("125304493142");
		pcr.setTtc("789456");
		
		
		byte[] bytes = coder.encode(pcr);
		PurchaseCancellationRequest pcr2 = (PurchaseCancellationRequest) coder.decode(bytes);
		
		System.out.println(JSONUtils.fromObject(pcr));
		System.out.println(JSONUtils.fromObject(pcr2));
		System.out.println("password:"+ enc.decryptPassword(kf.findKey(null), pcr.getPin()));
		assert pcr.equals(pcr2);

	}
	
	@Test
	public void testResponse() throws CipherException, EncodeException, DecodeException {
		PurchaseCancellationResponse pcr = new PurchaseCancellationResponse();
		pcr.setDateTime(new Date());
		pcr.setResponseCode("00");
		pcr.setNodeId("12345678");
		pcr.setTerminalId("125304493142");
		pcr.setTtc("789456");
		
		
		byte[] bytes = coder.encode(pcr);
		PurchaseCancellationResponse pcr2 = (PurchaseCancellationResponse) coder.decode(bytes);
		
		System.out.println(JSONUtils.fromObject(pcr));
		System.out.println(JSONUtils.fromObject(pcr2));

		assert pcr.equals(pcr2);

	}

}
