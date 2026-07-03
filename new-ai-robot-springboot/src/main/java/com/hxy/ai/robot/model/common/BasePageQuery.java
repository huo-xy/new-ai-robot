package com.hxy.ai.robot.model.common;

import lombok.Data;

/**
 * @Author 霍鑫宇
 * @Description:
 * @Date Created in 2026-04-19 08:55
 * @Modified By;
 */
@Data
public class BasePageQuery {
    /**
     * 当前页码，默认第一页
     * */
    private Long current = 1L;
    /**
     * 每页展示的数量，默认10条
     * */
    private Long size = 10L;
}
