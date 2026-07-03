package com.hxy.vector.store.reader;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * @Author 霍鑫宇
 * @Description:
 * @Date Created in 2026-04-15 20:18
 * @Modified By;
 */
@Component
public class MyTextReader {

    @Value("classpath:/document/manual.txt")
    private Resource resource;

    /**
     * 读取 Txt 文档
     * @return
     * */
    public List<Document> loadText() {
        // 创建 TextReader 对象
        TextReader textReader = new TextReader(resource);
        // 添加自定义元件
        textReader.getCustomMetadata()
                .put("filename", "manual.txt");
        // 读取并转换为Document 文档集合
        return textReader.read();
    }

    /**
     * 读取 Txt 文档并分块拆分
     * @return
     * */
    public List<Document> loadTextAndSplit() {
        TextReader textReader = new TextReader(resource);

        List<Document> documents = textReader.get();

        List<Document> splitDocuments = new TokenTextSplitter().apply(documents);

        return splitDocuments;
    }
}
