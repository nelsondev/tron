package com.nelsontron.snackpack;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CraftCommand implements CommandExecutor {

    SnackPack snackPack;

    public CraftCommand(SnackPack snackPack) {
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
            pack = snack.getPack("craft");

            if (pack == null) {
                result = ChatColor.RED + "> You dont own an craft pack yet. /pack help.";
                return true;
            }

            player.openWorkbench(null, true);
            result = ChatColor.GRAY + "Opened craft pack.";

        } catch (ClassCastException ex) {
            result = ChatColor.RED + "> Sender must be a player.";

        } finally {
            if (result != null)
                sender.sendMessage(result);
        }

        return false;
    }
}
