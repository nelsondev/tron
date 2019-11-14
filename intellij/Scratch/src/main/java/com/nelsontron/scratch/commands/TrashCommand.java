package com.nelsontron.scratch.commands;

import com.nelsontron.scratch.ScratcherHolder;
import com.nelsontron.scratch.entity.Scratcher;
import com.nelsontron.scratch.utils.Chatils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TrashCommand implements CommandExecutor {

    ScratcherHolder holder;
    Chatils chatils;

    public TrashCommand(ScratcherHolder holder) {
        this.holder = holder;
        this.chatils = holder.getChatils();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player;
        Scratcher scratcher;
        ItemStack item;

        String result = null;

        try {

            player = (Player) sender;
            scratcher = holder.getScratcher(player);
            item = player.getInventory().getItemInMainHand();

            if (label.equalsIgnoreCase("trash")) {
                if (item == null || item.getType() == Material.AIR) {
                    result = chatils.formatErrorMessage("You need to be holding an item");
                    return true;
                }

                scratcher.setTrashedItem(item);
                player.getInventory().setItemInMainHand(null);
                result = chatils.formatChatMessage("Trashed item in hand. Use /untrash to undo this");

                return true;
            }
            if (label.equalsIgnoreCase("untrash")) {
                item = scratcher.getTrashedItem();

                if (player.getInventory().firstEmpty() == -1) {
                    result = chatils.formatErrorMessage("Your inventory is full");
                    return true;
                }
                player.getInventory().addItem(item);
                scratcher.setTrashedItem(null);
                result = chatils.formatChatMessage("Un-trashed item");

                return true;
            }
        } catch (ClassCastException ex) {
            result = chatils.formatErrorMessage("You must be a player to use this command");
        } finally {
            if (result != null)
                sender.sendMessage(result);
        }

        return false;
    }
}
