package com.nelsontron.scratch;

import com.nelsontron.mjarket.Mjarket;
import com.nelsontron.scratch.commands.admin.ScratchCommand;
import com.nelsontron.scratch.data.Stats;
import com.nelsontron.scratch.entity.Scratcher;
import com.nelsontron.scratch.listeners.*;
import com.nelsontron.scratch.utils.Utils;
import org.bukkit.plugin.java.JavaPlugin;

public final class Scratch extends JavaPlugin {

    private ScratcherHolder holder;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();

        holder = new ScratcherHolder(this)
                .registerAllScratchers()
                .loadAllScratchers();

        // register events
        registerEvents();
        Scratcher.registerEvents(this, holder);
        Stats.registerEvents(this, holder);

        // register commands
        Utils.registerCommands(new ScratchCommand(getConfig(), holder), this, "scratch");
        Scratcher.registerCommands(this, holder);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        saveConfig();

        holder.saveAllScratchers();
        holder.unregisterAllScratchers();
    }

    // getters
    // methods
    public void registerEvents() {
        if (!getConfig().getBoolean("scratch.harder-mobs"))
            getServer().getPluginManager().registerEvents(new HarderMobs(), this);
        if (!getConfig().getStringList("scratch.messages.join").isEmpty())
            getServer().getPluginManager().registerEvents(new JoinMessage(getConfig(), holder), this);
        if (getConfig().getString("scratch.messages.motd") != null && !getConfig().getString("scratch.messages.motd").isEmpty())
            getServer().getPluginManager().registerEvents(new Motd(getConfig(), holder), this);
        if (getConfig().getBoolean("scratch.old-food"))
            getServer().getPluginManager().registerEvents(new OldFood(), this);
        if (getConfig().getString("scratch.messages.welcome") != null && !getConfig().getString("scratch.messages.welcome").isEmpty())
            getServer().getPluginManager().registerEvents(new WelcomeMessage(getConfig(), holder), this);
    }
}
