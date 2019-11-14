package com.nelsontron.scratch.listeners.stats;

import com.nelsontron.scratch.ScratcherHolder;
import com.nelsontron.scratch.entity.Scratcher;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class MobKill implements Listener {
    private ScratcherHolder holder;

    public MobKill(ScratcherHolder holder) {
        this.holder = holder;
    }

    @EventHandler
    public void playerKillMob(EntityDeathEvent ev) {

        if (ev.getEntity().getKiller() == null)
            return;

        Player player = ev.getEntity().getKiller();
        Scratcher scratcher = holder.getScratcher(player);
        scratcher.getStats().incrementMobKills();
    }
}
