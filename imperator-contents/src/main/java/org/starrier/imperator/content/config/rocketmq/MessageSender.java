/*
package org.starrier.imperator.content.config.rocketmq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

*/
/**
 * @author starrier
 * @date 2020/11/22
 * <p>
 * 发送可靠同步消息 ,可以拿到SendResult 返回数据
 * 同步发送是指消息发送出去后，会在收到mq发出响应之后才会发送下一个数据包的通讯方式。
 * 这种方式应用场景非常广泛，例如重要的右键通知、报名短信通知、营销短信等。
 * @param topic   topic:tag
 * @param message 消息体 可以为一个对象
 * @param timeout 超时时间 毫秒
 * <p>
 * 发送 可靠异步消息
 * 发送消息后，不等mq响应，接着发送下一个数据包。发送方通过设置回调接口接收服务器的响应，并可对响应结果进行处理。
 * 异步发送一般用于链路耗时较长，对于RT响应较为敏感的业务场景，例如用户上传视频后通过启动转码服务，转码完成后通推送转码结果。
 * <p>
 * 参数1： topic:tag
 * 参数2:  消息体 可以为一个对象
 * 参数3： 回调对象
 * <p>
 * 发送单向消息
 * @param topic   topic:tag
 * @param message 消息体 可以为一个对象
 * <p>
 * 发送单向的顺序消息
 * @param topic   主题
 * @param message 消息内容
 * @param hasKey  hash 值
 * <p>
 * 延迟发送
 * <p>
 * 这里设置4，即30s的延迟
 * messageDelayLevel=1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
 * @param topic
 * @param message
 * <p>
 * 发送可靠同步消息 ,可以拿到SendResult 返回数据
 * 同步发送是指消息发送出去后，会在收到mq发出响应之后才会发送下一个数据包的通讯方式。
 * 这种方式应用场景非常广泛，例如重要的右键通知、报名短信通知、营销短信等。
 * @param topic   topic:tag
 * @param message 消息体 可以为一个对象
 * @param timeout 超时时间 毫秒
 * <p>
 * 发送 可靠异步消息
 * 发送消息后，不等mq响应，接着发送下一个数据包。发送方通过设置回调接口接收服务器的响应，并可对响应结果进行处理。
 * 异步发送一般用于链路耗时较长，对于RT响应较为敏感的业务场景，例如用户上传视频后通过启动转码服务，转码完成后通推送转码结果。
 * <p>
 * 参数1： topic:tag
 * 参数2:  消息体 可以为一个对象
 * 参数3： 回调对象
 * <p>
 * 发送单向消息
 * @param topic   topic:tag
 * @param message 消息体 可以为一个对象
 * <p>
 * 发送单向的顺序消息
 * @param topic   主题
 * @param message 消息内容
 * @param hasKey  hash 值
 * <p>
 * 延迟发送
 * <p>
 * 这里设置4，即30s的延迟
 * messageDelayLevel=1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
 * @param topic
 * @param message
 *//*

@Slf4j
@Component
public class MessageSender {

    @Value("${rocket.mq.delay.level:4}")
    private Integer delayLevel;

    @Value("${rocket.mq.delay.timeout:100}")
    private Long delayTimeout;

    private final RocketMQTemplate rocketMQTemplate;

    public MessageSender(RocketMQTemplate rocketMQTemplate) {
        this.rocketMQTemplate = rocketMQTemplate;
    }

    */
/**
 * 发送可靠同步消息 ,可以拿到SendResult 返回数据
 * 同步发送是指消息发送出去后，会在收到mq发出响应之后才会发送下一个数据包的通讯方式。
 * 这种方式应用场景非常广泛，例如重要的右键通知、报名短信通知、营销短信等。
 *
 * @param topic   topic:tag
 * @param message 消息体 可以为一个对象
 * @param timeout 超时时间 毫秒
 *//*

    public void syncSend(String topic, Object message, long timeout) {
        rocketMQTemplate.syncSend("springboot-topic:tag", "这是一条同步消息", 10000);
    }

    */
/**
 * 发送 可靠异步消息
 * 发送消息后，不等mq响应，接着发送下一个数据包。发送方通过设置回调接口接收服务器的响应，并可对响应结果进行处理。
 * 异步发送一般用于链路耗时较长，对于RT响应较为敏感的业务场景，例如用户上传视频后通过启动转码服务，转码完成后通推送转码结果。
 * <p>
 * 参数1： topic:tag
 * 参数2:  消息体 可以为一个对象
 * 参数3： 回调对象
 *//*

    public void asyncSend(String topic, Object message) throws Exception {

        rocketMQTemplate.asyncSend(topic, message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("回调sendResult:" + sendResult);
            }

            @Override
            public void onException(Throwable e) {
                log.info(e.getMessage());
            }

        });
        TimeUnit.SECONDS.sleep(100000);

    }

    */
/**
 * 发送单向消息
 *
 * @param topic   topic:tag
 * @param message 消息体 可以为一个对象
 *//*

    public void sendOneWay(String topic, Object message) {
        rocketMQTemplate.sendOneWay(topic, message);
    }

    */
/**
 * 发送单向的顺序消息
 *
 * @param topic   主题
 * @param message 消息内容
 * @param hasKey  hash 值
 *//*

    public void sendOneWayOrderly(String topic, Object message, String hasKey) {
        rocketMQTemplate.sendOneWayOrderly(topic, message, hasKey);
    }

    */
/**
 * 延迟发送
 * <p>
 * 这里设置4，即30s的延迟
 * messageDelayLevel=1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
 *
 * @param topic
 * @param message
 *//*

    public void sendDelay(String topic, String message) {

        Message<String> msg = new GenericMessage<>(message);
        rocketMQTemplate.asyncSend(topic, msg, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                //发送成功
            }

            @Override
            public void onException(Throwable throwable) {
                //发送失败
            }
        }, delayTimeout, delayLevel);
    }

}
*/
