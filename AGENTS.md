# AGENTS.md

Compact guidance for OpenCode sessions working in this repo.

## Project

Minecraft Paper 26.2 反作弊插件，Java 25，Maven 构建。单文件实现：所有 100 个检测器和命令处理都在 `src/main/java/nb114514/antibitch/AntiBitch.java`（约 3300 行）里。没有分包、没有独立的 Detector 类——不要去找不存在的结构。

## Build & verify

- 构建 26.2（默认）：`mvn -B package --file pom.xml` → `target/AntiBitch-1.0-SNAPSHOT-26.2.jar`
- 构建 1.21.11：`mvn -B package -P 1.21.11 --file pom.xml` → `target/AntiBitch-1.0-SNAPSHOT-1.21.11.jar`
- 仅编译：`mvn clean compile`（默认 26.2）或 `mvn clean compile -P 1.21.11`
- 测试：`mvn test`——**仓库无测试**，CI 里也是 `continue-on-error: true`，等同于 no-op。不要假设有测试框架。
- 没有 lint / formatter / typecheck 命令；Java 编译即类型检查。

## 版本与 Minecraft 适配（双 jar profile）

`pom.xml` 用两个 Maven profile 产出兼容不同 MC/Java 版本的 jar，同一份源码无需改动：

| Profile | Minecraft | Java | Paper API | 产物 jar |
|---|---|---|---|---|
| `26.2`（默认） | 26.2 | 25 | `26.2.build.24-alpha` | `AntiBitch-*-26.2.jar` |
| `1.21.11` | 1.21.11 | 21 | `1.21.11-R0.1-SNAPSHOT` | `AntiBitch-*-1.21.11.jar` |

每个 profile 在 `<properties>` 里设置四个属性：`java.version`、`paperapi.version`、`mc.api-version`、`build.classifier`。编译用 `maven-compiler-plugin` 的 `<release>${java.version}</release>`（`--release` 交叉编译，JDK 25 即可构建两个 profile，无需切换 JDK）。

`src/main/resources/paper-plugin.yml` 的 `api-version` 用占位符 `'${mc.api-version}'`，靠 Maven resource filtering（`<filtering>true</filtering>`）按 profile 替换。`version:` 仍是 `'${project.version}'`。**不要**手改 `paper-plugin.yml` 里的版本字段。

**新增/升级 MC 版本时**：复制一个现有 profile 块，改 `<id>` 与四个属性即可。查最新可用 Paper API 版本：`curl -s https://repo.papermc.io/repository/maven-public/io/papermc/paper/paper-api/maven-metadata.xml` 看 `<latest>`/`<release>`。

**版本字符串格式**：1.21.x 时代用 `<MC>-R0.1-SNAPSHOT`（如 `1.21.11-R0.1-SNAPSHOT`）；26.x 起改为 `<MC>.build.<N>-<alpha|beta|stable>`（如 `26.2.build.24-alpha`），且 26.x 只有 alpha/beta 构建时也会被标为 `release`。

**兼容性前提**：源码只用稳定的通用 Bukkit 事件与 API（`PlayerMoveEvent`、`Material`、`Player.isGliding()` 等），这些在 1.21.11 和 26.2 之间未移除，因此编译 against 1.21.11 API 的 jar 在 26.2 运行时也能调用。新增检测若用了 26.2 独有 API，会破坏 1.21.11 profile 构建——避免这样做。

## 依赖范围

`paper-api` 是 `provided` 作用域——服务器运行时自带，不要改成 `compile`，也不要把 Paper API 打进 fat jar。

## 已知源码缺陷（改动前必读）

这是真实存在、可验证的 bug，但符合"娱乐性质检测"的整体定位；除非用户明确要求修复，**不要主动重构**：
- `KillAura`、`AutoClicker`、`InventoryCleaner` 三个检测在触发时都错误地累加到 `reachViolations`，而不是各自的 violation map（`AntiBitch.java` 中 `onEntityDamageByEntity` 和 `onInventoryClick`）。**新加的检测不要沿用此 bug**——每个检测使用自己独立的 violation map。

## 设计意图

源码注释明确：检测逻辑为"娱乐性质，可能存在误报"。不要以"提升检测准确度"为由自行加强判定阈值或加新检测，除非用户要求。

## 命令 API

插件使用 Paper 1.20.5+ 的新 Brigadier 命令 API（`io.papermc.paper.command.brigadier`），通过 `getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, ...)` 注册 `/antibitch`（别名 `ab`、`ac`）。三个子命令 reload/status/version 作为 literal 子节点，权限检查用 `.requires(ctx -> ...)`。**不要**用旧的 `onCommand` + `paper-plugin.yml` commands 声明方式——`paper-plugin.yml` 已移除 `commands:` 块，只保留 `permissions:`。新 API 在 1.21.11 和 26.2 两个 profile 间签名一致，同一份源码可共用。

## CI / 发布

`.github/workflows/maven.yml`：`main` 和 `develop` 分支 push / PR 触发 JDK 25 矩阵构建（`26.2` 与 `1.21.11` 两个 profile），各上传一个 jar 产物。GitHub Release **仅在推送 tag 时**创建，包含两个 jar。注意 release job 的 `if: github.ref == 'refs/heads/main'` 与内部 `if: startsWith(github.ref, 'refs/tags/')` 同时存在——只有打 tag 才会真正发布。

## 提交规范

Conventional Commits，**中文描述**：`fix: 修复 NoFall 检测中的类型转换错误`、`feat: 适配 Minecraft 1.21.11 和 Java 21...`。
