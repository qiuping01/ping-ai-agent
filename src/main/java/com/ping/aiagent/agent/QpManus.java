package com.ping.aiagent.agent;

import com.ping.aiagent.advisor.MyLoggerAdvisor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.stereotype.Component;

/**
 * QpManus - AI 超级智能体
 * <p>
 * 拥有自主规划能力的智能体，可以直接使用
 * 能够智能选择和组合工具来完成复杂任务
 */
@Slf4j
@Component
public class QpManus extends ToolCallAgent {

    /**
     * 构造 QpManus 智能体
     *
     * @param allTools 可用工具集合
     * @param dashscopeChatModel 阿里聊天模型
     */
    public QpManus(ToolCallback[] allTools, ChatModel dashscopeChatModel) {
        super(allTools);

        // 设置智能体名称
        this.setName("QpManus");

        // 系统提示词 - 定义智能体角色和能力
        String SYSTEM_PROMPT = """  
                You are QpManus, an all-capable AI assistant, aimed at solving any task presented by the user.
                You have various tools at your disposal that you can call upon to efficiently complete complex requests.
                """;
        this.setSystemPrompt(SYSTEM_PROMPT);

        // 下一步提示词 - 指导智能体决策过程
        String NEXT_STEP_PROMPT = """  
                Based on user needs, proactively select the most appropriate tool or combination of tools.
                For complex tasks, you can break down the problem and use different tools step by step to solve it.
                After using each tool, clearly explain the execution results and suggest the next steps.
                If you want to stop the interaction at any point, use the `terminate` tool/function call.
                """;
        this.setNextStepPrompt(NEXT_STEP_PROMPT);

        // 设置最大执行步骤数
        this.setMaxSteps(20);

        // 初始化 AI 对话客户端
        ChatClient chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultAdvisors(new MyLoggerAdvisor())
                .build();

        // 将构建好的chatClient设置给当前智能体
        this.setChatClient(chatClient);
    }
}
