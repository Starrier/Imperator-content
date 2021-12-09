package org.starrier.imperator.content.config.hystrix;

import lombok.SneakyThrows;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.starrier.imperator.content.entity.article.Article;

import java.util.concurrent.Callable;

/**
 * @author Starrier
 * @date 2018/12/19.
 */
public class HystrixThreadCallable<S> implements Callable<S> {
    private final RequestAttributes requestAttributes;
    private final Callable<S> delegate;
    private Article params;

    public HystrixThreadCallable(Callable<S> callable, RequestAttributes requestAttributes, Article params) {
        this.delegate = callable;
        this.requestAttributes = requestAttributes;
        this.params = params;
    }

    @Override
    @SneakyThrows(Exception.class)
    public S call() {
        try {
            RequestContextHolder.setRequestAttributes(requestAttributes);
            HystrixThreadLocal.threadLocal.set(params);
            return delegate.call();
        } finally {
            RequestContextHolder.resetRequestAttributes();
            HystrixThreadLocal.threadLocal.remove();
        }
    }

}
