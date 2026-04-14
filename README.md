# Vue2 + Java Spring Boot + MySQL 全栈项目

这是一个基于DNF（地下城与勇士）游戏风格的沉浸式管理系统，包含Vue2前端、Java Spring Boot后端和MySQL数据库。

## 项目结构

```
vue2-java-mysql-project/
├── frontend/              # Vue2前端项目
│   ├── src/
│   │   ├── components/    # 公共组件
│   │   │   ├── AnimatedRouter.vue      # 路由动画组件
│   │   │   ├── CharacterCard.vue       # 角色卡片组件(3D翻转)
│   │   │   ├── CharacterCardBack.vue   # 角色卡片背面
│   │   │   └── CharacterDialog.vue     # 角色创建/编辑对话框
│   │   ├── views/         # 页面视图
│   │   │   ├── Home.vue          # 首页
│   │   │   ├── Login.vue         # 登录页面
│   │   │   ├── Register.vue      # 注册页面
│   │   │   ├── Users.vue         # 用户管理
│   │   │   ├── Role.vue          # 角色管理
│   │   │   ├── Equipment.vue     # 装备管理
│   │   │   ├── Fashion.vue       # 时装管理
│   │   │   ├── Enhancement.vue   # 强化系统
│   │   │   ├── Enchantment.vue   # 附魔系统
│   │   │   ├── Badge.vue         # 徽章系统
│   │   │   ├── Title.vue         # 称号系统
│   │   │   ├── Pet.vue           # 宠物系统
│   │   │   └── Mist.vue          # 迷雾大陆
│   │   ├── layout/         # 布局组件
│   │   │   └── MainLayout.vue    # 主布局(侧边栏+顶部栏)
│   │   ├── router/        # 路由配置
│   │   ├── store/         # Vuex状态管理
│   │   ├── api/           # API接口
│   │   │   ├── index.js         # API统一导出
│   │   │   ├── user.js          # 用户API
│   │   │   ├── character.js     # 角色API
│   │   │   ├── product.js       # 产品API
│   │   │   └── order.js         # 订单API
│   │   ├── utils/         # 工具函数
│   │   │   ├── animations.js    # 动画工具
│   │   │   └── http.js          # HTTP请求封装
│   │   ├── assets/
│   │   │   └── styles/          # 样式文件
│   │   │       ├── variables.less  # LESS变量定义
│   │   │       ├── mixins.less      # LESS混合函数
│   │   │       ├── animations.less  # CSS动画定义
│   │   │       ├── base.less        # 基础样式
│   │   │       ├── index.less       # 样式入口
│   │   │       └── element-theme.less  # Element UI主题
│   │   ├── App.vue         # 根组件
│   │   └── main.js        # 入口文件
│   ├── tests/e2e/         # 端到端测试
│   ├── public/            # 静态资源
│   ├── dist/              # 构建输出
│   ├── package.json       # 依赖配置
│   ├── vue.config.js      # Vue配置
│   ├── jest.config.js    # Jest配置
│   └── jest-puppeteer.config.js  # Puppeteer配置
├── backend/               # Java Spring Boot后端
│   ├── src/main/java/com/example/demo/
│   │   ├── controller/    # 控制器层
│   │   │   ├── UserController.java
│   │   │   ├── CharacterController.java
│   │   │   ├── CharacterTypeController.java
│   │   │   ├── AuthController.java      # 认证控制器
│   │   │   └── FileUploadController.java
│   │   ├── service/       # 服务层
│   │   │   ├── UserService.java
│   │   │   ├── AuthService.java        # 认证服务
│   │   │   ├── CharacterService.java
│   │   │   ├── CharacterTypeService.java
│   │   │   └── impl/      # 服务实现
│   │   ├── repository/    # 数据访问层
│   │   │   ├── UserRepository.java
│   │   │   ├── CharacterRepository.java
│   │   │   └── CharacterTypeRepository.java
│   │   ├── model/         # 数据模型
│   │   │   ├── User.java
│   │   │   ├── Character.java
│   │   │   └── CharacterType.java
│   │   ├── dto/           # 数据传输对象
│   │   │   ├── UserDTO.java
│   │   │   ├── CharacterDTO.java
│   │   │   ├── CharacterTypeDTO.java
│   │   │   ├── LoginDTO.java           # 登录请求DTO
│   │   │   ├── RegisterDTO.java         # 注册请求DTO
│   │   │   ├── AuthResponse.java       # 认证响应DTO
│   │   │   └── RoleUpdateDTO.java      # 角色更新DTO
│   │   ├── config/        # 配置类
│   │   │   ├── WebConfig.java
│   │   │   ├── SecurityConfig.java      # Spring Security配置
│   │   │   └── JwtAuthFilter.java       # JWT认证过滤器
│   │   ├── utils/         # 工具类
│   │   │   └── JwtUtil.java            # JWT工具类
│   │   └── exception/     # 异常处理
│   │       └── GlobalExceptionHandler.java
│   ├── src/main/resources/
│   │   └── application.properties  # 应用配置
│   └── pom.xml            # Maven依赖
├── database/              # 数据库配置
│   ├── init.sql           # 数据库初始化脚本
│   └── README.md          # 数据库配置说明
└── README.md             # 项目说明
```

