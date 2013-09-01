package cn.liang.m8583.message;

import cn.liang.m8583.field.NumberField;

public class MessageType extends NumberField{

	
	public MessageType(String mt){
		super(0,"message type",4,0);
		super.setNumber(mt);
	}
	

}
