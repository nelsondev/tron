package com.nelsontron.scratch.listeners.stats;

import com.nelsontron.scratch.ScratcherHolder;
import com.nelsontron.scratch.entity.Scratcher;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

public class CraftedItem implements Listener {
    private ScratcherHolder holder;

    public CraftedItem(ScratcherHolder holder) {
        this.holder = holder;
    }

    @EventHandler
    public void playerCraftItem(CraftItemEvent ev) {
        Player player = (Player) ev.getWhoClicked();
        Scratcher scratcher = holder.getScratcher(player);
        scratcher.getStats().incrementCraftedItems();
    }
}
