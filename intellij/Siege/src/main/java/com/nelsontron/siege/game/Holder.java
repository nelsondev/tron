package com.nelsontron.siege.game;

import com.nelsontron.siege.Siege;
import com.nelsontron.siege.data.Data;
import com.nelsontron.siege.data.GameCreator;
import com.nelsontron.siege.entity.Fighter;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public interface Holder {

    // getters
    Siege getSiege();
    List<Fighter> getFighters();
    Fighter getFighter(Player player);
    Fighter getFighter(UUID uniqueId);
    List<Arena> getArenas();
    Arena getArena(String name);
    Game getGame();
    List<GameCreator> getCreators();
    GameCreator getCreator(Player player);

    // setters
    void addFighter(Fighter fighter);
    void addAllFighters(List<Fighter> fighters);
    Fighter instantiateFighter(Player player);
    List<Fighter> instantiateAllFighters();
    void deserializeAllFighters();
    void setArenas(List<Arena> arenas);
    void addArena(Arena arena);
    void removeArena(Arena arena);
    void setGame(Game game);

    // methods
    void serializeAllFighters();
    void removeFighter(Fighter fighter);
    void cleanFighters();
    void clearFighters();
    void loadArenaData();
    Data createUserData(Fighter fighter);
    void clearGameCreator(GameCreator creator);
    void createGameCreator(Player player);
}
