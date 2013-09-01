package cn.liang.m8583.message.support;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import cn.liang.m8583.field.basic.AdditionalAmount;
import cn.liang.m8583.key.DesEncryptor;
import cn.liang.m8583.key.Encryptor;
import cn.liang.m8583.key.KeyFinder;
import cn.liang.m8583.key.KeyFinderDemo;
import cn.liang.m8583.message.support.PurchaseRequest;
import cn.liang.m8583.message.support.PurchaseResponse;
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
public class PurchaseTest {
	
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
	public void testPurchaseRequest() throws CipherException, EncodeException, DecodeException {
		
		PurchaseRequest pr = new PurchaseRequest();
		pr.setAmount(1000L);
		pr.setCard("15889780283");
		pr.setCurrencyCode("CNY");
		pr.setNodeId("abcdef12");
		pr.setTerminalId("125123456789");
		pr.setOperator("999999");
		pr.setPin(enc.encryptPassword(kf.findKey(null), "987654"));
		pr.setTtc("864295");
		List<String> waybill = new ArrayList<String>();
		waybill.add("129876543210");
		waybill.add("120123456789");
		pr.setWaybill(waybill);
		
		byte[] bytes = coder.encode(pr);
		PurchaseRequest pr2 = (PurchaseRequest) coder.decode(bytes);
		
		System.out.println(JSONUtils.fromObject(pr));
		System.out.println(JSONUtils.fromObject(pr2));
		System.out.println("password:"+ enc.decryptPassword(kf.findKey(null), pr.getPin()));
		assert pr.equals(pr2);
		
	}
	
	@Test
	public void testPurchaseResponse() throws CipherException, EncodeException, DecodeException {
		PurchaseResponse pr = new PurchaseResponse();
		pr.setCard("1234567812345678");
		pr.setAmount(100L);
		pr.setAal(aal);
		pr.setDateTime(new Date());
		pr.setNodeId("12345678");
		pr.setResponseCode("00");
		pr.setTerminalId("125304493142");
		pr.setTtc("654321");
		System.out.println(JSONUtils.fromObject(pr));
		
		byte[] bytes = coder.encode(pr);
		PurchaseResponse pr2 = (PurchaseResponse) coder.decode(bytes);
		//pr2.setAmount(200);
		System.out.println(JSONUtils.fromObject(pr));
		System.out.println(JSONUtils.fromObject(pr2));
		
		assert pr.equals(pr2);
		Assert.assertEquals(pr, pr2);
	}

}
