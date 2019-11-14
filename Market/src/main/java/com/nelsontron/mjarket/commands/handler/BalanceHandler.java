package com.nelsontron.mjarket.commands.handler;

import com.nelsontron.mjarket.entity.Consumer;
import com.nelsontron.mjarket.entity.ConsumerData;
import com.nelsontron.mjarket.entity.Government;
import com.nelsontron.mjarket.exceptions.SenderNotPlayerException;
import com.nelsontron.mjarket.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BalanceHandler implements CommandExecutor {

    private Government government;

    public BalanceHandler(Government government) {
        this.government = government;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {

        Player player;
        Consumer consumer;
        ConsumerData consumerData = government.getConsumerData();

        String commandResult = null;

        try {
            player = Utils.castPlayer(commandSender);
            consumer = consumerData.getConsumer(player.getUniqueId());

            if (args.length == 0) {
                commandResult = ChatColor.GOLD +""+consumer.getDoub() + ChatColor.GRAY + " Doubloons.";
            }
            else {
                Player player1 = Bukkit.getPlayer(args[0]);

                if (player1 == null) {
                    commandResult = ChatColor.RED + "> Unable to find player.";
                    return true;
                }
                Consumer consumer1 = consumerData.getConsumer(player1.getUniqueId());
                commandResult = ChatColor.GOLD +""+consumer1.getDoub() + ChatColor.GRAY + " Doubloons.";
            }

            return true;

        } catch(SenderNotPlayerException ex) {
            commandResult = ex.getMessage();

        } finally {
            if (commandResult != null)
                commandSender.sendMessage(commandResult);
        }

        return false;
    }
}

