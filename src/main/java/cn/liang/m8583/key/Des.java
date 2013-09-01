package cn.liang.m8583.key;

import java.io.UnsupportedEncodingException;

import org.slf4j.LoggerFactory;

/**
 * DES算法。ECB,无填充方式。
 * @author  Liang Yabao
 * 2012-3-30
 */
public class Des {

	/***************************** 压缩替换S-Box **************************************************/

	private static final int[][] s1 = {
			{ 14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7 },

			{ 0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8 },

			{ 4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0 },

			{ 15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13 } };

	private static final int[][] s2 = {
			{ 15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10 },

			{ 3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5 },

			{ 0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15 },

			{ 13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9 } };

	private static final int[][] s3 = {
			{ 10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8 },

			{ 13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1 },

			{ 13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7 },

			{ 1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12 } };

	private static final int[][] s4 = {
			{ 7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15 },

			{ 13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9 },

			{ 10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4 },

			{ 3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14 } };

	private static final int[][] s5 = {
			{ 2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9 },

			{ 14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6 },

			{ 4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14 },

			{ 11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3 } };

	private static final int[][] s6 = {
			{ 12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11 },

			{ 10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8 },

			{ 9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6 },

			{ 4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13 } };

	private static final int[][] s7 = {
			{ 4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1 },

			{ 13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6 },

			{ 1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2 },

			{ 6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12 } };

	private static final int[][] s8 = {
			{ 13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7 },

			{ 1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2 },

			{ 7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8 },

			{ 2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11 } };

	//置换规则表
	private static final int[] ip = { 58, 50, 42, 34, 26, 18, 10, 2,

	60, 52, 44, 36, 28, 20, 12, 4,

	62, 54, 46, 38, 30, 22, 14, 6,

	64, 56, 48, 40, 32, 24, 16, 8,

	57, 49, 41, 33, 25, 17, 9, 1,

	59, 51, 43, 35, 27, 19, 11, 3,

	61, 53, 45, 37, 29, 21, 13, 5,

	63, 55, 47, 39, 31, 23, 15, 7 };

	//逆置换规则表
	private static final int[] _ip = { 40, 8, 48, 16, 56, 24, 64, 32,

	39, 7, 47, 15, 55, 23, 63, 31,

	38, 6, 46, 14, 54, 22, 62, 30,

	37, 5, 45, 13, 53, 21, 61, 29,

	36, 4, 44, 12, 52, 20, 60, 28,

	35, 3, 43, 11, 51, 19, 59, 27,

	34, 2, 42, 10, 50, 18, 58, 26,

	33, 1, 41, 9, 49, 17, 57, 25 };

	
	// 每次密钥循环左移位数

	private static final int[] LS = { 1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2,
			2, 1 };


	/**
	 * 将十六进制A--F转换成对应数
	 * @param ch
	 * @return
	 * @throws Exception
	 */
	public static int hex2int(char ch) throws Exception {

		char t = Character.toUpperCase(ch);

		switch (t) {
		case '0':
			return 0;
		case '1':
			return 1;
		case '2':
			return 2;
		case '3':
			return 3;
		case '4':
			return 4;
		case '5':
			return 5;
		case '6':
			return 6;
		case '7':
			return 7;
		case '8':
			return 8;
		case '9':
			return 9;
		case 'A':
			return 10;
		case 'B':
			return 11;
		case 'C':
			return 12;
		case 'D':
			return 13;
		case 'E':
			return 14;
		case 'F':
			return 15;
		default:
			throw new Exception("hex2int was wrong");
		}
	}

	/**
	 * 
	 * 将字符串转换成二进制数组
	 * 
	 * @param source	每个字符表示16进制数'0'-'9','A'-'F'
	 * @return
	 */

