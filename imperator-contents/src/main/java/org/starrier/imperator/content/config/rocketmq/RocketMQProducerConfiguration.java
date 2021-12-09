package org.starrier.imperator.content.config.rocketmq;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * @author Starrier
 * @date 2020/11/22
 */
@Slf4j
@SpringBootConfiguration
public class RocketMQProducerConfiguration {

    @Value("${rocketmq.producer.groupName}")
    private String groupName;

    @Value("${rocketmq.producer.namesrvAddr}")
    private String namesrvAddr;

    @Value("${rocketmq.producer.instanceName}")
    private String instanceName;

    @Value("${rocketmq.producer.maxMessageSize:4M}")
    private int maxMessageSize;

    @Value("${rocketmq.producer.sendMsgTimeout}")
    private int sendMsgTimeout;

    @Bean
    public DefaultMQProducer getRocketMQProducer() {

        if (StringUtils.isBlank(this.groupName)) {
            return null;
        }
        if (StringUtils.isBlank(this.namesrvAddr)) {
            return null;
        }
        if (StringUtils.isBlank(this.instanceName)) {
            return null;
        }

        DefaultMQProducer producer;
        producer = new DefaultMQProducer(this.groupName);
        producer.setNamesrvAddr(this.namesrvAddr);
        producer.setInstanceName(instanceName);
        producer.setMaxMessageSize(this.maxMessageSize);
        producer.setSendMsgTimeout(this.sendMsgTimeout);
     /*   try {
            producer.start();
            log.info(String.format("producer is start ! groupName:[%s],namesrvAddr:[%s]", this.groupName, this.namesrvAddr));
        } catch (MQClientException e) {
            log.error("producer is error {}", e.getMessage(), e);
        }*/
        return producer;
    }
}
