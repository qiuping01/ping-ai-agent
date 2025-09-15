package com.ping.aiagent.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmailToolTest {

    @Value("${spring.mail.username}")
    private String defaultFromEmail;

    @Autowired
    private JavaMailSender mailSender;

    @Test
    void sendSimpleEmail() {
        String Subject = "邮件标题";
        String content = "Hello 12153!";
        String to = "16608683257@163.com";
        EmailTool emailTool = new EmailTool(mailSender, defaultFromEmail);
        String result = emailTool.sendSimpleEmail(Subject, content, to);
        Assertions.assertNotNull(result);
        System.out.println("邮件发送成功");
    }
}