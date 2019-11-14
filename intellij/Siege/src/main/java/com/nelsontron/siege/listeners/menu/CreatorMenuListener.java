package com.nelsontron.siege.listeners.menu;

import com.nelsontron.siege.data.KitCreator;
import com.nelsontron.siege.data.MenuGui;
import com.nelsontron.siege.entity.Fighter;
import com.nelsontron.siege.game.Holder;
import com.nelsontron.siege.kits.Item;
import com.nelsontron.siege.kits.SiegeItem;
import com.nelsontron.siege.utils.BukkitUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;

public class CreatorMenuListener implements Listener {

    Holder holder;

    public CreatorMenuListener(Holder holder) {
        this.holder = holder;
    }

    /*
     * LOAD
     */
    @EventHandler
    public void inventoryLoad(InventoryOpenEvent ev) {
        Player player = (Player) ev.getPlayer();
        Fighter fighter = holder.getFighter(player);
        KitCreator creator = fighter.getCreator();

        Inventory inventory = ev.getInventory();
        InventoryView view = ev.getView();

        /*
         * NULL CHECKS
         */
        if (creator == null
                || !view.getTitle().equalsIgnoreCase("creator"))
            return;
    }

    /*
     * EDIT
     */
    private int[] shiftItems(Inventory inventory, int direction, int ...slots) {
        int[] result = new int[slots.length];
        int slot;
        ItemStack item;

        int count = 0;
        for (int s : slots) {
            // grab the new slot that the item will be shifted to.
            slot = s+direction;
            // get old item
            item = inventory.getItem(s);
            // set new slot to old item
            inventory.setItem(slot, item);
            // clear its old position
            inventory.setItem(s, null);
            // save to result;
            result[count] = slot;

            count++;
        }
        return result;
    }
    private void changeMaterialLater(Inventory inventory, int slot, Material toMaterial, int delay) {
        ItemStack item = inventory.getItem(slot);

        if (item == null) return;

        holder.getSiege().getServer().getScheduler().scheduleSyncDelayedTask(holder.getSiege(), () -> {
            item.setType(toMaterial);
        }, delay);
    }
    private void changeItemLater(Inventory inventory, int slot, Item toItem, int delay) {
        holder.getSiege().getServer().getScheduler().scheduleSyncDelayedTask(holder.getSiege(), () -> {
            inventory.setItem(slot, toItem.getItem());
        }, delay);
    }
    private void animateArmour(Inventory inventory) {

        // type slots = 5, 6, 7, 8
        // armour slots = 14, 15, 16, 17

        ItemStack ench = inventory.getItem(5);
        ItemStack effects = inventory.getItem(6);
        ItemStack perks = inventory.getItem(7);
        ItemStack kitoptions = inventory.getItem(8);
        ItemStack back = inventory.getItem(9);
        ItemStack boot = inventory.getItem(14);
        ItemStack leg = inventory.getItem(15);
        ItemStack chest = inventory.getItem(16);
        ItemStack helm = inventory.getItem(17);

        // rename items so player cant fiddle with em
        inventory.setItem(5, BukkitUtils.changeItemName(ench, ChatColor.DARK_RED + "" + ChatColor.UNDERLINE + "Leather"));
        inventory.setItem(6, BukkitUtils.changeItemName(effects, ChatColor.DARK_GRAY + "" + ChatColor.UNDERLINE + "Iron"));
        inventory.setItem(7, BukkitUtils.changeItemName(perks, ChatColor.DARK_AQUA + "" + ChatColor.UNDERLINE + "Diamond"));
        inventory.setItem(8, BukkitUtils.changeItemName(kitoptions, ChatColor.GOLD + "" + ChatColor.UNDERLINE + "Gold"));

        inventory.setItem(9, BukkitUtils.changeItemLore(BukkitUtils.changeItemName(back, ChatColor.RED + "Previous"), ChatColor.GRAY + "Back to your kit creator."));

        inventory.setItem(14, BukkitUtils.changeItemName(boot, ChatColor.WHITE + "" + ChatColor.UNDERLINE + "Boots"));
        inventory.setItem(15, BukkitUtils.changeItemName(leg, ChatColor.WHITE + "" + ChatColor.UNDERLINE + "Legs"));
        inventory.setItem(16, BukkitUtils.changeItemName(chest, ChatColor.WHITE + "" + ChatColor.UNDERLINE + "Chest"));
        inventory.setItem(17, BukkitUtils.changeItemName(helm, ChatColor.WHITE + "" + ChatColor.UNDERLINE + "Helm"));

        int[] material = shiftItems(inventory, -1, 2, 5, 6, 7, 8);
        int[] armour = shiftItems(inventory, -1, 11, 14, 15, 16, 17);
        int[] options = shiftItems(inventory, 1, 0, 9);

        Item leather = new SiegeItem();
        Item iron = new SiegeItem();
        Item diamond = new SiegeItem();
        Item gold = new SiegeItem();

        leather.setItem(new ItemStack(Material.LEATHER));
        iron.setItem(new ItemStack(Material.IRON_INGOT));
        diamond.setItem(new ItemStack(Material.DIAMOND));
        gold.setItem(new ItemStack(Material.GOLD_INGOT));

        leather.setName(ChatColor.DARK_RED + "" + ChatColor.UNDERLINE + "Leather");
        iron.setName(ChatColor.DARK_GRAY + "" + ChatColor.UNDERLINE + "Iron");
        diamond.setName(ChatColor.DARK_AQUA + "" + ChatColor.UNDERLINE + "Diamond");
        gold.setName(ChatColor.GOLD + "" + ChatColor.UNDERLINE + "Gold");

        leather.setDescription(ChatColor.RED + "Tattered;new"
                + ChatColor.GRAY + "You bear the clothes on your back.;new ;new"
                + ChatColor.GRAY + "+ 1 agility;new"
                + ChatColor.GRAY + "+ 1 armour;new"
                + ChatColor.GRAY + "- 0 weight");
        iron.setDescription(ChatColor.WHITE + "Battered;new"
                + ChatColor.GRAY + "A shifty set from the local smith.;new ;new"
                + ChatColor.GRAY + "+ 0 agility;new"
                + ChatColor.GRAY + "+ 2 armour;new"
                + ChatColor.GRAY + "- 1 weight");

        diamond.setDescription(ChatColor.AQUA + "Perfect;new"
                + ChatColor.GRAY + "You probably found this in battle.;new ;new"
                + ChatColor.GRAY + "- 1 agility;new"
                + ChatColor.GRAY + "+ 3 armour;new"
                + ChatColor.GRAY + "- 2 weight");

        gold.setDescription(ChatColor.YELLOW + "Masterpiece;new"
                + ChatColor.GRAY + "You're of great importance, yeah?;new ;new"
                + ChatColor.GRAY + "- 2 agility;new"
                + ChatColor.GRAY + "+ 4 armour;new"
                + ChatColor.GRAY + "- 3 weight");

        leather.updateMeta();
        iron.updateMeta();
        diamond.updateMeta();
        gold.updateMeta();

        holder.getSiege().getServer().getScheduler().scheduleSyncDelayedTask(holder.getSiege(), () -> {
            shiftItems(inventory, 1, options);
            shiftItems(inventory, -1, material);
            shiftItems(inventory, -1, armour);

            changeItemLater(inventory, 3, leather, 2);
            changeItemLater(inventory, 4, iron, 4);
            changeItemLater(inventory, 5, diamond, 6);
            changeItemLater(inventory, 6, gold, 8);

            MenuGui.fillInventory(inventory);
        }, 5);
    }

