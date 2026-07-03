package com.hxy.mcp.qq.tools;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @Author 霍鑫宇
 * @Description:
 * @Date Created in 2026-04-15 11:31
 * @Modified By;
 */
@Component
@Slf4j
public class QQTool {

    @Resource
    private RestTemplate restTemplate;

    @Value("${api-key}")
    private String apiKey;

    @Tool(description = "根据 QQ 号获取 QQ 信息")
    public String getQQInfo(String qq) {
        log.info("## 获取 QQ 信息，qq: {}",qq);

        String url = String.format("https://api.guiguiya.com/api/qq_info?apiKey=%s&qq=%s",apiKey,qq);
        String result = restTemplate.getForObject(url, String.class);

        log.info("## 返参: {}", result);
        return result;
    }
}
