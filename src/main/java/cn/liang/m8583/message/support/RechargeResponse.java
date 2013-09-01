package cn.liang.m8583.message.support;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import cn.liang.m8583.field.basic.AdditionalAmount;
import cn.liang.m8583.message.MessageType;
import cn.liang.m8583.message.RequestResponse;
import cn.liang.m8583.transcoder.Message8583;


/**
 * 充值响应报文
 * @author  Liang Yabao
 * 2012-3-12
 * 
 */
public class RechargeResponse extends RequestResponse{
	
	public RechargeResponse(MessageType mt) {
		super(mt);
		// TODO Auto-generated constructor stub
	}

	//避免null值，在响应码不为“00”时，可以使用默认值
	private String card="";
	private long amount=0L;

	private String preferentialInformation="";
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


	public String getPreferentialInformation() {
		return preferentialInformation;
	}

	public void setPreferentialInformation(String preferentialInformation) {
		this.preferentialInformation = preferentialInformation;
	}
	
	public void setAdditionalAmountList(List<AdditionalAmount> aal){
		this.aal = aal;
	}
	
	public List<AdditionalAmount> getAdditionalAmountList(){
		return aal;
	}

	@Override
	public Message8583 encode() {
		Message8583 mes = super.encode();
		mes.setCardNumber(card);
		mes.setAmount(amount);

		mes.setAdditionalAmountList(aal);
		mes.setPreferentialInformation(preferentialInformation);
		return mes;
	}

	@Override
	public void decode(Message8583 mes) {
		super.decode(mes);
		this.aal = mes.getAdditionalAmountList();
		this.amount = mes.getAmount();

		this.card = mes.getCardNumber();
		this.preferentialInformation = mes.getPreferentialInformation();
		
	}
	
	@Override 
	public boolean equals(Object obj){
		if(!super.equals(obj)){
			return false;
		}
		if(obj instanceof RechargeResponse){
			RechargeResponse rr = (RechargeResponse) obj;
			
			return this.amount== rr.getAmount()
					&& this.card.equals(rr.getCard())
					&& this.preferentialInformation .equals(rr.getPreferentialInformation())
					&& aal.equals(rr.getAdditionalAmountList());
		}else{
			return false;
		}
	}

}
