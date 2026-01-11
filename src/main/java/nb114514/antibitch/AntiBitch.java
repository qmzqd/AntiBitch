package nb114514.antibitch;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AntiBitch - Minecraft Paper 服务器反作弊插件
 *
 * <p>本插件提供以下反作弊检测功能：</p>
 * <ul>
 *   <li>Reach 检测 - 检测异常攻击距离</li>
 *   <li>Speed 检测 - 检测异常移动速度</li>
 *   <li>Fly 检测 - 检测异常飞行行为</li>
 *   <li>AutoClicker 检测 - 检测异常点击速度</li>
 *   <li>KillAura 检测 - 检测异常攻击频率</li>
 *   <li>NoFall 检测 - 检测无坠落伤害</li>
 *   <li>Criticals 检测 - 检测异常暴击频率</li>
 *   <li>Timer 检测 - 检测时间加速</li>
 *   <li>NoSlow 检测 - 检测无减速</li>
 *   <li>Sprint 检测 - 检测自动疾跑</li>
 *   <li>Aimbot 检测 - 检测自动瞄准</li>
 *   <li>FastBow 检测 - 检测快速射箭</li>
 *   <li>Regen 检测 - 检测快速回血</li>
 *   <li>Scaffold 检测 - 检测自动搭桥</li>
 *   <li>AutoTool 检测 - 检测自动切换工具</li>
 *   <li>AutoSoup 检测 - 检测自动喝汤</li>
 *   <li>InventoryCleaner 检测 - 检测自动清理背包</li>
 *   <li>Sneak 检测 - 检测自动潜行</li>
 *   <li>Hitbox 检测 - 检测碰撞箱修改</li>
 * </ul>
 *
 * <p>注意：本插件的检测逻辑为娱乐性质，可能存在误报。</p>
 *
 * @author qmzqd
 * @version 1.0-SNAPSHOT
 * @since 1.0
 */
public class AntiBitch extends JavaPlugin implements Listener {
    // 使用 ConcurrentHashMap 确保线程安全
    private final Map<UUID, Integer> reachViolations = new ConcurrentHashMap<>();
    private final Map<UUID, Long> moveTimes = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> moveCount = new ConcurrentHashMap<>();

    // AutoClicker 检测数据
    private final Map<UUID, Long> lastClickTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> clickCount = new ConcurrentHashMap<>();

    // KillAura 检测数据
    private final Map<UUID, Long> lastAttackTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> attackCount = new ConcurrentHashMap<>();

    // NoFall 检测数据
    private final Map<UUID, Double> lastFallHeight = new ConcurrentHashMap<>();

    // Criticals 检测数据
    private final Map<UUID, Integer> critCount = new ConcurrentHashMap<>();

    // Timer 检测数据
    private final Map<UUID, Long> lastMoveTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> timerViolations = new ConcurrentHashMap<>();

    // NoSlow 检测数据
    private final Map<UUID, Long> lastEatTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> noSlowViolations = new ConcurrentHashMap<>();

    // Sprint 检测数据
    private final Map<UUID, Integer> sprintViolations = new ConcurrentHashMap<>();

    // Aimbot 检测数据
    private final Map<UUID, Float> lastYaw = new ConcurrentHashMap<>();
    private final Map<UUID, Float> lastPitch = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> aimbotViolations = new ConcurrentHashMap<>();

    // FastBow 检测数据
    private final Map<UUID, Long> bowPullTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> fastBowViolations = new ConcurrentHashMap<>();

    // Regen 检测数据
    private final Map<UUID, Long> lastHealTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> regenViolations = new ConcurrentHashMap<>();

    // Scaffold 检测数据
    private final Map<UUID, Long> lastPlaceTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> scaffoldViolations = new ConcurrentHashMap<>();

    // AutoTool 检测数据
    private final Map<UUID, Long> lastToolSwitchTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> autoToolViolations = new ConcurrentHashMap<>();

    // AutoSoup 检测数据
    private final Map<UUID, Long> lastConsumeTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> autoSoupViolations = new ConcurrentHashMap<>();

    // InventoryCleaner 检测数据
    private final Map<UUID, Long> lastInventoryClickTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> inventoryClickCount = new ConcurrentHashMap<>();

    // Sneak 检测数据
    private final Map<UUID, Long> sneakStartTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> sneakViolations = new ConcurrentHashMap<>();

