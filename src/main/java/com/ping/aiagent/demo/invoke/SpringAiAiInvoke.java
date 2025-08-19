//package com.ping.aiagent.demo.invoke;
//
//import jakarta.annotation.Resource;
//import org.springframework.ai.chat.messages.AssistantMessage;
//import org.springframework.ai.chat.model.ChatModel;
//import org.springframework.ai.chat.prompt.Prompt;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
///**
// * Spring AI 模型框架调用 AI 大模型 （阿里）
// *
// * CommandLineRunner 实现单次执行 run方法
// * 当项目启动的时候，spring 会扫描 component 注解的 bean
// * 发现有 CommandLineRunner 实现接口，就会执行 run 方法
// * 就能在项目启动时自动注入依赖并且调用 Ai 大模型
// */
//@Component
//public class SpringAiAiInvoke implements CommandLineRunner {
//
//    @Resource
//    private ChatModel dashscopeChatModel; // 先通过名称 找不到再通过类型 一定要是这个名称
//
//    @Override
//    public void run(String... args) throws Exception {
//        AssistantMessage assistantMessage = dashscopeChatModel.call(new Prompt("你好，我是秋棠"))
//                .getResult()
//                .getOutput(); // 取出 getResult 对象的内容
//        System.out.println(assistantMessage.getText()); // 拿到文本响应
//    }
//}
