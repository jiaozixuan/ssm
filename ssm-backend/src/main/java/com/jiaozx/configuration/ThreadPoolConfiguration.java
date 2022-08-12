package com.jiaozx.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.*;

/**
 * @ClassName ThreadPoolConfiguration
 * @Description TODO
 * @Author @jiaozx
 * @Date 2022/8/12 17:37
 * @Version 1.0
 */
@Configuration
@EnableAsync
@Slf4j
public class ThreadPoolConfiguration {

    // 核心线程池大小
    private int corePoolSize = 50;

    // 最大可创建的线程数
    private int maxPoolSize = 200;

    // 队列最大长度
    private int queueCapacity = 100;

    // 线程池维护线程所允许的空闲时间
    private int keepAliveTime = 200;

    @Bean
    public Executor myTaskAsyncPool() {
        System.out.println("************myTaskAsyncPool***************");
        VisiableThreadPoolTaskExecutor executor = new VisiableThreadPoolTaskExecutor();
        //设置线程池属性
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setKeepAliveSeconds(keepAliveTime);
        executor.setQueueCapacity(queueCapacity);
        //设置线程池前缀（可配置到yml文件中进行动态读取）
        executor.setThreadNamePrefix("jiaozx-ssm-log-");

        // setRejectedExecutionHandler：当pool已经达到线程池最大线程max size的时候，如何处理新任务
        // CallerRunsPolicy：不在新线程中执行任务，而是由调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        return executor;
    }


}
