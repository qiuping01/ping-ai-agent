package com.ping.aiagent.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FileOperationToolForLocalTest {

    @Test
    void readFile() {
        FileOperationToolForLocal fileOperationToolForLocal = new FileOperationToolForLocal();
        String fileName = "AI 恋爱大师.txt";
        String result = fileOperationToolForLocal.readFile(fileName);
        Assertions.assertNotNull(result);
    }

    @Test
    void writeFile() {
        FileOperationToolForLocal fileOperationToolForLocal = new FileOperationToolForLocal();
        String fileName = "AI 恋爱大师.txt";
        String content = "AI 恋爱大师的开发流程，愿天下有情人终成眷属";
        String result = fileOperationToolForLocal.writeFile(fileName,content);
        Assertions.assertNotNull(result);
    }
}