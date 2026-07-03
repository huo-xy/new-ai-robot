package com.hxy.ai.robot.model.vo.chat;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @Author 霍鑫宇
 * @Description:
 * @Date Created in 2026-04-19 09:56
 * @Modified By;
 */
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class RenameChatReqVO {

    @NotNull(message = "对话 ID 不能为空")
    private Long id;

    @NotBlank(message = "对话摘要不能为空")
    private String summary;
}