    // Hitbox 检测数据
    private final Map<UUID, Integer> hitboxViolations = new ConcurrentHashMap<>();

    // 配置参数
    private int maxReachViolations;
    private double maxReach;
    private int maxSpeed;
    private int maxAirMoves;
    private int maxClicksPerSecond;
    private int maxAutoClickerViolations;
    private int maxAttacksPerSecond;
    private int maxKillAuraViolations;
    private double noFallThreshold;
    private int maxCritsPerSecond;
    private int maxTimerViolations;
    private int maxNoSlowViolations;
    private int maxSprintViolations;
    private int maxAimbotViolations;
    private int minBowPullTime;
    private int maxFastBowViolations;
    private int minHealInterval;
    private int maxRegenViolations;
    private int minPlaceInterval;
    private int maxScaffoldViolations;
    private int minToolSwitchInterval;
    private int maxAutoToolViolations;
    private int minConsumeInterval;
    private int maxAutoSoupViolations;
    private int maxInventoryClicksPerSecond;
    private int maxInventoryCleanerViolations;
    private int maxSneakDuration;
    private int maxSneakViolations;
    private int maxHitboxViolations;

    @Override
    public void onEnable() {
        // 保存默认配置文件
        saveDefaultConfig();
        // 加载配置
        loadConfigValues();

        // 注册事件监听器
        Bukkit.getServer().getPluginManager().registerEvents(this, this);

        // 注册命令
        this.getCommand("antibitch").setExecutor(this);

        getLogger().info("AntiBitch 插件已启用！");
    }

    @Override
    public void onDisable() {
        // 清理所有数据
        reachViolations.clear();
        moveTimes.clear();
        moveCount.clear();
        lastClickTime.clear();
        clickCount.clear();
        lastAttackTime.clear();
        attackCount.clear();
        lastFallHeight.clear();
        critCount.clear();
        lastMoveTime.clear();
        timerViolations.clear();
        lastEatTime.clear();
        noSlowViolations.clear();
        sprintViolations.clear();
        lastYaw.clear();
        lastPitch.clear();
        aimbotViolations.clear();
        bowPullTime.clear();
        fastBowViolations.clear();
        lastHealTime.clear();
        regenViolations.clear();
        lastPlaceTime.clear();
        scaffoldViolations.clear();
        lastToolSwitchTime.clear();
        autoToolViolations.clear();
        lastConsumeTime.clear();
        autoSoupViolations.clear();
        lastInventoryClickTime.clear();
        inventoryClickCount.clear();
        sneakStartTime.clear();
        sneakViolations.clear();
        hitboxViolations.clear();
        getLogger().info("AntiBitch 插件已禁用！");
    }

    /**
     * 从配置文件加载参数值
     */
    private void loadConfigValues() {
        FileConfiguration config = getConfig();
        maxReachViolations = config.getInt("reach.max-violations", 5);
        maxReach = config.getDouble("reach.max-distance", 3.0);
        maxSpeed = config.getInt("speed.max-speed", 20);
        maxAirMoves = config.getInt("fly.max-air-moves", 10);
        maxClicksPerSecond = config.getInt("autoclicker.max-clicks-per-second", 15);
        maxAutoClickerViolations = config.getInt("autoclicker.max-violations", 5);
        maxAttacksPerSecond = config.getInt("killaura.max-attacks-per-second", 10);
        maxKillAuraViolations = config.getInt("killaura.max-violations", 5);
        noFallThreshold = config.getDouble("nofall.threshold", 3.0);
        maxCritsPerSecond = config.getInt("criticals.max-crits-per-second", 5);
        maxTimerViolations = config.getInt("timer.max-violations", 5);
        maxNoSlowViolations = config.getInt("noslow.max-violations", 5);
        maxSprintViolations = config.getInt("sprint.max-violations", 5);
        maxAimbotViolations = config.getInt("aimbot.max-violations", 5);
        minBowPullTime = config.getInt("fastbow.min-pull-time", 500);
        maxFastBowViolations = config.getInt("fastbow.max-violations", 5);
        minHealInterval = config.getInt("regen.min-heal-interval", 1000);
        maxRegenViolations = config.getInt("regen.max-violations", 5);
        minPlaceInterval = config.getInt("scaffold.min-place-interval", 100);
        maxScaffoldViolations = config.getInt("scaffold.max-violations", 5);
        minToolSwitchInterval = config.getInt("autotool.min-switch-interval", 50);
        maxAutoToolViolations = config.getInt("autotool.max-violations", 5);
        minConsumeInterval = config.getInt("autosoup.min-consume-interval", 200);
        maxAutoSoupViolations = config.getInt("autosoup.max-violations", 5);
        maxInventoryClicksPerSecond = config.getInt("inventorycleaner.max-clicks-per-second", 20);
        maxInventoryCleanerViolations = config.getInt("inventorycleaner.max-violations", 5);
        maxSneakDuration = config.getInt("sneak.max-duration", 30000);
        maxSneakViolations = config.getInt("sneak.max-violations", 5);
        maxHitboxViolations = config.getInt("hitbox.max-violations", 5);
        debugLog("配置参数已加载");
    }

