package com.jiaozx.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.*;

/**
 * @ClassName ThreadPoolConfiguration
 * @Description 线程池配置类，用于创建和管理线程池以支持异步任务执行
 * @Author jiaozx
 * @Date 2022/8/12 17:37
 * @Version 1.0
 */
@Configuration
@EnableAsync
@Slf4j
public class ThreadPoolConfiguration {

    // 默认线程池名称标识
    public static final String DEFAULT_TASK_EXECUTOR_NAME = "myTaskAsyncPool";

    // 核心线程数：线程池中始终保持的活动线程数量
    private int corePoolSize = 50;

    // 最大线程数：线程池允许的最大线程数量（当队列满时会创建新线程直到达到此值）
    private int maxPoolSize = 200;

    // 队列容量：用来存储等待执行的任务的队列大小
    private int queueCapacity = 100;

    // 空闲线程存活时间：非核心线程在空闲多长时间后会被销毁（单位：秒）
    private int keepAliveTime = 200;

    /**
     * 创建并配置一个自定义的线程池实例。
     *
     * @return 返回配置好的线程池执行器
     */
    @Bean(DEFAULT_TASK_EXECUTOR_NAME)
    public Executor myTaskAsyncPool() {
        log.info("初始化线程池: {}", DEFAULT_TASK_EXECUTOR_NAME);
        VisiableThreadPoolTaskExecutor executor = new VisiableThreadPoolTaskExecutor();

        // 设置线程池基本属性
        executor.setCorePoolSize(corePoolSize);           // 设置核心线程数
        executor.setMaxPoolSize(maxPoolSize);             // 设置最大线程数
        executor.setKeepAliveSeconds(keepAliveTime);      // 设置空闲线程存活时间
        executor.setQueueCapacity(queueCapacity);         // 设置任务队列容量

        // 设置线程名称前缀，便于日志追踪
        executor.setThreadNamePrefix("jiaozx-ssm-log-");

        // 设置拒绝策略：当线程池已满且队列已满，由调用线程直接执行任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        // 初始化线程池
        executor.initialize();

        return executor;
    }
}
