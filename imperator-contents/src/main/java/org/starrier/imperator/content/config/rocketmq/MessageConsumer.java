/*
package org.starrier.imperator.content.config.rocketmq;

import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

*/
/**
 * MessageModel：集群模式；广播模式
 * ConsumeMode：顺序消费；无序消费
 *
 * @author starrier
 * @date 2020/11/22
 * <p>
 * rocketmq会自动捕获异常回滚  (官方默认会重复消费16次)，
 * <p>
 * rocketmq会自动捕获异常回滚  (官方默认会重复消费16次)，
 *//*

@Component
@RocketMQMessageListener(topic = "springboot-topic", consumerGroup = "consumer-group",
        //selectorExpression = "tag1",selectorType = SelectorType.TAG,
        messageModel = MessageModel.CLUSTERING, consumeMode = ConsumeMode.CONCURRENTLY)
public class MessageConsumer implements RocketMQListener<String> {

    */
/**
 * rocketmq会自动捕获异常回滚  (官方默认会重复消费16次)，
 *//*

    @Override
    public void onMessage(String message) {
        System.out.println("----------接收到rocketmq消息:" + message);
        // rocketmq会自动捕获异常回滚  (官方默认会重复消费16次)
        int a = 1 / 0;
    }


}
*/
