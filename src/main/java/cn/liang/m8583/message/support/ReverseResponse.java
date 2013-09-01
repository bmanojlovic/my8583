package cn.liang.m8583.message.support;

import cn.liang.m8583.message.InputMessage;
import cn.liang.m8583.message.MessageType;
import cn.liang.m8583.message.RequestResponse;
import cn.liang.m8583.transcoder.Message8583;


/**
 * 冲正交易响应
 * @author 325336, Liang Yabao
 * 2012-3-20
 */
public class ReverseResponse extends RequestResponse{

	public ReverseResponse(MessageType mt) {
		super(mt);
		// TODO Auto-generated constructor stub
	}

	//没有必要初始化为空字符串，必填字段，不应有默认值。可以调用route方法填充。
	private String processCode;

	@Override
	public  String getProcessCode() {
		return processCode;
	}
	/**
	 * 设置处理码
	 * @param processCode	6个数字，与原交易相同
	 */
	public void setProcessCode(String processCode){
		this.processCode = processCode;
	}
	@Override
	public void decode(Message8583 mes) {
		super.decode(mes);
		
		this.processCode = mes.getProcessCode();
	}	

	@Override
	public Message8583 encode() {
		Message8583 mes = super.encode();
		
		mes.setProcessCode(processCode);
		return mes;
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
