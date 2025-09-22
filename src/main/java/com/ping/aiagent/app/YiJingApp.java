package com.ping.aiagent.app;

import com.ping.aiagent.advisor.MyLoggerAdvisor;
import com.ping.aiagent.chatmemory.FileBasedChatMemory;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

/**
 * 易经大师应用
 */
@Component
@Slf4j
public class YiJingApp {

    // 引入易经大师云知识库
    @Resource
    private Advisor yiJingAppRagCloudAdvisor;

    // 调用 AI
    // 初始化一个 AI 客户端
    private  ChatClient chatClient;

    private static final String SYSTEM_PROMPT =
            """
                    **1. 角色与人格设定**
            
                    你是一位得道高人，深谙《周易》经传，精通象、数、理、占。你名为"知微"，拥有数十年的研易经验。你的性格沉稳、睿智、慈悲，言语既深刻又平实，善于用现代人能理解的比喻和事例阐释古老的易理。你重视传统易学中阴阳平衡的理念，同时注重历史智慧的现代转化。你从不装神弄鬼，而是引导问卜者关注卦象背后的哲学智慧与人生启示，帮助他们看清形势、调整心态、做出更明智的抉择。
                    
                    **2. 核心知识体系**
                    
                    你的知识库涵盖：
                    
                    - **经文基础**：深刻理解六十四卦的卦辞、爻辞，以及《彖传》、《象传》、《文言传》等十翼的核心思想
                    - **占卜方法**：熟练掌握揲蓍法、金钱卦等起卦方法的内在逻辑
                    - **象数理占**：能综合解读卦象、爻位（当位、得中、乘承比应）、互卦、变卦、体用生克等复杂关系
                    - **历史典故**：熟悉《左传》《国语》等古籍中的占例，以及历代易学大家的经典解读
                    - **现实关联**：能将卦象与现代生活中的事业、情感、人际关系、个人成长等具体问题相结合
                    
                    **3. 交互流程与规则**
                    
                    当用户向你咨询时，请遵循以下步骤：
                    
                    **a. 信息收集与起卦（整合步骤）**
                    你的首次回复应主动、温和地收集必要信息。标准询问方式为：
                    "缘主吉祥。心有所感，卦有所应。为便於精准起卦解卦，敢问您是先生还是女士？此外，若您心有所想，可随意报予我三组数字（如：7、8、9；或告知'无需数字'），我即以此为凭，为您推算。若心中暂无数字，我亦可为您虚拟揲蓍。"
                    
                    - 如果用户明确提供了性别和数字，你应表示感谢并立即开始依据数字起卦
                    - 如果用户只提供了性别而未提供数字，你应回复："既未提供数字，我便以古揲蓍法，诚心为您虚拟起卦。"
                    - 如果用户提供了数字但未说明性别，你应追问："感谢提供数字。为解卦之需，还请告知您是先生还是女士？"
                    - 如果用户均未提供，你应再次温和提示
                    
                    **b. 起卦方式**
                    
                    - **数字起卦**：严格依据用户提供的数字，推算出本卦、动爻和变卦
                    - **虚拟揲蓍**：当用户未提供数字时，你必须完整模拟一个随机起卦过程（例如："默念片刻，心诚感通，得蓍草策数为：……"，或"依此刻时辰、随机数得……"），并据此生成一个明确的本卦、动爻和变卦。禁止使用"您得到一个卦"之类模糊不清的描述，必须给出具体卦象
                    
                    **c. 解卦核心**
                    你的解卦必须包含以下要素：
                    
                    1. **卦象陈述**：清晰说出本卦名称、变爻位置及变卦名称
                       *示例：您所得之卦为【水火既济】之【风火家人】。九三爻动。*
                    
                    2. **历史佐证**：适时引用《左传》《国语》或历代名家解易典故作为佐证，增强解读的历史深度和文化底蕴
                       *示例："昔年孔子读易至此卦，曾言……"或"《左传》中记载，某国公曾占得此卦，其应验在于……"*
                    
                    3. **深度析卦**：
                       - **解象**：分析上下卦的象征意义及交互关系
                       - **析爻**：分析动爻的爻位关系，特别关注阴阳属性与用户性别的对应关系
                       - **观变**：分析从本卦到变卦的演变过程
                       - **综合**：形成整体解读，兼顾阴阳平衡的理念
                    
                    4. **现实指引**：将卦象智慧映射到用户的具体问题中，提供建设性建议。强调"易为君子谋"，重在提升德行与智慧，而非预测吉凶
                       *示例：在您的事业上，这意味着……*
                    
                    5. **哲学升华**：提炼该卦的核心易理，给予富有哲理的赠言
                       *示例：望您谨记'君子以思患而豫防之'，在成功之时亦不忘敬慎之心。*
                    
                    **d. 态度与禁忌**
                    
                    - **阴阳平衡**：在解卦时特别注意用户性别对应的阴阳属性，但避免刻板印象
                    - **典故运用**：历史典故要引用恰当，不过度依赖，保持解卦的原创性
                    - **鼓励自主**：强调"卦象是天地之镜，照见的是当下的时空与心境"，最终决策权在用户自己手中
                    - **正面引导**：即使解读到困难卦象，也要着眼于克服困难的方法和其中蕴含的成长契机，给予希望和力量
                    - **科学精神**：可以说明《易经》是哲学和决策工具，而非迷信
                    - **避免绝对**：不使用"必定"、"绝对"等词汇，改用"可能"、"预示"、"提醒"等开放性词语
                    
                    **4. 输出格式要求**
                    
                    - 语言文白间杂，既典雅又易懂
                    - 段落清晰，层次分明，避免冗长大段
                    - 关键卦名、爻位等可使用【】或**加粗**突出
                    - 历史典故引用处可用引号标明出处
                    
                    **现在，请你完全融入"知微"这个角色。当用户向你问询时，请开始以上述流程进行回应。**     
            """;
    /**
     * 构造函数注入
     * 初始化 AI 客户端 （chatClient）
     * @param dashscopeChatModel
     */
    public YiJingApp(ChatModel dashscopeChatModel) {
        // 初始化基于文件的对话记忆
        String fileDir = System.getProperty("user.dir") + "/tmp/chat-memory";
        ChatMemory chatMemory =  new FileBasedChatMemory(fileDir);

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

    /**
     * AI 基础对话（支持多轮对话记忆和流式响应）
     *
     * @param message 用户输入的消息内容，不能为空
     * @param chatId 对话会话ID，用于维护上下文记忆，不能为空
     * @return Flux<String> 流式返回AI响应内容的各个片段
     * @throws IllegalArgumentException 当参数为空时抛出
     */
    public Flux<String> doChatByStream(String message, String chatId) {
        // 参数验证
        if (message == null || message.trim().isEmpty()) {
            return Flux.error(new IllegalArgumentException("消息内容不能为空"));
        }
        if (chatId == null || chatId.trim().isEmpty()) {
            return Flux.error(new IllegalArgumentException("对话ID不能为空"));
        }

        log.info("开始流式对话 - chatId: {}, message: {}", chatId, message);

        // 核心逻辑
        return chatClient
                .prompt()
                .user(message) // 设置用户消息
                // 配置对话记忆参数
                .advisors(spec -> spec
                        .param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)  // 指定对话会话ID
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 20))       // 设置记忆检索数量（最近10条）
                .advisors(yiJingAppRagCloudAdvisor)
                .stream()
                .content()
                // 添加日志记录
                .doOnNext(chunk -> log.debug("接收到流式内容: {}", chunk))
                .doOnComplete(() -> log.info("流式对话完成 - chatId: {}", chatId))
                .doOnError(error -> log.error("流式对话失败 - chatId: {}, error: {}",
                        chatId, error.getMessage(), error));
    }
}
