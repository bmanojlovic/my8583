package cn.liang.m8583.message.support;

import cn.liang.m8583.field.basic.ProcessCode;
import cn.liang.m8583.message.InputMessage;
import cn.liang.m8583.message.MessageType;
import cn.liang.m8583.message.RequestResponse;


/**
 * 冲正交易响应
 * @author  Liang Yabao
 * 2012-3-20
 */
public class ReverseResponse extends RequestResponse{

	public ReverseResponse(MessageType mt) {
		super(mt);
		// TODO Auto-generated constructor stub
	}

	//没有必要初始化为空字符串，必填字段，不应有默认值。可以调用route方法填充。
	private ProcessCode processCode = new ProcessCode();

	@Override
	public  String getProcessCode() {
		return processCode;
	}
	/**
	 * 设置处理码
	 * @param processCode	6个数字，与原交易相同
	 */
	public void setProcessCode(ProcessCode processCode){
		this.processCode = processCode;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)||!(obj instanceof ReverseResponse)) {
			return false;
		}
		ReverseResponse rq = (ReverseResponse) obj;
		return super.equals(rq) 
				&& rq.getProcessCode().equals(processCode);

	}
	
	@Override
	public void route(InputMessage im){
		super.route(im);
		this.processCode = im.getProcessCode();
	}

}
