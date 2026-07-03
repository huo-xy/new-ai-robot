package com.hxy.ai.robot.domain.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author 霍鑫宇
 * @Classname
 * @Description TODO
 * @Date 2026/4/26 10:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_file_chunk_info")
public class FileChunkInfoDO {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String fileMd5;
    private Integer chunkNumber;
    private String chunkPath;
    private Long chunkSize;
    private LocalDateTime createTime;
}
