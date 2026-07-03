package com.hxy.ai.robot.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @Author 霍鑫宇
 * @Classname
 * @Description TODO
 * @Date 2026/4/22 10:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AiCustomerServiceMdUploadedEvent {

    /**
     * t_ai_customer_service_md_storage
     * */
    private Long id;

    /**
     * 存储路径
     * */
    private String filePath;

    /**
     * 元数据
     * */
    private Map<String, Object> metadatas;
}
