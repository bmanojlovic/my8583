package cn.liang.m8583.message.support;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cn.liang.m8583.field.basic.TransactionDetail;
import cn.liang.m8583.field.basic.TransactionQuery;
import cn.liang.m8583.field.basic.TransactionRecord;
import cn.liang.m8583.key.CipherUtil;
import cn.liang.m8583.key.DesEncryptor;
import cn.liang.m8583.key.Encryptor;
import cn.liang.m8583.key.KeyFinder;
import cn.liang.m8583.key.KeyFinderDemo;
import cn.liang.m8583.message.support.TransactionDetailRequest;
import cn.liang.m8583.message.support.TransactionDetailResponse;
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
public class TransactionDetailTest {

	private KeyFinder kf;
	private Encryptor enc;
	private Transcoder coder;
	
	@Before
	public void  initCoder(){
		kf = new KeyFinderDemo();
		enc = new DesEncryptor();
		coder = new DefaultTranscoder(kf,enc);
	}
	
	List<TransactionRecord> list = new ArrayList<TransactionRecord>();
	@Before
	public void initTransactionRecordList() {
		
		TransactionRecord tr = new TransactionRecord();
		tr.setCard("15889780283");
		tr.setAmount(100);
		tr.setAdditionalInfo("手机号", "15889780283");
		tr.setCurrency("CNY");
		tr.setDateTime(new Date());
		tr.setRetrievalReferenceNumber("01234567890123456789");
		tr.setTerminalTransactionCode("123456");
		tr.setType(0);
		tr.setWaybillList(genWaybillList());
		
		TransactionRecord tr2 = new TransactionRecord();
		tr2.setCard("15889780283");
		tr2.setAmount(100);
		tr2.setAdditionalInfo("手机号", "15889780283");
		tr2.setCurrency("CNY");
		tr2.setDateTime(new Date());
		tr2.setRetrievalReferenceNumber("01234567890123456781");
		tr2.setTerminalTransactionCode("123455");
		tr2.setType(0);
		tr2.setWaybillList(genWaybillList());

		list.add(tr);
	}
	
	public List<String> genWaybillList(){
		List<String> list = new ArrayList<String>();
		list.add("123456789012");
		list.add("123456789034");
		list.add("123456789056");
		list.add("123456789078");
		list.add("123456789090");
		
		return list;
	}
	@Test
	public void testRequest() throws CipherException, EncodeException, DecodeException {
		TransactionDetailRequest req = new TransactionDetailRequest();
		req.setCard("13800138000");
		req.setNodeId("12345678");
		req.setOperator("123456");
		req.setTerminalId("012345678912");
		req.setPin(enc.encryptPassword(kf.findKey(null), "123456") );
		req.setTtc("123456");
		req.setTransactionQuery(new TransactionQuery(35,21,8));
		
		DefaultTranscoder en = new DefaultTranscoder(new KeyFinderDemo(),new DesEncryptor());

		byte[] bytes = en.encode(req);
		
		TransactionDetailRequest req2 =  (TransactionDetailRequest) en.decode(bytes);
		System.out.println(JSONUtils.fromObject(req));
		System.out.println(JSONUtils.fromObject(req2));
		assert req.equals(req2);
		assert req2.getCard().equals("13800138000");
	}


	@Test
	public void testResponse() throws EncodeException, DecodeException {
		TransactionDetailResponse tdr = new TransactionDetailResponse();
		tdr.setDateTime(new Date());
		tdr.setNodeId("12345678");
		tdr.setResponseCode("00");
		tdr.setTerminalId("123456789012");
		tdr.setTtc("123456");
		
		TransactionDetail transactionDetail = new TransactionDetail();
		transactionDetail.setOffset(1);
		transactionDetail.setTotalRecord(3);
		
		transactionDetail.setTransactionRecordList(list);
		
		TransactionQuery tq = new TransactionQuery(0,1,5);
		
		tdr.setTransactionDetail(transactionDetail);
		tdr.setTransactionQuery(tq);
		
		Transcoder e = new DefaultTranscoder(new KeyFinderDemo(),new DesEncryptor());
		byte[] bytes = e.encode(tdr);
		
		TransactionDetailResponse tdrdec = (TransactionDetailResponse) e.decode(bytes);
		String s1 = JSONUtils.fromObject(tdr);
		System.out.println(s1);
		String s2 = JSONUtils.fromObject(tdrdec);
		System.out.println(s2);
		assert tdr.equals(tdrdec);
		
		
	}

}
