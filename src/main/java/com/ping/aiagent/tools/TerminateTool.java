package com.ping.aiagent.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;

/**
 * 终止工具
 * <p>
 * 用于让智能代理在完成任务或无法继续执行时优雅地结束工作流程
 */
@Slf4j
public class TerminateTool {

    @Tool(description = """
            Terminate the interaction when the request is met OR if the assistant cannot proceed further with the task.
            When you have finished all the tasks, call this tool to end the work.
            """)
    public String doTerminate() {
        log.info("智能代理调用终止工具，任务执行完成");
        return "Task completed,灵蛇智能已完成任务";
    }
}
