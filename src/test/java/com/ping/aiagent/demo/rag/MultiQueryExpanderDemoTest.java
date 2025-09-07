package com.ping.aiagent.demo.rag;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.expansion.QueryExpander;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MultiQueryExpanderDemoTest {

    @Resource
    private MultiQueryExpanderDemo multiQueryExpanderDemo;

    @Test
    void expandQuery() {
        List<Query> queries = multiQueryExpanderDemo.expandQuery("啥是九三大阅兵啊啊啊啊啊啊？！请回答我呵呵哈哈哈");
        Assertions.assertNotNull(queries);
    }
}