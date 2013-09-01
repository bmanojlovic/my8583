package cn.liang.m8583.key;

/**
 * 标准MAC算法。
 * @author 325336, Liang Yabao
 * 2012-4-13
 */
public class Mac extends Des{
	
	private static int HEX = 0;

	private static int ASC = 1;
	
	/**
	 * 
	 * 将data分散
	 * 
	 * @param data
	 * 
	 * @param key
	 * 
	 * @param type
	 * 
	 * @return
	 */

	public static String divData(String data, String key, int type) {

		String left = null;

		String right = null;

		if (type == HEX) {

			left = key.substring(0, 16);

			right = key.substring(16, 32);

		}

		if (type == ASC) {

			left = ASC_2_HEX(key.substring(0, 8));

			right = ASC_2_HEX(key.substring(8, 16));

		}

		// 加密

		data = des(data, left, Mode.ENCRYPTION);

		// 解密

		data = des(data, right, Mode.DECRYPTION);

		// 加密

		data = des(data, left, Mode.ENCRYPTION);

		return data;

	}

	/**
	 * 
	 * 取反(10001)--->(01110)
	 * 
	 * @param source
	 * 
	 * @return
	 */

	public static String reverse(String source) {

		int[] data = string2Binary(source);

		int j = 0;

		for (int i : data) {

			data[j++] = 1 - i;

		}

		return binary2ASC(intArr2Str(data));

	}

	/**
	 * 
	 * 主密钥需要经过两次分散获得IC卡中的子密钥
	 * 
	 * @param issuerFlag发卡方标识符
	 * 
	 * @param appNo应用序列号即卡号
	 * 
	 * @param mpk主密钥
	 * 
	 * @return
	 */

	public static String getDPK(String issuerFlag, String appNo, String mpk) {

		// 第一次分散

		StringBuffer issuerMPK = new StringBuffer();

		// 获取Issuer MPK左半边

		issuerMPK.append(divData(issuerFlag, mpk, 0));

		// 获取Issuer MPK右半边

		issuerMPK.append(divData(reverse(issuerFlag), mpk, 0));

		// 第二次分散

		StringBuffer dpk = new StringBuffer();

		// 获取DPK左半边

		dpk.append(divData(appNo, issuerMPK.toString(), 0));

		// 获取DPK右半边

		dpk.append(divData(reverse(appNo), issuerMPK.toString(), 0));

		return dpk.toString();

	}

	/**
	 * 
	 * 计算MAC(hex)
	 * 
	 * ANSI-X9.9-MAC(16的整数�?不补)
	 * 
	 * PBOC-DES-MAC(16的整数倍)
	 * 
	 * 使用单倍长密钥DES算法
	 * 
	 * @param key	密钥(16个hex字符，即8个字节)
	 * 
	 * @param vector	初始向量0000000000000000
	 * 
	 * @param data	数据，先转换成16进制字符串表示，再填充成为16的整数倍长度
	 * 
	 * @return mac	
	 */

	public static String mac(String key, String vector, String data, int type) {

		if (type == ASC) {

			data = ASC_2_HEX(data);

		}

		int len = data.length();

		int arrLen = len / 16 + 1;

		String[] D = new String[arrLen];
		
		//填充
		if (vector == null)
			vector = "0000000000000000";

		if (len % 16 == 0) {

			data += "8000000000000000";

		} else {

			data += "80";

			for (int i = 0; i < 15 - len % 16; i++) {

				data += "00";

			}

		}
		//填充结束

		//每8个字节，即16个hex字符为一段，截取成一段段的。
		for (int i = 0; i < arrLen; i++) {
			D[i] = data.substring(i * 16, i * 16 + 16);
			System.out.println("D[" + i + "]=" + D[i]);
		}

		// D0 Xor Vector
		String I = xOr(D[0], vector);
		String O = null;
		
		// System.out.println("I=" + I);
		for (int i = 1; i < arrLen; i++) {
			//每段异或后进行一次单DES加密
			O = des(I, key, Mode.ENCRYPTION);
			// System.out.println("O=" + O);
			I = xOr(D[i], O);
			// System.out.println("I=" + I);
		}

		O = des(I, key, Mode.ENCRYPTION);//最后一段的加密操作
		return O;
	}

	/**
	 * 
	 * 将s1和s2做异或，然后返回
	 * 
	 * @param s1
	 * 
	 * @param s2
	 * 
	 * @return
	 */

	public static String xOr(String s1, String s2) {

		int[] iArr = diffOr(string2Binary(s1), string2Binary(s2));

		return binary2ASC(intArr2Str(iArr));

	}

	public static void printArr(int[] source) {

		int len = source.length;

		for (int i = 0; i < len; i++) {

			System.out.print(source[i]);

		}

		System.out.println();

	}

}
