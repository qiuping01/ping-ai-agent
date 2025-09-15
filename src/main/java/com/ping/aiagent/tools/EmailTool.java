package com.ping.aiagent.tools;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * 邮件发送工具
 * 提供简单文本邮件和复杂MIME邮件的发送功能
 * 所有邮件都使用配置中已认证的默认发件人地址发送
 */
@Component
public class EmailTool {

    private final JavaMailSender mailSender;
    private final String defaultFrom;

    public EmailTool(JavaMailSender mailSender,
                     @Value("${spring.mail.username}") String defaultFrom) {
        this.mailSender = mailSender;
        this.defaultFrom = defaultFrom; // 从配置中获取默认发件人
    }

    /**
     * 发送简单文本邮件
     * 使用配置中的默认发件人地址发送简单文本邮件
     *
     * @param subject 邮件主题
     * @param content 邮件内容（纯文本）
     * @param to 收件人邮箱地址
     * @return 发送结果描述
     */
    @Tool(name = "sendEmailSimple", description = "Send a simple text email without attachments")
    public String sendSimpleEmail(
            @ToolParam(description = "Email subject") String subject,
            @ToolParam(description = "Email content") String content,
            @ToolParam(description = "Recipient email address") String to) {
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setSubject(subject);
            mail.setText(content);
            mail.setTo(to);
            mail.setFrom(defaultFrom); // 使用配置的默认发件人
            mailSender.send(mail);
            return "Simple email sent successfully to " + to;
        } catch (Exception e) {
            return "Error sending simple email: " + e.getMessage();
        }
    }

    /**
     * 发送MIME邮件
     * 使用配置中的默认发件人地址发送MIME邮件
     * 支持HTML内容和附件
     *
     * @param subject 邮件主题
     * @param content 邮件内容（可以是HTML）
     * @param to 收件人邮箱地址
     * @param attachmentPath 附件文件路径（可选）
     * @return 发送结果描述
     */
    @Tool(name = "sendEmailMime", description = "Send a MIME email that can contain HTML content and attachments")
    public String sendMimeEmail(
            @ToolParam(description = "Email subject") String subject,
            @ToolParam(description = "Email content (can be HTML)") String content,
            @ToolParam(description = "Recipient email address") String to,
            @ToolParam(description = "Path to attachment file (optional)") String attachmentPath) {
        try {
            MimeMessage mail = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setSubject(subject);
            helper.setText(content,true);
            helper.setTo(to);
            helper.setFrom(defaultFrom); // 使用配置的默认发件人

            if (attachmentPath != null && !attachmentPath.trim().isEmpty()) {
                File file = new File(attachmentPath);
                if (file.exists() && file.isFile()) {
                    helper.addAttachment(file.getName(), file);
                } else {
                    return "Error: Attachment file not found at path: " + attachmentPath;
                }
            }

            mailSender.send(mail);
            return "MIME email sent successfully to " + to;
        } catch (MessagingException e) {
            return "Error sending MIME email: " + e.getMessage();
        }
    }
}