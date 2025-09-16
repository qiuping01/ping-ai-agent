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
                我的另一半居住在上海静安区，请帮我找到 5 公里内合适的约会地点，
                并结合一些网络图片，制定一份详细的约会计划，
                计划字数控制在800字左右，
                并以 PDF 格式输出，注意一点：“不要把markdown语法嵌入到PDF中了，避免使用markdown标识符，尽量以传统word文字格式展示”，
                最后以邮件的形式将 PDF 发送给邮箱：“16608683257@163.com”
                """;
        String answer = qpManus.run(userPrompt);
        Assertions.assertNotNull(answer);
    }
}