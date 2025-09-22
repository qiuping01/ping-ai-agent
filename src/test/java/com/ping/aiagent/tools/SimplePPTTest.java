package com.ping.aiagent.tools;

/**
 * 简单的PPT工具测试类
 * 解决依赖导入问题后的快速验证
 */
public class SimplePPTTest {
    
    public static void main(String[] args) {
        System.out.println("=== PPT生成工具简单测试 ===\n");
        
        PPTGenerationTool pptTool = new PPTGenerationTool();
        
        // 测试1: 基础PPT生成
        testBasicGeneration(pptTool);
        
        // 测试2: 结构化PPT生成
        testStructuredGeneration(pptTool);
        
        // 测试3: 中文内容测试
        testChineseContent(pptTool);
        
        System.out.println("=== 测试完成 ===");
        System.out.println("请检查生成的PPT文件是否正常");
    }
    
    private static void testBasicGeneration(PPTGenerationTool tool) {
        System.out.println("测试1: 基础PPT生成");
        
        String content = """
            ###TITLE###AI工具测试演示
            这是一个测试演示文稿
            ---SLIDE---
            ###TITLE###功能特点
            • 支持中文字符
            • 多种主题颜色
            • 自动布局调整
            • 简单易用的API
            ---SLIDE---
            ###TITLE###技术实现
            使用Apache POI库
            支持PPTX格式
            兼容Office和WPS
            """;
        
        try {
            String result = tool.generatePPT("basic_test.pptx", content, 0);
            System.out.println("结果: " + result);
        } catch (Exception e) {
            System.out.println("错误: " + e.getMessage());
        }
        System.out.println();
    }
    
    private static void testStructuredGeneration(PPTGenerationTool tool) {
        System.out.println("测试2: 结构化PPT生成");
        
        String slidesContent = """
            项目概述|• 项目名称：AI办公助手
            • 开发周期：3个月
            • 团队规模：5人
            • 技术栈：Java + Spring Boot
            ---
            核心功能|• PDF文档生成
            • PPT演示文稿创建
            • 文档格式转换
            • 智能内容分析
            ---
            项目进展|• 需求分析：已完成
            • 系统设计：已完成
            • 开发实现：进行中
            • 测试部署：计划中
            """;
        
        try {
            String result = tool.generateStructuredPPT("structured_test.pptx", "项目进度汇报", slidesContent, 1);
            System.out.println("结果: " + result);
        } catch (Exception e) {
            System.out.println("错误: " + e.getMessage());
        }
        System.out.println();
    }
    
    private static void testChineseContent(PPTGenerationTool tool) {
        System.out.println("测试3: 中文内容测试");
        
        String chineseContent = """
            ###TITLE###中文字符测试
            测试各种中文字符的显示效果
            ---SLIDE---
            ###TITLE###常用汉字
            • 你好世界
            • 春夏秋冬
            • 东南西北
            • 金木水火土
            ---SLIDE---
            ###TITLE###特殊符号
            • 人民币符号：￥
            • 中文标点：，。！？；：
            • 中文括号：（）【】
            • 书名号：《》
            """;
        
        try {
            String result = tool.generatePPT("chinese_test.pptx", chineseContent, 2);
            System.out.println("结果: " + result);
        } catch (Exception e) {
            System.out.println("错误: " + e.getMessage());
        }
        System.out.println();
    }
}