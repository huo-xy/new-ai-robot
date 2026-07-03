package com.hxy.vector.store.reader;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author 霍鑫宇
 * @Description:
 * @Date Created in 2026-04-15 20:40
 * @Modified By;
 */
@Component
public class MyMarkdownReader {

    @Value("classpath:/document/code.md")
    private Resource resource;

    public List<Document> loadMarkdown() {
        // 阅读器配置
        MarkdownDocumentReaderConfig config = MarkdownDocumentReaderConfig.builder()
                .withHorizontalRuleCreateDocument(true) // 水平线创建新文档
                .withIncludeCodeBlock(false) // 排除代码块
                .withIncludeBlockquote(false) // 排除块引用
                .withAdditionalMetadata("filename", "code.md") // 添加自定义元数据
                .build();

        // 新建阅读器
        MarkdownDocumentReader reader = new MarkdownDocumentReader(resource, config);

        // 读取并转化为 Document 文档集合
        return reader.get();
    }
}
