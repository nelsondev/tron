package com.nelsontron.siege.listeners.menu;

import com.nelsontron.siege.data.KitCreator;
import com.nelsontron.siege.data.MenuGui;
import com.nelsontron.siege.entity.Fighter;
import com.nelsontron.siege.game.Holder;
import com.nelsontron.siege.kits.Item;
import com.nelsontron.siege.kits.Kit;
import com.nelsontron.siege.kits.SiegeItem;
import com.nelsontron.siege.utils.BukkitUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.awt.*;
import java.awt.event.KeyEvent;

public class ListMenuListener implements Listener {

    Holder holder;

    public ListMenuListener(Holder holder) {
        this.holder = holder;
    }

    /*
     * LOAD
     */
    @EventHandler
    public void inventoryOpen(InventoryOpenEvent ev) {
        Player player = (Player) ev.getPlayer();
        Fighter fighter = holder.getFighter(player);
        Kit[] kits = fighter.getKits();
        InventoryView view = ev.getView();

        if (!view.getTitle().equalsIgnoreCase("list"))
            return;

        for (int i = 0; i < kits.length; i++) {
            Kit kit = kits[i];
            if (kit == null) continue;

            kit.getIcon().setName(ChatColor.UNDERLINE + "Kit");
            if (fighter.getKit() != null)
                if (kit == fighter.getKit())
                    kit.getIcon().setName(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "Selected");
            kit.getIcon().updateMeta();

            ev.getInventory().setItem(i+2, kit.getIcon().getItem());
        }
    }

