package com.ping.aiagent.agent;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class QpManusTest {

    @Resource
    private QpManus qpManus;

    @Test
    void run() {
        String userPrompt = """  
                写一个50字的日记，
                并以 PDF 格式输出，
                最后以邮件的形式将 PDF 添加为邮件的附件发送给邮箱：“16608683257@163.com”
                """;
        String answer = qpManus.run(userPrompt);
        Assertions.assertNotNull(answer);
    }
}