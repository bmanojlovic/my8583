package cn.liang.m8583.key;

/**
 * 密钥查找器
 * POS终端的工作密钥与终端标识相关
 * @author 325336, Liang Yabao
 * 2012-3-22
 */
public interface KeyFinder {

	public byte[] findKey(String terminalID);
	
	public byte[] findMasterKey();
	
}
