package com.nelsontron.scratch.commands;

import com.nelsontron.scratch.ScratcherHolder;
import com.nelsontron.scratch.utils.Chatils;
import com.nelsontron.scratch.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NickCommand implements CommandExecutor {

    ScratcherHolder holder;
    Chatils chatils;

    public NickCommand(ScratcherHolder holder) {
        this.holder = holder;
        this.chatils = holder.getChatils();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player;
        String nickname;

        String result = null;

        try {

            player = (Player) sender;

            if (args.length == 0) {
                result = chatils.formatErrorMessage(command.getUsage());
                return true;
            }

            player.setCustomNameVisible(true);

            nickname = ChatColor.translateAlternateColorCodes('&', Utils.stringify(args, " "));
            player.setDisplayName(nickname);
            player.setCustomName(nickname);
            player.setPlayerListName(nickname);

            result = chatils.formatChatMessage("Set nick name");

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
