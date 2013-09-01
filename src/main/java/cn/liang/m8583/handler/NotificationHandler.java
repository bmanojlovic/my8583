package cn.liang.m8583.handler;

import cn.liang.m8583.message.Notification;

/**
 * @author 325336, Liang Yabao
 * 2012-3-12
 * 对应于Notification类报文
 */
public interface NotificationHandler extends Handler{

	public String notificationMessageType();
	
	public void handle(Notification notif);
}
