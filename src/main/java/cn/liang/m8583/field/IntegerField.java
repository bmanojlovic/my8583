package cn.liang.m8583.field;


public abstract class IntegerField extends Field {

	public IntegerField(int index, String name, int maxLength) {
		super(index, name, maxLength,0, DataType.INTEGER);
		this.setActLength(maxLength);
	}
	
	public long getInteger(){
		long amount =0L;
		byte[] data = this.getData();
		int base = 1;//个位的基数为一。
		//每个字节表示两个数字，
		//右靠BCD码表示
		//从最后一个字节开始，即低位算起
		for(int i = data.length-1;i>=0;i--){
			byte b = data[i];
			//低四位，表示一个数字，乘以所在的位置基数
			amount +=(b&0x0F)*base;
			base*=10;//十进制
			//高四位
			amount +=((b&0xF0)>>4)*base;
			base*=10;//十进制
		}
		return amount;
	}
	
	public void setInteger(long amount) {
		if (amount >= 1000000000000L) {
			throw new RuntimeException("the amount is too big!");
		}

		//字段定长
		int byteLen = (this.getMaxLength()+1)/2;
		byte[] bytes = new byte[byteLen];
		int base = 10;//十进制
		
		for (int i = byteLen-1; amount > 0 && i >= 0; i--) {

			// low bits
			bytes[i] |= (amount % base);//BCD形式表示低四位
			amount /= base;
			// high bits
			bytes[i] |= ((amount % base) << 4);//BCD形式表示高四位
			amount /= base;
		}
		
		this.setData(bytes);

		
	}
}
