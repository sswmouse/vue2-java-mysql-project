# Vue2 + Java Spring Boot + MySQL 全栈项目

这是一个完整的全栈项目示例，包含Vue2前端、Java Spring Boot后端和MySQL数据库。

## 项目结构

```
vue2-java-mysql-project/
├── frontend/              # Vue2前端项目
│   ├── src/
│   │   ├── components/    # 公共组件
│   │   ├── views/         # 页面视图
│   │   ├── router/        # 路由配置
│   │   ├── store/         # Vuex状态管理
│   │   ├── api/           # API接口
│   │   └── utils/         # 工具函数
│   ├── public/            # 静态资源
│   ├── package.json       # 依赖配置
│   └── vue.config.js      # Vue配置
├── backend/               # Java Spring Boot后端
│   ├── src/main/java/com/example/demo/
│   │   ├── controller/    # 控制器层
│   │   ├── service/       # 服务层
│   │   ├── repository/    # 数据访问层
│   │   ├── model/         # 数据模型
│   │   ├── dto/           # 数据传输对象
│   │   ├── config/        # 配置类
│   │   └── exception/     # 异常处理
│   ├── src/main/resources/
│   │   └── application.properties  # 应用配置
│   └── pom.xml            # Maven依赖
├── database/              # 数据库配置
│   ├── init.sql           # 数据库初始化脚本
│   └── README.md          # 数据库配置说明
└── README.md             # 项目说明
```

## 功能特性

- ✅ 用户管理（增删改查）
- ✅ Vue2 + Element UI前端界面
- ✅ Vue Router路由管理
- ✅ Vuex状态管理
- ✅ Axios HTTP客户端
- ✅ Java Spring Boot REST API
- ✅ Spring Data JPA数据访问
- ✅ MySQL数据库集成
- ✅ 跨域支持（CORS）
- ✅ 全局异常处理
- ✅ 数据验证
- ✅ 响应式前端设计

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

前端服务将在 http://localhost:8099 启动

### 4. 访问应用

打开浏览器访问 http://localhost:8099

## API接口

### 用户管理

| 方法 | 端点 | 描述 |
|------|------|------|
| GET | /users | 获取所有用户 |
| GET | /users/{id} | 获取单个用户 |
| POST | /users | 创建用户 |
| PUT | /users/{id} | 更新用户 |
| DELETE | /users/{id} | 删除用户 |

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

## 项目配置

### 前端配置
- 端口: 8099
- API代理: `/api` -> `http://localhost:3000`
- 使用Element UI组件库

### 后端配置
- 端口: 3000
- 数据库连接: `jdbc:mysql://localhost:3306/vue2_java_mysql`
- 默认用户名/密码: root/root

### 数据库配置
- 数据库名: `vue2_java_mysql`
- 字符集: `utf8mb4`
- 排序规则: `utf8mb4_unicode_ci`

## 开发说明

### 前端开发

1. 页面组件位于 `frontend/src/views/`
2. 公共组件位于 `frontend/src/components/`
3. API接口定义在 `frontend/src/api/index.js`
4. 路由配置在 `frontend/src/router/index.js`
5. 状态管理在 `frontend/src/store/index.js`

### 后端开发

1. 控制器层: `backend/src/main/java/com/example/demo/controller/`
2. 服务层: `backend/src/main/java/com/example/demo/service/`
3. 数据访问层: `backend/src/main/java/com/example/demo/repository/`
4. 数据模型: `backend/src/main/java/com/example/demo/model/`
5. 配置文件: `backend/src/main/resources/application.properties`

## 常见问题

### 1. 前端无法连接后端
- 检查后端是否运行在 http://localhost:8081
- 检查 `vue.config.js` 中的代理配置
- 检查浏览器控制台错误信息

### 2. 数据库连接失败
- 检查MySQL服务是否运行
- 修改 `application.properties` 中的数据库配置
- 检查数据库用户名和密码

### 3. 跨域问题
- 后端已配置CORS，允许 `http://localhost:8099`
- 如果需要其他域名访问，修改 `WebConfig.java`

### 4. 构建问题
- 前端: 确保Node.js版本兼容
- 后端: 确保Java 17+ 和 Maven正确安装

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
npm run test:e2e -- --testPathPattern=users
```

### 测试用例

1. **首页测试** (`tests/e2e/home.test.js`)
   - 页面正确加载
   - 显示主要内容
   - 导航菜单正常工作
   - 无JavaScript错误

2. **用户管理页面测试** (`tests/e2e/users.test.js`)
   - 页面正确加载
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

### 编写新测试
在 `frontend/tests/e2e/` 目录下创建新的 `.test.js` 文件：
```javascript
describe('页面描述', () => {
  beforeAll(async () => {
    await page.goto('http://localhost:8099/your-route')
  })
  
  test('测试用例', async () => {
    await page.click('selector')
    expect(...).toBe(...)
  })
})
```

## 扩展建议

1. **添加认证功能**
   - JWT令牌认证
   - 登录/注册页面
   - 权限控制

2. **添加更多功能模块**
   - 产品管理
   - 订单管理
   - 文件上传

3. **性能优化**
   - 前端路由懒加载
   - 后端缓存策略
   - 数据库索引优化

4. **部署配置**
   - Docker容器化
   - Nginx反向代理
   - CI/CD流水线

## 许可证

MIT License