package com.nelsontron.mjarket.commands.handler;

import com.nelsontron.mjarket.entity.Consumer;
import com.nelsontron.mjarket.entity.ConsumerData;
import com.nelsontron.mjarket.entity.Government;
import com.nelsontron.mjarket.exceptions.MjarketCommandException;
import com.nelsontron.mjarket.exceptions.SenderNotPlayerException;
import com.nelsontron.mjarket.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PayHandler implements CommandExecutor {

    Government government;

    public PayHandler(Government government) {
        this.government = government;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {

        Player player;
        Player player2 = null;
        Consumer sender;
        Consumer payee = null;
        int amount;
        ConsumerData consumerData = government.getConsumerData();

        String commandResult = null;

        try {

            player = Utils.castPlayer(commandSender);
            sender = consumerData.getConsumer(player.getUniqueId());

            if (args.length == 0) {
                commandResult = ChatColor.GOLD +  "/pay" + ChatColor.GRAY + " <playername> <amount>.\n";
            }
            else if (args.length == 2) {

                // parse the player from a name.
                for (Player online : Bukkit.getOnlinePlayers()) {
                    if (online.getName().equalsIgnoreCase(args[0])) {
                        player2 = online;
                        payee = consumerData.getConsumer(online.getUniqueId());
                    }
                }

                amount = Integer.parseInt(args[1]);
                amount = Math.abs(amount);

                if (payee == null)
                    throw new MjarketCommandException(ChatColor.RED + "> Player '" + args[0] + "' not found.");

                // check if a player has enough doubloons to do that.
                sender.hasDoubloons(amount);

                sender.subtractDoubloons(amount);
                payee.addDoubloons(amount);

                commandResult = ChatColor.GOLD + "" + amount + ChatColor.GRAY + " doubloons have been sent to " + player2.getName() + ".";
                player2.sendMessage(ChatColor.GRAY + "You've received " + ChatColor.GOLD + amount + ChatColor.GRAY + " doubloons from " + player.getName() + ".");
            }
            /**
             * ADMIN COMMANDS
             */
            else if (args.length == 3) {

                if (player.hasPermission("mjarket.admin")) {
                    if (args[0].equalsIgnoreCase("-give")) {
                        // parse the player from a name.
                        for (Player online : Bukkit.getOnlinePlayers()) {
                            if (online.getName().equalsIgnoreCase(args[1])) {
                                player2 = online;
                                payee = consumerData.getConsumer(online.getUniqueId());
                            }
                        }

                        amount = Integer.parseInt(args[2]);

                        if (payee == null)
                            throw new MjarketCommandException(ChatColor.RED + "> Player '" + args[1] + "' not found.");

                        payee.setDoub(payee.getDoub() + amount);

                        commandResult = ChatColor.GOLD + "" + amount + ChatColor.GRAY + " doubloons have been sent to " + player2.getName() + ".";
                        player2.sendMessage(ChatColor.GRAY + "You've received " + ChatColor.GOLD + amount + ChatColor.GRAY + " doubloons.");
                    }
                    if (args[0].equalsIgnoreCase("-set")) {
                        // parse the player from a name.
                        for (Player online : Bukkit.getOnlinePlayers()) {
                            if (online.getName().equalsIgnoreCase(args[1])) {
                                player2 = online;
                                payee = consumerData.getConsumer(online.getUniqueId());
                            }
                        }

                        amount = Integer.parseInt(args[2]);

                        if (payee == null)
                            throw new MjarketCommandException(ChatColor.RED + "> Player '" + args[1] + "' not found.");

                        payee.setDoub(amount);

                        commandResult = ChatColor.GRAY + "Set " + player2.getName() + "'s balance to " + ChatColor.GOLD + amount + ChatColor.GRAY + " doubloons.";
                        player2.sendMessage(ChatColor.GRAY + "Your balance has been set to " + ChatColor.GOLD + amount + ChatColor.GRAY + " doubloons.");
                    }
                    if (args[0].equalsIgnoreCase("-remove")) {
                        // parse the player from a name.
                        for (Player online : Bukkit.getOnlinePlayers()) {
                            if (online.getName().equalsIgnoreCase(args[1])) {
                                player2 = online;
                                payee = consumerData.getConsumer(online.getUniqueId());
                            }
                        }

                        amount = Integer.parseInt(args[2]);

                        if (payee == null)
                            throw new MjarketCommandException(ChatColor.RED + "> Player '" + args[1] + "' not found.");

                        payee.setDoub(payee.getDoub() - amount);

                        commandResult = ChatColor.GRAY + "" + amount + ChatColor.GRAY + " doubloons have been removed from " + player2.getName() + ".";
                        player2.sendMessage(ChatColor.GOLD + "" + amount + ChatColor.GRAY + " doubloons have been removed from your balance.");
                    }
                }

            } else {
                throw new MjarketCommandException(ChatColor.RED + "> You need to specify a player name and an amount.\n" +
                        "Try /pay partybrother 25.");
            }

            return true;

        } catch (ClassCastException ex) {
            commandResult = ChatColor.RED + "> You must be a player to send this command.";

        } catch (NumberFormatException ex) {
            commandResult = ChatColor.RED + "> Couldn't parse integer in last command argument <amount>. /pay partybrother 25 <--- here.";

        } catch(MjarketCommandException | SenderNotPlayerException ex) {
            commandResult = ex.getMessage();

        } finally {
            if (commandResult != null)
                commandSender.sendMessage(commandResult);
        }

        return false;
    }
}
