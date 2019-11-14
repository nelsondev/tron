package com.nelsontron.scratch.listeners.stats;

import com.nelsontron.scratch.ScratcherHolder;
import com.nelsontron.scratch.entity.Scratcher;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockDestroyed implements Listener {
    private ScratcherHolder holder;

    public BlockDestroyed(ScratcherHolder holder) {
        this.holder = holder;
    }

    @EventHandler
    public void blockDestroyed(BlockBreakEvent ev) {
        Player player = ev.getPlayer();
        Scratcher scratcher = holder.getScratcher(player);
        scratcher.getStats().incrementBlocksDestroyed();
    }
}
