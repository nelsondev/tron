package com.nelsontron.siege.data;

import com.nelsontron.siege.entity.Fighter;
import com.nelsontron.siege.game.Holder;
import com.nelsontron.siege.kits.Item;
import com.nelsontron.siege.kits.Kit;
import com.nelsontron.siege.kits.SiegeItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nullable;

public class MenuGui implements Listener, CommandExecutor {
    private Holder holder;

    public MenuGui(Holder holder) {
        this.holder = holder;
    }

    public static void fillInventory(Inventory inventory) {
        while (inventory.firstEmpty() != -1) {
            Item item = new SiegeItem();
            item.setName("*");
            item.setItem(new ItemStack(Material.RED_STAINED_GLASS_PANE));
            item.updateMeta();
            inventory.setItem(inventory.firstEmpty(), item.getItem());
        }
    }

    public static void fillInventory(Inventory inventory, String name) {
        while (inventory.firstEmpty() != -1) {
            Item item = new SiegeItem();
            item.setItem(new ItemStack(Material.RED_STAINED_GLASS_PANE));
            item.setName(name);
            item.updateMeta();
            inventory.setItem(inventory.firstEmpty(), item.getItem());
        }
    }

    public static void fillInventory(Inventory inventory, String name, String description) {
        while (inventory.firstEmpty() != -1) {
            Item item = new SiegeItem();
            item.setItem(new ItemStack(Material.RED_STAINED_GLASS_PANE));
            item.setName(name);
            item.setDescription(description);
            item.updateMeta();
            inventory.setItem(inventory.firstEmpty(), item.getItem());
        }
    }

    public static Inventory getMenuInventory(Fighter fighter) {
        Inventory inventory = Bukkit.createInventory(null, 9, "Menu");
        Item creator;
        Item team;
        Item game;
        Item close;

        // instantiate
        creator = new SiegeItem();
        team = new SiegeItem();
        game = new SiegeItem();
        close = new SiegeItem();

        // set items
        creator.setItem(new ItemStack(Material.DIAMOND));
        team.setItem(new ItemStack(Material.WHITE_WOOL));
        game.setItem(new ItemStack(Material.EMERALD));
        close.setItem(new ItemStack(Material.BARRIER));

        // set names
        creator.setName("List");
        /*
         * Set team item
         */
        if (fighter.getTeam() != null) {
            if (fighter.getTeam().equalsIgnoreCase("blue")) {
                team.setItem(new ItemStack(Material.BLUE_WOOL));
                team.setName(ChatColor.BLUE + "Blue");
            }
            else {
                team.setItem(new ItemStack(Material.RED_WOOL));
                team.setName(ChatColor.RED + "Red");
            }
        }
        else
            team.setName("Team");
        /*
         * End Team item
         */
        game.setName("Siege");
        close.setName(ChatColor.RED + "Close");

        // set description
        creator.setDescription(ChatColor.AQUA + "List your kits");
        team.setDescription(ChatColor.GRAY + "Current team");
        game.setDescription(ChatColor.GRAY + "Current game");
        close.setDescription(ChatColor.GRAY + "Close this window");

        // update meta
        creator.updateMeta();
        team.updateMeta();
        game.updateMeta();
        close.updateMeta();

        // set items
        inventory.setItem(0, creator.getItem());
        inventory.setItem(1, team.getItem());
        inventory.setItem(7, game.getItem());
        inventory.setItem(8, close.getItem());

        // fill empty space
        fillInventory(inventory);

        return inventory;
    }

