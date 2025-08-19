package com.ping.aiagent.demo.invoke;

import com.ping.aiagent.contant.TestAPI;
import dev.langchain4j.community.model.dashscope.QwenChatModel;

public class LangChainAiInvoke {

    public static void main(String[] args) {
        QwenChatModel qwenChatModel = QwenChatModel.builder()
                .apiKey(TestAPI.API_KEY)
                .modelName("qwen-max")
                .build();
        String answer = qwenChatModel.chat(
                "我是程序员秋棠，这是Java知识星球 user.qiutang.icu 的超级智能体项目");
        System.out.println(answer);

    }
}
