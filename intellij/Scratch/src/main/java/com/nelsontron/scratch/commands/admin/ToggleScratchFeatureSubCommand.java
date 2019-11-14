package com.nelsontron.scratch.commands.admin;

import com.nelsontron.scratch.ScratcherHolder;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class ToggleScratchFeatureSubCommand {

    /**
     * @return String
     */
    public static String getMessage(FileConfiguration config, ScratcherHolder holder, String configPath) {

        boolean feature;
        ConfigurationSection sec = config.getConfigurationSection("scratch");

        if (sec.get(configPath) == null)
            return holder.getChatils().formatErrorMessage("Unable to find Scratch feature with file path " + configPath, configPath);

        if (!(sec.get(configPath) instanceof Boolean))
            return holder.getChatils().formatErrorMessage("Scratch feature " + configPath + " is not toggleable", configPath);

        feature = sec.getBoolean(configPath);
        feature = !feature;

        config.set("scratch." + configPath, feature);

        if (feature)
            return holder.getChatils().formatChatMessage("Successfully enabled Scratch feature " + configPath + " please reload the plugin", configPath);
        else
            return holder.getChatils().formatChatMessage("Successfully disabled Scratch feature " + configPath + " please reload the plugin", configPath);
    }
}
