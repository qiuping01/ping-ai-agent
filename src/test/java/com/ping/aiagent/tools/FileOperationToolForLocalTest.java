package com.ping.aiagent.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FileOperationToolForLocalTest {

    @Test
    void readFile() {
        FileOperationToolForLocal fileOperationTool = new FileOperationToolForLocal();
        String fileName = "AI 恋爱大师.txt";
        String result = fileOperationTool.readFile(fileName);
        Assertions.assertNotNull(result);
    }

    @Test
    void writeFile() {
        FileOperationToolForLocal fileOperationTool = new FileOperationToolForLocal();
        String fileName = "AI 恋爱大师.txt";
        String content = "AI 恋爱大师的开发流程";
        String result = fileOperationTool.writeFile(fileName,content);
        Assertions.assertNotNull(result);
    }
}