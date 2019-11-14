package com.nelsontron.treeguy;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public final class TreeGuy extends JavaPlugin implements Listener, CommandExecutor {

    private HashMap<UUID, Tree> activePlayers;
    private final int MAX_BLOCKS = 128;

    @Override
    public void onEnable() {
        // Plugin startup logic

        activePlayers = new HashMap<>();

        // register commands
        getCommand("tree").setExecutor(this);

        // register events
        getServer().getPluginManager().registerEvents(this, this);
    }

    private boolean isWood(Block block) {
        boolean isOak = block.getType() == Material.OAK_LOG;
        boolean isAcadia = block.getType() == Material.ACACIA_LOG;
        boolean isBirch = block.getType() == Material.BIRCH_LOG;
        boolean isDark = block.getType() == Material.DARK_OAK_LOG;
        boolean isSpruce = block.getType() == Material.SPRUCE_LOG;
        boolean isJungle = block.getType() == Material.JUNGLE_LOG;

        return isOak || isAcadia || isBirch || isDark || isSpruce || isJungle;
    }

    static Material getSapling(Material type) {
        Material sap = null;

        boolean isOak = type == Material.OAK_LOG;
        boolean isAcadia = type == Material.ACACIA_LOG;
        boolean isBirch = type == Material.BIRCH_LOG;
        boolean isDark = type == Material.DARK_OAK_LOG;
        boolean isSpruce = type == Material.SPRUCE_LOG;
        boolean isJungle = type == Material.JUNGLE_LOG;

        if (isOak) sap = Material.OAK_SAPLING;
        if (isAcadia) sap = Material.ACACIA_SAPLING;
        if (isBirch) sap = Material.BIRCH_SAPLING;
        if (isDark) sap = Material.DARK_OAK_SAPLING;
        if (isSpruce) sap = Material.SPRUCE_SAPLING;
        if (isJungle) sap = Material.JUNGLE_SAPLING;

        return sap;
    }

    private boolean isAxe(ItemStack item) {
        boolean isDiamond = item.getType() == Material.DIAMOND_AXE;
        boolean isGold = item.getType() == Material.GOLDEN_AXE;
        boolean isIron = item.getType() == Material.IRON_AXE;
        boolean isStone = item.getType() == Material.STONE_AXE;
        boolean isWood = item.getType() == Material.WOODEN_AXE;

        return isDiamond || isGold || isIron || isStone || isWood;
    }

    @EventHandler
    public void blockBreakEvent(BlockBreakEvent ev) {
        Player player = ev.getPlayer();

        if (activePlayers.containsKey(player.getUniqueId()) && isWood(ev.getBlock()) && isAxe(player.getInventory().getItemInMainHand()) && player.isSneaking()) {
            Tree tree = new Tree(ev.getBlock().getLocation());
            activePlayers.put(player.getUniqueId(), tree);
            tree.placeSaplings();
            tree.breakBlocks(player);
            ev.setCancelled(true);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player;
        String result = null;

        try {

            player = (Player) sender;

            if (label.equalsIgnoreCase("tree")) {

                if (args.length == 0) {
                    if (activePlayers.containsKey(player.getUniqueId())) {
                        activePlayers.remove(player.getUniqueId());
                        result = ChatColor.GRAY + "> Toggled tree chopping " + ChatColor.WHITE + ChatColor.UNDERLINE + "off.";
                    }
                    else {
                        activePlayers.put(player.getUniqueId(), null);
                        result = ChatColor.GRAY + "> Toggled tree chopping " + ChatColor.WHITE + ChatColor.UNDERLINE + "on.";
                    }
                }
                else {
                    if (args[0].equalsIgnoreCase("restore")) {
                        if (activePlayers.containsKey(player.getUniqueId()) && activePlayers.get(player.getUniqueId()) != null) {

                            activePlayers.get(player.getUniqueId()).restoreBlocks();

                            for (Tree.Log log : activePlayers.get(player.getUniqueId()).getLogs()) {
                                player.getInventory().removeItem(new ItemStack(log.getBlock().getType(), log.getBlock().getDrops().size()));
                            }

                            result = ChatColor.GRAY + "> Restored last chopped wood.";

                            activePlayers.put(player.getUniqueId(), null);
                        }
                        else {
                            result = ChatColor.RED + "> No chop in memory.. Have you even enabled /tree?";
                        }
                    }
                }
            }
            else if (label.equalsIgnoreCase("ore")) {

            }

            return true;

        } catch (ClassCastException ex) {

            result = ChatColor.RED + "> Sender must be a player.";

        } finally {
            if (result != null)
                sender.sendMessage(result);
        }

        return false;
    }
}
