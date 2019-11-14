package com.nelsontron.scratch.listeners.stats;

import com.nelsontron.scratch.ScratcherHolder;
import com.nelsontron.scratch.entity.Scratcher;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class ConsumedItem implements Listener {
    private ScratcherHolder holder;

    public ConsumedItem(ScratcherHolder holder) {
        this.holder = holder;
    }

    @EventHandler
    public void playerItemConsume(PlayerItemConsumeEvent ev) {
        Player player = ev.getPlayer();
        Scratcher scratcher = holder.getScratcher(player);
        scratcher.getStats().incrementConsumedItems();
    }
}
