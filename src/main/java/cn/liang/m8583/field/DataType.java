package cn.liang.m8583.field;

public enum DataType{
	
	/**
	 * ASCII字符，一个字节表示一个ASCII字符，元素个数以字节数计算
	 */
	STRING(0),
	
	/**
	 * 整型数据，BCD压缩表示，靠右表示。元素个数为数字个数。
	 */
	INTEGER(1),
	
	/**
	 * 二进制数据，元素个数以字节数计算。
	 */
	BINARY(2),
	
	/**
	 * 数字字符串，BCD压缩表示，靠左表示。元素个数为数字个数。
	 */
	NUMBER(3);
	
	private DataType(int flag){
		this.flag = flag;
	}
	private int flag = -1;
	
	public int getFlag(){
		return flag;
	}
}