package cn.liang.m8583.transcoder;

import cn.liang.m8583.message.support.BalanceRequest;
import cn.liang.m8583.message.support.BalanceResponse;
import cn.liang.m8583.message.support.PinRequest;
import cn.liang.m8583.message.support.PinResponse;
import cn.liang.m8583.message.support.PurchaseCancellationRequest;
import cn.liang.m8583.message.support.PurchaseCancellationResponse;
import cn.liang.m8583.message.support.PurchaseRequest;
import cn.liang.m8583.message.support.PurchaseResponse;
import cn.liang.m8583.message.support.RechargeCancellationRequest;
import cn.liang.m8583.message.support.RechargeCancellationResponse;
import cn.liang.m8583.message.support.RechargeRequest;
import cn.liang.m8583.message.support.RechargeResponse;
import cn.liang.m8583.message.support.ReverseRequest;
import cn.liang.m8583.message.support.ReverseResponse;
import cn.liang.m8583.message.support.TransactionDetailRequest;
import cn.liang.m8583.message.support.TransactionDetailResponse;
import cn.liang.m8583.message.support.WorkingKeyRequest;
import cn.liang.m8583.message.support.WorkingKeyResponse;

/**
 * 报文引擎默认实现
 * @author 325336, Liang Yabao
 * 2012-3-22
 */
public class DefaultTranscoder extends TranscoderImpl{

	/**
	 * 
	 * @param finder	工作密钥查找器
	 * @param encryptor	加密机
	 */
	public DefaultTranscoder(){

		
		//消费
		this.accept("7200", "000000", PurchaseRequest.class);
		this.accept("7210", "000000", PurchaseResponse.class);
		
		//充值
		this.accept("7200", "210000", RechargeRequest.class);
		this.accept("7210", "210000", RechargeResponse.class);
		
		//余额查询
		this.accept("7100", "310000", BalanceRequest.class);
		this.accept("7110", "310000", BalanceResponse.class);
		
		//消费撤销
		this.accept("7200", "200000", PurchaseCancellationRequest.class);
		this.accept("7210", "200000", PurchaseCancellationResponse.class);
		
		//充值撤销
		this.accept("7200", "170000", RechargeCancellationRequest.class);
		this.accept("7210", "170000", RechargeCancellationResponse.class);

		
		//冲正交易的处理码与冲正的原始交易相同，所以processCode是实例成员，不是静态成员
		//冲正交易,消费
		this.accept("7400", "000000", ReverseRequest.class);
		this.accept("7410", "000000", ReverseResponse.class);
		//冲正交易,消费撤销
		this.accept("7400", "200000", ReverseRequest.class);
		this.accept("7410", "200000", ReverseResponse.class);
		//冲正交易,充值
		this.accept("7400", "210000", ReverseRequest.class);
		this.accept("7410", "210000", ReverseResponse.class);
		//冲正交易,充值撤销
		this.accept("7400", "170000", ReverseRequest.class);
		this.accept("7410", "170000", ReverseResponse.class);
		
		//交易明细
		this.accept("7100", "380000", TransactionDetailRequest.class);
		this.accept("7110", "380000", TransactionDetailResponse.class);
		
		//同步密钥
		this.accept("7800", "790000", WorkingKeyRequest.class);
		this.accept("7810", "790000", WorkingKeyResponse.class);
		
		//修改个人密码
		this.accept("7800", "700000", PinRequest.class);
		this.accept("7810", "700000", PinResponse.class);
		

	}
	
}
