package com.nelsontron.scratch.commands;

import com.nelsontron.scratch.ScratcherHolder;
import com.nelsontron.scratch.entity.Scratcher;
import com.nelsontron.scratch.utils.Chatils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {

    ScratcherHolder holder;
    Chatils chatils;

    public SpawnCommand(ScratcherHolder holder) {
        this.holder = holder;
        this.chatils = holder.getChatils();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player;
        Scratcher scratcher;

        String result = null;

        try {

            player = (Player) sender;
            scratcher = holder.getScratcher(player);
            scratcher.teleport(player.getWorld().getSpawnLocation());
            result = chatils.formatChatMessage("Teleported to spawn");

            return true;
        } catch (ClassCastException ex) {
            result = chatils.formatErrorMessage("You must be a player to use this command");
        } finally {
            if (result != null)
                sender.sendMessage(result);
        }

        return false;
    }
}
