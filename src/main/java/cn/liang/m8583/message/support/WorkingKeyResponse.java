package cn.liang.m8583.message.support;

import java.util.Arrays;

import cn.liang.m8583.field.basic.Argument;
import cn.liang.m8583.message.MessageType;
import cn.liang.m8583.message.RequestResponse;
import cn.liang.m8583.transcoder.Message8583;


/**
 * 同步工作密钥的响应
 * @author 325336, Liang Yabao
 * 2012-3-22
 */
public class WorkingKeyResponse extends RequestResponse{

	public WorkingKeyResponse(MessageType mt) {
		super(mt);
		// TODO Auto-generated constructor stub
	}

	//不应有默认值，必填字段
	private byte[] newWorkingKey ;
	
	private Argument argument = new Argument();

	public Argument getArgument() {
		return argument;
	}

	public void setArgument(Argument argument) {
		this.argument = argument;
	}

	public byte[] getNewWorkingKey() {
		return newWorkingKey;
	}

	public void setNewWorkingKey(byte[] newWorkingKey) {
		this.newWorkingKey = newWorkingKey;
	}
	
	@Override
	public Message8583 encode() {
		Message8583 mes = super.encode();
		//如果newWorkingKey为空，则表示responseCode不为“00”，客户端不应再使用此字段。
		if(newWorkingKey!=null){
			mes.setNewWorkingKeyData(newWorkingKey);
		}
		if(argument!=null){
			mes.setArgument(argument);
		}
		
		return mes;
	}

	@Override
	public void decode(Message8583 mes) {
		super.decode(mes);
		this.newWorkingKey = mes.getNewWorkingKeyData();
		this.argument = mes.getArgument();
		
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)||!(obj instanceof WorkingKeyResponse)) {
			return false;
		}
		WorkingKeyResponse wkr = (WorkingKeyResponse) obj;
		return super.equals(wkr) 
				&& Arrays.equals(wkr.getNewWorkingKey(), this.getNewWorkingKey())
				&& argument.equals(wkr.getArgument());

	}
}
