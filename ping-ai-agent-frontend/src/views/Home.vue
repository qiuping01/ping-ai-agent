<template>
  <div class="home-container">
    <!-- 主要内容区域 -->
    <div class="main-content">
      <!-- Logo 和标题区域 -->
      <div class="header-section">
        <div class="header-content">
          <div class="logo-container">
            <img src="/logo.png" alt="Serpent AI Logo" class="logo" />
          </div>
          <div class="title-container">
            <h1 class="main-title">Serpent AI</h1>
            <p class="subtitle">灵蛇智启，洞察无界</p>
          </div>
        </div>
      </div>

      <!-- 功能卡片区域 -->
      <div class="cards-container">
        <!-- 恋爱大师卡片 -->
        <div class="feature-card love-master-card" @click="navigateTo('/love-master')">
          <div class="card-icon">
            <div class="icon-wrapper love-icon">
              <div class="heart-container">
                <div class="heart-outer">
                  <div class="heart-inner">💝</div>
                </div>
              </div>
            </div>
          </div>
          <div class="card-content">
            <h3 class="card-title">恋爱大师</h3>
            <p class="card-description">专业的情感咨询师，提供恋爱建议和情感指导</p>
            <div class="card-tags">
              <span class="tag">情感分析</span>
              <span class="tag">恋爱技巧</span>
              <span class="tag">关系维护</span>
            </div>
          </div>
          <div class="card-arrow">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
              <path d="M9 18L15 12L9 6" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </div>
        </div>

        <!-- QpManus-灵蛇智能卡片 -->
        <div class="feature-card opmanus-card" @click="navigateTo('/super-agent')">
          <div class="card-icon">
            <div class="icon-wrapper opmanus-icon">
              <div class="snake-container">
                <div class="snake-outer">
                  <div class="snake-inner">
                    <svg width="32" height="32" viewBox="0 0 32 32" fill="none">
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
          </div>
          <div class="card-content">
            <h3 class="card-title">QpManus-灵蛇智能</h3>
            <p class="card-description">强大的AI助手，具备多种手工具和能力</p>
            <div class="card-tags">
              <span class="tag">多工具集成</span>
              <span class="tag">智能分析</span>
              <span class="tag">问题解决</span>
              <span class="tag">生成PDF</span>
              <span class="tag">生成PPT</span>
              <span class="tag">发送邮件</span>
            </div>
          </div>
          <div class="card-arrow">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
              <path d="M9 18L15 12L9 6" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </div>
        </div>
        
        <!-- 易经大师卡片 -->
        <div class="feature-card yi-jing-card" @click="navigateTo('/yi-jing-master')">
          <div class="card-icon">
            <div class="icon-wrapper yi-jing-icon">
              <div class="yin-yang-container">
                <div class="yin-yang-outer">
                  <div class="yin-yang-inner">
                    <div class="yin-dot"></div>
                    <div class="yang-dot"></div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="card-content">
            <h3 class="card-title">易经大师</h3>
            <p class="card-description">专业的易经占卜顾问，解读易经卦象，提供生活指导</p>
            <div class="card-tags">
              <span class="tag">易经占卜</span>
              <span class="tag">卦象解读</span>
              <span class="tag">生活指导</span>
              <span class="tag">决策建议</span>
              <span class="tag">周易预测</span>
              <span class="tag">命理分析</span>
            </div>
          </div>
          <div class="card-arrow">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
              <path d="M9 18L15 12L9 6" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </div>
        </div>
      </div>
    </div>

    <!-- 底部信息 -->
    <div class="footer-info">
      <p>Powered by Qiu Ping • <a href="mailto:16608683257@163.com" class="email-link">16608683257@163.com</a></p>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useHead } from '@vueuse/head'

// 设置页面标题和元数据
useHead({
  title: 'Serpent AI - 灵蛇智启，洞察无界',
  meta: [
    {
      name: 'description',
      content: 'Serpent AI 提供恋爱大师和 OpManus 灵蛇智能服务，满足您的各种AI对话需求'
    },
    {
      name: 'keywords',
      content: 'Serpent AI,AI智能体,AI应用,恋爱大师,AI助手,智能对话,灵蛇智能,首页'
    }
  ]
})

