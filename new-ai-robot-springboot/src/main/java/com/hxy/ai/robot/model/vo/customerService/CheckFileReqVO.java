package com.hxy.ai.robot.model.vo.customerService;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author 霍鑫宇
 * @Classname
 * @Description TODO
 * @Date 2026/4/26 10:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckFileReqVO {

    @NotBlank(message = "文件 MD5 不能为空")
    private String fileMd5;
}