	public static int[] string2Binary(String source) {

		int len = source.length();

		int[] dest = new int[len * 4];

		char[] arr = source.toCharArray();

		for (int i = 0; i < len; i++) {

			int t = 0;

			try {

				t = hex2int(arr[i]);


			} catch (Exception e) {

				e.printStackTrace();

			}

			String[] str = Integer.toBinaryString(t).split("");

			int k = i * 4 + 3;

			for (int j = str.length - 1; j > 0; j--) {

				dest[k] = Integer.parseInt(str[j]);

				k--;

			}

		}

		return dest;

	}

	

	//表示16进制的字符数组。
		private static final char[] hex={
			'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'
		};
		
	/**
	 * 4位长度的二进制字符串转换成一个16进制字符
	 * 
	 * @param s
	 *            4位长度的二进制字符串，每个字符表示'0'或者'1'
	 * @return
	 */
	public static String binary2Hex(String s) {

		int len = s.length();

		int result = 0;

		int k = 0;

		if (len > 4)
			return null;
		for (int i = len; i > 0; i--) {
			result += Integer.parseInt(s.substring(i - 1, i)) * (1 << k);
			k++;
		}
		return int2hex(result);
	}

	/**
	 * 
	 * 将int转换成Hex
	 * 
	 * @param i
	 * 
	 * @return
	 * 
	 * @throws Exception
	 */

	public static String int2hex(int i) {

		return String.valueOf(hex[i]);
	}

	/**
	 * 
	 * 将二进制字符串转换成十六进制字符
	 * 
	 * @param s
	 * 
	 * @return
	 */

	public static String binary2ASC(String s) {

		String str = "";

		int ii = 0;

		int len = s.length();

		// 不够4bit左补0

		if (len % 4 != 0) {

			while (ii < 4 - len % 4) {

				s = "0" + s;

			}

		}

		for (int i = 0; i < len / 4; i++) {

			str += binary2Hex(s.substring(i * 4, i * 4 + 4));

		}

		return str;

	}

	/**
	 * 
	 * IP初始置换
	 * 
	 * @param source
	 * 
	 * @return
	 */

	public static int[] changeIP(int[] source) {

		int[] dest = new int[64];

		for (int i = 0; i < 64; i++) {

			dest[i] = source[ip[i] - 1];

		}

		return dest;

	}

	/**
	 * 
	 * IP-1逆置
	 * 
	 * @param source
	 * 
	 * @return
	 */

	public static int[] changeInverseIP(int[] source) {

		int[] dest = new int[64];

		for (int i = 0; i < 64; i++) {

			dest[i] = source[_ip[i] - 1];

		}

		return dest;

	}

	/**
	 * 
	 * 32bit扩展48bit
	 * 
	 * @param source
	 * 
	 * @return
	 */

	public static int[] expend(int[] source) {

		int[] ret = new int[48];

		int[] temp = { 32, 1, 2, 3, 4, 5,

		4, 5, 6, 7, 8, 9,

		8, 9, 10, 11, 12, 13,

		12, 13, 14, 15, 16, 17,

		16, 17, 18, 19, 20, 21,

		20, 21, 22, 23, 24, 25,

		24, 25, 26, 27, 28, 29,

		28, 29, 30, 31, 32, 1 };

		for (int i = 0; i < 48; i++) {

			ret[i] = source[temp[i] - 1];

		}

		return ret;

	}

	/**
	 * 
	 * 48bit压缩32bit
	 * 
	 * @param source
	 *            (48bit)
	 * 
	 * @return R(32bit)
	 * 
	 *         B=E(R)⊕K，将48 位的B 分成8 个分组，B=B1B2B3B4B5B6B7B8
	 */

	public static int[] press(int[] source) {

		int[] ret = new int[32];

		int[][] temp = new int[8][6];

		int[][][] s = { s1, s2, s3, s4, s5, s6, s7, s8 };

		StringBuffer str = new StringBuffer();

		for (int i = 0; i < 8; i++) {

			for (int j = 0; j < 6; j++) {

				temp[i][j] = source[i * 6 + j];

			}

		}

		for (int i = 0; i < 8; i++) {

			// (16)

			int x = temp[i][0] * 2 + temp[i][5];

			// (2345)

			int y = temp[i][1] * 8 + temp[i][2] * 4 + temp[i][3] * 2
					+ temp[i][4];

			int val = s[i][x][y];

			String ch = int2hex(val);

			str.append(ch);

		}

		ret = string2Binary(str.toString());

		// 置换P

		ret = dataP(ret);

		return ret;

	}

