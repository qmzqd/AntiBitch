# AntiBitch 项目文档

## 项目概述

AntiBitch 是一个用于 Minecraft Paper 服务器的反作弊插件，旨在检测和防止玩家使用 Reach（攻击距离扩大）、Speed（加速）和 Fly（飞行）等作弊行为。

- **项目名称**: AntiBitch
- **版本**: 1.0-SNAPSHOT
- **主类**: `nb114514.antibitch.AntiBitch`
- **API 版本**: 1.20
- **Java 版本**: 1.8 (编译) / 17 (CI 环境)

## 项目结构

```
AntiBitch/
├── src/main/java/nb114514/antibitch/
│   ├── AntiBitch.java      # 主插件类，集成所有检测功能
│   ├── Reach.java          # Reach 检测类（独立版本）
│   └── speed.java          # Speed 检测类（独立版本）
├── src/main/resources/
│   └── paper-plugin.yml    # Paper 插件配置文件
├── .github/workflows/
│   └── maven.yml           # GitHub Actions CI/CD 配置
├── pom.xml                 # Maven 项目配置
├── README.md               # 项目说明
└── IFLOW.md                # 本文档
```

## 技术栈

- **语言**: Java 1.8
- **构建工具**: Maven
- **服务器 API**: Paper API 1.20.1-R0.1-SNAPSHOT
- **CI/CD**: GitHub Actions

## 功能特性

### 1. Reach 检测
检测玩家攻击距离是否超过允许范围。

- **最大攻击距离**: 3.0 方块
- **最大违规次数**: 5 次
- **违规处理**:
  - 前 4 次违规：广播警告消息
  - 第 5 次违规：踢出玩家并重置违规记录

### 2. Speed 检测
检测玩家移动速度是否异常。

- **最大允许速度**: 20 (单位：方块/秒)
- **违规处理**: 立即踢出玩家

### 3. Fly 检测
检测玩家在空中的异常移动行为。

- **最大空中移动次数**: 10 次
- **违规处理**: 踢出玩家并提示"此服务器未启用飞行！"

## 构建和部署

### 构建命令

```bash
# 编译项目
mvn compile

# 打包项目（生成 JAR 文件）
mvn package

# 清理构建产物
mvn clean

# 完整构建（清理 + 编译 + 打包）
mvn clean package

# 跳过测试打包
mvn package -DskipTests
```

### 构建产物

构建完成后，JAR 文件位于：
```
target/AntiBitch-1.0-SNAPSHOT.jar
```

### 部署

1. 将生成的 JAR 文件复制到 Paper 服务器的 `plugins` 目录
2. 重启服务器或使用 `/reload` 命令加载插件

## 依赖管理

### Maven 仓库

```xml
<repositories>
    <repository>
        <id>papermc-repo</id>
        <url>https://repo.papermc.io/repository/maven-public/</url>
    </repository>
    <repository>
        <id>sonatype</id>
        <url>https://oss.sonatype.org/content/groups/public/</url>
    </repository>
</repositories>
```

### 主要依赖

```xml
<dependency>
    <groupId>io.papermc.paper</groupId>
    <artifactId>paper-api</artifactId>
    <version>1.20.1-R0.1-SNAPSHOT</version>
    <scope>provided</scope>
</dependency>
```

## 开发实践

### 代码结构

- **事件监听**: 使用 Bukkit 事件系统实现反作弊检测
- **状态管理**: 使用 `HashMap` 追踪玩家违规记录
- **消息格式**: 使用 `§` 颜色代码和 `ChatColor.translateAlternateColorCodes()` 格式化消息

### 关键配置参数

在 `AntiBitch.java` 中可调整以下参数：

```java
private final int MAX_REACH_VIOLATIONS = 5;  // Reach 最大违规次数
private final double MAX_REACH = 3.0D;       // 最大攻击距离
```

### 代码风格

- 使用中文注释和消息
- 类名使用 PascalCase
- 变量名使用 camelCase
- 常量使用 UPPER_SNAKE_CASE

## CI/CD

项目使用 GitHub Actions 进行持续集成：

- **触发条件**: 推送到 main 分支或针对 main 分支的 Pull Request
- **运行环境**: Ubuntu Latest
- **JDK 版本**: 17 (Temurin 分发版)
- **构建命令**: `mvn -B package --file pom.xml`

## 已知问题

1. **代码重复**: `Reach.java` 和 `speed.java` 与 `AntiBitch.java` 中存在功能重复
2. **Java 版本不一致**: 项目使用 Java 1.8 编译，但 CI 环境使用 JDK 17
3. **消息内容**: 部分踢出消息包含不当语言

## 未来改进建议

1. **代码重构**:
   - 移除重复的 `Reach.java` 和 `speed.java` 文件
   - 将检测逻辑拆分为独立的检测器类
   - 实现配置文件支持，允许服务器管理员自定义检测参数

2. **功能增强**:
   - 添加更多反作弊检测（如 KillAura、AutoClicker 等）
   - 实现警告系统而非直接踢出
   - 添加日志记录功能
   - 支持管理员命令（如 `/antibitch reload`）

3. **代码质量**:
   - 统一 Java 版本配置
   - 添加单元测试
   - 改进消息内容和格式
   - 添加详细的代码注释

4. **文档完善**:
   - 添加详细的 README.md
   - 创建插件使用指南
   - 添加开发者文档

## 贡献指南

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 许可证

本项目未指定许可证。

## 联系方式

- **GitHub**: https://github.com/qmzqd/AntiBitch

---

*最后更新时间: 2026年1月11日*