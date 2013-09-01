package cn.liang.m8583.transcoder;

import org.junit.Before;
import org.junit.Test;

import cn.liang.m8583.field.BitMap;
import cn.liang.m8583.field.Field;
import cn.liang.m8583.message.Message;
import cn.liang.m8583.message.support.RechargeRequest;
import cn.liang.m8583.transcoder.Message8583;



/**
 * @author 325336, Liang Yabao
 * 2012-3-12
 */
public class MessageTest {

	@Before
	public void registMessage(){
		Message.registMessage("7200", "210000", RechargeRequest.class);
	}
	@Test
	public void testCardNumber() {
		int i = 49;
		char c = (char) i;
		System.out.println(c == 49);
		
		byte b= 0x53;
		System.out.println(((b & 0xF0)>>4)+48);
		System.out.println((char)(((b & 0xF0)>>4)+48));
		System.out.println((char)(((b & 0x0F))+48));
		assert ((char) (((b & 0xF0)>>4)+48) =='5'):"FAIL";
		Message8583 mes = new Message8583();
		Field field = mes.fields[1];
		byte[] data = {
				0x12,0x53,0x04,0x49,0x31,0x42
		};
		field.setActLength(12);
		field.setData(data);
		
		assert mes.getCardNumber().equals("125304493142"):mes.getCardNumber();
		System.out.println(mes.getCardNumber());
	}

	@Test
	public void testBitMapExist(){
		byte[] bitmap= new byte[8];
		bitmap[0] = 0x4B;
		
		System.out.println(BitMap.exist(bitmap,1));
	}
	
	@Test
	public void testAmount(){
		Message8583 mes = new Message8583();
		Field field = mes.fields[4-1];
		byte[] data = {
				0x00,0x00,0x04,0x49,0x31,0x42
		};
		field.setData(data);
		assert mes.getAmount()== 4493142L : mes.getAmount();
		System.out.println(mes.getAmount());
	}
	
	@Test
	public void testAmount2(){
		Message8583 mes = new Message8583();
		mes.setAmount(356985L);
		System.out.println("testAmount2: "+mes.getAmount());
		assert mes.getAmount() == 356985L :"amount getter or setter is wrong!";
	}
	
	//@Test
	public void testBitMap(){
		byte[] bytes= new byte[8];
		bytes[0]=0x50;
		bytes[1]=0x20;
		bytes[2]=0x00;
		bytes[5] = (byte) 0xc0;
		int i=3;
		i>>>=1;
		System.out.println("before exist "+bytes[4]);
		for(int index:new BitMap(bytes).exist()){
			System.out.println("bitmap index: "+index);
		}
		 bytes= new byte[8];
		bytes[0]=0x72;
		byte[] bs =new BitMap(bytes).getBytes();
		assert new BitMap(bytes).getBytes()[0] == 0x72:"getBytes() is wrong !";
		assert new BitMap(bytes).getBytes()[5] == 0xc0:"getBytes() is wrong !";
		
	}

	@Test
	public void testBitMap2(){
		BitMap bm = new BitMap();
		bm.setExistField(1);
		bm.setExistField(3);
		byte[] bytes = bm.getBytes();
		System.out.println(bytes[0]);
		
	}


	@Test
	public void testSplit(){
		RechargeRequest rq = new RechargeRequest();
		rq.setAmount(350L);
		rq.setCard("125304493142");
		rq.setNodeId("12345678");
		rq.setOperator("system");
		rq.setTerminalId("123456789abc");
		rq.setTtc("567891");
		Message8583 mes = rq.encode();
		byte[] bytes = mes.encode();
		Message8583 mes2 = new Message8583();
		mes2.decode(bytes);
		
		RechargeRequest rq2= new RechargeRequest();
		rq2.decode(mes2);
		System.out.println("testSplit getCard: "+rq2.getCard());
		assert rq2.getCard().equals(rq.getCard()):"testSplit card number convert wrong!";
		assert rq2.getAmount()== rq.getAmount():"testSplit amount wrong!";
		assert rq2.getNodeId().equals(rq.getNodeId()):"testSplit nodeid";
		assert rq2.getOperator().equals(rq.getOperator()):"operator";
		assert rq2.getTerminalId().equals(rq.getTerminalId()):"terminal id";
		assert rq2.getTtc().equals(rq.getTtc()):"ttc";
		assert rq2.equals(rq):"testSplit  convert wrong!";
	}
	
	@Test
	public void testSetExistField(){
		BitMap bitMap = new BitMap();
		bitMap.setExistField(62);
		System.out.println("testSetExistField: "+bitMap.longValue());
	}
}
