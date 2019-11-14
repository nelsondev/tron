package com.nelsontron.scratch.commands.admin;

import com.nelsontron.scratch.ScratcherHolder;
import com.nelsontron.scratch.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class SetScratchFeatureSubCommand {
    /**
     * @return String
     */
    public static String getMessage(FileConfiguration config, ScratcherHolder holder, String configPath, String[] entry) {

        StringBuilder result = new StringBuilder();
        ConfigurationSection sec = config.getConfigurationSection("scratch");

        if (sec.get(configPath) == null)
            return holder.getChatils().formatErrorMessage("Unable to find Scratch feature with file path " + configPath, configPath);

        if (sec.get(configPath) instanceof Boolean)
            return holder.getChatils().formatErrorMessage("Please use the /scratch toggle command for " + configPath, configPath);

        if (!(sec.get(configPath) instanceof String))
            return holder.getChatils().formatErrorMessage("Scratch feature " + configPath + " is not a string.", configPath);

        String feature = sec.getString(configPath);

        feature = Utils.stringify(entry, " ");

        sec.set(configPath, feature);

        if (configPath.contains("colours")) {
            result.append(holder.getChatils().formatChatMessage("Set Scratch feature " + configPath + " to: "
                    + ChatColor.translateAlternateColorCodes('&', sec.getString(configPath)
                    + "THIS COLOUR"), configPath));
        }
        else {
            result.append(holder.getChatils().formatChatMessage("Scratch feature " + configPath + " to: " + sec.get(configPath), configPath));
        }

        return result.toString();
    }
}
