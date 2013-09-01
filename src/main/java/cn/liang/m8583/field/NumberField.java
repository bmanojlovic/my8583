package cn.liang.m8583.field;

import cn.liang.m8583.transcoder.MessageUtil;

/**
 * 数字字符串，BCD压缩表示，靠左表示。元素个数为数字个数。
 * 参考卡号
 */
public abstract class NumberField extends Field {

	public NumberField(int index, String name, int maxLength, int varLength) {
		super(index, name, maxLength,varLength, DataType.NUMBER);
	}

	public void setNumber(String number) {
		// card number is bcd code,2N=1B
		if (number.length() > this.getMaxLength()) {
			throw new RuntimeException("the card number is too long!");
		}
		char[] chars = number.toCharArray();
		byte[] bytes = MessageUtil.num2bcd(chars);

		this.setData(bytes);
		this.setActLength(number.length());

	}

	public String getNumber() {
		if ((this.getActLength() + 1) / 2 != this.getData().length) {
			throw new RuntimeException("the lengthInByte is not right!");
		}
		return new String(MessageUtil.bcd2num(this.getData(), this.getActLength()));
	}

}
