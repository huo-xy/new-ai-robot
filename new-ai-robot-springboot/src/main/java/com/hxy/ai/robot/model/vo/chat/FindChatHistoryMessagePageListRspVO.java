package com.hxy.ai.robot.model.vo.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author 霍鑫宇
 * @Description:
 * @Date Created in 2026-04-19 08:57
 * @Modified By;
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindChatHistoryMessagePageListRspVO {
    /**
     * 消息 ID
     * */
    private Long id;
    /**
     * 对话 ID
     * */
    private String chatId;
    /**
     * 消息内容
     * */
    private String content;
    /**
     * 消息类型
     * */
    private String role;
    /**
     * 消息发送时间
     * */
    private LocalDateTime createTime;

    /**
     * 推理内容
     * */
    private String reasoning;
}