    @EventHandler
    public void inventoryClick(InventoryClickEvent ev) {
        Player player = (Player) ev.getWhoClicked();
        Fighter fighter = holder.getFighter(player);
        KitCreator creator = fighter.getCreator();

        Inventory inventory = ev.getClickedInventory();
        InventoryView view = ev.getView();
        ItemStack clicked = ev.getCurrentItem();
        String itemName;

        /*
         * NULL CHECKS
         */
        if (creator == null
                || inventory == null
                || !view.getTitle().equalsIgnoreCase("creator"))
            return;

        if (clicked == null) {
            ev.setCancelled(true);
            return;
        }

        if (ev.getClick() != ClickType.LEFT) {
            ev.setCancelled(true);
            return;
        }

        itemName = ChatColor.stripColor(clicked.getItemMeta().getDisplayName());

        /*
         * ITEM CHECKS
         */
        float pitch = new Random().nextFloat();
        if (itemName.equalsIgnoreCase("back")) {
            player.openInventory(MenuGui.getKitInventory(fighter));
        }
        else if (itemName.equalsIgnoreCase("Previous")) {
            player.openInventory(MenuGui.getKitCreatorInventory(creator.toKit()));
        }
        else if (itemName.equalsIgnoreCase("armour")) {
            animateArmour(inventory);
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
