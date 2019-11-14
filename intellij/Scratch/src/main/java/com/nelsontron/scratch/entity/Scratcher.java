package com.nelsontron.scratch.entity;

import com.nelsontron.scratch.Scratch;
import com.nelsontron.scratch.ScratcherHolder;
import com.nelsontron.scratch.commands.*;
import com.nelsontron.scratch.data.Lastcation;
import com.nelsontron.scratch.data.Scrata;
import com.nelsontron.scratch.data.Stats;
import com.nelsontron.scratch.data.Warp;
import com.nelsontron.scratch.listeners.scratcher.PlayerJoin;
import com.nelsontron.scratch.listeners.scratcher.PlayerQuit;
import com.nelsontron.scratch.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Location;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Scratcher {
    private ScratcherHolder holder;
    private UUID uuid;
    private Lastcation lastcation;
    private Stats stats;
    private Scrata data;
    private ItemStack trashedItem;
    private List<Warp> warps;
    private DomainData domainData;

    public Scratcher(ScratcherHolder holder, UUID uuid) {
        this.holder = holder;
        this.uuid = uuid;
        this.lastcation = null;
        this.stats = new Stats();
        this.data = holder.createPlayerData(uuid).save();
        this.trashedItem = null;
        this.warps = new ArrayList<>();
        this.domainData = new DomainData();
    }

    // getters
    public UUID getUuid() {
        return uuid;
    }
    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }
    public Lastcation getLastcation() {
        return lastcation;
    }
    public Stats getStats() {
        return stats;
    }
    public Scrata getData() {
        return data;
    }
    public ItemStack getTrashedItem() { return trashedItem; }
    public List<Warp> getWarps() {
        return warps;
    }
    public Warp getWarp(String name) {
        Warp result = null;
        for (Warp w : warps) {
            if (!w.getName().equalsIgnoreCase(name)) continue;

            result = w;
            break;
        }
        return result;
    }
    public DomainData getDomainData() {
        return domainData;
    }

    // setters
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
    public void setLastcation(Lastcation lastcation) {
        this.lastcation = lastcation;
    }
    public void setStats(Stats stats) {
        this.stats = stats;
    }
    public void setData(Scrata data) {
        this.data = data;
    }
    public void setTrashedItem(ItemStack item) { this.trashedItem = item; }
    public void setWarps(List<Warp> warps) {
        this.warps = warps;
    }
    public void addWarp(Warp warp) {
        this.warps.add(warp);
    }
    public void removeWarp(Warp warp) {
        this.warps.remove(warp);
    }

    // methods
    public void teleport(Location location) {
        Player player = getPlayer();

        if (player == null) return;

        if (lastcation == null)
            setLastcation(new Lastcation(player.getLocation()));
        else
            lastcation.setLocation(player.getLocation());

        if (location == null)
            return;

//        if (location.getWorld() != player.getWorld()) {
//            domainData.updateCurrentDomainData();
//            player.teleportAsync(location, PlayerTeleportEvent.TeleportCause.COMMAND);
//            World world = Bukkit.getWorld(location.getWorld().getName().split("_")[0]);
//            if (world != null)
//                domainData.updatePlayerDomainInventory(world);
//            player.setGameMode(WorldCommand.getGamemode(location.getWorld().getName()));
//        }
//        else {
            player.teleportAsync(location, PlayerTeleportEvent.TeleportCause.COMMAND);
//        }
    }

    public void teleport(Lastcation location) {
        Player player = getPlayer();

        if (player == null) return;

        if (lastcation == null)
            setLastcation(new Lastcation(player.getLocation()));
        else
            lastcation.setLocation(player.getLocation());

        if (location == null)
            return;

//        if (location.getWorld() != player.getWorld()) {
//            domainData.updateCurrentDomainData();
//            player.teleportAsync(location, PlayerTeleportEvent.TeleportCause.COMMAND);
//            World world = Bukkit.getWorld(location.getWorld().getName().split("_")[0]);
//            if (world != null)
//                domainData.updatePlayerDomainInventory(world);
//            player.setGameMode(WorldCommand.getGamemode(location.getWorld().getName()));
//        }
//        else {
        player.teleportAsync(location.getLocation(), PlayerTeleportEvent.TeleportCause.COMMAND);
//        }
    }

    public List<Map<String, Object>> serializeWarpList() {
        List<Map<String, Object>> maps = new ArrayList<>();
        for (Warp warp : warps) {
            maps.add(warp.serialize());
        }
        return maps;
    }
    public List<Warp> deserializeWarpList(List<Map<String, Object>> list) {
        List<Warp> warps = new ArrayList<>();

        for (Map<String, Object> map : list) {
            warps.add(new Warp().deserialize(map));
        }

        return warps;
    }
    public Warp guessWarp(String name) {
        final int POSSIBLE_RESULT_MAX = 3;

        Warp warp = null;
        String guess = null;
        List<String> guessList = new ArrayList<>();;

        for (Warp w : warps) {

            // warp found, return that.
            if (w.getName().equalsIgnoreCase(name)) {
                return w;
            }

            int probability = 0;

            boolean isSync = false;

            List<Character> typedChars = name.chars().mapToObj(c -> (char) c).collect(Collectors.toList());

            if (w.getName().contains(name))
                probability += 2;

            for (Character c : w.getName().toCharArray()) {
                if (typedChars.contains(c)) {

                    probability += (isSync) ? 2 : 1;
                    isSync = true;
                    typedChars.remove(c);
                }
                else {
                    isSync = false;
                }
            }

            if (typedChars.size() != 0)
                probability -= typedChars.size();

            if (probability < POSSIBLE_RESULT_MAX)
                continue;

            guessList.add(w.getName() + " " + probability);
        }

        System.out.println(guessList + " guesses");

        // local variable maxNum to store what number is the biggest found recently.
        int maxNum = 0;
        // loop through list of warps
        for (String warpData : guessList) {
            // split by space, grab the warp name and probablity
            String[] arr = warpData.split(" ");
            int matchNum; // local variable to grab the probablity number
            try {
                matchNum = Integer.parseInt(arr[1]);
            } catch(NumberFormatException ex) {
                matchNum = 0;
            }
            // if current warps value is over the current max
            if (matchNum > maxNum) {
                guess = arr[0]; // set guess to this warp
                maxNum = matchNum;
            }
        }

        // we found a guess
        if (guess != null) {

            for (Warp w : warps)
                if (w.getName().equalsIgnoreCase(guess))
                    warp = w;
        }
        return warp;
    }
    public void sortWarpsAlphabeticalAscending() {
        List<String> names = new ArrayList<>();
        for (Warp w : warps) {
            names.add(w.getName());
        }

        names.sort(Comparator.comparing( String::toString ));

        List<Warp> list = new ArrayList<>();
        for (String name : names) {
            for (Warp w : warps) {
                if (w.getName().equalsIgnoreCase(name))
                    list.add(w);
            }
        }

        warps = list;
    }
    public void sortWarpsAlphabeticalDecending() {
        List<String> names = new ArrayList<>();
        for (Warp w : warps) {
            names.add(w.getName());
        }

        names.sort(Comparator.comparing( String::toString ).reversed());

        List<Warp> list = new ArrayList<>();
        for (String name : names) {
            for (Warp w : warps) {
                if (w.getName().equalsIgnoreCase(name))
                    list.add(w);
            }
        }

        warps = list;
    }

    private Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("uuid", uuid.toString());
        if (lastcation != null)
            map.put("lastcation", lastcation.serialize());
        map.put("stats", stats.serialize());
        if (trashedItem != null)
            map.put("trashed-item", trashedItem);
        if (!warps.isEmpty())
            map.put("warps", serializeWarpList());
        if (domainData != null)
            map.put("domain", domainData.serialize());
        return map;
    }
    private Scratcher deserialize(Map<String, Object> map) {
        if (map.containsKey("uuid"))
            uuid = UUID.fromString((String)map.get("uuid"));
        if (map.containsKey("lastcation")) {
            MemorySection sec = (MemorySection) map.get("lastcation");
            lastcation = new Lastcation().deserialize(sec.getValues(false));
        }
        if (map.containsKey("stats")) {
            MemorySection sec = (MemorySection) map.get("stats");
            stats = stats.deserialize(sec.getValues(false));
        }
        if (map.containsKey("trashed-item"))
            trashedItem = (ItemStack) map.get("trashed-item");
        if (map.containsKey("warps")) {
            warps = deserializeWarpList((List<Map<String, Object>>) map.get("warps"));
        }
        if (map.containsKey("old-warps")) {
            List<String> strings = (List<String>) map.get("old-warps");
            for (String str : strings) {
                warps.add(new Warp(str));
            }
        }
        if (map.containsKey("old-stats")) {
            List<Integer> ints = (List<Integer>) map.get("old-stats");
            stats.fromList(ints);
        }
        if (map.containsKey("domain"))
            domainData = domainData.deserialize(((MemorySection)map.get("domain")).getValues(false));

        return this;
    }
    public void save() {
        domainData.updateCurrentDomainData();

        data.getData().set("data", this.serialize());
        data.save();
    }
    public Scratcher load() {
        ConfigurationSection section = data.getData().getConfigurationSection("data");

        if (section == null) return this;

        deserialize(section.getValues(false));
        return this;
    }

    // static methods

    /**
     * REGISTER EVENTS
     *
     * register all the events related to this class through a static function.
     *
     * @param scratch - needed to grab the server instance as well as to register events the certain plugin scratch
     * @param holder - needed as extension of scratch. Holds all player data objects in the scratch instance.
     */
    public static void registerEvents(Scratch scratch, ScratcherHolder holder) {
        scratch.getServer().getPluginManager().registerEvents(new PlayerJoin(holder), scratch);
        scratch.getServer().getPluginManager().registerEvents(new PlayerQuit(holder), scratch);
    }

    /**
     * REGISTER COMMANDS
     *
     * register all commands related to this class through a static function.
     *
     *
     */
    public static void registerCommands(Scratch scratch, ScratcherHolder holder) {
        ConfigurationSection sec = scratch.getConfig().getConfigurationSection("scratch.commands");

        if (sec == null)
            return;

        if (sec.getBoolean("back"))
            Utils.registerCommands(new BackCommand(holder), scratch, "back");
        if (sec.getBoolean("clear"))
            Utils.registerCommands(new ClearCommand(holder), scratch, "clear");
        if (sec.getBoolean("ender") && Bukkit.getPluginManager().getPlugin("SnackPack") == null)
            Utils.registerCommands(new EnderCommand(holder), scratch, "ender");
        if (sec.getBoolean("here"))
            Utils.registerCommands(new HereCommand(holder), scratch, "here", "h");
        if (sec.getBoolean("nick"))
            Utils.registerCommands(new NickCommand(holder), scratch, "nick");
        if (sec.getBoolean("spawn"))
            Utils.registerCommands(new SpawnCommand(holder), scratch, "spawn");
        if (sec.getBoolean("stats"))
            Utils.registerCommands(new StatsCommand(holder), scratch, "stats");
        if (sec.getBoolean("there"))
            Utils.registerCommands(new ThereCommand(holder), scratch, "there", "t");
        if (sec.getBoolean("trash"))
            Utils.registerCommands(new TrashCommand(holder), scratch, "trash");
        if (sec.getBoolean("untrash"))
            Utils.registerCommands(new TrashCommand(holder), scratch, "untrash");
        if (sec.getBoolean("warp"))
            Utils.registerCommands(new WarpCommand(holder), scratch, "warp", "w");
        if (sec.getBoolean("world"))
            Utils.registerCommands(new WorldCommand(scratch.getConfig(), holder), scratch, "world");
    }

    public class DomainData {

        private List<Domain> domains;

        public DomainData() {
            domains = new ArrayList<>();
        }

        public List<Domain> getDomains() {
            return domains;
        }
        public Domain getDomain(String name) {
            Domain world = null;
            for (Domain w : domains) {
                if (w.getName().equalsIgnoreCase(name)) {
                    world = w;
                }
            }
            return world;
        }

        public void setDomains(List<Domain> worlds) {
            this.domains = worlds;
        }
        public Domain createDomain() {
            String name = getPlayer().getWorld().getName().split("_")[0];

            Location location = getPlayer().getLocation();
            ItemStack[] items = getPlayer().getInventory().getContents();

            if (getDomain(name) != null) return null;

            Domain domain = this.new Domain(name, location, items);
            domains.add(domain);

            return domain;
        }
        public Domain createDomain(World world) {
            String name = world.getName().split("_")[0];

            if (getDomain(name) != null) return null;

            Domain domain = this.new Domain(name, world.getSpawnLocation());
            domains.add(domain);

            return domain;
        }

        public boolean isInDomain() {
            if (getDomain(getPlayer().getWorld().getName()) == null)
                return false;
            else
                return true;
        }
        private void updateItems() {
            Domain domain;
            ItemStack[] items;
            Player player;

            player = getPlayer();
            items = player.getInventory().getContents();
            domain = getDomain(player.getWorld().getName());

            if (domain == null)
                return;

            domain.setItems(items);
        }
        private void updateLocation() {
            Domain domain;
            Location location;
            Player player;

            player = getPlayer();
            domain = getDomain(player.getWorld().getName());
            location = domain.getLocation();

            if (location == null)
                domain.setLocation(new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch()));
            else
                domain.setLocation(player.getLocation());
        }
        public void updateCurrentDomainData() {
            if (isInDomain()) {
                updateItems();
                updateLocation();
            }
            else
                createDomain();
        }

        private void updatePlayerPosition(Domain domain) {
            if (domain == null)
                return;

            if (domain.getLocation() != null)
                teleport(domain.getLocation());
        }
        private void updatePlayerInventory(Domain domain) {
            if (domain == null)
                return;

            if (domain.getItems() == null)
                domain.setItems(new ItemStack[getPlayer().getInventory().getSize()]);

            getPlayer().getInventory().setContents(domain.getItems());
        }

        // send a player to a new domain
        public void updatePlayerDomainInventory(World world) {

            Domain domain;
            domain = getDomain(world.getName());

            // create a new domain if it doesn't exist.
            if (domain == null) domain = createDomain(world);

            updatePlayerInventory(domain);
        }
        public void updatePlayerDomain(World world) {

            Domain domain;
            domain = getDomain(world.getName());

            // create a new domain if it doesn't exist.
            if (domain == null) domain = createDomain(world);

            updatePlayerPosition(domain);
            updatePlayerInventory(domain);
        }

        private List<Map<String, Object>> serializeDomains() {
            List<Map<String, Object>> list = new ArrayList<>();

            for (Domain domain : domains) {
                list.add(domain.serialize());
            }
            return list;
        }
        private List<Domain> deserializeDomains(List<Map<String, Object>> list) {
            List<Domain> domains = new ArrayList<>();

            for (Map<String, Object> map : list) {
                domains.add(new Domain().deserialize(map));
            }
            return domains;
        }

        public Map<String, Object> serialize() {
            Map<String, Object> map = new HashMap<>();
            map.put("data", serializeDomains());
            return map;
        }
        public DomainData deserialize(Map<String, Object> map) {
            if (map.containsKey("data"))
                domains = deserializeDomains((List<Map<String, Object>>) map.get("data"));
            return this;
        }

        public class Domain {

            private String name;
            private Location location;
            private ItemStack[] items;

            public Domain() {
                this.name = null;
                this.location = null;
                this.items = null;
            }

            public Domain(String name, Location location) {
                this.name = name;
                this.location = location;
                this.items = null;
            }

            public Domain(String name, Location location, ItemStack[] items) {
                this.name = name;
                this.location = location;
                this.items = items;
            }

            public String getName() {
                return name;
            }
            public Location getLocation() {
                return location;
            }
            public ItemStack[] getItems() {
                return items;
            }

            public void setName(String name) {
                this.name = name;
            }
            public void setLocation(Location location) {
                this.location = location;
            }
            public void setItems(ItemStack[] items) {
                this.items = items;
            }

            public Map<String, Object> serialize() {
                Map<String, Object> map = new HashMap<>();
                if (name != null)
                    map.put("name", name);
                if (location != null)
                    map.put("location", location);
                if (items != null)
                    map.put("items", items);
                return map;
            }

            private ItemStack[] deserializeItemStacks(List<ItemStack> list) {
                ItemStack[] items = new ItemStack[list.size()];

                for (int i = 0; i < list.size(); i++) {
                    ItemStack item = list.get(i);
                    items[i] = item;
                }
                return items;
            }
            public Domain deserialize(Map<String, Object> map) {
                if (map.containsKey("name"))
                    name = (String) map.get("name");
                if (map.containsKey("location"))
                    location = (Location) map.get("location");
                if (map.containsKey("items"))
                    items = deserializeItemStacks((List<ItemStack>)map.get("items"));

                return this;
            }
        }
    }
}
