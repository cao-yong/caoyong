package com.caoyong.core.message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;

import com.caoyong.core.bean.base.ResultBase;
import com.caoyong.core.bean.product.Color;
import com.caoyong.core.bean.product.Product;
import com.caoyong.core.bean.product.Sku;
import com.caoyong.core.service.CmsService;
import com.caoyong.core.service.staticpage.StaticPageService;

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
    private StaticPageService staticPageService;
    @Autowired
    private CmsService        cmsService;

    @Override
    public void onMessage(Message message) {
        ActiveMQTextMessage am = (ActiveMQTextMessage) message;
        try {
            //从发布者接受到的id
            String id = am.getText();
            Map<String, Object> root = new HashMap<>();

            log.info("deal with mq message start, id:{}", id);
            //商品信息
            ResultBase<Product> productResult = cmsService.selectProductById(Long.parseLong(id));
            if (productResult.isSuccess() && null != productResult.getValue()) {
                root.put("product", productResult.getValue());
            }
            //sku信息
            ResultBase<List<Sku>> skusResult = cmsService.selectSkuListByProductId(Long.parseLong(id));
            if (skusResult.isSuccess() && null != skusResult.getValue()) {
                root.put("skus", skusResult.getValue());
                //获取颜色,并去重 ,java8函数式编程实现
                List<Color> colors = skusResult.getValue().stream().map(Sku::getColor).distinct()
                        .collect(Collectors.toList());
                root.put("colors", colors);
            }
            //静态化
            staticPageService.productStaticPage(root, id);
        } catch (JMSException e) {
            log.error("ActiveMQ error:", e.getMessage(), e);
        } catch (Exception e) {
            log.error("Listener error:", e.getMessage(), e);
        }
    }

}
