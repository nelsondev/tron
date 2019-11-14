package com.nelsontron.scratch.commands;

import com.nelsontron.mjarket.Mjarket;
import com.nelsontron.mjarket.entity.Consumer;
import com.nelsontron.mjarket.exceptions.MjarketCommandException;
import com.nelsontron.scratch.ScratcherHolder;
import com.nelsontron.scratch.data.Warp;
import com.nelsontron.scratch.entity.Scratcher;
import com.nelsontron.scratch.exceptions.WarpCommandException;
import com.nelsontron.scratch.utils.Chatils;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WarpCommand implements CommandExecutor {
    ScratcherHolder holder;
    Chatils chatils;

    public WarpCommand(ScratcherHolder holder) {
        this.holder = holder;
        this.chatils = holder.getChatils();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player;
        Consumer consumer;
        Scratcher scratcher;
        String subCommand;
        String warpName = null;
        String warpRename = null;
        String catagoryName = null;
        Warp warp;

        String commandResolve = null;

        try {
            player = (Player) sender;
            scratcher = holder.getScratcher(player);
            consumer = Mjarket.getGovernment().getConsumerData().getConsumer(player.getUniqueId());

            if (args.length == 0) {
                chatils.setPunctuated(false);
                commandResolve = chatils.formatChatMessage("Warps:", "Warps") + "\n";
                for (Warp w : scratcher.getWarps()) {
                    commandResolve += "    " + chatils.formatChatMessage(w.getName()) + "\n";
                }
                chatils.setPunctuated(true);
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
                        chatils.setPunctuated(false);
                        commandResolve = chatils.formatChatMessage("Warps : " + chatils.getUnderlined() + catagoryName + chatils.getSecondary() + " >", "Warps") + "\n";
                        for (Warp w : scratcher.getWarps()) {
                            if (w.getCategory() != null) {
                                if (w.getCategory().equalsIgnoreCase(catagoryName) || w.getCategory().contains(catagoryName)) {

                                    commandResolve += "    " + chatils.formatChatMessage(w.getName()) + "\n";
                                }
                            }
                        }
                        chatils.setPunctuated(true);
                    }
                    else {
                        chatils.setPunctuated(false);
                        commandResolve = chatils.formatChatMessage("Warps:", "Warps") + "\n";
                        HashMap<String, List<String>> categories = new HashMap<>();

                        for (Warp w : scratcher.getWarps()) {

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

                            commandResolve += "    " + chatils.formatChatMessage(w.getName()) + "\n";
                        }

                        for (String category : categories.keySet()) {

                            commandResolve += "  - " + chatils.formatChatMessage(category, category) + " -\n";

                            for (String name : categories.get(category)) {


                                commandResolve += "    " + chatils.formatChatMessage(name) + "\n";
                            }
                        }
                        chatils.setPunctuated(true);
                    }
                }
                else if (subCommand.equalsIgnoreCase("help")) {
                    commandResolve = chatils.formatChatMessage("Warp:", "Warp") + "\n";;
                    commandResolve += ChatColor.GRAY + "    /warp <name>\n";
                    //commandResolve += "      - " + ChatColor.GOLD + Mjarket.WARP_PRICE + ChatColor.GRAY + " doubloons to warp.\n";
                    commandResolve += "    /warp set <name> <category> - create a warp.\n";
                    commandResolve += "      - category is optional.\n";
                    //commandResolve += "      - " + ChatColor.GOLD + Mjarket.WARP_SET_PRICE + ChatColor.GRAY + " doubloons to set.\n";
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

                    if (scratcher.getWarp(warpName) != null)
                        throw new WarpCommandException("You already have a warp named that.");

                    if (warpName == null)
                        throw new WarpCommandException("Please specify a warp name.");

                    warp = new Warp();
                    warp.setName(warpName);
                    warp.setLocation(player.getLocation());

                    if (catagoryName != null)
                        warp.setCategory(catagoryName);

                    scratcher.addWarp(warp);

                    /**
                     * MJARKET
                     */
                    consumer.setDoub(consumer.getDoub()-Mjarket.WARP_SET_PRICE);
                    consumer.getServices().add(Mjarket.WARP_SET_PRICE);
                    consumer.setActivity(consumer.getActivity() + 25);

                    commandResolve = chatils.formatChatMessage( "Created new warp named " + warp.getName(), warp.getName()) + "\n";
                    if (catagoryName != null)
                        commandResolve += chatils.formatChatMessage( "Warp catagory has been set to " + catagoryName, catagoryName);
                }
                else if (subCommand.equalsIgnoreCase("rem") || subCommand.equalsIgnoreCase("remove")) {

                    warp = scratcher.getWarp(warpName);

                    if (warp == null)
                        throw new WarpCommandException("Unable to find warp '" + warpName + ".'");

                    scratcher.removeWarp(warp);

                    commandResolve = chatils.formatChatMessage("Removed warp " + warp.getName(), warp.getName());
                }
                else if (subCommand.equalsIgnoreCase("ren") || subCommand.equalsIgnoreCase("rename")) {

                    warp = scratcher.getWarp(warpName);

                    if (warp == null)
                        throw new WarpCommandException("Unable to find warp " + warpName);

                    if (warpRename == null)
                        throw new WarpCommandException("Invalid command arguments. You're missing the new warp name");

                    warp.setName(warpRename);

                    commandResolve = chatils.formatChatMessage("Renamed warp " + warpName + " to " + warpRename, warpName, warpRename);
                }
                else if (subCommand.equalsIgnoreCase("mov") || subCommand.equalsIgnoreCase("move")) {

                    warp = scratcher.getWarp(warpName);

                    if (warp == null)
                        throw new WarpCommandException("Unable to find warp '" + warpName + ".'");

                    if (warpRename == null)
                        throw new WarpCommandException("Invalid command arguments. You're missing the new category name");

                    warp.setCategory(catagoryName);

                    commandResolve = chatils.formatChatMessage("Moved warp " + warp.getName() + " to a category " + catagoryName, warp.getName(), warp.getCategory());
                }
                else if (subCommand.equalsIgnoreCase("sort")) {

                    if (warpName.equalsIgnoreCase("-alphabeticallyAscending") || warpName.equalsIgnoreCase("-alphaAscending") || warpName.equalsIgnoreCase("-aA")) {

                        scratcher.sortWarpsAlphabeticalAscending();
                        commandResolve = chatils.formatChatMessage("Sorted warps alphabetically ascending");
                    }
                    else if (warpName.equalsIgnoreCase("-alphabeticallyDescending") || warpName.equalsIgnoreCase("-alphaDecending") || warpName.equalsIgnoreCase("-aD")) {

                        scratcher.sortWarpsAlphabeticalDecending();
                        commandResolve = chatils.formatChatMessage("Sorted warps alphabetically decending");
                    }
                    else {
                        throw new WarpCommandException("Invalid sorting method. Try -alphabetically or -a");
                    }
                }
                // player wants to tp to a warp.
                else {
                    warpName = args[0];
                    warp = scratcher.guessWarp(warpName);

                    /**
                     * MJARKET
                     */
                    if (consumer.getDoub() < Mjarket.WARP_PRICE) {
                        float remaining = Mjarket.WARP_PRICE-consumer.getDoub();
                        throw new WarpCommandException("You need " + remaining +" more doubloons.");
                    }

                    if (warp == null)
                        throw new WarpCommandException("Unable to find warp " + warpName);

                    scratcher.teleport(warp.getLocation());

                    /**
                     * MJARKET
                     */
                    consumer.setDoub(consumer.getDoub()-Mjarket.WARP_PRICE);
                    consumer.getServices().add(Mjarket.WARP_PRICE);
                    consumer.setActivity(consumer.getActivity() + 1);

                    commandResolve = chatils.formatChatMessage("Warped to " + warpName, warpName);
                }
            }

        } catch (ClassCastException ex) {
            commandResolve = chatils.formatErrorMessage("Command sender must be a player to use this command");
        } catch (WarpCommandException ex) {
            commandResolve = chatils.formatErrorMessage(ex.getMessage());
        } finally {
            if (commandResolve != null)
                sender.sendMessage(commandResolve);
        }

        return true;
    }
}
