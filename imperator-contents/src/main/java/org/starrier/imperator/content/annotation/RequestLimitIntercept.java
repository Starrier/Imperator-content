package org.starrier.imperator.content.annotation;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.starrier.common.result.Result;
import org.starrier.common.result.ResultCode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * 请求拦截
 */
@Slf4j
@Component
public class RequestLimitIntercept extends HandlerInterceptorAdapter {

    private final RedisTemplate redisTemplate;
    @Value("${token.value.validate.param:token}")
    private String tokenValidateParam;

    public RequestLimitIntercept(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * @code isAssignableFrom() 判定此 Class 对象所表示的类或接口与指定的 Class 参数所表示的类或接口是否相同，或是否是其超类或超接口
     * isAssignableFrom()方法是判断是否为某个类的父类
     * instanceof关键字是判断是否某个类的子类
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {
            //HandlerMethod 封装方法定义相关的信息,如类,方法,参数等
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            // 如果 方法上有注解就优先选择方法上的参数，否则类上的参数
            RequestLimit requestLimit = getTagAnnotation(method, RequestLimit.class);
            if (requestLimit != null) {
                if (isLimit(request, requestLimit)) {
                    responseOut(response, Result.error(ResultCode.REQUEST_LIMIT));
                    return false;
                }
            }
        }

        return super.preHandle(request, response, handler);
    }

    //判断请求是否受限
    public boolean isLimit(HttpServletRequest request, RequestLimit requestLimit) {

        // 受限的redis 缓存key ,因为这里用浏览器做测试，我就用sessionid 来做唯一key,如果是app ,可以使用 用户ID 之类的唯一标识。
        String token = request.getHeader(tokenValidateParam);
        if (StringUtils.isEmpty(token)) {
            return false;
        }

        log.info("current token is :[{}]", token);

        // 从缓存中获取，当前这个请求访问了几次
        ValueOperations<String, Integer> valueOperations = redisTemplate.opsForValue();
        Integer redisCount = valueOperations.get(token);
        if (redisCount == null) {
            //初始 次数
            valueOperations.set(token, 1, requestLimit.second(), TimeUnit.SECONDS);
        } else {
            if (redisCount >= requestLimit.maxCount()) {
                return true;
            }
            // 次数自增
            valueOperations.increment(token);
        }
        return false;
    }

    /**
     * 获取目标注解
     * 如果方法上有注解就返回方法上的注解配置，否则类上的
     *
     * @param method
     * @param annotationClass
     * @param <A>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <A extends Annotation> A getTagAnnotation(Method method, Class<A> annotationClass) {
        // 获取方法中是否包含注解
        Annotation methodAnnotate = method.getAnnotation(annotationClass);
        //获取 类中是否包含注解，也就是controller 是否有注解
        Annotation classAnnotate = method.getDeclaringClass().getAnnotation(annotationClass);
        return (A) (methodAnnotate != null ? methodAnnotate : classAnnotate);
    }

    /**
     * 回写给客户端
     *
     * @param response
     * @param result
     * @throws IOException
     */
    private void responseOut(HttpServletResponse response, Result result) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try (PrintWriter out = response.getWriter()) {
            out.append(JSONObject.toJSONString(result));
        }
    }
}
