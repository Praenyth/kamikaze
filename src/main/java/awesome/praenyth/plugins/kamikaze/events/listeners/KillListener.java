package awesome.praenyth.plugins.kamikaze.events.listeners;

import awesome.praenyth.plugins.kamikaze.events.AnyKillEvent;
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
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KillListener implements Listener {

    private static Map<UUID, Boolean> playersToBeKilled = new HashMap<>();

    private final Plugin plugin;

    public KillListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void playerOtherSourceDamageListener(EntityDamageEvent e) {

        if (!e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
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

                            for (Player player:
                                    Bukkit.getOnlinePlayers()) {
                                player.sendMessage(ChatColor.RED + attacker.getDisplayName() + " has attempted to kill " + victim.getDisplayName() + "!");
                            }

                            Firework firework = attacker.getWorld().spawn(attacker.getLocation(), Firework.class);
                            FireworkMeta fireworkMeta = firework.getFireworkMeta();
                            fireworkMeta.setPower(2);
                            fireworkMeta.addEffect(FireworkEffect.builder()
                                    .withColor(Color.FUCHSIA, Color.BLACK)
                                    .withColor(Color.FUCHSIA, Color.BLACK)
                                    .withColor(Color.FUCHSIA, Color.BLACK)
                                    .with(FireworkEffect.Type.BALL)
                                    .trail(true)
                                    .build());
                            firework.setFireworkMeta(fireworkMeta);

                            firework.addPassenger(attacker);
                            playersToBeKilled.put(attacker.getUniqueId(), true);

                            Bukkit.getScheduler().runTaskLater(plugin, () -> {

                                if (attacker.isOnline()) {
                                    attacker.setHealth(0f);
                                    playersToBeKilled.put(attacker.getUniqueId(), false);
                                } else {
                                    plugin.getLogger().info(attacker.getDisplayName()+" tried to log out to escape death!");
                                }


                            }, 40);
                            victim.setLastDamageCause(e);

                        }

                    }
                }
            }
        }

    }

    @EventHandler
    public void playerAttackListener(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            Player victim = (Player) e.getEntity();
            Player attacker = (Player) e.getDamager();

            if (e.getFinalDamage() >= victim.getHealth()) {

                victim.setHealth(20f);

                e.setCancelled(true);

                for (Player player:
                        Bukkit.getOnlinePlayers()) {
                    player.sendMessage(ChatColor.RED + attacker.getDisplayName() + " has attempted to kill " + victim.getDisplayName() + "!");
                }

                Firework firework = attacker.getWorld().spawn(attacker.getLocation(), Firework.class);
                FireworkMeta fireworkMeta = firework.getFireworkMeta();
                fireworkMeta.setPower(2);
                fireworkMeta.addEffect(FireworkEffect.builder()
                        .withColor(Color.FUCHSIA, Color.BLACK)
                        .withColor(Color.FUCHSIA, Color.BLACK)
                        .withColor(Color.FUCHSIA, Color.BLACK)
                        .with(FireworkEffect.Type.BALL)
                        .trail(true)
                        .build());
                firework.setFireworkMeta(fireworkMeta);

                firework.addPassenger(attacker);
                playersToBeKilled.put(attacker.getUniqueId(), true);

                Bukkit.getScheduler().runTaskLater(plugin, () -> {

                    if (attacker.isOnline()) {
                        attacker.setHealth(0f);
                        playersToBeKilled.put(attacker.getUniqueId(), false);
                    } else {
                        plugin.getLogger().info(attacker.getDisplayName()+" tried to log out to escape death!");
                    }


                }, 40);
                victim.setLastDamageCause(new EntityDamageEvent(victim, EntityDamageEvent.DamageCause.CUSTOM, 0));

            }

        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        if (playersToBeKilled.containsKey(player.getUniqueId())) {
            if (playersToBeKilled.get(player.getUniqueId())) {
                for (Player pl : Bukkit.getOnlinePlayers()) {
                    pl.sendMessage(ChatColor.RED+player.getDisplayName()+" tried to log out to avoid being killed :3");
                }
                player.setHealth(0f);
                playersToBeKilled.put(player.getUniqueId(), false);
            }
        } else {
            playersToBeKilled.put(player.getUniqueId(), false);
        }
    }

}
