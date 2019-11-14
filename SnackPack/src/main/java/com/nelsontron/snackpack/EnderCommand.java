package com.nelsontron.snackpack;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EnderCommand implements CommandExecutor {

    SnackPack snackPack;

    public EnderCommand(SnackPack snackPack) {
        this.snackPack = snackPack;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player;
        SnackPack.Snack snack;
        SnackPack.Pack pack;

        String result = null;

        try {

            player = (Player) sender;
            snack = snackPack.holder.getSnacker(player);
            pack = snack.getPack("ender");

            if (pack == null) {
                result = ChatColor.RED + "> You dont own an ender pack yet. /pack help.";
                return true;
            }

            player.openInventory(pack.toInventory(player));
            result = ChatColor.GRAY + "Opened ender pack.";

        } catch (ClassCastException ex) {
            result = ChatColor.RED + "> Sender must be a player.";

        } finally {
            if (result != null)
                sender.sendMessage(result);
        }

        return false;
    }
}