    public static Inventory getKitInventory(Fighter fighter) {

        Inventory inventory = Bukkit.createInventory(null, 9, "List");

        Item back = new SiegeItem();
        Item kit0 = new SiegeItem();
        Item kit1 = new SiegeItem();
        Item kit2 = new SiegeItem();
        Item kit3 = new SiegeItem();
        Item kit4 = new SiegeItem();
        Item editor = new SiegeItem();

        back.setItem(new ItemStack(Material.BARRIER));
        kit0.setItem(new ItemStack(Material.WHITE_STAINED_GLASS));
        kit1.setItem(new ItemStack(Material.WHITE_STAINED_GLASS));
        kit2.setItem(new ItemStack(Material.WHITE_STAINED_GLASS));
        kit3.setItem(new ItemStack(Material.WHITE_STAINED_GLASS));
        kit4.setItem(new ItemStack(Material.WHITE_STAINED_GLASS));
        editor.setItem(new ItemStack(Material.WRITABLE_BOOK));

        back.setName(ChatColor.RED + "Back");
        kit0.setName("Empty");
        kit1.setName("Empty");
        kit2.setName("Empty");
        kit3.setName("Empty");
        kit4.setName("Empty");
        editor.setName(ChatColor.DARK_AQUA + "Editor");

        back.setDescription(ChatColor.GRAY + "Previous menu");
        kit0.setDescription(ChatColor.GRAY + "Create a new kit");
        kit1.setDescription(ChatColor.GRAY + "Create a new kit");
        kit2.setDescription(ChatColor.GRAY + "Create a new kit");
        kit3.setDescription(ChatColor.GRAY + "Create a new kit");
        kit4.setDescription(ChatColor.GRAY + "Create a new kit");
        editor.setDescription(ChatColor.GRAY + "Switch to edit mode");

        back.updateMeta();
        kit0.updateMeta();
        kit1.updateMeta();
        kit2.updateMeta();
        kit3.updateMeta();
        kit4.updateMeta();
        editor.updateMeta();

        boolean hasKit0 = fighter.getKits()[0] != null;
        boolean hasKit1 = fighter.getKits()[1] != null;
        boolean hasKit2 = fighter.getKits()[2] != null;
        boolean hasKit3 = fighter.getKits()[3] != null;
        boolean hasKit4 = fighter.getKits()[4] != null;

        if (hasKit0)
            kit0 = fighter.getKits()[0].getIcon();
        if (hasKit1)
            kit1 = fighter.getKits()[1].getIcon();
        if (hasKit2)
            kit2 = fighter.getKits()[2].getIcon();
        if (hasKit3)
            kit3 = fighter.getKits()[3].getIcon();
        if (hasKit4)
            kit4 = fighter.getKits()[4].getIcon();

        inventory.setItem(0, back.getItem());
        inventory.setItem(2, kit0.getItem());
        inventory.setItem(3, kit1.getItem());
        inventory.setItem(4, kit2.getItem());
        inventory.setItem(5, kit3.getItem());
        inventory.setItem(6, kit4.getItem());
        inventory.setItem(8, editor.getItem());

        fillInventory(inventory);

        return inventory;
    }

