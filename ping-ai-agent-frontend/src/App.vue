<script setup>
import HelloWorld from './components/HelloWorld.vue'
</script>

<template>
  <router-view />
</template>

<style>
* {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

html, body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', 'Helvetica Neue', Helvetica, Arial, sans-serif;
  font-size: 16px;
  color: #1f2937;
  background: linear-gradient(135deg, #a8d8ff 0%, #c8e6ff 50%, #e8f4ff 100%);
  width: 100%;
  height: 100%;
  overflow-x: hidden;
  line-height: 1.6;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

#app {
  width: 100%;
  height: 100%;
}

a {
  text-decoration: none;
  color: inherit;
}

button {
  cursor: pointer;
  border: none;
  outline: none;
  font-family: inherit;
}

input, textarea {
  font-family: inherit;
  outline: none;
}

/* 响应式字体大小 */
@media (max-width: 768px) {
  html, body {
    font-size: 15px;
  }
}

@media (max-width: 480px) {
  html, body {
    font-size: 14px;
  }
}

/* 滚动条样式 */
::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

::-webkit-scrollbar-track {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 4px;
}

::-webkit-scrollbar-thumb {
  background: rgba(74, 144, 226, 0.3);
  border-radius: 4px;
  transition: background 0.2s;
}

::-webkit-scrollbar-thumb:hover {
  background: rgba(74, 144, 226, 0.5);
}

/* 禁用文本选择样式 */
::selection {
  background: transparent;
}

::-moz-selection {
  background: transparent;
}

/* 全局禁用选择 */
* {
  -webkit-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
  user-select: none;
  -webkit-touch-callout: none;
  -webkit-tap-highlight-color: transparent;
}

/* 禁用右键菜单 */
body {
  -webkit-touch-callout: none;
  -webkit-user-select: none;
  -khtml-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
  user-select: none;
}

/* 焦点样式 */
:focus-visible {
  outline: 2px solid #4a90e2;
  outline-offset: 2px;
}
</style>
