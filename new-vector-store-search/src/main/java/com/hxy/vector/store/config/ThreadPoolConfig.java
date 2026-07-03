package com.hxy.vector.store.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author 霍鑫宇
 * @Description:
 * @Date Created in 2026-04-16 15:06
 * @Modified By;
 */
@Configuration
public class ThreadPoolConfig {

    /**
     * HTTP 请求线程池（IO 密集型任务）
     * @return
     * */
    @Bean("httpRequestExecutor")
    public ThreadPoolTaskExecutor httpRequestExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(50); // 核心线程数
        executor.setMaxPoolSize(200); // 最大线程数
        executor.setQueueCapacity(1000); // 任务队列容量
        executor.setKeepAliveSeconds(120); // 空间线程存活时间
        executor.setThreadNamePrefix("http-fetcher-"); // 线程名前缀
        executor.initialize(); // 初始化线程池
        return executor;
    }

    /**
     * 结果处理线程池( CPU 密集型任务)
     * @return
     * */
    @Bean("resultProcessingExecutor")
    public ThreadPoolTaskExecutor resultProcessingExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(Runtime.getRuntime().availableProcessors()); // 核心线程数
        executor.setMaxPoolSize(Runtime.getRuntime().availableProcessors() * 2); // 最大线程数 不超过核心两倍
        executor.setQueueCapacity(200);
        executor.setThreadNamePrefix("result-processor-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy()); // 拒绝策略
        executor.initialize();
        return executor;
    }
}
