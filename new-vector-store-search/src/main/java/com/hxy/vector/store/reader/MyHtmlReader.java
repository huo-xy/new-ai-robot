package com.hxy.vector.store.reader;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.jsoup.JsoupDocumentReader;
import org.springframework.ai.reader.jsoup.config.JsoupDocumentReaderConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author 霍鑫宇
 * @Description:
 * @Date Created in 2026-04-15 20:50
 * @Modified By;
 */
@Component
public class MyHtmlReader {

    @Value("classpath:/document/my-page.html")
    private Resource resource;

    public List<Document> loadHtml() {
        JsoupDocumentReaderConfig config = JsoupDocumentReaderConfig.builder()
                .selector("article p")
                .charset("UTF-8")
                .includeLinkUrls(true)
                .metadataTags(List.of("author", "date"))
                .additionalMetadata("source", "my-page.html")
                .build();

        JsoupDocumentReader reader = new JsoupDocumentReader(resource, config);

        return reader.get();
    }
}
