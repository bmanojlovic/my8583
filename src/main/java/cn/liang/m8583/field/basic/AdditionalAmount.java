package cn.liang.m8583.field.basic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.liang.m8583.transcoder.MessageUtil;

/**
 * 附加金额（除了交易金额以外的金额信息）
 * 
1-2：账户类型
3-4：金额类型
5-7：币种
8：'D'（现在都是借记）
9-20：12个数字，左填0。
 *
 * @author  Liang Yabao
 * 2012-3-20
 */
public class AdditionalAmount {
	public static final int SIZE = 20;
	private static final byte AMOUNT_SIGN  = 'D';
	
	private static final Logger logger = LoggerFactory.getLogger(AdditionalAmount.class);
	
	
	public  String accountType;
	public   String amountType;
	public final  CurrencyCode currencyCode = new CurrencyCode();
	public final  Amount amount = new Amount(-1,"AdditionalAmount");
	

	/**
	 * 
	 * @param accountType 10：储值卡,20：虚拟卡,60：积分账户
	 * @param amountType 01，表示账户余额；60，表示积分余额；61，本次交易赠送积分；
	 * @param currencyCode 币种CNY，人民币；币种ITR，积分（自定义）；
	 * @param amount
	 */
	public AdditionalAmount(String accountType,String amountType, String currencyCode, long amount) {
		this.accountType = accountType;
		this.amountType = amountType;
		this.currencyCode.setAscii(currencyCode);
		this.amount.setInteger(amount);
	}

	
	public byte[] encode() {
		if(accountType.length()!=2){
			throw new RuntimeException("AdditionalAmount's account type must be two digits!");
		}
		if(amountType.length()!=2){
			throw new RuntimeException("AdditionalAmount's amount type must be two digits!");
		}
		
		byte[] bytes = new byte[SIZE];
		

		//账户类型
		System.arraycopy(MessageUtil.str2asc(accountType), 0, bytes, 0, 2);
		//金额类型
		byte[] at = MessageUtil.str2asc(amountType);
		System.arraycopy(at, 0, bytes, 2, at.length);
		//币种
		currencyCode.write(bytes, 4);
		
		//记账符号
		bytes[7] = AMOUNT_SIGN;
		//金额 
		amount.write(bytes, 8);
		return bytes;
	}
	
	public AdditionalAmount decode(byte[] bytes) {
		//账户类型0-1
		byte[] act = new byte[2];
		System.arraycopy(bytes, 0, act, 0, 2);
		this.accountType = MessageUtil.asc2str(act);
		//金额类型2-3
		byte[] amt = new byte[2];
		System.arraycopy(bytes, 2, amt, 0, 2);
		this.amountType = MessageUtil.asc2str(amt);
		//币种 4-6
		byte[] cc = new byte[3];
		System.arraycopy(bytes, 4, cc, 0, 3);
		this.currencyCode.read(cc, 0);
		//byte[] ambs = new byte[12];
		//System.arraycopy(bytes, 8, ambs, 0, 12);
		//this.amount = MessageUtil.asc2int(ambs);
		this.amount.read(bytes,8);
		return this;
	}
	

}