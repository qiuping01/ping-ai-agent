package com.ping.aiagent.app;

import com.ping.aiagent.advisor.MyLoggerAdvisor;
import com.ping.aiagent.chatmemory.FileBasedChatMemory;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

/**
 * 恋爱大师应用
 */
@Component
@Slf4j
public class LoveApp {

    // 用于从恋爱知识库中检索相关内容的向量存储客户端
    @Resource
    private VectorStore loveAppVectorStore;

    @Resource
    private VectorStore pgVectorVectorStore;

    // 用于从云恋爱知识库中检索
    @Resource
    private Advisor loveAppRagCloudAdvisor;


    // 调用 AI
    // 初始化一个 AI 客户端
    private final ChatClient chatClient;

    public static final String SYSTEM_PROMPT =
            "扮演深耕恋爱心理领域的专家。开场向用户表明身份，告知用户可倾诉恋爱难题。" +
            "围绕单身、恋爱、已婚三种状态提问：单身状态询问社交圈拓展及追求心仪对象的困扰；" +
            "恋爱状态询问沟通、习惯差异引发的矛盾；已婚状态询问家庭责任与亲属关系处理的问题。" +
            "引导用户详述事情经过、对方反应及自身想法，以便给出专属解决方案。";

    /**
     * 构造函数注入
     * 初始化 AI 客户端 （chatClient）
     * @param dashscopeChatModel
     */
    public LoveApp(ChatModel dashscopeChatModel) {
        // 初始化基于文件的对话记忆
        String fileDir = System.getProperty("user.dir") + "/tmp/chat-memory";
        ChatMemory chatMemory =  new FileBasedChatMemory(fileDir);

//        // 初始化基于内存的对话记忆
//        ChatMemory chatMemory = new InMemoryChatMemory();

        chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory),
                        // 自定义日志 Advisor，可按需开启
                        new MyLoggerAdvisor()
                        // 自定义推理增强 Advisor，可按需开启
//                        ,new ReReadingAdvisor()
                ) // 默认拦截器
                .build();
    }

    /**
     * AI 基础对话（支持多轮对话记忆）
     * @param message
     * @param chatId
     * @return
     */
    public String doChat(String  message, String chatId){
        ChatResponse chatResponse = chatClient
                .prompt()
                .user(message) //用户预设
                // 指定当前关联的上下文是哪个对话的，制定两个参数
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        log.info("content:{}", content);
        return content;
    }

    record LoveReport(String title, List<String> suggestions) {
    }

    /**
     * AI 恋爱报告功能（实战结构化输出）
     * @param message
     * @param chatId
     * @return
     */
    public LoveReport doChatWithReport(String message, String chatId) {
        LoveReport loveReport = chatClient
                .prompt()
                .system(SYSTEM_PROMPT + "每次对话后都要生成恋爱结果，标题为{用户名}的恋爱报告，内容为建议列表")
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .call()
                .entity(LoveReport.class);
        log.info("loveReport: {}", loveReport);
        return loveReport;
    }

    /**
     * 基于 RAG（检索增强生成）的恋爱知识对话服务方法。
     * 该方法接收用户消息，结合对话历史记忆，并从专属向量知识库中检索相关信息，
     * 最终生成一个上下文丰富、准确且个性化的回答。
     *
     * 核心流程：用户输入 -> 记忆检索 -> 知识库检索 -> 大模型生成 -> 返回输出
     *
     * @param message 用户输入的对话消息内容
     * @param chatId  本次对话会话的唯一标识符，用于获取和更新特定对话的历史上下文记忆
     * @return        由AI生成的、基于恋爱知识库和对话历史的回答内容
     */
    public String doChatWithRag(String message, String chatId) {

        // 构建并执行复杂的对话链（Chain），使用Fluent API进行链式调用
        ChatResponse chatResponse = chatClient
                .prompt() // 发起一个提示词（Prompt）构建流程
                .user(message) // 设置用户输入为本次对话的主要查询内容

                //  Advisor 1: 配置对话记忆检索顾问
                //  - 目的：为LLM提供对话上下文，实现多轮连贯对话
                //  - 参数1: 设置从记忆库中查找与此chatId相关的历史对话
                //  - 参数2: 设置最多检索10条历史消息作为上下文
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))

                //  Advisor 2: 注入日志记录顾问
                .advisors(new MyLoggerAdvisor())

                //  Advisor 3-1: 注入核心的 RAG 检索顾问（最重要的环节,基于本地文件）
                //  - 目的：将用户问题发送到向量知识库进行语义搜索，获取相关知识片段（应用 RAG 知识库问答）
                //  - 参数: loveAppVectorStore 恋爱应用专用的向量存储客户端，包含嵌入后的知识数据
                //  - 机制：该顾问会将检索到的相关知识作为上下文注入到 Prompt 中，增强 LLM 的回答能力
//                .advisors(new QuestionAnswerAdvisor(loveAppVectorStore))

                //  Advisor 3-2: 应用 RAG 检索增强服务 （基于云知识库服务）
                //.advisors(loveAppRagCloudAdvisor)

                // Advisor 3-3: 应用 RAG 检索增强服务 （基于PGVector）
                .advisors(new QuestionAnswerAdvisor(pgVectorVectorStore))

                // 执行调用：将组装好的提示链发送给AI模型并获取响应
                .call()
                .chatResponse();

        // 从复杂的响应对象中提取最终的文本输出内容
        String content = chatResponse.getResult().getOutput().getText();

        // 记录日志：将AI生成的最终回答内容输出到日志系统
        log.info("AI生成的回答内容: {}", content);

        // 返回回答
        return content;
    }
}


