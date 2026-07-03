package com.hxy.ai.robot.model.vo.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author 霍鑫宇
 * @Description:
 * @Date Created in 2026-04-18 14:18
 * @Modified By;
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewChatRspVO {
    /**
     * 摘要
     * */
    private String summary;

    /**
     * 对话 UUID
     * */
    private String uuid;
}
