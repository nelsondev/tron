package com.nelsontron.snackpack;

import com.nelsontron.mjarket.Mjarket;
import com.nelsontron.mjarket.entity.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class SnackPack extends JavaPlugin {

    SnackHolder holder;

    @Override
    public void onEnable() {
        // Plugin startup logic

        holder = new SnackHolder(this);
        holder.registerAllSnackers();

        getServer().getPluginManager().registerEvents(holder, this);
        Utils.registerCommands(new Commands(), this, "pack", "p", "b");
        Utils.registerCommands(new CraftCommand(this), this, "craft", "c", "cra");
        Utils.registerCommands(new EnderCommand(this), this, "ender", "end");
        Utils.registerCommands(new CutterCommand(this), this, "cutter", "cut");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        holder.unregisterAllSnackers();
    }

    class SnackHolder implements Listener {

        SnackPack snackPack;
        List<Snack> snackers;
        List<String> categories;

        public SnackHolder(SnackPack snackPack) {
            this.snackPack = snackPack;
            this.snackers = new ArrayList<>();

            categories = new ArrayList<>();
            categories.add("enchant-book-anvil-lapis");
            categories.add("diamond");
            categories.add("ingot");
            categories.add("coal-charcoal");
            categories.add("pickaxe");
            categories.add("_axe");
            categories.add("spade");
            categories.add("sword");
            categories.add("helm");
            categories.add("chestplate");
            categories.add("leggin");
            categories.add("boots");
            categories.add("hoe-stick-rod");
            categories.add("wheat-sugar-help-bamboo-carrot-potato-pumpk");
            categories.add("seed");
            categories.add("apple-stew-bread-raw-cookie-cake-steak-cooked-pie-tropical");
            categories.add("log-wood");
            categories.add("plank");
            categories.add("brick");
            categories.add("stone-andes-dior-dirt-cobble-granite-sand");
            categories.add("table-chest-furnace-barrel-cutter-fire-lantern");
            categories.add("redstone-ator-repeater-piston-button-pressu-dispenser-dropper-gate-door");
            categories.add("misc");
        }

        List<Snack> getSnackers() {
            return snackers;
        }
        List<String> getCategories() { return categories; }

        Snack getSnacker(String name) {
            Snack snack = null;
            for (Snack s : snackers) {
                if (s.getName().equalsIgnoreCase(name))
                    snack = s;
            }
            return snack;
        }

        Snack getSnacker(Player player) {
            Snack snack = null;
            for (Snack s : snackers) {
                if (s.getUuid() == player.getUniqueId())
                    snack = s;
            }
            return snack;
        }

        void registerSnacker(Snack snacker) {
            snacker.load();
            snackers.add(snacker);
        }

        void unregisterSnacker(Snack snacker) {
            snacker.save();
            snackers.remove(snacker);
        }

        void registerAllSnackers() {
            for (Player online : Bukkit.getOnlinePlayers()) {
                Snack snacker = new Snack(snackPack, online);
                snacker.load();
                snackers.add(snacker);
            }
        }

        void unregisterAllSnackers() {
            for (Player online : Bukkit.getOnlinePlayers()) {
                Snack snacker = getSnacker(online);
                snacker.save();
                snackers.remove(snacker);
            }
        }

        /**
         * LISTENER
         */

        @EventHandler
        public void playerJoin(PlayerJoinEvent ev) {
            Snack snacker = new Snack(snackPack, ev.getPlayer());
            holder.registerSnacker(snacker);
        }

        @EventHandler
        public void playerQuit(PlayerQuitEvent ev) {
            Snack snacker = holder.getSnacker(ev.getPlayer());
            holder.unregisterSnacker(snacker);
        }

        @EventHandler
        public void invClose(InventoryCloseEvent ev) {
            Snack snacker;
            String name;
            Pack pack;
            if (ev.getView().getTitle().contains("Pack:")) {
                snacker = getSnacker((Player) ev.getPlayer());
                name = ev.getView().getTitle().split(" ")[1];
                pack = snacker.getPack(name.toLowerCase());
                pack.fromInventory(ev.getInventory());
            }
        }
    }

    class Snack {
        UUID uuid;
        String name;
        List<Pack> packs;
        Data data;
        String defaultPack;
        List<String> categories;

        public Snack(SnackPack snackpack, Player player) {
            this.uuid = player.getUniqueId();
            this.name = player.getName();
            this.packs = new ArrayList<>();
            this.data = new Data(snackpack, player);
            this.defaultPack = null;

            categories = new ArrayList<>();
            categories.add("enchant-book-anvil-lapis");
            categories.add("pickaxe");
            categories.add("_axe");
            categories.add("spade");
            categories.add("sword");
            categories.add("helm");
            categories.add("chestplate");
            categories.add("leggin");
            categories.add("boots");
            categories.add("diamond");
            categories.add("ingot");
            categories.add("coal-charcoal");
            categories.add("hoe-stick-rod");
            categories.add("wheat-sugar-help-bamboo-carrot-potato-pumpk");
            categories.add("seed");
            categories.add("apple-stew-bread-raw-cookie-cake-steak-cooked-pie-tropical");
            categories.add("log-wood");
            categories.add("plank");
            categories.add("brick-clay-terra");
            categories.add("stone-andes-dior-dirt-cobble-granite-sand");
            categories.add("table-chest-furnace-barrel-cutter-fire-lantern");
            categories.add("redstone-ator-repeater-piston-button-pressu-dispenser-dropper-gate-door");
            categories.add("misc");
        }

        // getters
        UUID getUuid() {
            return uuid;
        }
        Player getPlayer() {
            return Bukkit.getPlayer(uuid);
        }
        String getName() {
            return name;
        }
        List<Pack> getPacks() { return packs; }
        Pack getPack(String name) {
            Pack pack = null;
            for (Pack p : packs) {
                if (p.getName().equalsIgnoreCase(name))
                    pack = p;
            }
            return pack;
        }
        String getDefaultPack() { return defaultPack; }
        Pack createPack(String name, int size, String type) {
            Pack pack = new Pack(name, size, type);
            packs.add(pack);
            return pack;
        }

        // setters
        void setDefaultPack(String name) { defaultPack = name; }

        // methods
        boolean isMaxPacks() {
            if (packs.size() >= 4)
                return true;
            else
                return false;
        }
        void clean() {
            for (String key : data.getData().getKeys(false)) {
                data.getData().set(key, null);
            }
            data.save();
        }

        void save() {
            clean();
            data.getData().set("default-pack", defaultPack);
            for (Pack pack : packs) {
                pack.clean();
                data.getData().set(pack.getName() + ".name", pack.getName());
                data.getData().set(pack.getName() + ".size", pack.getSize());
                data.getData().set(pack.getName() + ".type", pack.getType());
                data.getData().set(pack.getName() + ".items", pack.getItems());
                data.getData().set(pack.getName() + ".upgrades", pack.getUpgrades());
            }
            data.save();
        }
        void load() {
            defaultPack = data.getData().getString("default-pack");
            for (String key : data.getData().getKeys(false)) {
                String name = data.getData().getString(key + ".name");
                int size = data.getData().getInt(key + ".size");
                String type = data.getData().getString(key + ".type");
                Pack pack = new Pack(name, size, type);
                pack.setUpgrades(data.getData().getStringList(key + ".upgrades"));

                if (key.contains(".") || key.equalsIgnoreCase("default-pack"))
                    continue;

                for (Object ob : data.getData().getList(key + ".items")) {
                    ItemStack item = null;
                    if (ob instanceof ItemStack)
                        item = (ItemStack) ob;
                    if (item == null)
                        continue;
                    pack.getItems().add(item);
                }
                packs.add(pack);
            }
        }
    }

    class Pack {
        String name;
        List<ItemStack> items;
        int size;
        String type;
        List<String> upgrades;

        public Pack(String name, int size, String type) {
            this.name = name;
            this.items = new ArrayList<>();
            this.size = size;
            this.type = type;
            this.upgrades = new ArrayList<>();
        }

        String getName() { return name; }
        List<ItemStack> getItems() {
            return items;
        }
        int getSize() { return size; }
        String getType() { return type; }
        List<String> getUpgrades() { return upgrades; }

        void setItems(List<ItemStack> items) { this.items = items; }
        void setName(String name) { this.name = name; }
        void setSize(int size) { this.size = size; }
        void setUpgrades(List<String> list) { this.upgrades = list; }
        void incrementSize() { this.size += 9; }
        boolean isMaxSize() {
            if (size >= 54)
                return true;
            else
                return false;
        }

        void sort() {
            items = Utils.sortItemStackList(items, holder.getCategories(),"-");
        }

        boolean isOre(ItemStack item) {
            boolean isIron = item.getType() == Material.IRON_ORE;
            boolean isGold = item.getType() == Material.GOLD_ORE;

            return isIron || isGold;
        }

        ItemStack getSmeltedItemStack(ItemStack ore) {
            boolean isIron = ore.getType() == Material.IRON_ORE;
            boolean isGold = ore.getType() == Material.GOLD_ORE;

            ItemStack result;

            if (isIron)
                result = new ItemStack(Material.IRON_INGOT, ore.getAmount());
            else if (isGold)
                result = new ItemStack(Material.GOLD_INGOT, ore.getAmount());
            else
                result = null;

            return result;
        }

        void smelt() {
            for (ItemStack item : items) {

                if (item == null)
                    continue;

                if (isOre(item)) {
                    ItemStack smelted = getSmeltedItemStack(item);
                    if (smelted == null)
                        continue;
                    item.setType(smelted.getType());
                }
            }
        }

        Inventory toInventory(Player player) {
            clean();
            Inventory inv;
            if (type.equalsIgnoreCase("ender")) {
                inv = player.getEnderChest();
            }
            else {
                inv = Bukkit.createInventory(player, size, "Pack: " + name);

                if (upgrades.contains("autosmelt"))
                    smelt();

                if (upgrades.contains("autosort"))
                    sort();

                for (ItemStack item : items) {
                    if (item != null) {
                        inv.addItem(item);
                    }
                }
            }

            return inv;
        }
        void fromInventory(Inventory inv) {
            items.clear();
            for (ItemStack item : inv.getContents()) {
                if (item != null)
                    items.add(item);
            }
        }

        void clean() {
            items.remove(null);
        }
    }

    class Commands implements CommandExecutor {

        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

            Player player;
            Snack snack;
            Consumer consumer;
            Pack pack;

            String subCommand;
            String result = null;

            try {

                player = (Player) sender;
                snack = holder.getSnacker(player);
                consumer = Mjarket.getGovernment().getConsumerData().getConsumer(player.getUniqueId());

                // player wants to use default pack
                if (args.length == 0) {
                    if (snack.getDefaultPack() != null) {
                        pack = snack.getPack(snack.getDefaultPack());

                        if (pack == null) {
                            result = ChatColor.RED + "> Pack '" + snack.getDefaultPack() + "' not found.";
                            return true;
                        }

                        if (pack.getType().equalsIgnoreCase("craft")) {
                            player.openWorkbench(null, true);
                            return true;
                        }

                        if (pack.getType().equalsIgnoreCase("ender")) {
                            player.openInventory(player.getEnderChest());
                            return true;
                        }

                        player.openInventory(pack.toInventory(player));
                    } else {
                        result = ChatColor.RED + "> No default pack set. Do /pack help.";
                        return true;
                    }
                }
                // player wants to use a sub command
                else if (args.length == 2) {
                    subCommand = args[0];

                    if (subCommand.equalsIgnoreCase("create")) {

                        if (snack.isMaxPacks()) {
                            result = ChatColor.RED + "> You have the maximum amount of packs. (4)";
                            return false;
                        }

                        if (args[1].equalsIgnoreCase("ender")) {
                            /**
                             * MJARKET
                             */
                            if (consumer.getDoub() < Mjarket.PACK_ENDER_PRICE) {
                                float remaining = Mjarket.PACK_ENDER_PRICE - consumer.getDoub();
                                result = ChatColor.RED + "> You need " + remaining + " more doubloons.";
                                return true;
                            }

                            if (snack.getPack(args[1]) != null) {
                                result = ChatColor.RED + "> You already have a pack named that.";
                                return true;
                            }

                            pack = snack.createPack(args[1], 9, "ender");
                            player.openInventory(pack.toInventory(player));

                            /**
                             * MJARKET
                             */
                            consumer.setDoub(consumer.getDoub() - Mjarket.PACK_ENDER_PRICE);
                            consumer.setActivity(consumer.getActivity() + 250);

                            result = ChatColor.GRAY + "Created new ender pack.";
                        } else if (args[1].equalsIgnoreCase("craft")) {
                            /**
                             * MJARKET
                             */
                            if (consumer.getDoub() < Mjarket.PACK_CRAFT_PRICE) {
                                float remaining = Mjarket.PACK_CRAFT_PRICE - consumer.getDoub();
                                result = ChatColor.RED + "> You need " + remaining + " more doubloons.";
                                return true;
                            }

                            if (snack.getPack(args[1]) != null) {
                                result = ChatColor.RED + "> You already have a pack named that.";
                                return true;
                            }

                            pack = snack.createPack(args[1], 9, "craft");
                            player.openWorkbench(null, true);

                            /**
                             * MJARKET
                             */
                            consumer.setDoub(consumer.getDoub() - Mjarket.PACK_CRAFT_PRICE);
                            consumer.getServices().add(Mjarket.PACK_CRAFT_PRICE);
                            consumer.setActivity(consumer.getActivity() + 250);

                            result = ChatColor.GRAY + "Created new crafting pack.";
                        }
                        else if (args[1].equalsIgnoreCase("help") || args[1].equalsIgnoreCase("list")) {
                            result = ChatColor.RED + "You cant name a pack that.";
                        }
                        else {

                            /**
                             * MJARKET
                             */
                            if (consumer.getDoub() < Mjarket.PACK_CUSTOM_PRICE) {
                                float remaining = Mjarket.PACK_CUSTOM_PRICE - consumer.getDoub();
                                result = ChatColor.RED + "> Not enough doubloons.";
                                return true;
                            }

                            if (snack.getPack(args[1]) != null) {
                                result = ChatColor.RED + "> You already have a pack named that.";
                                return true;
                            }

                            pack = snack.createPack(args[1], 9, "chest");
                            player.openInventory(pack.toInventory(player));

                            /**
                             * MJARKET
                             */
                            consumer.setDoub(consumer.getDoub() - Mjarket.PACK_CUSTOM_PRICE);
                            consumer.getServices().add(Mjarket.PACK_CUSTOM_PRICE);
                            consumer.setActivity(consumer.getActivity() + 250);

                            result = ChatColor.GRAY + "Created new pack named '" + args[1] + ".'";
                        }
                    }
                    else if (subCommand.equalsIgnoreCase("default")) {
                        pack = snack.getPack(args[1]);
                        if (pack == null) {
                            result = ChatColor.RED + "> Pack '" + args[1] + "' not found.";
                            return true;
                        }

                        snack.setDefaultPack(pack.getName());
                        result = ChatColor.GRAY + "'" + args[1] + "' has been set to default pack.";
                    }
                    else if (subCommand.equalsIgnoreCase("remove")) {
                        pack = snack.getPack(args[1]);
                        if (pack == null) {
                            result = ChatColor.RED + "> Pack '" + args[1] + "' not found.";
                            return true;
                        }

                        if (pack.getItems().size() != 0) {
                            result = ChatColor.RED + "> There's still items in that pack. Please empty it before removing it.";
                            return true;
                        }

                        snack.getPacks().remove(pack);
                        result = ChatColor.GRAY + "Removed new pack named '" + args[1] + ".'";
                    }
                    else {
                        result = ChatColor.RED + "> Sub command not found. Do /pack help.";
                        return true;
                    }
                }
                // player wants to use a sub command
                else if (args.length == 3) {
                    subCommand = args[0];

                    if (subCommand.equalsIgnoreCase("rename")) {
                        pack = snack.getPack(args[1]);
                        if (pack == null) {
                            result = ChatColor.RED + "> Pack '" + args[1] + "' not found.";
                            return true;
                        }

                        pack.setName(args[2]);
                        result = ChatColor.GRAY + "Renamed pack '" + args[1] + "' to '" + pack.getName() + ".'";
                    }
                    else if (subCommand.equalsIgnoreCase("upgrade")) {
                        String upgrade = args[1];
                        pack = snack.getPack(args[2]);
                        if (pack == null) {
                            result = ChatColor.RED + "> Pack '" + args[1] + "' not found.";
                            return true;
                        }

                        if (pack.getType().equalsIgnoreCase("craft")
                                || pack.getType().equalsIgnoreCase("cutter")
                                || pack.getType().equalsIgnoreCase("ender")) {
                            result = ChatColor.RED + "> You cant upgrade that kind of pack.";
                            return true;
                        }

                        if (upgrade.equalsIgnoreCase("size")) {

                            /**
                             * MJARKET
                             */
                            if (consumer.getDoub() < Mjarket.PACK_UPGRADE_SIZE_PRICE) {
                                float remaining = Mjarket.PACK_UPGRADE_SIZE_PRICE-consumer.getDoub();
                                result = ChatColor.RED + "> Not enough doubloons.";
                                return true;
                            }

                            if (pack.isMaxSize()) {
                                result = ChatColor.RED + "> That pack is at max size.";
                                return true;
                            }
                            pack.incrementSize();

                            /**
                             * MJARKET
                             */
                            consumer.setDoub(consumer.getDoub()-Mjarket.PACK_UPGRADE_SIZE_PRICE);
                            consumer.getServices().add(Mjarket.PACK_UPGRADE_SIZE_PRICE);
                            consumer.setActivity(consumer.getActivity() + 250);

                            result = ChatColor.GRAY + "Pack size upgraded.";
                        }
                        else if (upgrade.equalsIgnoreCase("autosort")) {
                            /**
                             * MJARKET
                             */
                            if (consumer.getDoub() < Mjarket.PACK_UPGRADE_AUTO_SORT_PRICE) {
                                float remaining = Mjarket.PACK_UPGRADE_AUTO_SORT_PRICE-consumer.getDoub();
                                result = ChatColor.RED + "> Not enough doubloons.";
                                return true;
                            }

                            if (pack.upgrades.contains("autosort")) {
                                result = ChatColor.RED + "> You already have that upgrade.";
                                return true;
                            }
                            pack.getUpgrades().add("autosort");

                            /**
                             * MJARKET
                             */
                            consumer.setDoub(consumer.getDoub()-Mjarket.PACK_UPGRADE_AUTO_SORT_PRICE);
                            consumer.getServices().add(Mjarket.PACK_UPGRADE_AUTO_SORT_PRICE);
                            consumer.setActivity(consumer.getActivity() + 250);

                            result = ChatColor.GRAY + "Pack upgraded with auto sort.";
                        }
                        else if (upgrade.equalsIgnoreCase("autosmelt")) {
                            /**
                             * MJARKET
                             */
                            if (consumer.getDoub() < Mjarket.PACK_UPGRADE_AUTO_SMELT_PRICE) {
                                float remaining = Mjarket.PACK_UPGRADE_AUTO_SMELT_PRICE-consumer.getDoub();
                                result = ChatColor.RED + "> Not enough doubloons.";
                                return true;
                            }

                            if (pack.upgrades.contains("autosmelt")) {
                                result = ChatColor.RED + "> You already have that upgrade.";
                                return true;
                            }
                            pack.getUpgrades().add("autosmelt");

                            /**
                             * MJARKET
                             */
                            consumer.setDoub(consumer.getDoub()-Mjarket.PACK_UPGRADE_AUTO_SMELT_PRICE);
                            consumer.getServices().add(Mjarket.PACK_UPGRADE_AUTO_SMELT_PRICE);
                            consumer.setActivity(consumer.getActivity() + 250);

                            result = ChatColor.GRAY + "Pack upgraded with auto smelt.";
                        }
                        else {
                            result = ChatColor.RED + "> Upgrade '" + upgrade + "' not found. Upgrades available " +
                                    "include: size, autosort, and autosmelt.";
                        }
                    }
                    else {
                        result = ChatColor.RED + "> Sub command not found. Do /pack help.";
                        return true;
                    }
                }
                // player wants a named pack
                else {
                    if (args[0].equalsIgnoreCase("help")) {
                        result = ChatColor.DARK_RED + "SnackPack >\n";
                        result += ChatColor.GRAY + "    /pack - Opens default pack.\n";
                        result += ChatColor.GRAY + "    /pack list - Lists your packs.\n";
                        result += ChatColor.GRAY + "    /pack <name> - Opens an existing pack.\n";
                        result += ChatColor.GRAY + "    /pack create <name> - Creates a new pack.\n";
                        result += ChatColor.GRAY + "      - custom : " + ChatColor.GOLD + Mjarket.PACK_CUSTOM_PRICE + ChatColor.GRAY + " doubloons to create snackpack.\n";
                        result += ChatColor.GRAY + "      - craft : " + ChatColor.GOLD + Mjarket.PACK_CRAFT_PRICE + ChatColor.GRAY + " doubloons to add /craft.\n";
                        result += ChatColor.GRAY + "      - ender : " + ChatColor.GOLD + Mjarket.PACK_ENDER_PRICE + ChatColor.GRAY + " doubloons to add /ender.\n";
                        result += ChatColor.GRAY + "    /pack remove <name> - Removes an existing pack.\n";
                        result += ChatColor.GRAY + "    /pack rename <name> <newName> - Renames an existing pack.\n";
                        result += ChatColor.GRAY + "    /pack default <name> - Sets default pack.\n";
                        result += ChatColor.GRAY + "    /pack upgrade <upgrade> <name> - Upgrade a pack.\n";
                        result += ChatColor.GRAY + "      - size : " + ChatColor.GOLD + Mjarket.PACK_UPGRADE_SIZE_PRICE + ChatColor.GRAY + " doubloons to increase by 9.\n";
                        result += ChatColor.GRAY + "      - autosort : " + ChatColor.GOLD + Mjarket.PACK_UPGRADE_AUTO_SORT_PRICE + ChatColor.GRAY + " doubloons to add upgrade.\n";
                        result += ChatColor.GRAY + "      - autosmelt : " + ChatColor.GOLD + Mjarket.PACK_UPGRADE_AUTO_SMELT_PRICE + ChatColor.GRAY + " doubloons to add upgrade.\n";
                    }
                    else if (args[0].equalsIgnoreCase("list")) {

                        result = ChatColor.DARK_RED + "Packs:\n";

                        for (Pack p : snack.getPacks()) {
                            result += ChatColor.GRAY + "    " + p.getName() + "\n";
                        }
                    }
                    else {

                        pack = snack.getPack(args[0]);
                        if (pack == null) {
                            result = ChatColor.RED + "> Pack '" + args[0] + "' not found.";
                            return true;
                        }

                        if (pack.getType().equalsIgnoreCase("craft")) {
                            player.openWorkbench(null, true);
                        } else
                            player.openInventory(pack.toInventory(player));
                    }
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
}
