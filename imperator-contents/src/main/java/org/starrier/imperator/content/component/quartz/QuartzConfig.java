/*
package org.starrier.imperator.content.component.quartz;

import org.quartz.Trigger;
import org.quartz.ee.servlet.QuartzInitializerListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.Objects;

import static org.starrier.imperator.content.component.constant.ProfileConstant.ONE;

*/
/**
 * Quartz  Configuration
 *
 * @author Starrier
 * @date 2018/11/10.
 * <p>
 * 定义 Quartz 调度工厂
 * <p>
 * 用于 Quartz 集群， QuartzScheduler 启动时更新已存在的 Job
 * 延迟启动，应用启动 1s 后
 * 注册触发器
 * <p>
 * 配置定时任务
 * <p>
 * 是否并发执行  ？ true: 当前任务未执行完成，继续执行下一个 false : 等待上一个任务完成，再继续执行下一个
 * 设置任务名称
 * 设置任务分组，可持久化（多任务时使用）
 * 需要执行的对象
 * 设置需要执行的方法
 * <p>
 * 配置定时任务的触发器，也就是什么时候出发执行定时任务
 * <p>
 * 设置触发时间
 * 触发器名称
 * <p>
 * 定义 Quartz 调度工厂
 * <p>
 * 用于 Quartz 集群， QuartzScheduler 启动时更新已存在的 Job
 * 延迟启动，应用启动 1s 后
 * 注册触发器
 * <p>
 * 配置定时任务
 * <p>
 * 是否并发执行  ？ true: 当前任务未执行完成，继续执行下一个 false : 等待上一个任务完成，再继续执行下一个
 * 设置任务名称
 * 设置任务分组，可持久化（多任务时使用）
 * 需要执行的对象
 * 设置需要执行的方法
 * <p>
 * 配置定时任务的触发器，也就是什么时候出发执行定时任务
 * <p>
 * 设置触发时间
 * 触发器名称
 * <p>
 * 定义 Quartz 调度工厂
 * <p>
 * 用于 Quartz 集群， QuartzScheduler 启动时更新已存在的 Job
 * 延迟启动，应用启动 1s 后
 * 注册触发器
 * <p>
 * 配置定时任务
 * <p>
 * 是否并发执行  ？ true: 当前任务未执行完成，继续执行下一个 false : 等待上一个任务完成，再继续执行下一个
 * 设置任务名称
 * 设置任务分组，可持久化（多任务时使用）
 * 需要执行的对象
 * 设置需要执行的方法
 * <p>
 * 配置定时任务的触发器，也就是什么时候出发执行定时任务
 * <p>
 * 设置触发时间
 * 触发器名称
 * <p>
 * 定义 Quartz 调度工厂
 * <p>
 * 用于 Quartz 集群， QuartzScheduler 启动时更新已存在的 Job
 * 延迟启动，应用启动 1s 后
 * 注册触发器
 * <p>
 * 配置定时任务
 * <p>
 * 是否并发执行  ？ true: 当前任务未执行完成，继续执行下一个 false : 等待上一个任务完成，再继续执行下一个
 * 设置任务名称
 * 设置任务分组，可持久化（多任务时使用）
 * 需要执行的对象
 * 设置需要执行的方法
 * <p>
 * 配置定时任务的触发器，也就是什么时候出发执行定时任务
 * <p>
 * 设置触发时间
 * 触发器名称
 *//*

@Configuration
public class QuartzConfig {

    private final QuartzScheduledTasks quartzScheduledTasks;

    public QuartzConfig(QuartzScheduledTasks quartzScheduledTasks) {
        this.quartzScheduledTasks = quartzScheduledTasks;
    }

    */
/**
 * 定义 Quartz 调度工厂
 *//*

    @Bean(name = "scheduler")
    public SchedulerFactoryBean schedulerFactoryBean(Trigger trigger) {
        SchedulerFactoryBean bean = new SchedulerFactoryBean();
        */
/**
 * 用于 Quartz 集群， QuartzScheduler 启动时更新已存在的 Job
 * 延迟启动，应用启动 1s 后
 * 注册触发器
 * *//*

        bean.setOverwriteExistingJobs(true);
        bean.setStartupDelay(ONE);
        bean.setTriggers(trigger);
        return bean;
    }


    */
/**
 * 配置定时任务
 *//*

    @Bean(name = "jobDetail")
    public MethodInvokingJobDetailFactoryBean detailFactoryBean(QuartzScheduledTasks quartzScheduledTasks) {
        MethodInvokingJobDetailFactoryBean jobDetailFactoryBean = new MethodInvokingJobDetailFactoryBean();
        */
/**
 *  是否并发执行  ？ true: 当前任务未执行完成，继续执行下一个 false : 等待上一个任务完成，再继续执行下一个
 *  设置任务名称
 *  设置任务分组，可持久化（多任务时使用）
 *  需要执行的对象
 *  设置需要执行的方法
 * *//*

        jobDetailFactoryBean.setConcurrent(false);
        jobDetailFactoryBean.setName("QUARTZ-");
        jobDetailFactoryBean.setGroup("IMPERATOR-QUARTZ");
        jobDetailFactoryBean.setTargetObject(quartzScheduledTasks);
        jobDetailFactoryBean.setTargetMethod("test");
        return jobDetailFactoryBean;
    }

    */
/**
 * 配置定时任务的触发器，也就是什么时候出发执行定时任务
 *//*

    @Bean(name = "jobTrigger")
    public CronTriggerFactoryBean cronTriggerFactoryBean(MethodInvokingJobDetailFactoryBean jobDetailFactoryBean) {
        CronTriggerFactoryBean triggerFactoryBean = new CronTriggerFactoryBean();
        */
/**
 * 设置触发时间
 * 触发器名称
 * *//*

        triggerFactoryBean.setJobDetail(Objects.requireNonNull(jobDetailFactoryBean.getObject()));
        triggerFactoryBean.setCalendarName("0 0/1 11 * * ?");
        triggerFactoryBean.setName("QUARTZ-");
        return triggerFactoryBean;
    }

    @Bean
    public QuartzInitializerListener quartzInitializerListener() {
        return new QuartzInitializerListener();
    }

}*/
