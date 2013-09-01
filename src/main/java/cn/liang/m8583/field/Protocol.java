package cn.liang.m8583.field;

import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.liang.m8583.field.*;
import cn.liang.m8583.field.basic.CardNumber;
import cn.liang.m8583.field.basic.ProcessCode;
import cn.liang.m8583.field.basic.TTC;
import cn.liang.m8583.field.basic.Terminal;
import cn.liang.m8583.message.MessageType;
import cn.liang.m8583.transcoder.Message8583;


/**
 * 8583编码的报文，用于切分与组装字节数组形态的报文。 此类应该定义字段位域，定义报文对应的messageType与processCode
 * 
 * @author 325336, Liang Yabao 2012-3-12
 */
public class Protocol {

	private static final int MAX_WAYBILL_LIST_SIZE = 999 / 12;

	private static final int WAYBILL_SIZE = 12;

	private static final Logger logger = LoggerFactory
			.getLogger(Message8583.class);

	private static final int WORKING_KEY_SIZE = 24;
	private static final int WORKING_KEY_CHECK_VALUE_SIZE = 4;

	public static final Charset CHARSET = Charset.forName("GBK");

	// fields中需要放置数据，是报文的一个成员变量
	private Field[] fields;
	private MessageType messageType = new MessageType("0000");
	private BitMap bitMap = new BitMap();

	public Protocol() {
		// 定义字段，将作为原型
		fields = new Field[64 + 1];
		
		fields[0] = messageType;
		fields[1] = bitMap;

		fields[2] = new CardNumber();
		fields[3] = new ProcessCode();
		fields[4] = new Field("TRANSACTION AMOUNT ", 12, 0, DataType.INTEGER);
		fields[5] = Field.NO_USE_12;
		fields[6] = Field.NO_USE_12;
		fields[7] = new Field("TRANSACTION DATE AND TIME ", 10, 0,
				DataType.STRING);
		fields[8] = Field.NO_USE_8;
		fields[9] = Field.NO_USE_8;
		fields[10] = Field.NO_USE_8;
		fields[11] = new TTC();
		fields[12] = new Field("TIME, LOCAL TRANSACTION ", 6, 0,
				DataType.NUMBER);
		fields[13] = new Field("DATE, LOCAL TRANSACTION ", 8, 0,
				DataType.NUMBER);
		fields[14] = new Field("DATE, EXPIRATION ", 4, 0, DataType.STRING);
		fields[15] = new Field("DATE, SETTLEMENT ", 4, 0, DataType.STRING);
		fields[16] = Field.NO_USE;
		fields[17] = new Field("DATE, CAPTURE ", 4, 0, DataType.STRING);
		fields[18] = new Field("MERCHANT'S TYPE ", 4, 0, DataType.STRING);
		fields[19] = Field.NO_USE;
		fields[20] = Field.NO_USE;
		fields[21] = Field.NO_USE;
		fields[22] = new Field("POINT OF SERVICE ENTRY MODE ", 3, 0,
				DataType.STRING);
		fields[23] = Field.NO_USE;
		fields[24] = new Field("Network International identifier ", 3, 0,
				DataType.INTEGER);
		fields[25] = new Field("POINT OF SERVICE CONDITION CODE ", 2, 0,
				DataType.STRING);
		fields[26] = Field.NO_USE;
		fields[27] = Field.NO_USE;
		fields[28] = new Field("field27 ", 6, 0, DataType.STRING);
		fields[29] = Field.NO_USE;
		fields[30] = Field.NO_USE;
		fields[31] = Field.NO_USE;
		fields[32] = new Field("ACQUIRER INSTITUTION ID. CODE ", 11, 2,
				DataType.STRING);
		/* FLD 33 */
		// new Field("FORWARDING INSTITUTION ID. CODE ", 11, 2,
		// Field.DataType.STRING),
		fields[33] = new Field("PHONE NUMBER ", 11, 2, DataType.NUMBER);
		fields[34] = new Field("TRANSACTION QUERY ", 28, 2, DataType.NUMBER);
		fields[35] = new Field("TRACK 2 DATA ", 37, 2, DataType.STRING);
		fields[36] = new Field("TRACK 3 DATA ", 104, 3, DataType.STRING);
		fields[37] = new Field("RETRIEVAL REFERENCE NUMBER ", 20, 0,
				DataType.STRING);
		fields[38] = new Field("AUTH. IDENTIFICATION RESPONSE ", 6, 0,
				DataType.STRING);
		fields[39] = new Field("RESPONSE CODE ", 2, 0, DataType.STRING);
		fields[40] = Field.NO_USE;
		fields[41] = new Terminal();
		fields[42] = new Field("NODE ID. ", 8, 0, DataType.STRING);
		fields[43] = new Field("CARD ACCEPTOR NAME LOCATION ", 40, 0,
				DataType.STRING);
		fields[44] = new Field("ADDITIONAL RESPONSE DATA ", 25, 2,
				DataType.STRING);
		fields[45] = Field.NO_USE;
		fields[46] = Field.NO_USE;
		fields[47] = new Field("field47 ", 999, 3, DataType.STRING);
		fields[48] = new Field("ADDITIONAL DATA --- PRIVATE ", 999, 3,
				DataType.STRING);
		fields[49] = new Field("CURRENCY CODE,TRANSACTION ", 3, 0,
				DataType.STRING);
		fields[50] = new Field("CURRENCY CODE,SETTLEMENT ", 3, 0,
				DataType.STRING);
		fields[51] = Field.NO_USE;
		fields[52] = new Password();
		fields[53] = new Field("SECURITY RELATED CONTROL INformATION", 16, 0,
				DataType.STRING);
		fields[54] = new Field("ADDITIONAL AMOUNTS ", 120, 3, DataType.STRING);
		fields[55] = Field.NO_USE;
		fields[56] = Field.NO_USE;
		fields[57] = new Field("PREFERENTIAL INFO. ", 999, 3, DataType.STRING);
		fields[58] = Field.NO_USE;
		fields[59] = new Field("TRANSACTION DETAIL ", 999, 3, DataType.STRING);
		fields[60] = new Field("WAYBILL NO. ", 999, 3, DataType.NUMBER);
		fields[61] = new Field("OPERATOR ", 6, 3, DataType.STRING);
		fields[62] = Field.NO_USE;
		fields[63] = new Field("ERROR INFO. ", 999, 3, DataType.STRING);
		fields[64] = new Field("MESSAGE AUTHENTICATION CODE ", 8, 0,
				DataType.BINARY);
	}

