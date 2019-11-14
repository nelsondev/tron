package com.nelsontron.sanction;

import com.nelsontron.sanction.util.DataUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;

public class Main extends JavaPlugin {

    Sanction sanction;

    /**
     * PLUGIN START
     */
    @Override
    public void onEnable() {
        // Plugin startup logic
        sanction = new Sanction(this)
                .instantiateAllPlayers()
                .loadAllPlayers();

        Yaml yaml = new Yaml();

        Ob ob = new Ob();

        try {
            DataUtils.save(yaml, ob, this.getDataFolder() + "/test.yaml");
        } catch (IOException ex) {
            this.saveDefaultConfig();
            this.onEnable();

            warning("COULDN'T FIND PLUGIN DATA FOLDER... CREATING NOW");
        }

        try {
            System.out.println(DataUtils.load(yaml, this.getDataFolder() + "/test.yaml"));
        }
        catch (FileNotFoundException ex) {

        }
    }

    public void info(String string) {
        Bukkit.getLogger().info("[" + this.getName() + "] " + string);
    }

    public void warning(String string) {
        Bukkit.getLogger().warning("[" + this.getName() + "] " + string);
    }

    public void severe(String string) {
        Bukkit.getLogger().severe("[" + this.getName() + "] " + string);
    }

    private class Ob {
        private String name;

        public Ob() {
            this.name = "nelly";
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * PLUGIN END
     */
    @Override
    public void onDisable() {
        // Plugin shutdown logic

    }
}