    /**
     * 输出调试日志
     */
    private void debugLog(String message) {
        if (getConfig().getBoolean("settings.debug-mode", false)) {
            getLogger().info("[DEBUG] " + message);
        }
    }

    /**
     * 处理玩家退出事件，清理内存中的玩家数据
     *
     * @param event 玩家退出事件
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();
        reachViolations.remove(playerId);
        moveTimes.remove(playerId);
        moveCount.remove(playerId);
        lastClickTime.remove(playerId);
        clickCount.remove(playerId);
        lastAttackTime.remove(playerId);
        attackCount.remove(playerId);
        lastFallHeight.remove(playerId);
        critCount.remove(playerId);
        lastMoveTime.remove(playerId);
        timerViolations.remove(playerId);
        lastEatTime.remove(playerId);
        noSlowViolations.remove(playerId);
        sprintViolations.remove(playerId);
        lastYaw.remove(playerId);
        lastPitch.remove(playerId);
        aimbotViolations.remove(playerId);
        bowPullTime.remove(playerId);
        fastBowViolations.remove(playerId);
        lastHealTime.remove(playerId);
        regenViolations.remove(playerId);
        lastPlaceTime.remove(playerId);
        scaffoldViolations.remove(playerId);
        lastToolSwitchTime.remove(playerId);
        autoToolViolations.remove(playerId);
        lastConsumeTime.remove(playerId);
        autoSoupViolations.remove(playerId);
        lastInventoryClickTime.remove(playerId);
        inventoryClickCount.remove(playerId);
        sneakStartTime.remove(playerId);
        sneakViolations.remove(playerId);
        hitboxViolations.remove(playerId);
        debugLog("清理玩家数据: " + playerId);
    }

    /**
     * 处理实体攻击事件，检测 Reach、KillAura、Criticals、Aimbot 和 Hitbox 作弊
     *
     * @param event 实体攻击事件
     */
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;

        Player player = (Player) event.getDamager();
        Entity target = event.getEntity();
        UUID playerId = player.getUniqueId();
        double distance = player.getLocation().distance(target.getLocation());

        // Aimbot 检测（娱乐性质：检测头部转动速度异常）
        if (getConfig().getBoolean("aimbot.enabled", true)) {
            float currentYaw = player.getLocation().getYaw();
            float currentPitch = player.getLocation().getPitch();

            Float lastYawValue = lastYaw.get(playerId);
            Float lastPitchValue = lastPitch.get(playerId);

            if (lastYawValue != null && lastPitchValue != null) {
                float yawDiff = Math.abs(currentYaw - lastYawValue);
                float pitchDiff = Math.abs(currentPitch - lastPitchValue);

                // 如果头部转动速度过快，可能是 Aimbot
                if (yawDiff > 90 || pitchDiff > 90) {
                    int violations = aimbotViolations.getOrDefault(playerId, 0) + 1;
                    aimbotViolations.put(playerId, violations);

                    if (violations >= maxAimbotViolations) {
                        String kickMessage = getConfig().getString("aimbot.kick-message", "检测到自动瞄准");
                        player.kickPlayer(kickMessage);
                        getLogger().info("玩家 " + player.getName() + " 因 Aimbot 作弊被踢出");
                        return;
                    }
                }
            }

            lastYaw.put(playerId, currentYaw);
            lastPitch.put(playerId, currentPitch);
        }

