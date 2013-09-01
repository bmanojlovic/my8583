package cn.liang.m8583.transcoder.exception;

import cn.liang.m8583.field.Field;

/**
 * 字段溢出异常
 * 通常是一个字段的内容超出最大长度
 * @author 325336, Liang Yabao
 * 2012-4-13
 */
public class FieldOverflowException extends RuntimeException {

	public FieldOverflowException(Field field) {
		super("field \"" + field.getName() + "\" is overflow, max size is "
				+ field.getMaxLength() + " !");
	}
}