## 编码规范

### 标签属性换行规则
单属性标签写在一行，多属性标签需要换行处理，例如：
```html
<div class="main-layout">
    <aside
        class="sidebar"
        :class="{ collapsed: isCollapsed }"
    >
```

### 其他规范
1. Vue组件默认缩进4个空格
2. Class命名使用小写 + 连字符（kebab-case）
3. 元素id保持页面唯一，避免name和id相同
4. 标签使用小写
5. 方法变量驼峰命名，常量使用全大写+下划线
6. 减少使用var，多用let/const
7. CSS使用LESS嵌套，使用scoped隔离
8. 使用ESLint进行代码规范
9. 代码注释参考koroFileHeader格式
10. 避免使用行内样式，公共样式提取
11. 无多余变量
12. Props指定类型和默认值

### 工作流程
1. 任务拆分
2. 提交前测试
3. 更新内容同步修改md文档

### 沟通偏好
1. 使用中文沟通
2. 保持回答简洁
3. 安全操作自动确认（重大风险操作仍需询问）

---

## 功能特性

### 核心功能
- ✅ 用户管理（增删改查）
- ✅ 角色管理（CRUD + 拖拽排序 + 3D翻转卡片）
- ✅ 登录注册系统（JWT认证）
- ✅ 权限管理系统（admin/user角色）
- ✅ 装备管理系统
- ✅ 时装管理系统
- ✅ 强化系统
- ✅ 附魔系统
- ✅ 徽章系统
- ✅ 称号系统
- ✅ 宠物系统
- ✅ 迷雾大陆探索系统

### 技术特性
- ✅ Vue2 + Element UI前端界面
- ✅ Vue Router路由管理
- ✅ Vuex状态管理
- ✅ Axios HTTP客户端
- ✅ GSAP + ScrollTrigger 动画引擎
- ✅ vuedraggable 拖拽排序
- ✅ LESS CSS预处理器
- ✅ DNF霓虹暗色主题
- ✅ Java Spring Boot REST API
- ✅ Spring Data JPA数据访问
- ✅ Spring Security安全框架
- ✅ JWT Token认证
- ✅ BCrypt密码加密
- ✅ MySQL数据库集成
- ✅ 跨域支持（CORS）
- ✅ 全局异常处理
- ✅ 数据验证
- ✅ 文件上传功能
- ✅ 响应式前端设计
- ✅ 端到端测试（Puppeteer + Jest）

## 快速开始

### 环境要求

- Node.js 14+ (前端)
- Java 17+ (后端)
- MySQL 8.0+ (数据库)
- Maven 3.6+ (可选，用于构建Java项目)

### 1. 数据库设置

1. 安装MySQL（如果尚未安装）
2. 运行初始化脚本：
```bash
cd database
mysql -u root -p < init.sql
```

