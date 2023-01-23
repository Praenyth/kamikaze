package awesome.praenyth.plugins.kamikaze.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AnyKillEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player victim;
    private final Player attacker;
    private final double finalDamage;
    private boolean isCancelled;

    public AnyKillEvent(Player victim, Player attacker, double finalDamage) {
        this.victim = victim;
        this.attacker = attacker;
        this.finalDamage = finalDamage;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public Player getVictim() {
        return victim;
    }

    public Player getAttacker() {
        return attacker;
    }

    public double getFinalDamage() {
        return finalDamage;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

}
