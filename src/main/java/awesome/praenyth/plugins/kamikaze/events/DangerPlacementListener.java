package awesome.praenyth.plugins.kamikaze.events;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.ExplosiveMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.plugin.Plugin;

public class DangerPlacementListener implements Listener {

    private Plugin plugin;

    public DangerPlacementListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onLavaPlacement(PlayerBucketEmptyEvent e) {

        Player player = e.getPlayer();
        World world = player.getWorld();

        for (Entity entity : world.getNearbyEntities(e.getBlock().getLocation(), 2, 2, 2)) {
            if (entity instanceof Player) {
                if (!entity.equals(player)) {
                    player.sendMessage(ChatColor.RED + "Move that thing away from other people thanks!");
                    e.setCancelled(true);
                }
            }
        }

    }

    @EventHandler
    public void onFNSPlacement(PlayerInteractEvent e) {

        Player player = e.getPlayer();
        World world = player.getWorld();

        if (e.hasItem()) {
            if (e.getItem().getType().equals(Material.FLINT_AND_STEEL)) {
                if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                    for (Entity entity : world.getNearbyEntities(e.getClickedBlock().getLocation(), 2, 3, 2)) {
                        if (entity instanceof Player) {
                            if (!entity.equals(player)) {
                                player.sendMessage(ChatColor.RED + "Move that thing away from other people thanks!");
                                e.setCancelled(true);
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onTNTMinecartPlacement(VehicleCreateEvent e) {
        if (e.getVehicle() instanceof ExplosiveMinecart) {
            for (Entity entity : e.getVehicle().getNearbyEntities(5, 5, 5)) {
                entity.sendMessage(ChatColor.RED + "Yeah can you not");
                entity.sendMessage(ChatColor.RED + "also this is going to explode in 3 seconds");
            }

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                e.getVehicle().getWorld().createExplosion(e.getVehicle().getLocation(), 5);
            }, 60);
        }
    }

    @EventHandler
    public void onTNTPlacement(BlockPlaceEvent e) {
        if (e.getBlockPlaced().getType().equals(Material.TNT)) {
            Location location = e.getBlockPlaced().getLocation();
            World world = location.getWorld();

            world.createExplosion(location, 3);
            world.getBlockAt(location).setType(Material.AIR);

            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(e.getPlayer().getDisplayName()+ " tried to place bombs but wired them incorrectly");
            }
        }
    }

    @EventHandler
    public void onDripstonePlacement(BlockPlaceEvent e) {

        if (e.getBlockPlaced().getType().equals(Material.POINTED_DRIPSTONE)) {
            Player placer = e.getPlayer();
            Location location = e.getBlockPlaced().getLocation();

            location.getWorld().getBlockAt(location).setType(Material.AIR);
            location.getWorld().getBlockAt(location.clone().add(0,1,0)).setType(Material.AIR);
            location.getWorld().generateTree(location, TreeType.TREE);
            placer.sendMessage(ChatColor.AQUA+"Have a tree instead :grin:");
        }

    }


}
