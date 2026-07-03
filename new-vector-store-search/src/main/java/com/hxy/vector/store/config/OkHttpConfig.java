package com.hxy.vector.store.config;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @Author 霍鑫宇
 * @Description:
 * @Date Created in 2026-04-16 12:31
 * @Modified By;
 */
@Configuration
public class OkHttpConfig {

    @Bean
    public OkHttpClient okHttpClient(
            @Value("${okhttp.connect-timeout}") int connectTimeout,
            @Value("${okhttp.read-timeout}") int readTimeout,
            @Value("${okhttp.write-timeout}") int writeTimeout,
            @Value("${okhttp.max-idle-connection}") int maxIdleConnections,
            @Value("${okhttp.keep-alive-duration}") int keepAliveDuration) {

        return new OkHttpClient.Builder()
                .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(readTimeout, TimeUnit.MILLISECONDS)
                .writeTimeout(writeTimeout, TimeUnit.MILLISECONDS)
                .connectionPool(new ConnectionPool(maxIdleConnections, keepAliveDuration, TimeUnit.MINUTES))
                .build();
    }
}
