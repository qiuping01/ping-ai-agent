<template>
  <div class="chat-container" :class="aiType">
    <!-- 聊天记录区域 -->
    <div class="chat-messages" ref="messagesContainer">
      <div v-for="(msg, index) in messages" :key="index" class="message-wrapper" :class="msg.isUser ? 'user-wrapper' : 'ai-wrapper'">
        <!-- AI消息 -->
        <div v-if="!msg.isUser" 
             class="message ai-message" 
             :class="[msg.type]">
          <div class="avatar ai-avatar">
            <AiAvatarFallback :type="aiType" />
          </div>
          <div class="message-bubble">
            <div class="message-content-wrapper">
              <div class="message-content" v-html="parseMarkdown(msg.content)"></div>

              <!-- 示例消息复制按钮 -->
              <button 
                v-if="isExampleMessage(msg.content)" 
                class="copy-button" 
                @click="handleCopyExample(getDefaultExampleText(), $event.currentTarget)"
                title="复制示例消息">
                <svg width="16" height="16" viewBox="0 0 16 16" fill="currentColor">
                  <path d="M4 1.5H9a.5.5 0 0 1 .5.5v1a.5.5 0 0 1-.5.5H4a.5.5 0 0 1-.5-.5v-1a.5.5 0 0 1 .5-.5zm0 2H9a2.5 2.5 0 0 1 2.5 2.5v7a2.5 2.5 0 0 1-2.5 2.5H4a2.5 2.5 0 0 1-2.5-2.5v-7A2.5 2.5 0 0 1 4 3.5zm-2-1A3.5 3.5 0 0 0 .5 6v7A3.5 3.5 0 0 0 4 16.5h5a3.5 3.5 0 0 0 3.5-3.5v-1a.5.5 0 0 0-1 0v1a2.5 2.5 0 0 1-2.5 2.5H4a2.5 2.5 0 0 1-2.5-2.5v-7A2.5 2.5 0 0 1 4 4h5a2.5 2.5 0 0 1 2.5 2.5v1a.5.5 0 0 0 1 0v-1A3.5 3.5 0 0 0 9 3.5H4a3.5 3.5 0 0 0-3.5 3.5v7a3.5 3.5 0 0 0 3.5 3.5h5a3.5 3.5 0 0 0 3.5-3.5v-7A3.5 3.5 0 0 0 9 1.5H4z"/>
                </svg> 复制示例
              </button>

              <span v-if="connectionStatus === 'connecting' && index === messages.length - 1" class="typing-indicator">▋</span>
            </div>
            <div class="message-time">{{ formatTime(msg.time) }}</div>
          </div>
        </div>
        
        <!-- 用户消息 -->
        <div v-else class="message user-message" :class="[msg.type]">
          <div class="message-bubble">
            <div class="message-content-wrapper">
              <div class="message-content" v-html="parseMarkdown(msg.content)"></div>
            </div>
            <div class="message-time">{{ formatTime(msg.time) }}</div>
          </div>
          <div class="avatar user-avatar">
            <UserAvatar />
          </div>
        </div>
      </div>
    </div>

    <!-- 输入区域 -->
    <div class="chat-input-container">
      <div class="input-wrapper">
        <input 
          v-model="inputMessage"
          type="text"
          placeholder="请输入您的问题..."
          class="chat-input"
          @keydown.enter.prevent="sendMessage"
        />
        <button class="send-button" @click="sendMessage">
          <svg width="18" height="18" viewBox="0 0 16 16" fill="currentColor">
            <path d="M15.854.146a.5.5 0 0 1 .112.54L16 1.5v12a.5.5 0 0 1-.646.487L8.5 14.519l-7.354 1.47A.5.5 0 0 1 0 15.5v-12a.5.5 0 0 1 .146-.354L.5 1H14a.5.5 0 0 1 .354.146zM1.5 2.488L8 3.985l6.5-1.506L1.5 2.488zM8 4.985V14.5L1.354 12.968 8 4.985zM14 1H2v11.5L14 5.015V1z"/>
          </svg>
        </button>
      </div>
      <div class="connection-status">
        <span class="status-indicator" :class="connectionStatus"></span>
        <span class="status-text">{{ getStatusText(connectionStatus) }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, nextTick, computed, onMounted } from 'vue'
