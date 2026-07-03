package com.hxy.vector.store.reader;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.JsonReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author 霍鑫宇
 * @Description:
 * @Date Created in 2026-04-15 20:29
 * @Modified By;
 */
@Component
public class MyJsonReader {

    @Value("classpath:/document/tv.json")
    private Resource resource;

    /**
     * 读取 Json 文件
     * @return
     * */
    public List<Document> loadJson() {
        JsonReader jsonReader = new JsonReader(resource, "description", "content", "title");

        return jsonReader.get();
    }
}
