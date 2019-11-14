package com.nelsontron.scratch.listeners.stats;

import com.nelsontron.scratch.ScratcherHolder;
import com.nelsontron.scratch.entity.Scratcher;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class BlockTravelled implements Listener {
    private ScratcherHolder holder;

    public BlockTravelled(ScratcherHolder holder) {
        this.holder = holder;
    }

    @EventHandler
    public void playerMove(PlayerMoveEvent ev) {
        Player player = ev.getPlayer();
        Scratcher scratcher = holder.getScratcher(player);
        if ((ev.getTo().getBlockX() != ev.getFrom().getBlockX()) || (ev.getTo().getBlockZ() != ev.getFrom().getBlockZ())) {
            scratcher.getStats().incrementBlocksTravelled();
        }
    }
}
