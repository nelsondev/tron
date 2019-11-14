package com.nelsontron.scratch.listeners.scratcher;

import com.nelsontron.scratch.ScratcherHolder;
import com.nelsontron.scratch.entity.Scratcher;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {
    private ScratcherHolder holder;

    public PlayerQuit(ScratcherHolder holder) {
        this.holder = holder;
    }

    @EventHandler
    public void playerQuit(PlayerQuitEvent ev) {
        Player player = ev.getPlayer();
        Scratcher scratcher = holder.getScratcher(player);
        scratcher.save();
        holder.unregisterScratcher(scratcher);
    }
 }
