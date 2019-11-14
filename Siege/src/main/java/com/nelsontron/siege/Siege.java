package com.nelsontron.siege;

import com.nelsontron.siege.commands.GameCommand;
import com.nelsontron.siege.commands.MenuCommand;
import com.nelsontron.siege.entity.SiegeFighter;
import com.nelsontron.siege.game.*;
import com.nelsontron.siege.listeners.SetupScriptListener;
import com.nelsontron.siege.listeners.menu.CreatorMenuListener;
import com.nelsontron.siege.listeners.menu.ListMenuListener;
import com.nelsontron.siege.listeners.menu.MainMenuListener;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public final class Siege extends JavaPlugin {

    // The siege main class reference to its "SiegeHolder" or interface "Holder."
    private Holder holder;
    // give the plugin a map to store tasks in. This is pretty much specifically because
    // tasks in bukkit are annoying and you cant just sleep 10; or something since we're
    // on such a high level.
    public Map<String, Integer> tasks = new HashMap<>();

    /**
     * onEnable interface override from "JavaPlugin"
     * override the default onEnable() function within
     * the object "JavaPlugin" of which we extend.
     *
     * All main() function processes should go here. IE:
     * Object Initialization, configuration saving, data
     * saving, minecraft command registry, and minecraft
     * event registry.
     */
    @Override
    public void onEnable() {

        // Plugin startup logic
        saveDefaultConfig();

        // create a new SiegeHolder(); object which requires a
        // Siege object to carry out its processes. The SiegeHolder
        // object handles most siege processes and data for security.
        holder = new SiegeHolder(this);
        // load in user defined arenas within the Server Owner's
        // configurable plugin file or "config.yml" which is
        // generated on plugin start.
        holder.loadArenaData();
        // add all online players to the holders data storage list
        holder.addAllFighters(holder.instantiateAllFighters());
        holder.deserializeAllFighters();

        // register commands
        PluginCommand game = getCommand("game");
        PluginCommand kit = getCommand("kit");

        if (game != null)
            game.setExecutor(new GameCommand((holder)));
        if (kit != null)
            kit.setExecutor(new MenuCommand(holder));

        // register events
        SiegeFighter.registerEvents(holder);
        getServer().getPluginManager().registerEvents(new SetupScriptListener(holder), this);
        getServer().getPluginManager().registerEvents(new CreatorMenuListener(holder), this);
        getServer().getPluginManager().registerEvents(new ListMenuListener(holder), this);
        getServer().getPluginManager().registerEvents(new MainMenuListener(holder), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        holder.serializeAllFighters();
        holder.clearFighters();
    }

    /**
     * Cancel a bukkit scheduled task that was saved within
     * the "tasks" Map defined above at the top of the class.
     *
     * @param key - key related to hashmap "tasks" defined above.
     */
    public void cancelTask(String key) {
        try {
            Bukkit.getScheduler().cancelTask(tasks.get(key));
            tasks.remove(key); // remove from siege tasks.
        } catch(NullPointerException ex) {
            return;
        }
    }
}