    /*
     * EDIT
     */
    private void editorButton(Player player, Inventory inventory, ItemStack clicked, int slot) {
        if (clicked.getType() == Material.WRITABLE_BOOK) {
            Item item = new SiegeItem();
            item.setItem(new ItemStack(Material.GREEN_WOOL));
            item.setName(ChatColor.GREEN + "Editor");
            item.setDescription(ChatColor.GRAY + "Switch off editor mode;new ;new" +
                    ChatColor.GRAY + "Left-click to edit;new" +
                    ChatColor.GRAY + "Middle-click to remove;new" +
                    ChatColor.GRAY + "Left-click to change icon.");
            item.updateMeta();
            inventory.setItem(slot, item.getItem());

            // this cancels itself
            holder.getSiege().tasks.put("editor-mode-" + player.getName(), holder.getSiege().getServer().getScheduler().scheduleSyncRepeatingTask(holder.getSiege(), ()-> {

                ItemStack tempItem = inventory.getItem(slot);

                if (tempItem == null) {
                    holder.getSiege().cancelTask("editor-mode-" + player.getName());
                    return;
                }

                if (tempItem.getType() == Material.WRITABLE_BOOK) {
                    holder.getSiege().cancelTask("editor-mode-" + player.getName());
                    return;
                }

                if (tempItem.getType() == Material.GREEN_WOOL)
                    tempItem.setType(Material.RED_WOOL);
                else
                    tempItem.setType(Material.GREEN_WOOL);

            }, 15, 15));
        }
        else {
            Item item = new SiegeItem();
            item.setItem(new ItemStack(Material.WRITABLE_BOOK));
            item.setName(ChatColor.DARK_AQUA + "Editor");
            item.setDescription(ChatColor.GRAY + "Switch to edit mode");
            item.updateMeta();
            inventory.setItem(slot, item.getItem());
        }
    }
    private void emptyButton(Inventory inventory, ItemStack clicked, int slot) {
        final ItemStack oldItem = clicked;

        Item areYouSure = new SiegeItem();
        areYouSure.setItem(new ItemStack(Material.GREEN_WOOL));
        areYouSure.setName("New");
        areYouSure.setDescription(ChatColor.GRAY + "Are you sure?;new" +
                ChatColor.DARK_RED + "Click to confirm.;new");
        areYouSure.updateMeta();
        inventory.setItem(slot, areYouSure.getItem());

        holder.getSiege().getServer().getScheduler().scheduleSyncDelayedTask(holder.getSiege(), ()-> {
            inventory.setItem(slot, oldItem);
        }, 60);
    }
    private void newButton(Player player, Fighter fighter, Inventory inventory, int slot, int adjustedSlot) {
        KitCreator creator = new KitCreator(fighter).fromKit(fighter.getDefaultKit());
        ItemStack editor = inventory.getItem(8);

        if (editor == null) return;

        creator.setSlot(adjustedSlot);
        inventory.setItem(slot, creator.getIcon().getItem());
        fighter.setCreator(creator);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_SMALL_FALL, 0.5f, 1.0f);
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,  0.25f, 1.0f);

        editorButton(player, inventory, editor, 8);

        holder.getSiege().tasks.put("newkit-" + player.getName(), holder.getSiege().getServer().getScheduler().scheduleSyncDelayedTask(holder.getSiege(), () -> {
            fighter.getKits()[fighter.getCreator().getSlot()] = fighter.getCreator().toKit();
            player.openInventory(MenuGui.getKitCreatorInventory(fighter.getCreator().toKit()));
        }, 40));
    }
    private void kitButton(Player player, Fighter fighter, Inventory inventory, int adjustedSlot, ClickType click) {
        Kit kit;
        ItemStack editor = inventory.getItem(8);
        ItemMeta meta;
        KitCreator creator;

        if (editor == null) return;

        meta = editor.getItemMeta();

        if (meta.getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "Editor")) {

            holder.getSiege().cancelTask("newkit-" + player.getName());

            if (click == ClickType.LEFT) {
                kit = fighter.getKits()[adjustedSlot];

                if (kit == null) {
                    kit = fighter.getDefaultKit();
                    fighter.getKits()[adjustedSlot] = kit;
                }

                creator = new KitCreator(fighter).fromKit(kit);
                fighter.setCreator(creator);

                player.openInventory(MenuGui.getKitCreatorInventory(kit));
            }
            else if (click == ClickType.MIDDLE) {
                if (fighter.getKit() == fighter.getKits()[adjustedSlot])
                    fighter.setKit(-1);
                fighter.getKits()[adjustedSlot] = null;
                Inventory editorButton = MenuGui.getKitInventory(fighter);
                player.openInventory(editorButton);
                editorButton(player, player.getOpenInventory().getTopInventory(), player.getOpenInventory().getTopInventory().getItem(8), 8);
            }
            else if (click == ClickType.RIGHT) {
                kit = fighter.getKits()[adjustedSlot];

                if (kit == null) {
                    kit = fighter.getDefaultKit();
                    fighter.getKits()[adjustedSlot] = kit;
                }

                kit.getIcon().setItem(new ItemStack(BukkitUtils.getRandomBlock()));
                player.openInventory(MenuGui.getKitInventory(fighter));
                editorButton(player, player.getOpenInventory().getTopInventory(), player.getOpenInventory().getTopInventory().getItem(8), 8);
            }
        }
        // player wants to select a kit
        else {
            kit = fighter.getKits()[adjustedSlot];

            if (kit == null) {
                kit = fighter.getDefaultKit();
                fighter.getKits()[adjustedSlot] = kit;
            }

            fighter.setKit(adjustedSlot);
            player.openInventory(MenuGui.getKitInventory(fighter));
        }
    }

    @EventHandler
    public void inventoryClick(InventoryClickEvent ev) {
        Player player = (Player) ev.getWhoClicked();
        Fighter fighter = holder.getFighter(player);

        Inventory inventory = ev.getClickedInventory();
        InventoryView view = ev.getView();
        ItemStack clicked = ev.getCurrentItem();
        int slot = ev.getSlot();
        int adjustedSlot = slot - 2;
        String itemName;

        /*
         * NULL CHECKS
         */
        if (inventory == null ||
                !view.getTitle().equalsIgnoreCase("list"))
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
            if (ev.getClick() != ClickType.LEFT) {
                ev.setCancelled(true);
                return;
            }
            player.openInventory(MenuGui.getMenuInventory(fighter));
        }

        if (itemName.equalsIgnoreCase("editor")) {
            if (ev.getClick() != ClickType.LEFT) {
                ev.setCancelled(true);
                return;
            }
            editorButton(player, inventory, clicked, slot);
        }

        if (itemName.equalsIgnoreCase("empty")) {
            if (ev.getClick() != ClickType.LEFT) {
                ev.setCancelled(true);
                return;
            }
            emptyButton(inventory, clicked, slot);
        }

        if (itemName.equalsIgnoreCase("new")) {
            if (ev.getClick() != ClickType.LEFT) {
                ev.setCancelled(true);
                return;
            }
            newButton(player, fighter, inventory, slot, adjustedSlot);
        }

        if (itemName.equalsIgnoreCase("kit") || itemName.equalsIgnoreCase("selected")) {
            kitButton(player, fighter, inventory, adjustedSlot, ev.getClick());
        }

        ev.setCancelled(true);
    }
}
