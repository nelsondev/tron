package com.nelsontron.siege.listeners.menu;

import com.nelsontron.siege.data.KitCreator;
import com.nelsontron.siege.data.MenuGui;
import com.nelsontron.siege.entity.Fighter;
import com.nelsontron.siege.game.Holder;
import com.nelsontron.siege.kits.Item;
import com.nelsontron.siege.kits.Kit;
import com.nelsontron.siege.kits.SiegeItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class MainMenuListener implements Listener {

    Holder holder;

    public MainMenuListener(Holder holder) {
        this.holder = holder;
    }

    /*
     * LOAD
     */
    @EventHandler
    public void inventoryOpen(InventoryOpenEvent ev) {
    }

    /*
     * EDIT
     */
    @EventHandler
    public void inventoryClick(InventoryClickEvent ev) {
        Player player = (Player) ev.getWhoClicked();
        Fighter fighter = holder.getFighter(player);
        KitCreator creator;

        Inventory inventory = ev.getClickedInventory();
        InventoryView view = ev.getView();
        ItemStack clicked = ev.getCurrentItem();
        String itemName;

        /*
         * NULL CHECKS
         */
        if (inventory == null ||
                !view.getTitle().equalsIgnoreCase("menu"))
            return;

        if (clicked == null) {
            ev.setCancelled(true);
            return;
        }

        itemName = ChatColor.stripColor(clicked.getItemMeta().getDisplayName());

        /*
         * ITEM CHECKS
         */
        if (itemName.equalsIgnoreCase("back")) {
            player.closeInventory();
        }

        if (itemName.equalsIgnoreCase("list")) {
            player.openInventory(MenuGui.getKitInventory(fighter));
        }

        ev.setCancelled(true);
    }

    /*
     * SAVE
     */
    @EventHandler
    public void inventoryClose(InventoryCloseEvent ev) {

    }
}
