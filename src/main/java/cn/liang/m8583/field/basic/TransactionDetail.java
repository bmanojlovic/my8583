package cn.liang.m8583.field.basic;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.liang.m8583.transcoder.MessageUtil;

/**
 * 交易明细
 * @author 325336, Liang Yabao
 * 2012-3-20
 */
public class TransactionDetail {
	
	private static final Logger logger = LoggerFactory.getLogger(TransactionDetail.class);
	
	//总记录数
	private int totalRecord =0 ;
	
	//从第几条记录开始
	private int offset =0;

	//页内记录
	private List<TransactionRecord> list = Collections.EMPTY_LIST;

	public static final TransactionDetail EMPTY = new TransactionDetail(0, 0,
			Collections.EMPTY_LIST);

	public TransactionDetail() {
	}

	public TransactionDetail(int totalRecord, int offset,
			List<TransactionRecord> list) {
		this.totalRecord = totalRecord;
		this.offset = offset;
		this.list = list;
	}
	
	public int getTotalRecord() {
		return totalRecord;
	}

	/**
	 * 设置总记录数
	 * @param totalRecord 小于1000
	 */
	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
	}

	public int getOffset() {
		return offset;
	}

	/**
	 * 设置从第几条记录开始
	 * @param offset 小于1000
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}

	public List<TransactionRecord> getTransactionRecordList() {
		return list;
	}

	public void setTransactionRecordList(List<TransactionRecord> list) {
		this.list = list;
	}
	
	public byte[] encode(){
		byte[] trb = MessageUtil.int2asc(totalRecord, 3);
		byte[] osb = MessageUtil.int2asc(offset, 3);
		byte[] lsb = MessageUtil.int2asc(list.size(), 2);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			//总记录数
			baos.write(trb);
			//从第几条记录开始
			baos.write(osb);
			//返回的记录规模
			baos.write(lsb);
			//返回的记录
			for(TransactionRecord tr: list){
				byte[] trbs = tr.encode();
				//记录的长度
				baos.write(MessageUtil.int2asc(trbs.length, 3));
				//记录编码
				baos.write(trbs);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		byte[] bytes = baos.toByteArray();
		if (logger.isDebugEnabled()) {
			//logger.debug("[json]{}", JSONUtils.fromObject(this));
			logger.debug("[byte]{}", new String(MessageUtil.byte2hex(bytes)));
		}
		return bytes;
	}
	
	public void decode(byte[] bytes){
		this.totalRecord = (int) MessageUtil.asc2long(bytes, 0, 3);
		this.offset = (int) MessageUtil.asc2long(bytes, 3, 3);
		int size = (int) MessageUtil.asc2long(bytes, 6, 2);
		int cur =8;
		this.list = new ArrayList<TransactionRecord>(size);
		for(int i =0;i<size;i++){
			int len = (int) MessageUtil.asc2long(bytes, cur, 3);
			cur+=3;
			byte[] tr = new byte[len];
			System.arraycopy(bytes, cur, tr, 0, len);
			if (logger.isDebugEnabled()) {
				logger.debug("TransactionRecord length:{},content:{}", len,
						new String(MessageUtil.byte2hex(tr)));
			}
			list.add(new TransactionRecord().decode(tr));
			cur+=len;
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("[byte]{}", new String(MessageUtil.byte2hex(bytes)));
			//logger.debug("[json]{}", JSONUtils.fromObject(this));
		}
	}
	
	/**
	 * 在解码后，拆分运单号
	 * 
	 * @param waybillList
	 */
	public void splitWaybill(List<String> waybillList) {
		logger.debug("waybillList's size is {}" , waybillList.size());	
		Iterator<String> it = waybillList.iterator();
		for (TransactionRecord rec : list) {
			rec.splitWaybill(it);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof TransactionDetail)) {
			return false;
		}
		TransactionDetail aa = (TransactionDetail) obj;
		return aa.getTotalRecord()==this.totalRecord
				&& aa.getOffset()== this.offset
				&& aa.getTransactionRecordList().equals(list);

	}
}
