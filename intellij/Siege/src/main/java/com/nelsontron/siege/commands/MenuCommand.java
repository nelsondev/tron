package com.nelsontron.siege.commands;

import com.nelsontron.siege.data.MenuGui;
import com.nelsontron.siege.entity.Fighter;
import com.nelsontron.siege.game.Holder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MenuCommand implements CommandExecutor {
    Holder holder;

    public MenuCommand(Holder holder) {
        this.holder = holder;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player;
        Fighter fighter;

        String result = null;

        try {

            player = (Player) sender;
            fighter = holder.getFighter(player);

            player.openInventory(MenuGui.getMenuInventory(fighter));
        }
        catch (ClassCastException ex) {

        }
        finally {
            if (result != null)
                sender.sendMessage(result);
        }

        return false;
    }
}
