package com.eveindex.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试控制器
 */
@Tag(name = "测试接口", description = "用于测试API文档是否正常显示")
@RestController
@RequestMapping("/test")
public class TestController {

    @Operation(summary = "测试接口", description = "返回Hello World，验证API文档是否正常工作")
    @GetMapping("/hello")
    public String hello() {
        return "Hello World! API文档正常工作！";
    }

    @Operation(summary = "获取系统信息", description = "返回系统基本信息")
    @GetMapping("/info")
    public String getInfo() {
        return "风险监控系统 v1.0.0 - API文档测试成功";
    }
}