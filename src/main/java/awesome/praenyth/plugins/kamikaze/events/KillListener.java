package awesome.praenyth.plugins.kamikaze.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.HashMap;
import java.util.Map;

public class KillListener implements Listener {

    public static Map<String, String> victimAttackerMap = new HashMap<>();

    @EventHandler
    public void playerKillListener(PlayerDeathEvent e) {

        Player victim = e.getEntity();
        Player killer = e.getEntity().getKiller();

        if (killer != null && !killer.getDisplayName().equals(victim.getDisplayName())) {

            Location victimLastLocation = victim.getLocation();
            e.setKeepInventory(true);
            e.getDrops().clear();
            e.setKeepLevel(true);
            victim.setHealth(20f);
            victim.teleport(victimLastLocation);
            e.setDeathMessage("");

            killer.damage(2000f);
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(ChatColor.RED + killer.getDisplayName() + " attempted to kill " + victim.getDisplayName() + ".");
            }

            if (killer.getDisplayName() != null) {
                victimAttackerMap.remove(killer.getDisplayName());
            }
            if (victim.getDisplayName() != null) {
                victimAttackerMap.remove(victim.getDisplayName());
            }
        }
    }

    @EventHandler
    public void playerPostAttackListener(EntityDamageEvent e) {

        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();

            if (e.getFinalDamage() >= player.getHealth()) {
                if (victimAttackerMap.containsKey(player.getDisplayName())) {
                    Player killer = Bukkit.getPlayer(victimAttackerMap.get(player.getDisplayName()));
                    victimAttackerMap.remove(player.getDisplayName());
                    killer.damage(3000f);
                    e.setDamage(0);
                    player.setHealth(20f);
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.sendMessage(ChatColor.RED + killer.getDisplayName() + " attempted to kill " + player.getDisplayName() + ".");
                    }
                }
            }
        }

    }

    @EventHandler
    public void playerPreAttackListener(EntityDamageByEntityEvent e) {

        Entity eVictim = e.getEntity();
        Entity eAttacker = e.getDamager();

        if (eAttacker instanceof Player && eVictim instanceof Player) {

            Player victim = (Player) eVictim;
            Player attacker = (Player) eAttacker;

            if (e.getFinalDamage() >= victim.getHealth()) {

                if (!victimAttackerMap.containsKey(victim.getDisplayName()) && !victimAttackerMap.containsKey(attacker.getDisplayName())) {
                    victimAttackerMap.put(victim.getDisplayName(), attacker.getDisplayName());
                    victimAttackerMap.remove(attacker.getDisplayName(), victim.getDisplayName());
                }

            }

        }

    }

}
