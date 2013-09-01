package cn.liang.m8583.message;

import cn.liang.m8583.transcoder.Message8583;

/**
 * @author 325336, Liang Yabao
 * 2012-3-09
 * 输入类报文，即通常的pos发给系统的报文
 */
public abstract class InputMessage extends Message{

	public InputMessage(MessageType mt) {
		super(mt);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 操作员，必填。
	 * 针对顺丰业务的特殊字段。
	 */
	private String operator;


	public String getOperator() {
		return operator;
	}

	

	public void setOperator(String operator) {
		this.operator = operator;
	}


	@Override
	public void decode(Message8583 mes) {
		super.decode(mes);
		this.operator = mes.getOperator();
	}	

	@Override
	public Message8583 encode() {
		Message8583 mes = super.encode();
		mes.setOperator(this.operator);
		return mes;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj) ||!(obj instanceof InputMessage)) {
			return false;
		}
		InputMessage im = (InputMessage) obj;
		return im.getOperator().equals(operator);
	}
}
