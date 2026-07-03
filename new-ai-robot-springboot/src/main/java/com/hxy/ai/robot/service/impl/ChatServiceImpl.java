package com.hxy.ai.robot.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hxy.ai.robot.domain.dos.AiCustomerServiceFileStorageDO;
import com.hxy.ai.robot.domain.dos.ChatDO;
import com.hxy.ai.robot.domain.dos.ChatMessageDO;
import com.hxy.ai.robot.domain.dos.FileChunkInfoDO;
import com.hxy.ai.robot.domain.mapper.AiCustomerServiceFileStorageMapper;
import com.hxy.ai.robot.domain.mapper.ChatMapper;
import com.hxy.ai.robot.domain.mapper.ChatMessageMapper;
import com.hxy.ai.robot.domain.mapper.FileChunkInfoMapper;
import com.hxy.ai.robot.enums.AiCustomerServiceFileStatusEnum;
import com.hxy.ai.robot.enums.ResponseCodeEnum;
import com.hxy.ai.robot.exception.BizException;
import com.hxy.ai.robot.model.vo.chat.*;
import com.hxy.ai.robot.model.vo.customerService.CheckFileReqVO;
import com.hxy.ai.robot.model.vo.customerService.CheckFileRspVO;
import com.hxy.ai.robot.service.ChatService;
import com.hxy.ai.robot.utils.PageResponse;
import com.hxy.ai.robot.utils.Response;
import com.hxy.ai.robot.utils.StringUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @Author 霍鑫宇
 * @Description:
 * @Date Created in 2026-04-18 14:24
 * @Modified By;
 */
@Service
@Slf4j
public class ChatServiceImpl implements ChatService {

    @Resource
    private ChatMapper chatMapper;
    @Autowired
    private ChatMessageMapper chatMessageMapper;
    @Resource
    private FileChunkInfoMapper fileChunkInfoMapper;
    @Autowired
    private AiCustomerServiceFileStorageMapper aiCustomerServiceFileStorageMapper;

    /**
     * 新建对话
     *
     * @param newChatReqVO
     * @return
     */
    @Override
    public Response<NewChatRspVO> newChat(NewChatReqVO newChatReqVO) {
        // 用户发送的消息
        String message = newChatReqVO.getMessage();

        // 生成对话 UUID
        String uuid = UUID.randomUUID().toString();
        // 截全用户发送的消息，作为对话摘要
        String summary = StringUtil.truncate(message, 20);

        // 存储对话记录到数据库中
        chatMapper.insert(ChatDO.builder()
                .summary(summary)
                .uuid(uuid)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build());

        // 将摘要、UUID 返回给前端
        return Response.success(NewChatRspVO.builder()
                .uuid(uuid)
                .summary(summary)
                .build());
    }

    /**
     * 查询历史消息
     * @param findChatHistoryMessagePageListReqVO
     * @return
     */
    @Override
    public PageResponse<FindChatHistoryMessagePageListRspVO> findChatHistoryMessagePageList(FindChatHistoryMessagePageListReqVO findChatHistoryMessagePageListReqVO) {
        // 获取当前页，以及每页需要展示的数量
        Long current = findChatHistoryMessagePageListReqVO.getCurrent();
        Long size = findChatHistoryMessagePageListReqVO.getSize();
        String chatId = findChatHistoryMessagePageListReqVO.getChatId();

        // 执行分页查询
        Page< ChatMessageDO> chatMessageDOPage = chatMessageMapper.selectPageList(current, size, chatId);

        List<ChatMessageDO> chatMessageDOS = chatMessageDOPage.getRecords();
        // DO 转 VO
        List<FindChatHistoryMessagePageListRspVO> vos = null;
        if (CollectionUtil.isNotEmpty(chatMessageDOS)) {
            vos = chatMessageDOS.stream()
                    .map(chatMessageDO -> FindChatHistoryMessagePageListRspVO.builder()
                                    .id(chatMessageDO.getId())
                                    .chatId(chatMessageDO.getChatUuid())
                                    .content(chatMessageDO.getContent())
                                    .reasoning(chatMessageDO.getReasoningContent())
                                    .role(chatMessageDO.getRole())
                                    .createTime(chatMessageDO.getCreateTime())
                                    .build())
                    // 升序排序
                    .sorted(Comparator.comparing(FindChatHistoryMessagePageListRspVO::getCreateTime))
                    .collect(Collectors.toList());
        }
        return PageResponse.success(chatMessageDOPage, vos);
    }

    /**
     * 查询历史对话
     * @param findChatHistoryPageListReqVO
     * @return
     * */
    @Override
    public PageResponse<FindChatHistoryPageListRspVO> findChatHistoryPageList(FindChatHistoryPageListReqVO findChatHistoryPageListReqVO) {
        // 获取当前页，以及每页需要展示的数量
        Long current = findChatHistoryPageListReqVO.getCurrent();
        Long size = findChatHistoryPageListReqVO.getSize();
        // 执行分页查询
        Page<ChatDO> chatDOPage = chatMapper.selectPageList(current, size);

        // 获取查询结果
        List<ChatDO> chatDOS = chatDOPage.getRecords();

        // DO 转 VO
        List<FindChatHistoryPageListRspVO> vos = null;
        if (CollectionUtil.isNotEmpty(chatDOS)) {
            vos = chatDOS.stream()
                    .map(chatDO -> FindChatHistoryPageListRspVO.builder()
                            .id(chatDO.getId())
                            .uuid(chatDO.getUuid())
                            .summary(chatDO.getSummary())
                            .updateTime(chatDO.getUpdateTime())
                            .build())
                    .collect(Collectors.toList());
        }

        return PageResponse.success(chatDOPage, vos);
    }

    /**
     * 重命名对话摘要
     *
     * @param renameChatReqVO
     * @return
     * */
    @Override
    public Response<?> renameChatSummary(RenameChatReqVO renameChatReqVO) {
        // 获取对话 UUID
        Long chatId = renameChatReqVO.getId();
        // 获取新的对话摘要
        String summary = renameChatReqVO.getSummary();

        // 更新对话摘要
        chatMapper.updateById(ChatDO.builder().id(chatId).summary(summary).build());

        return Response.success();
    }

    /**
     * 删除对话
     *
     * @param deleteChatReqVO
     * @return
     * */
    @Override
    public Response<?> deleteChat(DeleteChatReqVO deleteChatReqVO) {
        // 获取对话 UUID
        String uuid = deleteChatReqVO.getUuid();
        // 删除对话
        int count = chatMapper.delete(Wrappers.<ChatDO>lambdaQuery()
                .eq(ChatDO::getUuid, uuid)
        );
        if (count == 0) {
            throw new BizException(ResponseCodeEnum.CHAT_NOT_EXISTED);
        }

        // 批量删除此对话下的消息
        chatMessageMapper.delete(Wrappers.<ChatMessageDO>lambdaQuery()
                .eq(ChatMessageDO::getChatUuid, uuid)
        );
        return Response.success();
    }


}