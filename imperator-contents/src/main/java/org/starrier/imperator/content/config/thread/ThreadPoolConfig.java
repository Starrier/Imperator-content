package org.starrier.imperator.content.config.thread;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Starrier
 * @date 2018/11/10.
 */
@Slf4j
@Configuration
@EnableAsync
public class ThreadPoolConfig {

    /**
     * 根据 cpu 的数量动态的配置核心线程数和最大线程数
     */
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    /**
     * 核心线程数 = CPU核心数 + 1
     */
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    /**
     * 线程池最大线程数 = CPU核心数 * 2 + 1
     */
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    @Value("${thread.pool.queue.capacity.size:200}")
    private Integer threadPoolQueueCapacitySize;
    @Value("${thread.pool.thread.keep.alive.seconds:60}")
    private Integer threadPoolThreadKeepAliveSeconds;

    /**
     * 1. Set the number of core threads
     * 2. Set the maximum number of threads
     * 3. Set the queue capacity
     * 4. Set the thread active time(seconds)
     * 5. Set the default thread name
     * 6. Set the rejection policy
     * 6. Close the thread pool after all the tasks have ended.
     */
    @Bean
    public TaskExecutor imperatorExcutor() {


        log.info(" ThreadPool is using ....");
        log.info(" Asynchronous task is about to execute ");

        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();

        taskExecutor.setCorePoolSize(CORE_POOL_SIZE);

        taskExecutor.setMaxPoolSize(MAXIMUM_POOL_SIZE);

        taskExecutor.setQueueCapacity(threadPoolQueueCapacitySize);

        taskExecutor.setKeepAliveSeconds(threadPoolThreadKeepAliveSeconds);
        taskExecutor.setThreadNamePrefix("Service-Article-");
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        /*
         * 1. Wait for all the tasks to end before closing the thread pool
         * 2. Set the wait time for the tasks in the thread pool,
         *    and if there is no destruction in the thread pool,
         *    force it to be destroyed to ensure that all the application is
         *    finally closed and blocked from time to time.
         * 3. Initialize the thread pool
         * */
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        taskExecutor.setAwaitTerminationSeconds(60);
        taskExecutor.initialize();

        return taskExecutor;
    }
}
