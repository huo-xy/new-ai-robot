package com.hxy.vector.store.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author 霍鑫宇
 * @Description:
 * @Date Created in 2026-04-16 12:41
 * @Modified By;
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchResult {

    /**
     * 页面访问链接
     * */
    private String url;

    /**
     * 相关性评分
     * */
    private Double score;

    /**
     * 页面内容
     * */
    private String content;
}
