package cn.liang.m8583.key;

import static org.junit.Assert.*;

import org.junit.Test;

import cn.liang.m8583.key.Des;
import cn.liang.m8583.key.Mac;
import cn.liang.m8583.key.Des.Mode;

/**
 * @author  Liang Yabao
 * 2012-4-5
 */
public class DesTest {
	
	private static int ENC = 0;

	private static int DEC = 1;

	//hex表示，长度应为16，即8个字节。一个密钥
	private String key = "abcdef0123456789";
	
	//hex表示，长度应为16，即8个字节。数据块
	private String data = "0123456789abcdef";

	// hex表示，长度应为16，即8个字节。一个密钥
	private String key1 = "a957ef0123cda7a9";

	// hex表示，长度应为16，即8个字节。数据块
	private String data1 = "aa23cf6789a936ef";

	@Test
	public void test() {
		
		//System.out.println(Des.string2Binary(key));
		String encStr = Des.des(data, key, Des.Mode.ENCRYPTION);
		System.out.println(encStr);
		String decStr = Des.des(encStr, key, Des.Mode.DECRYPTION);
		System.out.println(decStr);
		assert decStr.equals(data.toUpperCase());
		
		String encStr1 = Des.des(data1, key1, Des.Mode.ENCRYPTION);
		System.out.println(encStr1);
		String decStr1 = Des.des(encStr1, key1, Des.Mode.DECRYPTION);
		System.out.println(decStr1);
		assert decStr1.equals(data1.toUpperCase());
		
		String k="FFFFFFFFFFFFFFFF";
		String encStr2 = Des.des("0000000000000000",k , Des.Mode.ENCRYPTION);
		System.out.println("加密:"+encStr2);
		assert "CAAAAF4DEAF1DBAE".equals(encStr2);
		String decStr2 = Des.des(encStr2, k, Des.Mode.DECRYPTION);
		System.out.println(decStr2);
		assert decStr2.equals("0000000000000000".toUpperCase());
		
	}

	@Test
	public void testDes(){
		String k="3132333435363738";
		String dataIn="3132333435363738";
		String dataOut = Des.des(dataIn,k , Des.Mode.ENCRYPTION);
		System.out.println("加密testDes>:"+dataOut);
		assert "96D0028878D58C89".equals(dataOut);
		String decStr2 = Des.des(dataOut, k, Des.Mode.DECRYPTION);
		System.out.println("解密testDes>"+decStr2);
		assert decStr2.equals("3132333435363738".toUpperCase());
	}
	
	// @Test
	public void testMain() {

		System.out.println("/*************Tripe-DES*************/");

		// 主密钥

		String masterKey = "11111111111111112222222222222222";

		// 数据

		String data = "1111111111111111";

		System.out.println(Des.des3(data, masterKey, Mode.ENCRYPTION));

		String dpk = Mac.getDPK("1111111111111111", "1111111111111111",
				"11111111111111111111111111111111");

		System.out.println(dpk);

	}
	
	@Test
	public void testASC_2_HEX(){
		String asc = "123456";
		String hex = Des.ASC_2_HEX(asc);
		System.out.println(hex);
		assert hex.equals("313233343536");
	}
	
	@Test
	public void testXY() {
		for (int k = 0; k < 30; k++) {
			int i = getXY(2, k);
			System.out.println("Des.getXY:"+i);
			assert i == (1 << k) : "getXY not equals left move";
		}
	}
	
	/**
	 * 
	 * 返回x的y次方
	 * 
	 * @param x
	 * 
	 * @param y
	 * 
	 * @return
	 */

	public static int getXY(int x, int y) {

		int temp = x;

		if (y == 0)
			x = 1;

		for (int i = 2; i <= y; i++) {

			x *= temp;

		}

		return x;

	}
}