import AiAvatarFallback from './AiAvatarFallback.vue'
import UserAvatar from './UserAvatar.vue'
import { marked } from 'marked'

// Props
const props = defineProps({
  messages: {
    type: Array,
    default: () => []
  },
  aiType: {
    type: String,
    default: 'default'
  },
  connectionStatus: {
    type: String,
    default: 'ready'
  }
})

// Emits
const emit = defineEmits(['send-message'])

// Refs
const inputMessage = ref('')
const messagesContainer = ref(null)

// 获取AI头像
const getAvatarImage = computed(() => {
  switch (aiType) {
    case 'love':
      return '/ai-love-avatar.png' // 恋爱大师头像
    case 'super':
      return '/ai-super-avatar.png' // 超级智能体头像
    case 'yijing':
      return '/ai-yijing-avatar.png' // 易经大师头像
    default:
      return '/ai-default-avatar.png' // 默认头像
  }
})

// 发送消息
const sendMessage = () => {
  if (!inputMessage.value.trim()) return
  
  emit('send-message', inputMessage.value)
  inputMessage.value = ''
}

// 检查是否是示例消息
const isExampleMessage = (content) => {
  return content.includes('💡 使用示例') && content.includes('example-item')
}

// 获取默认示例文本
const getDefaultExampleText = () => {
  return '帮我写一个200字的计算机发展报告，并以PPT的形式发送给邮箱：your@email.com';
}

// 格式化时间
const formatTime = (timestamp) => {
  const date = new Date(timestamp)
  return date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
}

// 解析Markdown内容
const parseMarkdown = (content) => {
  if (!content) return '';
  
  try {
    // 配置marked选项
    marked.setOptions({
      breaks: true,  // 将回车转换为<br>
      gfm: true,     // 启用GitHub风格的Markdown
      sanitize: false // 允许HTML内容（需要后端确保安全性）
    })
    
    // 直接返回HTML内容，因为欢迎消息已经是HTML格式
    if (content.includes('<div class="welcome-container">')) {
      return content;
    }
    
    // 对于其他内容，使用marked解析
    const result = marked.parse(content);
    return result || content;
  } catch (error) {
    console.error('解析Markdown失败:', error);
    return content;
  }
};

// 处理复制到剪贴板功能
const handleCopyExample = async (text, buttonElement) => {
  if (!text) {
    text = getDefaultExampleText();
  }
  
  try {
    await navigator.clipboard.writeText(text);
    
    // 显示复制成功的提示
    if (buttonElement) {
      const originalContent = buttonElement.innerHTML;
      buttonElement.innerHTML = '<svg width="16" height="16" viewBox="0 0 16 16" fill="currentColor"><path d="M13.854 3.646a.5.5 0 0 1 0 .708l-7 7a.5.5 0 0 1-.708 0l-3.5-3.5a.5.5 0 1 1 .708-.708L6.5 10.293l6.646-6.647a.5.5 0 0 1 .708 0z"/></svg> 已复制';
      buttonElement.style.backgroundColor = '#28a745';
      buttonElement.style.borderColor = '#28a745';
      buttonElement.style.color = 'white';
      
      setTimeout(() => {
        buttonElement.innerHTML = originalContent;
        buttonElement.style.backgroundColor = '';
        buttonElement.style.borderColor = '';
        buttonElement.style.color = '';
      }, 2000);
    }
    
    return true;
  } catch (err) {
    console.error('复制失败:', err);
    return false;
  }
};

