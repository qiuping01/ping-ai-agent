package com.ping.aiagent.reader;

import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.boot.test.context.SpringBootTest;
import javax.annotation.Resource;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SimpleDocumentReaderTest {

    @Resource
    private SimpleDocumentReader documentReader;

    @Test
    void testReadAllDocuments() {
        List<Document> allDocuments = documentReader.readAll();
        for (Document document : allDocuments) {
            System.out.println("#####################################");
            System.out.println(document);
        }
        assertNotNull(allDocuments);
        System.out.println("#####################################");
        System.out.println("Found " + allDocuments.size() + " documents");
    }

    @Test
    void testReadBatch() {
        List<Document> batchDocuments = documentReader.readBatch(
                List.of("doc-001", "doc-002", "doc-003")
        );
        assertNotNull(batchDocuments);
        System.out.println("Found #####################################");
        System.out.println("Batch documents: " + batchDocuments.size());
    }

    @Test
    void testReadAllDocumentsPrint() {
        List<Document> allDocuments = documentReader.readAll();
        assertNotNull(allDocuments);
        System.out.println("Number of documents: " + allDocuments.size());

        // 打印第一个文档的类名和所有方法/字段（反射方式，仅供参考思路）
        if (!allDocuments.isEmpty()) {
            Document doc = allDocuments.get(0);
            System.out.println("Document class: " + doc.getClass().getName());
            // 尝试打印对象本身，看其toString()方法输出什么信息
            System.out.println("Found #####################################");
            System.out.println("Document toString: " + doc.toString());
            // 如果有getMetadata()方法，可以查看元数据
            // System.out.println("Metadata: " + doc.getMetadata());
        }
    }
}