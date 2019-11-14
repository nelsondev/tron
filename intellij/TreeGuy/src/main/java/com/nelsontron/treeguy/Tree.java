package com.nelsontron.treeguy;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

class Tree {

    private final int MAX_TIMEOUT = 64;
    private int timeout;
    private List<Log> logs;

    Tree(Location location) {
        timeout = 0;
        logs = new ArrayList<>();

        findBlocks(location.getBlock());
    }

    List<Log> getLogs() { return logs; }
    Log getLog(Location location) {
        Log log = null;
        for (Log l : logs) {
            if (l.getLocation() == location)
                log = l;
        }
        return log;
    }

    private boolean containsLocation(Location location) {
        boolean result = false;
        for (Log l : logs) {
            if (l.getLocation().getX() == location.getX() && l.getLocation().getY() == location.getY() && l.getLocation().getZ() == location.getZ())
                result = true;
        }
        return result;
    }

    private boolean isSameBlock(Block block, Block block2) {
        if (block.getType() == block2.getType())
            return true;
        else
            return false;
    }

    private void findBlocks(Block block) {
        Log log;

        timeout++;

        if (timeout > MAX_TIMEOUT) return;

        for (BlockFace bf : BlockFace.values()) {
            Block b = block.getRelative(bf);

            if (!isSameBlock(block, b)) continue;
            if (containsLocation(b.getLocation())) continue;

            log = new Log(b);
            logs.add(log);
            findBlocks(b);
        }
    }

    void breakBlocks(Player player) {
        for (Log log : logs) {
            if (log != null) {
                if (player.getInventory().firstEmpty() != -1)
                    log.destroy(player);
                else {
                    player.sendMessage(ChatColor.RED + "> Inventory full. Stopping the chop.");
                    break;
                }
            }
        }
    }

    void restoreBlocks() {
        for (Log log : logs) {
            if (log != null)
                log.restore();
        }
    }

    void placeSaplings() {
        Block block;
        Material sapling;
        for (Log log : logs) {
            block = log.getBlock();
            if (log.isAbleToPlaceSapling()) {
                if (block == null)
                    return;

                sapling = TreeGuy.getSapling(block.getType());
                if (sapling != null)
                    block.setType(sapling);
            }
        }
    }

    class Log {

        private Location location;
        private Material type;
        private BlockData data;
        private boolean canPlaceSapling;

        Log(Block block) {
            this.location = block.getLocation();
            this.type = block.getType();
            this.data = block.getBlockData();
            this.canPlaceSapling = setCanPlaceSapling();
        }

        Location getLocation() { return location; }
        Material getType() { return type; }
        boolean isAbleToPlaceSapling() { return canPlaceSapling; }

        Block getBlock() {
            return location.getBlock();
        }

        void destroy(Player player) {
            Block block = location.getBlock();

            for (ItemStack drop : block.getDrops()) {
                player.getInventory().addItem(drop);
            }

            if (block.getType() != TreeGuy.getSapling(type))
                block.setType(Material.AIR);
        }

        void restore() {
            Block block = location.getBlock();
            if (block.getType() != type)
                block.setType(type);
            block.setBlockData(data);
        }

        boolean setCanPlaceSapling() {
            boolean bool = false;
            for (BlockFace bf : BlockFace.values()) {
                Block faceBlock = getBlock().getRelative(bf);
                if (faceBlock.getType() == Material.DIRT)
                    bool = true;
            }
            return bool;
        }
    }
}
