package cn.liang.m8583.message;

import java.util.Date;

import cn.liang.m8583.transcoder.Message8583;

/**
 * @author 325336, Liang Yabao
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
		// TODO Auto-generated constructor stub
	}
	private Date dateTime = new Date();
	private String responseCode;

	public Date getDateTime() {
		return dateTime;
	}


	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}


	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseCode() {
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
	
	
	@Override
	public Message8583 encode() {
		Message8583 mes = super.encode();
		mes.setResponseCode(responseCode);
		mes.setTransactionDateTime( dateTime);
		
		return mes;
	}


	@Override
	public void decode(Message8583 mes) {
		super.decode(mes);
		this.dateTime = mes.getTransactionDateTime();
		this.responseCode = mes.getResponseCode();
		
	}
	@Override
	public boolean equals(Object obj){
		if(!super.equals(obj) ||!(obj instanceof OutputMessage)){
			return false;
		}
		OutputMessage om = (OutputMessage) obj;
		return this.dateTime.toString().equals(om.getDateTime().toString())
				&& this.responseCode.equals(om.getResponseCode());
	}


}
