package awesome.praenyth.plugins.kamikaze;

import awesome.praenyth.plugins.kamikaze.events.KillListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Kamikaze extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new KillListener(), this);

    }

    @Override
    public void onDisable() {

    }
}