// 处理示例消息渲染后的逻辑
const handleExampleMessageRender = () => {
  nextTick(() => {
    const exampleItems = document.querySelectorAll('.example-item');
    exampleItems.forEach(item => {
      // 检查是否已经添加了复制按钮
      if (!item.querySelector('.inline-copy-btn')) {
        const text = item.textContent;
        const copyBtn = document.createElement('button');
        copyBtn.className = 'inline-copy-btn';
        copyBtn.title = '复制此示例';
        copyBtn.innerHTML = '<svg width="14" height="14" viewBox="0 0 16 16" fill="currentColor"><path d="M4 1.5H9a.5.5 0 0 1 .5.5v1a.5.5 0 0 1-.5.5H4a.5.5 0 0 1-.5-.5v-1a.5.5 0 0 1 .5-.5zm0 2H9a2.5 2.5 0 0 1 2.5 2.5v7a2.5 2.5 0 0 1-2.5 2.5H4a2.5 2.5 0 0 1-2.5-2.5v-7A2.5 2.5 0 0 1 4 3.5z"/></svg>';
        copyBtn.onclick = (e) => {
          e.stopPropagation();
          handleCopyExample(text, copyBtn);
        };
        item.appendChild(copyBtn);
      }
    });
  });
};

// 监听消息变化，处理示例消息渲染
watch(() => props.messages, (newMessages) => {
  const hasExampleMessage = newMessages.some(msg => !msg.isUser && isExampleMessage(msg.content));
  if (hasExampleMessage) {
    handleExampleMessageRender();
  }
}, { deep: true });

// 自动滚动到底部
const scrollToBottom = async () => {
  await nextTick()
  if (messagesContainer.value) {
    messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
  }
};

// 监听消息变化与内容变化，自动滚动
watch(() => props.messages.length, () => {
  scrollToBottom();
});

watch(() => props.messages.map(m => m.content).join(''), () => {
  scrollToBottom();
});

onMounted(() => {
  scrollToBottom();
  
  // 添加事件委托处理复制按钮点击
  if (messagesContainer.value) {
    messagesContainer.value.addEventListener('click', (event) => {
      const copyButton = event.target.closest('.copy-button');
      if (copyButton) {
        // 阻止事件冒泡
        event.stopPropagation();
        
        // 获取要复制的文本
        const text = copyButton.getAttribute('data-text');
        
        // 处理复制
        handleCopyExample(text, copyButton);
      }
    });
  }
});

// 获取连接状态文本
const getStatusText = (status) => {
  switch (status) {
    case 'connecting':
      return '连接中'
    case 'connected':
      return '已连接'
    case 'error':
      return '连接错误'
    default:
      return '就绪'
  }
};
</script>

<style scoped>
.chat-container {
  display: flex;
  flex-direction: column;
  height: 70vh;
  min-height: 600px;
  background-color: #f5f5f5;
  border-radius: 8px;
  overflow: hidden;
  position: relative;
}

/* 易经大师特定样式 - 最强优先级确保白色背景 */
.chat-container.yijing .message-wrapper.ai-wrapper .message.ai-message .message-bubble,
.chat-container.yijing .message-wrapper.ai-wrapper .message.ai-answer .message-bubble,
.chat-container.yijing .message-wrapper.ai-wrapper .message.ai-final .message-bubble {
  background-color: #ffffff !important;
  background-image: none !important;
  border: 1px solid #e0e0e0 !important;
  border-radius: 18px !important;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1) !important;
  position: relative;
  z-index: 1;
}

/* 防止任何可能的背景色干扰 */
.chat-container.yijing * {
  background-color: transparent !important;
}

/* 仅为易经大师消息泡泡设置白色背景 */
.chat-container.yijing .message.ai-message .message-bubble,
.chat-container.yijing .message.ai-answer .message-bubble,
.chat-container.yijing .message.ai-final .message-bubble {
  background-color: #ffffff !important;
  background-image: none !important;
  border: 1px solid #e0e0e0 !important;
  z-index: 2;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
  scrollbar-width: thin;
  scrollbar-color: #ccc #f0f0f0;
}

.chat-messages::-webkit-scrollbar {
  width: 6px;
}

.chat-messages::-webkit-scrollbar-track {
  background: #f0f0f0;
}

.chat-messages::-webkit-scrollbar-thumb {
  background: #ccc;
  border-radius: 3px;
}

.message-wrapper {
  display: flex;
  margin-bottom: 12px;
  animation: fadeIn 0.3s ease-in;
  width: 100%;
}

