package cn.liang.m8583.message.support;

import java.util.Collections;
import java.util.List;

import cn.liang.m8583.field.basic.AdditionalAmount;
import cn.liang.m8583.message.InputMessage;
import cn.liang.m8583.message.MessageType;
import cn.liang.m8583.message.RequestResponse;
import cn.liang.m8583.transcoder.Message8583;


/**
 * 余额响应报文
 * @author 325336, Liang Yabao
 * 2012-3-12
 * 
 */
public class BalanceResponse extends RequestResponse{

	public BalanceResponse(MessageType mt) {
		super(mt);
		// TODO Auto-generated constructor stub
	}

	/**避免null值，在响应码不为“00”时，可以使用默认值*/
	private List<AdditionalAmount> aal=Collections.emptyList(); 

	private String cardNumber="";
	
	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	@Override
	public Message8583 encode() {
		Message8583 mes = super.encode();
		mes.setAdditionalAmountList(aal);
		mes.setCardNumber(cardNumber);

		return mes;
	}

	@Override
	public void decode(Message8583 mes) {
		super.decode(mes);
		this.aal = mes.getAdditionalAmountList();
		this.cardNumber = mes.getCardNumber();
		
	}
	
	public void setAdditionalAmountList(List<AdditionalAmount> aal){
		this.aal = aal;
	}
	public List<AdditionalAmount> getAdditionalAmountList(){
		return aal;
	}
	
	
	@Override
	public boolean equals(Object obj){
		if(!super.equals(obj)||!(obj instanceof BalanceResponse)){
			return false;
		}
		BalanceResponse br = (BalanceResponse)obj;
		
		return  this.aal.equals(br.getAdditionalAmountList());
				
	}
	
	public void route(InputMessage im){
		super.route(im);
		if(im instanceof BalanceRequest){
			BalanceRequest br = (BalanceRequest)im;
			//this.cardNumber = br.getCard();
		}
	}
}
