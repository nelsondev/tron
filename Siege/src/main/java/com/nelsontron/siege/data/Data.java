package com.nelsontron.siege.data;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class Data {
    private File file;
    private FileConfiguration data;

    /**
     * Constructor TodoData
     * ....................
     * creating a to-do data entity are player specific. Each player must
     * have an appropriate to-do data object attached on to their Project data.
     *
     * creating new to do data can be done on player join, as well as plugin enable.
     * assigning data to a player can be done by creating a new Project object.
     * The new Project(); constructor takes the parameters of a TodoData object for
     * easy data manipulation.
     *
     * @param player - define a player to grab a unique ID from.
     */
    public Data(JavaPlugin main, Player player) {
        this.file = new File(main.getDataFolder() + "/playerData/" + player.getUniqueId() + ".yml");
        this.data = YamlConfiguration.loadConfiguration(file);
    }

    public Data(JavaPlugin main, UUID uniqueId) {
        this.file = new File(main.getDataFolder() + "/playerData/" + uniqueId + ".yml");
        this.data = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * return file
     */
    public File getFile() {
        return file;
    }

    /**
     * return data
     */
    public FileConfiguration getData() {
        return data;
    }

    /**
     * save data
     */
    public void save() {
        try {
            data.save(file);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    public void clean() {
        file = null;
        data = null;
    }
}
