package com.nelsontron.scratch.commands.admin;

import com.nelsontron.scratch.ScratcherHolder;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class GetScratchFeatureSubCommand {
    /**
     * @return String
     */
    public static String getMessage(FileConfiguration config, ScratcherHolder holder, String configPath) {

        StringBuilder result = new StringBuilder();
        ConfigurationSection sec = config.getConfigurationSection("scratch");

        if (sec.get(configPath) == null)
            return holder.getChatils().formatErrorMessage("Unable to find Scratch feature with file path " + configPath, configPath);

        if (configPath.contains("colours")) {
            result.append(holder.getChatils().formatChatMessage("Scratch feature " + configPath + ": "
                    + ChatColor.translateAlternateColorCodes('&', sec.getString(configPath)
                    + "THIS COLOUR"), configPath));
        }
        else {
            result.append(holder.getChatils().formatChatMessage("Scratch feature " + configPath + ": " + sec.get(configPath), configPath));
        }

        return result.toString();
    }
}
