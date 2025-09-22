<template>
  <div class="super-agent-container">
    <div class="header">
      <div class="back-button" @click="goBack">
        <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
          <path d="M19 12H5M12 19L5 12L12 5" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
        返回
      </div>
      <div class="header-center">
        <div class="opmanus-icon-small">
          <div class="snake-container-small">
            <div class="snake-outer-small">
              <div class="snake-inner-small">
                <svg width="24" height="24" viewBox="0 0 32 32" fill="none">
                  <!-- 蛇身体 - 科技感蛇形 -->
                  <path d="M10 20C12 18 14 18 16 20C18 22 20 22 22 20C24 18 26 16 26 14C26 12 24 10 22 10C20 10 18 12 16 12C14 12 12 10 10 10C8 10 6 12 6 14C6 16 8 18 10 20Z" stroke="white" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" fill="none"/>
                  <!-- 蛇头 - 更明显的三角形 -->
                  <path d="M26 14L28 14L27 12" stroke="white" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                  <!-- 蛇眼 - 更大更明显 -->
                  <circle cx="28" cy="14" r="1.5" fill="white"/>
                  <!-- 蛇舌 - 增加辨识度 -->
                  <path d="M28 15L30 14M28 15L30 16" stroke="white" stroke-width="1.5" stroke-linecap="round"/>
                  <!-- 科技感线条 -->
                  <path d="M8 14L26 14" stroke="white" stroke-width="1" stroke-dasharray="2" opacity="0.7"/>
                  <path d="M12 18L22 18" stroke="white" stroke-width="1" stroke-dasharray="2" opacity="0.7"/>
                  <path d="M12 10L22 10" stroke="white" stroke-width="1" stroke-dasharray="2" opacity="0.7"/>
                  <!-- 科技感圆点 -->
                  <circle cx="16" cy="14" r="1" fill="white" opacity="0.8"/>
                  <circle cx="12" cy="14" r="0.8" fill="white" opacity="0.6"/>
                  <circle cx="20" cy="14" r="0.8" fill="white" opacity="0.6"/>
                </svg>
              </div>
            </div>
          </div>
        </div>
        <h1 class="title">QpManus-灵蛇智能</h1>
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
          ai-type="super"
          @send-message="sendMessage"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue';
import { useRouter } from 'vue-router';
import { useHead } from '@vueuse/head';
import ChatRoom from '../components/ChatRoom.vue';
import AppFooter from '../components/AppFooter.vue';
import { chatWithManus } from '../api';

// 设置页面标题和元数据
useHead({
  title: 'QpManus-灵蛇智能 - Serpent AI',
  meta: [
    {
      name: 'description',
      content: 'OpManus 灵蛇智能是 Serpent AI 的全能助手，能解答各类专业问题，提供精准建议和解决方案'
    },
    {
      name: 'keywords',
      content: 'Serpent AI,OpManus,灵蛇智能,智能助手,专业问答,AI问答,专业建议,AI智能体'
    }
  ]
});

const router = useRouter();
const messages = ref([]);
const connectionStatus = ref('disconnected');
let eventSource = null;

// 添加消息到列表
function addMessage(content, isUser, type = '') {
  messages.value.push({
    content: content,
    isUser: isUser,
    type: type,
    time: new Date().getTime()
  });
}

