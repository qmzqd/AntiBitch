# AntiBitch

一个用于 Minecraft Paper 服务器的反作弊插件，提供 100 个娱乐性质的反作弊检测。

> ⚠️ **注意**：本插件的检测逻辑为**娱乐性质**，阈值宽松，可能存在误报。不适合用于严肃的反作弊场景。源码中包含若干已知缺陷（见 [AGENTS.md](AGENTS.md)），符合整体定位，未修复。

## 特性

- **100 个检测器**，覆盖移动、战斗、交互、方块、容器、聊天等各类行为
- **单文件实现**：所有逻辑集中在 `AntiBitch.java` 中，无分包、无独立 Detector 类
- **配置驱动**：每个检测均可独立启用/禁用，阈值与踢出消息可在 `config.yml` 中调整
- **线程安全**：使用 `ConcurrentHashMap` 存储玩家数据
- **自动清理**：玩家退出时自动释放其检测数据，避免内存泄漏

## 环境要求

| 项 | 要求 |
|---|---|
| Minecraft | Paper 26.2 |
| Java | 25 |
| 构建工具 | Maven 3.6+ |

## 构建

```bash
mvn -B package --file pom.xml
```

产物位于 `target/AntiBitch-*.jar`（shade 打胖包，不含 Paper API）。

仅编译验证：

```bash
mvn clean compile
```

> 仓库无测试框架，`mvn test` 为 no-op。

## 安装

将 `target/AntiBitch-*.jar` 放入 Paper 服务器的 `plugins/` 目录，重启服务器。首次启动会生成默认配置 `plugins/AntiBitch/config.yml`。

## 命令

| 命令 | 别名 | 权限 | 说明 |
|---|---|---|---|
| `/antibitch reload` | `/ab`, `/ac` | `antibitch.admin` | 重载配置文件 |
| `/antibitch status` | | `antibitch.admin` | 查看所有检测的启用状态 |
| `/antibitch version` | | `antibitch.admin` | 查看插件版本 |

`antibitch.admin` 权限默认授予 OP。

## 检测列表（100 个）

### 移动类
Speed、Fly、Timer、NoSlow、Sprint、NoFall、WaterWalk (Jesus)、Glide、Step、Spider、NoWeb、FastLadder、HighJump、Dolphin (FastSwim)、Phase (Vclip)、Blink (Teleport)、FastSneak、Derp、ElytraSpeed、FastSprint、IceSpeed、Bhop、AirJump、Jetpack、FastClimbVine、FastDescend、Strafe、Float、FastSneakAir、HeadRoll、TeleportUp、BoatFly

### 战斗类
Reach、AutoClicker、KillAura、Criticals、Aimbot、Hitbox、NoSwing、ReachVertical、AttackThroughWall、CriticalFake、TriggerBot、NoCooldown、MultiAttack、SnapAim、AttackWhileSprinting

### 交互类
FastBow、AutoTool、FastInteract、FastDoor、FastFenceGate、FastLever、FastButton、FastTrapdoor、AutoPot、FastBucket

### 方块类
Scaffold、Nuker、FastBreak、XRay、Tower、BreakReach、FastOre、BreakWhileMoving、PlaceReach、FastPlace

### 物品/容器类
AutoSoup、InventoryCleaner、FastEat、FastPotion、FastMilk、FastHoney、ChestStealer、AutoArmor、FastShiftClick、FastHotbarSwap、AutoFish、FastDrop、FastPickup、FastProjectile、FastEgg、FastShear

### 切换/状态类
Sneak、SneakSpam、SprintSpam、FlightSpam、GlideSpam、SwimSpam、SwapSpam、NoHunger、FastExp

### 其他
Regen、AntiKnockback、AutoTotem、BedSpam、FastBucketEmpty、FastBucketFill、ChatSpam、CommandSpam

## 配置

所有检测在 `plugins/AntiBitch/config.yml` 中配置。每个检测块包含：

```yaml
<检测名>:
  enabled: true              # 是否启用
  max-violations: 5          # 触发踢出的违规次数
  kick-message: "..."        # 踢出消息
  # 部分检测另有阈值参数（如 max-speed、min-interval 等）
```

全局设置：

```yaml
settings:
  debug-mode: false          # 调试日志
  broadcast-alerts: true     # 触发时是否全服广播
```

修改后执行 `/antibitch reload` 即时生效。

## 项目结构

```
src/main/java/nb114514/antibitch/AntiBitch.java   # 全部检测与命令逻辑（单文件）
src/main/resources/paper-plugin.yml               # 插件描述（api-version、命令、权限）
src/main/resources/config.yml                     # 默认配置模板
pom.xml                                           # Maven 构建（Java 25、Paper 26.2）
.github/workflows/maven.yml                       # CI：JDK 25 构建并上传 jar
```

## 版本适配

升级 Minecraft 版本时需同时修改两处：
- `pom.xml` 中 `paper-api` 的 `<version>`
- `src/main/resources/paper-plugin.yml` 中的 `api-version`

查最新 Paper API 版本：

```bash
curl -s https://repo.papermc.io/repository/maven-public/io/papermc/paper/paper-api/maven-metadata.xml
```

## 许可

见仓库根目录许可声明。

## 相关文档

- [AGENTS.md](AGENTS.md) — 面向 OpenCode/AI 协作的紧凑指引，包含已知源码缺陷与构建约定
