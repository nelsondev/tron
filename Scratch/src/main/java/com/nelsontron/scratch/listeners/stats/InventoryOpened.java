package com.nelsontron.scratch.listeners.stats;

import com.nelsontron.scratch.ScratcherHolder;
import com.nelsontron.scratch.entity.Scratcher;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class InventoryOpened implements Listener {
    private ScratcherHolder holder;

    public InventoryOpened(ScratcherHolder holder) {
        this.holder = holder;
    }

    @EventHandler
    public void inventoryOpened(InventoryOpenEvent ev) {
        Player player = (Player) ev.getPlayer();
        Scratcher scratcher = holder.getScratcher(player);
        scratcher.getStats().incrementInventoryOpened();
    }
}