const router = useRouter()

const navigateTo = (path) => {
  router.push(path)
}
</script>

<style scoped>
.home-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #a8d8ff 0%, #c8e6ff 30%, #d8ecff 70%, #e8f4ff 100%);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px 20px;
  position: relative;
  overflow: hidden;
  /* 禁用文本选择 */
  -webkit-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
  user-select: none;
  /* 禁用拖拽 */
  -webkit-user-drag: none;
  -khtml-user-drag: none;
  -moz-user-drag: none;
  -o-user-drag: none;
  user-drag: none;
}

.home-container::before {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle at 30% 20%, rgba(255, 255, 255, 0.3) 0%, transparent 50%),
              radial-gradient(circle at 70% 80%, rgba(255, 255, 255, 0.2) 0%, transparent 50%);
  animation: float 20s ease-in-out infinite;
  pointer-events: none;
}

@keyframes float {
  0%, 100% { transform: translate(0, 0) rotate(0deg); }
  33% { transform: translate(30px, -30px) rotate(1deg); }
  66% { transform: translate(-20px, 20px) rotate(-1deg); }
}

.main-content {
  width: 100%;
  max-width: 1400px;
  display: flex;
  flex-direction: column;
  align-items: center;
  flex: 1;
  justify-content: center;
}

/* Logo 和标题区域 */
.header-section {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 70px;
  width: 100%;
}

.header-content {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 30px;
  }
  
  .title-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    text-align: center;
  }
  
  /* 响应式调整 */
  @media (max-width: 768px) {
    .header-content {
      flex-direction: column;
      gap: 20px;
    }
    
    .main-title {
      margin: 20px 0 10px 0;
    }
  }

.logo-container {
  margin: 0;
}

.logo {
  width: 140px;
  height: 140px;
  border-radius: 50%;
  background: linear-gradient(135deg, #87ceeb, #b8dff0);
  padding: 15px;
  box-shadow: 
    0 0 0 8px rgba(135, 206, 235, 0.3),
    0 20px 40px rgba(135, 206, 235, 0.2),
    inset 0 0 0 2px rgba(255, 255, 255, 0.5);
  transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
  position: relative;
}

.logo::before {
  content: '';
  position: absolute;
  top: -10px;
  left: -10px;
  right: -10px;
  bottom: -10px;
  border-radius: 50%;
  background: linear-gradient(45deg, rgba(135, 206, 235, 0.1), rgba(184, 223, 240, 0.1));
  z-index: -1;
  animation: pulse 3s ease-in-out infinite;
}

@keyframes pulse {
  0%, 100% { transform: scale(1); opacity: 0.7; }
  50% { transform: scale(1.1); opacity: 0.3; }
}

.logo:hover {
  transform: scale(1.08) translateY(-5px);
  box-shadow: 
    0 0 0 12px rgba(135, 206, 235, 0.4),
    0 30px 60px rgba(135, 206, 235, 0.3),
    inset 0 0 0 2px rgba(255, 255, 255, 0.7);
}

.main-title {
  font-size: 4.5rem;
  font-weight: 200;
  background: linear-gradient(135deg, #6bb6ff 0%, #4a90e2 25%, #87ceeb 50%, #5ba3d4 75%, #6bb6ff 100%);
  background-size: 300% 300%;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin: 0 0 10px 0;
  letter-spacing: 8px;
  position: relative;
  animation: gradientShift 6s ease-in-out infinite;
  filter: drop-shadow(0 8px 16px rgba(107, 182, 255, 0.15));
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Helvetica Neue', Arial, sans-serif;
}

@keyframes gradientShift {
  0%, 100% {
    background-position: 0% 50%;
  }
  50% {
    background-position: 100% 50%;
  }
}

.main-title::before {
  content: '';
  position: absolute;
  top: -10px;
  left: -20px;
  right: -20px;
  bottom: -10px;
  background: linear-gradient(135deg, rgba(107, 182, 255, 0.1), rgba(74, 144, 226, 0.05));
  border-radius: 20px;
  z-index: -1;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.main-title:hover::before {
  opacity: 1;
}

.main-title::after {
  content: '';
  position: absolute;
  bottom: -12px;
  left: 50%;
  transform: translateX(-50%);
  width: 120px;
  height: 3px;
  background: linear-gradient(90deg, transparent, rgba(107, 182, 255, 0.8), rgba(74, 144, 226, 0.6), rgba(135, 206, 235, 0.8), transparent);
  border-radius: 2px;
  animation: lineGlow 3s ease-in-out infinite alternate;
}

@keyframes lineGlow {
  0% {
    box-shadow: 0 0 5px rgba(107, 182, 255, 0.3);
    opacity: 0.7;
  }
  100% {
    box-shadow: 0 0 20px rgba(107, 182, 255, 0.6), 0 0 30px rgba(74, 144, 226, 0.4);
    opacity: 1;
  }
}

.subtitle {
  font-size: 1.2rem;
  color: #7a8fa3;
  font-weight: 200;
  margin: 0;
  letter-spacing: 4px;
  opacity: 0.85;
  position: relative;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Helvetica Neue', Arial, sans-serif;
  text-shadow: 0 2px 4px rgba(122, 143, 163, 0.2);
}

.subtitle::before,
.subtitle::after {
  content: '';
  position: absolute;
  top: 50%;
  width: 40px;
  height: 1px;
  background: linear-gradient(90deg, transparent, rgba(122, 143, 163, 0.4), transparent);
}

.subtitle::before {
  left: -60px;
}

.subtitle::after {
  right: -60px;
}

/* 功能卡片区域 */
.cards-container {
  display: flex;
  gap: 50px;
  flex-wrap: wrap;
  justify-content: center;
  width: 100%;
}

.feature-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  padding: 30px;
  width: 360px;
  cursor: pointer;
  transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
  box-shadow: 
    0 8px 32px rgba(0, 0, 0, 0.08),
    0 2px 8px rgba(0, 0, 0, 0.04),
    inset 0 1px 0 rgba(255, 255, 255, 0.8);
  border: 1px solid rgba(255, 255, 255, 0.6);
  position: relative;
  overflow: hidden;
}

.feature-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.4), transparent);
  transition: left 0.6s;
}

