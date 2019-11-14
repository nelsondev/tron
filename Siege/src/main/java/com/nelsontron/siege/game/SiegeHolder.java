package com.nelsontron.siege.game;

import com.nelsontron.siege.Siege;
import com.nelsontron.siege.data.Data;
import com.nelsontron.siege.data.GameCreator;
import com.nelsontron.siege.entity.Fighter;
import com.nelsontron.siege.entity.SiegeFighter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.*;

public class SiegeHolder implements Holder {
    private Siege siege;
    private List<Fighter> fighters;
    private List<Arena> arenas;
    private Game game;
    private List<GameCreator> creators;

    public SiegeHolder(Siege siege) {
        this.siege = siege;
        this.fighters = new ArrayList<>();
        this.arenas = new ArrayList<>();
        this.game = null;
        this.creators = new ArrayList<>();
    }

    // getters
    public Siege getSiege() {
        return siege;
    }
    public List<Fighter> getFighters() {
        return new ArrayList<>(fighters);
    }
    public Fighter getFighter(Player player) {
        Fighter result = null;
        for (Fighter fighter : fighters) {
            if (fighter.getUniqueId().toString().equalsIgnoreCase(player.getUniqueId().toString()))
                result = fighter;
        }
        return result;
    }
    public Fighter getFighter(UUID uniqueId) {
        Fighter result = null;
        for (Fighter fighter : fighters) {
            if (fighter.getUniqueId() == uniqueId)
                result = fighter;
        }
        return result;
    }
    public List<Arena> getArenas() {
        return arenas;
    }
    public Arena getArena(String name) {
        Arena arena = null;
        for (Arena a : arenas) {
            if (a.getName().equalsIgnoreCase(name))
                arena = a;
        }
        return arena;
    }
    public Game getGame() {
        return game;
    }
    public List<GameCreator> getCreators() {
        return creators;
    }
    public GameCreator getCreator(Player player) {
        GameCreator creator = null;
        for (GameCreator c : creators) {
            if (c.getPlayer() == player)
                creator = c;
        }
        return creator;
    }

    // setters
    public void addFighter(Fighter fighter) {
        this.fighters.add(fighter);
    }
    public void addAllFighters(List<Fighter> fighters) { this.fighters.addAll(fighters); }
    public Fighter instantiateFighter(Player player) {
        return new SiegeFighter(this, player.getUniqueId());
    }
    public List<Fighter> instantiateAllFighters() {
        List<Fighter> fighters = new ArrayList<>();
        for (Player player : siege.getServer().getOnlinePlayers())
            fighters.add(instantiateFighter(player));
        return fighters;
    }
    public void deserializeAllFighters() {
        for (Fighter fighter : fighters)
            fighter.load();
    }
    public void setArenas(List<Arena> arenas) {
        this.arenas = arenas;
    }
    public void addArena(Arena arena) {
        this.arenas.add(arena);
    }
    public void removeArena(Arena arena) {
        this.arenas.remove(arena);
    }
    public void setGame(Game game) {
        this.game = game;
    }

    // methods
    public void serializeAllFighters() {
        for (Fighter fighter : fighters)
            fighter.save();
    }
    public void removeFighter(Fighter fighter) {
        this.fighters.remove(fighter);
    }
    public void cleanFighters() {
        for (Fighter fighter : fighters)
            fighter.clean();
    }
    public void clearFighters() {
        this.cleanFighters();
        this.fighters.clear();
    }
    public void loadArenaData() {
        ConfigurationSection section = siege.getConfig().getConfigurationSection("siege");
        List<Map<String, Object>> list = (List<Map<String, Object>>) section.get("arenas");
        for (Map<String, Object> map : list) {
            addArena(SiegeArena.deserialize(map));
        }
    }
    public Data createUserData(Fighter fighter) {
        return new Data(siege, fighter.getUniqueId());
    }
    public void createGameCreator(Player player) { creators.add(new GameCreator(player)); }
    public void clearGameCreator(GameCreator creator) {
        creators.remove(creator);
    }
}
