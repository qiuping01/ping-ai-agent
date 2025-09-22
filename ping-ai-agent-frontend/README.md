# AI智能体应用平台前端

## 项目简介

这是一个基于Vue3开发的AI智能体应用平台，整合了三种不同类型的AI智能体服务，为用户提供全方位的智能交互体验。平台采用现代化的前端架构，通过Server-Sent Events（SSE）技术实现与后端的实时通信，确保流畅的聊天交互体验。

## 核心功能

### 🧙‍♂️ 易经大师 (YiJingMaster)
专业的易经占卜顾问，能够根据用户提供的信息进行卦象解读，提供生活指导和决策建议。支持数字起卦和解卦功能，帮助用户洞察未来趋势。

### 💑 恋爱大师 (LoveMaster)
智能情感顾问，专注于解答用户的恋爱问题和情感困惑。提供专业的情感分析、建议和策略，帮助用户处理各种恋爱关系问题。

### 🐍 OpManus 灵蛇智能 (SuperAgent)
全能型AI助手，能够解答各类专业问题，提供精准的建议和解决方案。具备处理复杂任务的能力，为用户提供高效的智能服务。

## 技术栈

- **前端框架**: Vue 3
- **路由管理**: Vue Router 4
- **HTTP请求**: Axios
- **实时通信**: Server-Sent Events (SSE)
- **构建工具**: Vite
- **文档生成**: marked
- **页面元数据**: @vueuse/head
- **开发语言**: JavaScript

## 项目结构

```
├── src/
│   ├── api/             # API接口封装
│   ├── components/      # 公共组件
│   │   ├── AiAvatarFallback.vue  # AI头像组件
│   │   ├── AppFooter.vue          # 页脚组件
│   │   ├── ChatRoom.vue           # 聊天界面组件
│   │   └── UserAvatar.vue         # 用户头像组件
│   ├── router/          # 路由配置
│   ├── views/           # 页面视图
│   │   ├── Home.vue           # 首页
│   │   ├── LoveMaster.vue     # 恋爱大师页面
│   │   ├── SuperAgent.vue     # 灵蛇智能页面
│   │   └── YiJingMaster.vue   # 易经大师页面
│   ├── App.vue          # 应用入口组件
│   ├── main.js          # 程序入口文件
│   └── style.css        # 全局样式
├── index.html           # HTML模板
├── package.json         # 项目配置
└── vite.config.js       # Vite配置
```

## 快速开始

### 环境要求

- Node.js >= 16.0.0
- npm >= 7.0.0

### 安装依赖

```bash
npm install
```

### 启动开发服务器

```bash
npm run dev
```

开发服务器启动后，可通过浏览器访问 [http://localhost:3000](http://localhost:3000) 查看应用。

### 构建生产版本

```bash
npm run build
```

构建后的文件将生成在 `dist` 目录中，可用于部署到生产环境。

### 预览生产版本

```bash
npm run preview
```

在部署前，可以使用此命令本地预览生产构建的效果。

## API接口说明

项目通过SSE技术与后端进行实时通信，主要依赖以下API接口：

- **易经大师**: `/api/ai/yiJing_app/chat/sse_emitter`
- **恋爱大师**: `/api/ai/love_app/chat/sse`
- **灵蛇智能**: `/api/ai/manus/chat`

### 开发环境

在开发环境下，后端服务默认配置为 `http://localhost:8123/api`。

### 生产环境

在生产环境下，API请求将使用相对路径 `/api`，适用于前后端部署在同一域名下的场景。

## 路由说明

| 路由路径 | 名称 | 描述 |
|---------|------|------|
| `/` | Home | 应用首页 |
| `/love-master` | LoveMaster | 恋爱大师页面 |
| `/super-agent` | SuperAgent | 灵蛇智能页面 |
| `/yi-jing-master` | YiJingMaster | 易经大师页面 |

## 开发注意事项

1. 确保后端服务已正常启动并可访问
2. 开发过程中如果遇到CORS问题，可在后端配置允许跨域访问
3. SSE连接可能会在网络不稳定时断开，应用已实现自动重连机制
4. 如需修改API基础URL，可在 `src/api/index.js` 文件中调整

## License

[MIT](https://opensource.org/licenses/MIT)

## 致谢

感谢所有为项目做出贡献的开发者！
