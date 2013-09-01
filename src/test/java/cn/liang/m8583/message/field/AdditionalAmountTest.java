package cn.liang.m8583.message.field;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cn.liang.m8583.field.basic.AdditionalAmount;

/**
 * @author  Liang Yabao
 * 2012-4-5
 */
public class AdditionalAmountTest {

	List<AdditionalAmount> list = new ArrayList<AdditionalAmount>();
	AdditionalAmount aa;
	
	@Before 
	public void init(){
		aa = new AdditionalAmount("20","01","CNY",999999999999L);
		list.add(aa);
	}
	@Test
	public void test() {
		
		
		byte[] bb = aa.encode();
		AdditionalAmount aa2 = new AdditionalAmount("20","01","CNY",999999999999L);
		aa2.decode(bb);

		System.out.println(aa2.equals(aa));
	}

}
