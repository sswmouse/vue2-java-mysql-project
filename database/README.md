# MySQL 数据库配置

## 安装MySQL

### macOS (使用Homebrew)
```bash
brew install mysql
brew services start mysql
```

### Windows
1. 下载MySQL Installer: https://dev.mysql.com/downloads/installer/
2. 安装并设置root密码为 `123456`

### Linux (Ubuntu/Debian)
```bash
sudo apt update
sudo apt install mysql-server
sudo systemctl start mysql
sudo mysql_secure_installation
```

## 初始化数据库

1. 登录MySQL
```bash
mysql -u root -p
```

2. 运行初始化脚本
```sql
SOURCE /path/to/vue2-java-mysql-project/database/init.sql;
```

或者直接复制脚本内容到MySQL命令行。

## 配置修改

根据你的MySQL设置，可能需要修改后端配置文件：

`backend/src/main/resources/application.properties`

```properties
# 如果你的MySQL端口不是3306
spring.datasource.url=jdbc:mysql://localhost:3306/vue2_java_mysql?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8

# 修改用户名和密码
spring.datasource.username=root
spring.datasource.password=your_password
```

## 测试连接

启动后端应用后，检查日志中是否显示数据库连接成功：

```
Hibernate: create table users ...
```

## 常见问题

### 连接被拒绝
- 检查MySQL服务是否运行：`mysqladmin ping`
- 检查用户名和密码是否正确
- 检查数据库名是否正确

### 时区问题
如果遇到时区错误，在连接URL中添加时区设置：
```
&serverTimezone=Asia/Shanghai
```

### 权限问题
确保用户有创建表的权限，或使用root用户。