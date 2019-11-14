package com.nelsontron.mjarket.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class Data {

    private final String FILE_PATH;
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
     * @param playerId - define player unique id for appropriate file naming.
     */
    public Data(JavaPlugin main, UUID playerId) {
        this.FILE_PATH = main.getDataFolder() + "/playerData/";
        this.file = new File(FILE_PATH + playerId + ".yml");
        this.data = YamlConfiguration.loadConfiguration(file);
    }

    public Data(JavaPlugin main, String path, String fileName) {
        this.FILE_PATH = main.getDataFolder() + "/" + path + "/";
        this.file = new File(FILE_PATH + fileName + ".yml");
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

    // TODO: specific methods for getting projects, lists, and todo items.

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