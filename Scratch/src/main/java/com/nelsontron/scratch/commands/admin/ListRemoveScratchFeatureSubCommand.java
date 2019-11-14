package com.nelsontron.scratch.commands.admin;

import com.nelsontron.scratch.ScratcherHolder;
import com.nelsontron.scratch.utils.Utils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class ListRemoveScratchFeatureSubCommand {
    /**
     * @return String
     */
    public static String getMessage(FileConfiguration config, ScratcherHolder holder, String configPath, String[] entry) {

        StringBuilder result = new StringBuilder();
        List list;
        int index;
        ConfigurationSection sec = config.getConfigurationSection("scratch");

        if (sec.get(configPath) == null)
            return holder.getChatils().formatErrorMessage("Unable to find Scratch feature with file path " + configPath, configPath);

        if (!(sec.get(configPath) instanceof List))
            return holder.getChatils().formatErrorMessage("Scratch feature " + configPath + " is not a list", configPath);

        list = sec.getList(configPath);

        if (list == null)
            return holder.getChatils().formatErrorMessage("Scratch feature " + configPath + " is not a list", configPath);

        try {
            index = Integer.parseInt(entry[0]);
        } catch (NumberFormatException ex) {
            return holder.getChatils().formatErrorMessage("You must define an index. Which one would you like to remove? From 0-" + (list.size()-1), configPath);
        }

        if (list.size() <= index)
            return holder.getChatils().formatErrorMessage("Index "+ index + " out of bounds. Please define an item that exists", index+"");

        list.remove(index);

        result.append(holder.getChatils().formatChatMessage("Removed entry " + index + " from feature " + configPath, configPath, index+""));

        return result.toString();
    }
}
