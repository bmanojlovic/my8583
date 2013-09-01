package cn.liang.m8583.message.support;

import java.util.Collections;
import java.util.List;

import cn.liang.m8583.field.basic.AdditionalAmount;
import cn.liang.m8583.message.MessageType;
import cn.liang.m8583.message.RequestResponse;
import cn.liang.m8583.transcoder.Message8583;


/**
 * 消费响应
 * @author 325336, Liang Yabao
 * 2012-3-20
 */
public class PurchaseResponse extends RequestResponse{
	
	public PurchaseResponse(MessageType mt) {
		super(mt);
		// TODO Auto-generated constructor stub
	}

	//避免null值，在响应码不为“00”时，可以使用默认值
	private String card="";

	private long amount=0L;
	
	private List<AdditionalAmount> aal=Collections.emptyList();
	
	public String getCard() {
		return card;
	}
	public void setCard(String card) {
		this.card = card;
	}

	public long getAmount() {
		return amount;
	}
	public void setAmount(long amount) {
		this.amount = amount;
	}
	public List<AdditionalAmount> getAal() {
		return aal;
	}
	public void setAal(List<AdditionalAmount> aal) {
		this.aal = aal;
	}

	@Override
	public Message8583 encode() {
		Message8583 mes = super.encode();
		mes.setCardNumber(card);
		mes.setAmount(amount);
		mes.setAdditionalAmountList(aal);
		
		return mes;
	}
	
	@Override
	public void decode(Message8583 mes) {
		super.decode(mes);
		this.card = mes.getCardNumber();
		this.amount = mes.getAmount();
		this.aal = mes.getAdditionalAmountList();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!super.equals(obj) ||!( obj instanceof PurchaseResponse )){
			return false;
		}
		PurchaseResponse pr = (PurchaseResponse)obj;
		return this.card.equals(pr.getCard())
				&& this.getAmount()== pr.getAmount()
				&& this.getAal().equals(pr.getAal());
	}

}
