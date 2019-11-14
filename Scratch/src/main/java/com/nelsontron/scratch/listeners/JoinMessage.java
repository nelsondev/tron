package com.nelsontron.scratch.listeners;

import com.nelsontron.scratch.ScratcherHolder;
import com.nelsontron.scratch.utils.Chatils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;
import java.util.Random;

public class JoinMessage implements Listener {
    FileConfiguration config;
    ScratcherHolder holder;
    Chatils chatils;

    public JoinMessage(FileConfiguration config, ScratcherHolder holder) {
        this.config = config;
        this.holder = holder;
        this.chatils = holder.getChatils();
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent ev) {
        List<String> list = config.getStringList("scratch.messages.join");
        if (list.isEmpty())
            return;

        int ran = new Random().nextInt(list.size()-1);
        String result = list.get(ran);
        result = chatils.formatStringVariables(result);
        result = result.replace("${playername}", ev.getPlayer().getName());
        result = chatils.formatChatMessage(result);

        ev.setJoinMessage(result);
    }
}
