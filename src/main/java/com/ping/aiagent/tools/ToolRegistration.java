package com.ping.aiagent.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbacks;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

@Slf4j
@Configuration
public class ToolRegistration {

    @Value("${search-api.api-key}")
    private String searchApiKey;

    @Value("${spring.mail.username}")
    private String defaultFromEmail;

    private final JavaMailSender mailSender;

    public ToolRegistration(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Bean
    public ToolCallback[] allTools() {
        log.info("创建工具调用...");
        FileOperationToolForLocal fileOperationToolForLocal = new FileOperationToolForLocal();
        PDFGenerationTool pdfGenerationTool = new PDFGenerationTool();
        ResourceDownloadTool resourceDownloadTool = new ResourceDownloadTool();
        TerminalOperationTool terminalOperationTool = new TerminalOperationTool();
        WebSearchTool webSearchTool = new WebSearchTool(searchApiKey);
        WebScrapingTool webScrapingTool = new WebScrapingTool();
        // 添加默认发件人
        EmailTool emailTool = new EmailTool(mailSender, defaultFromEmail);
        LoveDocumentTool loveDocumentTool = new LoveDocumentTool(emailTool, defaultFromEmail);
        return ToolCallbacks.from(
                fileOperationToolForLocal,
                pdfGenerationTool,
                resourceDownloadTool,
                terminalOperationTool,
                webSearchTool,
                webScrapingTool,
                emailTool,
                loveDocumentTool
        );
    }
}