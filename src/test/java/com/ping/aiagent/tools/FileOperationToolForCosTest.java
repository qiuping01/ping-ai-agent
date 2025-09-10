package com.ping.aiagent.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional // 确保测试后回滚数据库操作
class FileOperationToolForCosTest {

    @Autowired
    private FileOperationToolForCos fileOperationToolForCos;

    @Test
    void readFile() {
        String fileName = "腾讯COS测试3.txt";
        String result = fileOperationToolForCos.readFile(fileName);
        System.out.println("Read result: " + result);
        Assertions.assertNotNull(result);
    }

    @Test
    void writeFile() {
        String fileName = "腾讯COS测试3.txt";
        String content = "测试文件上传腾讯COS333333333333333";
        String result = fileOperationToolForCos.writeFile(fileName, content);
        System.out.println("Write result: " + result);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.contains("success") || result.contains("ID"));
    }

    @Test
    void deleteFile() {
        String fileName = "腾讯COS测试3.txt";
        String result = fileOperationToolForCos.deleteFile(fileName);
        System.out.println("Delete result: " + result);
        Assertions.assertNotNull(result);
    }
}