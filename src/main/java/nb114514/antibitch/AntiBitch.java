package nb114514.antibitch;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.entity.EntityToggleSwimEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;

import java.util.Map;
import java.util.Set;
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
 *   <li>Nuker 检测 - 检测瞬间破坏多方块</li>
 *   <li>FastBreak 检测 - 检测破坏方块间隔过短</li>
 *   <li>FastEat 检测 - 检测进食间隔过短</li>
 *   <li>WaterWalk 检测 - 检测水面行走</li>
 *   <li>Glide 检测 - 检测空中缓降滑行</li>
 *   <li>Step 检测 - 检测异常台阶上升</li>
 *   <li>AntiKnockback 检测 - 检测击退抵抗</li>
 *   <li>NoSwing 检测 - 检测攻击无挥动</li>
 *   <li>Spider 检测 - 检测贴墙攀爬</li>
 *   <li>NoWeb 检测 - 检测蛛网内快速移动</li>
 *   <li>FastLadder 检测 - 检测梯子快速攀爬</li>
 *   <li>BoatFly 检测 - 检测骑船滞空</li>
 *   <li>HighJump 检测 - 检测异常高跳</li>
 *   <li>Dolphin 检测 - 检测水中快速游泳</li>
 *   <li>Phase 检测 - 检测穿墙传送</li>
 *   <li>Blink 检测 - 检测异常远距瞬移</li>
 *   <li>FastSneak 检测 - 检测潜行加速</li>
 *   <li>Derp 检测 - 检测头部异常转动</li>
 *   <li>ElytraSpeed 检测 - 检测滑翔速度异常</li>
 *   <li>XRay 检测 - 检测稀有矿石异常高产</li>
 *   <li>Tower 检测 - 检测自动搭塔</li>
 *   <li>ChestStealer 检测 - 检测快速偷箱子</li>
 *   <li>AutoArmor 检测 - 检测自动装备护甲</li>
 *   <li>AutoFish 检测 - 检测自动钓鱼</li>
 *   <li>FastSprint 检测 - 检测疾跑速度异常</li>
 *   <li>IceSpeed 检测 - 检测冰上加速</li>
 *   <li>Bhop 检测 - 检测连跳</li>
 *   <li>AirJump 检测 - 检测空中起跳</li>
 *   <li>Jetpack 检测 - 检测持续上升</li>
 *   <li>FastClimbVine 检测 - 检测藤蔓快速攀爬</li>
 *   <li>FastDescend 检测 - 检测异常快速下落</li>
 *   <li>Strafe 检测 - 检测疾跑横向位移异常</li>
 *   <li>Float 检测 - 检测空中悬浮</li>
 *   <li>FastSneakAir 检测 - 检测空中潜行</li>
 *   <li>HeadRoll 检测 - 检测头部异常俯仰</li>
 *   <li>TeleportUp 检测 - 检测纵向瞬移上升</li>
 *   <li>ReachVertical 检测 - 检测纵向攻击距离过大</li>
 *   <li>AttackThroughWall 检测 - 检测隔墙攻击</li>
 *   <li>CriticalFake 检测 - 检测伪造暴击</li>
 *   <li>TriggerBot 检测 - 检测精准冷却攻击</li>
 *   <li>NoCooldown 检测 - 检测攻击无冷却</li>
 *   <li>MultiAttack 检测 - 检测多实体攻击</li>
 *   <li>SnapAim 检测 - 检测攻击瞬间朝向突变</li>
 *   <li>AttackWhileSprinting 检测 - 检测疾跑攻击</li>
 *   <li>FastInteract 检测 - 检测快速交互</li>
 *   <li>FastDoor 检测 - 检测快速门切换</li>
 *   <li>FastFenceGate 检测 - 检测快速栅栏门切换</li>
 *   <li>FastLever 检测 - 检测快速拉杆切换</li>
 *   <li>FastButton 检测 - 检测快速按钮切换</li>
 *   <li>FastTrapdoor 检测 - 检测快速活板门切换</li>
 *   <li>AutoPot 检测 - 检测自动喷溅药水</li>
 *   <li>FastBucket 检测 - 检测快速桶使用</li>
 *   <li>FastPotion 检测 - 检测快速喝药水</li>
 *   <li>FastMilk 检测 - 检测快速喝牛奶</li>
 *   <li>FastHoney 检测 - 检测快速喝蜂蜜</li>
 *   <li>BreakReach 检测 - 检测破坏方块距离过远</li>
 *   <li>FastOre 检测 - 检测快速破坏矿石</li>
 *   <li>BreakWhileMoving 检测 - 检测移动中破坏方块</li>
 *   <li>PlaceReach 检测 - 检测放置方块距离过远</li>
 *   <li>FastPlace 检测 - 检测快速放置方块</li>
 *   <li>FastShiftClick 检测 - 检测快速 shift-click</li>
 *   <li>FastHotbarSwap 检测 - 检测快速快捷栏切换</li>
 *   <li>SneakSpam 检测 - 检测快速潜行切换</li>
 *   <li>FastDrop 检测 - 检测快速丢物品</li>
 *   <li>FastPickup 检测 - 检测快速捡物品</li>
 *   <li>FastProjectile 检测 - 检测快速发射抛射物</li>
 *   <li>SprintSpam 检测 - 检测快速疾跑切换</li>
 *   <li>FlightSpam 检测 - 检测快速飞行切换</li>
 *   <li>GlideSpam 检测 - 检测快速滑翔切换</li>
 *   <li>SwimSpam 检测 - 检测快速游泳切换</li>
 *   <li>SwapSpam 检测 - 检测快速主副手切换</li>
 *   <li>NoHunger 检测 - 检测饥饿值异常</li>
 *   <li>FastShear 检测 - 检测快速剪羊毛</li>
 *   <li>FastBucketEmpty 检测 - 检测快速倒空桶</li>
 *   <li>FastBucketFill 检测 - 检测快速装满桶</li>
 *   <li>BedSpam 检测 - 检测快速上床</li>
 *   <li>AutoTotem 检测 - 检测自动图腾</li>
 *   <li>FastExp 检测 - 检测经验获取过快</li>
 *   <li>ChatSpam 检测 - 检测刷屏聊天</li>
 *   <li>CommandSpam 检测 - 检测刷屏命令</li>
 *   <li>FastEgg 检测 - 检测快速丢鸡蛋</li>
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
    private final Map<UUID, Integer> reachViolations = newViolationMap("reach");
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
    private final Map<UUID, Integer> timerViolations = newViolationMap("timer");

    // NoSlow 检测数据
    private final Map<UUID, Long> lastEatTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> noSlowViolations = newViolationMap("noSlow");

    // Sprint 检测数据
    private final Map<UUID, Integer> sprintViolations = newViolationMap("sprint");

    // Aimbot 检测数据
    private final Map<UUID, Float> lastYaw = new ConcurrentHashMap<>();
    private final Map<UUID, Float> lastPitch = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> aimbotViolations = newViolationMap("aimbot");

    // FastBow 检测数据
    private final Map<UUID, Long> bowPullTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> fastBowViolations = newViolationMap("fastBow");

    // Regen 检测数据
    private final Map<UUID, Long> lastHealTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> regenViolations = newViolationMap("regen");

    // Scaffold 检测数据
    private final Map<UUID, Long> lastPlaceTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> scaffoldViolations = newViolationMap("scaffold");

    // AutoTool 检测数据
    private final Map<UUID, Long> lastToolSwitchTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> autoToolViolations = newViolationMap("autoTool");

    // AutoSoup 检测数据
    private final Map<UUID, Long> lastConsumeTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> autoSoupViolations = newViolationMap("autoSoup");

    // InventoryCleaner 检测数据
    private final Map<UUID, Long> lastInventoryClickTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> inventoryClickCount = new ConcurrentHashMap<>();

    // Sneak 检测数据
    private final Map<UUID, Long> sneakStartTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> sneakViolations = newViolationMap("sneak");

    // Hitbox 检测数据
    private final Map<UUID, Integer> hitboxViolations = newViolationMap("hitbox");

    // Nuker / FastBreak 检测数据（共用 BlockBreakEvent handler）
    private final Map<UUID, Long> lastBlockBreakTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> blockBreakCount = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> nukerViolations = newViolationMap("nuker");
    private final Map<UUID, Integer> fastBreakViolations = newViolationMap("fastBreak");

    // FastEat 检测数据
    private final Map<UUID, Long> lastEatConsumeTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> fastEatViolations = newViolationMap("fastEat");

    // WaterWalk / Glide / Step / Spider / NoWeb / FastLadder 检测数据（共用 PlayerMoveEvent handler）
    private final Map<UUID, Integer> waterWalkViolations = newViolationMap("waterWalk");
    private final Map<UUID, Integer> glideViolations = newViolationMap("glide");
    private final Map<UUID, Integer> stepViolations = newViolationMap("step");
    private final Map<UUID, Integer> spiderViolations = newViolationMap("spider");
    private final Map<UUID, Integer> noWebViolations = newViolationMap("noWeb");
    private final Map<UUID, Integer> fastLadderViolations = newViolationMap("fastLadder");

    // AntiKnockback 检测数据
    private final Map<UUID, Integer> antiKnockbackViolations = newViolationMap("antiKnockback");

    // NoSwing 检测数据
    private final Map<UUID, Long> lastSwingTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> noSwingViolations = newViolationMap("noSwing");

    // BoatFly 检测数据
    private final Map<UUID, Integer> boatAirMoves = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> boatFlyViolations = newViolationMap("boatFly");

    // HighJump / Dolphin / Phase / Blink / FastSneak / Derp / ElytraSpeed 检测数据（共用 onMoveChecks）
    private final Map<UUID, Integer> highJumpViolations = newViolationMap("highJump");
    private final Map<UUID, Integer> dolphinViolations = newViolationMap("dolphin");
    private final Map<UUID, Integer> phaseViolations = newViolationMap("phase");
    private final Map<UUID, Integer> blinkViolations = newViolationMap("blink");
    private final Map<UUID, Integer> fastSneakViolations = newViolationMap("fastSneak");
    private final Map<UUID, Float> lastMoveYaw = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> derpViolations = newViolationMap("derp");
    private final Map<UUID, Integer> elytraSpeedViolations = newViolationMap("elytraSpeed");

    // XRay 检测数据
    private final Map<UUID, Long> lastOreTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> oreCount = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> xrayViolations = newViolationMap("xray");

    // Tower 检测数据
    private final Map<UUID, Long> lastTowerTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> towerViolations = newViolationMap("tower");

    // ChestStealer 检测数据（与 InventoryCleaner 独立）
    private final Map<UUID, Long> lastChestClickTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> chestClickCount = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> chestStealerViolations = newViolationMap("chestStealer");

    // AutoArmor 检测数据
    private final Map<UUID, Long> lastArmorEquipTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> autoArmorViolations = newViolationMap("autoArmor");

    // AutoFish 检测数据
    private final Map<UUID, Long> lastFishTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> fishCount = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> autoFishViolations = newViolationMap("autoFish");

    // === 第二批扩展检测数据 ===
    // Movement (onMoveChecks)
    private final Map<UUID, Integer> fastSprintViolations = newViolationMap("fastSprint");
    private final Map<UUID, Integer> iceSpeedViolations = newViolationMap("iceSpeed");
    private final Map<UUID, Integer> bhopViolations = newViolationMap("bhop");
    private final Map<UUID, Long> lastGroundTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> airJumpViolations = newViolationMap("airJump");
    private final Map<UUID, Integer> jetpackViolations = newViolationMap("jetpack");
    private final Map<UUID, Integer> fastClimbVineViolations = newViolationMap("fastClimbVine");
    private final Map<UUID, Integer> fastDescendViolations = newViolationMap("fastDescend");
    private final Map<UUID, Integer> strafeViolations = newViolationMap("strafe");
    private final Map<UUID, Integer> floatViolations = newViolationMap("float");
    private final Map<UUID, Integer> fastSneakAirViolations = newViolationMap("fastSneakAir");
    private final Map<UUID, Float> lastMovePitch = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> headRollViolations = newViolationMap("headRoll");
    private final Map<UUID, Integer> teleportUpViolations = newViolationMap("teleportUp");

    // Combat (onEntityDamageByEntity)
    private final Map<UUID, Integer> reachVerticalViolations = newViolationMap("reachVertical");
    private final Map<UUID, Integer> attackThroughWallViolations = newViolationMap("attackThroughWall");
    private final Map<UUID, Integer> criticalFakeViolations = newViolationMap("criticalFake");
    private final Map<UUID, Long> lastAttackEnd = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> triggerBotViolations = newViolationMap("triggerBot");
    private final Map<UUID, Long> lastAttackStart = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> noCooldownViolations = newViolationMap("noCooldown");
    private final Map<UUID, Long> lastMultiAttackTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> multiAttackCount = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> multiAttackViolations = newViolationMap("multiAttack");
    private final Map<UUID, Float> preAttackYaw = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> snapAimViolations = newViolationMap("snapAim");
    private final Map<UUID, Integer> attackWhileSprintingViolations = newViolationMap("attackWhileSprinting");

    // Interact (onPlayerInteract)
    private final Map<UUID, Long> lastInteractTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> fastInteractViolations = newViolationMap("fastInteract");
    private final Map<UUID, Long> lastDoorTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> fastDoorViolations = newViolationMap("fastDoor");
    private final Map<UUID, Long> lastFenceGateTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> fastFenceGateViolations = newViolationMap("fastFenceGate");
    private final Map<UUID, Long> lastLeverTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> fastLeverViolations = newViolationMap("fastLever");
    private final Map<UUID, Long> lastButtonTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> fastButtonViolations = newViolationMap("fastButton");
    private final Map<UUID, Long> lastTrapdoorTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> fastTrapdoorViolations = newViolationMap("fastTrapdoor");
    private final Map<UUID, Long> lastPotionTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> autoPotViolations = newViolationMap("autoPot");
    private final Map<UUID, Long> lastBucketTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> fastBucketViolations = newViolationMap("fastBucket");

    // ItemConsume
    private final Map<UUID, Long> lastPotionConsumeTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> fastPotionViolations = newViolationMap("fastPotion");
    private final Map<UUID, Long> lastMilkTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> fastMilkViolations = newViolationMap("fastMilk");
    private final Map<UUID, Long> lastHoneyTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> fastHoneyViolations = newViolationMap("fastHoney");

    // BlockBreak
    private final Map<UUID, Integer> breakReachViolations = newViolationMap("breakReach");
    private final Map<UUID, Long> lastOreBreakTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> fastOreViolations = newViolationMap("fastOre");
    private final Map<UUID, Integer> breakWhileMovingViolations = newViolationMap("breakWhileMoving");

    // BlockPlace
    private final Map<UUID, Integer> placeReachViolations = newViolationMap("placeReach");
    private final Map<UUID, Long> lastGeneralPlaceTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> fastPlaceViolations = newViolationMap("fastPlace");

    // InventoryClick / ItemHeld
    private final Map<UUID, Long> lastShiftClickTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> fastShiftClickViolations = newViolationMap("fastShiftClick");
    private final Map<UUID, Long> lastHotbarSwapTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> fastHotbarSwapViolations = newViolationMap("fastHotbarSwap");

    // SneakSpam
    private final Map<UUID, Long> lastSneakToggleTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> sneakSpamViolations = newViolationMap("sneakSpam");

    // New-event handlers
    private final Map<UUID, Long> lastDropTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> fastDropViolations = newViolationMap("fastDrop");
    private final Map<UUID, Long> lastPickupTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> fastPickupViolations = newViolationMap("fastPickup");
    private final Map<UUID, Long> lastProjectileTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> fastProjectileViolations = newViolationMap("fastProjectile");
    private final Map<UUID, Long> lastSprintToggleTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> sprintSpamViolations = newViolationMap("sprintSpam");
    private final Map<UUID, Long> lastFlightToggleTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> flightSpamViolations = newViolationMap("flightSpam");
    private final Map<UUID, Long> lastGlideToggleTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> glideSpamViolations = newViolationMap("glideSpam");
    private final Map<UUID, Long> lastSwimToggleTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> swimSpamViolations = newViolationMap("swimSpam");
    private final Map<UUID, Long> lastSwapTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> swapSpamViolations = newViolationMap("swapSpam");
    private final Map<UUID, Integer> noHungerViolations = newViolationMap("noHunger");
    private final Map<UUID, Long> lastShearTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> fastShearViolations = newViolationMap("fastShear");
    private final Map<UUID, Long> lastBucketEmptyTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> fastBucketEmptyViolations = newViolationMap("fastBucketEmpty");
    private final Map<UUID, Long> lastBucketFillTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> fastBucketFillViolations = newViolationMap("fastBucketFill");
    private final Map<UUID, Long> lastBedTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> bedSpamViolations = newViolationMap("bedSpam");
    private final Map<UUID, Long> lastTotemTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> autoTotemViolations = newViolationMap("autoTotem");
    private final Map<UUID, Long> lastExpTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> fastExpViolations = newViolationMap("fastExp");
    private final Map<UUID, Long> lastChatTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> chatSpamViolations = newViolationMap("chatSpam");
    private final Map<UUID, Long> lastCommandTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> commandSpamViolations = newViolationMap("commandSpam");
    private final Map<UUID, Long> lastEggTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> fastEggViolations = newViolationMap("fastEgg");

    // 玩家单独开关：
    //   全局开时 disabledPlayers 中的玩家被显式禁用
    //   全局关时 enabledPlayers 中的玩家被显式启用
    private final Set<UUID> disabledPlayers = ConcurrentHashMap.newKeySet();
    private final Set<UUID> enabledPlayers = ConcurrentHashMap.newKeySet();
    private final ThreadLocal<String> nextViolationCheckName = new ThreadLocal<>();

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

    // Nuker / FastBreak
    private int maxNukerBlocks;
    private int maxNukerViolations;
    private int minBreakInterval;
    private int maxFastBreakViolations;

    // FastEat
    private int minEatInterval;
    private int maxFastEatViolations;

    // WaterWalk
    private int maxWaterWalkViolations;

    // Glide
    private double maxGlideFallSpeed;
    private int maxGlideViolations;

    // Step
    private double maxStepHeight;
    private int maxStepViolations;

    // AntiKnockback
    private double minKnockbackRatio;
    private int maxAntiKnockbackViolations;

    // NoSwing
    private long swingWindowMs;
    private int maxNoSwingViolations;

    // Spider
    private int maxSpiderViolations;

    // NoWeb
    private int maxNoWebSpeed;
    private int maxNoWebViolations;

    // FastLadder
    private int maxFastLadderSpeed;
    private int maxFastLadderViolations;

    // BoatFly
    private int maxBoatAirMoves;
    private int maxBoatFlyViolations;

    // HighJump
    private double minHighJumpHeight;
    private int maxHighJumpViolations;

    // Dolphin (FastSwim)
    private int maxSwimSpeed;
    private int maxDolphinViolations;

    // Phase (Vclip)
    private double maxPhaseDistance;
    private int maxPhaseViolations;

    // Blink (Teleport)
    private double maxBlinkDistance;
    private int maxBlinkViolations;

    // FastSneak
    private int maxSneakSpeed;
    private int maxFastSneakViolations;

    // Derp
    private double maxDerpYawChange;
    private int maxDerpViolations;

    // ElytraSpeed
    private int maxElytraSpeed;
    private int maxElytraSpeedViolations;

    // XRay
    private int maxOresPerMinute;
    private int maxXrayViolations;

    // Tower
    private long minTowerInterval;
    private int maxTowerViolations;

    // ChestStealer
    private int maxChestClicksPerSecond;
    private int maxChestStealerViolations;

    // AutoArmor
    private long minArmorEquipInterval;
    private int maxAutoArmorViolations;

    // AutoFish
    private int maxFishPerMinute;
    private int maxAutoFishViolations;

    // === 第二批扩展检测配置 ===
    // Movement
    private int maxFastSprintSpeed;
    private int maxFastSprintViolations;
    private int maxIceSpeed;
    private int maxIceSpeedViolations;
    private int maxBhopJumps;
    private int maxBhopViolations;
    private int maxAirJumpViolations;
    private double minJetpackUp;
    private int maxJetpackViolations;
    private int maxFastClimbVineSpeed;
    private int maxFastClimbVineViolations;
    private double maxFastDescendSpeed;
    private int maxFastDescendViolations;
    private int maxStrafeViolations;
    private int maxFloatViolations;
    private int maxFastSneakAirViolations;
    private double maxHeadRollChange;
    private int maxHeadRollViolations;
    private double maxTeleportUp;
    private int maxTeleportUpViolations;

    // Combat
    private double maxReachVertical;
    private int maxReachVerticalViolations;
    private int maxAttackThroughWallViolations;
    private int maxCriticalFakeViolations;
    private int maxTriggerBotViolations;
    private long minAttackCooldown;
    private int maxNoCooldownViolations;
    private int maxMultiAttackCount;
    private int maxMultiAttackViolations;
    private double maxSnapAimChange;
    private int maxSnapAimViolations;
    private int maxAttackWhileSprintingViolations;

    // Interact
    private long minInteractInterval;
    private int maxFastInteractViolations;
    private long minDoorInterval;
    private int maxFastDoorViolations;
    private long minFenceGateInterval;
    private int maxFastFenceGateViolations;
    private long minLeverInterval;
    private int maxFastLeverViolations;
    private long minButtonInterval;
    private int maxFastButtonViolations;
    private long minTrapdoorInterval;
    private int maxFastTrapdoorViolations;
    private long minPotionInterval;
    private int maxAutoPotViolations;
    private long minBucketInterval;
    private int maxFastBucketViolations;

    // ItemConsume
    private long minPotionConsumeInterval;
    private int maxFastPotionViolations;
    private long minMilkInterval;
    private int maxFastMilkViolations;
    private long minHoneyInterval;
    private int maxFastHoneyViolations;

    // BlockBreak
    private double maxBreakReach;
    private int maxBreakReachViolations;
    private long minOreBreakInterval;
    private int maxFastOreViolations;
    private int maxBreakWhileMovingSpeed;
    private int maxBreakWhileMovingViolations;

    // BlockPlace
    private double maxPlaceReach;
    private int maxPlaceReachViolations;
    private long minFastPlaceInterval;
    private int maxFastPlaceViolations;

    // InventoryClick / ItemHeld
    private long minShiftClickInterval;
    private int maxFastShiftClickViolations;
    private long minHotbarSwapInterval;
    private int maxFastHotbarSwapViolations;

    // SneakSpam
    private long minSneakToggleInterval;
    private int maxSneakSpamViolations;

    // New-event handlers
    private long minDropInterval;
    private int maxFastDropViolations;
    private long minPickupInterval;
    private int maxFastPickupViolations;
    private long minProjectileInterval;
    private int maxFastProjectileViolations;
    private long minSprintToggleInterval;
    private int maxSprintSpamViolations;
    private long minFlightToggleInterval;
    private int maxFlightSpamViolations;
    private long minGlideToggleInterval;
    private int maxGlideSpamViolations;
    private long minSwimToggleInterval;
    private int maxSwimSpamViolations;
    private long minSwapInterval;
    private int maxSwapSpamViolations;
    private int maxNoHungerViolations;
    private long minShearInterval;
    private int maxFastShearViolations;
    private long minBucketEmptyInterval;
    private int maxFastBucketEmptyViolations;
    private long minBucketFillInterval;
    private int maxFastBucketFillViolations;
    private long minBedInterval;
    private int maxBedSpamViolations;
    private long minTotemInterval;
    private int maxAutoTotemViolations;
    private long minExpInterval;
    private int maxFastExpViolations;
    private long minChatInterval;
    private int maxChatSpamViolations;
    private long minCommandInterval;
    private int maxCommandSpamViolations;
    private long minEggInterval;
    private int maxFastEggViolations;

    @Override
    public void onEnable() {
        // 保存默认配置文件
        saveDefaultConfig();
        // 加载配置
        loadConfigValues();

        // 注册事件监听器
        Bukkit.getServer().getPluginManager().registerEvents(this, this);

        // 注册命令（Paper 1.20.5+ 新 Brigadier 命令 API）
        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            Commands commands = event.registrar();
            LiteralCommandNode<io.papermc.paper.command.brigadier.CommandSourceStack> node = buildAntiBitchCommand();
            commands.register(node, java.util.List.of("ab", "ac"));
        });

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
        lastBlockBreakTime.clear();
        blockBreakCount.clear();
        nukerViolations.clear();
        fastBreakViolations.clear();
        lastEatConsumeTime.clear();
        fastEatViolations.clear();
        waterWalkViolations.clear();
        glideViolations.clear();
        stepViolations.clear();
        spiderViolations.clear();
        noWebViolations.clear();
        fastLadderViolations.clear();
        antiKnockbackViolations.clear();
        lastSwingTime.clear();
        noSwingViolations.clear();
        boatAirMoves.clear();
        boatFlyViolations.clear();
        highJumpViolations.clear();
        dolphinViolations.clear();
        phaseViolations.clear();
        blinkViolations.clear();
        fastSneakViolations.clear();
        lastMoveYaw.clear();
        derpViolations.clear();
        elytraSpeedViolations.clear();
        lastOreTime.clear();
        oreCount.clear();
        xrayViolations.clear();
        lastTowerTime.clear();
        towerViolations.clear();
        lastChestClickTime.clear();
        chestClickCount.clear();
        chestStealerViolations.clear();
        lastArmorEquipTime.clear();
        autoArmorViolations.clear();
        lastFishTime.clear();
        fishCount.clear();
        autoFishViolations.clear();
        fastSprintViolations.clear(); iceSpeedViolations.clear(); bhopViolations.clear();
        lastGroundTime.clear(); airJumpViolations.clear(); jetpackViolations.clear();
        fastClimbVineViolations.clear(); fastDescendViolations.clear(); strafeViolations.clear();
        floatViolations.clear(); fastSneakAirViolations.clear(); lastMovePitch.clear();
        headRollViolations.clear(); teleportUpViolations.clear();
        reachVerticalViolations.clear(); attackThroughWallViolations.clear(); criticalFakeViolations.clear();
        lastAttackEnd.clear(); triggerBotViolations.clear(); lastAttackStart.clear();
        noCooldownViolations.clear(); lastMultiAttackTime.clear(); multiAttackCount.clear(); multiAttackViolations.clear();
        preAttackYaw.clear(); snapAimViolations.clear(); attackWhileSprintingViolations.clear();
        lastInteractTime.clear(); fastInteractViolations.clear();
        lastDoorTime.clear(); fastDoorViolations.clear();
        lastFenceGateTime.clear(); fastFenceGateViolations.clear();
        lastLeverTime.clear(); fastLeverViolations.clear();
        lastButtonTime.clear(); fastButtonViolations.clear();
        lastTrapdoorTime.clear(); fastTrapdoorViolations.clear();
        lastPotionTime.clear(); autoPotViolations.clear();
        lastBucketTime.clear(); fastBucketViolations.clear();
        lastPotionConsumeTime.clear(); fastPotionViolations.clear();
        lastMilkTime.clear(); fastMilkViolations.clear();
        lastHoneyTime.clear(); fastHoneyViolations.clear();
        breakReachViolations.clear(); lastOreBreakTime.clear(); fastOreViolations.clear(); breakWhileMovingViolations.clear();
        placeReachViolations.clear(); lastGeneralPlaceTime.clear(); fastPlaceViolations.clear();
        lastShiftClickTime.clear(); fastShiftClickViolations.clear();
        lastHotbarSwapTime.clear(); fastHotbarSwapViolations.clear();
        lastSneakToggleTime.clear(); sneakSpamViolations.clear();
        lastDropTime.clear(); fastDropViolations.clear();
        lastPickupTime.clear(); fastPickupViolations.clear();
        lastProjectileTime.clear(); fastProjectileViolations.clear();
        lastSprintToggleTime.clear(); sprintSpamViolations.clear();
        lastFlightToggleTime.clear(); flightSpamViolations.clear();
        lastGlideToggleTime.clear(); glideSpamViolations.clear();
        lastSwimToggleTime.clear(); swimSpamViolations.clear();
        lastSwapTime.clear(); swapSpamViolations.clear();
        noHungerViolations.clear();
        lastShearTime.clear(); fastShearViolations.clear();
        lastBucketEmptyTime.clear(); fastBucketEmptyViolations.clear();
        lastBucketFillTime.clear(); fastBucketFillViolations.clear();
        lastBedTime.clear(); bedSpamViolations.clear();
        lastTotemTime.clear(); autoTotemViolations.clear();
        lastExpTime.clear(); fastExpViolations.clear();
        lastChatTime.clear(); chatSpamViolations.clear();
        lastCommandTime.clear(); commandSpamViolations.clear();
        lastEggTime.clear(); fastEggViolations.clear();
        disabledPlayers.clear();
        enabledPlayers.clear();
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
        maxNukerBlocks = config.getInt("nuker.max-blocks-per-second", 15);
        maxNukerViolations = config.getInt("nuker.max-violations", 5);
        minBreakInterval = config.getInt("fastbreak.min-break-interval", 50);
        maxFastBreakViolations = config.getInt("fastbreak.max-violations", 5);
        minEatInterval = config.getInt("fasteat.min-eat-interval", 1500);
        maxFastEatViolations = config.getInt("fasteat.max-violations", 5);
        maxWaterWalkViolations = config.getInt("waterwalk.max-violations", 5);
        maxGlideFallSpeed = config.getDouble("glide.max-fall-speed", 0.2);
        maxGlideViolations = config.getInt("glide.max-violations", 5);
        maxStepHeight = config.getDouble("step.max-height", 0.6);
        maxStepViolations = config.getInt("step.max-violations", 5);
        minKnockbackRatio = config.getDouble("antiknockback.min-ratio", 0.5);
        maxAntiKnockbackViolations = config.getInt("antiknockback.max-violations", 5);
        swingWindowMs = config.getLong("noswing.swing-window-ms", 500);
        maxNoSwingViolations = config.getInt("noswing.max-violations", 5);
        maxSpiderViolations = config.getInt("spider.max-violations", 5);
        maxNoWebSpeed = config.getInt("noweb.max-speed", 5);
        maxNoWebViolations = config.getInt("noweb.max-violations", 5);
        maxFastLadderSpeed = config.getInt("fastladder.max-speed", 10);
        maxFastLadderViolations = config.getInt("fastladder.max-violations", 5);
        maxBoatAirMoves = config.getInt("boatfly.max-air-moves", 5);
        maxBoatFlyViolations = config.getInt("boatfly.max-violations", 5);
        minHighJumpHeight = config.getDouble("highjump.min-height", 1.0);
        maxHighJumpViolations = config.getInt("highjump.max-violations", 5);
        maxSwimSpeed = config.getInt("dolphin.max-swim-speed", 12);
        maxDolphinViolations = config.getInt("dolphin.max-violations", 5);
        maxPhaseDistance = config.getDouble("phase.max-distance", 1.5);
        maxPhaseViolations = config.getInt("phase.max-violations", 5);
        maxBlinkDistance = config.getDouble("blink.max-distance", 5.0);
        maxBlinkViolations = config.getInt("blink.max-violations", 5);
        maxSneakSpeed = config.getInt("fastsneak.max-speed", 3);
        maxFastSneakViolations = config.getInt("fastsneak.max-violations", 5);
        maxDerpYawChange = config.getDouble("derp.max-yaw-change", 180.0);
        maxDerpViolations = config.getInt("derp.max-violations", 5);
        maxElytraSpeed = config.getInt("elytraspeed.max-speed", 30);
        maxElytraSpeedViolations = config.getInt("elytraspeed.max-violations", 5);
        maxOresPerMinute = config.getInt("xray.max-ores-per-minute", 10);
        maxXrayViolations = config.getInt("xray.max-violations", 5);
        minTowerInterval = config.getLong("tower.min-interval", 200);
        maxTowerViolations = config.getInt("tower.max-violations", 5);
        maxChestClicksPerSecond = config.getInt("cheststealer.max-clicks-per-second", 20);
        maxChestStealerViolations = config.getInt("cheststealer.max-violations", 5);
        minArmorEquipInterval = config.getLong("autoarmor.min-equip-interval", 100);
        maxAutoArmorViolations = config.getInt("autoarmor.max-violations", 5);
        maxFishPerMinute = config.getInt("autofish.max-fish-per-minute", 10);
        maxAutoFishViolations = config.getInt("autofish.max-violations", 5);
        // 第二批扩展
        maxFastSprintSpeed = config.getInt("fastsprint.max-speed", 14);
        maxFastSprintViolations = config.getInt("fastsprint.max-violations", 5);
        maxIceSpeed = config.getInt("icespeed.max-speed", 16);
        maxIceSpeedViolations = config.getInt("icespeed.max-violations", 5);
        maxBhopJumps = config.getInt("bhop.max-jumps", 5);
        maxBhopViolations = config.getInt("bhop.max-violations", 5);
        maxAirJumpViolations = config.getInt("airjump.max-violations", 5);
        minJetpackUp = config.getDouble("jetpack.min-up", 0.3);
        maxJetpackViolations = config.getInt("jetpack.max-violations", 5);
        maxFastClimbVineSpeed = config.getInt("fastclimbvine.max-speed", 5);
        maxFastClimbVineViolations = config.getInt("fastclimbvine.max-violations", 5);
        maxFastDescendSpeed = config.getDouble("fastdescend.max-speed", -0.6);
        maxFastDescendViolations = config.getInt("fastdescend.max-violations", 5);
        maxStrafeViolations = config.getInt("strafe.max-violations", 5);
        maxFloatViolations = config.getInt("float.max-violations", 5);
        maxFastSneakAirViolations = config.getInt("fastsneakair.max-violations", 5);
        maxHeadRollChange = config.getDouble("headroll.max-change", 60.0);
        maxHeadRollViolations = config.getInt("headroll.max-violations", 5);
        maxTeleportUp = config.getDouble("teleportup.max-distance", 2.0);
        maxTeleportUpViolations = config.getInt("teleportup.max-violations", 5);
        maxReachVertical = config.getDouble("reachvertical.max-distance", 2.0);
        maxReachVerticalViolations = config.getInt("reachvertical.max-violations", 5);
        maxAttackThroughWallViolations = config.getInt("attackthroughwall.max-violations", 5);
        maxCriticalFakeViolations = config.getInt("criticalfake.max-violations", 5);
        maxTriggerBotViolations = config.getInt("triggerbot.max-violations", 5);
        minAttackCooldown = config.getLong("nocooldown.min-cooldown", 400);
        maxNoCooldownViolations = config.getInt("nocooldown.max-violations", 5);
        maxMultiAttackCount = config.getInt("multiattack.max-count", 3);
        maxMultiAttackViolations = config.getInt("multiattack.max-violations", 5);
        maxSnapAimChange = config.getDouble("snapaim.max-change", 120.0);
        maxSnapAimViolations = config.getInt("snapaim.max-violations", 5);
        maxAttackWhileSprintingViolations = config.getInt("attackwhilesprinting.max-violations", 5);
        minInteractInterval = config.getLong("fastinteract.min-interval", 50);
        maxFastInteractViolations = config.getInt("fastinteract.max-violations", 5);
        minDoorInterval = config.getLong("fastdoor.min-interval", 200);
        maxFastDoorViolations = config.getInt("fastdoor.max-violations", 5);
        minFenceGateInterval = config.getLong("fastfencegate.min-interval", 200);
        maxFastFenceGateViolations = config.getInt("fastfencegate.max-violations", 5);
        minLeverInterval = config.getLong("fastlever.min-interval", 200);
        maxFastLeverViolations = config.getInt("fastlever.max-violations", 5);
        minButtonInterval = config.getLong("fastbutton.min-interval", 200);
        maxFastButtonViolations = config.getInt("fastbutton.max-violations", 5);
        minTrapdoorInterval = config.getLong("fasttrapdoor.min-interval", 200);
        maxFastTrapdoorViolations = config.getInt("fasttrapdoor.max-violations", 5);
        minPotionInterval = config.getLong("autopot.min-interval", 300);
        maxAutoPotViolations = config.getInt("autopot.max-violations", 5);
        minBucketInterval = config.getLong("fastbucket.min-interval", 200);
        maxFastBucketViolations = config.getInt("fastbucket.max-violations", 5);
        minPotionConsumeInterval = config.getLong("fastpotion.min-interval", 1500);
        maxFastPotionViolations = config.getInt("fastpotion.max-violations", 5);
        minMilkInterval = config.getLong("fastmilk.min-interval", 2000);
        maxFastMilkViolations = config.getInt("fastmilk.max-violations", 5);
        minHoneyInterval = config.getLong("fasthoney.min-interval", 2000);
        maxFastHoneyViolations = config.getInt("fasthoney.max-violations", 5);
        maxBreakReach = config.getDouble("breakreach.max-distance", 5.0);
        maxBreakReachViolations = config.getInt("breakreach.max-violations", 5);
        minOreBreakInterval = config.getLong("fastore.min-interval", 200);
        maxFastOreViolations = config.getInt("fastore.max-violations", 5);
        maxBreakWhileMovingSpeed = config.getInt("breakwhilemoving.max-speed", 10);
        maxBreakWhileMovingViolations = config.getInt("breakwhilemoving.max-violations", 5);
        maxPlaceReach = config.getDouble("placereach.max-distance", 5.0);
        maxPlaceReachViolations = config.getInt("placereach.max-violations", 5);
        minFastPlaceInterval = config.getLong("fastplace.min-interval", 50);
        maxFastPlaceViolations = config.getInt("fastplace.max-violations", 5);
        minShiftClickInterval = config.getLong("fastshiftclick.min-interval", 50);
        maxFastShiftClickViolations = config.getInt("fastshiftclick.max-violations", 5);
        minHotbarSwapInterval = config.getLong("fasthotbarswap.min-interval", 50);
        maxFastHotbarSwapViolations = config.getInt("fasthotbarswap.max-violations", 5);
        minSneakToggleInterval = config.getLong("sneakspam.min-interval", 100);
        maxSneakSpamViolations = config.getInt("sneakspam.max-violations", 5);
        minDropInterval = config.getLong("fastdrop.min-interval", 100);
        maxFastDropViolations = config.getInt("fastdrop.max-violations", 5);
        minPickupInterval = config.getLong("fastpickup.min-interval", 100);
        maxFastPickupViolations = config.getInt("fastpickup.max-violations", 5);
        minProjectileInterval = config.getLong("fastprojectile.min-interval", 400);
        maxFastProjectileViolations = config.getInt("fastprojectile.max-violations", 5);
        minSprintToggleInterval = config.getLong("sprintspam.min-interval", 100);
        maxSprintSpamViolations = config.getInt("sprintspam.max-violations", 5);
        minFlightToggleInterval = config.getLong("flightspam.min-interval", 100);
        maxFlightSpamViolations = config.getInt("flightspam.max-violations", 5);
        minGlideToggleInterval = config.getLong("glidespam.min-interval", 100);
        maxGlideSpamViolations = config.getInt("glidespam.max-violations", 5);
        minSwimToggleInterval = config.getLong("swimspam.min-interval", 100);
        maxSwimSpamViolations = config.getInt("swimspam.max-violations", 5);
        minSwapInterval = config.getLong("swapspam.min-interval", 100);
        maxSwapSpamViolations = config.getInt("swapspam.max-violations", 5);
        maxNoHungerViolations = config.getInt("nohunger.max-violations", 5);
        minShearInterval = config.getLong("fastshear.min-interval", 200);
        maxFastShearViolations = config.getInt("fastshear.max-violations", 5);
        minBucketEmptyInterval = config.getLong("fastbucketempty.min-interval", 200);
        maxFastBucketEmptyViolations = config.getInt("fastbucketempty.max-violations", 5);
        minBucketFillInterval = config.getLong("fastbucketfill.min-interval", 200);
        maxFastBucketFillViolations = config.getInt("fastbucketfill.max-violations", 5);
        minBedInterval = config.getLong("bedspam.min-interval", 1000);
        maxBedSpamViolations = config.getInt("bedspam.max-violations", 5);
        minTotemInterval = config.getLong("autototem.min-interval", 500);
        maxAutoTotemViolations = config.getInt("autototem.max-violations", 5);
        minExpInterval = config.getLong("fastexp.min-interval", 100);
        maxFastExpViolations = config.getInt("fastexp.max-violations", 5);
        minChatInterval = config.getLong("chatspam.min-interval", 1000);
        maxChatSpamViolations = config.getInt("chatspam.max-violations", 5);
        minCommandInterval = config.getLong("commandspam.min-interval", 500);
        maxCommandSpamViolations = config.getInt("commandspam.max-violations", 5);
        minEggInterval = config.getLong("fastegg.min-interval", 400);
        maxFastEggViolations = config.getInt("fastegg.max-violations", 5);
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

    private Map<UUID, Integer> newViolationMap(String checkName) {
        return new ConcurrentHashMap<UUID, Integer>() {
            @Override
            public Integer put(UUID playerId, Integer violations) {
                Integer previous = super.put(playerId, violations);
                String overrideCheckName = nextViolationCheckName.get();
                notifyViolation(overrideCheckName != null ? overrideCheckName : checkName, playerId, previous, violations);
                return previous;
            }
        };
    }

    private void notifyViolation(String checkName, UUID playerId, Integer previous, Integer violations) {
        if (violations == null || violations <= 0 || (previous != null && violations <= previous)) {
            return;
        }

        Runnable notifyTask = () -> {
            Player target = Bukkit.getPlayer(playerId);
            String playerName = target != null ? target.getName() : playerId.toString();
            String displayCheckName = formatCheckName(checkName);
            String message = ChatColor.RED + "[AntiBitch] " + ChatColor.YELLOW + playerName
                    + ChatColor.RED + " 触发 " + ChatColor.GOLD + displayCheckName
                    + ChatColor.RED + " 检测 VL " + ChatColor.YELLOW + violations;

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (onlinePlayer.hasPermission("antibitch.admin")) {
                    onlinePlayer.sendMessage(message);
                }
            }
        };

        if (Bukkit.isPrimaryThread()) {
            notifyTask.run();
        } else {
            Bukkit.getScheduler().runTask(this, notifyTask);
        }
    }

    private String formatCheckName(String checkName) {
        if (checkName == null || checkName.isEmpty()) {
            return "Unknown";
        }
        return Character.toUpperCase(checkName.charAt(0)) + checkName.substring(1);
    }

    private void punish(Player player, String kickMessage) {
        if (!Bukkit.isPrimaryThread()) {
            Bukkit.getScheduler().runTask(this, () -> punish(player, kickMessage));
            return;
        }

        String reason = kickMessage != null ? kickMessage : "检测到作弊行为";
        Bukkit.broadcastMessage(ChatColor.RED + "[AntiBitch] 玩家 " + ChatColor.YELLOW + player.getName()
                + ChatColor.RED + " 因作弊检测被踢出: " + ChatColor.YELLOW + reason);
        player.kickPlayer(kickMessage);
    }

    private void putViolation(Map<UUID, Integer> violationsMap, UUID playerId, int violations, String checkName) {
        nextViolationCheckName.set(checkName);
        try {
            violationsMap.put(playerId, violations);
        } finally {
            nextViolationCheckName.remove();
        }
    }

    /**
     * 检查插件全局开关是否启用
     *
     * @return 全局启用时返回 true
     */
    private boolean isGloballyEnabled() {
        return getConfig().getBoolean("settings.enabled", true);
    }

    /**
     * 检查指定玩家是否应进行检测。
     * 全局开时默认所有玩家检测，disabledPlayers 中的玩家被显式禁用；
     * 全局关时默认所有玩家不检测，enabledPlayers 中的玩家被显式启用。
     *
     * @param player 待检测的玩家
     * @return 该玩家应被检测时返回 true
     */
    private boolean isDetectionEnabled(Player player) {
        UUID id = player.getUniqueId();
        return isGloballyEnabled() ? !disabledPlayers.contains(id) : enabledPlayers.contains(id);
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
        lastBlockBreakTime.remove(playerId);
        blockBreakCount.remove(playerId);
        nukerViolations.remove(playerId);
        fastBreakViolations.remove(playerId);
        lastEatConsumeTime.remove(playerId);
        fastEatViolations.remove(playerId);
        waterWalkViolations.remove(playerId);
        glideViolations.remove(playerId);
        stepViolations.remove(playerId);
        spiderViolations.remove(playerId);
        noWebViolations.remove(playerId);
        fastLadderViolations.remove(playerId);
        antiKnockbackViolations.remove(playerId);
        lastSwingTime.remove(playerId);
        noSwingViolations.remove(playerId);
        boatAirMoves.remove(playerId);
        boatFlyViolations.remove(playerId);
        highJumpViolations.remove(playerId);
        dolphinViolations.remove(playerId);
        phaseViolations.remove(playerId);
        blinkViolations.remove(playerId);
        fastSneakViolations.remove(playerId);
        lastMoveYaw.remove(playerId);
        derpViolations.remove(playerId);
        elytraSpeedViolations.remove(playerId);
        lastOreTime.remove(playerId);
        oreCount.remove(playerId);
        xrayViolations.remove(playerId);
        lastTowerTime.remove(playerId);
        towerViolations.remove(playerId);
        lastChestClickTime.remove(playerId);
        chestClickCount.remove(playerId);
        chestStealerViolations.remove(playerId);
        lastArmorEquipTime.remove(playerId);
        autoArmorViolations.remove(playerId);
        lastFishTime.remove(playerId);
        fishCount.remove(playerId);
        autoFishViolations.remove(playerId);
        fastSprintViolations.remove(playerId); iceSpeedViolations.remove(playerId); bhopViolations.remove(playerId);
        lastGroundTime.remove(playerId); airJumpViolations.remove(playerId); jetpackViolations.remove(playerId);
        fastClimbVineViolations.remove(playerId); fastDescendViolations.remove(playerId); strafeViolations.remove(playerId);
        floatViolations.remove(playerId); fastSneakAirViolations.remove(playerId); lastMovePitch.remove(playerId);
        headRollViolations.remove(playerId); teleportUpViolations.remove(playerId);
        reachVerticalViolations.remove(playerId); attackThroughWallViolations.remove(playerId); criticalFakeViolations.remove(playerId);
        lastAttackEnd.remove(playerId); triggerBotViolations.remove(playerId); lastAttackStart.remove(playerId);
        noCooldownViolations.remove(playerId); lastMultiAttackTime.remove(playerId); multiAttackCount.remove(playerId); multiAttackViolations.remove(playerId);
        preAttackYaw.remove(playerId); snapAimViolations.remove(playerId); attackWhileSprintingViolations.remove(playerId);
        lastInteractTime.remove(playerId); fastInteractViolations.remove(playerId);
        lastDoorTime.remove(playerId); fastDoorViolations.remove(playerId);
        lastFenceGateTime.remove(playerId); fastFenceGateViolations.remove(playerId);
        lastLeverTime.remove(playerId); fastLeverViolations.remove(playerId);
        lastButtonTime.remove(playerId); fastButtonViolations.remove(playerId);
        lastTrapdoorTime.remove(playerId); fastTrapdoorViolations.remove(playerId);
        lastPotionTime.remove(playerId); autoPotViolations.remove(playerId);
        lastBucketTime.remove(playerId); fastBucketViolations.remove(playerId);
        lastPotionConsumeTime.remove(playerId); fastPotionViolations.remove(playerId);
        lastMilkTime.remove(playerId); fastMilkViolations.remove(playerId);
        lastHoneyTime.remove(playerId); fastHoneyViolations.remove(playerId);
        breakReachViolations.remove(playerId); lastOreBreakTime.remove(playerId); fastOreViolations.remove(playerId); breakWhileMovingViolations.remove(playerId);
        placeReachViolations.remove(playerId); lastGeneralPlaceTime.remove(playerId); fastPlaceViolations.remove(playerId);
        lastShiftClickTime.remove(playerId); fastShiftClickViolations.remove(playerId);
        lastHotbarSwapTime.remove(playerId); fastHotbarSwapViolations.remove(playerId);
        lastSneakToggleTime.remove(playerId); sneakSpamViolations.remove(playerId);
        lastDropTime.remove(playerId); fastDropViolations.remove(playerId);
        lastPickupTime.remove(playerId); fastPickupViolations.remove(playerId);
        lastProjectileTime.remove(playerId); fastProjectileViolations.remove(playerId);
        lastSprintToggleTime.remove(playerId); sprintSpamViolations.remove(playerId);
        lastFlightToggleTime.remove(playerId); flightSpamViolations.remove(playerId);
        lastGlideToggleTime.remove(playerId); glideSpamViolations.remove(playerId);
        lastSwimToggleTime.remove(playerId); swimSpamViolations.remove(playerId);
        lastSwapTime.remove(playerId); swapSpamViolations.remove(playerId);
        noHungerViolations.remove(playerId);
        lastShearTime.remove(playerId); fastShearViolations.remove(playerId);
        lastBucketEmptyTime.remove(playerId); fastBucketEmptyViolations.remove(playerId);
        lastBucketFillTime.remove(playerId); fastBucketFillViolations.remove(playerId);
        lastBedTime.remove(playerId); bedSpamViolations.remove(playerId);
        lastTotemTime.remove(playerId); autoTotemViolations.remove(playerId);
        lastExpTime.remove(playerId); fastExpViolations.remove(playerId);
        lastChatTime.remove(playerId); chatSpamViolations.remove(playerId);
        lastCommandTime.remove(playerId); commandSpamViolations.remove(playerId);
        lastEggTime.remove(playerId); fastEggViolations.remove(playerId);
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
        if (!isDetectionEnabled(player)) return;
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
                        punish(player, kickMessage);
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
                    punish(player, kickMessage);
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
                    punish(player, kickMessage);
                    reachViolations.remove(playerId);
                    getLogger().info("玩家 " + player.getName() + " 因 Reach 作弊被踢出");
                    return;
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
                    putViolation(reachViolations, playerId, violations, "killAura");

                    if (violations >= maxKillAuraViolations) {
                        String kickMessage = getConfig().getString("killaura.kick-message", "检测到异常攻击频率");
                        punish(player, kickMessage);
                        getLogger().info("玩家 " + player.getName() + " 因 KillAura 作弊被踢出");
                        return;
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
                        punish(player, kickMessage);
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
                    putViolation(reachViolations, playerId, violations, "autoClicker");

                    if (violations >= maxAutoClickerViolations) {
                        String kickMessage = getConfig().getString("autoclicker.kick-message", "检测到异常点击速度");
                        punish(player, kickMessage);
                        getLogger().info("玩家 " + player.getName() + " 因 AutoClicker 作弊被踢出");
                        return;
                    }
                }
            }
        }

        // NoSwing 检测（娱乐性质：检测攻击时无挥动动画）
        if (getConfig().getBoolean("noswing.enabled", true)) {
            long now = System.currentTimeMillis();
            Long lastSwing = lastSwingTime.get(playerId);
            // 如果近期没有挥动记录，可能是 NoSwing
            if (lastSwing == null || now - lastSwing > swingWindowMs) {
                int violations = noSwingViolations.getOrDefault(playerId, 0) + 1;
                noSwingViolations.put(playerId, violations);

                if (violations >= maxNoSwingViolations) {
                    String kickMessage = getConfig().getString("noswing.kick-message", "检测到攻击无挥动");
                    punish(player, kickMessage);
                    getLogger().info("玩家 " + player.getName() + " 因 NoSwing 作弊被踢出");
                    return;
                }
            }
        }

        // ReachVertical 检测（娱乐性质：纵向攻击距离过大）
        if (getConfig().getBoolean("reachvertical.enabled", true)) {
            double vDist = Math.abs(player.getLocation().getY() - target.getLocation().getY());
            if (vDist > maxReachVertical) {
                int violations = reachVerticalViolations.getOrDefault(playerId, 0) + 1;
                reachVerticalViolations.put(playerId, violations);
                if (violations >= maxReachVerticalViolations) {
                    punish(player, getConfig().getString("reachvertical.kick-message", "检测到纵向攻击距离过大"));
                    getLogger().info("玩家 " + player.getName() + " 因 ReachVertical 作弊被踢出");
                    return;
                }
            }
        }

        // AttackThroughWall 检测（娱乐性质：隔着实心方块攻击）
        if (getConfig().getBoolean("attackthroughwall.enabled", true)) {
            org.bukkit.util.Vector dir = target.getLocation().toVector().subtract(player.getEyeLocation().toVector());
            double len = dir.length();
            if (len > 0.01) {
                dir.multiply(1.0 / len);
                org.bukkit.util.Vector eye = player.getEyeLocation().toVector().clone();
                org.bukkit.World world = player.getWorld();
                int steps = (int) Math.min(len, 5);
                boolean blocked = false;
                for (int i = 1; i <= steps; i++) {
                    org.bukkit.Location probe = new org.bukkit.Location(world, eye.getX() + dir.getX() * i, eye.getY() + dir.getY() * i, eye.getZ() + dir.getZ() * i);
                    if (probe.getBlock().getType().isSolid()) { blocked = true; break; }
                }
                if (blocked) {
                    int violations = attackThroughWallViolations.getOrDefault(playerId, 0) + 1;
                    attackThroughWallViolations.put(playerId, violations);
                    if (violations >= maxAttackThroughWallViolations) {
                        punish(player, getConfig().getString("attackthroughwall.kick-message", "检测到隔墙攻击"));
                        getLogger().info("玩家 " + player.getName() + " 因 AttackThroughWall 作弊被踢出");
                        return;
                    }
                }
            }
        }

        // CriticalFake 检测（娱乐性质：地面攻击却标记暴击）
        if (getConfig().getBoolean("criticalfake.enabled", true)
                && player.isOnGround() && event.getDamage() > 0) {
            int violations = criticalFakeViolations.getOrDefault(playerId, 0) + 1;
            criticalFakeViolations.put(playerId, violations);
            if (violations >= maxCriticalFakeViolations) {
                punish(player, getConfig().getString("criticalfake.kick-message", "检测到伪造暴击"));
                getLogger().info("玩家 " + player.getName() + " 因 CriticalFake 作弊被踢出");
                return;
            }
        }

        // NoCooldown 检测（娱乐性质：攻击间隔短于冷却）
        if (getConfig().getBoolean("nocooldown.enabled", true)) {
            long now2 = System.currentTimeMillis();
            Long lastStart = lastAttackStart.get(playerId);
            if (lastStart != null && now2 - lastStart < minAttackCooldown) {
                int violations = noCooldownViolations.getOrDefault(playerId, 0) + 1;
                noCooldownViolations.put(playerId, violations);
                if (violations >= maxNoCooldownViolations) {
                    punish(player, getConfig().getString("nocooldown.kick-message", "检测到攻击无冷却"));
                    getLogger().info("玩家 " + player.getName() + " 因 NoCooldown 作弊被踢出");
                    return;
                }
            }
            lastAttackStart.put(playerId, now2);
        }

        // TriggerBot 检测（娱乐性质：冷却结束瞬间精准攻击）
        if (getConfig().getBoolean("triggerbot.enabled", true)) {
            Long lastEnd = lastAttackEnd.get(playerId);
            long now2 = System.currentTimeMillis();
            if (lastEnd != null && now2 - lastEnd >= minAttackCooldown && now2 - lastEnd < minAttackCooldown + 50) {
                int violations = triggerBotViolations.getOrDefault(playerId, 0) + 1;
                triggerBotViolations.put(playerId, violations);
                if (violations >= maxTriggerBotViolations) {
                    punish(player, getConfig().getString("triggerbot.kick-message", "检测到精准冷却攻击"));
                    getLogger().info("玩家 " + player.getName() + " 因 TriggerBot 作弊被踢出");
                    return;
                }
            }
            lastAttackEnd.put(playerId, now2);
        }

        // MultiAttack 检测（娱乐性质：短窗口内命中多个实体）
        if (getConfig().getBoolean("multiattack.enabled", true)) {
            long now2 = System.currentTimeMillis();
            long lastMulti = lastMultiAttackTime.getOrDefault(playerId, now2 - 1000);
            int cnt = multiAttackCount.getOrDefault(playerId, 0);
            if (now2 - lastMulti > 1000) {
                multiAttackCount.put(playerId, 1);
            } else {
                multiAttackCount.put(playerId, cnt + 1);
                if (cnt + 1 > maxMultiAttackCount) {
                    int violations = multiAttackViolations.getOrDefault(playerId, 0) + 1;
                    multiAttackViolations.put(playerId, violations);
                    if (violations >= maxMultiAttackViolations) {
                        punish(player, getConfig().getString("multiattack.kick-message", "检测到多实体攻击"));
                        getLogger().info("玩家 " + player.getName() + " 因 MultiAttack 作弊被踢出");
                        return;
                    }
                }
            }
            lastMultiAttackTime.put(playerId, now2);
        }

        // SnapAim 检测（娱乐性质：攻击瞬间朝向突变）
        if (getConfig().getBoolean("snapaim.enabled", true)) {
            float curYaw = player.getLocation().getYaw();
            Float pre = preAttackYaw.get(playerId);
            if (pre != null) {
                float diff = Math.abs(curYaw - pre);
                if (diff > 180) diff = 360 - diff;
                if (diff > maxSnapAimChange) {
                    int violations = snapAimViolations.getOrDefault(playerId, 0) + 1;
                    snapAimViolations.put(playerId, violations);
                    if (violations >= maxSnapAimViolations) {
                        punish(player, getConfig().getString("snapaim.kick-message", "检测到攻击瞬间朝向突变"));
                        getLogger().info("玩家 " + player.getName() + " 因 SnapAim 作弊被踢出");
                        return;
                    }
                }
            }
            preAttackYaw.put(playerId, curYaw);
        }

        // AttackWhileSprinting 检测（娱乐性质：疾跑攻击未减速）
        if (getConfig().getBoolean("attackwhilesprinting.enabled", true)
                && player.isSprinting()) {
            int violations = attackWhileSprintingViolations.getOrDefault(playerId, 0) + 1;
            attackWhileSprintingViolations.put(playerId, violations);
            if (violations >= maxAttackWhileSprintingViolations) {
                punish(player, getConfig().getString("attackwhilesprinting.kick-message", "检测到疾跑攻击"));
                getLogger().info("玩家 " + player.getName() + " 因 AttackWhileSprinting 作弊被踢出");
                return;
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
        if (!isDetectionEnabled(player)) return;

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
            punish(player, kickMessage);
            getLogger().info("玩家 " + player.getName() + " 因 Speed 作弊被踢出 (速度: " + speed + ")");
        } else {
            moveTimes.put(playerId, now);
            int count = moveCount.getOrDefault(playerId, 0) + 1;
            moveCount.put(playerId, count);

            if (count > maxAirMoves) {
                String kickMessage = getConfig().getString("fly.kick-message", "此服务器未启用飞行功能");
                punish(player, kickMessage);
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
                    punish(player, kickMessage);
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
                        punish(player, kickMessage);
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
                    punish(player, kickMessage);
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
        if (!isDetectionEnabled(player)) return;

        // NoFall 检测
        if (getConfig().getBoolean("nofall.enabled", true) && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            Double fallDistance = lastFallHeight.get(playerId);
            if (fallDistance != null && fallDistance > noFallThreshold) {
                // 如果玩家从高处落下但没有受到伤害，可能是 NoFall
                if (event.getDamage() == 0) {
                    String kickMessage = getConfig().getString("nofall.kick-message", "检测到无坠落伤害");
                    punish(player, kickMessage);
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
        if (!isDetectionEnabled(player)) return;

        // Regen 检测
        if (getConfig().getBoolean("regen.enabled", true)) {
            long now = System.currentTimeMillis();
            long lastHeal = lastHealTime.getOrDefault(playerId, now - minHealInterval);

            if (now - lastHeal < minHealInterval) {
                int violations = regenViolations.getOrDefault(playerId, 0) + 1;
                regenViolations.put(playerId, violations);

                if (violations >= maxRegenViolations) {
                    String kickMessage = getConfig().getString("regen.kick-message", "检测到快速回血");
                    punish(player, kickMessage);
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
        if (!isDetectionEnabled(player)) return;
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
                            punish(player, kickMessage);
                            getLogger().info("玩家 " + player.getName() + " 因 FastBow 作弊被踢出");
                            return;
                        }
                    }
                }
            }
        }

        long now = System.currentTimeMillis();
        boolean right = event.getAction().name().contains("RIGHT");
        org.bukkit.block.Block cb = event.getClickedBlock();

        // FastInteract 检测（娱乐性质：交互间隔过短）
        if (getConfig().getBoolean("fastinteract.enabled", true) && right) {
            long last = lastInteractTime.getOrDefault(playerId, now - minInteractInterval);
            if (now - last < minInteractInterval) {
                int violations = fastInteractViolations.getOrDefault(playerId, 0) + 1;
                fastInteractViolations.put(playerId, violations);
                if (violations >= maxFastInteractViolations) {
                    punish(player, getConfig().getString("fastinteract.kick-message", "检测到快速交互"));
                    getLogger().info("玩家 " + player.getName() + " 因 FastInteract 作弊被踢出");
                    return;
                }
            }
            lastInteractTime.put(playerId, now);
        }

        // FastDoor / FastFenceGate / FastLever / FastButton / FastTrapdoor 检测
        if (cb != null && right) {
            Material t = cb.getType();
            long lastT;
            if (t == Material.OAK_DOOR || t == Material.IRON_DOOR || t == Material.SPRUCE_DOOR
                    || t == Material.BIRCH_DOOR || t == Material.JUNGLE_DOOR || t == Material.ACACIA_DOOR
                    || t == Material.DARK_OAK_DOOR || t == Material.MANGROVE_DOOR || t == Material.CHERRY_DOOR
                    || t == Material.BAMBOO_DOOR || t == Material.CRIMSON_DOOR || t == Material.WARPED_DOOR) {
                lastT = lastDoorTime.getOrDefault(playerId, now - minDoorInterval);
                if (now - lastT < minDoorInterval) {
                    int v = fastDoorViolations.getOrDefault(playerId, 0) + 1; fastDoorViolations.put(playerId, v);
                    if (v >= maxFastDoorViolations) { punish(player, getConfig().getString("fastdoor.kick-message", "检测到快速门切换")); getLogger().info("玩家 " + player.getName() + " 因 FastDoor 作弊被踢出"); return; }
                }
                lastDoorTime.put(playerId, now);
            } else if (t.name().endsWith("_FENCE_GATE")) {
                lastT = lastFenceGateTime.getOrDefault(playerId, now - minFenceGateInterval);
                if (now - lastT < minFenceGateInterval) {
                    int v = fastFenceGateViolations.getOrDefault(playerId, 0) + 1; fastFenceGateViolations.put(playerId, v);
                    if (v >= maxFastFenceGateViolations) { punish(player, getConfig().getString("fastfencegate.kick-message", "检测到快速栅栏门切换")); getLogger().info("玩家 " + player.getName() + " 因 FastFenceGate 作弊被踢出"); return; }
                }
                lastFenceGateTime.put(playerId, now);
            } else if (t == Material.LEVER) {
                lastT = lastLeverTime.getOrDefault(playerId, now - minLeverInterval);
                if (now - lastT < minLeverInterval) {
                    int v = fastLeverViolations.getOrDefault(playerId, 0) + 1; fastLeverViolations.put(playerId, v);
                    if (v >= maxFastLeverViolations) { punish(player, getConfig().getString("fastlever.kick-message", "检测到快速拉杆切换")); getLogger().info("玩家 " + player.getName() + " 因 FastLever 作弊被踢出"); return; }
                }
                lastLeverTime.put(playerId, now);
            } else if (t.name().endsWith("_BUTTON")) {
                lastT = lastButtonTime.getOrDefault(playerId, now - minButtonInterval);
                if (now - lastT < minButtonInterval) {
                    int v = fastButtonViolations.getOrDefault(playerId, 0) + 1; fastButtonViolations.put(playerId, v);
                    if (v >= maxFastButtonViolations) { punish(player, getConfig().getString("fastbutton.kick-message", "检测到快速按钮切换")); getLogger().info("玩家 " + player.getName() + " 因 FastButton 作弊被踢出"); return; }
                }
                lastButtonTime.put(playerId, now);
            } else if (t.name().endsWith("_TRAPDOOR")) {
                lastT = lastTrapdoorTime.getOrDefault(playerId, now - minTrapdoorInterval);
                if (now - lastT < minTrapdoorInterval) {
                    int v = fastTrapdoorViolations.getOrDefault(playerId, 0) + 1; fastTrapdoorViolations.put(playerId, v);
                    if (v >= maxFastTrapdoorViolations) { punish(player, getConfig().getString("fasttrapdoor.kick-message", "检测到快速活板门切换")); getLogger().info("玩家 " + player.getName() + " 因 FastTrapdoor 作弊被踢出"); return; }
                }
                lastTrapdoorTime.put(playerId, now);
            }
        }

        // AutoPot 检测（娱乐性质：喷溅药水间隔过短）
        if (getConfig().getBoolean("autopot.enabled", true) && item != null
                && (item.getType() == Material.SPLASH_POTION || item.getType() == Material.LINGERING_POTION)) {
            long last = lastPotionTime.getOrDefault(playerId, now - minPotionInterval);
            if (now - last < minPotionInterval) {
                int violations = autoPotViolations.getOrDefault(playerId, 0) + 1;
                autoPotViolations.put(playerId, violations);
                if (violations >= maxAutoPotViolations) {
                    punish(player, getConfig().getString("autopot.kick-message", "检测到自动喷溅药水"));
                    getLogger().info("玩家 " + player.getName() + " 因 AutoPot 作弊被踢出");
                    return;
                }
            }
            lastPotionTime.put(playerId, now);
        }

        // FastBucket 检测（娱乐性质：桶交互间隔过短）
        if (getConfig().getBoolean("fastbucket.enabled", true) && item != null
                && (item.getType() == Material.WATER_BUCKET || item.getType() == Material.LAVA_BUCKET
                || item.getType() == Material.BUCKET)) {
            long last = lastBucketTime.getOrDefault(playerId, now - minBucketInterval);
            if (now - last < minBucketInterval) {
                int violations = fastBucketViolations.getOrDefault(playerId, 0) + 1;
                fastBucketViolations.put(playerId, violations);
                if (violations >= maxFastBucketViolations) {
                    punish(player, getConfig().getString("fastbucket.kick-message", "检测到快速桶使用"));
                    getLogger().info("玩家 " + player.getName() + " 因 FastBucket 作弊被踢出");
                    return;
                }
            }
            lastBucketTime.put(playerId, now);
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
        if (!isDetectionEnabled(player)) return;
        long now = System.currentTimeMillis();

        // Scaffold 检测
        if (getConfig().getBoolean("scaffold.enabled", true)) {
            long lastPlace = lastPlaceTime.getOrDefault(playerId, now - minPlaceInterval);

            if (now - lastPlace < minPlaceInterval) {
                int violations = scaffoldViolations.getOrDefault(playerId, 0) + 1;
                scaffoldViolations.put(playerId, violations);

                if (violations >= maxScaffoldViolations) {
                    String kickMessage = getConfig().getString("scaffold.kick-message", "检测到自动搭桥");
                    punish(player, kickMessage);
                    getLogger().info("玩家 " + player.getName() + " 因 Scaffold 作弊被踢出");
                    return;
                }
            }
            lastPlaceTime.put(playerId, now);
        }

        // Tower 检测（娱乐性质：脚下反复放方块快速垂直上升）
        if (getConfig().getBoolean("tower.enabled", true)) {
            org.bukkit.block.Block placed = event.getBlock();
            org.bukkit.block.Block below = player.getLocation().getBlock().getRelative(org.bukkit.block.BlockFace.DOWN);
            // 放置位置在玩家脚下附近
            if (Math.abs(placed.getX() - below.getX()) <= 1
                    && Math.abs(placed.getZ() - below.getZ()) <= 1
                    && placed.getY() >= below.getY()) {
                long lastT = lastTowerTime.getOrDefault(playerId, now - minTowerInterval);
                if (now - lastT < minTowerInterval) {
                    int violations = towerViolations.getOrDefault(playerId, 0) + 1;
                    towerViolations.put(playerId, violations);

                    if (violations >= maxTowerViolations) {
                        String kickMessage = getConfig().getString("tower.kick-message", "检测到自动搭塔");
                        punish(player, kickMessage);
                        getLogger().info("玩家 " + player.getName() + " 因 Tower 作弊被踢出");
                        return;
                    }
                }
                lastTowerTime.put(playerId, now);
            }
        }

        // PlaceReach 检测（娱乐性质：放置方块距离过远）
        if (getConfig().getBoolean("placereach.enabled", true)) {
            double pdist = player.getEyeLocation().distance(event.getBlock().getLocation().add(0.5, 0.5, 0.5));
            if (pdist > maxPlaceReach) {
                int violations = placeReachViolations.getOrDefault(playerId, 0) + 1;
                placeReachViolations.put(playerId, violations);
                if (violations >= maxPlaceReachViolations) {
                    punish(player, getConfig().getString("placereach.kick-message", "检测到放置方块距离过远"));
                    getLogger().info("玩家 " + player.getName() + " 因 PlaceReach 作弊被踢出");
                    return;
                }
            }
        }

        // FastPlace 检测（娱乐性质：通用快速放置）
        if (getConfig().getBoolean("fastplace.enabled", true)) {
            long last = lastGeneralPlaceTime.getOrDefault(playerId, now - minFastPlaceInterval);
            if (now - last < minFastPlaceInterval) {
                int violations = fastPlaceViolations.getOrDefault(playerId, 0) + 1;
                fastPlaceViolations.put(playerId, violations);
                if (violations >= maxFastPlaceViolations) {
                    punish(player, getConfig().getString("fastplace.kick-message", "检测到快速放置方块"));
                    getLogger().info("玩家 " + player.getName() + " 因 FastPlace 作弊被踢出");
                    return;
                }
            }
            lastGeneralPlaceTime.put(playerId, now);
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
        if (!isDetectionEnabled(player)) return;

        // AutoTool 检测
        if (getConfig().getBoolean("autotool.enabled", true)) {
            long now = System.currentTimeMillis();
            long lastSwitch = lastToolSwitchTime.getOrDefault(playerId, now - minToolSwitchInterval);

            if (now - lastSwitch < minToolSwitchInterval) {
                int violations = autoToolViolations.getOrDefault(playerId, 0) + 1;
                autoToolViolations.put(playerId, violations);

                if (violations >= maxAutoToolViolations) {
                    String kickMessage = getConfig().getString("autotool.kick-message", "检测到自动切换工具");
                    punish(player, kickMessage);
                    getLogger().info("玩家 " + player.getName() + " 因 AutoTool 作弊被踢出");
                    return;
                }
            }
            lastToolSwitchTime.put(playerId, now);
        }

        // FastHotbarSwap 检测（娱乐性质：快捷栏切换间隔过短）
        if (getConfig().getBoolean("fasthotbarswap.enabled", true)) {
            long now2 = System.currentTimeMillis();
            long last = lastHotbarSwapTime.getOrDefault(playerId, now2 - minHotbarSwapInterval);
            if (now2 - last < minHotbarSwapInterval) {
                int violations = fastHotbarSwapViolations.getOrDefault(playerId, 0) + 1;
                fastHotbarSwapViolations.put(playerId, violations);
                if (violations >= maxFastHotbarSwapViolations) {
                    punish(player, getConfig().getString("fasthotbarswap.kick-message", "检测到快速快捷栏切换"));
                    getLogger().info("玩家 " + player.getName() + " 因 FastHotbarSwap 作弊被踢出");
                    return;
                }
            }
            lastHotbarSwapTime.put(playerId, now2);
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
        if (!isDetectionEnabled(player)) return;

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
                    putViolation(reachViolations, playerId, violations, "inventoryCleaner");

                    if (violations >= maxInventoryCleanerViolations) {
                        String kickMessage = getConfig().getString("inventorycleaner.kick-message", "检测到自动清理背包");
                        punish(player, kickMessage);
                        getLogger().info("玩家 " + player.getName() + " 因 InventoryCleaner 作弊被踢出");
                        return;
                    }
                }
            }
            lastInventoryClickTime.put(playerId, now);
        }

        // ChestStealer 检测（娱乐性质：容器内点击过快，仅箱子/木桶/潜影盒类容器）
        if (getConfig().getBoolean("cheststealer.enabled", true)) {
            InventoryType topType = event.getInventory().getType();
            if (topType == InventoryType.CHEST || topType == InventoryType.BARREL
                    || topType == InventoryType.SHULKER_BOX) {
                long now = System.currentTimeMillis();
                long lastC = lastChestClickTime.getOrDefault(playerId, now - 1000);
                int clicks = chestClickCount.getOrDefault(playerId, 0);

                if (now - lastC > 1000) {
                    chestClickCount.put(playerId, 1);
                } else {
                    chestClickCount.put(playerId, clicks + 1);
                    if (clicks + 1 > maxChestClicksPerSecond) {
                        int violations = chestStealerViolations.getOrDefault(playerId, 0) + 1;
                        chestStealerViolations.put(playerId, violations);

                        if (violations >= maxChestStealerViolations) {
                            String kickMessage = getConfig().getString("cheststealer.kick-message", "检测到快速偷箱子");
                            punish(player, kickMessage);
                            getLogger().info("玩家 " + player.getName() + " 因 ChestStealer 作弊被踢出");
                            return;
                        }
                    }
                }
                lastChestClickTime.put(playerId, now);
            }
        }

        // AutoArmor 检测（娱乐性质：快速装备护甲）
        if (getConfig().getBoolean("autoarmor.enabled", true)
                && event.getSlotType() == org.bukkit.event.inventory.InventoryType.SlotType.ARMOR) {
            long now = System.currentTimeMillis();
            long lastA = lastArmorEquipTime.getOrDefault(playerId, now - minArmorEquipInterval);
            if (now - lastA < minArmorEquipInterval) {
                int violations = autoArmorViolations.getOrDefault(playerId, 0) + 1;
                autoArmorViolations.put(playerId, violations);

                if (violations >= maxAutoArmorViolations) {
                    String kickMessage = getConfig().getString("autoarmor.kick-message", "检测到自动装备护甲");
                    punish(player, kickMessage);
                    getLogger().info("玩家 " + player.getName() + " 因 AutoArmor 作弊被踢出");
                    return;
                }
            }
            lastArmorEquipTime.put(playerId, now);
        }

        // FastShiftClick 检测（娱乐性质：shift-click 间隔过短）
        if (getConfig().getBoolean("fastshiftclick.enabled", true) && event.isShiftClick()) {
            long now2 = System.currentTimeMillis();
            long last = lastShiftClickTime.getOrDefault(playerId, now2 - minShiftClickInterval);
            if (now2 - last < minShiftClickInterval) {
                int violations = fastShiftClickViolations.getOrDefault(playerId, 0) + 1;
                fastShiftClickViolations.put(playerId, violations);
                if (violations >= maxFastShiftClickViolations) {
                    punish(player, getConfig().getString("fastshiftclick.kick-message", "检测到快速 shift-click"));
                    getLogger().info("玩家 " + player.getName() + " 因 FastShiftClick 作弊被踢出");
                    return;
                }
            }
            lastShiftClickTime.put(playerId, now2);
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
        if (!isDetectionEnabled(player)) return;

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
                            punish(player, kickMessage);
                            getLogger().info("玩家 " + player.getName() + " 因 Sneak 作弊被踢出");
                            return;
                        }
                    }
                }
                sneakStartTime.remove(playerId);
            }
        }

        // SneakSpam 检测（娱乐性质：潜行切换过快）
        if (getConfig().getBoolean("sneakspam.enabled", true)) {
            long now2 = System.currentTimeMillis();
            long last = lastSneakToggleTime.getOrDefault(playerId, now2 - minSneakToggleInterval);
            if (now2 - last < minSneakToggleInterval) {
                int violations = sneakSpamViolations.getOrDefault(playerId, 0) + 1;
                sneakSpamViolations.put(playerId, violations);
                if (violations >= maxSneakSpamViolations) {
                    punish(player, getConfig().getString("sneakspam.kick-message", "检测到快速潜行切换"));
                    getLogger().info("玩家 " + player.getName() + " 因 SneakSpam 作弊被踢出");
                    return;
                }
            }
            lastSneakToggleTime.put(playerId, now2);
        }
    }

    /**
     * 处理玩家钓鱼事件，检测 AutoFish 作弊
     *
     * @param event 玩家钓鱼事件
     */
    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        if (!(event.getPlayer() instanceof Player)) return;
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        if (!isDetectionEnabled(player)) return;

        if (getConfig().getBoolean("autofish.enabled", true)) {
            // 降级方案：用 getCaught() 判断成功捕获（跨版本稳定，不依赖 getState() 枚举）
            if (event.getCaught() != null) {
                long now = System.currentTimeMillis();
                long lastF = lastFishTime.getOrDefault(playerId, now - 60000);
                int fish = fishCount.getOrDefault(playerId, 0);

                if (now - lastF > 60000) {
                    fishCount.put(playerId, 1);
                } else {
                    fishCount.put(playerId, fish + 1);
                    if (fish + 1 > maxFishPerMinute) {
                        int violations = autoFishViolations.getOrDefault(playerId, 0) + 1;
                        autoFishViolations.put(playerId, violations);

                        if (violations >= maxAutoFishViolations) {
                            String kickMessage = getConfig().getString("autofish.kick-message", "检测到自动钓鱼");
                            punish(player, kickMessage);
                            getLogger().info("玩家 " + player.getName() + " 因 AutoFish 作弊被踢出");
                            return;
                        }
                    }
                }
                lastFishTime.put(playerId, now);
            }
        }
    }

    /**
     * 处理方块破坏事件，检测 Nuker 和 FastBreak 作弊
     *
     * @param event 方块破坏事件
     */
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        if (!isDetectionEnabled(player)) return;
        long now = System.currentTimeMillis();

        // Nuker 检测（娱乐性质：1 秒窗口内破坏方块数）
        if (getConfig().getBoolean("nuker.enabled", true)) {
            long lastBreak = lastBlockBreakTime.getOrDefault(playerId, now - 1000);
            int blocks = blockBreakCount.getOrDefault(playerId, 0);

            if (now - lastBreak > 1000) {
                blockBreakCount.put(playerId, 1);
            } else {
                blockBreakCount.put(playerId, blocks + 1);
                if (blocks + 1 > maxNukerBlocks) {
                    int violations = nukerViolations.getOrDefault(playerId, 0) + 1;
                    nukerViolations.put(playerId, violations);

                    if (violations >= maxNukerViolations) {
                        String kickMessage = getConfig().getString("nuker.kick-message", "检测到瞬间破坏多方块");
                        punish(player, kickMessage);
                        getLogger().info("玩家 " + player.getName() + " 因 Nuker 作弊被踢出");
                        return;
                    }
                }
            }
        }

        // FastBreak 检测（娱乐性质：破坏间隔过短）
        if (getConfig().getBoolean("fastbreak.enabled", true)) {
            long lastBreak = lastBlockBreakTime.getOrDefault(playerId, now - minBreakInterval);
            if (now - lastBreak < minBreakInterval) {
                int violations = fastBreakViolations.getOrDefault(playerId, 0) + 1;
                fastBreakViolations.put(playerId, violations);

                if (violations >= maxFastBreakViolations) {
                    String kickMessage = getConfig().getString("fastbreak.kick-message", "检测到破坏方块间隔过短");
                    punish(player, kickMessage);
                    getLogger().info("玩家 " + player.getName() + " 因 FastBreak 作弊被踢出");
                    return;
                }
            }
        }

        // XRay 检测（娱乐性质：稀有矿石时间窗口高产）
        if (getConfig().getBoolean("xray.enabled", true)) {
            Material broken = event.getBlock().getType();
            boolean rare = broken == Material.DIAMOND_ORE || broken == Material.DEEPSLATE_DIAMOND_ORE
                    || broken == Material.EMERALD_ORE || broken == Material.DEEPSLATE_EMERALD_ORE
                    || broken == Material.GOLD_ORE || broken == Material.DEEPSLATE_GOLD_ORE
                    || broken == Material.ANCIENT_DEBRIS;
            if (rare) {
                long lastOre = lastOreTime.getOrDefault(playerId, now - 60000);
                int ores = oreCount.getOrDefault(playerId, 0);

                if (now - lastOre > 60000) {
                    oreCount.put(playerId, 1);
                } else {
                    oreCount.put(playerId, ores + 1);
                    if (ores + 1 > maxOresPerMinute) {
                        int violations = xrayViolations.getOrDefault(playerId, 0) + 1;
                        xrayViolations.put(playerId, violations);

                        if (violations >= maxXrayViolations) {
                            String kickMessage = getConfig().getString("xray.kick-message", "检测到稀有矿石异常高产");
                            punish(player, kickMessage);
                            getLogger().info("玩家 " + player.getName() + " 因 XRay 作弊被踢出");
                            return;
                        }
                    }
                }
                lastOreTime.put(playerId, now);
            }
        }

        // BreakReach 检测（娱乐性质：破坏方块距离过远）
        if (getConfig().getBoolean("breakreach.enabled", true)) {
            double bdist = player.getEyeLocation().distance(event.getBlock().getLocation().add(0.5, 0.5, 0.5));
            if (bdist > maxBreakReach) {
                int violations = breakReachViolations.getOrDefault(playerId, 0) + 1;
                breakReachViolations.put(playerId, violations);
                if (violations >= maxBreakReachViolations) {
                    punish(player, getConfig().getString("breakreach.kick-message", "检测到破坏方块距离过远"));
                    getLogger().info("玩家 " + player.getName() + " 因 BreakReach 作弊被踢出");
                    return;
                }
            }
        }

        // FastOre 检测（娱乐性质：矿石破坏间隔过短）
        if (getConfig().getBoolean("fastore.enabled", true)) {
            Material broken = event.getBlock().getType();
            boolean ore = broken.name().endsWith("_ORE") || broken == Material.ANCIENT_DEBRIS;
            if (ore) {
                long last = lastOreBreakTime.getOrDefault(playerId, now - minOreBreakInterval);
                if (now - last < minOreBreakInterval) {
                    int violations = fastOreViolations.getOrDefault(playerId, 0) + 1;
                    fastOreViolations.put(playerId, violations);
                    if (violations >= maxFastOreViolations) {
                        punish(player, getConfig().getString("fastore.kick-message", "检测到快速破坏矿石"));
                        getLogger().info("玩家 " + player.getName() + " 因 FastOre 作弊被踢出");
                        return;
                    }
                }
                lastOreBreakTime.put(playerId, now);
            }
        }

        // BreakWhileMoving 检测（娱乐性质：高速移动中破坏方块）
        if (getConfig().getBoolean("breakwhilemoving.enabled", true)) {
            double horiz = Math.sqrt(player.getVelocity().getX() * player.getVelocity().getX() + player.getVelocity().getZ() * player.getVelocity().getZ()) * 20;
            if (horiz > maxBreakWhileMovingSpeed) {
                int violations = breakWhileMovingViolations.getOrDefault(playerId, 0) + 1;
                breakWhileMovingViolations.put(playerId, violations);
                if (violations >= maxBreakWhileMovingViolations) {
                    punish(player, getConfig().getString("breakwhilemoving.kick-message", "检测到移动中破坏方块"));
                    getLogger().info("玩家 " + player.getName() + " 因 BreakWhileMoving 作弊被踢出");
                    return;
                }
            }
        }
        lastBlockBreakTime.put(playerId, now);
    }

    /**
     * 处理玩家消耗物品事件，检测 FastEat 和 AutoSoup 作弊
     *
     * @param event 玩家消耗物品事件
     */
    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        if (!isDetectionEnabled(player)) return;
        long now = System.currentTimeMillis();
        ItemStack item = event.getItem();

        // FastEat 检测（娱乐性质：进食间隔过短）
        if (getConfig().getBoolean("fasteat.enabled", true) && item.getType().isEdible()) {
            long lastEat = lastEatConsumeTime.getOrDefault(playerId, now - minEatInterval);
            if (now - lastEat < minEatInterval) {
                int violations = fastEatViolations.getOrDefault(playerId, 0) + 1;
                fastEatViolations.put(playerId, violations);

                if (violations >= maxFastEatViolations) {
                    String kickMessage = getConfig().getString("fasteat.kick-message", "检测到进食间隔过短");
                    punish(player, kickMessage);
                    getLogger().info("玩家 " + player.getName() + " 因 FastEat 作弊被踢出");
                    return;
                }
            }
            lastEatConsumeTime.put(playerId, now);
        }

        // AutoSoup 检测（娱乐性质：喝汤间隔过短，仅汤类）
        if (getConfig().getBoolean("autosoup.enabled", true)
                && (item.getType() == Material.MUSHROOM_STEW
                || item.getType() == Material.RABBIT_STEW
                || item.getType() == Material.BEETROOT_SOUP
                || item.getType() == Material.SUSPICIOUS_STEW)) {
            long lastConsume = lastConsumeTime.getOrDefault(playerId, now - minConsumeInterval);
            if (now - lastConsume < minConsumeInterval) {
                int violations = autoSoupViolations.getOrDefault(playerId, 0) + 1;
                autoSoupViolations.put(playerId, violations);

                if (violations >= maxAutoSoupViolations) {
                    String kickMessage = getConfig().getString("autosoup.kick-message", "检测到自动喝汤");
                    punish(player, kickMessage);
                    getLogger().info("玩家 " + player.getName() + " 因 AutoSoup 作弊被踢出");
                    return;
                }
            }
            lastConsumeTime.put(playerId, now);
        }

        // FastPotion 检测（娱乐性质：饮用瓶装药水间隔过短）
        if (getConfig().getBoolean("fastpotion.enabled", true) && item.getType() == Material.POTION) {
            long last = lastPotionConsumeTime.getOrDefault(playerId, now - minPotionConsumeInterval);
            if (now - last < minPotionConsumeInterval) {
                int violations = fastPotionViolations.getOrDefault(playerId, 0) + 1;
                fastPotionViolations.put(playerId, violations);
                if (violations >= maxFastPotionViolations) {
                    punish(player, getConfig().getString("fastpotion.kick-message", "检测到快速喝药水"));
                    getLogger().info("玩家 " + player.getName() + " 因 FastPotion 作弊被踢出");
                    return;
                }
            }
            lastPotionConsumeTime.put(playerId, now);
        }

        // FastMilk 检测（娱乐性质：牛奶桶消耗间隔过短）
        if (getConfig().getBoolean("fastmilk.enabled", true) && item.getType() == Material.MILK_BUCKET) {
            long last = lastMilkTime.getOrDefault(playerId, now - minMilkInterval);
            if (now - last < minMilkInterval) {
                int violations = fastMilkViolations.getOrDefault(playerId, 0) + 1;
                fastMilkViolations.put(playerId, violations);
                if (violations >= maxFastMilkViolations) {
                    punish(player, getConfig().getString("fastmilk.kick-message", "检测到快速喝牛奶"));
                    getLogger().info("玩家 " + player.getName() + " 因 FastMilk 作弊被踢出");
                    return;
                }
            }
            lastMilkTime.put(playerId, now);
        }

        // FastHoney 检测（娱乐性质：蜂蜜瓶消耗间隔过短）
        if (getConfig().getBoolean("fasthoney.enabled", true) && item.getType() == Material.HONEY_BOTTLE) {
            long last = lastHoneyTime.getOrDefault(playerId, now - minHoneyInterval);
            if (now - last < minHoneyInterval) {
                int violations = fastHoneyViolations.getOrDefault(playerId, 0) + 1;
                fastHoneyViolations.put(playerId, violations);
                if (violations >= maxFastHoneyViolations) {
                    punish(player, getConfig().getString("fasthoney.kick-message", "检测到快速喝蜂蜜"));
                    getLogger().info("玩家 " + player.getName() + " 因 FastHoney 作弊被踢出");
                    return;
                }
            }
            lastHoneyTime.put(playerId, now);
        }
    }

    /**
     * 处理玩家移动事件（扩展检测），检测 WaterWalk、Glide、Step、Spider、NoWeb、FastLadder 作弊
     *
     * @param event 玩家移动事件
     */
    @EventHandler
    public void onMoveChecks(PlayerMoveEvent event) {
        if (event.getTo() == null || event.getFrom() == null) {
            return;
        }

        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        if (!isDetectionEnabled(player)) return;
        long now = System.currentTimeMillis();

        if (player.isFlying()) {
            return;
        }

        org.bukkit.Location from = event.getFrom();
        org.bukkit.Location to = event.getTo();
        double dy = to.getY() - from.getY();
        double dx = to.getX() - from.getX();
        double dz = to.getZ() - from.getZ();
        double horizontal = Math.sqrt(dx * dx + dz * dz);

        // WaterWalk 检测（娱乐性质：水面行走）
        if (getConfig().getBoolean("waterwalk.enabled", true)) {
            Block below = to.getBlock().getRelative(org.bukkit.block.BlockFace.DOWN);
            if (below.getType() == Material.WATER
                    && player.isOnGround()
                    && !player.isSwimming()
                    && horizontal > 0.1) {
                int violations = waterWalkViolations.getOrDefault(playerId, 0) + 1;
                waterWalkViolations.put(playerId, violations);

                if (violations >= maxWaterWalkViolations) {
                    String kickMessage = getConfig().getString("waterwalk.kick-message", "检测到水面行走");
                    punish(player, kickMessage);
                    getLogger().info("玩家 " + player.getName() + " 因 WaterWalk 作弊被踢出");
                    return;
                }
            }
        }

        // Glide 检测（娱乐性质：空中缓降滑行）
        if (getConfig().getBoolean("glide.enabled", true)
                && !player.isOnGround()
                && player.getFallDistance() > 0
                && dy > -maxGlideFallSpeed
                && dy <= 0) {
            int violations = glideViolations.getOrDefault(playerId, 0) + 1;
            glideViolations.put(playerId, violations);

            if (violations >= maxGlideViolations) {
                String kickMessage = getConfig().getString("glide.kick-message", "检测到空中缓降滑行");
                punish(player, kickMessage);
                getLogger().info("玩家 " + player.getName() + " 因 Glide 作弊被踢出");
                return;
            }
        }

        // Step 检测（娱乐性质：单次纵向上升超台阶高度）
        if (getConfig().getBoolean("step.enabled", true) && dy > maxStepHeight) {
            int violations = stepViolations.getOrDefault(playerId, 0) + 1;
            stepViolations.put(playerId, violations);

            if (violations >= maxStepViolations) {
                String kickMessage = getConfig().getString("step.kick-message", "检测到异常台阶上升");
                punish(player, kickMessage);
                getLogger().info("玩家 " + player.getName() + " 因 Step 作弊被踢出");
                return;
            }
        }

        // Spider 检测（娱乐性质：贴墙垂直上升）
        if (getConfig().getBoolean("spider.enabled", true)
                && dy > 0.1
                && horizontal < 0.1
                && !player.isOnGround()) {
            Block side = to.getBlock();
            boolean againstWall = side.getRelative(org.bukkit.block.BlockFace.NORTH).getType().isSolid()
                    || side.getRelative(org.bukkit.block.BlockFace.SOUTH).getType().isSolid()
                    || side.getRelative(org.bukkit.block.BlockFace.EAST).getType().isSolid()
                    || side.getRelative(org.bukkit.block.BlockFace.WEST).getType().isSolid();
            if (againstWall) {
                int violations = spiderViolations.getOrDefault(playerId, 0) + 1;
                spiderViolations.put(playerId, violations);

                if (violations >= maxSpiderViolations) {
                    String kickMessage = getConfig().getString("spider.kick-message", "检测到贴墙攀爬");
                    punish(player, kickMessage);
                    getLogger().info("玩家 " + player.getName() + " 因 Spider 作弊被踢出");
                    return;
                }
            }
        }

        // NoWeb 检测（娱乐性质：蛛网内快速移动）
        if (getConfig().getBoolean("noweb.enabled", true)
                && to.getBlock().getType() == Material.COBWEB) {
            double totalSpeed = Math.sqrt(horizontal * horizontal + dy * dy) * 20;
            if (totalSpeed > maxNoWebSpeed) {
                int violations = noWebViolations.getOrDefault(playerId, 0) + 1;
                noWebViolations.put(playerId, violations);

                if (violations >= maxNoWebViolations) {
                    String kickMessage = getConfig().getString("noweb.kick-message", "检测到蛛网内快速移动");
                    punish(player, kickMessage);
                    getLogger().info("玩家 " + player.getName() + " 因 NoWeb 作弊被踢出");
                    return;
                }
            }
        }

        // FastLadder 检测（娱乐性质：梯子快速攀爬）
        if (getConfig().getBoolean("fastladder.enabled", true)
                && to.getBlock().getType() == Material.LADDER) {
            double climbSpeed = Math.abs(dy) * 20;
            if (climbSpeed > maxFastLadderSpeed) {
                int violations = fastLadderViolations.getOrDefault(playerId, 0) + 1;
                fastLadderViolations.put(playerId, violations);

                if (violations >= maxFastLadderViolations) {
                    String kickMessage = getConfig().getString("fastladder.kick-message", "检测到梯子快速攀爬");
                    punish(player, kickMessage);
                    getLogger().info("玩家 " + player.getName() + " 因 FastLadder 作弊被踢出");
                    return;
                }
            }
        }

        // HighJump 检测（娱乐性质：异常高跳）
        if (getConfig().getBoolean("highjump.enabled", true)
                && !player.isOnGround()
                && dy > minHighJumpHeight
                && player.getFallDistance() < 0.1) {
            int violations = highJumpViolations.getOrDefault(playerId, 0) + 1;
            highJumpViolations.put(playerId, violations);

            if (violations >= maxHighJumpViolations) {
                String kickMessage = getConfig().getString("highjump.kick-message", "检测到异常高跳");
                punish(player, kickMessage);
                getLogger().info("玩家 " + player.getName() + " 因 HighJump 作弊被踢出");
                return;
            }
        }

        // Dolphin 检测（娱乐性质：水中快速游泳）
        if (getConfig().getBoolean("dolphin.enabled", true)
                && (player.getLocation().getBlock().getType() == Material.WATER
                || player.getLocation().getBlock().getType() == Material.BUBBLE_COLUMN)) {
            double swimSpeed = horizontal * 20;
            if (swimSpeed > maxSwimSpeed) {
                int violations = dolphinViolations.getOrDefault(playerId, 0) + 1;
                dolphinViolations.put(playerId, violations);

                if (violations >= maxDolphinViolations) {
                    String kickMessage = getConfig().getString("dolphin.kick-message", "检测到水中快速游泳");
                    punish(player, kickMessage);
                    getLogger().info("玩家 " + player.getName() + " 因 Dolphin 作弊被踢出");
                    return;
                }
            }
        }

        // Phase 检测（娱乐性质：穿墙传送式纵向位移）
        if (getConfig().getBoolean("phase.enabled", true)
                && Math.abs(dy) > maxPhaseDistance) {
            int violations = phaseViolations.getOrDefault(playerId, 0) + 1;
            phaseViolations.put(playerId, violations);

            if (violations >= maxPhaseViolations) {
                String kickMessage = getConfig().getString("phase.kick-message", "检测到穿墙传送");
                punish(player, kickMessage);
                getLogger().info("玩家 " + player.getName() + " 因 Phase 作弊被踢出");
                return;
            }
        }

        // Blink 检测（娱乐性质：异常远距瞬移）
        if (getConfig().getBoolean("blink.enabled", true)
                && horizontal > maxBlinkDistance) {
            int violations = blinkViolations.getOrDefault(playerId, 0) + 1;
            blinkViolations.put(playerId, violations);

            if (violations >= maxBlinkViolations) {
                String kickMessage = getConfig().getString("blink.kick-message", "检测到异常远距瞬移");
                punish(player, kickMessage);
                getLogger().info("玩家 " + player.getName() + " 因 Blink 作弊被踢出");
                return;
            }
        }

        // FastSneak 检测（娱乐性质：潜行加速）
        if (getConfig().getBoolean("fastsneak.enabled", true)
                && player.isSneaking()
                && player.isOnGround()) {
            double sneakSpeed = horizontal * 20;
            if (sneakSpeed > maxSneakSpeed) {
                int violations = fastSneakViolations.getOrDefault(playerId, 0) + 1;
                fastSneakViolations.put(playerId, violations);

                if (violations >= maxFastSneakViolations) {
                    String kickMessage = getConfig().getString("fastsneak.kick-message", "检测到潜行加速");
                    punish(player, kickMessage);
                    getLogger().info("玩家 " + player.getName() + " 因 FastSneak 作弊被踢出");
                    return;
                }
            }
        }

        // Derp 检测（娱乐性质：头部异常转动）
        if (getConfig().getBoolean("derp.enabled", true)) {
            float currentYaw = to.getYaw();
            Float lastY = lastMoveYaw.get(playerId);
            if (lastY != null) {
                float yawDiff = Math.abs(currentYaw - lastY);
                if (yawDiff > 180) yawDiff = 360 - yawDiff;
                if (yawDiff > maxDerpYawChange) {
                    int violations = derpViolations.getOrDefault(playerId, 0) + 1;
                    derpViolations.put(playerId, violations);

                    if (violations >= maxDerpViolations) {
                        String kickMessage = getConfig().getString("derp.kick-message", "检测到头部异常转动");
                        punish(player, kickMessage);
                        getLogger().info("玩家 " + player.getName() + " 因 Derp 作弊被踢出");
                        return;
                    }
                }
            }
            lastMoveYaw.put(playerId, currentYaw);
        }

        // ElytraSpeed 检测（娱乐性质：滑翔速度异常）
        if (getConfig().getBoolean("elytraspeed.enabled", true)
                && player.isGliding()) {
            double elySpeed = horizontal * 20;
            if (elySpeed > maxElytraSpeed) {
                int violations = elytraSpeedViolations.getOrDefault(playerId, 0) + 1;
                elytraSpeedViolations.put(playerId, violations);

                if (violations >= maxElytraSpeedViolations) {
                    String kickMessage = getConfig().getString("elytraspeed.kick-message", "检测到滑翔速度异常");
                    punish(player, kickMessage);
                    getLogger().info("玩家 " + player.getName() + " 因 ElytraSpeed 作弊被踢出");
                    return;
                }
            }
        }

        // FastSprint 检测（娱乐性质：疾跑速度超上限）
        if (getConfig().getBoolean("fastsprint.enabled", true)
                && player.isSprinting() && player.isOnGround()) {
            double sp = horizontal * 20;
            if (sp > maxFastSprintSpeed) {
                int violations = fastSprintViolations.getOrDefault(playerId, 0) + 1;
                fastSprintViolations.put(playerId, violations);
                if (violations >= maxFastSprintViolations) {
                    punish(player, getConfig().getString("fastsprint.kick-message", "检测到疾跑速度异常"));
                    getLogger().info("玩家 " + player.getName() + " 因 FastSprint 作弊被踢出");
                    return;
                }
            }
        }

        // IceSpeed 检测（娱乐性质：冰上移动过快）
        if (getConfig().getBoolean("icespeed.enabled", true) && player.isOnGround()) {
            Block foot = from.getBlock();
            if (foot.getType() == Material.ICE || foot.getType() == Material.PACKED_ICE
                    || foot.getType() == Material.BLUE_ICE || foot.getType() == Material.FROSTED_ICE) {
                if (horizontal * 20 > maxIceSpeed) {
                    int violations = iceSpeedViolations.getOrDefault(playerId, 0) + 1;
                    iceSpeedViolations.put(playerId, violations);
                    if (violations >= maxIceSpeedViolations) {
                        punish(player, getConfig().getString("icespeed.kick-message", "检测到冰上加速"));
                        getLogger().info("玩家 " + player.getName() + " 因 IceSpeed 作弊被踢出");
                        return;
                    }
                }
            }
        }

        // Bhop 检测（娱乐性质：连续跳跃无地面停留）
        if (getConfig().getBoolean("bhop.enabled", true)) {
            if (player.isOnGround()) {
                lastGroundTime.put(playerId, now);
            } else {
                Long lg = lastGroundTime.get(playerId);
                if (lg != null && now - lg < 100 && dy > 0.2) {
                    int violations = bhopViolations.getOrDefault(playerId, 0) + 1;
                    bhopViolations.put(playerId, violations);
                    if (violations >= maxBhopViolations) {
                        punish(player, getConfig().getString("bhop.kick-message", "检测到连跳"));
                        getLogger().info("玩家 " + player.getName() + " 因 Bhop 作弊被踢出");
                        return;
                    }
                }
            }
        }

        // AirJump 检测（娱乐性质：从空中起跳）
        if (getConfig().getBoolean("airjump.enabled", true)
                && !player.isOnGround() && dy > 0.3 && player.getFallDistance() < 0.1) {
            int violations = airJumpViolations.getOrDefault(playerId, 0) + 1;
            airJumpViolations.put(playerId, violations);
            if (violations >= maxAirJumpViolations) {
                punish(player, getConfig().getString("airjump.kick-message", "检测到空中起跳"));
                getLogger().info("玩家 " + player.getName() + " 因 AirJump 作弊被踢出");
                return;
            }
        }

        // Jetpack 检测（娱乐性质：持续向上非飞行/滑翔）
        if (getConfig().getBoolean("jetpack.enabled", true)
                && !player.isFlying() && !player.isGliding()
                && !player.isOnGround() && dy > minJetpackUp) {
            int violations = jetpackViolations.getOrDefault(playerId, 0) + 1;
            jetpackViolations.put(playerId, violations);
            if (violations >= maxJetpackViolations) {
                punish(player, getConfig().getString("jetpack.kick-message", "检测到持续上升"));
                getLogger().info("玩家 " + player.getName() + " 因 Jetpack 作弊被踢出");
                return;
            }
        }

        // FastClimbVine 检测（娱乐性质：藤蔓攀爬过快）
        if (getConfig().getBoolean("fastclimbvine.enabled", true)
                && to.getBlock().getType() == Material.VINE) {
            if (dy * 20 > maxFastClimbVineSpeed) {
                int violations = fastClimbVineViolations.getOrDefault(playerId, 0) + 1;
                fastClimbVineViolations.put(playerId, violations);
                if (violations >= maxFastClimbVineViolations) {
                    punish(player, getConfig().getString("fastclimbvine.kick-message", "检测到藤蔓快速攀爬"));
                    getLogger().info("玩家 " + player.getName() + " 因 FastClimbVine 作弊被踢出");
                    return;
                }
            }
        }

        // FastDescend 检测（娱乐性质：下落速度超过重力）
        if (getConfig().getBoolean("fastdescend.enabled", true)
                && !player.isOnGround() && dy < maxFastDescendSpeed) {
            int violations = fastDescendViolations.getOrDefault(playerId, 0) + 1;
            fastDescendViolations.put(playerId, violations);
            if (violations >= maxFastDescendViolations) {
                punish(player, getConfig().getString("fastdescend.kick-message", "检测到异常快速下落"));
                getLogger().info("玩家 " + player.getName() + " 因 FastDescend 作弊被踢出");
                return;
            }
        }

        // Strafe 检测（娱乐性质：疾跑时横向位移过大）
        if (getConfig().getBoolean("strafe.enabled", true)
                && player.isSprinting() && player.isOnGround()
                && horizontal > 0.5 && Math.abs(dy) < 0.01) {
            int violations = strafeViolations.getOrDefault(playerId, 0) + 1;
            strafeViolations.put(playerId, violations);
            if (violations >= maxStrafeViolations) {
                punish(player, getConfig().getString("strafe.kick-message", "检测到疾跑横向位移异常"));
                getLogger().info("玩家 " + player.getName() + " 因 Strafe 作弊被踢出");
                return;
            }
        }

        // Float 检测（娱乐性质：空中原地悬浮）
        if (getConfig().getBoolean("float.enabled", true)
                && !player.isOnGround() && !player.isFlying()
                && horizontal < 0.01 && Math.abs(dy) < 0.01) {
            int violations = floatViolations.getOrDefault(playerId, 0) + 1;
            floatViolations.put(playerId, violations);
            if (violations >= maxFloatViolations) {
                punish(player, getConfig().getString("float.kick-message", "检测到空中悬浮"));
                getLogger().info("玩家 " + player.getName() + " 因 Float 作弊被踢出");
                return;
            }
        }

        // FastSneakAir 检测（娱乐性质：空中潜行）
        if (getConfig().getBoolean("fastsneakair.enabled", true)
                && player.isSneaking() && !player.isOnGround()) {
            int violations = fastSneakAirViolations.getOrDefault(playerId, 0) + 1;
            fastSneakAirViolations.put(playerId, violations);
            if (violations >= maxFastSneakAirViolations) {
                punish(player, getConfig().getString("fastsneakair.kick-message", "检测到空中潜行"));
                getLogger().info("玩家 " + player.getName() + " 因 FastSneakAir 作弊被踢出");
                return;
            }
        }

        // HeadRoll 检测（娱乐性质：pitch 异常变化）
        if (getConfig().getBoolean("headroll.enabled", true)) {
            float curPitch = to.getPitch();
            Float lastP = lastMovePitch.get(playerId);
            if (lastP != null && Math.abs(curPitch - lastP) > maxHeadRollChange) {
                int violations = headRollViolations.getOrDefault(playerId, 0) + 1;
                headRollViolations.put(playerId, violations);
                if (violations >= maxHeadRollViolations) {
                    punish(player, getConfig().getString("headroll.kick-message", "检测到头部异常俯仰"));
                    getLogger().info("玩家 " + player.getName() + " 因 HeadRoll 作弊被踢出");
                    return;
                }
            }
            lastMovePitch.put(playerId, curPitch);
        }

        // TeleportUp 检测（娱乐性质：纵向位置瞬间上升无跳跃序列）
        if (getConfig().getBoolean("teleportup.enabled", true)
                && dy > maxTeleportUp && player.getFallDistance() < 0.1) {
            int violations = teleportUpViolations.getOrDefault(playerId, 0) + 1;
            teleportUpViolations.put(playerId, violations);
            if (violations >= maxTeleportUpViolations) {
                punish(player, getConfig().getString("teleportup.kick-message", "检测到纵向瞬移上升"));
                getLogger().info("玩家 " + player.getName() + " 因 TeleportUp 作弊被踢出");
                return;
            }
        }
    }

    /**
     * 处理玩家速度事件，检测 AntiKnockback 作弊
     *
     * @param event 玩家速度事件
     */
    @EventHandler
    public void onPlayerVelocity(PlayerVelocityEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        if (!isDetectionEnabled(player)) return;

        if (getConfig().getBoolean("antiknockback.enabled", true)) {
            org.bukkit.util.Vector vel = event.getVelocity();
            double horizontal = Math.sqrt(vel.getX() * vel.getX() + vel.getZ() * vel.getZ());
            // 如果击退水平速度被显著削减
            if (horizontal < minKnockbackRatio && horizontal > 0) {
                int violations = antiKnockbackViolations.getOrDefault(playerId, 0) + 1;
                antiKnockbackViolations.put(playerId, violations);

                if (violations >= maxAntiKnockbackViolations) {
                    String kickMessage = getConfig().getString("antiknockback.kick-message", "检测到击退抵抗");
                    punish(player, kickMessage);
                    getLogger().info("玩家 " + player.getName() + " 因 AntiKnockback 作弊被踢出");
                }
            }
        }
    }

    /**
     * 处理玩家挥动动画事件，记录挥动时间用于 NoSwing 检测
     *
     * @param event 玩家动画事件
     */
    @EventHandler
    public void onPlayerAnimation(PlayerAnimationEvent event) {
        Player player = event.getPlayer();
        if (getConfig().getBoolean("noswing.enabled", true)) {
            lastSwingTime.put(player.getUniqueId(), System.currentTimeMillis());
        }
    }

    /**
     * 处理载具移动事件，检测 BoatFly 作弊
     *
     * @param event 载具移动事件
     */
    @EventHandler
    public void onVehicleMove(VehicleMoveEvent event) {
        if (event.getTo() == null || event.getFrom() == null) {
            return;
        }

        org.bukkit.entity.Vehicle vehicle = event.getVehicle();
        if (!(vehicle instanceof org.bukkit.entity.Boat)) {
            return;
        }

        org.bukkit.entity.Entity passenger = vehicle.getPassengers().isEmpty() ? null : vehicle.getPassengers().get(0);
        if (!(passenger instanceof Player)) {
            return;
        }

        Player player = (Player) passenger;
        UUID playerId = player.getUniqueId();
        if (!isDetectionEnabled(player)) return;

        if (getConfig().getBoolean("boatfly.enabled", true)) {
            // 船在空中（下方非液体/非固体）即视为滞空
            Block below = vehicle.getLocation().getBlock().getRelative(org.bukkit.block.BlockFace.DOWN);
            boolean inAir = !below.isLiquid() && !below.getType().isSolid();

            if (inAir) {
                int moves = boatAirMoves.getOrDefault(playerId, 0) + 1;
                boatAirMoves.put(playerId, moves);

                if (moves > maxBoatAirMoves) {
                    int violations = boatFlyViolations.getOrDefault(playerId, 0) + 1;
                    boatFlyViolations.put(playerId, violations);

                    if (violations >= maxBoatFlyViolations) {
                        String kickMessage = getConfig().getString("boatfly.kick-message", "检测到骑船滞空");
                        punish(player, kickMessage);
                        getLogger().info("玩家 " + player.getName() + " 因 BoatFly 作弊被踢出");
                        vehicle.eject();
                        boatAirMoves.remove(playerId);
                    }
                }
            } else {
                boatAirMoves.remove(playerId);
            }
        }
    }

    /**
     * 处理玩家丢物品事件，检测 FastDrop 作弊
     */
    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        if (!isDetectionEnabled(player)) return;
        if (getConfig().getBoolean("fastdrop.enabled", true)) {
            long now = System.currentTimeMillis();
            long last = lastDropTime.getOrDefault(playerId, now - minDropInterval);
            if (now - last < minDropInterval) {
                int violations = fastDropViolations.getOrDefault(playerId, 0) + 1;
                fastDropViolations.put(playerId, violations);
                if (violations >= maxFastDropViolations) {
                    punish(player, getConfig().getString("fastdrop.kick-message", "检测到快速丢物品"));
                    getLogger().info("玩家 " + player.getName() + " 因 FastDrop 作弊被踢出");
                }
            }
            lastDropTime.put(playerId, now);
        }
    }

    /**
     * 处理实体捡物品事件，检测 FastPickup 作弊
     */
    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        UUID playerId = player.getUniqueId();
        if (!isDetectionEnabled(player)) return;
        if (getConfig().getBoolean("fastpickup.enabled", true)) {
            long now = System.currentTimeMillis();
            long last = lastPickupTime.getOrDefault(playerId, now - minPickupInterval);
            if (now - last < minPickupInterval) {
                int violations = fastPickupViolations.getOrDefault(playerId, 0) + 1;
                fastPickupViolations.put(playerId, violations);
                if (violations >= maxFastPickupViolations) {
                    punish(player, getConfig().getString("fastpickup.kick-message", "检测到快速捡物品"));
                    getLogger().info("玩家 " + player.getName() + " 因 FastPickup 作弊被踢出");
                }
            }
            lastPickupTime.put(playerId, now);
        }
    }

    /**
     * 处理抛射物发射事件，检测 FastProjectile 作弊
     */
    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (!(event.getEntity().getShooter() instanceof Player)) return;
        Player player = (Player) event.getEntity().getShooter();
        UUID playerId = player.getUniqueId();
        if (!isDetectionEnabled(player)) return;
        if (getConfig().getBoolean("fastprojectile.enabled", true)) {
            long now = System.currentTimeMillis();
            long last = lastProjectileTime.getOrDefault(playerId, now - minProjectileInterval);
            if (now - last < minProjectileInterval) {
                int violations = fastProjectileViolations.getOrDefault(playerId, 0) + 1;
                fastProjectileViolations.put(playerId, violations);
                if (violations >= maxFastProjectileViolations) {
                    punish(player, getConfig().getString("fastprojectile.kick-message", "检测到快速发射抛射物"));
                    getLogger().info("玩家 " + player.getName() + " 因 FastProjectile 作弊被踢出");
                }
            }
            lastProjectileTime.put(playerId, now);
        }
    }

    /**
     * 处理疾跑切换事件，检测 SprintSpam 作弊
     */
    @EventHandler
    public void onPlayerToggleSprint(PlayerToggleSprintEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        if (!isDetectionEnabled(player)) return;
        if (getConfig().getBoolean("sprintspam.enabled", true)) {
            long now = System.currentTimeMillis();
            long last = lastSprintToggleTime.getOrDefault(playerId, now - minSprintToggleInterval);
            if (now - last < minSprintToggleInterval) {
                int violations = sprintSpamViolations.getOrDefault(playerId, 0) + 1;
                sprintSpamViolations.put(playerId, violations);
                if (violations >= maxSprintSpamViolations) {
                    punish(player, getConfig().getString("sprintspam.kick-message", "检测到快速疾跑切换"));
                    getLogger().info("玩家 " + player.getName() + " 因 SprintSpam 作弊被踢出");
                }
            }
            lastSprintToggleTime.put(playerId, now);
        }
    }

    /**
     * 处理飞行切换事件，检测 FlightSpam 作弊
     */
    @EventHandler
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        if (!isDetectionEnabled(player)) return;
        if (getConfig().getBoolean("flightspam.enabled", true)) {
            long now = System.currentTimeMillis();
            long last = lastFlightToggleTime.getOrDefault(playerId, now - minFlightToggleInterval);
            if (now - last < minFlightToggleInterval) {
                int violations = flightSpamViolations.getOrDefault(playerId, 0) + 1;
                flightSpamViolations.put(playerId, violations);
                if (violations >= maxFlightSpamViolations) {
                    punish(player, getConfig().getString("flightspam.kick-message", "检测到快速飞行切换"));
                    getLogger().info("玩家 " + player.getName() + " 因 FlightSpam 作弊被踢出");
                }
            }
            lastFlightToggleTime.put(playerId, now);
        }
    }

    /**
     * 处理滑翔切换事件，检测 GlideSpam 作弊
     */
    @EventHandler
    public void onEntityToggleGlide(EntityToggleGlideEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        UUID playerId = player.getUniqueId();
        if (!isDetectionEnabled(player)) return;
        if (getConfig().getBoolean("glidespam.enabled", true)) {
            long now = System.currentTimeMillis();
            long last = lastGlideToggleTime.getOrDefault(playerId, now - minGlideToggleInterval);
            if (now - last < minGlideToggleInterval) {
                int violations = glideSpamViolations.getOrDefault(playerId, 0) + 1;
                glideSpamViolations.put(playerId, violations);
                if (violations >= maxGlideSpamViolations) {
                    punish(player, getConfig().getString("glidespam.kick-message", "检测到快速滑翔切换"));
                    getLogger().info("玩家 " + player.getName() + " 因 GlideSpam 作弊被踢出");
                }
            }
            lastGlideToggleTime.put(playerId, now);
        }
    }

    /**
     * 处理游泳切换事件，检测 SwimSpam 作弊
     */
    @EventHandler
    public void onEntityToggleSwim(EntityToggleSwimEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        UUID playerId = player.getUniqueId();
        if (!isDetectionEnabled(player)) return;
        if (getConfig().getBoolean("swimspam.enabled", true)) {
            long now = System.currentTimeMillis();
            long last = lastSwimToggleTime.getOrDefault(playerId, now - minSwimToggleInterval);
            if (now - last < minSwimToggleInterval) {
                int violations = swimSpamViolations.getOrDefault(playerId, 0) + 1;
                swimSpamViolations.put(playerId, violations);
                if (violations >= maxSwimSpamViolations) {
                    punish(player, getConfig().getString("swimspam.kick-message", "检测到快速游泳切换"));
                    getLogger().info("玩家 " + player.getName() + " 因 SwimSpam 作弊被踢出");
                }
            }
            lastSwimToggleTime.put(playerId, now);
        }
    }

    /**
     * 处理主副手切换事件，检测 SwapSpam 作弊
     */
    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        if (!isDetectionEnabled(player)) return;
        if (getConfig().getBoolean("swapspam.enabled", true)) {
            long now = System.currentTimeMillis();
            long last = lastSwapTime.getOrDefault(playerId, now - minSwapInterval);
            if (now - last < minSwapInterval) {
                int violations = swapSpamViolations.getOrDefault(playerId, 0) + 1;
                swapSpamViolations.put(playerId, violations);
                if (violations >= maxSwapSpamViolations) {
                    punish(player, getConfig().getString("swapspam.kick-message", "检测到快速主副手切换"));
                    getLogger().info("玩家 " + player.getName() + " 因 SwapSpam 作弊被踢出");
                }
            }
            lastSwapTime.put(playerId, now);
        }
    }

    /**
     * 处理饥饿值变化事件，检测 NoHunger 作弊
     */
    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        UUID playerId = player.getUniqueId();
        if (!isDetectionEnabled(player)) return;
        if (getConfig().getBoolean("nohunger.enabled", true)) {
            // 如果饥饿值本应下降但保持满值
            if (event.getFoodLevel() >= 20 && player.getFoodLevel() < 20) {
                int violations = noHungerViolations.getOrDefault(playerId, 0) + 1;
                noHungerViolations.put(playerId, violations);
                if (violations >= maxNoHungerViolations) {
                    punish(player, getConfig().getString("nohunger.kick-message", "检测到饥饿值异常"));
                    getLogger().info("玩家 " + player.getName() + " 因 NoHunger 作弊被踢出");
                }
            }
        }
    }

    /**
     * 处理剪羊毛事件，检测 FastShear 作弊
     */
    @EventHandler
    public void onPlayerShearEntity(PlayerShearEntityEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        if (!isDetectionEnabled(player)) return;
        if (getConfig().getBoolean("fastshear.enabled", true)) {
            long now = System.currentTimeMillis();
            long last = lastShearTime.getOrDefault(playerId, now - minShearInterval);
            if (now - last < minShearInterval) {
                int violations = fastShearViolations.getOrDefault(playerId, 0) + 1;
                fastShearViolations.put(playerId, violations);
                if (violations >= maxFastShearViolations) {
                    punish(player, getConfig().getString("fastshear.kick-message", "检测到快速剪羊毛"));
                    getLogger().info("玩家 " + player.getName() + " 因 FastShear 作弊被踢出");
                }
            }
            lastShearTime.put(playerId, now);
        }
    }

    /**
     * 处理倒空桶事件，检测 FastBucketEmpty 作弊
     */
    @EventHandler
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        if (!isDetectionEnabled(player)) return;
        if (getConfig().getBoolean("fastbucketempty.enabled", true)) {
            long now = System.currentTimeMillis();
            long last = lastBucketEmptyTime.getOrDefault(playerId, now - minBucketEmptyInterval);
            if (now - last < minBucketEmptyInterval) {
                int violations = fastBucketEmptyViolations.getOrDefault(playerId, 0) + 1;
                fastBucketEmptyViolations.put(playerId, violations);
                if (violations >= maxFastBucketEmptyViolations) {
                    punish(player, getConfig().getString("fastbucketempty.kick-message", "检测到快速倒空桶"));
                    getLogger().info("玩家 " + player.getName() + " 因 FastBucketEmpty 作弊被踢出");
                }
            }
            lastBucketEmptyTime.put(playerId, now);
        }
    }

    /**
     * 处理装满桶事件，检测 FastBucketFill 作弊
     */
    @EventHandler
    public void onPlayerBucketFill(PlayerBucketFillEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        if (!isDetectionEnabled(player)) return;
        if (getConfig().getBoolean("fastbucketfill.enabled", true)) {
            long now = System.currentTimeMillis();
            long last = lastBucketFillTime.getOrDefault(playerId, now - minBucketFillInterval);
            if (now - last < minBucketFillInterval) {
                int violations = fastBucketFillViolations.getOrDefault(playerId, 0) + 1;
                fastBucketFillViolations.put(playerId, violations);
                if (violations >= maxFastBucketFillViolations) {
                    punish(player, getConfig().getString("fastbucketfill.kick-message", "检测到快速装满桶"));
                    getLogger().info("玩家 " + player.getName() + " 因 FastBucketFill 作弊被踢出");
                }
            }
            lastBucketFillTime.put(playerId, now);
        }
    }

    /**
     * 处理上床事件，检测 BedSpam 作弊
     */
    @EventHandler
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        if (!isDetectionEnabled(player)) return;
        if (getConfig().getBoolean("bedspam.enabled", true)) {
            long now = System.currentTimeMillis();
            long last = lastBedTime.getOrDefault(playerId, now - minBedInterval);
            if (now - last < minBedInterval) {
                int violations = bedSpamViolations.getOrDefault(playerId, 0) + 1;
                bedSpamViolations.put(playerId, violations);
                if (violations >= maxBedSpamViolations) {
                    punish(player, getConfig().getString("bedspam.kick-message", "检测到快速上床"));
                    getLogger().info("玩家 " + player.getName() + " 因 BedSpam 作弊被踢出");
                }
            }
            lastBedTime.put(playerId, now);
        }
    }

    /**
     * 处理不死图腾触发事件，检测 AutoTotem 作弊
     */
    @EventHandler
    public void onEntityResurrect(EntityResurrectEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        UUID playerId = player.getUniqueId();
        if (!isDetectionEnabled(player)) return;
        if (getConfig().getBoolean("autototem.enabled", true)) {
            long now = System.currentTimeMillis();
            long last = lastTotemTime.getOrDefault(playerId, now - minTotemInterval);
            if (now - last < minTotemInterval) {
                int violations = autoTotemViolations.getOrDefault(playerId, 0) + 1;
                autoTotemViolations.put(playerId, violations);
                if (violations >= maxAutoTotemViolations) {
                    punish(player, getConfig().getString("autototem.kick-message", "检测到自动图腾"));
                    getLogger().info("玩家 " + player.getName() + " 因 AutoTotem 作弊被踢出");
                }
            }
            lastTotemTime.put(playerId, now);
        }
    }

    /**
     * 处理经验值变化事件，检测 FastExp 作弊
     */
    @EventHandler
    public void onPlayerExpChange(PlayerExpChangeEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        if (!isDetectionEnabled(player)) return;
        if (getConfig().getBoolean("fastexp.enabled", true)) {
            long now = System.currentTimeMillis();
            long last = lastExpTime.getOrDefault(playerId, now - minExpInterval);
            if (now - last < minExpInterval) {
                int violations = fastExpViolations.getOrDefault(playerId, 0) + 1;
                fastExpViolations.put(playerId, violations);
                if (violations >= maxFastExpViolations) {
                    punish(player, getConfig().getString("fastexp.kick-message", "检测到经验获取过快"));
                    getLogger().info("玩家 " + player.getName() + " 因 FastExp 作弊被踢出");
                }
            }
            lastExpTime.put(playerId, now);
        }
    }

    /**
     * 处理聊天事件，检测 ChatSpam 作弊（异步事件，踢出需调度到主线程）
     */
    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        if (!isDetectionEnabled(player)) return;
        if (getConfig().getBoolean("chatspam.enabled", true)) {
            long now = System.currentTimeMillis();
            long last = lastChatTime.getOrDefault(playerId, now - minChatInterval);
            if (now - last < minChatInterval) {
                int violations = chatSpamViolations.getOrDefault(playerId, 0) + 1;
                chatSpamViolations.put(playerId, violations);
                if (violations >= maxChatSpamViolations) {
                    final String msg = getConfig().getString("chatspam.kick-message", "检测到刷屏聊天");
                    org.bukkit.Bukkit.getScheduler().runTask(this, () -> punish(player, msg));
                    getLogger().info("玩家 " + player.getName() + " 因 ChatSpam 作弊被踢出");
                }
            }
            lastChatTime.put(playerId, now);
        }
    }

    /**
     * 处理命令预处理事件，检测 CommandSpam 作弊
     */
    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        if (!isDetectionEnabled(player)) return;
        if (getConfig().getBoolean("commandspam.enabled", true)) {
            long now = System.currentTimeMillis();
            long last = lastCommandTime.getOrDefault(playerId, now - minCommandInterval);
            if (now - last < minCommandInterval) {
                int violations = commandSpamViolations.getOrDefault(playerId, 0) + 1;
                commandSpamViolations.put(playerId, violations);
                if (violations >= maxCommandSpamViolations) {
                    punish(player, getConfig().getString("commandspam.kick-message", "检测到刷屏命令"));
                    getLogger().info("玩家 " + player.getName() + " 因 CommandSpam 作弊被踢出");
                }
            }
            lastCommandTime.put(playerId, now);
        }
    }

    /**
     * 处理丢鸡蛋事件，检测 FastEgg 作弊
     */
    @EventHandler
    public void onPlayerEggThrow(PlayerEggThrowEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        if (!isDetectionEnabled(player)) return;
        if (getConfig().getBoolean("fastegg.enabled", true)) {
            long now = System.currentTimeMillis();
            long last = lastEggTime.getOrDefault(playerId, now - minEggInterval);
            if (now - last < minEggInterval) {
                int violations = fastEggViolations.getOrDefault(playerId, 0) + 1;
                fastEggViolations.put(playerId, violations);
                if (violations >= maxFastEggViolations) {
                    punish(player, getConfig().getString("fastegg.kick-message", "检测到快速丢鸡蛋"));
                    getLogger().info("玩家 " + player.getName() + " 因 FastEgg 作弊被踢出");
                }
            }
            lastEggTime.put(playerId, now);
        }
    }

    /**
     * 构建 /antibitch Brigadier 命令树（Paper 1.20.5+ 新命令 API）。
     * 子节点：reload、status、version；均要求 antibitch.admin 权限。
     *
     * @return 命令树的根节点
     */
    private LiteralCommandNode<io.papermc.paper.command.brigadier.CommandSourceStack> buildAntiBitchCommand() {
        return Commands.literal("antibitch")
                .requires(ctx -> ctx.getSender().hasPermission("antibitch.admin"))
                .executes(ctx -> {
                    CommandSender sender = ctx.getSource().getSender();
                    sender.sendMessage(ChatColor.GOLD + "=== AntiBitch 插件管理 ===");
                    sender.sendMessage(ChatColor.YELLOW + "/antibitch reload - 重载配置文件");
                    sender.sendMessage(ChatColor.YELLOW + "/antibitch status - 查看插件状态");
                    sender.sendMessage(ChatColor.YELLOW + "/antibitch version - 查看插件版本");
                    sender.sendMessage(ChatColor.YELLOW + "/antibitch on - 启用全局检测");
                    sender.sendMessage(ChatColor.YELLOW + "/antibitch off - 禁用全局检测");
                    sender.sendMessage(ChatColor.YELLOW + "/antibitch toggle <player> - 切换玩家检测开关");
                    sender.sendMessage(ChatColor.YELLOW + "/antibitch list - 查看开关状态");
                    return 1;
                })
                .then(Commands.literal("reload")
                        .executes(ctx -> {
                            CommandSender sender = ctx.getSource().getSender();
                            reloadConfig();
                            loadConfigValues();
                            sender.sendMessage(ChatColor.GREEN + "配置文件已重载！");
                            getLogger().info(sender.getName() + " 重载了配置文件");
                            return 1;
                        }))
                .then(Commands.literal("status")
                        .executes(ctx -> {
                            CommandSender sender = ctx.getSource().getSender();
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
                            sender.sendMessage(ChatColor.YELLOW + "Nuker 检测: " + (getConfig().getBoolean("nuker.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "FastBreak 检测: " + (getConfig().getBoolean("fastbreak.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "FastEat 检测: " + (getConfig().getBoolean("fasteat.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "WaterWalk 检测: " + (getConfig().getBoolean("waterwalk.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "Glide 检测: " + (getConfig().getBoolean("glide.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "Step 检测: " + (getConfig().getBoolean("step.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "AntiKnockback 检测: " + (getConfig().getBoolean("antiknockback.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "NoSwing 检测: " + (getConfig().getBoolean("noswing.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "Spider 检测: " + (getConfig().getBoolean("spider.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "NoWeb 检测: " + (getConfig().getBoolean("noweb.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "FastLadder 检测: " + (getConfig().getBoolean("fastladder.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "BoatFly 检测: " + (getConfig().getBoolean("boatfly.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "HighJump 检测: " + (getConfig().getBoolean("highjump.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "Dolphin 检测: " + (getConfig().getBoolean("dolphin.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "Phase 检测: " + (getConfig().getBoolean("phase.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "Blink 检测: " + (getConfig().getBoolean("blink.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "FastSneak 检测: " + (getConfig().getBoolean("fastsneak.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "Derp 检测: " + (getConfig().getBoolean("derp.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "ElytraSpeed 检测: " + (getConfig().getBoolean("elytraspeed.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "XRay 检测: " + (getConfig().getBoolean("xray.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "Tower 检测: " + (getConfig().getBoolean("tower.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "ChestStealer 检测: " + (getConfig().getBoolean("cheststealer.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "AutoArmor 检测: " + (getConfig().getBoolean("autoarmor.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "AutoFish 检测: " + (getConfig().getBoolean("autofish.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "FastSprint 检测: " + (getConfig().getBoolean("fastsprint.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "IceSpeed 检测: " + (getConfig().getBoolean("icespeed.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "Bhop 检测: " + (getConfig().getBoolean("bhop.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "AirJump 检测: " + (getConfig().getBoolean("airjump.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "Jetpack 检测: " + (getConfig().getBoolean("jetpack.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "FastClimbVine 检测: " + (getConfig().getBoolean("fastclimbvine.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "FastDescend 检测: " + (getConfig().getBoolean("fastdescend.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "Strafe 检测: " + (getConfig().getBoolean("strafe.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "Float 检测: " + (getConfig().getBoolean("float.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "FastSneakAir 检测: " + (getConfig().getBoolean("fastsneakair.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "HeadRoll 检测: " + (getConfig().getBoolean("headroll.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "TeleportUp 检测: " + (getConfig().getBoolean("teleportup.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "ReachVertical 检测: " + (getConfig().getBoolean("reachvertical.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "AttackThroughWall 检测: " + (getConfig().getBoolean("attackthroughwall.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "CriticalFake 检测: " + (getConfig().getBoolean("criticalfake.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "TriggerBot 检测: " + (getConfig().getBoolean("triggerbot.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "NoCooldown 检测: " + (getConfig().getBoolean("nocooldown.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "MultiAttack 检测: " + (getConfig().getBoolean("multiattack.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "SnapAim 检测: " + (getConfig().getBoolean("snapaim.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "AttackWhileSprinting 检测: " + (getConfig().getBoolean("attackwhilesprinting.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "FastInteract 检测: " + (getConfig().getBoolean("fastinteract.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "FastDoor 检测: " + (getConfig().getBoolean("fastdoor.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "FastFenceGate 检测: " + (getConfig().getBoolean("fastfencegate.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "FastLever 检测: " + (getConfig().getBoolean("fastlever.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "FastButton 检测: " + (getConfig().getBoolean("fastbutton.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "FastTrapdoor 检测: " + (getConfig().getBoolean("fasttrapdoor.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "AutoPot 检测: " + (getConfig().getBoolean("autopot.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "FastBucket 检测: " + (getConfig().getBoolean("fastbucket.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "FastPotion 检测: " + (getConfig().getBoolean("fastpotion.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "FastMilk 检测: " + (getConfig().getBoolean("fastmilk.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "FastHoney 检测: " + (getConfig().getBoolean("fasthoney.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "BreakReach 检测: " + (getConfig().getBoolean("breakreach.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "FastOre 检测: " + (getConfig().getBoolean("fastore.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "BreakWhileMoving 检测: " + (getConfig().getBoolean("breakwhilemoving.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "PlaceReach 检测: " + (getConfig().getBoolean("placereach.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "FastPlace 检测: " + (getConfig().getBoolean("fastplace.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "FastShiftClick 检测: " + (getConfig().getBoolean("fastshiftclick.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "FastHotbarSwap 检测: " + (getConfig().getBoolean("fasthotbarswap.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "SneakSpam 检测: " + (getConfig().getBoolean("sneakspam.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "FastDrop 检测: " + (getConfig().getBoolean("fastdrop.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "FastPickup 检测: " + (getConfig().getBoolean("fastpickup.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "FastProjectile 检测: " + (getConfig().getBoolean("fastprojectile.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "SprintSpam 检测: " + (getConfig().getBoolean("sprintspam.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "FlightSpam 检测: " + (getConfig().getBoolean("flightspam.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "GlideSpam 检测: " + (getConfig().getBoolean("glidespam.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "SwimSpam 检测: " + (getConfig().getBoolean("swimspam.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "SwapSpam 检测: " + (getConfig().getBoolean("swapspam.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "NoHunger 检测: " + (getConfig().getBoolean("nohunger.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "FastShear 检测: " + (getConfig().getBoolean("fastshear.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "FastBucketEmpty 检测: " + (getConfig().getBoolean("fastbucketempty.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "FastBucketFill 检测: " + (getConfig().getBoolean("fastbucketfill.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "BedSpam 检测: " + (getConfig().getBoolean("bedspam.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "AutoTotem 检测: " + (getConfig().getBoolean("autototem.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "FastExp 检测: " + (getConfig().getBoolean("fastexp.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "ChatSpam 检测: " + (getConfig().getBoolean("chatspam.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "CommandSpam 检测: " + (getConfig().getBoolean("commandspam.enabled") ? "启用" : "禁用"));
                            sender.sendMessage(ChatColor.YELLOW + "FastEgg 检测: " + (getConfig().getBoolean("fastegg.enabled") ? "启用" : "禁用"));
                            return 1;
                        }))
                .then(Commands.literal("version")
                        .executes(ctx -> {
                            CommandSender sender = ctx.getSource().getSender();
                            sender.sendMessage(ChatColor.GOLD + "AntiBitch v" + getDescription().getVersion());
                            return 1;
                        }))
                .then(Commands.literal("on")
                        .executes(ctx -> {
                            CommandSender sender = ctx.getSource().getSender();
                            getConfig().set("settings.enabled", true);
                            saveConfig();
                            sender.sendMessage(ChatColor.GREEN + "AntiBitch 全局检测已启用");
                            getLogger().info(sender.getName() + " 启用了全局检测");
                            return 1;
                        }))
                .then(Commands.literal("off")
                        .executes(ctx -> {
                            CommandSender sender = ctx.getSource().getSender();
                            getConfig().set("settings.enabled", false);
                            saveConfig();
                            sender.sendMessage(ChatColor.RED + "AntiBitch 全局检测已禁用");
                            getLogger().info(sender.getName() + " 禁用了全局检测");
                            return 1;
                        }))
                .then(Commands.literal("toggle")
                        .then(Commands.argument("player", com.mojang.brigadier.arguments.StringArgumentType.word())
                                .executes(ctx -> {
                                    CommandSender sender = ctx.getSource().getSender();
                                    String name = com.mojang.brigadier.arguments.StringArgumentType.getString(ctx, "player");
                                    Player target = Bukkit.getPlayerExact(name);
                                    if (target == null) {
                                        sender.sendMessage(ChatColor.RED + "找不到玩家: " + name);
                                        return 0;
                                    }
                                    UUID tid = target.getUniqueId();
                                    if (isGloballyEnabled()) {
                                        // 全局开：在 disabledPlayers 黑名单中切换
                                        if (disabledPlayers.contains(tid)) {
                                            disabledPlayers.remove(tid);
                                            sender.sendMessage(ChatColor.GREEN + "已为 " + target.getName() + " 启用检测");
                                            getLogger().info(sender.getName() + " 为 " + target.getName() + " 启用了检测");
                                        } else {
                                            disabledPlayers.add(tid);
                                            sender.sendMessage(ChatColor.YELLOW + "已为 " + target.getName() + " 禁用检测");
                                            getLogger().info(sender.getName() + " 为 " + target.getName() + " 禁用了检测");
                                        }
                                    } else {
                                        // 全局关：在 enabledPlayers 白名单中切换
                                        if (enabledPlayers.contains(tid)) {
                                            enabledPlayers.remove(tid);
                                            sender.sendMessage(ChatColor.YELLOW + "已为 " + target.getName() + " 禁用检测");
                                            getLogger().info(sender.getName() + " 为 " + target.getName() + " 禁用了检测");
                                        } else {
                                            enabledPlayers.add(tid);
                                            sender.sendMessage(ChatColor.GREEN + "已为 " + target.getName() + " 启用检测");
                                            getLogger().info(sender.getName() + " 为 " + target.getName() + " 启用了检测");
                                        }
                                    }
                                    return 1;
                                })))
                .then(Commands.literal("list")
                        .executes(ctx -> {
                            CommandSender sender = ctx.getSource().getSender();
                            sender.sendMessage(ChatColor.YELLOW + "全局检测: " + (isGloballyEnabled() ? ChatColor.GREEN + "启用" : ChatColor.RED + "禁用"));
                            if (isGloballyEnabled()) {
                                if (disabledPlayers.isEmpty()) {
                                    sender.sendMessage(ChatColor.YELLOW + "当前没有玩家被单独禁用检测");
                                } else {
                                    sender.sendMessage(ChatColor.GOLD + "=== 检测已禁用的玩家 ===");
                                    for (UUID id : disabledPlayers) {
                                        Player p = Bukkit.getPlayer(id);
                                        String name = p != null ? p.getName() : id.toString();
                                        sender.sendMessage(ChatColor.YELLOW + "- " + name);
                                    }
                                }
                            } else {
                                if (enabledPlayers.isEmpty()) {
                                    sender.sendMessage(ChatColor.YELLOW + "当前没有玩家被单独启用检测");
                                } else {
                                    sender.sendMessage(ChatColor.GOLD + "=== 检测已启用的玩家 ===");
                                    for (UUID id : enabledPlayers) {
                                        Player p = Bukkit.getPlayer(id);
                                        String name = p != null ? p.getName() : id.toString();
                                        sender.sendMessage(ChatColor.YELLOW + "- " + name);
                                    }
                                }
                            }
                            return 1;
                        }))
                .build();
    }
}