        // Hitbox 检测（娱乐性质：检测碰撞箱修改）
        if (getConfig().getBoolean("hitbox.enabled", true)) {
            // 如果攻击距离异常远但仍然命中，可能是 Hitbox 扩展
            if (distance > maxReach * 1.5) {
                int violations = hitboxViolations.getOrDefault(playerId, 0) + 1;
                hitboxViolations.put(playerId, violations);

                if (violations >= maxHitboxViolations) {
                    String kickMessage = getConfig().getString("hitbox.kick-message", "检测到碰撞箱修改");
                    player.kickPlayer(kickMessage);
                    getLogger().info("玩家 " + player.getName() + " 因 Hitbox 作弊被踢出");
                    return;
                }
            }
        }

        // Reach 检测
        if (getConfig().getBoolean("reach.enabled", true)) {
            if (distance > maxReach) {
                int vl = reachViolations.getOrDefault(playerId, 0) + 1;
                reachViolations.put(playerId, vl);

                if (vl >= maxReachViolations) {
                    String kickMessage = getConfig().getString("reach.kick-message", "检测到异常攻击距离");
                    player.kickPlayer(kickMessage);
                    reachViolations.remove(playerId);
                    getLogger().info("玩家 " + player.getName() + " 因 Reach 作弊被踢出");
                    return;
                } else {
                    if (getConfig().getBoolean("settings.broadcast-alerts", true)) {
                        String alertTemplate = getConfig().getString("reach.alert-message", "[AntiBitch] 玩家 %player% 触发了 Reach 检测 (距离: %distance% 方块)");
                        String alertMessage = alertTemplate
                                .replace("%player%", player.getName())
                                .replace("%distance%", String.format("%.2f", distance));
                        Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', alertMessage));
                    }
                }
            }
        }

        // KillAura 检测（娱乐性质：检测攻击频率）
        if (getConfig().getBoolean("killaura.enabled", true)) {
            long now = System.currentTimeMillis();
            long lastAttack = lastAttackTime.getOrDefault(playerId, now - 1000);
            int attacks = attackCount.getOrDefault(playerId, 0);

            // 如果距离上次攻击超过1秒，重置计数
            if (now - lastAttack > 1000) {
                attackCount.put(playerId, 1);
                lastAttackTime.put(playerId, now);
            } else {
                attackCount.put(playerId, attacks + 1);
                if (attacks + 1 > maxAttacksPerSecond) {
                    int violations = reachViolations.getOrDefault(playerId, 0) + 1;
                    reachViolations.put(playerId, violations);

                    if (violations >= maxKillAuraViolations) {
                        String kickMessage = getConfig().getString("killaura.kick-message", "检测到异常攻击频率");
                        player.kickPlayer(kickMessage);
                        getLogger().info("玩家 " + player.getName() + " 因 KillAura 作弊被踢出");
                        return;
                    } else {
                        if (getConfig().getBoolean("settings.broadcast-alerts", true)) {
                            String alertMessage = "[AntiBitch] 玩家 " + player.getName() + " 触发了 KillAura 检测";
                            Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', alertMessage));
                        }
                    }
                }
            }
        }

        // Criticals 检测（娱乐性质：检测暴击频率）
        if (getConfig().getBoolean("criticals.enabled", true)) {
            // 检测是否在空中造成暴击
            if (!player.isOnGround() && player.getFallDistance() > 0) {
                long now = System.currentTimeMillis();
                long lastCrit = lastAttackTime.getOrDefault(playerId, now - 1000);
                int crits = critCount.getOrDefault(playerId, 0);

                if (now - lastCrit > 1000) {
                    critCount.put(playerId, 1);
                } else {
                    critCount.put(playerId, crits + 1);
                    if (crits + 1 > maxCritsPerSecond) {
                        String kickMessage = getConfig().getString("criticals.kick-message", "检测到异常暴击频率");
                        player.kickPlayer(kickMessage);
                        getLogger().info("玩家 " + player.getName() + " 因 Criticals 作弊被踢出");
                        return;
                    }
                }
            }
        }

