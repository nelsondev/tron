package com.nelsontron.mjarket.commands.handler;

import com.nelsontron.mjarket.commands.sub.BuySubCommand;
import com.nelsontron.mjarket.commands.sub.ListSubCommand;
import com.nelsontron.mjarket.commands.sub.RemoveSubCommand;
import com.nelsontron.mjarket.commands.sub.SellSubCommand;
import com.nelsontron.mjarket.entity.Consumer;
import com.nelsontron.mjarket.entity.Government;
import com.nelsontron.mjarket.exceptions.*;
import com.nelsontron.mjarket.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class MjarketHandler implements CommandExecutor {

    /**
     * START MJARKET HANDLER > this object handles the "onCommand" function which comes with
     * implements CommandExecutor. It is fired when the /market command is sent.
     * <p>
     * This Object also contains definitions for exceptions that handle the help pages for
     * this commands sub commands.
     *
     * @author nelso && cale
     */

    private Government government;

    public MjarketHandler(Government government) {
        this.government = government;
    }

    /*
     * START MJARKET COMMAND HANDLER
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        /*
         * Define command variables that'll be used.
         *
         * player = command sender.
         * command = arg0. Or the first parameter.
         * parameters = arg1 and up.
         * message = the command message result.
         */
        Player player;
        Consumer consumer;
        String command;
        String[] parameters;
        String message = null;

        /*
         * HANDLES EXCEPTIONS:
         *
         * SenderNotPlayerException,
         * NoPermissionException,
         * InvalidMjarketCommandException
         * SellCommandException,
         * BuyCommandException,
         * RemoveCommandException
         */
        try {

            player = Utils.castPlayer(sender); // throws sender not player exception
            consumer = government.getConsumerData().getConsumer(player.getUniqueId());

            // player has permission
            Utils.checkSenderPermission(cmd.getLabel(), sender); // throws no permission exception

            if (args.length == 0) {

                message = ChatColor.GOLD + "Mjarket Help >\n";
                message += ChatColor.GRAY + "    /balance or /bal - show your balance of Doubloons.\n";
                message += "    /pay <player> <amount> - pay a player Doubloons.\n";
                message += "    /mjarket sell <price> - list the item in your hand.\n";
                message += "    /mjarket remove <ID> - remove a mjarket listing by ID.\n";
                message += "    /mjarket list - list the your current mjarket listings.\n";
                message += "    /mjarket list <item> - list current mjarket listings for an item.\n";
                message += "    /mjarket list <ID> - list stats for an item like name and enchantments.\n";
                message += "    /mjarket list <player> - list a players current mjarket listings.\n";
                message += "    /mjarket buy <item> - buy the lowest price item listed.\n";
                message += "    /mjarket buy <ID> - buy a specific mjarket listing.\n";

            } else {

                command = args[0];
                // if args length is > or = to 2, set parameters to a copy of args with the first index cut out.
                parameters = args.length >= 2 ? Arrays.copyOfRange(args, 1, args.length) : null;

                // TODO: individual market commands, sell, buy, remove, price

                /*
                 * START SELL COMMAND > sell command is used by /market sell <price>. It
                 * takes the players item global (kijiji like) in hand and uses the <price>
                 * argument to put it on the marketplace. All this does is add a new mjarket
                 * item to the players items list.
                 */
                if (command.equalsIgnoreCase("sell")) {

                    message = new SellSubCommand(government, consumer, parameters).getMessage();
                }

                /*
                 * START BUY COMMAND
                 */
                else if (command.equalsIgnoreCase("buy")) {

                    message = new BuySubCommand(government, consumer, parameters).getMessage();
                }

                /*
                 * REMOVE BUY COMMAND
                 */
                else if (command.equalsIgnoreCase("remove")) {

                    message = new RemoveSubCommand(government, consumer, parameters).getMessage();
                }

                /*
                 * REMOVE BUY COMMAND
                 */
                else if (command.equalsIgnoreCase("list")) {

                    message = new ListSubCommand(government, consumer, parameters).getMessage();
                }

                // no compatable sub command found.
                else {
                    throw new InvalidMjarketCommandException(command,
                            Utils.arrayToString(args, " "));
                }
            }

            // return command successful.
            return true;

            /*
             * START CATCH EXCEPTION HANDLING
             */

            // player has no permission to use the command.
            // command sender is not a player.
            // send the command message.
            // send sell command help.
            // send buy command help.
            // send remove command help.

        } catch ( NoPermissionException
                | SenderNotPlayerException
                | InvalidMjarketCommandException
                | MjarketCommandException ex) {

            // set the command return message as the exception message.
            message = ex.getMessage();

            // send the command message.
        } finally {
            if (message != null)
                sender.sendMessage(message);
        }
        return false;
    }
}
