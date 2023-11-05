package nb114514.antibitch;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AntiBitch extends JavaPlugin implements Listener {
    private final Map<UUID, Integer> reachViolations = new HashMap<>();
    private final Map<Player, Long> moveTimes = new HashMap<>();
    private final Map<Player, Integer> moveCount = new HashMap<>();
    private final int MAX_REACH_VIOLATIONS = 5; // 设置Reach违规值
    private final double MAX_REACH = 3.0D; // 设置最大可接受距离

    @Override
    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        System.out.println("刘艳插件已启用！");
    }

    @Override
    public void onDisable() {
        System.out.println("刘艳插件已禁用！");
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;

        Player player = (Player) event.getDamager();
        Entity target = event.getEntity();
        double distance = player.getLocation().distance(target.getLocation());

        if (distance > MAX_REACH) {
            UUID playerId = player.getUniqueId();
            int vl = reachViolations.getOrDefault(playerId, 0) + 1;
            reachViolations.put(playerId, vl);

            if (vl >= MAX_REACH_VIOLATIONS) {
                player.kickPlayer("Reach狗死妈妈！！！");
                reachViolations.remove(playerId);
            } else {
                String reachMessage = "刘艳AC §8==>> §f" + player.getName() + "§r 触发了 §9Reach！§8(§8blocks=§r" + distance + "§8)";
                Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', reachMessage));
            }
        }
    }

    // 合并了Speed和Fly检测的onMove方法
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.isFlying() || player.isOnGround()) {
            moveTimes.remove(player);
            return;
        }

        long now = System.currentTimeMillis();
        long last = moveTimes.getOrDefault(player, now);
        int moveDistance = (int) Math.round(event.getTo().distance(event.getFrom()));
        int moveTime = (int) (now - last);
        int speed = moveDistance * 1000 / moveTime;

        if (speed > 20) {
            player.kickPlayer("MD在服务器里前进四是吧");
        } else {
            moveTimes.put(player, now);
            int count = moveCount.getOrDefault(player, 0) + 1;
            moveCount.put(player, count);
            if (count > 10) {
                player.kickPlayer("此服务器未启用飞行！");
            }
        }
    }
}
