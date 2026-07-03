package com.hxy.ai.robot.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hxy.ai.robot.domain.dos.FileChunkInfoDO;

import java.util.List;

/**
 * @Author 霍鑫宇
 * @Classname
 * @Description TODO
 * @Date 2026/4/26 10:04
 */
public interface FileChunkInfoMapper extends BaseMapper<FileChunkInfoDO> {

    /**
     * 根据文件 MD5 值查询所有已上传的分片
     * @param fileMd5
     * @return
     * */
    default List<FileChunkInfoDO> selectChunkedList(String fileMd5) {
        return selectList(
                Wrappers.<FileChunkInfoDO>lambdaQuery()
                        .eq(FileChunkInfoDO::getFileMd5, fileMd5)
                        .orderByAsc(FileChunkInfoDO::getChunkNumber)
        );
    }

    /**
     * 查询指定分片是否已被上传
     * @param fileMd5
     * @param chunkNum
     * @return
     * */
    default Long selectCountByMd5AndChunkNum(String fileMd5, Integer chunkNum) {
        return selectCount(
                Wrappers.<FileChunkInfoDO>lambdaQuery()
                        .eq(FileChunkInfoDO::getFileMd5, fileMd5)
                        .eq(FileChunkInfoDO::getChunkNumber, chunkNum)
        );
    }

    /**
     * 根据文件 MD5 删除记录
     * @param fileMd5
     * @return
     * */
    default int deleteByMd5(String fileMd5) {
        return delete(Wrappers.<FileChunkInfoDO>lambdaQuery()
                .eq(FileChunkInfoDO::getFileMd5, fileMd5));
    }

}
