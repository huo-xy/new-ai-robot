package com.hxy.ai.robot.model.vo.customerService;

import com.hxy.ai.robot.model.common.BasePageQuery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * @Author 霍鑫宇
 * @Classname
 * @Description TODO
 * @Date 2026/4/23 09:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindMarkdownFilePageListReqVO extends BasePageQuery {

    /**
     * 文件名称
     * */
    private String fileName;

    /**
     * 起始日期
     * */
    private LocalDate startDate;

    /**
     * 结束日期
     * */
    private LocalDate endDate;
}
