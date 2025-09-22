<template>
  <div class="love-master-container">
    <div class="header">
      <div class="back-button" @click="goBack">
        <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
          <path d="M19 12H5M12 19L5 12L12 5" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
        返回
      </div>
      <div class="header-center">
        <div class="love-master-icon">
          <div class="heart-container-small">
            <div class="heart-outer-small">
              <div class="heart-inner-small"></div>
            </div>
          </div>
        </div>
        <h1 class="title">恋爱大师</h1>
      </div>
      <div class="chat-id">会话: {{ chatId.slice(-6) }}</div>
    </div>
    
    <div class="content-wrapper">
      <div class="chat-area">
        <ChatRoom 
          :messages="messages" 
          :connection-status="connectionStatus"
          ai-type="love"
          @send-message="sendMessage"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'
import { useRouter } from 'vue-router'
import { useHead } from '@vueuse/head'
import ChatRoom from '../components/ChatRoom.vue'
import AppFooter from '../components/AppFooter.vue'
import { chatWithLoveApp } from '../api'

// 设置页面标题和元数据
useHead({
  title: '恋爱大师 - Serpent AI',
  meta: [
    {
      name: 'description',
      content: '恋爱大师是 Serpent AI 的专业情感顾问，帮你解答各种恋爱问题，提供情感建议'
    },
    {
      name: 'keywords',
      content: 'Serpent AI,恋爱大师,情感顾问,恋爱咨询,AI聊天,情感问题,AI智能体'
    }
  ]
})

const router = useRouter()
const messages = ref([])
const chatId = ref('')
const connectionStatus = ref('disconnected')
let eventSource = null

// 生成随机会话ID
const generateChatId = () => {
  return 'love_' + Math.random().toString(36).substring(2, 10)
}

// 添加消息到列表
const addMessage = (content, isUser) => {
  messages.value.push({
    content,
    isUser,
    time: new Date().getTime()
  })
}

// 发送消息
const sendMessage = (message) => {
  addMessage(message, true)
  
  // 连接SSE
  if (eventSource) {
    eventSource.close()
  }
  
  // 创建一个空的AI回复消息
  const aiMessageIndex = messages.value.length
  addMessage('', false)
  
  connectionStatus.value = 'connecting'
  eventSource = chatWithLoveApp(message, chatId.value)
  
  // 监听SSE消息
  eventSource.onmessage = (event) => {
    try {
      const data = event.data
      if (data && data !== '[DONE]') {
        // 更新最新的AI消息内容，而不是创建新消息
        if (aiMessageIndex < messages.value.length) {
          messages.value[aiMessageIndex].content += data
        }
      }
      
      if (data === '[DONE]') {
        connectionStatus.value = 'disconnected'
        eventSource.close()
      }
    } catch (error) {
      console.error('SSE Message Processing Error:', error)
      // 即使发生错误也保持消息类型为正常的回答，避免粉红色背景
    }
  }
  
  // 监听SSE错误
  eventSource.onerror = (error) => {
    console.error('SSE Error:', error)
    connectionStatus.value = 'error'
    // 修改错误状态下的消息类型，避免触发粉红色背景
    if (aiMessageIndex < messages.value.length && messages.value[aiMessageIndex].type === 'ai-thinking') {
      messages.value[aiMessageIndex].type = 'ai-answer'
    }
    eventSource.close()
  }
}

// 返回主页
const goBack = () => {
  router.push('/')
}

// 页面加载时添加欢迎消息
onMounted(() => {
  // 生成聊天ID
  chatId.value = generateChatId()
  
  // 添加欢迎消息
  addMessage('欢迎来到AI恋爱大师，请告诉我你的恋爱问题，我会尽力给予帮助和建议。', false)
})

// 组件销毁前关闭SSE连接
onBeforeUnmount(() => {
  if (eventSource) {
    eventSource.close()
  }
})
</script>

<style scoped>
.love-master-container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background: linear-gradient(135deg, #a8d8ff 0%, #c8e6ff 50%, #e8f4ff 100%);
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 24px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  color: #1f2937;
  box-shadow: 0 2px 20px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 10;
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
}

