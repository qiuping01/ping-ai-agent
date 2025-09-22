# Ping AI Agent
## 项目简介
Ping AI Agent 是一个基于人工智能技术的智能助手系统，集成了多种大语言模型能力，提供了丰富的工具链支持，可实现智能问答、知识检索、内容生成等功能。系统采用前后端分离架构，后端基于Java Spring Boot开发，前端采用Vue 3实现。

## 项目演示
### 首页
![](https://cdn.nlark.com/yuque/0/2025/png/57681281/1758539884484-11f03b25-4952-4663-85b7-fe129b14ab78.png)

### 恋爱大师
![](https://cdn.nlark.com/yuque/0/2025/gif/57681281/1758540166258-700d8e0e-93a7-4e54-8f12-46498149fbb9.gif)

### 灵蛇智能
![](https://cdn.nlark.com/yuque/0/2025/gif/57681281/1758540294168-e4948a3f-f286-4c26-a5a3-afc02b631024.gif)

### 易经大师
![](https://cdn.nlark.com/yuque/0/2025/gif/57681281/1758540870035-739d3d77-3269-4f52-8545-72ce8080cfb1.gif)

## 项目架构
### 后端架构
+ **基础框架**：Spring Boot 3.5.4 + Java 21
+ **AI能力集成**：支持多种大模型接入（阿里云灵积、Ollama、火山引擎等）
+ **向量存储**：集成PGVector实现高效的向量检索
+ **工具链**：提供文件操作、邮件发送、PDF/PPT生成、Web搜索等丰富工具
+ **RAG增强**：实现检索增强生成，提升模型回答准确性

### 前端架构
+ **框架**：Vue 3 + Vite
+ **路由**：Vue Router
+ **HTTP客户端**：Axios
+ **Markdown解析**：marked

## 核心功能模块
### 1. 智能Agent
+ **基础Agent**：提供通用的AI交互能力
+ **ReActAgent**：实现思考-行动-观察的智能决策流程
+ **ToolCallAgent**：支持动态调用外部工具扩展能力

### 2. 知识检索与增强 (RAG)
+ 多模态文档解析（Markdown等）
+ 向量数据库存储与检索
+ 查询优化与扩展
+ 上下文感知的智能问答

### 3. 特色应用
+ **爱情大师**：基于特定知识库的情感咨询助手
+ **易经大师**：提供易经相关的知识问答和解析

### 4. 工具链系统
+ **文件操作工具**：支持本地和腾讯云COS文件操作
+ **内容生成工具**：PDF生成、PPT生成
+ **Web工具**：网页抓取、网络搜索
+ **系统工具**：终端操作、邮件发送

## 技术栈
### 后端技术
+ **核心框架**：Spring Boot 3.5.4
+ **AI框架**：Spring AI、LangChain4j
+ **向量数据库**：PostgreSQL + pgvector
+ **云服务集成**：腾讯云COS、阿里云灵积、火山引擎
+ **工具库**：Hutool、Apache POI、iTextPDF、Jsoup

### 前端技术
+ **框架**：Vue 3、Vue Router
+ **构建工具**：Vite
+ **UI组件**：自定义组件
+ **HTTP客户端**：Axios

## 项目结构
### 后端结构
```plain
src/main/java/com/ping/aiagent/
├── PingAiAgentApplication.java        # 应用入口
├── agent/                             # Agent相关实现
│   ├── BaseAgent.java                 # 基础Agent实现
│   ├── ReActAgent.java                # ReAct模式Agent
│   ├── ToolCallAgent.java             # 工具调用Agent
│   └── model/                         # Agent相关模型
├── app/                               # 特色应用实现
│   ├── LoveApp.java                   # 爱情大师应用
│   └── YiJingApp.java                 # 易经大师应用
├── rag/                               # RAG相关功能
│   ├── LoveAppDocumentLoader.java     # 文档加载器
│   └── PgVectorVectorStoreConfig.java # 向量存储配置
├── tools/                             # 工具链实现
│   ├── FileOperationToolForCos.java   # COS文件操作
│   ├── PDFGenerationTool.java         # PDF生成工具
│   ├── WebSearchTool.java             # 网络搜索工具
│   └── EmailTool.java                 # 邮件发送工具
├── controller/                        # REST控制器
│   ├── AiController.java              # AI相关接口
│   └── HealthController.java          # 健康检查接口
└── config/                            # 配置类
    ├── CorsConfig.java                # CORS配置
    └── CosConfig.java                 # COS配置
```

### 前端结构
```plain
ping-ai-agent-frontend/src/
├── App.vue                            # 根组件
├── main.js                            # 应用入口
├── api/index.js                       # API接口定义
├── components/                        # 通用组件
│   ├── ChatRoom.vue                   # 聊天室组件
│   └── UserAvatar.vue                 # 用户头像组件
├── views/                             # 页面视图
│   ├── Home.vue                       # 首页
│   ├── LoveMaster.vue                 # 爱情大师页面
│   ├── SuperAgent.vue                 # 超级助手页面
│   └── YiJingMaster.vue               # 易经大师页面
└── router/index.js                    # 路由配置
```

## 环境要求
### 后端环境
+ JDK 21 或更高版本
+ Maven 3.8+ 
+ PostgreSQL 14+ (带pgvector扩展)
+ 可选：腾讯云COS账号、阿里云灵积API密钥、火山引擎API密钥

### 前端环境
+ Node.js 16+ 
+ npm 7+ 或 yarn 1.22+

## 快速开始
### 后端启动
1. 确保已安装JDK 21和Maven
2. 配置数据库连接（application.properties或application.yml）
3. 安装pgvector扩展
4. 执行以下命令启动后端服务

```bash
mvn clean install
mvn spring-boot:run
```

### 前端启动
1. 进入前端目录

```bash
cd ping-ai-agent-frontend
```

2. 安装依赖

```bash
npm install
```

3. 启动开发服务器

```bash
npm run dev
```

4. 构建生产版本

```bash
npm run build
```

## 配置说明
### 主要配置文件
+ `application.properties` / `application.yml`：应用主配置文件
+ 配置项包括：
    - 数据库连接信息
    - AI模型API密钥
    - COS存储配置
    - 邮件服务配置

## API接口说明
### AI相关接口
+ **POST /api/ai/chat**：与AI助手进行对话
+ **POST /api/ai/invoke**：调用特定AI模型
+ **POST /api/ai/tool-call**：使用工具增强的AI调用

### 文档处理接口
+ **POST /api/ai/document/parse**：解析文档内容
+ **POST /api/ai/document/search**：搜索文档内容

## 特色功能使用指南
### 爱情大师
1. 访问 `/love-master` 页面
2. 输入您的情感问题
3. 系统将基于专业情感知识库为您提供分析和建议

### 易经大师
1. 访问 `/yijing-master` 页面
2. 提出您想咨询的问题
3. 系统将结合易经知识为您提供解读

### 超级助手
1. 访问 `/super-agent` 页面
2. 可以使用所有集成的工具功能
3. 支持文件上传、网页搜索、文档生成等高级功能

## 部署说明
### 后端部署
+ 构建可执行JAR包

```bash
mvn clean package -DskipTests
```

+ 使用Docker容器部署（可选）

### 前端部署
+ 构建静态资源

```bash
npm run build
```

+ 部署到Nginx或其他Web服务器
+ 项目中已包含Dockerfile和nginx.conf配置，可直接用于容器化部署

## 开发建议
1. **扩展新工具**：在`tools`包下创建新的工具类，并实现相应接口
2. **添加新应用**：参考`app`包中的现有应用实现新的特色功能
3. **自定义Agent**：继承`BaseAgent`类，实现自定义的Agent逻辑

## License
[MIT](https://opensource.org/licenses/MIT)

## 免责声明
本项目仅供学习和研究使用，所有输出内容仅供参考，不构成任何专业建议。

