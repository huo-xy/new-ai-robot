package com.hxy.ai.robot.event.listener;

import com.hxy.ai.robot.domain.dos.AiCustomerServiceFileStorageDO;
import com.hxy.ai.robot.domain.mapper.AiCustomerServiceFileStorageMapper;
import com.hxy.ai.robot.enums.AiCustomerServiceFileStatusEnum;
import com.hxy.ai.robot.event.AiCustomerServiceMdUploadedEvent;
import com.hxy.ai.robot.reader.MarkdownReader;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.FileSystemResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @Author 霍鑫宇
 * @Classname
 * @Description TODO
 * @Date 2026/4/22 10:28
 */
@Component
@Slf4j
public class AiCustomerServiceMdUploadedListener {

    @Resource
    private MarkdownReader markdownReader;
    @Resource
    private VectorStore vectorStore;
    @Resource
    private AiCustomerServiceFileStorageMapper aiCustomerServiceFileStorageMapper;
    @Resource
    private TransactionTemplate transactionTemplate;

    /**
     * Markdown 文件向量化
     * @param event
     * */
    @EventListener
    @Async("eventTaskExecutor")
    public void vectorizing(AiCustomerServiceMdUploadedEvent event) {
        log.info("## AiCustomerServiceMdUploadedEvent: {}", event);

        // 文件存储表主键 ID
        Long id = event.getId();
        // Markdown 文件存储路径
        String filePath = event.getFilePath();
        // 元数据
        Map<String, Object> metadatas = event.getMetadatas();

        // 更新存储文件的处理状态为 "向量化中"
        aiCustomerServiceFileStorageMapper.updateById(AiCustomerServiceFileStorageDO.builder()
                .id(id)
                .status(AiCustomerServiceFileStatusEnum.VECTORIZING.getCode())
                .updateTime(LocalDateTime.now())
                .build());

        // 编程式 事务
        boolean isSuccess = Boolean.TRUE.equals(transactionTemplate.execute(status -> {
            try {
                // 读取文件
                org.springframework.core.io.Resource resource = new FileSystemResource(filePath);

                // 解析为 Document 集合
                List<Document> documents = markdownReader.loadMarkdown(resource, metadatas);

                log.info("## documents: {}", documents);

                for (Document document : documents) {
                    List<Document> results = vectorStore.similaritySearch(SearchRequest.builder()
                            .query(document.getText())
                            .topK(1)
                            .build());

                    if (!results.isEmpty() && results.get(0).getScore() > 0.99)
                        continue;

                    vectorStore.add(List.of(document));
                }

                aiCustomerServiceFileStorageMapper.updateById(AiCustomerServiceFileStorageDO.builder()
                        .id(id)
                        .status(AiCustomerServiceFileStatusEnum.COMPLETED.getCode())
                        .updateTime(LocalDateTime.now())
                        .build());

                return true;
            } catch (Exception ex) {
                log.error("## Markdown 文件向量化失败：{}", event, ex);
                status.setRollbackOnly();
                return false;
            }
        }));

        if (!isSuccess) {
            aiCustomerServiceFileStorageMapper.updateById(AiCustomerServiceFileStorageDO.builder()
                    .id(id)
                    .status(AiCustomerServiceFileStatusEnum.FAILED.getCode())
                    .updateTime(LocalDateTime.now())
                    .build());
        }
    }
}
