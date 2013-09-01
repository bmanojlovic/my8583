package cn.liang.m8583.stream;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.liang.m8583.field.basic.AdditionalAmount;
import cn.liang.m8583.key.DesEncryptor;
import cn.liang.m8583.key.Encryptor;
import cn.liang.m8583.key.KeyFinder;
import cn.liang.m8583.key.KeyFinderDemo;
import cn.liang.m8583.message.Message;
import cn.liang.m8583.transcoder.DefaultTranscoder;
import cn.liang.m8583.transcoder.MessageUtil;
import cn.liang.m8583.transcoder.exception.CipherException;
import cn.liang.m8583.transcoder.exception.DecodeException;

import com.sfpay.framework.common.json.JSONUtils;

public class MultiThreadTest {

	private static final Logger logger = LoggerFactory.getLogger(MultiThreadTest.class);
	
	@Before
	public void before() throws SecurityException, IOException{
	}
	
	//@Test
	public void testWorkingKeyRequest() throws DecodeException{
		String str = "00 69 60 00 00 00 00 72 00 70 20 00 00 00 c0 50 19 11 13 26 91 18 82 80 00 00 00 00 00 00 00 00 10 12 34 56 31 32 33 34 35 36 37 38 39 30 31 32 31 32 33 34 35 36 37 38 43 4e 59 60 a2 98 64 3c 8e 5a d7 00 14 30 30 31 32 33 34 35 36 37 38 39 30 31 32 00 14 30 30 31 32 33 34 35 36 37 38 39 30 31 32 56 8f 31 49 55 a8 92 be";
		byte[] data = MessageUtil.hex_2_byte(str);
		
		DefaultTranscoder en = new DefaultTranscoder(new KeyFinderDemo(),new DesEncryptor());

		Message mes = en.decode(data);
		System.out.println(JSONUtils.fromObject(mes));
	}

	//@Test
	public void testThread() throws CipherException{
		final String pin = "60a298643c8e5ad7";
		final KeyFinder kf = new KeyFinderDemo();
		final Encryptor enc =new DesEncryptor();
		
		logger.info("pin:{}",pin);
		//String pwd = enc.decryptPassword(kf.findKey(null), MessageUtil.hex2byte(pin.toCharArray()));
		//System.out.println(pwd);
		for( int i =0;i<50;i++){
			Thread th= new Thread(new Runnable(){

				public void run() {
					String pwd;
					try {
						pwd = enc.decryptPassword(kf.findKey(null), MessageUtil.hex2byte(pin.toCharArray()));
						System.out.println("index "+pwd);
					} catch (CipherException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
				}
				
			});
			th.start();
		}
	}
	
	//@Test
	public void testSerialization() throws CipherException {
		final String pin = "60a298643c8e5ad7";
		final KeyFinder kf = new KeyFinderDemo();
		final Encryptor enc = new DesEncryptor();
		logger.debug("pin:{}",pin);
		for (int i = 0; i < 5; i++) {

			String pwd;
			try {
				pwd = enc.decryptPassword(kf.findKey(null),
						MessageUtil.hex2byte(pin.toCharArray()));
				System.out.println("index " + pwd);
			} catch (CipherException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
	
	@Test
	public void testAdditionalAmountThread() throws CipherException{
		
		for( int i =0;i<50;i++){
			Thread th= new Thread(new Runnable(){

				public void run() {
					//List list = new ArrayList();
					AdditionalAmount aa = new AdditionalAmount("20","01","CNY",999999999999L);
					//list.add(aa);
					byte[] bb = aa.encode();
					AdditionalAmount aa2 = new AdditionalAmount();
					aa2.decode(bb);
					System.out.println(JSONUtils.fromObject(aa));
					System.out.println(JSONUtils.fromObject(aa2));
					System.out.println(aa2.equals(aa));
				}
				
			});
			th.start();
		}
		try {
			Thread.currentThread().sleep(5000L);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
