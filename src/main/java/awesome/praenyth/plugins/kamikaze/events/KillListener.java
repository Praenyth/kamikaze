package awesome.praenyth.plugins.kamikaze.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.Plugin;

public class KillListener implements Listener {

    private final Plugin plugin;

    public KillListener(Plugin plugin) {
        this.plugin = plugin;
    }

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

                        e.setCancelled(true);
                        event1.setCancelled(true);
                        event.setCancelled(true);

                        for (Player player:
                                Bukkit.getOnlinePlayers()) {
                            player.sendMessage(ChatColor.RED + attacker.getDisplayName() + " has attempted to kill " + victim.getDisplayName() + "!");
                        }

                        Firework firework = attacker.getWorld().spawn(attacker.getLocation(), Firework.class);
                        FireworkMeta fireworkMeta = firework.getFireworkMeta();
                        fireworkMeta.setPower(2);
                        fireworkMeta.addEffect(FireworkEffect.builder().withColor(Color.FUCHSIA, Color.BLACK).withColor(Color.FUCHSIA, Color.BLACK).withColor(Color.FUCHSIA, Color.BLACK).with(FireworkEffect.Type.BALL).trail(true).build());
                        firework.setFireworkMeta(fireworkMeta);

                        firework.addPassenger(attacker);

                        Bukkit.getScheduler().runTaskLater(plugin, () -> {

                            attacker.setHealth(0f);

                        }, 40);

                    }
                }
            }
        }

    }

}
