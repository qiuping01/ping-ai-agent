<template>
  <div class="ai-avatar-fallback" :class="type">
    <!-- 恋爱大师头像 -->
    <div v-if="type === 'love'" class="love-avatar">
      <div class="heart-avatar-container">
        <div class="heart-avatar-outer">
          <div class="heart-avatar-inner">💝</div>
        </div>
      </div>
    </div>
    
    <!-- 灵蛇智能头像 -->
    <div v-else-if="type === 'super'" class="super-avatar">
      <div class="brain-avatar-container">
        <div class="brain-avatar-outer">
          <div class="brain-avatar-inner">
            <div class="neural-network-avatar">
              <div class="neural-node-avatar"></div>
              <div class="neural-node-avatar"></div>
              <div class="neural-node-avatar"></div>
              <div class="neural-node-avatar"></div>
              <div class="neural-connection-avatar connection-1-avatar"></div>
              <div class="neural-connection-avatar connection-2-avatar"></div>
              <div class="neural-connection-avatar connection-3-avatar"></div>
              <div class="neural-connection-avatar connection-4-avatar"></div>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 易经大师头像 -->
    <div v-else-if="type === 'yijing'" class="yijing-avatar">
      <div class="yin-yang-avatar-container">
        <div class="yin-yang-avatar-outer">
          <div class="yin-yang-avatar-inner">
            <div class="yin-dot-avatar"></div>
            <div class="yang-dot-avatar"></div>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 默认头像 -->
    <div v-else class="default-avatar">
      <div class="default-avatar-container">
        <div class="default-avatar-outer">
          <div class="default-avatar-inner">🤖</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  type: {
    type: String,
    default: 'default'
  }
})
</script>

<style scoped>
.ai-avatar-fallback {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  position: relative;
  overflow: hidden;
}

