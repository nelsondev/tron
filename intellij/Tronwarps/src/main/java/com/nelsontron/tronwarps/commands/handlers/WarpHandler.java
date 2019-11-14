package com.nelsontron.tronwarps.commands.handlers;

import com.nelsontron.mjarket.Mjarket;
import com.nelsontron.mjarket.entity.Consumer;
import com.nelsontron.tronwarps.Tronwarps;
import com.nelsontron.tronwarps.Warp;
import com.nelsontron.tronwarps.Warper;
import com.nelsontron.tronwarps.exceptions.WarpCommandException;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WarpHandler implements CommandExecutor {

    Tronwarps tronwarps;

    public WarpHandler(Tronwarps tronwarps) {
        this.tronwarps = tronwarps;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player;
        Consumer consumer;
        Warper warper;
        String subCommand;
        String warpName = null;
        String warpRename = null;
        String catagoryName = null;
        Warp warp;

        String commandResolve = null;

        try {
            player = (Player) sender;
            warper = tronwarps.getWarper(player);
            consumer = Mjarket.getGovernment().getConsumerData().getConsumer(player.getUniqueId());

            if (args.length == 0) {
                commandResolve = ChatColor.BLUE + "Warps >\n";
                for (Warp w : warper.getWarps()) {
                    commandResolve += ChatColor.GRAY + "    " + w.getName() + "\n";
                }
            }
            else {

                subCommand = args[0];

                if (args.length >= 2)
                    warpName = args[1];

                if (args.length >= 3) {
                    warpRename = args[2];
                    catagoryName = args[2];
                }

                if (subCommand.equalsIgnoreCase("list") || subCommand.equalsIgnoreCase("l")) {

                    if (args.length >= 2)
                        catagoryName = args[1];

                    if (catagoryName != null) {
                        commandResolve = ChatColor.BLUE + "Warps : " + ChatColor.GOLD + catagoryName + ChatColor.BLUE + " >\n";
                        for (Warp w : warper.getWarps()) {
                            if (w.getCategory() != null) {
                                if (w.getCategory().equalsIgnoreCase(catagoryName) || w.getCategory().contains(catagoryName)) {

                                    commandResolve += ChatColor.GRAY + "    " + w.getName() + "\n";
                                }
                            }
                        }
                    }
                    else {
                        commandResolve = ChatColor.BLUE + "Warps >\n";
                        HashMap<String, List<String>> categories = new HashMap<>();

                        for (Warp w : warper.getWarps()) {

                            if (w.getCategory() != null) {

                                if (categories.containsKey(w.getCategory())) {

                                    categories.get(w.getCategory()).add(w.getName());
                                }
                                else {
                                    categories.put(w.getCategory(), new ArrayList<>());
                                    categories.get(w.getCategory()).add(w.getName());
                                }

                                continue;
                            }

                            commandResolve += ChatColor.GRAY + "    " + w.getName() + "\n";
                        }

                        for (String category : categories.keySet()) {

                            commandResolve += ChatColor.GOLD  + "  - " + category + " -\n";

                            for (String name : categories.get(category)) {


                                commandResolve += ChatColor.GRAY + "    " + name + "\n";
                            }
                        }
                    }
                }
                else if (subCommand.equalsIgnoreCase("help")) {
                    commandResolve = ChatColor.BLUE + "Warp help >\n";
                    commandResolve += ChatColor.GRAY + "    /warp <name>\n";
                    commandResolve += "      - " + ChatColor.GOLD + Mjarket.WARP_PRICE + ChatColor.GRAY + " doubloons to warp.\n";
                    commandResolve += "    /warp set <name> <category> - create a warp.\n";
                    commandResolve += "      - category is optional.\n";
                    commandResolve += "      - " + ChatColor.GOLD + Mjarket.WARP_SET_PRICE + ChatColor.GRAY + " doubloons to set.\n";
                    commandResolve += "    /warp list <category> - list warps.\n";
                    commandResolve += "      - category is optional.\n";
                    commandResolve += "    /warp remove <name> - remove a warp\n";
                    commandResolve += "    /warp rename <name> <newName> - rename a warp.\n";
                    commandResolve += "    /warp move <name> <newCategory> - rename a warps category.\n";
                }
                else if (subCommand.equalsIgnoreCase("set") || subCommand.equalsIgnoreCase("s")) {

                    /**
                     * MJARKET
                     */
                    if (consumer.getDoub() < Mjarket.WARP_SET_PRICE) {
                        float remaining = Mjarket.WARP_SET_PRICE-consumer.getDoub();
                        throw new WarpCommandException("You need " + remaining +" more doubloons.");
                    }

                    if (warper.getWarp(warpName) != null)
                        throw new WarpCommandException("You already have a warp named that.");

                    if (warpName == null)
                        throw new WarpCommandException("Please specify a warp name.");

                    warp = new Warp();
                    warp.setName(warpName);
                    warp.setLocation(player.getLocation());

                    if (catagoryName != null)
                        warp.setCategory(catagoryName);

                    warper.addWarp(warp);

                    /**
                     * MJARKET
                     */
                    consumer.setDoub(consumer.getDoub()-Mjarket.WARP_SET_PRICE);
                    consumer.getServices().add(Mjarket.WARP_SET_PRICE);
                    consumer.setActivity(consumer.getActivity() + 25);

                    commandResolve = ChatColor.GRAY + "> Created new warp named '" + warp.getName() + ".'";
                    if (catagoryName != null)
                        commandResolve += "\n" + ChatColor.GRAY + "> Warp catagory has been set to '" + catagoryName + ".'";
                }
                else if (subCommand.equalsIgnoreCase("rem") || subCommand.equalsIgnoreCase("remove")) {

                    warp = warper.getWarp(warpName);

                    if (warp == null)
                        throw new WarpCommandException("Unable to find warp '" + warpName + ".'");

                    warper.removeWarp(warp);

                    commandResolve = ChatColor.GRAY  + "> Removed warp '" + warp.getName() + ".'";
                }
                else if (subCommand.equalsIgnoreCase("ren") || subCommand.equalsIgnoreCase("rename")) {

                    warp = warper.getWarp(warpName);

                    if (warp == null)
                        throw new WarpCommandException("Unable to find warp '" + warpName + ".'");

                    if (warpRename == null)
                        throw new WarpCommandException("Invalid command arguments. You're missing the new warp name.");

                    warp.setName(warpRename);

                    commandResolve = ChatColor.GRAY + "> Renamed warp '" + warpName + "' to '" + warpRename + ".'";
                }
                else if (subCommand.equalsIgnoreCase("mov") || subCommand.equalsIgnoreCase("move")) {

                    warp = warper.getWarp(warpName);

                    if (warp == null)
                        throw new WarpCommandException("Unable to find warp '" + warpName + ".'");

                    if (warpRename == null)
                        throw new WarpCommandException("Invalid command arguments. You're missing the new category name.");

                    warp.setCategory(catagoryName);

                    commandResolve = ChatColor.GRAY + "> Moved warp '" + warp.getName() + "' to category '" + catagoryName + ".'";
                }
                else if (subCommand.equalsIgnoreCase("sort")) {

                    if (warpName.equalsIgnoreCase("-alphabeticallyAscending") || warpName.equalsIgnoreCase("-alphaAscending") || warpName.equalsIgnoreCase("-aA")) {

                        warper.sortAlphabeticalAscending();
                        commandResolve = ChatColor.GRAY + "> Sorted warps alphabetically ascending.";
                    }
                    else if (warpName.equalsIgnoreCase("-alphabeticallyDescending") || warpName.equalsIgnoreCase("-alphaDecending") || warpName.equalsIgnoreCase("-aD")) {

                        warper.sortAlphabeticalDecending();
                        commandResolve = ChatColor.GRAY + "> Sorted warps alphabetically descending.";
                    }
                    else {
                        throw new WarpCommandException("Invalid sorting method. Try -alphabetically or -a.");
                    }
                }
                // player wants to tp to a warp.
                else {
                    warpName = args[0];
                    warp = warper.guessWarp(warpName);

                    /**
                     * MJARKET
                     */
                    if (consumer.getDoub() < Mjarket.WARP_PRICE) {
                        float remaining = Mjarket.WARP_PRICE-consumer.getDoub();
                        throw new WarpCommandException("You need " + remaining +" more doubloons.");
                    }

                    if (warp == null)
                        throw new WarpCommandException("Unable to find warp '" + warpName + ".'");

                    player.teleport(warp.getLocation());

                    /**
                     * MJARKET
                     */
                    consumer.setDoub(consumer.getDoub()-Mjarket.WARP_PRICE);
                    consumer.getServices().add(Mjarket.WARP_PRICE);
                    consumer.setActivity(consumer.getActivity() + 1);

                    commandResolve = ChatColor.GRAY + "> Warped to '" + warpName + ".'";
                }
            }

        } catch (ClassCastException ex) {
            commandResolve = ChatColor.RED + "> Command sender must be a player to use this command.";
        } catch (WarpCommandException ex) {
            commandResolve = ChatColor.RED + "> " + ex.getMessage();
        } finally {
            if (commandResolve != null)
                sender.sendMessage(commandResolve);
        }

        return false;
    }
}
