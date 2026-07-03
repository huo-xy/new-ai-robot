package com.hxy.vector.store.reader;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 霍鑫宇
 * @Description:
 * @Date Created in 2026-04-16 09:58
 * @Modified By;
 */
@Component
public class MyTikaWordReader {

    @Value("classpath:/document/my-word.docx")
    private Resource resource;

    public List<Document> loadWord() {
        // 创建阅读器
        TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(resource);

        List<Document> documents = tikaDocumentReader.get();

        TokenTextSplitter splitter = new TokenTextSplitter();
        return splitter.apply(documents);
    }
}
