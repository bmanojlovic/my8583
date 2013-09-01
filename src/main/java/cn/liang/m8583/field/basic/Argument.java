package cn.liang.m8583.field.basic;

import cn.liang.m8583.field.StringField;
import cn.liang.m8583.transcoder.MessageUtil;

/**
 * 签到时，下发的参数。
 * 此类容易受需求影响而发生变化。
 * 
 * @author  Liang Yabao
 * 2012-5-2
 */
public class Argument extends StringField{
	
	private long rmbNeedCode;
	private long integralNeedCode;
	private long limitOfRecharge;
	private int expireTime;

	public Argument(){
		super(63, "下发参数", 999, 0);
	}
	
	public Argument(long rmbNeedCode, long integralNeedCode,
			long limitOfRecharge, int expireTime) {
		this();
		this.rmbNeedCode = rmbNeedCode;
		this.integralNeedCode = integralNeedCode;
		this.limitOfRecharge = limitOfRecharge;
		this.expireTime = expireTime;
	}
	
	public long getRmbNeedCode() {
		return rmbNeedCode;
	}
	/**
	 * 需要验证码的消费起始金额（RMB），单位是分，最大12位十进制数
	 * @param rmbNeedCode
	 */
	public void setRmbNeedCode(long rmbNeedCode) {
		this.rmbNeedCode = rmbNeedCode;
	}
	public long getIntegralNeedCode() {
		return integralNeedCode;
	}
	/**
	 * 需要验证码的消费起始积分（ITR），单位是分，最大12位十进制数
	 * @param integralNeedCode
	 */
	public void setIntegralNeedCode(long integralNeedCode) {
		this.integralNeedCode = integralNeedCode;
	}
	public long getLimitOfRecharge() {
		return limitOfRecharge;
	}
	/**
	 * 设置充值上限，单位是分，最大12位十进制数
	 * @param limitOfRecharge
	 */
	public void setLimitOfRecharge(long limitOfRecharge) {
		this.limitOfRecharge = limitOfRecharge;
	}
	public int getExpireTime() {
		return expireTime;
	}
	
	/**
	 * 设置验证码的有效期时间，单位为秒，最大4位十进制数。
	 * @param expireTime
	 */
	public void setExpireTime(int expireTime) {
		this.expireTime = expireTime;
	}
	
	/**
	 * 编码，使用63域用法二。
	 * @return
	 */
	public byte[] encode() {
		
		byte[] bytes = new byte[40];
		//63.1	需要验证码的消费起始金额（RMB），单位是分，最大12位十进制数
		MessageUtil.long2asc(rmbNeedCode, 12, bytes, 0);
		//63.2	需要验证码的消费起始积分（ITR），单位是分，最大12位十进制数
		MessageUtil.long2asc(integralNeedCode, 12, bytes, 12);
		//63.3	充值上限，单位是分，最大12位十进制数
		MessageUtil.long2asc(limitOfRecharge, 12, bytes, 24);
		//63.4	验证码的有效期时间，单位为秒，最大4位十进制数。
		MessageUtil.long2asc(expireTime, 4, bytes, 36);
		return bytes;
	}

	public Argument decode(byte[] bytes) {

		// 63.1 需要验证码的消费起始金额（RMB），单位是分，最大12位十进制数
		rmbNeedCode = MessageUtil.asc2long(bytes, 0, 12);
		// 63.2 需要验证码的消费起始积分（ITR），单位是分，最大12位十进制数
		integralNeedCode = MessageUtil.asc2long(bytes, 12, 12);
		// 63.3 充值上限，单位是分，最大12位十进制数
		limitOfRecharge = MessageUtil.asc2long(bytes, 24, 12);
		// 63.4 验证码的有效期时间，单位为秒，最大4位十进制数。
		expireTime = (int) MessageUtil.asc2long(bytes, 36, 4);
		return this;

	}
	
}
