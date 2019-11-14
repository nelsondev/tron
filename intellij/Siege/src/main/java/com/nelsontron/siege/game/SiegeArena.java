package com.nelsontron.siege.game;

import com.nelsontron.siege.utils.BukkitUtils;
import com.sun.istack.internal.NotNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SiegeArena implements Arena {

    private String name;
    private Location blueSpawn;
    private Location redSpawn;
    private List<Block> blocks;

    public SiegeArena() {
        this.blueSpawn = null;
        this.redSpawn = null;
        this.blocks = new ArrayList<>();
    }

    // getters
    public String getName() {
        return name;
    }
    public Location getBlueSpawn() {
        return blueSpawn;
    }
    public Location getRedSpawn() {
        return redSpawn;
    }
    public List<Block> getBlocks() {
        return blocks;
    }
    public Block getBlock(org.bukkit.block.Block block) {
        Block result = null;
        for (Block b : blocks) {
            if (b.getLocation() == block.getLocation().toBlockLocation())
                result = b;
        }
        return result;
    }

    // setters
    public void setName(String name) {
        this.name = name;
    }
    public void setBlueSpawn(Location blueSpawn) {
        this.blueSpawn = blueSpawn;
    }
    public void setRedSpawn(Location redSpawn) {
        this.redSpawn = redSpawn;
    }
    public void setBlocks(List<Block> blocks) {
        this.blocks = blocks;
    }
    public void addBlock(org.bukkit.block.Block block) {
        blocks.add(new SiegeBlock(block));
    }
    public void removeBlock(org.bukkit.block.Block block) {
        blocks.remove(getBlock(block));
    }

    // methods
    public void destroyBlocks() {
        for (Block block : blocks) {
            block.destroy();
        }
    }
    public void restoreBlocks() {
        for (Block block : blocks) {
            block.restore();
        }
    }
    public void clearBlocks() {
        blocks.clear();
    }

    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        if (name != null)
            map.put("name", name);
        if (blueSpawn != null)
            map.put("spawn-blue", BukkitUtils.serializeLocation(blueSpawn));
        if (redSpawn != null)
            map.put("spawn-red", BukkitUtils.serializeLocation(redSpawn));
        return map;
    }

    // nested
    public interface Block {

        // getters
        int getX();
        int getY();
        int getZ();
        String getWorld();
        Material getType();
        BlockData getData();
        Location getLocation();
        org.bukkit.block.Block getBlock();

        // setters
        void setX(int x);
        void setY(int y);
        void setZ(int z);
        void setWorld(String world);
        void setBlock(@NotNull Block block);
        void setLocation(@NotNull Location location);
        void setType(Material type);
        void setData(BlockData data);

        // methods
        void destroy();
        void restore();
    }
    private class SiegeBlock implements Block {
        private int x, y, z;
        private String world;
        private Material type;
        private BlockData data;

        SiegeBlock() {
            x = 0;
            y = 0;
            z = 0;
            world = null;
            type = null;
            data = null;
        }

        SiegeBlock(org.bukkit.block.Block block) {
            Location location = block.getLocation().toBlockLocation();
            x = location.getBlockX();
            y = location.getBlockY();
            z = location.getBlockZ();
            world = location.getWorld().getName();
            type = location.getBlock().getType();
            data = location.getBlock().getBlockData();
        }

        // getters
        public int getX() {
            return x;
        }
        public int getY() {
            return y;
        }
        public int getZ() {
            return z;
        }
        public String getWorld() {
            return world;
        }
        public Material getType() {
            return type;
        }
        public BlockData getData() {
            return data;
        }
        public Location getLocation() {
            return new Location(Bukkit.getWorld(world), x, y, z);
        }
        public org.bukkit.block.Block getBlock() {
            return getLocation().getBlock();
        }

        // setters
        public void setX(int x) {
            this.x = x;
        }
        public void setY(int y) {
            this.y = y;
        }
        public void setZ(int z) {
            this.z = z;
        }
        public void setWorld(String world) {
            this.world = world;
        }
        public void setBlock(@NotNull Block block) {
            Location location = block.getLocation();
            x = location.getBlockX();
            y = location.getBlockY();
            z = location.getBlockZ();
            world = location.getWorld().getName();
            type = location.getBlock().getType();
            data = location.getBlock().getBlockData();
        }
        public void setLocation(@NotNull Location location) {
            x = location.getBlockX();
            y = location.getBlockY();
            z = location.getBlockZ();
            world = location.getWorld().getName();
            type = location.getBlock().getType();
            data = location.getBlock().getBlockData();
        }
        public void setType(Material type) {
            this.type = type;
        }
        public void setData(BlockData data) {
            this.data = data;
        }

        // methods
        public void destroy() {
            if (type != Material.AIR)
                getBlock().setType(Material.AIR);
        }
        public void restore() {
            getBlock().setType(type);
            getBlock().setBlockData(data);
        }
    }

    // static
    public static Arena deserialize(Map<String, Object> map) {
        SiegeArena arena = new SiegeArena();
        if (map.containsKey("name"))
            arena.setName((String) map.get("name"));
        if (map.containsKey("spawn-blue"))
            arena.setBlueSpawn(BukkitUtils.deserializeLocation((Map<String, Object>) map.get("spawn-blue")));
        if (map.containsKey("spawn-red"))
            arena.setRedSpawn(BukkitUtils.deserializeLocation((Map<String, Object>) map.get("spawn-red")));
        return arena;
    }
}
