package cn.liang.m8583.message.support;

import java.util.Collections;
import java.util.List;

import cn.liang.m8583.field.basic.AdditionalAmount;
import cn.liang.m8583.field.basic.CardNumber;
import cn.liang.m8583.message.InputMessage;
import cn.liang.m8583.message.MessageType;
import cn.liang.m8583.message.RequestResponse;


/**
 * 余额响应报文
 * @author  Liang Yabao
 * 2012-3-12
 * 
 */
public class BalanceResponse extends RequestResponse{

	public BalanceResponse(MessageType mt) {
		super(mt);
	}

	/**避免null值，在响应码不为“00”时，可以使用默认值*/
	private List<AdditionalAmount> aal=Collections.emptyList(); 

	private CardNumber cardNumber=new CardNumber();
	
	public CardNumber getCardNumber() {
		return cardNumber;
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
