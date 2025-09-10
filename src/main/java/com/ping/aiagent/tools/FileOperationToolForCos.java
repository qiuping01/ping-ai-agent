package com.ping.aiagent.tools;

import com.ping.aiagent.entity.FileMetadata;
import com.ping.aiagent.repository.FileMetadataRepository;
import com.ping.aiagent.service.CosService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * 文件操作工具类（操作腾讯云COS上的文件并保存元数据到数据库）
 */
@Component
public class FileOperationToolForCos {

    @Autowired
    private CosService cosService;

    @Autowired
    private FileMetadataRepository fileMetadataRepository;

    private final String COS_BASE_PATH = "ai-agent/files/";

    @Tool(description = "Read content from a file stored in cloud")
    public String readFile(@ToolParam(description = "Name of the file to read") String fileName) {
        try {
            String cosPath = COS_BASE_PATH + fileName;

            // 首先检查数据库记录是否存在
            Optional<FileMetadata> metadataOpt = fileMetadataRepository.findActiveByCosPath(cosPath);
            if (!metadataOpt.isPresent()) {
                return "Error: File not found in database";
            }

            // 从COS读取内容
            String content = cosService.downloadString(cosPath);
            return content;
        } catch (Exception e) {
            return "Error reading file from cloud: " + e.getMessage();
        }
    }

    @Tool(description = "Write content to a file stored in cloud")
    @Transactional
    public String writeFile(@ToolParam(description = "Name of the file to write") String fileName,
                            @ToolParam(description = "Content to write to the file") String content) {
        try {
            String cosPath = COS_BASE_PATH + fileName;

            // 检查是否已存在同名文件
            Optional<FileMetadata> existingFile = fileMetadataRepository.findActiveByCosPath(cosPath);
            if (existingFile.isPresent()) {
                // 可以选择覆盖或返回错误
                return "Error: File already exists. Use a different name or delete the existing file first.";
            }

            // 上传到COS
            String fileUrl = cosService.uploadString(content, cosPath);

            // 计算文件大小
            long size = content.getBytes(StandardCharsets.UTF_8).length;

            // 保存元数据到数据库
            FileMetadata metadata = new FileMetadata(
                    fileName,
                    cosPath,
                    fileUrl,
                    size,
                    null, // 如果没有用户ID，可以设为null
                    "text/plain" // 根据文件类型设置MIME类型
            );

            fileMetadataRepository.save(metadata);

            return "File written successfully to cloud. URL: " + fileUrl + ", Database ID: " + metadata.getId();
        } catch (Exception e) {
            return "Error writing file to cloud: " + e.getMessage();
        }
    }

    @Tool(description = "Delete a file from cloud storage")
    @Transactional
    public String deleteFile(@ToolParam(description = "Name of the file to delete") String fileName) {
        try {
            String cosPath = COS_BASE_PATH + fileName;

            // 查找数据库记录
            Optional<FileMetadata> metadataOpt = fileMetadataRepository.findActiveByCosPath(cosPath);
            if (!metadataOpt.isPresent()) {
                return "Error: File not found in database";
            }

            FileMetadata metadata = metadataOpt.get();

            // 从COS删除文件
            cosService.deleteFile(cosPath);

            // 软删除数据库记录（将状态设为0）
            fileMetadataRepository.softDelete(metadata.getId());

            return "File deleted successfully from cloud and database";
        } catch (Exception e) {
            return "Error deleting file: " + e.getMessage();
        }
    }
}