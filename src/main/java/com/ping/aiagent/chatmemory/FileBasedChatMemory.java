package com.ping.aiagent.chatmemory;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 基于文件持久化的对话记忆
 */
public class FileBasedChatMemory implements ChatMemory {

    private static final Logger logger = LoggerFactory.getLogger(FileBasedChatMemory.class);
    private final String BASE_DIR;

    private static final Kryo kryo = new Kryo();

    static {
        kryo.setRegistrationRequired(false); //不需要手动注册
        // 设置实例化策略 -- 标准
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
        // 注册可能用到的类
        kryo.register(ArrayList.class);
        kryo.register(Message.class);
        // 根据需要注册更多类...
    }

    // 创建构造函数，让用户传入文件的保存目录
    public FileBasedChatMemory(String dir) {
        this.BASE_DIR = dir;
        File baseDir = new File(dir);
        if (!baseDir.exists()) {
            // 使用 mkdirs() 创建所有必要的父目录
            boolean created = baseDir.mkdirs();
            if (!created) {
                logger.error("无法创建目录: {}", dir);
                throw new RuntimeException("无法创建目录: " + dir);
            } else {
                logger.info("成功创建目录: {}", dir);
            }
        }
    }

    @Override
    public void add(String conversationId, Message message) {
        List<Message> existingMessages = getOrCreateConversation(conversationId);
        existingMessages.add(message);
        saveConversation(conversationId, existingMessages);
    }

    @Override
    public void add(String conversationId, List<Message> messages) {
        List<Message> existingMessages = getOrCreateConversation(conversationId);
        existingMessages.addAll(messages);
        saveConversation(conversationId, existingMessages);
    }

    @Override
    public List<Message> get(String conversationId, int lastN) {
        List<Message> allMessages = getOrCreateConversation(conversationId);
        int startIndex = Math.max(0, allMessages.size() - lastN);
        return allMessages.subList(startIndex, allMessages.size());
    }

    @Override
    public void clear(String conversationId) {
        File file = getConversationFile(conversationId);
        if (file.exists()) {
            boolean deleted = file.delete();
            if (!deleted) {
                logger.warn("无法删除文件: {}", file.getAbsolutePath());
            }
        }
    }

    /**
     * 获取或创建会话消息的列表
     */
    private List<Message> getOrCreateConversation(String conversationId) {
        File file = getConversationFile(conversationId);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (Input input = new Input(new FileInputStream(file))) {
            @SuppressWarnings("unchecked")
            List<Message> messages = (List<Message>) kryo.readObject(input, ArrayList.class);
            return messages != null ? messages : new ArrayList<>();
        } catch (IOException e) {
            logger.error("读取对话记录失败: {}", file.getAbsolutePath(), e);
            return new ArrayList<>();
        }
    }

    /**
     * 保存对话信息
     */
    private void saveConversation(String conversationId, List<Message> messages) {
        File file = getConversationFile(conversationId);
        try (Output output = new Output(new FileOutputStream(file))) {
            kryo.writeObject(output, messages);
            output.flush();
        } catch (IOException e) {
            logger.error("保存对话记录失败: {}", file.getAbsolutePath(), e);
            throw new RuntimeException("保存对话记录失败", e);
        }
    }

    /**
     * 每个会话文件单独保存
     */
    private File getConversationFile(String conversationId) {
        return new File(BASE_DIR, conversationId + ".kryo");
    }
}