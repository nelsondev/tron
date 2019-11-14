package com.nelsontron.scratch.commands.admin;

import com.nelsontron.scratch.ScratcherHolder;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class ListScratchFeatureSubCommand {
    /**
     * @return String
     */
    public static String getMessage(FileConfiguration config, ScratcherHolder holder, String configPath) {

        StringBuilder result = new StringBuilder();
        List list;
        ConfigurationSection sec = config.getConfigurationSection("scratch");

        if (sec.get(configPath) == null)
            return holder.getChatils().formatErrorMessage("Unable to find Scratch feature with file path " + configPath, configPath);

        if (!(sec.get(configPath) instanceof List))
            return holder.getChatils().formatErrorMessage("Scratch feature " + configPath + " is not a list", configPath);

        holder.getChatils().setPunctuated(false);
        result.append(holder.getChatils().formatChatMessage("Scratch feature " + configPath + ":\n", configPath));

        list = sec.getList(configPath);

        if (list == null)
            return holder.getChatils().formatErrorMessage("Scratch feature " + configPath + " is not a list", configPath);

        for (Object ob : list) {
            result.append(holder.getChatils().formatChatMessage("    " + ob + "\n"));
        }
        holder.getChatils().setPunctuated(true);

        return result.toString();
    }
}