    public static Inventory getKitCreatorInventory(@Nullable Kit kit) {
        Inventory inventory = Bukkit.createInventory(null, 18, "Creator");

        Item points;
        Item enchants;
        Item effects;
        Item perks;
        Item options;
        Item close;
        Item boots;
        Item leggings;
        Item chestplate;
        Item helmet;

        // instantiate
        points = new SiegeItem();
        enchants = new SiegeItem();
        effects = new SiegeItem();
        perks = new SiegeItem();
        options = new SiegeItem();
        close = new SiegeItem();
        boots = new SiegeItem();
        leggings = new SiegeItem();
        chestplate = new SiegeItem();
        helmet = new SiegeItem();

        // default creator inv
        if (kit == null) {

            // set items
            points.setItem(new ItemStack(Material.NETHER_STAR));
            enchants.setItem(new ItemStack(Material.LAPIS_LAZULI));
            effects.setItem(new ItemStack(Material.NETHER_WART));
            perks.setItem(new ItemStack(Material.GUNPOWDER));
            options.setItem(new ItemStack(Material.ENDER_PEARL));
            close.setItem(new ItemStack(Material.BARRIER));
            boots.setItem(new ItemStack(Material.LEATHER_BOOTS));
            leggings.setItem(new ItemStack(Material.LEATHER_LEGGINGS));
            chestplate.setItem(new ItemStack(Material.LEATHER_CHESTPLATE));
            helmet.setItem(new ItemStack(Material.LEATHER_HELMET));

            // set names
            points.setName(ChatColor.UNDERLINE + "Points");
            enchants.setName(ChatColor.UNDERLINE + "Enchantments");
            effects.setName(ChatColor.UNDERLINE + "Effects");
            perks.setName(ChatColor.UNDERLINE + "Perks");
            options.setName(ChatColor.UNDERLINE + "Options");
            close.setName(ChatColor.RED + "Back");
            boots.setName(ChatColor.UNDERLINE + "Armour");
            leggings.setName(ChatColor.UNDERLINE + "Armour");
            chestplate.setName(ChatColor.UNDERLINE + "Armour");
            helmet.setName(ChatColor.UNDERLINE + "Armour");

            // set descriptions
            points.setDescription(ChatColor.AQUA + "Weight points;new;new" +
                    ChatColor.DARK_GRAY + "maximum weight the kit can hold;new" +
                    ChatColor.DARK_GRAY + "any addition of items, enchantments,;new" +
                    ChatColor.DARK_GRAY + "effects, perks, and armour affect;new" +
                    ChatColor.DARK_GRAY + "this number. Having more weight;new" +
                    ChatColor.DARK_GRAY + "to a kit may slow you down a little.");
            enchants.setDescription(ChatColor.GRAY + "Customize enchants");
            effects.setDescription(ChatColor.GRAY + "Customize effects");
            perks.setDescription(ChatColor.GRAY + "Customize perks");
            options.setDescription(ChatColor.AQUA + "Kit options");
            close.setDescription(ChatColor.GRAY + "Previous menu");
            boots.setDescription(ChatColor.GRAY + "Customize armour");
            leggings.setDescription(ChatColor.GRAY + "Customize armour");
            chestplate.setDescription(ChatColor.GRAY + "Customize armour");
            helmet.setDescription(ChatColor.GRAY + "Customize armour");

            // update meta
            points.updateMeta();
            enchants.updateMeta();
            effects.updateMeta();
            perks.updateMeta();
            options.updateMeta();
            close.updateMeta();
            boots.updateMeta();
            leggings.updateMeta();
            chestplate.updateMeta();
            helmet.updateMeta();

            inventory.setItem(0, points.getItem());
            inventory.setItem(5, enchants.getItem());
            inventory.setItem(6, effects.getItem());
            inventory.setItem(7, perks.getItem());
            inventory.setItem(8, options.getItem());
            inventory.setItem(9, close.getItem());
            inventory.setItem(14, boots.getItem());
            inventory.setItem(15, leggings.getItem());
            inventory.setItem(16, chestplate.getItem());
            inventory.setItem(17, helmet.getItem());

            fillInventory(inventory, ChatColor.UNDERLINE + "Item", ChatColor.GRAY + "Customize equipment");

            return inventory;
        }
        // GET PLAYER SPECIFIC KIT
        else {
            points.setItem(new ItemStack(Material.NETHER_STAR, kit.getPoints()));
            enchants.setItem(new ItemStack(Material.LAPIS_LAZULI));
            effects.setItem(new ItemStack(Material.NETHER_WART));
            perks.setItem(new ItemStack(Material.GUNPOWDER));
            options.setItem(new ItemStack(Material.ENDER_PEARL));
            close.setItem(new ItemStack(Material.BARRIER));

            if (kit.getBoots() != null)
                boots = kit.getBoots().clone();
            else
                boots.setItem(new ItemStack(Material.LEATHER_BOOTS));
            if (kit.getLeggings() != null)
                leggings = kit.getLeggings().clone();
            else
                leggings.setItem(new ItemStack(Material.LEATHER_LEGGINGS));
            if (kit.getChestplate() != null)
                chestplate = kit.getChestplate().clone();
            else
                chestplate.setItem(new ItemStack(Material.LEATHER_CHESTPLATE));
            if (kit.getHelmet() != null)
                helmet = kit.getHelmet().clone();
            else
                helmet.setItem(new ItemStack(Material.LEATHER_HELMET));

            // set names
            points.setName(ChatColor.UNDERLINE + "Points");
            enchants.setName(ChatColor.UNDERLINE + "Enchantments");
            effects.setName(ChatColor.UNDERLINE + "Effects");
            perks.setName(ChatColor.UNDERLINE + "Perks");
            options.setName(ChatColor.UNDERLINE + "Options");
            close.setName(ChatColor.RED + "Back");
            boots.setName(ChatColor.UNDERLINE + "Armour");
            leggings.setName(ChatColor.UNDERLINE + "Armour");
            chestplate.setName(ChatColor.UNDERLINE + "Armour");
            helmet.setName(ChatColor.UNDERLINE + "Armour");

            // set descriptions
            points.setDescription(ChatColor.AQUA + "Weight points;new" +
                    ChatColor.GRAY + "- " + kit.getPoints() + " weight left;new" +
                    ChatColor.DARK_GRAY + "maximum weight the kit can hold;new" +
                    ChatColor.DARK_GRAY + "any addition of items, enchantments,;new" +
                    ChatColor.DARK_GRAY + "effects, perks, and armour affect;new" +
                    ChatColor.DARK_GRAY + "this number. Having more weight;new" +
                    ChatColor.DARK_GRAY + "to a kit may slow you down a little.");
            enchants.setDescription(ChatColor.GRAY + "Customize enchants");
            effects.setDescription(ChatColor.GRAY + "Customize effects");
            perks.setDescription(ChatColor.GRAY + "Customize perks");
            options.setDescription(ChatColor.AQUA + "Kit options");
            close.setDescription(ChatColor.GRAY + "Previous menu");
            boots.setDescription(ChatColor.GRAY + "Customize armour");
            leggings.setDescription(ChatColor.GRAY + "Customize armour");
            chestplate.setDescription(ChatColor.GRAY + "Customize armour");
            helmet.setDescription(ChatColor.GRAY + "Customize armour");

            // update meta
            points.updateMeta();
            enchants.updateMeta();
            effects.updateMeta();
            perks.updateMeta();
            options.updateMeta();
            close.updateMeta();
            boots.updateMeta();
            leggings.updateMeta();
            chestplate.updateMeta();
            helmet.updateMeta();

            for (Item item : kit.getInventory()) {
                if (item == null) continue;
                item.updateMeta();
            }

            // set items
            inventory.setItem(0, points.getItem());
            inventory.setItem(5, enchants.getItem());
            inventory.setItem(6, effects.getItem());
            inventory.setItem(7, perks.getItem());
            inventory.setItem(8, options.getItem());
            inventory.setItem(9, close.getItem());
            inventory.setItem(14, boots.getItem());
            inventory.setItem(15, leggings.getItem());
            inventory.setItem(16, chestplate.getItem());
            inventory.setItem(17, helmet.getItem());

            if (kit.getInventory()[0] != null)
                inventory.setItem(1, kit.getInventory()[0].getItem());
            if (kit.getInventory()[1] != null)
                inventory.setItem(2, kit.getInventory()[1].getItem());
            if (kit.getInventory()[2] != null)
                inventory.setItem(3, kit.getInventory()[2].getItem());
            if (kit.getInventory()[3] != null)
                inventory.setItem(4, kit.getInventory()[3].getItem());
            if (kit.getInventory()[4] != null)
                inventory.setItem(10, kit.getInventory()[4].getItem());
            if (kit.getInventory()[5] != null)
                inventory.setItem(11, kit.getInventory()[5].getItem());
            if (kit.getInventory()[6] != null)
                inventory.setItem(12, kit.getInventory()[6].getItem());
            if (kit.getInventory()[7] != null)
                inventory.setItem(13, kit.getInventory()[7].getItem());

            fillInventory(inventory, ChatColor.UNDERLINE + "Item", ChatColor.GRAY + "Customize equipment");

            return inventory;
        }
    }

