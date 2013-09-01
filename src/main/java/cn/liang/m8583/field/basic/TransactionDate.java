package cn.liang.m8583.field.basic;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.liang.m8583.field.NumberField;

public class TransactionDate extends NumberField {

	public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
	
	public TransactionDate(){
		super(13,"DATE, LOCAL TRANSACTION ", 8, 0);
	}

	/**
	 * 交易日期
	 * 
	 * @return
	 */
	public Date getLocalDate() {

		try {
			return DATE_FORMAT.parse(this.getNumber());
		} catch (ParseException e) {
			throw new RuntimeException("DateFormat Exception:", e);
		}
	}

	public void setLocalDate(Date dateTime) {
		
		String dateString =DATE_FORMAT.format(dateTime);
		super.setNumber(dateString);
	}

}
