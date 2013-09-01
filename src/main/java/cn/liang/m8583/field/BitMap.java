package cn.liang.m8583.field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 位图，我们系统只用64位，内部以长整型表示。
 * 位图是8583报文的灵魂所在，报文的切分与组装都必须。
 * @author 325336, Liang Yabao
 * 2012-3-12
 */
public class BitMap extends BinaryField{

	private static final Logger logger = LoggerFactory.getLogger(BitMap.class);
	
	//掩码，与位运算，用于求 解最高位是否为1
	private static final long mask = 1L<<63;
	public static final int LENGTH_IN_BYTE = 8;

	//位图的内部表示，有利于设置与求解存在字段的索引
	private long bitmap;

	public BitMap() {
		super(1,"BIT MAP ", 8, 0);
	}
	
	/**
	 * 8个字节的数组构造成位图
	 * @param bytes
	 */
	public BitMap(byte[] bytes) {
		this();
		this.bitmap = bitmap(bytes);
		//System.out.println("bitmap: "+ bitmap);
	}

	private static long bitmap(byte[] bytes) {
		
		long bitmap = 0L;
		for (int i = 0; i < 8; i++) {
			bitmap <<= 8;
			bitmap |= (bytes[i]&0xFF);
		}
		return bitmap;
	}

	/**
	 * 位图的长整型表示方式
	 * @return
	 */
	public long longValue() {
		return bitmap;
	}
	
	/**
	 * 位图的字节数组表示形式
	 * @return 长度为8的字节数组
	 */
	public byte[] getBytes(){
		byte[] bytes= new byte[8];
		long bm = bitmap;
		for (int i = 7; i >= 0; i--) {
			bytes[i] = (byte) (bm & 0xFF);
			bm>>=8;
		}
		return bytes;
	}
	/**
	 * 从左到右，index从0开始。
	 * @deprecated
	 * @param bitmap
	 * @param index
	 * @return
	 */
	public static boolean exist(byte[] bitmap,int index){
		byte bit = bitmap[index/8];
		int pos = (index % 8);
		boolean existFlag =( bit &(0x80>>pos)) >0;
		
		return existFlag;
	}
	
	/**
	 * 设置某个字段的存在
	 * @param index 0至63的整数，超出此范围将抛出运行时异常。
	 */
	public void setExistField(int index){
		if(index>63||index<0){
			throw new RuntimeException("do no exist field index: "+ index);
		}
		long mask = 1L<<(63-index);
		bitmap |=mask;
	}
	
	
	/**
	 * 所有存在的字段索引
	 * @return 所有存在的字段索引
	 */
	public int[] exist() {
		int[] exist = new int[64];
		int count=0; 
		long bm = bitmap;
		int index =0;
		//第0个字段，我们没有用到，不考虑。
		while((bm <<= 1) != 0   ){
			index++;
			if((bm & mask) == mask){
				exist[count++]=index;
			}
		}
		int[] tmp = new int[count];
		System.arraycopy(exist, 0, tmp, 0, count);
		logger.debug("bitmap exist :{}",tmp);
		return tmp;
	}
	
	/**
	 * 是否存在指定索引位置的字段
	 * @param index	字段索引位置
	 * @return
	 */
	public boolean exist(int index){
		long mask = 1L<<(63-index);
		return (bitmap & mask)>0;
	}
	
	public int read(byte[] bytes,int cur){
		int curr = super.read(bytes, cur);
		this.bitmap(this.getData());
		return curr;
	}
	
}
