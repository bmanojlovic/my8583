package cn.liang.m8583.message.support;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import cn.liang.m8583.field.basic.AdditionalAmount;
import cn.liang.m8583.key.DesEncryptor;
import cn.liang.m8583.key.KeyFinderDemo;
import cn.liang.m8583.message.Message;
import cn.liang.m8583.message.support.RechargeRequest;
import cn.liang.m8583.message.support.RechargeResponse;
import cn.liang.m8583.transcoder.DefaultTranscoder;
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
 * @author 325336 <a href="mailto:liangyabao@sf-express.com">梁亚保</a>
 * 
 * CreateDate: 2012-5-21
 */
public class RechargeTest {

	@Test
	public void testRechargeRequest() throws DecodeException, EncodeException{
		RechargeRequest rq = new RechargeRequest();
		rq.setAmount(350L);
		rq.setCard("125304493142");
		rq.setNodeId("12345678");
		rq.setOperator("system");

		rq.setTerminalId("123456789abc");
		rq.setTtc("567891");
		
		DefaultTranscoder eg = new DefaultTranscoder(new KeyFinderDemo(),
				new DesEncryptor());
		byte[] bytes = eg.encode(rq);
		Message mes2 = eg.decode(bytes);

		RechargeRequest rq2= (RechargeRequest)mes2;

		System.out.println("testEngineDecode getCard: "+rq2.getCard());
		assert rq2.getCard().equals(rq.getCard()):"testEngineDecode card number convert wrong!";
		assert rq2.getAmount()== rq.getAmount():"testEngineDecode amount wrong!";
		assert rq2.getNodeId().equals(rq.getNodeId()):"testEngineDecode nodeid";
		assert rq2.getOperator().equals(rq.getOperator()):"operator";

		assert rq2.getTerminalId().equals(rq.getTerminalId()):"terminal id";
		assert rq2.getTtc().equals(rq.getTtc()):"ttc";
		assert rq2.equals(rq):"testEngineDecode  convert wrong!";
	}

	
	@Test
	public void testRechargeResponse() throws EncodeException, DecodeException {
		RechargeResponse rr = new RechargeResponse();
		rr.setTerminalId("123456789012");
		rr.setNodeId("12345678");
		rr.setDateTime(new Date());
		rr.setResponseCode("00");
		rr.setCard("15889780283");
		List list = new ArrayList();
		list.add(new AdditionalAmount("20", "01", "CNY", 320));
		rr.setAdditionalAmountList(list);
		rr.setAmount(100);
		rr.setPreferentialInformation("this is a test 优惠信息!");
		//rr.setUserName("梁亚保");
		rr.setTtc("123456");
		//System.out.println(JSONUtils.fromObject(rr));
		DefaultTranscoder e = new DefaultTranscoder(new KeyFinderDemo(),new DesEncryptor());

		byte[] bytes = e.encode(rr);

		RechargeResponse rr2 = (RechargeResponse) e.decode(bytes);
		//System.out.println(JSONUtils.fromObject(rr2));
		Assert.assertEquals(rr2.getNodeId(), rr.getNodeId());
		// Assert.assertEquals(rr2, rr);
		System.out.println(rr.getNodeId());
		System.out.println(rr2.getNodeId());
		assert rr2.getNodeId().equals(rr.getNodeId()) : "testRechargeResponse getNodeId";
		assert rr2.equals(rr) : "testRechargeResponse";

	}
}
