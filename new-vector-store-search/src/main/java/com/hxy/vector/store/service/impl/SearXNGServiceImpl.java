package com.hxy.vector.store.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hxy.vector.store.config.OkHttpConfig;
import com.hxy.vector.store.model.SearchResult;
import com.hxy.vector.store.service.SearXNGService;
import jakarta.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @Author 霍鑫宇
 * @Description:
 * @Date Created in 2026-04-16 14:04
 * @Modified By;
 */
@Service
@Slf4j
public class SearXNGServiceImpl implements SearXNGService {

    @Resource
    private OkHttpConfig okHttpConfig;
    @Resource
    private ObjectMapper objectMapper;
    @Value("${searxng.url}")
    private String searxngUrl;
    @Value("${searxng.count}")
    private int count;
    @Autowired
    private OkHttpClient okHttpClient;

    @Override
    public List<SearchResult> search(String query) {
        // 请求 URL
        HttpUrl httpUrl = HttpUrl.parse(searxngUrl).newBuilder()
                .addQueryParameter("q", query)
                .addQueryParameter("format", "json")
                .addQueryParameter("engines", "wolframalpha,presearch,seznam,mvmbl,encyclosearch,bpb,mojeek,right dao,wikimini,crowdview,searchmysite,bing,naver,360search")
                .build();

        // 创建 Http Get 请求
        Request request = new Request.Builder()
                .url(httpUrl)
                .get()
                .build();

        // 发送 HTTP 请求
        try (Response response = okHttpClient.newCall(request).execute()) {
            // 判断是否成功
            if (response.isSuccessful()) {
                // 拿到返回结果
                String result = response.body().string();
                log.info("## SearXNG 搜索结果：{}", result);

                // 解析 JSON 响应
                JsonNode root = objectMapper.readTree(result);
                JsonNode results = root.get("results"); // 获取结果数组

                // 定义 Record 类型：临时存储评分和节点引用
                record NodeWithUrlAndScore(double score, JsonNode node) {}

                // 处理搜索结果流
                // 提取评分
                // 按评分降序排列
                // 限制返回结果的数量
                List<NodeWithUrlAndScore> nodeWithScore = StreamSupport.stream(results.spliterator(), false)
                        .map(node -> {
                            // 只提取分数 避免构建完整对象
                            double score = node.path("score").asDouble(0.0); // 提取评分
                            return new NodeWithUrlAndScore(score, node);
                        })
                        .sorted(Comparator.comparingDouble(NodeWithUrlAndScore::score).reversed()) // 按评分降序
                        .limit(count) // 限制返回结果的数量
                        .toList();

                // 转换为 SearchResult 对象集合
                return nodeWithScore.stream()
                        .map(n -> {
                            JsonNode node = n.node();
                            String originalUrl = node.path("url").asText(""); // 提取 URL
                            return SearchResult.builder()
                                    .url(originalUrl)
                                    .score(n.score()) // 保留评分
                                    .build();
                        })
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            log.error("", e);
        }
        // 返回空集合
        return Collections.emptyList();
    }
}
