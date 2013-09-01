package cn.liang.m8583.field;

public abstract class BinaryField extends Field {

	/**
	 * 
	 * @param index		在协议位域中的索引位置
	 * @param name		此字段的名字
	 * @param maxLength	表示最大的字节个数
	 * @param varLength 用多少位十进制数字表示可变的长度。
	 */
	public BinaryField(int index,String name,int maxLength,int varLength){
		super(index,name,maxLength,varLength,DataType.BINARY);
	}
	
	public void setData(byte[] data){
		if(this.getVarLength()>0 && data.length>this.getMaxLength()){
			throw new RuntimeException("二进制字段数据长度不符合要求！");
		}
		if(this.getVarLength()==0 && data.length!=this.getMaxLength()){
			throw new RuntimeException("二进制字段数据长度不符合要求！");
		}
		this.setActLength(data.length);
		this.setData(data);
	}
	
	public byte[] getData(){
		return this.getData();
	}
}