// 发送消息
function sendMessage(message) {
  addMessage(message, true, 'user-question');
  
  // 连接SSE
  if (eventSource) {
    eventSource.close();
  }
  
  // 设置连接状态
  connectionStatus.value = 'connecting';
  
  // 添加思考动画消息
  const thinkingMessageIndex = messages.value.length;
  addMessage('🐍 灵蛇智能正在思考中...', false, 'ai-thinking');
  
  // 临时存储
  let messageBuffer = [];
  let lastBubbleTime = Date.now();
  let isFirstResponse = true;
  let hasReceivedContent = false;
  
  const chineseEndPunctuation = ['。', '！', '？', '…'];
  const minBubbleInterval = 800;
  
  // 过滤文件路径的函数
  function filterFilePaths(content) {
    // 匹配常见的文件路径模式并替换
    return content
      .replace(/[A-Za-z]:\\[^"'\s]+/g, '[文件已生成]')
      .replace(/\/[^"'\s]+\.(txt|pdf|ppt|pptx|doc|docx|xlsx|csv)/gi, '[文件已生成]')
      .replace(/D:\\code_space\\project_study\\ping-ai-agent\\[^"'\s]*/g, '[文件已生成]')
      .replace(/保存路径：[^"'\s]+/g, '保存路径：[文件已生成]')
      .replace(/文件路径：[^"'\s]+/g, '文件路径：[文件已生成]')
      .replace(/successfully to [^"'\s]+/g, 'successfully');
  }

  // 检查是否为任务完成消息
  function isTaskCompleted(content) {
    const completionKeywords = [
      'Task completed',
      '任务完成',
      '已完成任务',
      'doTerminate',
      'Successful execution',
      'SSE connection completed'
    ];
    return completionKeywords.some(function(keyword) {
      return content.includes(keyword);
    });
  }

  // 创建消息气泡的函数
  function createBubble(content, type) {
    type = type || 'ai-answer';
    if (!content.trim()) {
      return;
    }
    
    // 过滤文件路径
    const filteredContent = filterFilePaths(content);
    
    // 如果是第一次接收到内容，移除思考消息
    if (!hasReceivedContent) {
      messages.value.splice(thinkingMessageIndex, 1);
      hasReceivedContent = true;
    }
    
    // 添加适当的延迟，使消息显示更自然
    const now = Date.now();
    const timeSinceLastBubble = now - lastBubbleTime;
    
    if (isFirstResponse) {
      // 第一条消息立即显示
      addMessage(filteredContent, false, type);
      isFirstResponse = false;
    } else if (timeSinceLastBubble < minBubbleInterval) {
      // 如果与上一气泡间隔太短，添加一个延迟
      setTimeout(function() {
        addMessage(filteredContent, false, type);
      }, minBubbleInterval - timeSinceLastBubble);
    } else {
      // 正常添加消息
      addMessage(filteredContent, false, type);
    }
    
    lastBubbleTime = now;
    messageBuffer = [];
  }
  
  eventSource = chatWithManus(message);
  
  // 监听SSE消息
  eventSource.onmessage = function(event) {
    try {
      const data = event.data;
      
      if (data && data !== '[DONE]') {
        messageBuffer.push(data);
        
        // 检查是否应该创建新气泡
        const combinedText = messageBuffer.join('');
        
        // 句子结束或消息长度达到阈值
        const lastChar = data.charAt(data.length - 1);
        const hasCompleteSentence = chineseEndPunctuation.includes(lastChar) || data.includes('\n\n');
        const isLongEnough = combinedText.length > 40;
        
        if (hasCompleteSentence || isLongEnough) {
          createBubble(combinedText);
        }
      }
      
      if (data === '[DONE]') {
        // 如果还有未显示的内容，创建最后一个气泡
        if (messageBuffer.length > 0) {
          const remainingContent = messageBuffer.join('');
          createBubble(remainingContent, 'ai-final');
        }
        
        // 如果没有接收到任何内容，移除思考消息
        if (!hasReceivedContent) {
          messages.value.splice(thinkingMessageIndex, 1);
        }
        
        // 添加任务完成消息
        setTimeout(function() {
          addMessage('✅ 灵蛇智能已完成任务', false, 'ai-completed');
        }, 500);
        
        // 完成后关闭连接
        connectionStatus.value = 'disconnected';
        eventSource.close();
      }
    } catch (error) {
      console.error('SSE Message Processing Error:', error);
      // 即使发生错误也继续处理，避免影响用户体验
    }
  };
  
  // 监听SSE错误
  eventSource.onerror = function(error) {
    console.error('SSE Error:', error);
    
    // 检查是否实际上是任务完成（而非真正的错误）
    const allContent = messageBuffer.join('');
    const isActuallyCompleted = isTaskCompleted(allContent) || hasReceivedContent;
    
    if (isActuallyCompleted) {
      // 这是任务完成，不是错误
      if (messageBuffer.length > 0) {
        const remainingContent = messageBuffer.join('');
        createBubble(remainingContent, 'ai-final');
      }
      
      // 如果没有接收到任何内容，移除思考消息
      if (!hasReceivedContent && thinkingMessageIndex < messages.value.length) {
        messages.value.splice(thinkingMessageIndex, 1);
      }
      
      // 添加任务完成消息
      setTimeout(function() {
        addMessage('✅ 灵蛇智能已完成任务', false, 'ai-completed');
      }, 500);
      
      connectionStatus.value = 'disconnected';
    } else {
      // 这是真正的错误
      connectionStatus.value = 'error';
      
      // 移除思考消息
      if (!hasReceivedContent && thinkingMessageIndex < messages.value.length) {
        messages.value.splice(thinkingMessageIndex, 1);
      }
      
      // 如果出错时有未显示的内容，也创建气泡
      if (messageBuffer.length > 0) {
        const remainingContent = messageBuffer.join('');
        createBubble(remainingContent, 'ai-error');
      }
      
      // 添加错误消息
      addMessage('❌ 连接出现问题，请重试', false, 'ai-error');
    }
    
    eventSource.close();
  };
}

// 返回主页
function goBack() {
  router.push('/');
}

// 获取状态文本
function getStatusText() {
  switch (connectionStatus.value) {
    case 'connecting':
      return '连接中';
    case 'connected':
      return '已连接';
    case 'error':
      return '连接错误';
    default:
      return '就绪';
  }
}

// 定义示例消息数据
const exampleMessages = [
  '帮我写一个200字的计算机发展报告，并以PPT的形式发送给邮箱：your@email.com',
  '总结这份市场调研报告的核心观点和建议',
  '生成一份项目周会会议纪要模板',
  '分析最近三个月的销售数据趋势'
];

// 复制功能
async function handleCopyExample(text, buttonElement) {
  try {
    await navigator.clipboard.writeText(text);
    
    // 显示复制成功的提示
    const originalContent = buttonElement.innerHTML;
    buttonElement.innerHTML = '✅';
    buttonElement.style.backgroundColor = '#4caf50';
    buttonElement.style.color = 'white';
    
    setTimeout(function() {
      buttonElement.innerHTML = originalContent;
      buttonElement.style.backgroundColor = '';
      buttonElement.style.color = '';
    }, 1500);
    
    return true;
  } catch (err) {
    console.error('复制失败:', err);
    // 显示复制失败的提示
    const originalContent = buttonElement.innerHTML;
    buttonElement.innerHTML = '❌';
    buttonElement.style.backgroundColor = '#f44336';
    buttonElement.style.color = 'white';
    
    setTimeout(function() {
      buttonElement.innerHTML = originalContent;
      buttonElement.style.backgroundColor = '';
      buttonElement.style.color = '';
    }, 1500);
    
    return false;
  }
}

// 将函数暴露到全局，供动态创建的按钮使用
window.handleCopyExample = handleCopyExample;

// 构建欢迎消息的简单函数
function buildWelcomeMessage() {
  let html = '';
  html += '<div style="margin-bottom: 12px;">我是您的智能助手，具备强大的文档处理和办公能力。</div>';
  html += '<div style="margin-bottom: 8px;">🚀 使用方法：</div>';
  html += '<div style="color: #165DFF; font-weight: 500; margin-bottom: 4px;">文档生成</div>';
  html += '<div style="background: #E8F3FF; border-radius: 6px; padding: 4px 8px; border-left: 3px solid #165DFF; margin-bottom: 10px;">直接输入："生成PDF"或"生成PPT"</div>';
  html += '<div style="color: #165DFF; font-weight: 500; margin-bottom: 4px;">获取文件</div>';
  html += '<div style="background: #E8F3FF; border-radius: 6px; padding: 4px 8px; border-left: 3px solid #165DFF; margin-bottom: 10px;">提供邮箱地址：如 "16608683257@163.com"</div>';
  html += '<div style="color: #165DFF; font-weight: 500; margin-bottom: 4px;">智能对话</div>';
  html += '<div style="background: #E8F3FF; border-radius: 6px; padding: 4px 8px; border-left: 3px solid #165DFF; margin-bottom: 10px;">直接描述您的需求，我会智能分析并执行</div>';
  html += '<div style="color: #165DFF; font-weight: 500; margin-bottom: 4px;">完整示例 💡</div>';
  
  // 包含复制按钮的完整示例部分
  const exampleText = "帮我写一个200字的计算机发展报告，并以PPT的形式发送给邮箱：16608683257@163.com";
  html += '<div style="background: #E8F3FF; border-radius: 6px; padding: 4px 8px; border-left: 3px solid #165DFF; display: flex; align-items: center; margin-bottom: 10px;">';
  html += '<span style="flex: 1;">' + exampleText + '</span>';
  html += '<button style="background: rgba(22,93,255,0.1); border:1px solid rgba(22,93,255,0.2); color:#165DFF; border-radius:4px; padding:2px 6px; font-size:12px; cursor:pointer; transition:all 0.2s;" onclick="handleCopyExample(\'' + exampleText + '\', this)">📋</button>';
  html += '</div>';
  
  html += '<div style="font-size: 13px; color: #666;">💡 小贴士：您可以随时描述需求，我会自动选择合适的工具来帮助您！</div>';
  
  return html;
}

// 页面加载时添加欢迎消息
onMounted(function() {
  addMessage(buildWelcomeMessage(), false);
});

// 组件销毁前关闭SSE连接
onBeforeUnmount(function() {
  if (eventSource) {
    eventSource.close();
  }
});
</script>

<style scoped>
.super-agent-container {
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

.opmanus-icon-small {
  width: 44px;
  height: 44px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.snake-container-small {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}

.snake-outer-small {
  width: 44px;
  height: 44px;
  background: linear-gradient(135deg, #1a1a2e 0%, #16213e 25%, #0f3460 50%, #533483 75%, #764ba2 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 3px solid transparent;
  background-clip: padding-box;
  box-shadow: 
    0 0 0 2px rgba(255, 255, 255, 0.1),
    0 8px 32px rgba(102, 126, 234, 0.4),
    0 4px 16px rgba(118, 75, 162, 0.3),
    inset 0 1px 3px rgba(255, 255, 255, 0.2),
    inset 0 -1px 3px rgba(0, 0, 0, 0.3);
  position: relative;
  overflow: hidden;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
}

.snake-outer-small::before {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: linear-gradient(45deg, 
    transparent 30%, 
    rgba(255, 255, 255, 0.1) 40%, 
    rgba(102, 126, 234, 0.3) 50%, 
    rgba(255, 255, 255, 0.1) 60%, 
    transparent 70%);
  transform: rotate(-45deg);
  animation: snakeShimmerSmall 6s ease-in-out infinite;
}

.snake-outer-small::after {
  content: '';
  position: absolute;
  top: 2px;
  left: 2px;
  right: 2px;
  bottom: 2px;
  background: linear-gradient(135deg, 
    rgba(255, 255, 255, 0.1) 0%, 
    transparent 30%, 
    rgba(102, 126, 234, 0.1) 70%, 
    rgba(255, 255, 255, 0.05) 100%);
  border-radius: 50%;
  z-index: 1;
}

@keyframes snakeShimmerSmall {
  0%, 100% {
    transform: translateX(-100%) translateY(-100%) rotate(-45deg);
    opacity: 0.3;
  }
  50% {
    transform: translateX(100%) translateY(100%) rotate(-45deg);
    opacity: 0.8;
  }
}

.snake-inner-small {
  position: relative;
  z-index: 3;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  animation: snakeMoveSmall 6s ease-in-out infinite;
  filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.3));
}

@keyframes snakeMoveSmall {
  0%, 100% {
    transform: rotate(0deg) scale(1);
  }
  25% {
    transform: rotate(3deg) scale(1.02);
  }
  50% {
    transform: rotate(0deg) scale(1.05);
  }
  75% {
    transform: rotate(-3deg) scale(1.02);
  }
}

.opmanus-icon-small:hover .snake-outer-small {
  transform: scale(1.15);
  box-shadow: 
    0 0 0 3px rgba(255, 255, 255, 0.2),
    0 12px 40px rgba(102, 126, 234, 0.5),
    0 6px 20px rgba(118, 75, 162, 0.4),
    inset 0 2px 6px rgba(255, 255, 255, 0.3),
    inset 0 -2px 6px rgba(0, 0, 0, 0.4);
}

.opmanus-icon-small:hover .snake-inner-small {
  animation-duration: 2s;
  filter: drop-shadow(0 4px 8px rgba(0, 0, 0, 0.4));
}

.title {
  font-size: 20px;
  font-weight: 600;
  margin: 0;
  color: #1f2937;
}

.status-indicator {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: #6b7280;
  background: rgba(107, 114, 128, 0.1);
  padding: 6px 12px;
  border-radius: 20px;
}

.status-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  transition: all 0.3s;
}

.status-dot.disconnected {
  background-color: #10b981;
}

.status-dot.connecting {
  background-color: #f59e0b;
  animation: pulse 1.5s infinite;
}

.status-dot.connected {
  background-color: #10b981;
}

.status-dot.error {
  background-color: #ef4444;
}

@keyframes pulse {
  0%, 100% {
    opacity: 1;
  }
  50% {
    opacity: 0.5;
  }
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
  
  .status-indicator {
    font-size: 12px;
    padding: 4px 8px;
  }
  
  .chat-area {
    padding: 16px;
  }
  
  .opmanus-icon-small {
    width: 38px;
    height: 38px;
  }
  
  .snake-outer-small {
    width: 38px;
    height: 38px;
  }
  
  .back-button {
    font-size: 15px;
    padding: 6px 10px;
  }
  
  .header-center {
    gap: 10px;
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
  
  .status-indicator {
    display: none;
  }
  
  .chat-area {
    padding: 12px;
  }
  
  .opmanus-icon-small {
    width: 34px;
    height: 34px;
  }
  
  .snake-outer-small {
    width: 34px;
    height: 34px;
  }
  
  .header-center {
    gap: 8px;
  }
  
  /* 小屏幕下进一步优化图标 */
  .snake-outer-small {
    border-width: 2px;
    box-shadow: 
      0 0 0 1px rgba(255, 255, 255, 0.1),
      0 6px 24px rgba(102, 126, 234, 0.3),
      0 3px 12px rgba(118, 75, 162, 0.2),
      inset 0 1px 2px rgba(255, 255, 255, 0.2);
  }
  
  .snake-inner-small svg {
    width: 20px;
    height: 20px;
  }
}
</style>