package com.nelsontron.scratch.listeners.stats;

import com.nelsontron.scratch.ScratcherHolder;
import com.nelsontron.scratch.entity.Scratcher;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeath implements Listener {
    private ScratcherHolder holder;

    public PlayerDeath(ScratcherHolder holder) {
        this.holder = holder;
    }

    @EventHandler
    public void playerDeath(PlayerDeathEvent ev) {
        Player player = ev.getEntity();
        Scratcher scratcher = holder.getScratcher(player);
        scratcher.getStats().incrementDeaths();
    }
}