	/**
	 * 
	 * 置换P(32bit)
	 * 
	 * @param source
	 * 
	 * @return
	 */

	public static int[] dataP(int[] source) {

		int[] dest = new int[32];

		int[] temp = { 16, 7, 20, 21,

		29, 12, 28, 17,

		1, 15, 23, 26,

		5, 18, 31, 10,

		2, 8, 24, 14,

		32, 27, 3, 9,

		19, 13, 30, 6,

		22, 11, 4, 25 };

		int len = source.length;

		for (int i = 0; i < len; i++) {

			dest[i] = source[temp[i] - 1];

		}

		return dest;

	}

	/**
	 * 
	 * @param R
	 *            (32bit)
	 * 
	 * @param K
	 *            (48bit的轮子密钥
	 * 
	 * @return 32bit
	 */

	public static int[] f(int[] R, int[] K) {

		int[] dest = new int[32];

		int[] temp = new int[48];

		// 先将输入32bit扩展48bit

		int[] expendR = expend(R);// 48bit

		// 与轮子密钥进行异或运算

		temp = diffOr(expendR, K);

		// 压缩成32bit

		dest = press(temp);

		return dest;

	}

	/**
	 * 
	 * 两个等长的数组做异或
	 * 
	 * @param source1
	 * 
	 * @param source2
	 * 
	 * @return
	 */

	public static int[] diffOr(int[] source1, int[] source2) {

		int len = source1.length;

		int[] dest = new int[len];

		for (int i = 0; i < len; i++) {

			dest[i] = source1[i] ^ source2[i];

		}

		return dest;

	}

	/**
	 * 
	 * DES加密--->对称密钥
	 * 
	 * D = Ln(32bit)+Rn(32bit)
	 * 
	 * 经过16轮置�?
	 * 
	 * @param D
	 *            (16byte)明文
	 * 
	 * @param K
	 *            (16byte)轮子密钥
	 * 
	 * @return (16byte)密文
	 */

	public static String encrypt(String D, String K) {

		String str = "";

		int[] temp = new int[64];

		int[] data = string2Binary(D);

		// 第一步初始置换

		data = changeIP(data);

		int[][] left = new int[17][32];
		int[][] right = new int[17][32];

		for (int j = 0; j < 32; j++) {

			left[0][j] = data[j];
			right[0][j] = data[j + 32];

		}

		int[][] subKey = subKey(K);// sub key ok

		for (int i = 1; i < 17; i++) {

			// 获取(48bit)的轮子密钥

			int[] key = subKey[i - 1];

			// L1 = R0

			left[i] = right[i - 1];

			// R1 = L0 ^ f(R0,K1)

			int[] fTemp = f(right[i - 1], key);// 32bit

			right[i] = diffOr(left[i - 1], fTemp);

		}

		// 64位组合的时候，左右调换**************************************************

		for (int i = 0; i < 32; i++) {

			temp[i] = right[16][i];

			temp[32 + i] = left[16][i];

		}

		temp = changeInverseIP(temp);

		str = binary2ASC(intArr2Str(temp));

		return str;

	}

	/**
	 * 
	 * DES解密--->对称密钥
	 * 
	 * 解密算法与加密算法基本相同，不同之处仅在于轮子密钥的使用顺序逆序，即解密的第1
	 * 
	 * 轮子密钥为加密的第16 轮子密钥，解密的第2轮子密钥为加密的第15 轮子密钥，…，
	 * 
	 * 解密的第16 轮子密钥为加密的第1轮子密钥
	 * 
	 * @param source密文
	 * 
	 * @param key密钥
	 * 
	 * @return
	 */

