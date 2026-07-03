package com.hxy.ai.robot.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hxy.ai.robot.domain.dos.AiCustomerServiceFileStorageDO;

import java.time.LocalDate;
import java.util.Objects;

/**
 * @Author 霍鑫宇
 * @Classname
 * @Description TODO
 * @Date 2026/4/22 09:26
 */
public interface AiCustomerServiceFileStorageMapper extends BaseMapper<AiCustomerServiceFileStorageDO> {

    /**
     * 分页查询
     * @param current
     * @param size
     * @return
     * */
    default Page<AiCustomerServiceFileStorageDO> selectPageList(Long current, Long size, String fileName, LocalDate startDate, LocalDate endDate) {
        Page<AiCustomerServiceFileStorageDO> page = new Page<>(current, size);

        LambdaQueryWrapper<AiCustomerServiceFileStorageDO> wrapper = Wrappers.<AiCustomerServiceFileStorageDO>lambdaQuery()
                .like(StringUtils.isNotBlank(fileName), AiCustomerServiceFileStorageDO::getFileName, fileName)
                .ge(Objects.nonNull(startDate), AiCustomerServiceFileStorageDO::getCreateTime, startDate)
                .le(Objects.nonNull(endDate), AiCustomerServiceFileStorageDO::getCreateTime, endDate)
                .orderByDesc(AiCustomerServiceFileStorageDO::getCreateTime); // 创建时间倒序

        return selectPage(page, wrapper);
    }

    /**
     * 根据文件 MD5 值查询
     * @param fileMd5
     * @return
     * */
    default AiCustomerServiceFileStorageDO selectByMd5(String fileMd5) {
        return selectOne(Wrappers.<AiCustomerServiceFileStorageDO>lambdaQuery()
                .eq(AiCustomerServiceFileStorageDO::getFileMd5, fileMd5));
    }

    /**
     * 已上传分片数 +1
     * @param id
     * @return
     * */
    default int incrementUploadedChunks(Long id) {
        return update(Wrappers.<AiCustomerServiceFileStorageDO>lambdaUpdate()
                .eq(AiCustomerServiceFileStorageDO::getId, id)
                .setSql("uploaded_chunks = uploaded_chunks + 1"));
    }
}
