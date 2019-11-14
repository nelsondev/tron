package com.nelsontron.siege.entity;

import com.nelsontron.siege.data.Data;
import com.nelsontron.siege.data.KitCreator;
import com.nelsontron.siege.kits.Kit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface Fighter {

    // getters
    UUID getUniqueId();
    String getTeam();
    int getKills();
    int getDeaths();
    float getSr();
    Kit getKit();
    Player getPlayer();
    Kit[] getKits();
    KitCreator getCreator();
    boolean isAllowedToMove();

    // setters
    void setUniqueId(UUID uniqueId);
    void setTeam(String team);
    void setKills(int kills);
    void incrementKills(int amount);
    void decrementKills(int amount);

    void setDeaths(int deaths);
    void incrementDeaths(int amount);
    void decrementDeaths(int amount);

    void setSr(float sr);
    void incrementSr(float amount);
    void decrementSr(float amount);
    void incrementSr(Fighter killed);

    void setKit(int slot);
    void setData(Data data);
    void setCreator(KitCreator creator);

    Kit getDefaultKit();
    void setAllowedToMove(boolean allowedToMove);
    void applyKit();
    Map<String, Object> serialize();
    Fighter deserialize(Map<String, Object> map);
    void save();
    Fighter load();
    void clean();
}
