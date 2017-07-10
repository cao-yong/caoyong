package com.caoyong.core.message;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.activemq.command.ActiveMQTextMessage;
/**
 * 自定义消息Listener
 * @author yong.cao
 * @time 2017年7月11日上午12:08:57
 */
public class CustomMessageListener implements MessageListener{

	@Override
	public void onMessage(Message message) {
		ActiveMQTextMessage am = (ActiveMQTextMessage) message;
		try {
			System.out.println("mq message......................." + am.getText());
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
