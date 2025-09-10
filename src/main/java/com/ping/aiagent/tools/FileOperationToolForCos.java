package com.ping.aiagent.tools;

import com.ping.aiagent.service.CosService; // 引入刚才写的Service
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 文件操作工具类（现在操作的是腾讯云COS上的文件）
 */
@Component // 添加Component注解以便注入
public class FileOperationToolForCos {

    @Autowired
    private CosService cosService; // 注入COS服务

    // 你可以不再需要本地的 FILE_DIR 了，因为文件都存在COS上。
    // 但你可能需要一个在COS上存储的“基础路径”概念，例如 "ai-agent/files/"
    private final String COS_BASE_PATH = "ai-agent/files/";

    @Tool(description = "Read content from a file stored in cloud")
    public String readFile(@ToolParam(description = "Name of the file to read") String fileName) {
        try {
            String cosPath = COS_BASE_PATH + fileName; // 在COS上的完整路径
            String content = cosService.downloadString(cosPath);
            return content;
        } catch (Exception e) {
            return "Error reading file from cloud: " + e.getMessage();
        }
    }

    @Tool(description = "Write content to a file stored in cloud")
    public String writeFile(@ToolParam(description = "Name of the file to write") String fileName,
                            @ToolParam(description = "Content to write to the file") String content) {
        try {
            String cosPath = COS_BASE_PATH + fileName; // 在COS上的完整路径
            String fileUrl = cosService.uploadString(content, cosPath);
            // 返回的不再是本地路径，而是文件的云端访问路径（或预签名URL）
            return "File written successfully to cloud. URL: " + fileUrl;
        } catch (Exception e) {
            return "Error writing file to cloud: " + e.getMessage();
        }
    }

    // 你可以根据需要添加其他方法，例如删除文件
}