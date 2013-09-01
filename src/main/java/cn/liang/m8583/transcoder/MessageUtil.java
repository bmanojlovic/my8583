package cn.liang.m8583.transcoder;

/**
 * 8583报文的工具类，基本上都是静态方法。
 * 
 * 缩写约定：
 * hex:16进制字符。char字符，表示'0'-'9','a'-'f','A'-'F'
 * num:号码。char[];char字符，表示'0123456789'
 * digit:数字。char字符，表示'0'-'9'
 * str:字符串。java的String类，UTF16编码。
 * bcd:BCD码。byte[];一个字节表示两个BCD元素，每个元素表示0-9，即0000至1001
 * asc:ascii字符（串）。byte[];一个字节表示一个ascii字符。
 * 
 * @author  Liang Yabao
 * 2012-3-14
 */
public class MessageUtil {

	private static final int MAX_MESSAGE_SIZE = 65536;
	
	//表示16进制的字符数组。
	private static final char[] hex={
		'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'
	};

	/**
	 * 翻译成二进制整型整数。
	 * @param array 字节数组，每一个字节都是表示'0'至'9'。
	 * @return 二进制整型整数
	 */
	public static long asc2int(byte[] array){
		return asc2long(array,0,array.length);
	}
	
	
	/**
	 * 
	 * @param bytes 字节数组，每一个字节都是表示'0'至'9'。
	 * @param offset	源字节数组开始转换的偏移量
	 * @param len	要转换的字节长度。
	 * @return
	 */
	public static long asc2long(byte[] bytes, int offset, int len) {

		// 48是ascii码中'0'对应的整数，8583中字段长度以十进制表示
		long value = 0;
		if (len == 2) {
			value = (bytes[offset++] - 48) * 10 + (bytes[offset++] - 48);
			return value;
		} else if (len == 3) {
			value = (bytes[offset++] - 48) * 100 + (bytes[offset++] - 48)
					* 10 + (bytes[offset++] - 48);
			return value;
		} else {	
			long base = 1;
			for (int i = offset+len - 1; i >= offset; i--) {
				value += (bytes[i] - 48) * base;
				base *= 10;
			}
			return value;
		}
	}
	
	
	/**
	 * 左靠BCD格式的数字转换为Unicode字符数组。
	 * @param bytes 每一个字节表示两个十进制数字，左靠
	 * @return	号码，左靠表示
	 */
	public static char[] bcd2num(byte[] bytes,int count){
		char[] chars= new char[count];

		for (int i = 0; i < bytes.length; i++) {
			
			byte b = bytes[i];
			//高位字符
			chars[i * 2] = (char) (((b & 0xF0) >> 4) + 48);
			// 低位字符
			int index = i * 2 + 1;
			if (index < count) {
				chars[index] = (char) ((b & 0x0F) + 48);
			}
		}
		return chars;
	}

	/**
	 * 转换为BCD格式。
	 * 左靠BCD码表示
	 * @param chars 	号码，左靠表示，Unicode字符数组
	 * @return 一个字节表示两个十进制整数
	 */
	public static byte[] num2bcd(char[] chars){
		byte[] bytes = new byte[(chars.length+1)/2];
		for(int i =0;i<chars.length/2;i++){
			char hign= chars[i*2];
			char low = chars[i*2+1];
			if(low>'9'||low<'0'||hign>'9'||hign<'0'){
				throw new RuntimeException("the char can not convert to BCD number: "+low+" , "+hign);
			}
			bytes[i]|=(low-48);
			bytes[i]|=((hign-48)<<4);
		}
		//当字符个数为奇数时，处理最后一个字符
		if(chars.length%2==1){
			char hign= chars[chars.length-1];
			if(hign>'9'||hign<'0'){
				throw new RuntimeException("the char can not convert to BCD number: "+hign);
			}
			bytes[bytes.length-1]|=((hign-48)<<4);
		}
		return bytes;
	}
	
