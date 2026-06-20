# AGENTS.md

Compact guidance for OpenCode sessions working in this repo.

## Project

Minecraft Paper 26.2 反作弊插件，Java 25，Maven 构建。单文件实现：所有 100 个检测器和命令处理都在 `src/main/java/nb114514/antibitch/AntiBitch.java`（约 3300 行）里。没有分包、没有独立的 Detector 类——不要去找不存在的结构。

## Build & verify

- 构建（含 shade 打胖包）：`mvn -B package --file pom.xml` → 产物 `target/AntiBitch-*.jar`
- 仅编译：`mvn clean compile`
- 测试：`mvn test`——**仓库无测试**，CI 里也是 `continue-on-error: true`，等同于 no-op。不要假设有测试框架。
- 没有 lint / formatter / typecheck 命令；Java 编译即类型检查。

## 版本与 Minecraft 适配

升级 MC 版本时**必须同时改两处**，否则插件无法加载：
- `pom.xml` 里 `paper-api` 的 `<version>`（如 `26.2.build.24-alpha`）
- `src/main/resources/paper-plugin.yml` 里的 `api-version`（如 `26.2`）

**版本字符串格式已变**：从 1.21.x 时代起 Paper API 用 `<MC>-R0.1-SNAPSHOT`（如 `1.21.11-R0.1-SNAPSHOT`）；26.x 起改为 `<MC>.build.<N>-<alpha|beta|stable>`（如 `26.2.build.24-alpha`），且 26.x 只有 alpha/beta 构建时也会被标为 `release`。查最新可用版本：`curl -s https://repo.papermc.io/repository/maven-public/io/papermc/paper/paper-api/maven-metadata.xml` 看 `<latest>`/`<release>`。

插件版本号只在 `pom.xml` 的 `<version>` 里维护；`paper-plugin.yml` 用 `${project.version}`，靠 Maven resource filtering（`<filtering>true</filtering>`）在构建时替换。**不要**手改 `paper-plugin.yml` 里的 `version:`。

## 依赖范围

`paper-api` 是 `provided` 作用域——服务器运行时自带，不要改成 `compile`，也不要把 Paper API 打进 fat jar。

## 已知源码缺陷（改动前必读）

这是真实存在、可验证的 bug，但符合"娱乐性质检测"的整体定位；除非用户明确要求修复，**不要主动重构**：
- `KillAura`、`AutoClicker`、`InventoryCleaner` 三个检测在触发时都错误地累加到 `reachViolations`，而不是各自的 violation map（`AntiBitch.java` 中 `onEntityDamageByEntity` 和 `onInventoryClick`）。**新加的检测不要沿用此 bug**——每个检测使用自己独立的 violation map。

## 设计意图

源码注释明确：检测逻辑为"娱乐性质，可能存在误报"。不要以"提升检测准确度"为由自行加强判定阈值或加新检测，除非用户要求。

## CI / 发布

`.github/workflows/maven.yml`：`main` 和 `develop` 分支 push / PR 触发 JDK 25 构建并上传 jar 产物。GitHub Release **仅在推送 tag 时**创建。注意 release job 的 `if: github.ref == 'refs/heads/main'` 与内部 `if: startsWith(github.ref, 'refs/tags/')` 同时存在——只有打 tag 才会真正发布。

## 提交规范

Conventional Commits，**中文描述**：`fix: 修复 NoFall 检测中的类型转换错误`、`feat: 适配 Minecraft 1.21.11 和 Java 21...`。