### 2. 启动后端服务

```bash
cd backend

# 如果有Maven
mvn spring-boot:run

# 或者直接运行（需要先编译）
# mvn clean package
# java -jar target/vue2-java-mysql-backend-1.0.0.jar
```

后端服务将在 http://localhost:3000 启动

### 3. 启动前端服务

```bash
cd frontend

# 安装依赖
npm install

# 启动开发服务器
npm run serve
```

前端服务将在 http://localhost:8080 启动

### 4. 访问应用

打开浏览器访问 http://localhost:8080

## API接口

### 用户管理

| 方法 | 端点 | 描述 |
|------|------|------|
| GET | /users | 获取所有用户 |
| GET | /users/{id} | 获取单个用户 |
| POST | /users | 创建用户 |
| PUT | /users/{id} | 更新用户 |
| DELETE | /users/{id} | 删除用户 |

### 认证系统

| 方法 | 端点 | 描述 | 权限 |
|------|------|------|------|
| POST | /auth/login | 用户登录 | 公开 |
| POST | /auth/register | 用户注册 | 公开 |
| GET | /auth/me | 获取当前用户信息 | 需认证 |
| PUT | /auth/users/{id}/role | 修改用户角色 | 仅管理员 |

**登录请求示例:**
```json
{
  "username": "admin",
  "password": "password123"
}
```

**登录响应示例:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIs...",
  "userId": 1,
  "username": "admin",
  "email": "admin@example.com",
  "role": "admin"
}
```

### 角色管理

| 方法 | 端点 | 描述 |
|------|------|------|
| GET | /characters/user/{userId} | 获取用户的所有角色 |
| GET | /characters/{id} | 获取单个角色 |
| POST | /characters | 创建角色 |
| PUT | /characters/{id} | 更新角色 |
| DELETE | /characters/{id} | 删除角色 |

### 角色类型

| 方法 | 端点 | 描述 |
|------|------|------|
| GET | /character-types | 获取所有角色类型 |
| GET | /character-types/{id} | 获取单个角色类型 |
| GET | /character-types/cascade | 获取级联角色类型 |
| POST | /character-types | 创建角色类型 |
| PUT | /character-types/{id} | 更新角色类型 |
| DELETE | /character-types/{id} | 删除角色类型 |

### 文件上传

| 方法 | 端点 | 描述 |
|------|------|------|
| POST | /upload | 上传文件 |

**请求示例 (创建用户):**
```json
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "password123"
}
```

**响应示例:**
```json
{
  "id": 1,
  "username": "john_doe",
  "email": "john@example.com"
}
```

### 角色管理

**请求示例 (创建角色):**
```json
{
  "userId": 1,
  "characterName": "暗夜使者",
  "level": 1,
  "avatarUrl": "",
  "characterTypeId": 1
}
```

**响应示例:**
```json
{
  "id": 1,
  "userId": 1,
  "characterName": "暗夜使者",
  "level": 1,
  "avatarUrl": "",
  "characterType": {
    "id": 1,
    "className": "暗夜使者",
    "gender": "女",
    "jobName": "刺客"
  }
}
```

## 项目配置

### 前端配置
- 端口: 8080（开发环境）
- API代理: `/api` -> `http://localhost:3000`
- 使用Element UI组件库
- LESS预处理器
- GSAP动画库
- vuedraggable拖拽库

### 后端配置
- 端口: 3000
- 数据库连接: `jdbc:mysql://localhost:3306/vue2_java_mysql`
- 默认用户名/密码: root/root

### 数据库配置
- 数据库名: `vue2_java_mysql`
- 字符集: `utf8mb4`
- 排序规则: `utf8mb4_unicode_ci`

### DNF主题样式系统
- 主题变量: `frontend/src/assets/styles/variables.less`
- 混合函数: `frontend/src/assets/styles/mixins.less`
- CSS动画: `frontend/src/assets/styles/animations.less`

## 开发说明

### 前端开发

