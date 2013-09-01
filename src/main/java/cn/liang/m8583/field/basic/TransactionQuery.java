package cn.liang.m8583.field.basic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.liang.m8583.transcoder.MessageUtil;

/**
 * 交易记录查询
 * @author 325336, Liang Yabao
 * 2012-3-22
 */
public class TransactionQuery {
	
	private static final Logger logger = LoggerFactory.getLogger(TransactionQuery.class);
	
	//查询的交易类型
	private int type;
	//从第几条记录开始
	private int offset;
	//要求查询的交易笔数
	private int count;
	
	public TransactionQuery(){}
	
	public TransactionQuery(int type,int offset, int count){
		this.count = count;
		this.type = type;
		this.offset = offset;
	}
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int pageSize) {
		this.count = pageSize;
	}

	public byte[] encode(){
		byte[] bytes = new byte[7];
		MessageUtil.long2asc(type, 2, bytes, 0);
		MessageUtil.long2asc(offset, 3, bytes, 2);
		MessageUtil.long2asc(count, 2, bytes, 5);
		
		if (logger.isDebugEnabled()) {
			//logger.debug("[json]{}", JSONUtils.fromObject(this));
			logger.debug("[byte]{}", new String(MessageUtil.byte2hex(bytes)));
		}
		return bytes;
	}
	
	public void decode(byte[] bytes){
		this.type = (int) MessageUtil.asc2long(bytes, 0, 2);
		this.offset = (int) MessageUtil.asc2long(bytes, 2, 3);
		this.count = (int) MessageUtil.asc2long(bytes, 5, 2);
		
		if (logger.isDebugEnabled()) {
			logger.debug("[byte]{}", new String(MessageUtil.byte2hex(bytes)));
			//logger.debug("[json]{}", JSONUtils.fromObject(this));
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof TransactionQuery)) {
			return false;
		}
		TransactionQuery tq = (TransactionQuery) obj;
		return tq.count== this.count
				&& this.offset == tq.offset
				&& this.type == tq.type;
	}
	
}
