package cn.liang.m8583.key;

import java.io.UnsupportedEncodingException;

import cn.liang.m8583.transcoder.MessageUtil;


/**
 * 
 * @author  Liang Yabao
 * 2012-3-22
 */
public  class KeyFinderDemo implements KeyFinder{

	public byte[] findKey(String terminalID) {

			return MessageUtil.hex2byte("123456781234567812345678123456781234567812345678".toCharArray());
		

	}

	public byte[] findMasterKey() {
		try {
			return "123456781234567812345678".getBytes("GBK");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


}
