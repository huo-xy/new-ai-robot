package com.hxy.vector.store.controller;

import com.hxy.vector.store.advisor.NetworkSearchAdvisor;
import com.hxy.vector.store.model.SearchResult;
import com.hxy.vector.store.service.SearXNGService;
import com.hxy.vector.store.service.SearchResultContentFetcherService;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @Author 霍鑫宇
 * @Description:
 * @Date Created in 2026-04-16 14:33
 * @Modified By;
 */
@RestController
@RequestMapping("/network")
public class NetworkSearchController {

    @Resource
    private SearXNGService searXNGService;
    @Resource
    private SearchResultContentFetcherService searchResultContentFetcherService;
    @Resource
    private ChatClient chatClient;

    /**
     * 测试
     * @param message
     * @return
     * */
    @GetMapping(value = "/test")
    public List<SearchResult> generateStream(@RequestParam(value = "message")  String message) {
        // 获取搜索结果
        List<SearchResult> searchResults = searXNGService.search(message);

        CompletableFuture<List<SearchResult>> resultFuture = searchResultContentFetcherService.batchFetch(searchResults, 7, TimeUnit.SECONDS);

        List<SearchResult> searchResultList = resultFuture.join();

        return searchResultList;
    }

    /**
     * 流式对话
     * @param message
     * @return
     * */
    @GetMapping(value = "/chat", produces = "text/html;charset=utf-8")
    public Flux<String> chat(@RequestParam(value = "message")   String message) {
        return chatClient.prompt()
                .user(message)
                .advisors(new NetworkSearchAdvisor(searXNGService, searchResultContentFetcherService))
                .stream()
                .content();
    }
}
