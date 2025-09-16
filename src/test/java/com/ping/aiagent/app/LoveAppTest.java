package com.ping.aiagent.app;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
@Slf4j
@SpringBootTest
class LoveAppTest {

    @Resource
    private LoveApp loveApp;

    @Test
    void testChat() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "你好，我是程序员秋棠";
        String answer = loveApp.doChat(message, chatId);
        // 第二轮
        message = "我想让另一半（Java 知识星球）更爱我";
        answer = loveApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        // 第三轮
        message = "我的另一半叫什么来着？刚跟你说过，帮我回忆一下";
        answer = loveApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChat() {
    }

    @Test
    void doChatWithReport() {
        String chatId = UUID.randomUUID().toString();
        String message = "你好，我是程序员秋棠，正在热恋中，我想让我的另一半(Java知识星球)更爱我，但我不知道该怎么做";
        LoveApp.LoveReport loveReport = loveApp.doChatWithReport(chatId, message);
        Assertions.assertNotNull(loveReport);
    }

    @Test
    void doChatWithRag() {
        String chatId = UUID.randomUUID().toString();
        String message = "我已经结婚了，但是婚后关系不太亲密，怎么办？";
        String answer = loveApp.doChatWithRag(message, chatId);
        Assertions.assertNotNull(answer);
    }

    // AI 调用工具能力
    @Test
    void doChatWithTools() {
        // 测试联网搜索问题的答案
        testMessage("国庆想带女朋友在十堰约会，推荐详细的适合情侣的小众打卡地？");
//
//        // 测试网页抓取：恋爱案例分析
//        testMessage("最近和对象吵架了，看看csdn网站:(https://blog.csdn.net/DD2187718660?spm=1000.2115.3001.5343)的其他情侣是怎么解决矛盾的？");
//
//        // 测试资源下载：图片下载
//        testMessage("直接下载一张适合做手机壁纸的星空情侣图片为文件");
//
//        // 测试终端操作：执行代码
//        testMessage("执行 Python3 脚本来生成数据分析报告");
//
//        // 测试文件操作：保存用户档案
//        testMessage("保存我的恋爱档案为文本文件（命名为:恋爱报告.txt）");
//
        // 测试 PDF 生成
        testMessage("生成一份关于十堰的‘国庆约会计划’PDF" +
                "，包含详细的餐厅预订、活动流程和礼物清单，并将其发送给邮箱16608683257@163.com");

        // 测试 邮箱 发送附件
        testMessage("我和女朋友现在异地，我很想她，怎么办？" +
                "，将解决方法以纯文本形式发送给邮箱16608683257@163.com");
    }

    private void testMessage(String message) {
        String chatId = UUID.randomUUID().toString();
        String answer = loveApp.doChatWithTools(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithMcp() {
        String chatId = UUID.randomUUID().toString();
        // 测试地图 MCP
//        String message = "我的另一半居住在上海静安区，请帮我找到5公里内合适的约会地点";
//        String result = loveApp.doChatWithMcp(message, chatId);
//        Assertions.assertNotNull(result);
        // 测试图片搜索 MCP
        String message = "帮我搜索一些哄另一半开心的图片";
        String answer = loveApp.doChatWithMcp(message, chatId);
        Assertions.assertNotNull(answer);
    }
}