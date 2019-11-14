package com.nelsontron.permissions;

import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        new Perm(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