	public static String decrypt(String source, String key) {

		String str = "";

		int[] data = string2Binary(source);// 64bit

		// 第一步初始置换

		data = changeIP(data);

		int[] left = new int[32];

		int[] right = new int[32];

		int[] tmp = new int[32];

		for (int j = 0; j < 32; j++) {

			left[j] = data[j];

			right[j] = data[j + 32];

		}

		int[][] subKey = subKey(key);// sub key ok

		for (int i = 16; i > 0; i--) {

			// 获取(48bit)的轮子密钥

			/********* 不同之处 **********/

			int[] sKey = subKey[i - 1];

			tmp = left;

			// R1 = L0

			left = right;

			// L1 = R0 ^ f(L0,K1)

			int[] fTemp = f(right, sKey);// 32bit

			right = diffOr(tmp, fTemp);

		}

		// �?��组合的时候，左右调换**************************************************

		for (int i = 0; i < 32; i++) {

			data[i] = right[i];

			data[32 + i] = left[i];

		}

		data = changeInverseIP(data);

		for (int i = 0; i < data.length; i++) {

			str += data[i];

		}

		str = binary2ASC(str);

		return str;

	}

	/**
	 * 
	 * 单倍长密钥DES(16byte)
	 * 
	 * @param source
	 * 
	 * @param key
	 * 
	 * @param type
	 * 
	 * @return
	 */

	public static String des(String source, String key, Mode type) {

		if (source.length() != 16 || key.length() != 16)
			return null;

		if (type == Mode.ENCRYPTION) {

			return encrypt(source, key);

		}

		if (type == Mode.DECRYPTION) {

			return decrypt(source, key);

		}

		return null;

	}


	/**
	 * 
	 * 三重DES算法(双倍长密钥32个16进制字符，即16个字节))
	 * 
	 * 密钥K1和K2
	 * 
	 * 1、先用K1加密明文
	 * 
	 * 2、接着K2对上面的结果进行解密
	 * 
	 * 3、然后用K1对上面的结果进行加密
	 * 
	 * @param source
	 * @param key
	 * @param type
	 * @return
	 */

	public static String des3(String source, String key, Mode type) {

		if (key.length() != 32 || source.length() != 16)
			return null;

		String temp = null;

		String K1 = key.substring(0, key.length() / 2);

		String K2 = key.substring(key.length() / 2);

		LoggerFactory.getLogger(Des.class).debug("K1--->{} ; K2--->{} ;" , K1,K2);

		if (type == Mode.ENCRYPTION) {

			temp = encrypt(source, K1);

			temp = decrypt(temp, K2);

			return encrypt(temp, K1);

		}

		if (type == Mode.DECRYPTION) {

			temp = decrypt(source, K1);

			temp = encrypt(temp, K2);

			return decrypt(temp, K1);

		}

		return null;

	}

	/********** 48bit的轮子密钥的生成 ***************/

	/**
	 * 缩小选择换位1
	 * 64bit的密钥转换成56bit
	 * @param source	64个整数，每个整数都是0或者1
	 * @return	56个整数，每个整数都是0或者1
	 */
	public static int[] keyPC_1(int[] source) {

		int[] dest = new int[56];

		int[] temp = { 57, 49, 41, 33, 25, 17, 9,

		1, 58, 50, 42, 34, 26, 18,

		10, 2, 59, 51, 43, 35, 27,

		19, 11, 3, 60, 52, 44, 36,

		63, 55, 47, 39, 31, 23, 15,

		7, 62, 54, 46, 38, 30, 22,

		14, 6, 61, 53, 45, 37, 29,

		21, 13, 5, 28, 20, 12, 4 };

		for (int i = 0; i < 56; i++) {

			dest[i] = source[temp[i] - 1];

		}

		return dest;

	}

