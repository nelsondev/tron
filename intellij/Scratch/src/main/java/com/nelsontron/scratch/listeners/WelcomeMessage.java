package com.nelsontron.scratch.listeners;

import com.nelsontron.scratch.ScratcherHolder;
import com.nelsontron.scratch.utils.Chatils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;
import java.util.Random;

public class WelcomeMessage implements Listener {
    FileConfiguration config;
    ScratcherHolder holder;
    Chatils chatils;

    public WelcomeMessage(FileConfiguration config, ScratcherHolder holder) {
        this.config = config;
        this.holder = holder;
        this.chatils = holder.getChatils();
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent ev) {
        List<String> list = config.getStringList("scratch.messages.welcome");
        if (list.isEmpty())
            return;

        StringBuilder builder = new StringBuilder();

        chatils.setPunctuated(false);

        for (String str : list) {
            str = chatils.formatStringVariables(str);
            str = str.replace("${playername}", ev.getPlayer().getName());
            str = chatils.formatChatMessage(str);
            str += "\n";
            builder.append(str);
        }

        chatils.setPunctuated(true);

        ev.getPlayer().sendMessage(builder.toString());
    }
}
