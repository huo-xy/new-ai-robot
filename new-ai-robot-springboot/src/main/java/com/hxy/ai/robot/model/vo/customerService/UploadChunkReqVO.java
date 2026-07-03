package com.hxy.ai.robot.model.vo.customerService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author 霍鑫宇
 * @Classname
 * @Description TODO
 * @Date 2026/4/26 14:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UploadChunkReqVO {
    private String fileMd5;
    private String fileName;
    private Long fileSize;
    private Integer chunkNumber;
    private Integer totalChunks;
    private MultipartFile chunk;
}
