package cn.liang.m8583.stream;

import java.util.Arrays;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;

import cn.liang.m8583.field.Field;
import cn.liang.m8583.key.Des;
import cn.liang.m8583.key.DesEncryptor;
import cn.liang.m8583.key.KeyFinderDemo;
import cn.liang.m8583.message.Message;
import cn.liang.m8583.transcoder.DefaultTranscoder;
import cn.liang.m8583.transcoder.MessageUtil;
import cn.liang.m8583.transcoder.exception.DecodeException;

import com.sfpay.framework.common.json.JSONUtils;

/**
 * @author 325336, Liang Yabao
 * 2012-4-5
 */
public class StreamTest {
	
	@Before
	public void pre(){
		Logger logger = Logger.getLogger(Field.class.getName());
		logger.setLevel(Level.ALL);
		Handler handler = new ConsoleHandler();
		handler.setLevel(Level.FINE);
		logger.addHandler(handler);
		//System.out.println(logger.isLoggable(Level.ALL));
		
	}
	//@Test
	public void test() {
		byte[] bs = {1,2,3};
	}

	//@Test
	public void testHHT() throws DecodeException{
		String str = "00 4d 60 00 00 00 00 72 00 70 38 00 00 00 c0 00 09 11 01 32 69 11 88 28 21 00 00 00 00 00 00 00 10 00 00 10 17 32 57 20 12 03 29 31 32 35 31 32 33 34 35 36 37 38 39 37 35 35 59 00 00 00 00 00 06 39 39 39 39 39 39 b5 0f b4 bd 31 50 47 6e";
		byte[] data = MessageUtil.hex_2_byte(str);
		
		DefaultTranscoder en = new DefaultTranscoder(new KeyFinderDemo(),new DesEncryptor());

		Message mes = en.decode(data);
		//System.out.println(JSONUtils.fromObject(mes));
	}
	
	@Test
	public void testPurchase() throws DecodeException{
		String str = "00 61 60 00 00 00 00 72 00 70 38 00 00 00 c0 50 19 11 13 26 91 18 82 80 00 00 00 00 00 00 00 00 10 00 00 91 10 50 32 20 12 04 11 31 32 35 31 32 33 34 35 36 37 38 39 37 35 35 59 00 00 00 00 43 4e 59 96 d0 02 88 78 d5 8c 89 00 14 00 88 81 23 45 67 89 00 06 39 39 39 39 39 39 f3 01 57 9b 99 df 30 cf";
		byte[] data = MessageUtil.hex_2_byte(str);
		
		DefaultTranscoder en = new DefaultTranscoder(new KeyFinderDemo(),new DesEncryptor());

		Message mes = en.decode(data);
		//System.out.println(JSONUtils.fromObject(mes));
	}
	//@Test
	public void testBalanceRequest() throws DecodeException{
		String str = "00 4f 60 00 00 00 00 71 00 60 38 00 00 00 c0 10 09 11 13 26 91 18 82 80 31 00 00 00 00 29 11 00 "
+"59 20 12 04 01 31 32 35 31 32 33 34 35 36 37 38 39 37 35 35 59 00 00 00 00 62 dd 8e 4a 61 4e 1a "
+"f9 00 06 39 39 39 39 39 39 11 a1 28 52 22 36 37 3b";
		byte[] data = MessageUtil.hex_2_byte(str);
		
		DefaultTranscoder en = new DefaultTranscoder(new KeyFinderDemo(),new DesEncryptor());

		Message mes = en.decode(data);
		//System.out.println(JSONUtils.fromObject(mes));
	}
	
