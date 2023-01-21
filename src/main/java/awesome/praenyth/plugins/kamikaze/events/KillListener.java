package awesome.praenyth.plugins.kamikaze.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class KillListener implements Listener {

    @EventHandler
    public void playerOtherSourceDamageListener(EntityDamageEvent e) {

        if (e.getEntity() instanceof Player) {
            Player victim = (Player) e.getEntity();
            EntityDamageEvent event = victim.getLastDamageCause();
            if (event instanceof EntityDamageByEntityEvent) {
                EntityDamageByEntityEvent event1 = (EntityDamageByEntityEvent) event;
                if (event1.getDamager() instanceof Player) {
                    Player attacker = (Player) event1.getDamager();

                    if (e.getFinalDamage() >= victim.getHealth()) {

                        victim.setHealth(20f);
                        attacker.setHealth(0f);

                        e.setCancelled(true);
                        event1.setCancelled(true);
                        event.setCancelled(true);

                        for (Player player:
                                Bukkit.getOnlinePlayers()) {
                            player.sendMessage(ChatColor.RED + attacker.getDisplayName() + " has attempted to kill " + victim.getDisplayName() + "!");
                        }

                    }
                }
            }
        }

    }

}
