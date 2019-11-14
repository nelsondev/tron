package com.nelsontron.lockguy;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import com.nelsontron.lockguy.utils.Data;
import com.nelsontron.mjarket.Mjarket;
import com.nelsontron.mjarket.entity.Consumer;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.Hopper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class LockGuy extends JavaPlugin implements CommandExecutor {

    private LockerHolder lockerHolder = new LockerHolder(this);

    @Override
    public void onEnable() {

        // Plugin startup logic
        getCommand("lock").setExecutor(this);
        getCommand("sort").setExecutor(new SortCommand());

        getServer().getPluginManager().registerEvents(lockerHolder, this);

        for (Player online : Bukkit.getOnlinePlayers())
            lockerHolder.registerPlayer(online);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        for (Player online : Bukkit.getOnlinePlayers())
            lockerHolder.unregisterPlayer(online);
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player;
        Locker locker;
        Consumer consumer;
        Location cursor;
        Chest chest;
        Box box;
        InventoryHolder holder;

        String result = null;

        try {

            player = (Player) sender;
            locker = lockerHolder.getLocker(player.getUniqueId());
            consumer = Mjarket.getGovernment().getConsumerData().getConsumer(player.getUniqueId());
            Block target = player.getTargetBlock( 16);

            if (target.getState() instanceof Chest) {
                chest = (Chest) target.getState();
                cursor = chest.getLocation();
                holder = chest.getInventory().getHolder();
            }
            else {
                result = ChatColor.RED + "> Player must be looking at a chest.";
                return true;
            }

            if (label.equalsIgnoreCase("lock")) {

                if (holder instanceof DoubleChest) {

                    /**
                     * MJARKET
                     */
                    if (consumer.getDoub() < Mjarket.LOCK_PRICE) {
                        float remaining = Mjarket.LOCK_PRICE-consumer.getDoub();
                        result = ChatColor.RED + "> You need " + remaining +" more doubloons.";
                        return true;
                    }

                    Box leftBox;
                    Box rightBox;
                    Chest leftChest = (Chest)((DoubleChest)holder).getLeftSide();
                    Chest rightChest = (Chest)((DoubleChest)holder).getRightSide();

                    leftBox = locker.getBox(leftChest.getLocation());
                    rightBox = locker.getBox(rightChest.getLocation());

                    if (rightBox == null && leftBox == null) {
                        if (lockerHolder.getGlobalBox(leftChest) != null || lockerHolder.getGlobalBox(rightChest) != null) {
                            result = ChatColor.RED + "> That chest has already been locked.";
                            return true;
                        }
                        leftBox = new Box(leftChest);
                        rightBox = new Box(rightChest);
                        locker.getBoxes().add(leftBox);
                        locker.getBoxes().add(rightBox);
                        result = ChatColor.GRAY + "Locked double chest.";
                    }
                    else {
                        locker.getBoxes().remove(leftBox);
                        locker.getBoxes().remove(rightBox);
                        result = ChatColor.RED + "Unlocked double chest.";
                    }
                    /**
                     * MJARKET
                     */
                    consumer.setDoub(consumer.getDoub()-Mjarket.LOCK_PRICE);
                    consumer.getServices().add(Mjarket.LOCK_PRICE);
                    consumer.setActivity(consumer.getActivity() + 10);
                }
                else {
                    /**
                     * MJARKET
                     */
                    if (consumer.getDoub() < Mjarket.LOCK_PRICE) {
                        float remaining = Mjarket.LOCK_PRICE-consumer.getDoub();
                        result = ChatColor.RED + "> You need " + remaining +" more doubloons.";
                        return true;
                    }

                    box = locker.getBox(chest.getLocation());
                    if (box == null) {
                        if (lockerHolder.getGlobalBox(chest) != null) {
                            result = ChatColor.RED + "That chest has already been locked.";
                            return true;
                        }
                        box = new Box(chest);
                        locker.getBoxes().add(box);
                        result = ChatColor.GRAY + "Locked chest.";
                    }
                    else {
                        locker.getBoxes().remove(box);
                        result = ChatColor.RED + "Unlocked chest.";
                    }
                    /**
                     * MJARKET
                     */
                    consumer.setDoub(consumer.getDoub()-Mjarket.LOCK_PRICE);
                    consumer.getServices().add(Mjarket.LOCK_PRICE);
                    consumer.setActivity(consumer.getActivity() + 10);
                }
                player.playSound(player.getLocation(), Sound.BLOCK_CHEST_LOCKED, 5, 1);
            }
        } catch (ClassCastException ex) {
            result = ChatColor.RED + "> Error.";
        } finally {
            if (result != null)
                sender.sendMessage(result);
        }

        return true;
    }
}
