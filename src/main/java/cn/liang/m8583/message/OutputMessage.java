package cn.liang.m8583.message;

import cn.liang.m8583.field.basic.ResponseCode;
import cn.liang.m8583.field.basic.TransactionDate;
import cn.liang.m8583.field.basic.TransactionTime;

/**
 * @author  Liang Yabao
 * 2012-3-12
 * 输出类报文，即系统发给pos的报文。
 * 在响应码为00情况下的约定：
 * 1、encode中不检查null值的所有属性都应该设置，如果不设置，则在encode时抛出异常。
 * 2、encode中检查null值的属性是可选的设置
 * 在响应码不为00情况下的约定：
 * 1、如果某些字段应该出现，则可以是默认值
 * 2、如果某些字段不应该出现，则没必要有默认值，需在encode中检查null，不应写进Message8583里。
 * 3、如果某些字段（如responseCode）必须出现，则不应有默认值，不必检查null，让其在encode过程中抛出异常。
 */
public abstract class OutputMessage extends Message{

	public OutputMessage(MessageType mt) {
		super(mt);

	}

	private TransactionDate localDate = new TransactionDate();
	private TransactionTime localTime = new TransactionTime();
	private ResponseCode responseCode;

	public TransactionDate getLocalDate() {
		return localDate;
	}

	public TransactionTime getLocalTime() {
		return localTime;
	}



	public ResponseCode getResponseCode() {
		return responseCode;
	}

	/**
	 * 自动从请求的报文里寻找相应的路由信息，填充到响应报文里。
	 * 路由信息，这里是指节点标识，终端标识，pos流水号
	 * @param im 请求报文
	 */
	public void route(InputMessage im){
		this.setNodeId(im.getNodeId());
		this.setTerminalId(im.getTerminalId());
		this.setTtc(im.getTtc());
	}
	



}
