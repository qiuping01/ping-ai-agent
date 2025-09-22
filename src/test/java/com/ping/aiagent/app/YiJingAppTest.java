package com.ping.aiagent.app;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class YiJingAppTest {

    @Resource
    private YiJingApp yiJingApp;

    @Test
    void doChat() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "你好，我是程序员秋棠";
        String answer = yiJingApp.doChat(message, chatId);
        // 第二轮
        message = "我是男，随机数789";
        answer = yiJingApp.doChat(message, chatId);
        assertNotNull(answer);
        // 第三轮
        message = "我的另一半叫什么来着？刚跟你说过，帮我回忆一下";
        answer = yiJingApp.doChat(message, chatId);
        assertNotNull(answer);
    }
}