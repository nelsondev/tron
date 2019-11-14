package com.nelsontron.scratch.listeners.stats;

import com.nelsontron.scratch.ScratcherHolder;
import com.nelsontron.scratch.entity.Scratcher;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageGiven implements Listener {
    private ScratcherHolder holder;

    public DamageGiven(ScratcherHolder holder) {
        this.holder = holder;
    }

    @EventHandler
    public void playerDamageGiven(EntityDamageByEntityEvent ev) {

        if (!(ev.getDamager() instanceof Player))
            return;

        Player player = (Player) ev.getDamager();
        Scratcher scratcher = holder.getScratcher(player);
        scratcher.getStats().incrementDamageGiven();
    }
}
