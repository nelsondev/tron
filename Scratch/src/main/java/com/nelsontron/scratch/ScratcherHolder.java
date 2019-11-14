package com.nelsontron.scratch;

import com.nelsontron.scratch.data.Scrata;
import com.nelsontron.scratch.entity.Scratcher;
import com.nelsontron.scratch.utils.Chatils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ScratcherHolder {
    private Scratch scratch;
    private Chatils chatils;
    private List<Scratcher> scratchers;

    public ScratcherHolder(Scratch scratch) {
        this.scratch = scratch;
        this.chatils = new Chatils(scratch.getConfig());
        this.scratchers = new ArrayList<>();
    }

    // getters

    public Scratch getScratch() {
        return scratch;
    }
    public List<Scratcher> getScratchers() {
        return scratchers;
    }
    public Scratcher getScratcher(Player player) {

        Scratcher result = null;

        for (Scratcher scratcher : scratchers) {
            if (scratcher.getPlayer() == player)
                result = scratcher;
        }

        return result;
    }
    public Chatils getChatils() {
        return chatils;
    }

    // setters
    public void setScratchers(List<Scratcher> scratchers) {
        this.scratchers = scratchers;
    }

    // methods
    public void reloadPlugin() {
        Bukkit.getPluginManager().disablePlugin(scratch);
        Bukkit.getPluginManager().enablePlugin(scratch);
    }
    public void saveConfig() { scratch.saveConfig(); }
    public void reloadConfig() { scratch.reloadConfig(); }

    public void registerScratcher(Scratcher scratcher) {
        scratchers.add(scratcher);
    }

    public void unregisterScratcher(Scratcher scratcher) {
        scratchers.remove(scratcher);
    }

    public ScratcherHolder registerAllScratchers() {
        for (Player player : Bukkit.getOnlinePlayers())
            registerScratcher(new Scratcher(this, player.getUniqueId()));
        return this;
    }

    public void unregisterAllScratchers() {
        scratchers.clear();
    }

    public void saveAllScratchers() {
        for (Scratcher scratcher : scratchers)
            scratcher.save();
    }

    public ScratcherHolder loadAllScratchers() {
        for (Scratcher scratcher : scratchers)
            scratcher.load();
        return this;
    }

    public Scrata createPlayerData(UUID uuid) {
        return new Scrata(scratch, uuid);
    }
}
