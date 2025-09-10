package com.ping.aiagent.reader;

import org.springframework.ai.document.Document;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SimpleDocumentReader {
    
    private final JdbcTemplate jdbcTemplate;
    
    public SimpleDocumentReader(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    /**
     * 从数据库读取所有文档
     */
    public List<Document> readAll() {
        String sql = "SELECT * FROM documents";
        
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            // 构建元数据
            Map<String, Object> metadata = new HashMap<>();
            
            // 添加所有字段到元数据
            addIfNotEmpty(metadata, "id", rs.getString("id"));
            addIfNotEmpty(metadata, "doc_id", rs.getString("doc_id"));
            addIfNotEmpty(metadata, "title", rs.getString("title"));
            addIfNotEmpty(metadata, "author", rs.getString("author"));
            addIfNotEmpty(metadata, "category", rs.getString("category"));
            addIfNotEmpty(metadata, "created_at", rs.getString("created_at"));
            addIfNotEmpty(metadata, "status", rs.getString("status"));
            
            // 获取内容
            String content = rs.getString("content");
            if (content == null) {
                content = "";
            }
            
            // 创建 Spring AI Alibaba 的 Document 对象
            return new Document(content, metadata);
        });
    }
    
    /**
     * 根据ID读取单个文档
     */
    public Document readById(String docId) {
        String sql = "SELECT * FROM documents WHERE doc_id = ?";
        
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                Map<String, Object> metadata = new HashMap<>();
                
                addIfNotEmpty(metadata, "id", rs.getString("id"));
                addIfNotEmpty(metadata, "doc_id", rs.getString("doc_id"));
                addIfNotEmpty(metadata, "title", rs.getString("title"));
                addIfNotEmpty(metadata, "author", rs.getString("author"));
                addIfNotEmpty(metadata, "category", rs.getString("category"));
                addIfNotEmpty(metadata, "created_at", rs.getString("created_at"));
                addIfNotEmpty(metadata, "status", rs.getString("status"));
                
                String content = rs.getString("content");
                if (content == null) {
                    content = "";
                }
                
                return new Document(content, metadata);
            }, docId);
        } catch (Exception e) {
            throw new RuntimeException("Document with id " + docId + " not found", e);
        }
    }
    
    /**
     * 批量读取文档
     */
    public List<Document> readBatch(List<String> docIds) {
        if (docIds == null || docIds.isEmpty()) {
            return List.of();
        }
        
        String inClause = String.join(",", docIds.stream().map(id -> "?").toArray(String[]::new));
        String sql = String.format("SELECT * FROM documents WHERE doc_id IN (%s)", inClause);
        
        return jdbcTemplate.query(sql, docIds.toArray(), (rs, rowNum) -> {
            Map<String, Object> metadata = new HashMap<>();
            
            addIfNotEmpty(metadata, "id", rs.getString("id"));
            addIfNotEmpty(metadata, "doc_id", rs.getString("doc_id"));
            addIfNotEmpty(metadata, "title", rs.getString("title"));
            addIfNotEmpty(metadata, "author", rs.getString("author"));
            addIfNotEmpty(metadata, "category", rs.getString("category"));
            addIfNotEmpty(metadata, "created_at", rs.getString("created_at"));
            addIfNotEmpty(metadata, "status", rs.getString("status"));
            
            String content = rs.getString("content");
            if (content == null) {
                content = "";
            }
            
            return new Document(content, metadata);
        });
    }
    
    /**
     * 辅助方法：如果值不为空则添加到元数据
     */
    private void addIfNotEmpty(Map<String, Object> metadata, String key, String value) {
        if (StringUtils.hasText(value)) {
            metadata.put(key, value);
        }
    }
}