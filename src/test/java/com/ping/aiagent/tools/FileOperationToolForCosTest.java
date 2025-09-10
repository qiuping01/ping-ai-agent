package com.ping.aiagent.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FileOperationToolForCosTest {

    @Autowired  // 使用Spring注入而不是手动创建
    private FileOperationToolForCos fileOperationToolForCos;

    @Test
    void readFile() {
        String fileName = "回滚后腾讯COS测试.txt";
        String result = fileOperationToolForCos.readFile(fileName);
        System.out.println("Read result: " + result);
        Assertions.assertNotNull(result);
    }

    @Test
    void writeFile() {
        String fileName = "回滚后腾讯COS测试.txt";
        String content = "回滚后测试文件上传腾讯COS";
        String result = fileOperationToolForCos.writeFile(fileName, content);
        System.out.println("Write result: " + result);
        Assertions.assertNotNull(result);
        // 可以添加更具体的断言，比如检查是否包含成功信息
        Assertions.assertTrue(result.contains("success") || !result.contains("Error"));
    }
}