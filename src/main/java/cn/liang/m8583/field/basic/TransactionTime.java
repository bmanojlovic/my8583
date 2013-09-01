package cn.liang.m8583.field.basic;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.liang.m8583.field.NumberField;

public class TransactionTime extends NumberField {

	public static final DateFormat TIME_FORMAT = new SimpleDateFormat("HHmmss");
	
	public TransactionTime(){
		super(12,"TIME, LOCAL TRANSACTION ", 6, 0);
	}
	
	
	/**
	 * 交易时间
	 * 
	 * @return
	 */
	public Date getLocalTime() {
		try {
			return TIME_FORMAT.parse(this.getNumber());
		} catch (ParseException e) {
			throw new RuntimeException("DateFormat Exception:", e);
		}
	}

	public void setLocalTime(Date dateTime) {
		
		String timeString = TIME_FORMAT.format(dateTime);
		this.setNumber(timeString);

	}
	
}