        // AutoClicker 检测（娱乐性质：检测点击速度）
        if (getConfig().getBoolean("autoclicker.enabled", true)) {
            long now = System.currentTimeMillis();
            long lastClick = lastClickTime.getOrDefault(playerId, now - 1000);
            int clicks = clickCount.getOrDefault(playerId, 0);

            if (now - lastClick > 1000) {
                clickCount.put(playerId, 1);
                lastClickTime.put(playerId, now);
            } else {
                clickCount.put(playerId, clicks + 1);
                if (clicks + 1 > maxClicksPerSecond) {
                    int violations = reachViolations.getOrDefault(playerId, 0) + 1;
                    reachViolations.put(playerId, violations);

                    if (violations >= maxAutoClickerViolations) {
                        String kickMessage = getConfig().getString("autoclicker.kick-message", "检测到异常点击速度");
                        player.kickPlayer(kickMessage);
                        getLogger().info("玩家 " + player.getName() + " 因 AutoClicker 作弊被踢出");
                        return;
                    } else {
                        if (getConfig().getBoolean("settings.broadcast-alerts", true)) {
                            String alertMessage = "[AntiBitch] 玩家 " + player.getName() + " 触发了 AutoClicker 检测";
                            Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', alertMessage));
                        }
                    }
                }
            }
        }
    }

    /**
     * 处理玩家移动事件，检测 Speed 和 Fly 作弊
     *
     * @param event 玩家移动事件
     */
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        // 空指针检查
        if (event.getTo() == null || event.getFrom() == null) {
            return;
        }

        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        if (player.isFlying() || player.isOnGround()) {
            moveTimes.remove(playerId);
            return;
        }

        long now = System.currentTimeMillis();
        long last = moveTimes.getOrDefault(playerId, now);
        int moveDistance = (int) Math.round(event.getTo().distance(event.getFrom()));
        int moveTime = (int) (now - last);

        // 防止除零错误
        if (moveTime <= 0) {
            return;
        }

        int speed = moveDistance * 1000 / moveTime;

        if (speed > maxSpeed) {
            String kickMessage = getConfig().getString("speed.kick-message", "检测到异常移动速度");
            player.kickPlayer(kickMessage);
            getLogger().info("玩家 " + player.getName() + " 因 Speed 作弊被踢出 (速度: " + speed + ")");
        } else {
            moveTimes.put(playerId, now);
            int count = moveCount.getOrDefault(playerId, 0) + 1;
            moveCount.put(playerId, count);

            if (count > maxAirMoves) {
                String kickMessage = getConfig().getString("fly.kick-message", "此服务器未启用飞行功能");
                player.kickPlayer(kickMessage);
                getLogger().info("玩家 " + player.getName() + " 因 Fly 作弊被踢出");
                moveCount.remove(playerId);
            }
        }

        // Timer 检测（娱乐性质：检测时间加速）
        if (getConfig().getBoolean("timer.enabled", true)) {
            long lastMove = lastMoveTime.getOrDefault(playerId, now - 50);
            long moveInterval = now - lastMove;

            // 如果移动间隔过短（小于 30ms），可能是 Timer
            if (moveInterval < 30) {
                int violations = timerViolations.getOrDefault(playerId, 0) + 1;
                timerViolations.put(playerId, violations);

                if (violations >= maxTimerViolations) {
                    String kickMessage = getConfig().getString("timer.kick-message", "检测到时间加速");
                    player.kickPlayer(kickMessage);
                    getLogger().info("玩家 " + player.getName() + " 因 Timer 作弊被踢出");
                    return;
                }
            }
            lastMoveTime.put(playerId, now);
        }

        // NoSlow 检测（娱乐性质：检测吃东西时移动速度不减）
        if (getConfig().getBoolean("noslow.enabled", true)) {
            Long eatTime = lastEatTime.get(playerId);
            if (eatTime != null && now - eatTime < 2000) {
                // 如果玩家在吃东西后快速移动，可能是 NoSlow
                if (speed > 10) {
                    int violations = noSlowViolations.getOrDefault(playerId, 0) + 1;
                    noSlowViolations.put(playerId, violations);

                    if (violations >= maxNoSlowViolations) {
                        String kickMessage = getConfig().getString("noslow.kick-message", "检测到无减速");
                        player.kickPlayer(kickMessage);
                        getLogger().info("玩家 " + player.getName() + " 因 NoSlow 作弊被踢出");
                        return;
                    }
                }
            }
        }

        // Sprint 检测（娱乐性质：检测自动疾跑）
        if (getConfig().getBoolean("sprint.enabled", true)) {
            // 如果玩家没有按疾跑键但移动速度很快，可能是自动疾跑
            if (!player.isSprinting() && speed > 8 && player.isOnGround()) {
                int violations = sprintViolations.getOrDefault(playerId, 0) + 1;
                sprintViolations.put(playerId, violations);

                if (violations >= maxSprintViolations) {
                    String kickMessage = getConfig().getString("sprint.kick-message", "检测到自动疾跑");
                    player.kickPlayer(kickMessage);
                    getLogger().info("玩家 " + player.getName() + " 因 Sprint 作弊被踢出");
                    return;
                }
            }
        }

        // NoFall 检测（娱乐性质：检测无坠落伤害）
        if (!player.isOnGround() && !player.isFlying()) {
            lastFallHeight.put(playerId, (double) player.getFallDistance());
        }
    }

    /**
     * 处理实体伤害事件，检测 NoFall 作弊
     *
     * @param event 实体伤害事件
     */
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        UUID playerId = player.getUniqueId();

        // NoFall 检测
        if (getConfig().getBoolean("nofall.enabled", true) && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            Double fallDistance = lastFallHeight.get(playerId);
            if (fallDistance != null && fallDistance > noFallThreshold) {
                // 如果玩家从高处落下但没有受到伤害，可能是 NoFall
                if (event.getDamage() == 0) {
                    String kickMessage = getConfig().getString("nofall.kick-message", "检测到无坠落伤害");
                    player.kickPlayer(kickMessage);
                    getLogger().info("玩家 " + player.getName() + " 因 NoFall 作弊被踢出");
                }
            }
            lastFallHeight.remove(playerId);
        }
    }

    /**
     * 处理实体回血事件，检测 Regen 作弊
     *
     * @param event 实体回血事件
     */
    @EventHandler
    public void onEntityRegainHealth(EntityRegainHealthEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        UUID playerId = player.getUniqueId();

        // Regen 检测
        if (getConfig().getBoolean("regen.enabled", true)) {
            long now = System.currentTimeMillis();
            long lastHeal = lastHealTime.getOrDefault(playerId, now - minHealInterval);

            if (now - lastHeal < minHealInterval) {
                int violations = regenViolations.getOrDefault(playerId, 0) + 1;
                regenViolations.put(playerId, violations);

                if (violations >= maxRegenViolations) {
                    String kickMessage = getConfig().getString("regen.kick-message", "检测到快速回血");
                    player.kickPlayer(kickMessage);
                    getLogger().info("玩家 " + player.getName() + " 因 Regen 作弊被踢出");
                    return;
                }
            }
            lastHealTime.put(playerId, now);
        }
    }

    /**
     * 处理玩家交互事件，检测 FastBow 作弊
     *
     * @param event 玩家交互事件
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        ItemStack item = event.getItem();

        // FastBow 检测
        if (getConfig().getBoolean("fastbow.enabled", true) && item != null && item.getType() == Material.BOW) {
            if (event.getAction().name().contains("RIGHT")) {
                bowPullTime.put(playerId, System.currentTimeMillis());
            } else if (event.getAction().name().contains("LEFT")) {
                Long pullStart = bowPullTime.get(playerId);
                if (pullStart != null) {
                    long pullDuration = System.currentTimeMillis() - pullStart;
                    // 如果拉弓时间过短，可能是 FastBow
                    if (pullDuration < minBowPullTime) {
                        int violations = fastBowViolations.getOrDefault(playerId, 0) + 1;
                        fastBowViolations.put(playerId, violations);

                        if (violations >= maxFastBowViolations) {
                            String kickMessage = getConfig().getString("fastbow.kick-message", "检测到快速射箭");
                            player.kickPlayer(kickMessage);
                            getLogger().info("玩家 " + player.getName() + " 因 FastBow 作弊被踢出");
                            return;
                        }
                    }
                }
            }
        }
    }

    /**
     * 处理方块放置事件，检测 Scaffold 作弊
     *
     * @param event 方块放置事件
     */
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        // Scaffold 检测
        if (getConfig().getBoolean("scaffold.enabled", true)) {
            long now = System.currentTimeMillis();
            long lastPlace = lastPlaceTime.getOrDefault(playerId, now - minPlaceInterval);

            if (now - lastPlace < minPlaceInterval) {
                int violations = scaffoldViolations.getOrDefault(playerId, 0) + 1;
                scaffoldViolations.put(playerId, violations);

                if (violations >= maxScaffoldViolations) {
                    String kickMessage = getConfig().getString("scaffold.kick-message", "检测到自动搭桥");
                    player.kickPlayer(kickMessage);
                    getLogger().info("玩家 " + player.getName() + " 因 Scaffold 作弊被踢出");
                    return;
                }
            }
            lastPlaceTime.put(playerId, now);
        }
    }

    /**
     * 处理玩家手持物品事件，检测 AutoTool 作弊
     *
     * @param event 玩家手持物品事件
     */
    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        // AutoTool 检测
        if (getConfig().getBoolean("autotool.enabled", true)) {
            long now = System.currentTimeMillis();
            long lastSwitch = lastToolSwitchTime.getOrDefault(playerId, now - minToolSwitchInterval);

            if (now - lastSwitch < minToolSwitchInterval) {
                int violations = autoToolViolations.getOrDefault(playerId, 0) + 1;
                autoToolViolations.put(playerId, violations);

                if (violations >= maxAutoToolViolations) {
                    String kickMessage = getConfig().getString("autotool.kick-message", "检测到自动切换工具");
                    player.kickPlayer(kickMessage);
                    getLogger().info("玩家 " + player.getName() + " 因 AutoTool 作弊被踢出");
                    return;
                }
            }
            lastToolSwitchTime.put(playerId, now);
        }
    }

    /**
     * 处理背包点击事件，检测 InventoryCleaner 作弊
     *
     * @param event 背包点击事件
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        UUID playerId = player.getUniqueId();

        // InventoryCleaner 检测
        if (getConfig().getBoolean("inventorycleaner.enabled", true)) {
            long now = System.currentTimeMillis();
            long lastClick = lastInventoryClickTime.getOrDefault(playerId, now - 50);
            int clicks = inventoryClickCount.getOrDefault(playerId, 0);

            if (now - lastClick > 1000) {
                inventoryClickCount.put(playerId, 1);
                lastInventoryClickTime.put(playerId, now);
            } else {
                inventoryClickCount.put(playerId, clicks + 1);
                if (clicks + 1 > maxInventoryClicksPerSecond) {
                    int violations = reachViolations.getOrDefault(playerId, 0) + 1;
                    reachViolations.put(playerId, violations);

                    if (violations >= maxInventoryCleanerViolations) {
                        String kickMessage = getConfig().getString("inventorycleaner.kick-message", "检测到自动清理背包");
                        player.kickPlayer(kickMessage);
                        getLogger().info("玩家 " + player.getName() + " 因 InventoryCleaner 作弊被踢出");
                        return;
                    }
                }
            }
            lastInventoryClickTime.put(playerId, now);
        }
    }

    /**
     * 处理玩家潜行切换事件，检测 Sneak 作弊
     *
     * @param event 玩家潜行切换事件
     */
    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        // Sneak 检测
        if (getConfig().getBoolean("sneak.enabled", true)) {
            if (event.isSneaking()) {
                // 开始潜行
                sneakStartTime.put(playerId, System.currentTimeMillis());
            } else {
                // 停止潜行
                Long sneakStart = sneakStartTime.get(playerId);
                if (sneakStart != null) {
                    long sneakDuration = System.currentTimeMillis() - sneakStart;
                    // 如果潜行时间过长，可能是自动潜行
                    if (sneakDuration > maxSneakDuration) {
                        int violations = sneakViolations.getOrDefault(playerId, 0) + 1;
                        sneakViolations.put(playerId, violations);

                        if (violations >= maxSneakViolations) {
                            String kickMessage = getConfig().getString("sneak.kick-message", "检测到自动潜行");
                            player.kickPlayer(kickMessage);
                            getLogger().info("玩家 " + player.getName() + " 因 Sneak 作弊被踢出");
                            return;
                        }
                    }
                }
                sneakStartTime.remove(playerId);
            }
        }
    }

    /**
     * 处理插件命令
     *
     * @param sender 命令发送者
     * @param command 命令对象
     * @param label 命令标签
     * @param args 命令参数
     * @return 命令是否处理成功
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("antibitch.admin")) {
            sender.sendMessage(ChatColor.RED + "你没有权限使用此命令！");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatColor.GOLD + "=== AntiBitch 插件管理 ===");
            sender.sendMessage(ChatColor.YELLOW + "/antibitch reload - 重载配置文件");
            sender.sendMessage(ChatColor.YELLOW + "/antibitch status - 查看插件状态");
            sender.sendMessage(ChatColor.YELLOW + "/antibitch version - 查看插件版本");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                reloadConfig();
                loadConfigValues();
                sender.sendMessage(ChatColor.GREEN + "配置文件已重载！");
                getLogger().info(sender.getName() + " 重载了配置文件");
                break;
            case "status":
                sender.sendMessage(ChatColor.GOLD + "=== AntiBitch 状态 ===");
                sender.sendMessage(ChatColor.YELLOW + "版本: " + getDescription().getVersion());
                sender.sendMessage(ChatColor.YELLOW + "Reach 检测: " + (getConfig().getBoolean("reach.enabled") ? "启用" : "禁用"));
                sender.sendMessage(ChatColor.YELLOW + "Speed 检测: " + (getConfig().getBoolean("speed.enabled") ? "启用" : "禁用"));
                sender.sendMessage(ChatColor.YELLOW + "Fly 检测: " + (getConfig().getBoolean("fly.enabled") ? "启用" : "禁用"));
                sender.sendMessage(ChatColor.YELLOW + "AutoClicker 检测: " + (getConfig().getBoolean("autoclicker.enabled") ? "启用" : "禁用"));
                sender.sendMessage(ChatColor.YELLOW + "KillAura 检测: " + (getConfig().getBoolean("killaura.enabled") ? "启用" : "禁用"));
                sender.sendMessage(ChatColor.YELLOW + "NoFall 检测: " + (getConfig().getBoolean("nofall.enabled") ? "启用" : "禁用"));
                sender.sendMessage(ChatColor.YELLOW + "Criticals 检测: " + (getConfig().getBoolean("criticals.enabled") ? "启用" : "禁用"));
                sender.sendMessage(ChatColor.YELLOW + "Timer 检测: " + (getConfig().getBoolean("timer.enabled") ? "启用" : "禁用"));
                sender.sendMessage(ChatColor.YELLOW + "NoSlow 检测: " + (getConfig().getBoolean("noslow.enabled") ? "启用" : "禁用"));
                sender.sendMessage(ChatColor.YELLOW + "Sprint 检测: " + (getConfig().getBoolean("sprint.enabled") ? "启用" : "禁用"));
                sender.sendMessage(ChatColor.YELLOW + "Aimbot 检测: " + (getConfig().getBoolean("aimbot.enabled") ? "启用" : "禁用"));
                sender.sendMessage(ChatColor.YELLOW + "FastBow 检测: " + (getConfig().getBoolean("fastbow.enabled") ? "启用" : "禁用"));
                sender.sendMessage(ChatColor.YELLOW + "Regen 检测: " + (getConfig().getBoolean("regen.enabled") ? "启用" : "禁用"));
                sender.sendMessage(ChatColor.YELLOW + "Scaffold 检测: " + (getConfig().getBoolean("scaffold.enabled") ? "启用" : "禁用"));
                sender.sendMessage(ChatColor.YELLOW + "AutoTool 检测: " + (getConfig().getBoolean("autotool.enabled") ? "启用" : "禁用"));
                sender.sendMessage(ChatColor.YELLOW + "AutoSoup 检测: " + (getConfig().getBoolean("autosoup.enabled") ? "启用" : "禁用"));
                sender.sendMessage(ChatColor.YELLOW + "InventoryCleaner 检测: " + (getConfig().getBoolean("inventorycleaner.enabled") ? "启用" : "禁用"));
                sender.sendMessage(ChatColor.YELLOW + "Sneak 检测: " + (getConfig().getBoolean("sneak.enabled") ? "启用" : "禁用"));
                sender.sendMessage(ChatColor.YELLOW + "Hitbox 检测: " + (getConfig().getBoolean("hitbox.enabled") ? "启用" : "禁用"));
                break;
            case "version":
                sender.sendMessage(ChatColor.GOLD + "AntiBitch v" + getDescription().getVersion());
                break;
            default:
                sender.sendMessage(ChatColor.RED + "未知命令！使用 /antibitch 查看帮助");
        }

        return true;
    }
}