	/**
	 * 将密钥循环左移
	 * 
	 * @param source
	 *            二进制密钥位，28位
	 * @param ls
	 *            循环左移位数
	 * @return
	 */
	public static int[] keyLeftMove(int[] source, int ls) {

		int temp = 0;

		int len = source.length;

		//循环左移位数
		//int ls = LS[i];

		for (int k = 0; k < ls; k++) {

			temp = source[0];

			for (int j = 0; j < len - 1; j++) {

				source[j] = source[j + 1];

			}

			source[len - 1] = temp;

		}

		return source;

	}

	/**
	 * 缩小选择换位2
	 * 56bit的密钥转换成48bit
	 * @param source	56个整数，每个整数都是0或者1
	 * @return	48个整数，第个整数都是0或者1
	 */
	public static int[] keyPC_2(int[] source) {

		int[] dest = new int[48];

		int[] temp = { 14, 17, 11, 24, 1, 5,

		3, 28, 15, 6, 21, 10,

		23, 19, 12, 4, 26, 8,

		16, 7, 27, 20, 13, 2,

		41, 52, 31, 37, 47, 55,

		30, 40, 51, 45, 33, 48,

		44, 49, 39, 56, 34, 53,

		46, 42, 50, 36, 29, 32 };

		for (int i = 0; i < 48; i++) {

			dest[i] = source[temp[i] - 1];

		}

		return dest;

	}

	/**
	 * 
	 * 获取轮子密钥(48bit)
	 * 
	 * @param source	64位密钥
	 * 
	 * @return	16个子密钥，每个子密钥为48位
	 */
	public static int[][]  subKey(String source) {

		int[][] subKey = new int[16][48];

		// 装换64bit
		int[] temp = string2Binary(source);

		// 经过PC-1 64bit转换56bit
		int[] temp56 = keyPC_1(temp);
		
		// 将经过转换的56bit temp1均分成两部分
		int[] left = new int[28];
		int[] right = new int[28];
		for (int i = 0; i < 28; i++) {
			left[i] = temp56[i];
			right[i] = temp56[i + 28];
		}

		// 经过16次循环左移，然后PC-2置换

		for (int i = 0; i < 16; i++) {

			left = keyLeftMove(left, LS[i]);
			right = keyLeftMove(right, LS[i]);

			for (int j = 0; j < 28; j++) {

				temp56[j] = left[j];

				temp56[j + 28] = right[j];

			}

			subKey[i] = keyPC_2(temp56);

		}
		return subKey;
	}

	/**
	 * 
	 * 将ASC字符串转16进制字符串
	 * 
	 * @param asc
	 * 
	 * @return
	 */

	public static String ASC_2_HEX(String asc) {

		StringBuffer hex = new StringBuffer();

		try {

			byte[] bs = asc.toUpperCase().getBytes("UTF-8");

			for (byte b : bs) {

				hex.append(Integer.toHexString(new Byte(b).intValue()));

			}

		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();

		}

		return hex.toString();

	}

	/**
	 * 
	 * 16进制的字符串转换成ASC的字符串
	 * 
	 * 16进制的字符串压缩成BCD码30313233343536373839414243444546)-->(0123456789ABCDEF)
	 * 
	 * @param hex
	 * 
	 * @return
	 */

	public static String HEX_2_ASC(String hex) {

		String asc = null;

		int len = hex.length();

		byte[] bs = new byte[len / 2];

		for (int i = 0; i < len / 2; i++) {

			bs[i] = Byte.parseByte(hex.substring(i * 2, i * 2 + 2), 16);

		}

		try {

			asc = new String(bs, "UTF-8");

		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();

		}

		return asc;

	}

	/**
	 * 
	 * 将int类型数组拼接成字符串
	 * 
	 * @param arr
	 * 
	 * @return
	 */

	public static String intArr2Str(int[] arr) {

		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < arr.length; i++) {

			sb.append(arr[i]);

		}

		return sb.toString();

	}


	public static enum Mode{
		ENCRYPTION,DECRYPTION;
	}
}
