package com.nelsontron.scratch.listeners.stats;

import com.nelsontron.scratch.ScratcherHolder;
import com.nelsontron.scratch.entity.Scratcher;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageTaken implements Listener {
    private ScratcherHolder holder;

    public DamageTaken(ScratcherHolder holder) {
        this.holder = holder;
    }

    @EventHandler
    public void playerDamageTaken(EntityDamageEvent ev) {

        if (!(ev.getEntity() instanceof Player))
            return;

        Player player = (Player) ev.getEntity();
        Scratcher scratcher = holder.getScratcher(player);
        scratcher.getStats().incrementDamageTaken();
    }
}
