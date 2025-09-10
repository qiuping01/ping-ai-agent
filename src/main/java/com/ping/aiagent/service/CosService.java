package com.ping.aiagent.service;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

@Service
public class CosService {

    @Autowired
    private COSClient cosClient;

    @Value("${tencent.cos.bucket-name}")
    private String bucketName;

    @Value("${tencent.cos.base-url:}")
    private String baseUrl;

    /**
     * 上传字符串内容到COS
     *
     * @param content  字符串内容
     * @param cosPath  COS上的路径（包括文件名）
     * @return 文件的访问URL（如果是私有存储桶，则是预签名URL）
     */
    public String uploadString(String content, String cosPath) {
        try {
            // 将字符串转换为输入流
            byte[] contentBytes = content.getBytes("UTF-8");
            InputStream inputStream = new ByteArrayInputStream(contentBytes);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(contentBytes.length);

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, cosPath, inputStream, metadata);
            cosClient.putObject(putObjectRequest);

            // 如果你的存储桶是私有读，则需要生成预签名URL；如果是公有读，则可以直接拼接URL。
            if (baseUrl != null && !baseUrl.isEmpty()) {
                return baseUrl + "/" + cosPath; // 公有读直接返回URL
            } else {
                // 生成预签名URL（有效期为1小时）
                GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(bucketName, cosPath);
                Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000L);
                req.setExpiration(expiration);
                URL url = cosClient.generatePresignedUrl(req);
                return url.toString();
            }
        } catch (IOException e) {
            throw new RuntimeException("上传文件到COS失败", e);
        }
    }

    /**
     * 从COS下载文件并返回字符串内容
     *
     * @param cosPath COS上的路径
     * @return 文件内容字符串
     */
    public String downloadString(String cosPath) {
        try {
            COSObject cosObject = cosClient.getObject(bucketName, cosPath);
            try (COSObjectInputStream cosObjectInputStream = cosObject.getObjectContent()) {
                byte[] bytes = cosObjectInputStream.readAllBytes();
                return new String(bytes, "UTF-8");
            }
        } catch (IOException e) {
            throw new RuntimeException("从COS下载文件失败", e);
        }
    }

    /**
     * 删除COS上的文件
     *
     * @param cosPath COS上的路径
     */
    public void deleteFile(String cosPath) {
        try {
            cosClient.deleteObject(bucketName, cosPath);
        } catch (CosClientException e) {
            throw new RuntimeException("从COS删除文件失败", e);
        }
    }

    /**
     * 生成文件的预签名URL（用于私有存储桶的临时访问）
     *
     * @param cosPath      COS上的路径
     * @param expirationMs 过期时间（毫秒）
     * @return 预签名URL
     */
    public String generatePresignedUrl(String cosPath, Long expirationMs) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, cosPath);
        Date expiration = new Date(System.currentTimeMillis() + expirationMs);
        generatePresignedUrlRequest.setExpiration(expiration);
        URL url = cosClient.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }
}