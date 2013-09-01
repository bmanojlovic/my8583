package cn.liang.m8583.message.support;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cn.liang.m8583.field.basic.AdditionalAmount;
import cn.liang.m8583.key.DesEncryptor;
import cn.liang.m8583.key.Encryptor;
import cn.liang.m8583.key.KeyFinder;
import cn.liang.m8583.key.KeyFinderDemo;
import cn.liang.m8583.message.support.BalanceRequest;
import cn.liang.m8583.message.support.BalanceResponse;
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
public class BalanceTest {

	private KeyFinder kf;
	private Encryptor enc;
	private Transcoder coder;
	
	@Before
	public void  initCoder(){
		kf = new KeyFinderDemo();
		enc = new DesEncryptor();
		coder = new DefaultTranscoder(kf,enc);
	}

	List<AdditionalAmount> aal = new ArrayList<AdditionalAmount>();

	@Before 
	public void initAdditionalAmountList(){
		AdditionalAmount aa1 = new AdditionalAmount("20","01","CNY",999999999999L);
		AdditionalAmount aa2 = new AdditionalAmount("60","01","ITR",999999999999L);
		
		aal.add(aa1);
		aal.add(aa2);
	}

	@Test
	public void testRequest() throws CipherException, EncodeException, DecodeException {
		BalanceRequest br = new BalanceRequest();
		
		br.setCard("125304493142");
		br.setPin(enc.encryptPassword(kf.findKey(null), "63524178"));

		br.setNodeId("12345678");
		br.setOperator("999999");
		br.setTerminalId("125304493142");
		br.setTtc("789456");
		
		
		byte[] bytes = coder.encode(br);
		BalanceRequest br2 = (BalanceRequest) coder.decode(bytes);
		
		System.out.println(JSONUtils.fromObject(br));
		System.out.println(JSONUtils.fromObject(br2));
		System.out.println("password:"+ enc.decryptPassword(kf.findKey(null), br.getPin()));
		assert br.equals(br2);

	}
	
	@Test
	public void testResponse() throws EncodeException, DecodeException {
		BalanceResponse br = new BalanceResponse();
		br.setAdditionalAmountList(aal);
		br.setCardNumber("125304493142");

		br.setDateTime(new Date());
		br.setResponseCode("00");
		br.setNodeId("12345678");
		br.setTerminalId("125304493142");
		br.setTtc("789456");
		
		byte[] bytes = coder.encode(br);
		BalanceResponse br2 = (BalanceResponse) coder.decode(bytes);
		
		System.out.println(JSONUtils.fromObject(br));
		System.out.println(JSONUtils.fromObject(br2));

		assert br.equals(br2);

	}
}
