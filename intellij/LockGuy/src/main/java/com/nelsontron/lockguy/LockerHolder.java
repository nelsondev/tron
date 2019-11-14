package com.nelsontron.lockguy;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Hopper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LockerHolder implements Listener {

    private LockGuy lockGuy;
    private List<Locker> lockers;

    public LockerHolder(LockGuy lockGuy) {
        this.lockGuy = lockGuy;
        this.lockers = new ArrayList<>();
    }


    public List<Locker> getLockers() {
        return lockers;
    }

    // methods
    public boolean containsLocker(Locker locker) {
        return lockers.contains(locker);
    }
    public Locker getLocker(UUID uuid) {
        Locker locker = null;
        for (Locker l : lockers) {
            if (l.getUuid() == uuid) {
                locker = l;
                break;
            }
        }
        return  locker;
    }
    public Box getGlobalBox(Chest chest) {
        Box box = null;
        for (Locker locker : lockers) {
            box = locker.getBox(chest.getLocation());
            if (box == null)
                continue;
            else
                break;
        }
        return box;
    }
    public void registerPlayer(Player player) {
        Locker locker;
        locker = new Locker(lockGuy, player);

        locker.load();
        lockers.add(locker);
    }
    public void unregisterPlayer(Player player) {
        Locker locker;
        locker = getLocker(player.getUniqueId());
        if (locker != null) {
            locker.save();
            lockers.remove(locker);
        }
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent ev) {
        Player player = ev.getPlayer();
        registerPlayer(player);
    }
    @EventHandler
    public void playerQuit(PlayerQuitEvent ev) {
        Player player = ev.getPlayer();
        unregisterPlayer(player);
    }

    @EventHandler
    public void playerInteract(PlayerInteractEvent ev) {
        if (ev.getAction() == Action.RIGHT_CLICK_BLOCK && ev.getClickedBlock().getState() instanceof Chest) {

            Locker locker = getLocker(ev.getPlayer().getUniqueId());
            Chest chest = (Chest) ev.getClickedBlock().getState();
            Location location = chest.getLocation();

            boolean isLocked = false;

            for (Locker l : lockers) {
                if (l != locker && l.hasBox(location))
                    isLocked = true;
            }

            if (isLocked && !locker.hasBox(location)) {
                ev.getPlayer().sendMessage(ChatColor.RED + "That chest is locked.");
                ev.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void playerBreak(BlockBreakEvent ev) {
        Block block = ev.getBlock();
        if (block.getState() instanceof Chest) {
            Chest chest = (Chest) block.getState();
            if (getGlobalBox(chest) != null) {
                ev.getPlayer().sendMessage(ChatColor.RED + "That chest is locked. Please unlock it first.");
                ev.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent ev) {
        for (Block block : ev.blockList().toArray(new Block[ev.blockList().size()])){
            if(block.getState() instanceof Chest){
                Chest chest;
                chest = (Chest) block.getState();
                if (getGlobalBox(chest) != null) {
                    ev.blockList().remove(block);
                }
            }
        }
    }

    @EventHandler
    public void hopperSuccEvent(InventoryMoveItemEvent ev) {
        if (ev.getDestination().getLocation().getBlock().getState() instanceof Hopper) {
            if (ev.getSource().getLocation().getBlock().getState() instanceof Chest) {
                Chest chest = (Chest) ev.getSource().getLocation().getBlock().getState();
                if (getGlobalBox(chest) != null)
                    ev.setCancelled(true);
            }
        }
    }
}
