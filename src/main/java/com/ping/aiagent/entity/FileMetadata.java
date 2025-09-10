package com.ping.aiagent.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "file_metadata", schema = "public")
public class FileMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "original_file_name", nullable = false, length = 255)
    private String originalFileName;

    @Column(name = "cos_path", nullable = false, length = 500)
    private String cosPath;

    @Column(name = "file_url", length = 1000)
    private String fileUrl;

    @Column(name = "upload_time")
    private LocalDateTime uploadTime;

    @Column(name = "size")
    private Long size;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "mime_type", length = 100)
    private String mimeType;

    @Column(name = "status")
    private Integer status = 1;

    // 默认构造函数
    public FileMetadata() {
    }

    // 带参数的构造函数
    public FileMetadata(String originalFileName, String cosPath, String fileUrl,
                        Long size, Long userId, String mimeType) {
        this.originalFileName = originalFileName;
        this.cosPath = cosPath;
        this.fileUrl = fileUrl;
        this.size = size;
        this.userId = userId;
        this.mimeType = mimeType;
        this.uploadTime = LocalDateTime.now();
        this.status = 1;
    }

    // Getter 和 Setter 方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getCosPath() {
        return cosPath;
    }

    public void setCosPath(String cosPath) {
        this.cosPath = cosPath;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public LocalDateTime getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(LocalDateTime uploadTime) {
        this.uploadTime = uploadTime;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "FileMetadata{" +
                "id=" + id +
                ", originalFileName='" + originalFileName + '\'' +
                ", cosPath='" + cosPath + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                ", uploadTime=" + uploadTime +
                ", size=" + size +
                ", userId=" + userId +
                ", mimeType='" + mimeType + '\'' +
                ", status=" + status +
                '}';
    }
}