.message-wrapper.ai-wrapper {
  justify-content: flex-start;
}

.message-wrapper.user-wrapper {
  justify-content: flex-end;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.message {
  display: flex;
  max-width: 80%;
  word-wrap: break-word;
}

.ai-message {
  align-self: flex-start;
}

.user-message {
  align-self: flex-end;
  flex-direction: row-reverse;
}

.avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  overflow: hidden;
  margin: 0 8px;
  flex-shrink: 0;
}

.ai-avatar {
  background-color: #e3f2fd;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
}

.user-avatar {
  background-color: #f3e5f5;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
}

.message-bubble {
  position: relative;
  padding: 10px 14px 4px 14px;
  border-radius: 16px;
  word-break: break-word;
}

.ai-message .message-bubble {
  background-color: #ffffff;
  border-bottom-left-radius: 6px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.user-message .message-bubble {
  background-color: #4caf50;
  color: black;
  border-bottom-right-radius: 6px;
}

/* 易经大师特定用户消息样式 - 添加文本框效果 */
.chat-container.yijing .user-message .message-bubble {
  background-color: #4caf50;
  color: black;
  border: 1px solid rgba(0, 0, 0, 0.1);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 12px 16px 6px 16px;
  position: relative;
  z-index: 1;
}

/* 易经大师特定用户消息时间戳样式 - 确保可见 */
.chat-container.yijing .user-message .message-time {
  color: rgba(0, 0, 0, 0.7);
  opacity: 0.8;
}

.message-content-wrapper {
  position: relative;
  margin-bottom: 2px;
}

.message-content {
  line-height: 1.5;
  white-space: pre-wrap;
  text-align: left;
}

.message-content :deep(p) {
  margin: 0 0 12px 0;
}

.message-content :deep(p:last-child) {
  margin-bottom: 0;
}

.message-time {
  font-size: 10px;
  color: #999;
  text-align: right;
  margin-top: 1px;
  line-height: 0.9;
  opacity: 0.7;
}

.user-message .message-time {
  color: rgba(255, 255, 255, 0.7);
}

.typing-indicator {
  display: inline-block;
  width: 8px;
  height: 8px;
  background-color: #9e9e9e;
  border-radius: 50%;
  animation: typing 1.4s infinite ease-in-out;
}

@keyframes typing {
  0% {
    transform: scale(1);
    opacity: 1;
  }
  50% {
    transform: scale(1.2);
    opacity: 0.7;
  }
  100% {
    transform: scale(1);
    opacity: 1;
  }
}

.chat-input-container {
  padding: 16px;
  background-color: #ffffff;
  border-top: 1px solid #e0e0e0;
}

.input-wrapper {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
}

.chat-input {
  flex: 1;
  padding: 10px 16px;
  border: 1px solid #ddd;
  border-radius: 20px;
  outline: none;
  font-size: 14px;
  transition: border-color 0.3s;
}

.chat-input:focus {
  border-color: #4caf50;
}

.send-button {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-color: #4caf50;
  color: white;
  border: none;
  margin-left: 8px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background-color 0.3s;
}

.send-button:hover {
  background-color: #45a049;
}

.connection-status {
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  color: #666;
}

.status-indicator {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  margin-right: 6px;
  transition: background-color 0.3s;
}

.status-indicator.ready {
  background-color: #9e9e9e;
}

.status-indicator.connecting {
  background-color: #ff9800;
  animation: pulse 1.5s infinite;
}

.status-indicator.connected {
  background-color: #4caf50;
}

.status-indicator.error {
  background-color: #f44336;
}

@keyframes pulse {
  0% {
    opacity: 1;
  }
  50% {
    opacity: 0.5;
  }
  100% {
    opacity: 1;
  }
}

.copy-button {
  display: flex;
  align-items: center;
  padding: 4px 8px;
  font-size: 12px;
  background-color: #f5f5f5;
  color: #666;
  border: 1px solid #ddd;
  border-radius: 4px;
  cursor: pointer;
  margin-top: 8px;
  transition: all 0.3s;
}

.copy-button:hover {
  background-color: #e0e0e0;
  border-color: #bbb;
}

/* 示例项目内联复制按钮样式 */
.inline-copy-btn {
  position: absolute;
  right: 8px;
  top: 50%;
  transform: translateY(-50%);
  background: rgba(255, 255, 255, 0.9);
  border: 1px solid #ddd;
  border-radius: 10px;
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  opacity: 0;
  transition: all 0.3s ease;
  font-size: 12px;
  color: #666;
}

.example-item {
  position: relative;
  padding: 8px 32px 8px 16px;
  margin-bottom: 8px;
  background-color: #f0f7ff;
  border: 1px solid #e3f2fd;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  word-break: break-word;
}

.example-item:hover {
  background-color: #e3f2fd;
  transform: translateX(4px);
}

.example-item:hover .inline-copy-btn {
  opacity: 1;
}

.inline-copy-btn:hover {
  background: #4caf50;
  color: white;
  border-color: #4caf50;
}

/* 思考动画样式 */
.ai-thinking .message-bubble {
  background: linear-gradient(90deg, #f0f7ff 0%, #e3f2fd 50%, #f0f7ff 100%);
  background-size: 200% 100%;
  animation: thinkingGradient 2s ease-in-out infinite;
  border: 1px solid #e3f2fd;
  position: relative;
}

.ai-thinking .message-content::after {
  content: '';
  display: inline-block;
  width: 4px;
  height: 4px;
  border-radius: 50%;
  background-color: #4a90e2;
  margin-left: 8px;
  animation: thinkingDots 1.5s ease-in-out infinite;
}

.ai-thinking .message-content::before {
  content: '';
  position: absolute;
  left: -2px;
  top: -2px;
  right: -2px;
  bottom: -2px;
  background: linear-gradient(45deg, #4a90e2, #87ceeb, #4a90e2);
  background-size: 300% 300%;
  border-radius: 18px;
  z-index: -1;
  animation: thinkingBorder 3s ease-in-out infinite;
  opacity: 0.3;
}

@keyframes thinkingGradient {
  0%, 100% {
    background-position: 0% 50%;
  }
  50% {
    background-position: 100% 50%;
  }
}

@keyframes thinkingDots {
  0%, 20% {
    transform: scale(1);
    opacity: 1;
  }
  50% {
    transform: scale(1.5);
    opacity: 0.7;
  }
  80%, 100% {
    transform: scale(1);
    opacity: 1;
  }
}

@keyframes thinkingBorder {
  0%, 100% {
    background-position: 0% 50%;
  }
  50% {
    background-position: 100% 50%;
  }
}

/* 任务完成样式 */
.ai-completed .message-bubble {
  background: linear-gradient(135deg, #e8f5e8 0%, #f0fff0 100%);
  border: 1px solid #c8e6c8;
  box-shadow: 0 2px 8px rgba(76, 175, 80, 0.2);
}

.ai-completed .message-content {
  color: #2e7d32;
  font-weight: 500;
  display: flex;
  align-items: center;
}

.ai-completed .message-content::before {
  content: '';
  display: inline-block;
  width: 16px;
  height: 16px;
  background: #4caf50;
  border-radius: 50%;
  margin-right: 8px;
  animation: completedPulse 2s ease-in-out;
}

@keyframes completedPulse {
  0% {
    transform: scale(0);
    opacity: 0;
  }
  50% {
    transform: scale(1.2);
    opacity: 0.8;
  }
  100% {
    transform: scale(1);
    opacity: 1;
  }
}

/* 错误状态样式 */
.ai-error .message-bubble {
  background: linear-gradient(135deg, #ffeaea 0%, #fff5f5 100%);
  border: 1px solid #ffcdd2;
  box-shadow: 0 2px 8px rgba(244, 67, 54, 0.2);
}

.ai-error .message-content {
  color: #c62828;
  font-weight: 500;
}

/* 灵蛇智能新版欢迎消息样式 */
.serpent-welcome-new {
  font-size: 14px;
  line-height: 1.3;
  color: #333;
  max-width: 100%;
}

.serpent-welcome-new .main-title {
  font-size: 14px;
  font-weight: 500;
  color: #2c3e50;
  margin-bottom: 4px;
  text-align: center;
  padding: 6px 10px;
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
  border-radius: 6px;
  border-left: 3px solid #4a90e2;
}

.serpent-welcome-new .method-title {
  font-size: 13px;
  font-weight: 600;
  color: #4a90e2;
  margin-bottom: 3px;
  display: flex;
  align-items: center;
}

.serpent-welcome-new .cards-container {
  display: flex;
  flex-direction: column;
  gap: 2px;
  margin-bottom: 4px;
}

.serpent-welcome-new .feature-card {
  background: linear-gradient(135deg, #e3f2fd 0%, #f0f7ff 100%);
  border: 1px solid #bbdefb;
  border-left: 3px solid #2196f3;
  border-radius: 6px;
  padding: 6px 8px;
  transition: all 0.2s ease;
}

.serpent-welcome-new .feature-card:hover {
  transform: translateX(4px);
  box-shadow: 0 2px 8px rgba(33, 150, 243, 0.2);
}

.serpent-welcome-new .feature-card .card-title {
  font-size: 12px;
  font-weight: 600;
  color: #1976d2;
  margin-bottom: 1px;
}

.serpent-welcome-new .feature-card .card-content {
  font-size: 11px;
  color: #555;
  line-height: 1.2;
}

.serpent-welcome-new .example-card {
  background: linear-gradient(135deg, #fff3e0 0%, #fff8f0 100%);
  border: 1px solid #ffcc02;
  border-left: 3px solid #ff9800;
  border-radius: 6px;
  padding: 6px 8px;
  margin-bottom: 3px;
}

.serpent-welcome-new .example-card .example-title {
  font-size: 12px;
  font-weight: 600;
  color: #f57c00;
  margin-bottom: 2px;
}

.serpent-welcome-new .example-card .example-content {
  display: flex;
  align-items: flex-start;
  gap: 4px;
}

.serpent-welcome-new .example-card .example-text {
  flex: 1;
  font-size: 11px;
  color: #555;
  line-height: 1.2;
  background: rgba(255, 255, 255, 0.7);
  padding: 4px 6px;
  border-radius: 4px;
  border: 1px solid rgba(255, 152, 0, 0.2);
}

.serpent-welcome-new .example-card .example-copy-btn {
  background: #ff9800;
  border: none;
  border-radius: 4px;
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  font-size: 12px;
  color: white;
  transition: all 0.2s ease;
  flex-shrink: 0;
}

.serpent-welcome-new .example-card .example-copy-btn:hover {
  background: #f57c00;
  transform: scale(1.05);
}

.serpent-welcome-new .example-card .example-copy-btn:active {
  transform: scale(0.95);
}

.serpent-welcome-new .tip-section {
  background: linear-gradient(135deg, #e8f5e8 0%, #f0fff0 100%);
  border: 1px solid #c8e6c8;
  border-left: 3px solid #4caf50;
  border-radius: 6px;
  padding: 5px 8px;
  font-size: 11px;
  color: #2e7d32;
  display: flex;
  align-items: center;
  margin-top: 2px;
}

/* 响应式调整 */
@media (max-width: 480px) {
  .serpent-welcome-new .main-title {
    font-size: 14px;
    padding: 10px;
  }
  
  .serpent-welcome-new .feature-card {
    padding: 10px;
  }
  
  .serpent-welcome-new .feature-card .card-title {
    font-size: 12px;
  }
  
  .serpent-welcome-new .feature-card .card-content {
    font-size: 11px;
  }
  
  .serpent-welcome-new .example-card {
    padding: 10px;
  }
  
  .serpent-welcome-new .example-card .example-text {
    font-size: 11px;
    padding: 6px;
  }
  
  .serpent-welcome-new .example-card .example-copy-btn {
    width: 24px;
    height: 24px;
    font-size: 12px;
  }
  
  .serpent-welcome-new .tip-section {
    font-size: 11px;
    padding: 8px 10px;
  }
}
</style>