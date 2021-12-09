package org.starrier.imperator.content.config.hystrix;

import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariable;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableLifecycle;
import com.netflix.hystrix.strategy.eventnotifier.HystrixEventNotifier;
import com.netflix.hystrix.strategy.executionhook.HystrixCommandExecutionHook;
import com.netflix.hystrix.strategy.metrics.HystrixMetricsPublisher;
import com.netflix.hystrix.strategy.properties.HystrixPropertiesStrategy;
import com.netflix.hystrix.strategy.properties.HystrixProperty;
import lombok.SneakyThrows;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Starrier
 * @date 2018/12/19.
 */
public class SpringCloudHystrixConcurrencyStrategy extends HystrixConcurrencyStrategy {

    private HystrixConcurrencyStrategy delegateHystrixConcurrencyStrategy;

    public SpringCloudHystrixConcurrencyStrategy() {
        init();
    }

    @Override
    public <T> Callable<T> wrapCallable(Callable<T> callable) {
        return new HystrixThreadCallable<>(callable, RequestContextHolder.getRequestAttributes(), HystrixThreadLocal.threadLocal.get());
    }

    @SneakyThrows(Exception.class)
    private void init() {
        this.delegateHystrixConcurrencyStrategy = HystrixPlugins.getInstance().getConcurrencyStrategy();
        if (this.delegateHystrixConcurrencyStrategy instanceof SpringCloudHystrixConcurrencyStrategy) {
            return;
        }
        HystrixCommandExecutionHook commandExecutionHook = HystrixPlugins.getInstance().getCommandExecutionHook();
        HystrixEventNotifier eventNotifier = HystrixPlugins.getInstance().getEventNotifier();
        HystrixMetricsPublisher metricsPublisher = HystrixPlugins.getInstance().getMetricsPublisher();
        HystrixPropertiesStrategy propertiesStrategy = HystrixPlugins.getInstance().getPropertiesStrategy();
        HystrixPlugins.reset();
        HystrixPlugins.getInstance().registerConcurrencyStrategy(this);
        HystrixPlugins.getInstance().registerCommandExecutionHook(commandExecutionHook);
        HystrixPlugins.getInstance().registerEventNotifier(eventNotifier);
        HystrixPlugins.getInstance().registerMetricsPublisher(metricsPublisher);
        HystrixPlugins.getInstance().registerPropertiesStrategy(propertiesStrategy);
    }


    @Override
    public ThreadPoolExecutor getThreadPool(HystrixThreadPoolKey threadPoolKey,
                                            HystrixProperty<Integer> corePoolSize,
                                            HystrixProperty<Integer> maximumPoolSize,
                                            HystrixProperty<Integer> keepAliveTime, TimeUnit unit,
                                            BlockingQueue<Runnable> workQueue) {
        return this.delegateHystrixConcurrencyStrategy.getThreadPool(threadPoolKey, corePoolSize, maximumPoolSize,
                keepAliveTime, unit, workQueue);
    }

    @Override
    public BlockingQueue<Runnable> getBlockingQueue(int maxQueueSize) {
        return this.delegateHystrixConcurrencyStrategy.getBlockingQueue(maxQueueSize);
    }

    @Override
    public <T> HystrixRequestVariable<T> getRequestVariable(
            HystrixRequestVariableLifecycle<T> rv) {
        return this.delegateHystrixConcurrencyStrategy.getRequestVariable(rv);
    }
}
