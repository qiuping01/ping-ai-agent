package com.ping.aiagent.agent;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

/**
 * ReAct (Reasoning and Acting) 模式的代理抽象类
 * 实现了思考-行动的循环模式
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public abstract class ReActAgent extends BaseAgent {

    /**
     * 处理当前状态并决定下一步行动
     *
     * @return 是否需要执行行动，true表示需要执行，false表示不需要执行
     */
    public abstract boolean think();

    /**
     * 执行决定的行动
     *
     * @return 行动执行结果
     */
    public abstract String act();

    /**
     * 执行单个步骤：思考和行动
     *
     * @return 步骤执行结果
     */
    @Override
    public String step() {
        try {
            // 先思考
            boolean shouldAct = think();
            if (!shouldAct) {
                log.debug("Agent {} 思考完成，无需行动", getName());
                return "思考完成 - 无需行动";
            }
            // 再行动
            log.debug("Agent {} 决定执行行动", getName());
            return act();
        } catch (Exception e) {
            // 记录异常日志
            log.error("ReAct步骤执行异常", e);
            return "步骤执行失败：" + e.getMessage();
        }
    }
}
