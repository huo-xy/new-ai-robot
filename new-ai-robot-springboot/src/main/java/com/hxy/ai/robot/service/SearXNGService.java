package com.hxy.ai.robot.service;

import com.hxy.ai.robot.model.dto.SearchResultDTO;

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
    List<SearchResultDTO> search(String query);
}
