package awesome.praenyth.plugins.kamikaze;

import awesome.praenyth.plugins.kamikaze.events.KillListener;
import awesome.praenyth.plugins.kamikaze.events.DangerPlacementListener;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public final class Kamikaze extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new KillListener(this), this);
        getServer().getPluginManager().registerEvents(new DangerPlacementListener(this), this);

    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }
}