.feature-card:hover::before {
  left: 100%;
}

.feature-card:hover {
  transform: translateY(-10px) scale(1.02);
  box-shadow: 
    0 20px 60px rgba(0, 0, 0, 0.15),
    0 8px 20px rgba(0, 0, 0, 0.08),
    inset 0 1px 0 rgba(255, 255, 255, 0.9);
  border-color: rgba(107, 182, 255, 0.3);
}

.card-icon {
  margin-bottom: 18px;
}

.icon-wrapper {
  width: 56px;
  height: 56px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.8rem;
  margin-bottom: 12px;
  position: relative;
  transition: all 0.3s ease;
}

.icon-wrapper::before {
  content: '';
  position: absolute;
  top: -3px;
  left: -3px;
  right: -3px;
  bottom: -3px;
  border-radius: 50%;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.love-icon {
  background: transparent;
  border: none;
  box-shadow: none;
}

.heart-container {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}

.heart-outer {
  width: 56px;
  height: 56px;
  background: linear-gradient(135deg, #ff6b9d 0%, #ff8fab 50%, #ffb3d1 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 3px solid rgba(255, 255, 255, 0.8);
  box-shadow: 
    0 8px 32px rgba(255, 107, 157, 0.3),
    0 4px 16px rgba(255, 107, 157, 0.2),
    inset 0 2px 4px rgba(255, 255, 255, 0.3);
  position: relative;
  overflow: hidden;
  transition: all 0.3s cubic-bezier(0.175, 0.885, 0.32, 1.275);
}

.heart-outer::before {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: linear-gradient(45deg, transparent, rgba(255, 255, 255, 0.2), transparent);
  transform: rotate(-45deg);
  animation: shimmer 3s ease-in-out infinite;
}

@keyframes shimmer {
  0%, 100% { transform: translateX(-100%) translateY(-100%) rotate(-45deg); }
  50% { transform: translateX(100%) translateY(100%) rotate(-45deg); }
}

.heart-inner {
  position: relative;
  z-index: 2;
}

.heart-inner::before {
  content: '💖';
  font-size: 28px;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  filter: drop-shadow(0 2px 8px rgba(255, 107, 157, 0.4));
  animation: heartbeat 2s ease-in-out infinite;
}

@keyframes heartbeat {
  0%, 100% { transform: translate(-50%, -50%) scale(1); }
  50% { transform: translate(-50%, -50%) scale(1.1); }
}

.feature-card:hover .heart-outer {
  transform: scale(1.15);
  box-shadow: 
    0 12px 48px rgba(255, 107, 157, 0.4),
    0 6px 24px rgba(255, 107, 157, 0.3),
    inset 0 2px 4px rgba(255, 255, 255, 0.4);
}

.opmanus-icon {
  background: transparent;
  border: none;
  box-shadow: none;
}

.yi-jing-icon {
  background: transparent;
  border: none;
  box-shadow: none;
}

.snake-container {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}

.snake-outer {
  width: 56px;
  height: 56px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 50%, #667eea 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 3px solid rgba(255, 255, 255, 0.9);
  box-shadow: 
    0 8px 32px rgba(102, 126, 234, 0.3),
    0 4px 16px rgba(118, 75, 162, 0.2),
    inset 0 2px 4px rgba(255, 255, 255, 0.3);
  position: relative;
  overflow: hidden;
  transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
}

.snake-outer::before {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: linear-gradient(45deg, transparent, rgba(255, 255, 255, 0.2), transparent);
  transform: rotate(-45deg);
  animation: snakeShimmer 4s ease-in-out infinite;
}

@keyframes snakeShimmer {
  0%, 100% { transform: translateX(-100%) translateY(-100%) rotate(-45deg); }
  50% { transform: translateX(100%) translateY(100%) rotate(-45deg); }
}

.snake-inner {
  position: relative;
  z-index: 2;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  animation: snakeMove 4s ease-in-out infinite;
}

@keyframes snakeMove {
  0%, 100% { transform: rotate(0deg); }
  25% { transform: rotate(5deg); }
  75% { transform: rotate(-5deg); }
}

.feature-card:hover .snake-outer {
  transform: scale(1.15);
  box-shadow: 
    0 12px 48px rgba(102, 126, 234, 0.4),
    0 6px 24px rgba(118, 75, 162, 0.3),
    inset 0 2px 4px rgba(255, 255, 255, 0.4);
}

.feature-card:hover .snake-inner {
  animation-duration: 2s !important;
}

.feature-card:hover .icon-wrapper::before {
  opacity: 0.3;
}

.feature-card:hover .icon-wrapper {
  transform: scale(1.1) rotate(5deg);
}

/* 易经图标样式 */
.yin-yang-container {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}

.yin-yang-outer {
  width: 56px;
  height: 56px;
  background: linear-gradient(to bottom, #000 50%, #fff 50%);
  border-radius: 50%;
  border: 3px solid rgba(255, 255, 255, 0.9);
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  box-shadow: 
    0 8px 32px rgba(212, 175, 55, 0.3),
    0 4px 16px rgba(240, 230, 140, 0.2),
    inset 0 2px 4px rgba(255, 255, 255, 0.3);
  transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
}

.yin-yang-outer::before {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: linear-gradient(45deg, transparent, rgba(255, 255, 255, 0.2), transparent);
  transform: rotate(-45deg);
  animation: yinYangShimmer 4s ease-in-out infinite;
}

@keyframes yinYangShimmer {
  0%, 100% { transform: translateX(-100%) translateY(-100%) rotate(-45deg); }
  50% { transform: translateX(100%) translateY(100%) rotate(-45deg); }
}

.yin-yang-inner {
  position: relative;
  z-index: 2;
  width: 100%;
  height: 100%;
}

.yin-dot {
  position: absolute;
  width: 12px;
  height: 12px;
  top: 22px;
  left: 22px;
  background: #fff;
  border-radius: 50%;
  border: 2px solid #000;
  box-shadow: 0 0 8px rgba(255, 255, 255, 0.6);
  animation: dotPulse 3s ease-in-out infinite;
}

.yang-dot {
  position: absolute;
  width: 12px;
  height: 12px;
  top: 22px;
  left: 22px;
  background: #000;
  border-radius: 50%;
  border: 2px solid #fff;
  box-shadow: 0 0 8px rgba(0, 0, 0, 0.6);
  animation: dotPulse 3s ease-in-out infinite 1.5s;
}

@keyframes dotPulse {
  0%, 100% { 
    transform: scale(1);
  }
  50% { 
    transform: scale(1.2);
  }
}

.feature-card:hover .yin-yang-outer {
  transform: scale(1.15) rotate(180deg);
  box-shadow: 
    0 12px 48px rgba(212, 175, 55, 0.4),
    0 6px 24px rgba(240, 230, 140, 0.3),
    inset 0 2px 4px rgba(255, 255, 255, 0.4);
}

.card-content {
  flex: 1;
}

.card-title {
  font-size: 1.5rem;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 12px;
  position: relative;
}

.card-description {
  font-size: 0.95rem;
  color: #6b7785;
  line-height: 1.6;
  margin-bottom: 20px;
  font-weight: 400;
}

.card-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
}

.tag {
  background: linear-gradient(135deg, rgba(107, 182, 255, 0.12), rgba(107, 182, 255, 0.18));
  color: #5a9de8;
  padding: 5px 14px;
  border-radius: 16px;
  font-size: 0.75rem;
  font-weight: 500;
  border: 1px solid rgba(107, 182, 255, 0.25);
  transition: all 0.2s ease;
  backdrop-filter: blur(10px);
}

.tag:hover {
  background: linear-gradient(135deg, rgba(107, 182, 255, 0.2), rgba(107, 182, 255, 0.3));
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(107, 182, 255, 0.2);
}

.card-arrow {
  position: absolute;
  top: 30px;
  right: 30px;
  color: #d1d5db;
  transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
  opacity: 0.6;
}

.feature-card:hover .card-arrow {
  color: #6bb6ff;
  transform: translateX(5px) scale(1.1);
  opacity: 1;
}

/* 特定卡片样式 */
.feature-card.love-master-card {
  background: linear-gradient(135deg, #ff9a9e 0%, #fad0c4 99%, #fad0c4 100%);
  box-shadow: 0 15px 35px rgba(255, 154, 158, 0.3);
}

.feature-card.opmanus-card {
  background: linear-gradient(135deg, #84fab0 0%, #8fd3f4 100%);
  box-shadow: 0 15px 35px rgba(132, 250, 176, 0.3);
}

.feature-card.yi-jing-card {
  background: linear-gradient(135deg, #D4AF37 0%, #F0E68C 100%);
  box-shadow: 0 15px 35px rgba(212, 175, 55, 0.3);
}

/* 底部信息 */
.footer-info {
  position: absolute;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  text-align: center;
}

.footer-info p {
  color: #7a8a9a;
  font-size: 0.85rem;
  margin: 0;
  opacity: 0.8;
}

.email-link {
  color: #6bb6ff;
  text-decoration: none;
  transition: all 0.2s ease;
}

.email-link:hover {
  color: #4a90e2;
  text-decoration: underline;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .main-title {
    font-size: 2.5rem;
  }
  
  .subtitle {
    font-size: 1rem;
  }
  
  .cards-container {
    gap: 20px;
  }
  
  .feature-card {
    width: 100%;
    max-width: 400px;
    padding: 25px;
  }
  
  .logo {
    width: 100px;
    height: 100px;
  }
}

@media (max-width: 480px) {
  .home-container {
    padding: 20px 15px;
  }
  
  .main-title {
    font-size: 2rem;
  }
  
  .subtitle {
    font-size: 0.9rem;
  }
  
  .feature-card {
    padding: 20px;
  }
  
  .card-title {
    font-size: 1.3rem;
  }
  
  .card-description {
    font-size: 0.9rem;
  }
  
  .logo {
    width: 80px;
    height: 80px;
  }
  
  .header-section {
    margin-bottom: 40px;
  }
}
</style>