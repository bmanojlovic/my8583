package cn.liang.m8583.message;

import java.util.HashMap;
import java.util.Map;

import cn.liang.m8583.field.BitMap;
import cn.liang.m8583.field.Field;
import cn.liang.m8583.field.basic.Node;
import cn.liang.m8583.field.basic.ProcessCode;
import cn.liang.m8583.field.basic.TTC;
import cn.liang.m8583.field.basic.Terminal;


/**
 * @author  Liang Yabao 2012-3-12 
 * 报文超类. 
 * 这里是指已经翻译成java系统认识的报文，不是8583编码的报文.
 * message type 与process code是Message子类的一个属性，不是Message实例的属性。
 */
public abstract class Message {

	/**
	 * BitMap
	 */
	private BitMap bitMap;
	/**
	 * terminal transaction code
	 * 终端交易流水号，在一定时间内是唯一的。
	 */
	private TTC ttc;

	/**
	 * 终端标识，与key相关，因此所有报文中必须上送
	 */
	private Terminal terminalId;
	/**
	 * 分枝机构标识，与terminalId联合标识唯一。
	 */
	private Node nodeId;

	private ProcessCode processCode = new ProcessCode();
	
	
	public final MessageType mt;
	public Message(MessageType mt){
		this.mt = mt;
	}

	private boolean validateFlag= false;
	
	public boolean isAuthentic(){
		return validateFlag;
	}


	/**
	 * 设置报文验证码，是否是可信息
	 * @param oMac	原报文验证码
	 * @param rMac	重新计算的报文验证码
	 */
	public void authentic(boolean validateFlag){
		this.validateFlag = validateFlag;
	}
	
	 void setTerminalId(Terminal terminalId) {
		this.terminalId = terminalId;
	}


	 void setTtc(TTC ttc) {
		this.ttc = ttc;
	}
	
	 void setNodeId(Node nodeId){
		 this.nodeId = nodeId;
	 }
	public TTC getTtc() {
		return ttc;
	}

	public Terminal getTerminalId() {
		return terminalId;
	}

	public Node getNodeId() {
		return nodeId;
	}

	
	
	private static Map<Class<? extends Message>, String> messageTypeMap = new HashMap<Class<? extends Message>, String>();
	private static Map<Class<? extends Message>, String> processCodeMap = new HashMap<Class<? extends Message>, String>();
	private static Map<String, Class<? extends Message>> messageClassMap = new HashMap<String, Class<? extends Message>>();

	public static void registMessage(String messageType, String processCode,
			Class<? extends Message> clazz) {
		messageTypeMap.put(clazz, messageType);
		processCodeMap.put(clazz, processCode);
		messageClassMap.put(messageType + "#" + processCode, clazz);
	}

	private static String getMessageType(Class<? extends Message> clazz) {
		return messageTypeMap.get(clazz);
	}

	private static String getProcessCode(Class<? extends Message> clazz) {
		return processCodeMap.get(clazz);
	}

	public static Class<? extends Message> getMessageClass(String messageType,
			String processCode) {
		return messageClassMap.get(messageType + "#" + processCode);
	}

	public final String getMessageType() {
		//如何获得，是内部事情，不应该被override
		return Message.getMessageType(this.getClass());
	}

	/**
	 * 通常情况下不应该被override，但冲正交易，是与具体实例相关的，不与类绑定。
	 * @return	六数字，表示处理码
	 */
	public  ProcessCode getProcessCode() {
		return processCode;
	}


	/**
	 * 把报文从字段形态解码成为java认识的形态。
	 * @param mes
	 */
	public void decode(Field[] fields) {

	}	


	/**
	 * 编码成为报文中间形态（字段形态）
	 * 必填字段如果为空，则可能抛出异常
	 * @return
	 */
	public Field[] encode() {
		
		

		return new Field[]{mt,nodeId,terminalId,ttc};
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Message)) {
			return false;
		}
		Message im = (Message) obj;
		return im.getNodeId().equals(nodeId)
				&& im.getTerminalId().equals(terminalId)
				&& im.getTtc().equals(ttc);
	}

}
