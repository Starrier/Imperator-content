package org.starrier.imperator.content.config.rocketmq;

import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


/**
 * Created by eggyer on 2017/3/25.
 */
@Component
public class RocketMQConsumerConfiguration {


    public static final Logger LOGGER = LoggerFactory.getLogger(RocketMQConsumerConfiguration.class);
    @Value("${rocketmq.consumer.namesrvAddr}")
    private String namesrvAddr;
    @Value("${rocketmq.consumer.groupName}")
    private String groupName;
    @Value("${rocketmq.consumer.topic}")
    private String topic;
    @Value("${rocketmq.consumer.tag}")
    private String tag;
    @Value("${rocketmq.consumer.consumeThreadMin}")
    private int consumeThreadMin;
    @Value("${rocketmq.consumer.consumeThreadMax}")
    private int consumeThreadMax;

    @Autowired
    private MessageProcessor messageProcessor;

    @Bean
    public DefaultMQPushConsumer getRocketMQConsumer() {
        if (StringUtils.isBlank(groupName)) {

        }
        if (StringUtils.isBlank(namesrvAddr)) {

        }
        if (StringUtils.isBlank(topic)) {

        }
        if (StringUtils.isBlank(tag)) {

        }
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(groupName);

        consumer.setNamesrvAddr(namesrvAddr);
        consumer.setConsumeThreadMin(consumeThreadMin);
        consumer.setConsumeThreadMax(consumeThreadMax);

        MessageListener messageListener = new MessageListener(messageProcessor);
        consumer.registerMessageListener(messageListener);

        try {
            consumer.subscribe(topic, this.tag);
            //consumer.start();
            LOGGER.info("consumer is start !!! groupName:{},topic:{},namesrvAddr:{}", groupName, topic, namesrvAddr);
        } catch (MQClientException e) {
            LOGGER.error("consumer is start !!! groupName:{},topic:{},namesrvAddr:{}", groupName, topic, namesrvAddr, e);
        }
        return consumer;
    }

}