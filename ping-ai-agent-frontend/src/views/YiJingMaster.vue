<template>
  <div class="yi-jing-master-container">
    <div class="header">
      <div class="back-button" @click="goBack">
        <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
          <path d="M19 12H5M12 19L5 12L12 5" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
        返回
      </div>
      <div class="header-center">
        <div class="yi-jing-icon">
          <div class="yin-yang-container-small">
            <div class="yin-yang-outer-small">
              <div class="yin-yang-inner-small">
                <div class="yin-dot-small"></div>
                <div class="yang-dot-small"></div>
              </div>
            </div>
          </div>
        </div>
        <h1 class="title">易经大师</h1>
      </div>
      <div class="status-indicator">
        <div class="status-dot" :class="connectionStatus"></div>
        {{ getStatusText() }}
      </div>
    </div>
    
    <div class="content-wrapper">
      <div class="chat-area">
        <ChatRoom 
          :messages="messages" 
          :connection-status="connectionStatus"
          ai-type="yijing"
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
import { chatWithYiJingApp } from '../api'

// 设置页面标题和元数据
useHead({
  title: '易经大师 - Serpent AI',
  meta: [
    {
      name: 'description',
      content: '易经大师是 Serpent AI 的易经占卜顾问，帮您解读易经卦象，提供生活指导和决策建议'
    },
    {
      name: 'keywords',
      content: 'Serpent AI,易经大师,易经占卜,周易预测,风水命理,AI算命,AI智能体'
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
  return 'yijing_' + Math.random().toString(36).substring(2, 10)
}

// 添加消息到列表
const addMessage = (content, isUser, type = '') => {
  messages.value.push({
    content,
    isUser,
    type,
    time: new Date().getTime()
  })
}

// 发送消息
const sendMessage = (message) => {
  addMessage(message, true, 'user-question')
  
  // 连接SSE
  if (eventSource) {
    eventSource.close()
  }
  
  // 创建一个空的AI回复消息
  const aiMessageIndex = messages.value.length
  addMessage('', false, 'ai-answer')
  
  connectionStatus.value = 'connecting'
  eventSource = chatWithYiJingApp(message, chatId.value)
  
  // 监听SSE消息
  eventSource.onmessage = (event) => {
    try {
      const data = event.data
      if (data && data !== '[DONE]') {
        // 更新最新的AI消息内容，而不是创建新消息
        if (aiMessageIndex < messages.value.length) {
          messages.value[aiMessageIndex].content += data
          messages.value[aiMessageIndex].type = 'ai-answer'
        }
      } else if (data === '[DONE]') {
        // 对话完成
        connectionStatus.value = 'disconnected'
        if (aiMessageIndex < messages.value.length) {
          messages.value[aiMessageIndex].type = 'ai-final'
        }
      }
    } catch (error) {
      console.error('处理SSE消息错误:', error)
    }
  }
  
  eventSource.onerror = (error) => {
    console.error('SSE连接错误:', error)
    connectionStatus.value = 'disconnected'
    if (aiMessageIndex < messages.value.length) {
      // 不将消息类型设置为ai-error，避免错误样式
      messages.value[aiMessageIndex].type = 'ai-answer'
    }
    eventSource.close()
  }
}

// 获取状态文本
const getStatusText = () => {
  switch (connectionStatus.value) {
    case 'connecting':
      return '思考中...'
    case 'disconnected':
      return '就绪'
    default:
      return '未知'
  }
}

// 返回上一页
const goBack = () => {
  router.push('/')
}

onMounted(() => {
  // 生成会话ID
  chatId.value = generateChatId()
  
  // 添加欢迎消息
  addMessage('您好，我是易经大师。请问有什么可以为您占卜或解答的问题吗？', false)
})

onBeforeUnmount(() => {
  // 关闭SSE连接
  if (eventSource) {
    eventSource.close()
  }
})
</script>

<style scoped>
.yi-jing-master-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f5f5 0%, #e8e8e8 100%);
  display: flex;
  flex-direction: column;
  position: relative;
}

.header {
  background: linear-gradient(135deg, #8B4513 0%, #A0522D 100%);
  color: white;
  padding: 16px 24px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 2px 10px rgba(139, 69, 19, 0.3);
  position: sticky;
  top: 0;
  z-index: 1000;
}

.back-button {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 8px 12px;
  border-radius: 8px;
  transition: background-color 0.3s;
}

.back-button:hover {
  background-color: rgba(255, 255, 255, 0.1);
}

.back-button svg {
  margin-right: 6px;
}

.header-center {
  display: flex;
  align-items: center;
  flex: 1;
  justify-content: center;
}

.yi-jing-icon {
  margin-right: 12px;
}

.title {
  font-size: 24px;
  font-weight: 600;
  letter-spacing: 1px;
}

.chat-id {
  font-size: 14px;
  opacity: 0.8;
  background-color: rgba(255, 255, 255, 0.1);
  padding: 6px 12px;
  border-radius: 16px;
}

.status-indicator {
  display: flex;
  align-items: center;
  font-size: 14px;
  opacity: 0.9;
}

.status-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  margin-right: 6px;
  animation: pulse 2s infinite;
}

.status-dot.connecting {
  background-color: #ffc107;
}

.status-dot.disconnected {
  background-color: #28a745;
  animation: none;
}

@keyframes pulse {
  0% {
    transform: scale(1);
    opacity: 1;
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

.content-wrapper {
  flex: 1;
  padding: 24px;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
}

.chat-area {
  width: 100%;
  max-width: 800px;
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

/* 易经图标样式 */
.yin-yang-container-small {
  width: 36px;
  height: 36px;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

.yin-yang-outer-small {
  width: 100%;
  height: 100%;
  background: linear-gradient(to bottom, #000 50%, #fff 50%);
  border-radius: 50%;
  border: 2px solid #8B4513;
  position: relative;
}

.yin-yang-inner-small {
  position: absolute;
  width: 50%;
  height: 50%;
  top: 0;
  left: 25%;
  background: #000;
  border-radius: 50%;
  border: 2px solid #8B4513;
}

.yin-yang-inner-small::after {
  content: '';
  position: absolute;
  width: 50%;
  height: 50%;
  top: 50%;
  left: 25%;
  background: #fff;
  border-radius: 50%;
}

.yin-dot-small {
  position: absolute;
  width: 12%;
  height: 12%;
  top: 44%;
  left: 44%;
  background: #fff;
  border-radius: 50%;
  border: 1px solid #8B4513;
}

.yang-dot-small {
  position: absolute;
  width: 12%;
  height: 12%;
  top: 160%;
  left: 44%;
  background: #000;
  border-radius: 50%;
  border: 1px solid #8B4513;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .header {
    padding: 12px 16px;
  }
  
  .title {
    font-size: 20px;
  }
  
  .content-wrapper {
    padding: 16px;
  }
  
  .chat-area {
    box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
  }
}

@media (max-width: 480px) {
  .header {
    padding: 10px 12px;
  }
  
  .back-button {
    padding: 6px 8px;
  }
  
  .title {
    font-size: 18px;
  }
  
  .content-wrapper {
    padding: 8px;
  }
}
</style>