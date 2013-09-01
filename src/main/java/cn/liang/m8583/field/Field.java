package cn.liang.m8583.field;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.liang.m8583.transcoder.MessageUtil;
import cn.liang.m8583.transcoder.exception.FieldOverflowException;



/**
 * 8583报文的字段。在切分、组装、翻译报文时用到此类。
 * @author 325336, Liang Yabao
 * 2012-3-14
 */
public class Field {

	private static final Logger logger = LoggerFactory.getLogger(Field.class);
	/**
	 * 定义 
	 */
	public static final int VL0=0;
	/**
	 * 2位变长
	 */
	public static final int VL2=2;
	/**
	 * 3位变长
	 */
	public static final int VL3 =3;
	
	// 域索引
	private int index;
	// 域名
	private final String name;
	// 数据域长度，字节数
	private final int maxLength;
	// 几位变长
	private final int varLength;

	// 0 -- string, 1 -- int, 2 -- binary
	private final DataType dataType;
	
	// 实际长度，字节数（变长时使用）
	private int actLength;
	//存放具体值
	private byte[] data;

	// 保留
	private int attribute;
	
	private Field(int bitFlag,String name,int length,int lengthInByte,int variableFlag,DataType dataType,byte[] data,int attribute){
		this.index = bitFlag;
		this.name = name;
		this.maxLength = length;
		this.actLength = lengthInByte;
		this.varLength = variableFlag;
		this.dataType = dataType;
		this.data = data;
		this.attribute = attribute;
	}
	
	/**
	 * 
	 * @param name		字段名称
	 * @param length	定长或者最大长度
	 * @param size		实际长度
	 * @param variableFlag	几位变长
	 * @param dataType	数据类型
	 */
	private Field(String name,int length,int size,int variableFlag,DataType dataType){
		this(0, name, length, size, variableFlag, dataType,null,0);
	}

	/**
	 * 
	 * @param name		字段名称
	 * @param length	定长或者最大长度
	 * @param variableFlag	几位变长
	 * @param dataType	数据类型
	 */
	public Field(String name,int length,int variableFlag,DataType dataType){
		this(0, name, length, 0, variableFlag, dataType,null,0);
	}
	
	protected Field(int index,String name,int maxLength,int varLength,DataType dataType){
		this(index, name, maxLength, 0,varLength,  dataType,null,0);
	}
	
	public DataType dataType(){
		return dataType;
	}

	public String getName() {
		return name;
	}

	/**
	 * 字段是几位变长
	 * @return
	 */
	public int getVarLength() {
		return varLength;
	}

	/**
	 * 字段的定长或者最大长度
	 * @return
	 */
	public int getMaxLength() {
		return maxLength;
	}

	public byte[] getData() {
		return data;
	}

	protected void setData(byte[] data) {
		//字节个数不可以比元素个数更大
		if(data.length>this.getMaxLength()){
			throw new FieldOverflowException(this);
		}
		this.data = data;
	}
	
	/**
	 * 字段为变长时的实际元素个数，除BCD码外，表示字节数
	 * @return
	 */
	public int getActLength() {
		return actLength;
	}

	/**
	 * 设置字段的实际元素个数，除BCD码外，表示字节数
	 * @param size
	 */
	protected void setActLength(int size) {
		this.actLength = size;
	}

	/**
	 * 从字节数组形态的报文中读取当前字段的数据
	 * @param bytes 字节数组形态的报文
	 * @param cur 读取的起始位置
	 * @return
	 */
	public int read(byte[] bytes,int cur){
		//设置字段的实际元素个数（长度）。
		switch (this.getVarLength()) {
		case 0:
			
			this.setActLength(this.getMaxLength());
			break;
		case 2:
			//压缩BCD码表示长度
			byte b2 = bytes[cur++];
			int len2 = ((b2 & 0xF0) >> 4) * 10 + (b2 & 0x0F);
			
			this.setActLength(len2);
			break;
		case 3:
			int len3 = bytes[cur++] * 100;
			byte b3 = bytes[cur++];
			len3 += ((b3 & 0xF0) >> 4) * 10 + (b3 & 0x0F);
			
			this.setActLength(len3);
			break;
		default:
			throw new RuntimeException(
					"no this field for variable length !");
		}
		//计算字段数据的实际字节长度
		int byteLength = 0;
		if (this.dataType() == DataType.NUMBER
				|| this.dataType() == DataType.INTEGER) {
			byteLength = (this.getActLength() + 1) / 2;
		}else{
			byteLength = this.getActLength();
		}
		//读取字段数据内容
		this.setData(new byte[byteLength]);
		System.arraycopy(bytes, cur, this.getData(), 0,
				byteLength);
		cur += byteLength;

		if (logger.isDebugEnabled()) {
			logger.debug("[{}]size:{}", this.getName(), this.getActLength());
			logger.debug("[{}]read:{}", this.getName(),
					new String(MessageUtil.byte2hex(this.getData())));
		}
		
		return cur;
	}
	
	/**
	 * 将当前字段的数据写到字节数组形态的报文中
	 * @param bytes	字节数组形态的报文
	 * @param cur 写入的起始位置
	 * @return
	 */
	public int write(byte[] bytes,int cur){
		switch (this.getVarLength()) {
		
		case 0:
			System.arraycopy(this.getData(), 0, bytes, cur, this.getData().length);
			cur+=this.getData().length;
			break;
		case 2:

			int len = this.getActLength();
			int low = len%10;
			int high = len/10;
			bytes[cur] = (byte)low;
			bytes[cur] |=high<<4;
			cur++;

			System.arraycopy(this.getData(), 0, bytes, cur, this.getData().length);
			cur+=(this.getData().length);
			break;
		case 3:

			len = this.getActLength();
			int shift = MessageUtil.long2bcd(len, 3, bytes, cur);
			cur+=shift;
			System.arraycopy(this.getData(), 0, bytes, cur, this.getData().length);
			cur+=(this.getData().length);
			break;
		default:
			throw new RuntimeException(
					"no this field for variable length !");
		}
		
		logger.debug("[{}]write:{}", this.getName(),
				new String(MessageUtil.byte2hex(this.getData())));	
		
		return cur;
	}
	
	
	public static final Field NO_USE_999 = new Field("NO USE ",999, 0, 3, DataType.STRING);
	public static final Field NO_USE = NO_USE_999;
	public static final Field NO_USE_3 = new Field("NO USE ",3, 0, 0, DataType.STRING);
	public static final Field NO_USE_8 = new Field("NO USE ",8, 0, 0, DataType.STRING);
	public static final Field NO_USE_12 = new Field("NO USE ",12, 0, 0, DataType.STRING);
	//public static final Field NO_USE_12 = new Field("NO USE ",12, 0, 0, DataType.STRING);
}
