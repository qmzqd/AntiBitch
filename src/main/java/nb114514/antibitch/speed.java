package nb114514.antibitch;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class speed extends JavaPlugin implements Listener {
    private final Map<Player, Long> moveTimes = new HashMap<>();
    private final Map<Player, Integer> moveCount = new HashMap<>();

    @Override
    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
    }

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