	/**
	 * ascii字符数组转换为unicode字符串
	 * @param bytes 每一个字节表示一个ascii字符。
	 * @return
	 */
	public static String asc2str(byte[] bytes){
		char[] chars = new char[bytes.length];
		for(int i =0;i<chars.length;i++){
			chars[i] = (char) bytes[i];
		}
		return new String(chars);
	}

	/**
	 * ascii字符数组转换为unicode字符串
	 * @param bytes ascii字符数组
	 * @param offset 转换的起始位置
	 * @param length 转换的长度
	 * @return	字符串
	 */
	public static String asc2str(byte[] bytes,int offset,int length){
		char[] chars = new char[length];
		for(int i =0;i<chars.length;i++){
			chars[i] = (char) bytes[i+offset];
		}
		return new String(chars);
	}
	
	/**
	 * unicode字符串转换成ascii字符数组
	 * @param str unicode字符串
	 * @return ascii字符数组，每一个字节表示一个ascii字符。
	 */
	public static byte[] str2asc(String str){
		byte[] bytes = new byte[str.length()];
		for(int i =0;i<bytes.length;i++){
			bytes[i] = (byte) str.charAt(i);
		}
		return bytes;
	}
	
	/**
	 * 一个ascii字符数字转换成对应的整型。
	 * @param c
	 * @return
	 */
	public static int hex2int(char c){
		if(c<='9' && c>='0'){
			return c-48;
		}else if(c<='f' && c>='a'){
			return c-97+10;
		}else if(c<='F' && c>='A'){
			return c-65+10;
		}else{
			throw new RuntimeException("the char is not hex: "+c);
		}
	}
	

	/**
	 * 
	 * @param value	要转换成ascii表示的整型整数
	 * @param len 字符串长度
	 * @return	ascii字符数组
	 */
	public static byte[] int2asc(int value, int len) {
		byte[] bytes = new byte[len];
		//len表示未填充字节数。
		while ( len>0) {
			bytes[--len] = (byte) ((value % 10) + 48);// 从个位开始
			value /= 10;
		}
		
		return bytes;
	}
	
	/**
	 * 右靠BCD码表示，左补0
	 * @param lv 长整型整数
	 * @param digit	位数
	 * @return	BCD码表示的字节数组
	 */
	public static  byte[] long2bcd(long lv,int digit){
		byte[] bytes = new byte[(digit+1)/2];
		for(int i = bytes.length-1;i>=0 && lv>0;i--){
			//低位数字
			bytes[i] = (byte)(lv%10);
			lv/=10;
			//高位数字
			bytes[i] |=(lv%10)<<4;
			lv/=10;
		}
		return bytes;
	}
	
	/**
	 * 长整型转换成BCD码表示
	 * @param lv	被转换的长整型整数
	 * @param digit	需被转换的位数，如long 为320，digit为2,则百位的3不会转换。
	 * @param bytes	转换后存放的字节数组
	 * @param offset	转换后存放的字节数组起始存放位置
	 * @return	转换的字节数
	 */
	public static int long2bcd(long lv,int digit,byte[] bytes,int offset){
		int shift = (digit+1)/2;
		for(int i = offset+shift-1;i>=offset && lv>0;i--){
			//低位数字
			bytes[i] = (byte)(lv%10);
			lv/=10;
			//高位数字
			bytes[i] |=(lv%10)<<4;
			lv/=10;
		}
		//数字位数为奇数的情况，高四位不会被填充
		if(digit%2!=0){
			bytes[offset]&=0x0F;
		}
		return shift;
	}
	
	/**
	 * 长整型数据转码成ascii表示
	 * @param lv 长整型数据
	 * @param digit ascii字符个数
	 * @param bytes 目标字节数组
	 * @param offset 目标数组偏移量
	 * @return 实际偏移量。
	 */
	public static int long2asc(long lv,int digit,byte[] bytes,int offset){

		for(int i = offset+digit-1;i>=offset;i--){
			
			bytes[i] = (byte)(lv%10+48);
			lv/=10;
			
		}
		
		return digit;
	}
	
