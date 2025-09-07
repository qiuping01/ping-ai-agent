package com.ping.aiagent.rag;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 恋爱大师向量数据库配置（初始化基于内存的向量数据库 Bean）
 *
 * 代码逻辑：
 * - 使用 Spring AI 内置的、基于内存读写的向量数据库 SimpleVectorStore 来保存文档
 * - 调用 EmbeddingModel 将文档转换为向量
 */
@Slf4j
@Configuration
public class LoveAppVectorStoreConfig {

    @Resource
    private LoveAppDocumentLoader loveAppDocumentLoader;

    @Resource
    private MyTokenTextSplitter myTokenTextSplitter;

    @Bean
    VectorStore loveAppVectorStore(EmbeddingModel dashscopeEmbeddingModel) {
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(dashscopeEmbeddingModel).build();
        //添加文档
        List<Document> documentList = loveAppDocumentLoader.loadMarkdowns();
        //添加切词器自主切分文档
        List<Document> splitDocuments = myTokenTextSplitter.splitCustomized(documentList);
        // 添加检查，如果文档为空，抛出异常，阻止Bean创建
        if (splitDocuments.isEmpty()) {
            throw new IllegalStateException("初始化失败：未加载到任何Markdown文档，请检查'classpath:document/'路径下是否存在.md文件。");
        }
        simpleVectorStore.add(splitDocuments);
        log.info("恋爱知识文档加载成功，共加载了 {} 篇文档。", documentList.size());
        log.info("恋爱知识向量库初始化成功，共切分了 {} 篇文档。", splitDocuments.size());
        return simpleVectorStore;
    }
}
