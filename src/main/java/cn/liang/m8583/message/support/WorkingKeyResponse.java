package cn.liang.m8583.message.support;

import cn.liang.m8583.field.WorkingKey;
import cn.liang.m8583.field.basic.Argument;
import cn.liang.m8583.message.MessageType;
import cn.liang.m8583.message.RequestResponse;


/**
 * 同步工作密钥的响应
 * @author  Liang Yabao
 * 2012-3-22
 */
public class WorkingKeyResponse extends RequestResponse{

	public WorkingKeyResponse(MessageType mt) {
		super(mt);
	}

	//不应有默认值，必填字段
	private WorkingKey newWorkingKey = new WorkingKey();
	
	private Argument argument = new Argument();

	public Argument getArgument() {
		return argument;
	}

	public void setArgument(Argument argument) {
		this.argument = argument;
	}

	public WorkingKey getNewWorkingKey() {
		return newWorkingKey;
	}

	public void setNewWorkingKey(WorkingKey newWorkingKey) {
		this.newWorkingKey = newWorkingKey;
	}
	
}
