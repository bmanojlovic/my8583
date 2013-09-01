package cn.liang.m8583.util;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

import cn.liang.m8583.transcoder.MessageUtil;

/**
 * @author  Liang Yabao
 * 2012-4-5
 */
public class UtilTest {

	
	@Test
	public void testBitMap3(){
		long l =0L;
		byte b = (byte) 0xc0;
		System.out.println("testBitMap3 byte: "+b);
		l|=b;
		System.out.println("testBitMap3: "+ l);
		
		 b = (byte) 0xc0;
		 int i = (int)b;
		 System.out.println("testBitMap3 int: "+ i);
		 System.out.println("testBitMap3 int)(char: "+ (int)(char)b);
		 System.out.println("testBitMap3 int: "+( b&0xFF));
		 byte b2 =(byte)(0xc0 &0xFF);
		 System.out.println("testBitMap3 &: "+( b2));
	}
	
	@Test
	public void testCharacter(){
		char c = 'b';
		System.out.println("testCharacter: "+Integer.toBinaryString((int)c));
	}
	
	@Test
	public void testNull(){
		System.out.println("testNull"+null);
	}
	@Test 
	public void testNumber2BCD(){
		String str = "125304493142";
		byte[] bytes = MessageUtil.num2bcd(str.toCharArray());
		String str2 = new String(MessageUtil.bcd2num(bytes,12));
		System.out.println("testNumber2BCD 1 :"+str);
		System.out.println("testNumber2BCD 2 :"+str2);
		assert str2.equals(str):"testNumber2BCD error";
	}
	
	@Test
	public void testCharset(){
		String str = "1－2交易动作码  3－4付出帐户类型，" + "用于借记类，如查询、代收费、转场交易。"
				+ "5－6收入帐户类型，用于代收费、转帐等。" + "帐户人要求交易的交易金额，不含任何处理和交易费用。"
				+ "注：与余额不通用，余额，可用积分等应用54字段表示。"
				+"String str2 = new String(MessageUtil.bcd2Number(bytes,12));";
		try {
			byte[] bytes1 = str.getBytes("UTF-8");
			byte[] bytes2 = str.getBytes("GBK");
			byte[] bytes3 = str.getBytes("GB18030");
			byte[] bytes4 = str.getBytes("UTF-16");
			System.out.println("UTF-8: "+bytes1.length);
			System.out.println("GBK: "+bytes2.length);
			System.out.println("GB2312: "+bytes3.length);
			System.out.println("UTF-16: "+bytes4.length);
			System.out.println("GBK: "+ "1234567890abcdef-".getBytes("GBK").length);
			System.out.println("UTF-8: "+ "1234567890abcdef-".getBytes("UTF-16").length);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testString(){
		System.out.println("byte[0]:"+new String(new byte[0]));
	}
	
	@Test
	public void testAscii2Long(){
		byte[] bytes = {0x39,0x39,0x39,0x39,0x39,0x39,0x39,0x39,0x39,0x39,0x39,0x39};
		System.out.println("length:"+bytes.length);
		System.out.println("byte:"+bytes[0]);
		System.out.println("value:"+MessageUtil.asc2long(bytes, 0, 12));
	}
}
