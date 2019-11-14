package com.nelsontron.siege.game;

import org.bukkit.Location;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface Arena {

    // getters
    String getName();
    Location getBlueSpawn();
    Location getRedSpawn();
    List<SiegeArena.Block> getBlocks();

    // setters
    void setName(String name);
    void setBlueSpawn(Location blueSpawn);
    void setRedSpawn(Location redSpawn);
    void setBlocks(List<SiegeArena.Block> blocks);
    void addBlock(org.bukkit.block.Block block);
    void removeBlock(org.bukkit.block.Block block);

    // methods
    void destroyBlocks();
    void restoreBlocks();
    void clearBlocks();
    Map<String, Object> serialize();
}
