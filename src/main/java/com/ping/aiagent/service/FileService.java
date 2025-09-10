package com.ping.aiagent.service;

import com.ping.aiagent.entity.FileMetadata;
import com.ping.aiagent.repository.FileMetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

@Service
public class FileService {

    @Autowired
    private CosService cosService;
    
    @Autowired
    private FileMetadataRepository fileMetadataRepository;

    /**
     * 上传文件到COS并保存元数据到数据库
     */
    @Transactional
    public FileUploadResult uploadFile(String content, String fileName, Long userId, String mimeType) {
        try {
            // 生成COS路径（添加时间戳避免重名）
            String cosPath = "ai-agent/files/" + System.currentTimeMillis() + "_" + fileName;
            
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
                userId, 
                mimeType
            );
            fileMetadataRepository.save(metadata);
            
            return new FileUploadResult(true, "上传成功", fileUrl, metadata.getId());
        } catch (Exception e) {
            return new FileUploadResult(false, "上传失败: " + e.getMessage(), null, null);
        }
    }
    
    /**
     * 从COS读取文件内容
     */
    public String readFile(String cosPath) {
        try {
            return cosService.downloadString(cosPath);
        } catch (Exception e) {
            throw new RuntimeException("读取文件失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 删除文件（从COS和数据库）
     */
    @Transactional
    public boolean deleteFile(Long fileId) {
        try {
            // 从数据库获取文件元数据
            FileMetadata metadata = fileMetadataRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("文件不存在"));
            
            // 从COS删除文件
            cosService.deleteFile(metadata.getCosPath());
            
            // 从数据库删除元数据（或标记为删除状态）
            fileMetadataRepository.deleteById(fileId);
            
            return true;
        } catch (Exception e) {
            throw new RuntimeException("删除文件失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 文件上传结果对象
     */
    public static class FileUploadResult {
        private boolean success;
        private String message;
        private String fileUrl;
        private Long fileId;
        
        // 构造函数
        public FileUploadResult(boolean success, String message, String fileUrl, Long fileId) {
            this.success = success;
            this.message = message;
            this.fileUrl = fileUrl;
            this.fileId = fileId;
        }
        
        // Getter和Setter方法
        public boolean isSuccess() {
            return success;
        }
        
        public void setSuccess(boolean success) {
            this.success = success;
        }
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
        
        public String getFileUrl() {
            return fileUrl;
        }
        
        public void setFileUrl(String fileUrl) {
            this.fileUrl = fileUrl;
        }
        
        public Long getFileId() {
            return fileId;
        }
        
        public void setFileId(Long fileId) {
            this.fileId = fileId;
        }
    }
}