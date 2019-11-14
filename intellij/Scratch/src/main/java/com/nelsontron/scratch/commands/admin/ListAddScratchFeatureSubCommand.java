package com.nelsontron.scratch.commands.admin;

import com.nelsontron.scratch.ScratcherHolder;
import com.nelsontron.scratch.utils.Utils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class ListAddScratchFeatureSubCommand {
    /**
     * @return String
     */
    public static String getMessage(FileConfiguration config, ScratcherHolder holder, String configPath, String[] entry) {

        StringBuilder result = new StringBuilder();
        List list;
        ConfigurationSection sec = config.getConfigurationSection("scratch");

        if (sec.get(configPath) == null)
            return holder.getChatils().formatErrorMessage("Unable to find Scratch feature with file path " + configPath, configPath);

        if (!(sec.get(configPath) instanceof List))
            return holder.getChatils().formatErrorMessage("Scratch feature " + configPath + " is not a list", configPath);

        list = sec.getList(configPath);

        if (list == null)
            return holder.getChatils().formatErrorMessage("Scratch feature " + configPath + " is not a list", configPath);

        list.add(Utils.stringify(entry, " "));

        result.append(holder.getChatils().formatChatMessage("Added entry to feature " + configPath, configPath));

        return result.toString();
    }
}
