package cn.liang.m8583.message;

import cn.liang.m8583.field.basic.Operator;

/**
 * @author  Liang Yabao
 * 2012-3-09
 * 输入类报文，即通常的pos发给系统的报文
 */
public abstract class InputMessage extends Message{

	public InputMessage(MessageType mt) {
		super(mt);
	}

	/**
	 * 操作员，必填。
	 * 
	 */
	private Operator operator;


	public Operator getOperator() {
		return operator;
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
