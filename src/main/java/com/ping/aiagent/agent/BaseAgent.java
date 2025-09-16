package com.ping.aiagent.agent;

import cn.hutool.core.util.StrUtil;
import com.ping.aiagent.agent.model.AgentState;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * 抽象基础代理类 - 智能代理执行框架
 * <p>
 * 提供了一个通用的代理执行框架，支持基于步骤的任务执行模式
 * 代理可以在受控的环境中执行复杂任务，具备状态管理、内存维护和错误处理能力
 * 子类必须实现step方法
 */
@Data
@Slf4j
public abstract class BaseAgent {

    // 核心属性
    private String name;

    // 提示词
    private String systemPrompt;
    private String nextStepPrompt;

    // 代理状态
    private AgentState state = AgentState.IDLE;

    // 执行步骤控制
    private int currentStep = 0;
    private int maxSteps = 10;

    // LLM 大模型 - 预留
    private ChatClient chatClient;

    // Memory 记忆（需要自主维护会话上下文）- Spring AI Message
    private List<Message> messageList = new ArrayList<>();

    /**
     * 运行代理
     * @param userPrompt 用户提示词
     * @return 执行结果
     */
    public String run(String userPrompt) {
        // 1. 基础校验阶段
        if (this.state != AgentState.IDLE) {
            throw new RuntimeException("Can't run agent from state " + this.state);
        }
        if (StrUtil.isBlank(userPrompt)) {
            throw new RuntimeException("Can't run agent with empty user prompt");
        }
        // 2. 执行阶段
        // 状态转换：IDLE → RUNNING
        this.state = AgentState.RUNNING;
        // 内存维护：将用户消息加入上下文
        messageList.add(new UserMessage(userPrompt));
        // 结果收集：准备结果容器
        List<String> results = new ArrayList<>();
        // 3. 核心执行循环
        try {
            //执行循环
            for (int i = 0; i < maxSteps && state != AgentState.FINISHED; i++) {
                int stepNumber = i + 1;
                currentStep = stepNumber;
                log.info("Executing step {}/{}", stepNumber, maxSteps);
                //单步执行
                String stepResult = step();
                String result = "step " + stepNumber + ": " + stepResult;
                results.add(result);
            }
            // 检查是否超出步骤限制
            if (currentStep >= maxSteps) {
                state = AgentState.FINISHED;
                results.add("Terminated: Reached max steps (" + maxSteps + ")");
            } else if (state != AgentState.FINISHED) {
                // 如果循环正常结束但状态未设为FINISHED，设置为FINISHED
                state = AgentState.FINISHED;
            }
            return String.join("\n", results);
        } catch (Exception e) {
            state = AgentState.ERROR;
            log.error("Error executing agent " + this.name, e);
            return "Error executing agent " + e.getMessage();
        } finally {
            // 4.清理资源
            this.cleanup();
        }
    }

    /**
     * 定义单个步骤
     *
     * @return
     */
    public abstract String step();

    /**
     * 清理资源
     */
    protected void cleanup() {
        // 子类可以重写此方法来清理资源
    }

}