    public static void invalidClick(Holder holder, InventoryClickEvent ev, String description) {
        Inventory inventory = ev.getClickedInventory();
        final ItemStack wrong = ev.getCurrentItem().clone();
        final int slot = ev.getSlot();

        Player player = (Player) ev.getWhoClicked();

        if (wrong.getType() == Material.BARRIER) {
            ev.setCancelled(true);
            return;
        }

        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1.0f, 1.0f);
        Bukkit.getScheduler().scheduleSyncDelayedTask(holder.getSiege(), ()-> {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 0.8f, 0.0f);
        }, 1);

        Item item = new SiegeItem();
        item.setItem(new ItemStack(Material.BARRIER));
        item.setName(ChatColor.RED + "No");
        item.setDescription(ChatColor.GRAY + description);
        item.updateMeta();
        ev.setCurrentItem(item.getItem());
        Bukkit.getScheduler().scheduleSyncDelayedTask(holder.getSiege(), ()-> {
            if (inventory != null)
                inventory.setItem(slot, wrong);
        }, 10);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player;
        Fighter fighter;

        String result = null;

        try {
            player = (Player) sender;
            fighter = holder.getFighter(player);

            player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0f, 2.0f);
            player.openInventory(getMenuInventory(fighter));
            return true;
        }
        catch (ClassCastException ex) {

        }
        finally {
            if (result != null)
                sender.sendMessage(result);
        }

        return false;
    }
}
