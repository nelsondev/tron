package com.nelsontron.scratch.commands;

import com.nelsontron.scratch.ScratcherHolder;
import com.nelsontron.scratch.utils.Chatils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EnderCommand implements CommandExecutor {

    ScratcherHolder holder;
    Chatils chatils;

    public EnderCommand(ScratcherHolder holder) {
        this.holder = holder;
        this.chatils = holder.getChatils();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player;

        String result = null;

        try {

            player = (Player) sender;

            player.openInventory(player.getEnderChest());
            result = chatils.formatChatMessage("Opened ender chest");

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
