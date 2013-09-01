package cn.liang.m8583.transcoder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
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

import com.sfpay.framework.common.json.JSONUtils;

/**
 * @author 325336, Liang Yabao
 * 2012-4-5
 */
public class TranscoderTest {

	@Before
	public void registMessage() {
		Message.registMessage("7200", "210000", RechargeRequest.class);
	}

	@Test
	public void testTelephone() throws EncodeException, DecodeException {
		RechargeRequest rq = new RechargeRequest();
		rq.setAmount(190);
		rq.setCard("15818734240");
		rq.setNodeId("12345678");
		rq.setOperator("324924");
		rq.setTerminalId("123456789012");
		rq.setTtc("123456");

		DefaultTranscoder eg = new DefaultTranscoder(new KeyFinderDemo(),
				new DesEncryptor());
		byte[] bytes = eg.encode(rq);
		eg.decode(bytes);

	}

}
