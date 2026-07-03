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
 * @Date Created in 2026-04-16 10:08
 * @Modified By;
 */
@Component
public class MyTikaPptReader {

    @Value("classpath:/document/my-ppt.pptx")
    private Resource resource;

    public List<Document> loadPpt() {

        TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(resource);

        List<Document> documents = tikaDocumentReader.get();

        TokenTextSplitter splitter = new TokenTextSplitter(1000, 400, 10, 5000, true);
        return splitter.apply(documents);
    }
}