1. 页面组件位于 `frontend/src/views/`
2. 公共组件位于 `frontend/src/components/`
3. 主布局位于 `frontend/src/layout/MainLayout.vue`
4. API接口定义在 `frontend/src/api/`
5. 路由配置在 `frontend/src/router/index.js`
6. 状态管理在 `frontend/src/store/index.js`
7. 样式系统位于 `frontend/src/assets/styles/`

### 后端开发

1. 控制器层: `backend/src/main/java/com/example/demo/controller/`
2. 服务层: `backend/src/main/java/com/example/demo/service/`
3. 数据访问层: `backend/src/main/java/com/example/demo/repository/`
4. 数据模型: `backend/src/main/java/com/example/demo/model/`
5. 数据传输对象: `backend/src/main/java/com/example/demo/dto/`
6. 配置文件: `backend/src/main/resources/application.properties`

## 常见问题

### 1. 前端无法连接后端
- 检查后端是否运行在 http://localhost:3000
- 检查 `vue.config.js` 中的代理配置
- 检查浏览器控制台错误信息
- 确认前端端口为 http://localhost:8080

### 2. 数据库连接失败
- 检查MySQL服务是否运行
- 修改 `application.properties` 中的数据库配置
- 检查数据库用户名和密码

### 3. 跨域问题
- 后端已配置CORS，允许 `http://localhost:8080`
- 如果需要其他域名访问，修改 `WebConfig.java`

### 4. 构建问题
- 前端: 确保Node.js版本兼容
- 后端: 确保Java 17+ 和 Maven正确安装

### 5. 动画不生效
- 检查是否正确引入GSAP库
- 确认浏览器控制台无JavaScript错误
- 检查CSS动画类是否正确应用

## 端到端测试

项目已配置基于Puppeteer和Jest的端到端测试框架，用于自动化UI测试。

### 测试框架
- **Puppeteer**: 浏览器自动化工具
- **Jest**: 测试运行器和断言库
- **jest-puppeteer**: Jest与Puppeteer集成

### 运行测试

```bash
cd frontend

# 运行所有E2E测试（无头模式）
npm run test:e2e

# 显示浏览器窗口（调试模式）
HEADLESS=false npm run test:e2e

# 只运行特定测试文件
npm run test:e2e -- --testPathPatterns=users.test.js
```

### 测试用例

1. **首页测试** (`tests/e2e/home.test.js`)
   - 页面正确加载
   - GSAP动画正常执行
   - 导航菜单正常工作
   - 无JavaScript错误

2. **用户管理页面测试** (`tests/e2e/users.test.js`)
   - 页面正确加载
   - GSAP入场动画正常
   - 表单输入正常工作
   - 表单验证工作
   - 用户表格显示数据
   - 操作按钮存在
   - 重置表单功能正常
   - 完整的用户增删改查流程

### 测试配置
- 配置文件: `jest.config.js`, `jest-puppeteer.config.js`
- 测试目录: `frontend/tests/e2e/`
- 全局设置: `frontend/tests/e2e/setup.js`
- 调试工具: `frontend/test-puppeteer.js`

### 编写新测试
在 `frontend/tests/e2e/` 目录下创建新的 `.test.js` 文件：
```javascript
describe('页面描述', () => {
  beforeAll(async () => {
    await page.goto('http://localhost:8080/your-route')
  })
  
  test('测试用例', async () => {
    await page.click('selector')
    expect(...).toBe(...)
  })
})
```

## 扩展建议

1. **完善游戏功能模块**
   - 装备强化逻辑实现
   - 附魔系统后端实现
   - 宠物养成系统
   - 迷雾大陆探索逻辑

2. **增强认证功能**
   - 邮箱验证
   - 找回密码
   - 登录历史记录
   - 会话管理

3. **性能优化**
   - 前端路由懒加载
   - 组件按需加载
   - 后端缓存策略
   - 数据库索引优化

4. **部署配置**
   - Docker容器化
   - Nginx反向代理
   - CI/CD流水线

5. **测试覆盖**
   - 更多E2E测试用例
   - 后端单元测试
   - API集成测试

## 许可证

MIT License