package cn.liang.m8583.transcoder;

/**
 * 
 * 类说明：<br>
 * 报文认证工具
 * 
 * <p>
 * 详细描述：<br>
 * 
 * 
 * </p>
 * 
 * @author  <a href="mailto:liangyabao@sf-express.com">梁亚保</a>
 * 
 * CreateDate: 2012-5-31
 */
public interface MessageAuthenticationTool {

	/**
	 * 方法说明：<br>
	 * 
	 *
	 * @param mes	已经填充MAC的报文
	 * @param mak	报文认证密钥
	 * @return	报文是否可信的
	 */
	public boolean authenticate(byte[] mes,byte[] mak);
	
	/**
	 * 方法说明：<br>
	 * 
	 *
	 * @param mes	未经填充MAC的报文
	 * @param mak	报文认证密钥
	 * @return	经填充MAC的报文
	 */
	public byte[] sign(byte[] mes,byte[] mak);
}
