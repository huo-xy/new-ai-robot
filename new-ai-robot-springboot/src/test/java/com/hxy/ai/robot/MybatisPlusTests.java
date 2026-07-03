package com.hxy.ai.robot;

import com.hxy.ai.robot.domain.dos.ChatDO;
import com.hxy.ai.robot.domain.mapper.ChatMapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @Author 霍鑫宇
 * @Description:
 * @Date Created in 2026-04-18 10:01
 * @Modified By;
 */
@SpringBootTest
public class MybatisPlusTests {

    @Resource
    private ChatMapper chatMapper;

    /**
     * 添加数据
     * */
    @Test
    void testInsert(){
        chatMapper.insert(ChatDO.builder()
                .uuid(UUID.randomUUID().toString())
                .summary("新对话")
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build());
    }
}
