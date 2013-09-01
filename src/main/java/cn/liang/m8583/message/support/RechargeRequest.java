package cn.liang.m8583.message.support;

import cn.liang.m8583.message.MessageType;
import cn.liang.m8583.message.Request;
import cn.liang.m8583.transcoder.Message8583;


/**
 * 充值请求报文
 * @author 325336, Liang Yabao
 * 2012-3-12
 * 
 */
public class RechargeRequest extends Request {
	
	public RechargeRequest(MessageType mt) {
		super(mt);
		// TODO Auto-generated constructor stub
	}

	private String card;
	private long amount;

	public String getCard() {
		return card;
	}

	public long getAmount() {
		return amount;
	}
	public void setCard(String card) {
		this.card = card;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}


	@Override
	public void decode(Message8583 mes) {
		super.decode(mes);
		this.card = mes.getCardNumber();
		this.amount = mes.getAmount();

	}	

	@Override
	public Message8583 encode() {
		Message8583 mes = super.encode();
		
		mes.setAmount(this.amount);
		mes.setCardNumber(this.card);

		return mes;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)||!(obj instanceof RechargeRequest)) {
			return false;
		}
		RechargeRequest rq = (RechargeRequest) obj;
		return super.equals(rq) && rq.getCard().equals(card)
				&& rq.getAmount() == amount;

	}

}
