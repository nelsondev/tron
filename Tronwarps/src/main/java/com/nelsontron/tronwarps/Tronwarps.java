package com.nelsontron.tronwarps;

import com.nelsontron.tronwarps.commands.handlers.WarpHandler;
import com.nelsontron.tronwarps.listeners.RegisterPlayers;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class Tronwarps extends JavaPlugin {

    private List<Warper> warpers;

    @Override
    public void onEnable() {
        warpers = new ArrayList<>();

        // register all players
        registerAll();
        // load all players
        loadAll();

        // register listeners
        getServer().getPluginManager().registerEvents(new RegisterPlayers(this), this);

        // register commands
        Utils.registerCommands(new WarpHandler(this), this, "warp", "w");
    }

    @Override
    public void onDisable() {

        saveAll();
    }

    // methods
    public Warper registerPlayer(Player player) {
        Warper warper = new Warper(this, player);
        warpers.add(warper);
        return warper;
    }

    public Warper getWarper(Player player) {
        Warper w = null;
        for (Warper warper : warpers) {
            if (warper.getUuid().equals(player.getUniqueId()))
                w = warper;
        }
        return w;
    }

    public void removePlayer(Warper warper) {
        warpers.remove(warper);
    }

    private void registerAll() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Warper warper = new Warper(this, player);
            warpers.add(warper);
        }
    }
    private void saveAll() {
        for (Warper warper : warpers)
            warper.save();
    }
    private void loadAll() {
        for (Warper warper : warpers)
            warper.load();
    }
}
