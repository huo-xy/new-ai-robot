package com.hxy.vector.store.service;

import com.hxy.vector.store.model.SearchResult;

import java.util.List;

/**
 * @Author 霍鑫宇
 * @Description:
 * @Date Created in 2026-04-16 14:05
 * @Modified By;
 */
public interface SearXNGService {

    /**
     * 调用 SearXNG Api 获取搜索结果
     * @param query 搜索关键词
     * @return
     * */
    List<SearchResult> search(String query);
}
