package cn.liang.m8583.util;

import static org.junit.Assert.*;

import org.junit.Test;

import cn.liang.m8583.transcoder.MessageUtil;

/**
 * @author  Liang Yabao
 * 2012-4-5
 */
public class DeprecateTest {

	@Test
	public void testPinData() {
		String pin = "12345678";
		char[] chars = pin.toCharArray();

		byte[] bytes = new byte[4];
		for (int i = 0; i < bytes.length; i++) {
			int hign = MessageUtil.hex2int(chars[i * 2]);
			int low = MessageUtil.hex2int(chars[i * 2 + 1]);
			bytes[i] |= hign << 4;//字符转换为高四位
			bytes[i] |= low;//字符转换为低四位
		}
	}

}
