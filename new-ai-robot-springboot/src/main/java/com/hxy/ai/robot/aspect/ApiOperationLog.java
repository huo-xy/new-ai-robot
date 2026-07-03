package com.hxy.ai.robot.aspect;

import java.lang.annotation.*;

/**
 * @Author 霍鑫宇
 * @Description:
 * @Date Created in 2026-04-18 12:48
 * @Modified By;
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface ApiOperationLog {
    /**
     * API 功能描述
     *
     * @return
     * */
    String description() default "";
}
