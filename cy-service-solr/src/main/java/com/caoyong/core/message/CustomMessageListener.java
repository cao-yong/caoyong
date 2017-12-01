package com.caoyong.core.message;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.caoyong.common.enums.ProductIsShowEnum;
import com.caoyong.core.service.SearchService;

import lombok.extern.slf4j.Slf4j;

/**
 * 自定义消息Listener
 * 
 * @author yong.cao
 * @time 2017年7月11日上午12:08:57
 */

@Slf4j
public class CustomMessageListener implements MessageListener {
    @Autowired
    private SearchService searchService;

    @Override
    public void onMessage(Message message) {
        ActiveMQTextMessage am = (ActiveMQTextMessage) message;
        try {
            //调用sevice保存商品信息到solr
            if (StringUtils.isBlank(am.getText())) {
                return;
            }
            String[] msg = am.getText().split(":");
            Long id = Long.parseLong(msg[0]);
            log.info("deal with mq message start, id:{}", id);
            if (ProductIsShowEnum.PUT_OFF.getValue().equals(Integer.parseInt(msg[1]))) {
                searchService.deleteProductToSolr(id);
            } else if (ProductIsShowEnum.PUT_ON.getValue().equals(Integer.parseInt(msg[1]))) {
                searchService.insertProductToSolr(id);
            }
        } catch (JMSException e) {
            log.error("ActiveMQ error:", e.getMessage(), e);
        } catch (Exception e) {
            log.error("Listener error:", e.getMessage(), e);
        }
    }

}
