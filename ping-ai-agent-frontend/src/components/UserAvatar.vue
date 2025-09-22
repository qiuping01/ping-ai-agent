<template>
  <div class="user-avatar-container">
    <div class="user-avatar-outer">
      <div class="user-avatar-inner">
        <div class="user-icon">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" class="user-svg">
            <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            <circle cx="12" cy="7" r="4" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
        </div>
      </div>
      <div class="user-avatar-glow"></div>
    </div>
  </div>
</template>

<script setup>
// 用户头像组件
</script>

<style scoped>
.user-avatar-container {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}

.user-avatar-outer {
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 50%, #f093fb 100%);
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
  transition: all 0.3s cubic-bezier(0.175, 0.885, 0.32, 1.275);
}

.user-avatar-outer::before {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: linear-gradient(45deg, transparent, rgba(255, 255, 255, 0.2), transparent);
  transform: rotate(-45deg);
  animation: userShimmer 4s ease-in-out infinite;
}

@keyframes userShimmer {
  0%, 100% { transform: translateX(-100%) translateY(-100%) rotate(-45deg); }
  50% { transform: translateX(100%) translateY(100%) rotate(-45deg); }
}

.user-avatar-inner {
  position: relative;
  z-index: 2;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.user-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}

.user-svg {
  color: rgba(255, 255, 255, 0.95);
  filter: drop-shadow(0 1px 3px rgba(102, 126, 234, 0.3));
  animation: userPulse 3s ease-in-out infinite;
}

@keyframes userPulse {
  0%, 100% { 
    transform: scale(1);
    filter: drop-shadow(0 1px 3px rgba(102, 126, 234, 0.3));
  }
  50% { 
    transform: scale(1.05);
    filter: drop-shadow(0 2px 6px rgba(240, 147, 251, 0.4));
  }
}

.user-avatar-glow {
  position: absolute;
  top: -2px;
  left: -2px;
  right: -2px;
  bottom: -2px;
  border-radius: 50%;
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.3), rgba(240, 147, 251, 0.3));
  opacity: 0;
  transition: opacity 0.3s ease;
  z-index: -1;
  animation: userGlow 4s ease-in-out infinite;
}

@keyframes userGlow {
  0%, 100% { 
    opacity: 0;
    transform: scale(1);
  }
  50% { 
    opacity: 0.6;
    transform: scale(1.1);
  }
}

/* 悬停效果 */
.user-avatar-container:hover .user-avatar-outer {
  transform: scale(1.08);
  box-shadow: 
    0 6px 24px rgba(102, 126, 234, 0.3),
    0 3px 12px rgba(240, 147, 251, 0.2),
    inset 0 1px 2px rgba(255, 255, 255, 0.4);
}

.user-avatar-container:hover .user-avatar-glow {
  opacity: 0.8;
  transform: scale(1.15);
}

.user-avatar-container:hover .user-svg {
  animation-duration: 1.5s;
  transform: scale(1.1);
}

/* 响应式调整 */
@media (max-width: 768px) {
  .user-svg {
    width: 18px;
    height: 18px;
  }
}

@media (max-width: 480px) {
  .user-svg {
    width: 16px;
    height: 16px;
  }
}
</style>