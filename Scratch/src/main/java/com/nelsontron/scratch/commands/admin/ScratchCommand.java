package com.nelsontron.scratch.commands.admin;

import com.nelsontron.scratch.ScratcherHolder;
import com.nelsontron.scratch.utils.Chatils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;

public class ScratchCommand implements CommandExecutor {

    FileConfiguration config;
    ScratcherHolder holder;
    Chatils chatils;

    public ScratchCommand(FileConfiguration config, ScratcherHolder holder) {
        this.config = config;
        this.holder = holder;
        this.chatils = holder.getChatils();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        String subCommand;
        String configPath;
        String[] entry;

        if (args.length == 0) {
            sender.sendMessage(chatils.formatErrorMessage(command.getUsage()));
            return true;
        }

        subCommand = args[0];

        if (args.length == 1) {
            if (subCommand.equalsIgnoreCase("reload")) {
                holder.saveConfig();
                holder.reloadConfig();
                holder.reloadPlugin();
                sender.sendMessage(chatils.formatChatMessage("Reloaded Scratch config", "Scratch"));
            }
            else if (subCommand.equalsIgnoreCase("log")) {

            }
            else {
                sender.sendMessage(chatils.formatErrorMessage("Unable to find sub command " + subCommand, subCommand));
            }
            return true;
        }
        else if (args.length == 2) {
            configPath = args[1];
            if (subCommand.equalsIgnoreCase("toggle")) {
                sender.sendMessage(ToggleScratchFeatureSubCommand.getMessage(config, holder, configPath));
            }
            else if (subCommand.equalsIgnoreCase("list")) {
                sender.sendMessage(ListScratchFeatureSubCommand.getMessage(config, holder, configPath));
            }
            else if (subCommand.equalsIgnoreCase("get")) {
                sender.sendMessage(GetScratchFeatureSubCommand.getMessage(config, holder, configPath));
            }
            else {
                sender.sendMessage(chatils.formatErrorMessage("Unable to find sub command " + subCommand, subCommand));
            }
            return true;
        }
        else {
            configPath = args[1];
            entry = Arrays.copyOfRange(args, 2, args.length);

            if (entry.length <= 0) {
                sender.sendMessage(chatils.formatErrorMessage(command.getUsage()));
                return true;
            }

            if (subCommand.equalsIgnoreCase("add")) {
                sender.sendMessage(ListAddScratchFeatureSubCommand.getMessage(config, holder, configPath, entry));
            }
            else if (subCommand.equalsIgnoreCase("set")) {
                sender.sendMessage(SetScratchFeatureSubCommand.getMessage(config, holder, configPath, entry));
            }
            else if (subCommand.equalsIgnoreCase("remove")) {
                sender.sendMessage(ListRemoveScratchFeatureSubCommand.getMessage(config, holder, configPath, entry));
            }
            else {
                sender.sendMessage(chatils.formatErrorMessage("Unable to find sub command " + subCommand, subCommand));
            }
            return true;
        }
    }
}
