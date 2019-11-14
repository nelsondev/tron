package com.nelsontron.scratch.commands;

import com.nelsontron.scratch.ScratcherHolder;
import com.nelsontron.scratch.entity.Scratcher;
import com.nelsontron.scratch.utils.Chatils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.naming.NoPermissionException;

public class BackCommand implements CommandExecutor {

    ScratcherHolder holder;
    Chatils chatils;

    public BackCommand(ScratcherHolder holder) {
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

            if (scratcher.getLastcation() == null) {
                result = chatils.formatErrorMessage("No last teleport location has been set");
                return true;
            }

            scratcher.teleport(scratcher.getLastcation());
            result = chatils.formatChatMessage("Teleported back");

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
