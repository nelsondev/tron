package com.nelsontron.scratch.commands;

import com.nelsontron.scratch.ScratcherHolder;
import com.nelsontron.scratch.entity.Scratcher;
import com.nelsontron.scratch.utils.Chatils;
import com.nelsontron.scratch.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HereCommand implements CommandExecutor {

    ScratcherHolder holder;
    Chatils chatils;

    public HereCommand(ScratcherHolder holder) {
        this.holder = holder;
        this.chatils = holder.getChatils();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player;
        Scratcher scratcher;
        Player toPlayer;

        String result = null;

        try {

            if (args.length != 1) {
                result = chatils.formatErrorMessage(command.getUsage());
                return true;
            }

            player = (Player) sender;
            scratcher = holder.getScratcher(player);
            toPlayer = Utils.getPlayer(args[0]);

            if (toPlayer == null) {
                result = chatils.formatErrorMessage("Unable to find player " + args[0], args[0]);
                return true;
            }

            if (toPlayer == player) {
                result = chatils.formatErrorMessage("You can't teleport to yourself");
                return true;
            }

            scratcher.teleport(player.getLocation());
            result = chatils.formatChatMessage("Teleported " + toPlayer.getName() + " to you", toPlayer.getName());
            toPlayer.sendMessage(chatils.formatChatMessage(player.getName() + " teleported you to them", player.getName()));

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
