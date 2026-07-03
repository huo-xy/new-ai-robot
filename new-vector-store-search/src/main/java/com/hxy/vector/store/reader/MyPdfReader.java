package com.hxy.vector.store.reader;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author 霍鑫宇
 * @Description:
 * @Date Created in 2026-04-15 20:59
 * @Modified By;
 */
@Component
public class MyPdfReader {

    public List<Document> getDocsFromPdf() {

        PagePdfDocumentReader pdfReader = new PagePdfDocumentReader("classpath:/document/profile.pdf",
                PdfDocumentReaderConfig.builder()
                        .withPageBottomMargin(0) // 页面顶边距
                        .withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
                                .withNumberOfTopTextLinesToDelete(0) // 删除顶部文本行数为0
                                .build())
                        .withPagesPerDocument(1) // 每个文档包含一页
                        .build());

        return pdfReader.read();
    }
}