/* 恋爱大师头像样式 */
.love-avatar {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.heart-avatar-container {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}

.heart-avatar-outer {
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #ff6b9d 0%, #ff8fab 50%, #ffb3d1 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 2px solid rgba(255, 255, 255, 0.8);
  box-shadow: 
    0 4px 16px rgba(255, 107, 157, 0.25),
    0 2px 8px rgba(255, 107, 157, 0.15),
    inset 0 1px 2px rgba(255, 255, 255, 0.3);
  position: relative;
  overflow: hidden;
}

.heart-avatar-outer::before {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: linear-gradient(45deg, transparent, rgba(255, 255, 255, 0.15), transparent);
  transform: rotate(-45deg);
  animation: heartShimmerAvatar 3s ease-in-out infinite;
}

@keyframes heartShimmerAvatar {
  0%, 100% { transform: translateX(-100%) translateY(-100%) rotate(-45deg); }
  50% { transform: translateX(100%) translateY(100%) rotate(-45deg); }
}

.heart-avatar-inner {
  position: relative;
  z-index: 2;
  font-size: 16px;
  filter: drop-shadow(0 1px 3px rgba(255, 107, 157, 0.3));
  animation: heartbeatAvatar 2.5s ease-in-out infinite;
}

@keyframes heartbeatAvatar {
  0%, 100% { transform: scale(1); }
  50% { transform: scale(1.1); }
}

/* 灵蛇智能头像样式 */
.super-avatar {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.brain-avatar-container {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}

.brain-avatar-outer {
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 50%, #667eea 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 2px solid rgba(255, 255, 255, 0.9);
  box-shadow: 
    0 4px 16px rgba(102, 126, 234, 0.25),
    0 2px 8px rgba(118, 75, 162, 0.15),
    inset 0 1px 2px rgba(255, 255, 255, 0.3);
  position: relative;
  overflow: hidden;
}

.brain-avatar-outer::before {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: linear-gradient(45deg, transparent, rgba(255, 255, 255, 0.15), transparent);
  transform: rotate(-45deg);
  animation: brainShimmerAvatar 3.5s ease-in-out infinite;
}

@keyframes brainShimmerAvatar {
  0%, 100% { transform: translateX(-100%) translateY(-100%) rotate(-45deg); }
  50% { transform: translateX(100%) translateY(100%) rotate(-45deg); }
}

.brain-avatar-inner {
  position: relative;
  z-index: 2;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.neural-network-avatar {
  position: relative;
  width: 20px;
  height: 20px;
}

.neural-node-avatar {
  position: absolute;
  width: 2.5px;
  height: 2.5px;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 50%;
  box-shadow: 0 0 3px rgba(255, 255, 255, 0.6);
}

.neural-node-avatar:nth-child(1) {
  top: 3px;
  left: 8.5px;
  animation: pulseAvatar1 2s ease-in-out infinite;
}

.neural-node-avatar:nth-child(2) {
  top: 8.5px;
  left: 5px;
  animation: pulseAvatar2 2s ease-in-out infinite 0.2s;
}

.neural-node-avatar:nth-child(3) {
  top: 8.5px;
  left: 12px;
  animation: pulseAvatar3 2s ease-in-out infinite 0.4s;
}

.neural-node-avatar:nth-child(4) {
  top: 14px;
  left: 8.5px;
  animation: pulseAvatar1 2s ease-in-out infinite 0.6s;
}

.neural-connection-avatar {
  position: absolute;
  background: rgba(255, 255, 255, 0.4);
  border-radius: 0.5px;
}

.connection-1-avatar {
  width: 5px;
  height: 0.8px;
  top: 6.5px;
  left: 6px;
  transform: rotate(45deg);
  animation: connectionPulseAvatar 2s ease-in-out infinite;
}

.connection-2-avatar {
  width: 5px;
  height: 0.8px;
  top: 6.5px;
  left: 10px;
  transform: rotate(-45deg);
  animation: connectionPulseAvatar 2s ease-in-out infinite 0.3s;
}

.connection-3-avatar {
  width: 5px;
  height: 0.8px;
  top: 11px;
  left: 6px;
  transform: rotate(-45deg);
  animation: connectionPulseAvatar 2s ease-in-out infinite 0.6s;
}

.connection-4-avatar {
  width: 5px;
  height: 0.8px;
  top: 11px;
  left: 10px;
  transform: rotate(45deg);
  animation: connectionPulseAvatar 2s ease-in-out infinite 0.9s;
}

@keyframes pulseAvatar1 {
  0%, 100% { 
    transform: scale(1);
    box-shadow: 0 0 3px rgba(255, 255, 255, 0.6);
  }
  50% { 
    transform: scale(1.3);
    box-shadow: 0 0 6px rgba(102, 126, 234, 0.8);
  }
}

@keyframes pulseAvatar2 {
  0%, 100% { 
    transform: scale(1);
    box-shadow: 0 0 3px rgba(255, 255, 255, 0.6);
  }
  50% { 
    transform: scale(1.3);
    box-shadow: 0 0 6px rgba(118, 75, 162, 0.8);
  }
}

@keyframes pulseAvatar3 {
  0%, 100% { 
    transform: scale(1);
    box-shadow: 0 0 3px rgba(255, 255, 255, 0.6);
  }
  50% { 
    transform: scale(1.3);
    box-shadow: 0 0 6px rgba(102, 126, 234, 0.8);
  }
}

@keyframes connectionPulseAvatar {
  0%, 100% { 
    opacity: 0.4;
    box-shadow: none;
  }
  50% { 
    opacity: 1;
    box-shadow: 0 0 2px rgba(255, 255, 255, 0.8);
  }
}

/* 易经大师头像样式 */
.yijing-avatar {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.yin-yang-avatar-container {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}

.yin-yang-avatar-outer {
  width: 100%;
  height: 100%;
  background: linear-gradient(to bottom, #000 50%, #fff 50%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 2px solid rgba(255, 255, 255, 0.9);
  box-shadow: 
    0 4px 16px rgba(212, 175, 55, 0.25),
    0 2px 8px rgba(240, 230, 140, 0.15),
    inset 0 1px 2px rgba(255, 255, 255, 0.3);
  position: relative;
  overflow: hidden;
}

.yin-yang-avatar-outer::before {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: linear-gradient(45deg, transparent, rgba(255, 255, 255, 0.15), transparent);
  transform: rotate(-45deg);
  animation: yinYangShimmerAvatar 3s ease-in-out infinite;
}

@keyframes yinYangShimmerAvatar {
  0%, 100% { transform: translateX(-100%) translateY(-100%) rotate(-45deg); }
  50% { transform: translateX(100%) translateY(100%) rotate(-45deg); }
}

.yin-yang-avatar-inner {
  position: relative;
  z-index: 2;
  width: 100%;
  height: 100%;
}

.yin-dot-avatar {
  position: absolute;
  width: 8px;
  height: 8px;
  top: 14px;
  left: 14px;
  background: #fff;
  border-radius: 50%;
  border: 1px solid #000;
  box-shadow: 0 0 4px rgba(255, 255, 255, 0.6);
  animation: dotPulseAvatar 3s ease-in-out infinite;
}

.yang-dot-avatar {
  position: absolute;
  width: 8px;
  height: 8px;
  top: 14px;
  left: 14px;
  background: #000;
  border-radius: 50%;
  border: 1px solid #fff;
  box-shadow: 0 0 4px rgba(0, 0, 0, 0.6);
  animation: dotPulseAvatar 3s ease-in-out infinite 1.5s;
}

@keyframes dotPulseAvatar {
  0%, 100% { 
    transform: scale(1);
  }
  50% { 
    transform: scale(1.2);
  }
}

/* 默认头像样式 */
.default-avatar {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.default-avatar-container {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}

.default-avatar-outer {
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #a8a8a8 0%, #d0d0d0 50%, #a8a8a8 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 2px solid rgba(255, 255, 255, 0.9);
  box-shadow: 
    0 4px 16px rgba(168, 168, 168, 0.25),
    0 2px 8px rgba(208, 208, 208, 0.15),
    inset 0 1px 2px rgba(255, 255, 255, 0.3);
  position: relative;
  overflow: hidden;
}

.default-avatar-outer::before {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: linear-gradient(45deg, transparent, rgba(255, 255, 255, 0.15), transparent);
  transform: rotate(-45deg);
  animation: defaultShimmerAvatar 3.5s ease-in-out infinite;
}

@keyframes defaultShimmerAvatar {
  0%, 100% { transform: translateX(-100%) translateY(-100%) rotate(-45deg); }
  50% { transform: translateX(100%) translateY(100%) rotate(-45deg); }
}

.default-avatar-inner {
  position: relative;
  z-index: 2;
  font-size: 14px;
  filter: drop-shadow(0 1px 2px rgba(168, 168, 168, 0.3));
}

/* 悬停效果 */
.ai-avatar-fallback:hover .heart-avatar-outer {
  transform: scale(1.05);
  box-shadow: 
    0 6px 24px rgba(255, 107, 157, 0.3),
    0 3px 12px rgba(255, 107, 157, 0.2),
    inset 0 1px 2px rgba(255, 255, 255, 0.4);
}

.ai-avatar-fallback:hover .brain-avatar-outer {
  transform: scale(1.05);
  box-shadow: 
    0 6px 24px rgba(102, 126, 234, 0.3),
    0 3px 12px rgba(118, 75, 162, 0.2),
    inset 0 1px 2px rgba(255, 255, 255, 0.4);
}

.ai-avatar-fallback:hover .yin-yang-avatar-outer {
  transform: scale(1.05) rotate(45deg);
  box-shadow: 
    0 6px 24px rgba(212, 175, 55, 0.3),
    0 3px 12px rgba(240, 230, 140, 0.2),
    inset 0 1px 2px rgba(255, 255, 255, 0.4);
}

.ai-avatar-fallback:hover .default-avatar-outer {
  transform: scale(1.05);
  box-shadow: 
    0 6px 24px rgba(168, 168, 168, 0.3),
    0 3px 12px rgba(208, 208, 208, 0.2),
    inset 0 1px 2px rgba(255, 255, 255, 0.4);
}

.ai-avatar-fallback:hover .neural-node-avatar {
  animation-duration: 1s !important;
}

.ai-avatar-fallback:hover .neural-connection-avatar {
  animation-duration: 1s !important;
}

.ai-avatar-fallback:hover .yin-dot-avatar,
.ai-avatar-fallback:hover .yang-dot-avatar {
  animation-duration: 2s !important;
}
</style>