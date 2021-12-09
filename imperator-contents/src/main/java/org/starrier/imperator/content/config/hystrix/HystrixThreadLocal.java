package org.starrier.imperator.content.config.hystrix;


import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.starrier.imperator.content.entity.article.Article;

/**
 * @author Starrier
 * @date 2018/12/19.
 */
public class HystrixThreadLocal extends HandlerInterceptorAdapter {

    public static ThreadLocal<Article> threadLocal = new ThreadLocal<>();
}
