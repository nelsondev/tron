package com.nelsontron.scratch.commands;

import com.nelsontron.scratch.ScratcherHolder;
import com.nelsontron.scratch.entity.Scratcher;
import com.nelsontron.scratch.utils.Chatils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatsCommand implements CommandExecutor {

    ScratcherHolder holder;
    Chatils chatils;

    public StatsCommand(ScratcherHolder holder) {
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

            chatils.setPunctuated(false);
            result = chatils.formatChatMessage("Stats:", "Stats") + "\n";

            for (String key : scratcher.getStats().prettyMap().keySet()) {
                int val;
                if (key == null) continue;
                val = scratcher.getStats().prettyMap().get(key);
                if (val == 0) continue;

                result += "    " + chatils.formatChatMessage(key + ": " + val, val+"") + "\n";
            }
            return true;
        } catch (ClassCastException ex) {
            result = chatils.formatErrorMessage("You must be a player to use this command");
        } finally {
            if (result != null) {
                sender.sendMessage(result);
                chatils.setPunctuated(true);
            }
        }

        return false;
    }
}
