package com.hxy.ai.robot.reader;

import cn.hutool.core.collection.CollUtil;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author 霍鑫宇
 * @Classname
 * @Description TODO
 * @Date 2026/4/22 12:09
 */
@Component
public class MarkdownReader {

    /**
     * 读取 Markdown 文件为文档集合
     * @param resource
     * @param metadatas
     * @return
     * */
    public List<Document> loadMarkdown(Resource resource, Map<String, Object> metadatas) {
        MarkdownDocumentReaderConfig.Builder configBuilder = MarkdownDocumentReaderConfig.builder()
                .withHorizontalRuleCreateDocument(true)
                .withIncludeCodeBlock(false)
                .withIncludeBlockquote(false);

        if (CollUtil.isNotEmpty(metadatas)) {
            configBuilder.withAdditionalMetadata(metadatas);
        }

        MarkdownDocumentReader reader = new MarkdownDocumentReader(resource, configBuilder.build());

        return reader.get();
    }
}