	/**
	 * HEX字符串转换为字节数组
	 * @param hex	16进制字符串，可有空格字符
	 * @return	字节表示形式
	 */
	public static byte[] hex_2_byte(String hex){
		return hex2byte(trimSpace(hex).toCharArray());
	}
	
	/**
	 * 去除空格
	 * @param hex
	 * @return
	 */
	public static String trimSpace(String hex) {
		StringBuilder builder = new StringBuilder();
		for (char c : hex.toCharArray()) {
			if (c != ' ') {
				builder.append(c);
			}
		}
		return builder.toString();
	}
	
	/**
	 * 每两个字符转换成一个字节
	 * @param hex	16进制数字字符数组，无空格
	 * @return	字节数组
	 */
	public static byte[] hex2byte(char[] hex){
		if(hex.length%2!=0){
			throw new RuntimeException("hex to bytes ,should be even numbers");
		}
		byte[] bytes = new byte[hex.length/2];
		for (int i = 0; i < bytes.length; i++) {

			bytes[i] = (byte) (MessageUtil.hex2int(hex[i*2]) << 4);

			bytes[i] |= (byte) (MessageUtil.hex2int(hex[i*2+1] ));
			
		}
		
		return bytes;
	}
	
	/**
	 * 字节转换成16进制数字
	 * @param bytes
	 * @return
	 */
	public static char[] byte2hex(byte[] bytes) {
		char[] chars = new char[bytes.length * 2];//对应的一般应该有16个字符。
		for (int i = 0; i < bytes.length; i++) {

			byte b = bytes[i];
			//每四位表示成一个字符
			chars[i * 2] = hex[((b & 0xF0) >> 4)];
			//截得低四位
			chars[i * 2 + 1] = hex[(b & 0x0F)];

		}
		return chars;
	}
	
	public static String multiLine(char[] chars) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < chars.length; i++) {
			if (i % 10 == 0) {
				builder.append(System.getProperty("line.separator"));
			} else if (i % 2 == 0) {
				builder.append(' ');
			}
			builder.append(chars[i]);
		}
		return builder.toString();
	}

	/**
	 * 添加报文头
	 * @param data	报文体
	 * @return	包括报文头的整个报文
	 */
	public static byte[] addMessageHead(byte[] data) {
		//len表示报文头中的长度信息
		int len = data.length+5;//5表示TPDU占的字节数
		if(len>MAX_MESSAGE_SIZE){
			//最大长度相当于unsign short 表示范围
			throw new RuntimeException("the length can not bigger than 65536!");
		}
		byte[] bytes = new byte[len+2];//2表示报文长度所占的字节数
		
		//java语言是big endian表示。8583同样是大端表示。
		bytes[0]=(byte)((len&0xff00)>>8);//整数的高字节放在低地址
		bytes[1]=(byte)(len&0x00ff);//整数的低字节放在高地址
		//TPDU
		bytes[2]=0x60;
		bytes[3]=0x00;bytes[4]=0x00;bytes[5]=0x00;bytes[6]=0x00;
		System.arraycopy(data, 0, bytes, 7, data.length);
		return bytes;
	}
	

	/**
	 * 移除报文头
	 * @param bytes	包括报文头的完整报文
	 * @return	报文体
	 */
	public static byte[] removeMessageHead(byte[] bytes) {
		//检查报文的长度
		int len = ((bytes[0]&0xff)<<8)+(bytes[1]&0xff);
		if(bytes.length!= (len+2)){
			//2表示报文长度所占的字节数
			throw new RuntimeException("the message length is not right!");
		}
		//截取8583报文体
		byte[] data = new byte[len-5];//5表示TPDU占的字节数
		System.arraycopy(bytes, 7, data, 0, data.length);
		return data;
	}
	
	public static byte[] concat(byte[]... bs ){
		//计算总长度
		int length =0;
		for(byte[] b:bs){
			length +=b.length;
		}
		
		byte[] all= new byte[length];
		
		//当前复制的指针
		int cur =0;
		for(byte[] b:bs){
			System.arraycopy(b, 0, all, cur, b.length);
			cur +=b.length;
		}
		return all;
	}
}
