package com.nelsontron.scratch.listeners;

import com.nelsontron.scratch.Scratch;
import com.nelsontron.scratch.ScratcherHolder;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class Motd implements Listener {
    FileConfiguration config;
    ScratcherHolder holder;

    public Motd(FileConfiguration config, ScratcherHolder holder) {
        this.holder = holder;
        this.config = config;
    }

    @EventHandler
    public void playerRefresh(ServerListPingEvent ev) {
        if (config.getString("scratch.messages.motd") == null) return;

        ev.setMotd(ChatColor.translateAlternateColorCodes('&', holder.getChatils().formatStringVariables(config.getString("scratch.messages.motd"))));
    }
}
