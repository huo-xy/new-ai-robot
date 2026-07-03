package com.hxy.ai.robot.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @Author 霍鑫宇
 * @Description:
 * @Date Created in 2026-04-18 14:20
 * @Modified By;
 */
public class StringUtil {
    /**
     * 截取用户问题的前面部分文字作为摘要
     *
     * @param message
     * @param maxLength
     * @return
     * */
    public static String truncate(String message, int maxLength) {
        if (StringUtils.isBlank(message)) {
            return "";
        }

        String trimmed = message.trim();

        if (trimmed.length() <= maxLength) {
            return trimmed;
        }

        return trimmed.substring(0, maxLength);
    }
}
