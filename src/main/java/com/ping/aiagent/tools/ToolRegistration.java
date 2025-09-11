package com.ping.aiagent.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbacks;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 集中的工具注册类
 */
@Slf4j
@Configuration
public class ToolRegistration {

    @Value("${search-api.api-key}")
    private String searchApiKey;

    /**
     * 1. 工厂模式：创建复杂对象 / 多个对象
     * 2. 依赖注入模式：api 注入
     * 3. 注册模式：作为一个中央的集中注册点，替换工具删掉即可
     * 4. 适配器模式的应用：本质就是一个接头 / 转换器
     * @return
     */
    @Bean
    public ToolCallback[] allTools() {
        log.info("创建工具调用..."); // 添加日志
        FileOperationToolForLocal fileOperationToolForLocal = new FileOperationToolForLocal();
        PDFGenerationTool pdfGenerationTool = new PDFGenerationTool();
        ResourceDownloadTool resourceDownloadTool = new ResourceDownloadTool();
        TerminalOperationTool terminalOperationTool = new TerminalOperationTool();
        WebSearchTool webSearchTool = new WebSearchTool(searchApiKey);
        WebScrapingTool webScrapingTool = new WebScrapingTool();
        return ToolCallbacks.from(
                fileOperationToolForLocal,
                pdfGenerationTool,
                resourceDownloadTool,
                terminalOperationTool,
                webSearchTool,
                webScrapingTool
        );
    }
}