.back-button {
  font-size: 16px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 8px;
  transition: all 0.2s;
  color: #4a90e2;
  font-weight: 500;
  padding: 8px 12px;
  border-radius: 8px;
  background: rgba(74, 144, 226, 0.1);
}

.back-button:hover {
  background: rgba(74, 144, 226, 0.2);
  transform: translateX(-2px);
}

.header-center {
  display: flex;
  align-items: center;
  gap: 12px;
}

.love-master-icon {
  width: 44px;
  height: 44px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.heart-container-small {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}

.heart-outer-small {
  width: 44px;
  height: 44px;
  background: linear-gradient(135deg, #ff6b9d 0%, #ff8fab 50%, #ffb3d1 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 2px solid rgba(255, 255, 255, 0.9);
  box-shadow: 
    0 6px 24px rgba(255, 107, 157, 0.25),
    0 3px 12px rgba(255, 107, 157, 0.15),
    inset 0 1px 3px rgba(255, 255, 255, 0.3);
  position: relative;
  overflow: hidden;
  transition: all 0.3s ease;
}

.heart-outer-small::before {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: linear-gradient(45deg, transparent, rgba(255, 255, 255, 0.15), transparent);
  transform: rotate(-45deg);
  animation: shimmerSmall 4s ease-in-out infinite;
}

@keyframes shimmerSmall {
  0%, 100% { transform: translateX(-100%) translateY(-100%) rotate(-45deg); }
  50% { transform: translateX(100%) translateY(100%) rotate(-45deg); }
}

.heart-inner-small {
  position: relative;
  z-index: 2;
}

.heart-inner-small::before {
  content: '💖';
  font-size: 22px;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  filter: drop-shadow(0 1px 4px rgba(255, 107, 157, 0.3));
  animation: heartbeatSmall 3s ease-in-out infinite;
}

@keyframes heartbeatSmall {
  0%, 100% { transform: translate(-50%, -50%) scale(1); }
  50% { transform: translate(-50%, -50%) scale(1.05); }
}

.love-master-icon:hover .heart-outer-small {
  transform: scale(1.1);
  box-shadow: 
    0 8px 32px rgba(255, 107, 157, 0.3),
    0 4px 16px rgba(255, 107, 157, 0.2),
    inset 0 1px 3px rgba(255, 255, 255, 0.4);
}

.title {
  font-size: 20px;
  font-weight: 600;
  margin: 0;
  color: #1f2937;
}

.chat-id {
  font-size: 14px;
  color: #6b7280;
  background: rgba(107, 114, 128, 0.1);
  padding: 4px 8px;
  border-radius: 6px;
  font-family: monospace;
}

.content-wrapper {
  display: flex;
  flex-direction: column;
  flex: 1;
}

.chat-area {
  flex: 1;
  padding: 20px;
  overflow: hidden;
  position: relative;
  min-height: calc(100vh - 80px);
}

/* 响应式样式 */
@media (max-width: 768px) {
  .header {
    padding: 12px 16px;
  }
  
  .title {
    font-size: 18px;
  }
  
  .chat-id {
    font-size: 12px;
  }
  
  .chat-area {
    padding: 16px;
  }
  
  .love-master-icon {
    width: 38px;
    height: 38px;
  }
  
  .heart-outer-small {
    width: 38px;
    height: 38px;
  }
  
  .heart-inner-small::before {
    font-size: 18px;
  }
}

@media (max-width: 480px) {
  .header {
    padding: 10px 12px;
  }
  
  .back-button {
    font-size: 14px;
    padding: 6px 8px;
  }
  
  .title {
    font-size: 16px;
  }
  
  .chat-id {
    display: none;
  }
  
  .chat-area {
    padding: 12px;
  }
  
  .love-master-icon {
    width: 34px;
    height: 34px;
  }
  
  .heart-outer-small {
    width: 34px;
    height: 34px;
  }
  
  .heart-inner-small::before {
    font-size: 16px;
  }
  
  .header-center {
    gap: 8px;
  }
}
</style>