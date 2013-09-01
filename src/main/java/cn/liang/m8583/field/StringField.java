package cn.liang.m8583.field;

import cn.liang.m8583.transcoder.MessageUtil;

public abstract class StringField extends Field {

	public StringField(int index, String name, int maxLength, int varLength) {
		super(index, name, maxLength, varLength, DataType.ASCII);
	}

	public void setAscii(String tid) {
		// TODO 分定长与变长
		if (tid.length() != this.getMaxLength()) {
			throw new RuntimeException("the terminal id's length should be "
					+ this.getMaxLength() + "!");
		}
		byte[] data = new byte[this.getMaxLength()];
		byte[] bytes = MessageUtil.str2asc(tid);
		System.arraycopy(bytes, 0, data, 0, bytes.length);
		this.setData(data);

	}

	public String getAscii() {
		// TODO 分定长与变长
		if (this.getMaxLength() != this.getData().length) {
			throw new RuntimeException("the lengthInByte is not right!");
		}
		return MessageUtil.asc2str(this.getData());
	}
}
