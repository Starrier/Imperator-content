package org.starrier.imperator.content.controller;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.starrier.imperator.content.redis.mq.MyObjectDTO;
import org.starrier.imperator.content.redis.mq.Publish;

/**
 * @author starrier
 * @date 2020/11/21
 */
@Slf4j
@RestController
public class RedissionController {

    private static final String LOCK = "test";
    private static final String SUCCESS = "success";
    private static final String FAILED = "failed";
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private Publish publish;

    /**
     * 如果只有一个线程来访问，那么在rLock.tryLock() 这个方法就会阻塞，等锁释放然后再继续下面的操作，
     * 如果是多线程访问，那么在rLock.tryLock() 这个方法会直接返回false不阻塞了,继续往下执行
     * 可以配合Jmeter 来测试
     * 更多锁的demo,可以参考wiki: https://github.com/redisson/redisson/wiki/8.-%E5%88%86%E5%B8%83%E5%BC%8F%E9%94%81%E5%92%8C%E5%90%8C%E6%AD%A5%E5%99%A8
     *
     * @return
     */
    @GetMapping("/test")
    public Object test() {

        RLock rLock = redissonClient.getLock(LOCK);
        boolean isLock = false;
        try {
            isLock = rLock.tryLock();
            log.info(Thread.currentThread().getName() + "isLock=" + isLock);
            if (isLock) {
                log.info(Thread.currentThread().getName() + "我抢到锁了，开心，先休息10秒先");
                Thread.sleep(10 * 1000);
            } else {
                log.info(Thread.currentThread().getName() + "被人锁了，郁闷下次再来");
                return FAILED;
            }
        } catch (InterruptedException e) {
            log.error("InterruptedException has occur [{}]", e.getMessage(), e);
        } finally {
            if (isLock) {
                log.info(Thread.currentThread().getName() + "不玩了，开锁了！！！");
                rLock.unlock();
            }
        }
        return SUCCESS;
    }

    //发布消息
    @GetMapping("/publish")
    public Object publish(MyObjectDTO dto) {
        return this.publish.publish(dto);
    }


}
