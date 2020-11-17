package org.starrier.imperator.content.config.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import javax.annotation.Resource;

/**
 * @author starrier
 * @date 2020/11/16
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    @Resource
    private RedisConnectionFactory redisConnectionFactory;

    /**
     * retemplate相关配置

     * @return redisTemplate
     */
    @Bean
    @SuppressWarnings({"rawtypes", "unchecked"})
    public RedisTemplate<String, Object> redisTemplate() {

        //使用jackson2替换掉redisTempalte本身的序列化
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);

        //利用Jackson 使用 defaultTyping 实现通用的序列化和反序列化
        ObjectMapper om = new ObjectMapper();
        //指定要序列化的域，field，get和set，以及修饰符范围，ANY包括public和private
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        //指定序列化输入类型，必须是非final修饰 final修饰的类，比如String,Integer等会跑出异常
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        // 配置redisTemplate
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        //配置连接工厂
        template.setConnectionFactory(redisConnectionFactory);
        //指定序列化模式
        // key序列化
        template.setKeySerializer(jackson2JsonRedisSerializer);
        // value序列化
        template.setValueSerializer(jackson2JsonRedisSerializer);
        // Hash key序列化
        template.setHashKeySerializer(jackson2JsonRedisSerializer);
        // Hash value序列化
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();

        // 开启事务
        template.setEnableTransactionSupport(true);
        template.setConnectionFactory(redisConnectionFactory);

        return template;
    }

    /**
     * 对hash类型的数据操作
     *
     * @param redisTemplate redisTemplate
     * @return Hash Operation
     */
    @Bean
    public HashOperations<String, String, Object> hashOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForHash();
    }

    /**
     * 对redis字符串类型数据操作
     *
     * @param redisTemplate redisTemplate
     * @return String Operation
     */
    @Bean
    public ValueOperations<String, Object> valueOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForValue();
    }

    /**
     * 对链表类型的数据操作
     *
     * @param redisTemplate redisTemplate
     * @return List Operation
     */
    @Bean
    public ListOperations<String, Object> listOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForList();
    }

    /**
     * 对无序集合类型的数据操作
     *
     * @param redisTemplate redisTemplate
     * @return Set Operation
     */
    @Bean
    public SetOperations<String, Object> setOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForSet();
    }

    /**
     * 对有序集合类型的数据操作
     *
     * @param redisTemplate redisTemplate
     * @return ZSet Operation
     */
    @Bean
    public ZSetOperations<String, Object> zSetOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForZSet();
    }

}
