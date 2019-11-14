package com.nelsontron.sanction;

import com.nelsontron.sanction.entity.Bond;
import org.bukkit.Bukkit;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;

import java.util.*;

public class Sanction {

    private Main main;
    private List<Bond> bonds;
    private String defaultGroup;
    private boolean canFormatChat;
    private Map<String, Boolean> commands;

    public Sanction(Main main) {
        this.main = main;
        this.bonds = new ArrayList<>();
        this.defaultGroup = main.getConfig().getString("options.default-group");
        this.canFormatChat = main.getConfig().getBoolean("options.chat.format");
        this.commands = getEnabledCommands();
    }

    // getters
    public Main getMain() {
        return main;
    }
    public List<Bond> getBonds() {
        return bonds;
    }
    public String getDefaultGroup() {
        return defaultGroup;
    }
    public boolean willFormatChat() {
        return canFormatChat;
    }
    public Map<String, Boolean> getCommands() {
        return commands;
    }

    // setters
    public void setBonds(List<Bond> bonds) {
        this.bonds = bonds;
    }
    public void setDefaultGroup(String defaultGroup) {
        this.defaultGroup = defaultGroup;
    }
    public void setCanFormatChat(boolean canFormatChat) {
        this.canFormatChat = canFormatChat;
    }

    // methods
    private Map<String, Boolean> getEnabledCommands() {
        Map<String, Boolean> map = new HashMap<>();
        Map<String, Object> dataMap;
        MemorySection section = (MemorySection) main.getConfig().get("options.commands");

        if (section == null) return null;

        dataMap = section.getValues(false);
        for (String key : dataMap.keySet()) {
            if (key == null) continue;

            try {
                boolean result = (Boolean) dataMap.get(key);
                map.put(key, result);
            }
            catch (ClassCastException ex) {
                ex.printStackTrace();
            }
        }
        return map;
    }
    public void registerBond(Bond bond) {
        bonds.add(bond);
    }
    public void unregisterBond(Bond bond) {
        bonds.remove(bond);
    }
    public Bond instantiatePlayer(Player player) {
        return new Bond(this, player);
    }
    public Sanction instantiateAllPlayers() {
        for (Player player : Bukkit.getOnlinePlayers())
            bonds.add(new Bond(this, player));
        return this;
    }
    public Sanction registerAllPlayers(Bond ...bond) {
        Collections.addAll(bonds, bond);
        return this;
    }
    public Sanction unregisterAllPlayers() {
        bonds.clear();
        return this;
    }
    public Sanction saveAllPlayers() {
        for (Bond bond : bonds)
            bond.save();
        return this;
    }
    public Sanction loadAllPlayers() {
        for (Bond bond : bonds)
            bond.load();
        return this;
    }
    public void reload() {
        this.defaultGroup = main.getConfig().getString("options.default-group");
        this.canFormatChat = main.getConfig().getBoolean("options.chat.format");
        this.commands = getEnabledCommands();
        this.unregisterAllPlayers();
        this.instantiateAllPlayers();
        this.loadAllPlayers();
    }
}
