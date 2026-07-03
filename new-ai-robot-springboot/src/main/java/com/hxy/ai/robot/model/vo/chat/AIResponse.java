package com.hxy.ai.robot.model.vo.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author 霍鑫宇
 * @Description:
 * @Date Created in 2026-04-14 16:34
 * @Modified By;
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AIResponse {

    // 流式响应内容
    private String v;

    // 推理过程
    private String reasoning;
}
