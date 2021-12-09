package org.starrier.imperator.content.controller;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @author starrier
 * @date 2021/11/30
 */
@Component
public class OnlineUserStatsService {

    private static final String ONLINE_USERS = "online_users";

    private final StringRedisTemplate stringRedisTemplate;

    public OnlineUserStatsService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 添加用户在线信息
     * <p></p>
     *
     * @param userId 用户 id
     * @return 是否添加信息成功
     */
    public Boolean online(Integer userId) {
        return this.stringRedisTemplate.opsForZSet().add(ONLINE_USERS, userId.toString(), Instant.now().toEpochMilli());
    }

    /**
     * 获取一定时间内，在线的用户数量
     * <p></p>
     *
     * @param duration 指定的时间范围
     * @return 用户在线数量
     */
    public Long count(Duration duration) {
        LocalDateTime now = LocalDateTime.now();
        return this.stringRedisTemplate.opsForZSet().count(ONLINE_USERS,
                now.minus(duration).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                now.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
    }

    /**
     * 获取所有在线过的用户数量，不论时间
     * <p></p>
     *
     * @return 所有在线过的用户数量
     */
    public Long count() {
        return this.stringRedisTemplate.opsForZSet().zCard(ONLINE_USERS);
    }

    /**
     * 清除超过一定时间没在线的用户数据
     * <p/>
     *
     * @param duration
     * @return
     */
    public Long clear(Duration duration) {
        return this.stringRedisTemplate.opsForZSet().removeRangeByScore(ONLINE_USERS, 0,
                LocalDateTime.now().minus(duration).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
    }


}
