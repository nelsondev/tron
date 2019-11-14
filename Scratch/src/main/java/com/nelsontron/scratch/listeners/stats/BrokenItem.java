package com.nelsontron.scratch.listeners.stats;

import com.nelsontron.scratch.ScratcherHolder;
import com.nelsontron.scratch.entity.Scratcher;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemBreakEvent;

public class BrokenItem implements Listener {
    private ScratcherHolder holder;

    public BrokenItem(ScratcherHolder holder) {
        this.holder = holder;
    }

    @EventHandler
    public void itemBroken(PlayerItemBreakEvent ev) {
        Player player = ev.getPlayer();
        Scratcher scratcher = holder.getScratcher(player);
        scratcher.getStats().incrementBrokenItems();
    }
}