	//@Test
	public void testBalanceResponse() throws DecodeException{
		String str = "00 44 60 00 00 00 00 71 10 60 38 00 00 02 d0 04 01 11 13 26 91 18 82 80 31 00 00 00 00 29 11 01 "
+"04 20 12 04 01 30 31 31 32 35 31 32 33 34 35 36 37 38 39 37 35 35 59 00 00 00 00 00 00 00 00 00 "
+"00 00 00 00 00 00";
		byte[] data = MessageUtil.hex_2_byte(str);
		
		DefaultTranscoder en = new DefaultTranscoder(new KeyFinderDemo(),new DesEncryptor());

		Message mes = en.decode(data);
		System.out.println(JSONUtils.fromObject(mes));
	}
	//@Test
	public void testWorkingKeyRequest() throws DecodeException{
		String str = "00 42 60 00 00 00 00 78 00 20 38 01 00 00 c0 00 09 79 00 00 00 00 55 16 29 06 20 12 04 09 00 01 31 32 35 31 32 33 34 35 36 37 38 39 37 35 35 59 00 00 00 00 00 06 39 39 39 39 39 39 b9 28 67 01 2e 43 55 82";
		byte[] data = MessageUtil.hex_2_byte(str);
		
		DefaultTranscoder en = new DefaultTranscoder(new KeyFinderDemo(),new DesEncryptor());

		Message mes = en.decode(data);
		System.out.println(JSONUtils.fromObject(mes));
	}
	
	//@Test
	public void testDes(){
		String str = "00 4d 60 00 00 00 00 72 00 70 38 00 00 00 c0 00 09 11 01 32 69 11 88 28 21 00 00 00 00 00 00 00 10 00 00 10 17 32 57 20 12 03 29 31 32 35 31 32 33 34 35 36 37 38 39 37 35 35 59 00 00 00 00 00 06 39 39 39 39 39 39 b5 0f b4 bd 31 50 47 6e";
		byte[] data = MessageUtil.hex_2_byte(str);
		byte[] mac = xor(data,7,data.length-8-7);
		String macS = Des.des(new String(MessageUtil.byte2hex(mac)), "1234567812345678", Des.Mode.ENCRYPTION);
		System.out.println(MessageUtil.byte2hex(mac));
		System.out.println("des:"+macS);
		String macS3 = Des.des3(new String(MessageUtil.byte2hex(mac)), "01020304050607080102030405060708", Des.Mode.ENCRYPTION);
		
		System.out.println("3des:"+macS3);
		
		String mt2 = "72 00 70 38 00 00 00 c0 00 09 11 01 32 69 11 88 28 21 00 00 00 00 00 00 00 10 00 00 10 17 32 57 20 12 03 29 31 32 35 31 32 33 34 35 36 37 38 39 37 35 35 59 00 00 00 00 00 06 39 39 39 39 39 39";
		byte[] data2 = MessageUtil.hex_2_byte(mt2);
		System.out.println(Arrays.toString(data2));
		byte[] mac2 = xor(data2,0,data2.length);
		assert Arrays.equals(mac, mac2);
		
		byte[] macx2 = xor2(data2,data2.length);
		System.out.println(Arrays.toString(mac2));
		System.out.println(Arrays.toString(macx2));
		Arrays.equals(mac, macx2);
	}
	
	/**
	 * mac的异或运算
	 * @param src	原始报文
	 * @param offset	起始位置
	 * @param length	要校验的长度
	 * @return	校验结果
	 */
	private byte[] xor(byte[] src, int offset, int length) {
		byte[] mac = new byte[8];
		for (int i = 0; i < length; i++) {
			mac[i % 8] ^= src[offset + i];
		}
		return mac;
	}
	
	byte[] xor2(byte[] psMsg, int uiLength) {
		int i = 0;
		byte[] sOutMAC = new byte[8];

		int uiOffset = 0;
		while (uiLength > uiOffset) {
			if ((uiLength - uiOffset) <= 8) {
				for (i = 0; i < uiLength - uiOffset; i++)
					sOutMAC[i] ^= psMsg[uiOffset + i];
				break;
			}
			for (i = 0; i < 8; i++)
				sOutMAC[i] ^= psMsg[uiOffset + i];
			uiOffset += 8;
		}
		return sOutMAC;
	}

}
