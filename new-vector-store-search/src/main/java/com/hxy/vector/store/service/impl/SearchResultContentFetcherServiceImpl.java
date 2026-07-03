package com.hxy.vector.store.service.impl;

import com.hxy.vector.store.model.SearchResult;
import com.hxy.vector.store.service.SearchResultContentFetcherService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author 霍鑫宇
 * @Description:
 * @Date Created in 2026-04-16 15:19
 * @Modified By;
 */
@Service
@Slf4j
public class SearchResultContentFetcherServiceImpl implements SearchResultContentFetcherService {

    @Resource
    private OkHttpClient okHttpClient;

    @Resource(name = "resultProcessingExecutor")
    private ThreadPoolTaskExecutor processingExecutor;

    /**
     * 并发批量获取搜索结果页面的内容
     *
     * @param searchResults
     * @param timeout
     * @param unit
     * @return
     * */
    @Override
    public CompletableFuture<List<SearchResult>> batchFetch(List<SearchResult> searchResults, long timeout, TimeUnit unit) {
        List<CompletableFuture<SearchResult>> futures = searchResults.stream()
                .map(result -> asyncFetchContentForResult(result, timeout, unit))
                .toList();

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0])
        );

        return allFutures.thenApplyAsync(v ->
                futures.stream()
                        .map(future -> {
                            SearchResult searchResult = future.join();
                            String html = searchResult.getContent();
                            if (StringUtils.isNotBlank(html)) {
                                searchResult.setContent(Jsoup.parse(html).text());
                            }

                            return searchResult;
                        })
                        .collect(Collectors.toList()),
                processingExecutor
        );
    };

    /**
     * 同步获取指定 URL 的 HTML 内容
     * @param url
     * @return
     * */
    private String syncFetchHtmlContent(String url) {
        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful() || response.body() == null) {
                return "";
            }

            return response.body().string();
        } catch (IOException e) {
            return "";
        }
    }

    @Resource(name = "httpRequestExecutor")
    private ThreadPoolTaskExecutor httpExecutor;

    /**
     * 异步获取单个 SearchResult 对象对应的页面内容
     * @param result
     * @param timeout
     * @param unit
     * @return
     * */
    private CompletableFuture<SearchResult> asyncFetchContentForResult(
            SearchResult result,
            long timeout,
            TimeUnit unit) {
        // 异步线程处理
        return CompletableFuture.supplyAsync(() -> {
            String html = syncFetchHtmlContent(result.getUrl());

            return SearchResult.builder()
                    .url(result.getUrl())
                    .score(result.getScore())
                    .content(html)
                    .build();
        }, httpExecutor)
                .completeOnTimeout(createFallbackResult(result), timeout, unit)
                .exceptionally(e -> {
                    log.error("## 获取页面内容异常，URL：{}", result.getUrl(), e);
                    return createFallbackResult(result);
                });
    }

    /**
     * 创建回退结果
     * */
    private SearchResult createFallbackResult(SearchResult searchResul) {
        return SearchResult.builder()
                .url(searchResul.getUrl())
                .score(searchResul.getScore())
                .content("")
                .build();
    }
}
