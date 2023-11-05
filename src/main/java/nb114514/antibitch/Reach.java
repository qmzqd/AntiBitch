package nb114514.antibitch;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public final class Reach extends JavaPlugin implements Listener {
    private final HashMap<UUID, Integer> violations = new HashMap<>();
    private final int MAX_VIOLATIONS = 5; // 设置违规值

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            Entity target = event.getEntity();
            double maxReach = 3.0D;
            double distance = player.getLocation().distance(target.getLocation());
            if (distance > maxReach) {
                UUID playerId = player.getUniqueId();
                int vl = violations.getOrDefault(playerId, 0) + 1; // 增加违规次数
                violations.put(playerId, vl); // 更新违规次数

                if (vl >= MAX_VIOLATIONS) {
                    player.kickPlayer("Reach狗死妈妈！！！");
                    violations.remove(playerId); // 踢出玩家后重置违规次数
                } else {
                    String reachMessage = "刘艳AC §8==>> §f" + player.getName() + "§r 触发了 §9Reach！§8(§8blocks=§r" + distance + "§8)";
                    Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', reachMessage));
                }
            }
        }
    }
}