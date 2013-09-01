package cn.liang.m8583.message.support;

import java.util.ArrayList;
import java.util.List;

import cn.liang.m8583.field.basic.TransactionDetail;
import cn.liang.m8583.field.basic.TransactionQuery;
import cn.liang.m8583.field.basic.TransactionRecord;
import cn.liang.m8583.message.InputMessage;
import cn.liang.m8583.message.MessageType;
import cn.liang.m8583.message.RequestResponse;
import cn.liang.m8583.transcoder.Message8583;


/**
 * 响应交易明细
 * @author 325336, Liang Yabao
 * 2012-3-20
 */
public class TransactionDetailResponse extends RequestResponse{

	public TransactionDetailResponse(MessageType mt) {
		super(mt);
		// TODO Auto-generated constructor stub
	}

	private TransactionDetail transactionDetail = TransactionDetail.EMPTY;
	private TransactionQuery transactionQuery ;
	
	public TransactionQuery getTransactionQuery() {
		return transactionQuery;
	}

	public void setTransactionQuery(TransactionQuery transactionQuery) {
		this.transactionQuery = transactionQuery;
	}

	public TransactionDetail getTransactionDetail() {
		return transactionDetail;
	}

	public void setTransactionDetail(TransactionDetail transactionDetail) {
		this.transactionDetail = transactionDetail;
	}
	
	@Override
	public Message8583 encode() {
		Message8583 m8583 = super.encode();
		m8583.setTransactionQuery(transactionQuery);
		m8583.setTransactionDetail(transactionDetail);

		// 必须在设置明细后，再设置运单列表。
		List<String> allWaybill = new ArrayList<String>();
		// 必须有顺序的存放
		for (TransactionRecord rec : transactionDetail
				.getTransactionRecordList()) {
			allWaybill.addAll(rec.getWaybillList());
		}
		m8583.setWaybill(allWaybill);
		return m8583;
	}
	
	@Override
	public void decode(Message8583 mes) {
		super.decode(mes);
		this.transactionQuery = mes.getTransactionQuery();
		this.transactionDetail = mes.getTransactionDetail();
		//必须在解码明细后，再拆分运单列表。
		transactionDetail.splitWaybill(mes.getWaybill());
	}
	
	@Override
	public boolean equals(Object obj){
		if(!super.equals(obj)||!(obj instanceof TransactionDetailResponse)){
			return false;
		}
		TransactionDetailResponse tdr = (TransactionDetailResponse)obj;
		
		return this.transactionDetail.equals(tdr.getTransactionDetail());
				
	}
	
	@Override
	public void route(InputMessage im){
		super.route(im);
		
		if(im instanceof TransactionDetailRequest){
			this.transactionQuery = ((TransactionDetailRequest)im).getTransactionQuery();
		}
		
	}
}
