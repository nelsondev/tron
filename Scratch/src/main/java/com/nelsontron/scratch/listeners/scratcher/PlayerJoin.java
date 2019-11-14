package com.nelsontron.scratch.listeners.scratcher;

import com.nelsontron.scratch.ScratcherHolder;
import com.nelsontron.scratch.entity.Scratcher;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {
    private ScratcherHolder holder;

    public PlayerJoin(ScratcherHolder holder) {
        this.holder = holder;
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent ev) {
        Player player = ev.getPlayer();
        Scratcher scratcher = new Scratcher(holder, player.getUniqueId()).load();
        holder.registerScratcher(scratcher);
    }
}