	/**
	 * 把字节数组切分成各个字段。
	 * 
	 * @param bytes
	 *            字节数组形态的8583报文，从消息类型开始，包括mac字段。
	 */
	public void decode(byte[] bytes) {

		int cur = 0;

		cur = messageType.read(bytes, cur);

		cur = bitMap.read(bytes, cur);

		// logger.debug("bitmap :{}",MessageUtil.byte2hex(bitmapBytes));

		for (int index : bitMap.exist()) {

			Field field = fields[index];

			cur = field.read(bytes, cur);

		}
	}

	/**
	 * 把各个字段组装成一个8583字节报文。
	 * 
	 * @return
	 */
	public byte[] encode() {

		// 计算字节报文总长度
		int messageLength = 0;
		for (Field field : fields) {
			if (field.getData() != null) {
				// 字段的长度使用BCD压缩表示，0位、2位、3位分别用0字节、1字节、2字节表示。
				int fieldLength = (field.getVarLength() + 1) / 2;
				messageLength += (field.getData().length + fieldLength);
			}
		}

		logger.debug("the message length from message type to mac :{}",
				messageLength);
		// 字节数组报文
		byte[] bytes = new byte[messageLength];
		// 填写报文
		int cur = 0;
		for (Field field : fields) {
			if (field.getData() != null) {
				cur = field.write(bytes, cur);
			}
		}
		return bytes;
	}

}
