import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('../views/Home.vue'),
    meta: {
      title: 'Serpent AI - 灵蛇智启，洞察无界',
      description: 'Serpent AI 提供恋爱大师、OpManus 灵蛇智能和易经大师服务，满足您的各种AI对话需求'
    }
  },
  {
    path: '/love-master',
    name: 'LoveMaster',
    component: () => import('../views/LoveMaster.vue'),
    meta: {
      title: '恋爱大师 - Serpent AI',
      description: '恋爱大师是 Serpent AI 的专业情感顾问，帮你解答各种恋爱问题，提供情感建议'
    }
  },
  {
    path: '/super-agent',
    name: 'SuperAgent',
    component: () => import('../views/SuperAgent.vue'),
    meta: {
      title: 'OpManus 灵蛇智能 - Serpent AI',
      description: 'OpManus 灵蛇智能是 Serpent AI 的全能助手，能解答各类专业问题，提供精准建议和解决方案'
    }
  },
  {
    path: '/yi-jing-master',
    name: 'YiJingMaster',
    component: () => import('../views/YiJingMaster.vue'),
    meta: {
      title: '易经大师 - Serpent AI',
      description: '易经大师是 Serpent AI 的易经占卜顾问，帮您解读易经卦象，提供生活指导和决策建议'
    }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 全局导航守卫，设置文档标题
router.beforeEach((to, from, next) => {
  // 设置页面标题
  if (to.meta.title) {
    document.title = to.meta.title
  }
  next()
})

export default router