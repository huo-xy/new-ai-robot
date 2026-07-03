package com.hxy.vector.store.service;

import com.hxy.vector.store.model.SearchResult;

import java.util.List;
import java.util.Timer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @Author 霍鑫宇
 * @Description:
 * @Date Created in 2026-04-16 15:16
 * @Modified By;
 */
public interface SearchResultContentFetcherService {

    /**
     * 并发批量获取搜索结果页面的内容
     *
     * @param searchResults
     * @param timeout
     * @param unit
     * @return
     * */
    CompletableFuture<List<SearchResult>> batchFetch(List<SearchResult> searchResults,
                                                     long timeout,
                                                     TimeUnit unit);
}
