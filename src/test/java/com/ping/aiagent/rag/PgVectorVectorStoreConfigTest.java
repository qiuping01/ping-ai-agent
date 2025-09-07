package com.ping.aiagent.rag;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class PgVectorVectorStoreConfigTest {

    @Resource
    private VectorStore pgVectorVectorStore;

    @Test
    void pgVectorVectorStore() {
        List<Document> documents = List.of(
                new Document("阅兵有什么用？纪念二战胜利，提醒世人！", Map.of("meta1", "meta1")),
                new Document("中国在二战中付出了巨大的代价，留住了近七成的日本军力。"),
                new Document("中国必将走向世界之巅", Map.of("meta2", "meta2")));
        // 添加文档
        pgVectorVectorStore.add(documents);
        // 相似度查询
        List<Document> results =
                pgVectorVectorStore.similaritySearch(SearchRequest.builder().query("怎么纪念二战胜利啊").topK(3).build());
        Assertions.assertNotNull(results);
    }
}