package com.hxy.ai.robot.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.util.List;
import java.util.Objects;

/**
 * @Author 霍鑫宇
 * @Description:
 * @Date Created in 2026-04-19 08:45
 * @Modified By;
 */
@Data
public class PageResponse<T> extends Response<List<T>> {

    /**
     * 总记录数
     * */
    private long total = 0L;

    /**
     * 每页显示记录数
     * */
    private long size = 10L;

    /**
     * 当前页码
     * */
    private long current;

    /**
     * 总页数
     * */
    private long pages;

    /**
     * 成功响应
     * @param page Mybatis Plus 提供的分页接口
     * @param data
     * @return
     * @param <T>
     * */
    public static <T> PageResponse<T> success(IPage page, List<T> data) {
        PageResponse<T> response = new PageResponse<>();
        response.setSuccess(true);
        response.setCurrent(Objects.isNull(page) ? 1L : page.getCurrent());
        response.setTotal(Objects.isNull(page) ? 0L : page.getTotal());
        response.setSize(Objects.isNull(page) ? 10L : page.getSize());
        response.setPages(Objects.isNull(page) ? 0L : page.getPages());
        response.setData(data);
        return response;
    }

    public static <T> PageResponse<T> success(long total, long size, long current, List<T> data) {
        PageResponse<T> response = new PageResponse<>();
        response.setSuccess(true);
        response.setCurrent(current);
        response.setTotal(total);
        response.setSize(size);
        int pages = (int) Math.ceil((double) total / size);
        response.setPages(pages);
        response.setData(data);
        return response;
    }
}
