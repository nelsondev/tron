package com.nelsontron.siege.entity;

import com.nelsontron.siege.data.KitCreator;
import com.nelsontron.siege.game.Holder;
import com.nelsontron.siege.data.Data;
import com.nelsontron.siege.kits.Item;
import com.nelsontron.siege.kits.Kit;
import com.nelsontron.siege.kits.SiegeItem;
import com.nelsontron.siege.kits.SiegeKit;
import com.nelsontron.siege.listeners.MovementListener;
import com.nelsontron.siege.listeners.StaminaListener;
import com.nelsontron.siege.listeners.perks.DodgeListener;
import com.nelsontron.siege.utils.BukkitUtils;
import com.nelsontron.siege.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.*;

public class SiegeFighter implements Fighter {
    private UUID uniqueId;
    private String team;
    private int kills;
    private int deaths;
    private float sr;
    private int kit;
    private Kit[] kits;
    private KitCreator creator;
    private boolean isAllowedToMove;
    private final int MAX_KIT_SLOTS = 5;

    private Data data;

    public SiegeFighter() {
        this.uniqueId = null;
        this.team = null;
        this.kills = 0;
        this.deaths = 0;
        this.sr = 0;
        this.kit = -1;
        this.creator = null;
        this.data = null;
        this.kits = new Kit[MAX_KIT_SLOTS];
        this.isAllowedToMove = true;
    }

    public SiegeFighter(Holder holder, UUID uniqueId) {
        this.uniqueId = uniqueId;
        this.team = null;
        this.kills = 0;
        this.deaths = 0;
        this.sr = 0;
        this.kit = -1;
        this.creator = null;
        this.data = holder.createUserData(this);
        this.kits = new Kit[MAX_KIT_SLOTS];
        this.isAllowedToMove = true;
    }

    // getters
    public UUID getUniqueId() {
        return uniqueId;
    }
    public String getTeam() {
        return team;
    }
    public int getKills() {
        return kills;
    }
    public int getDeaths() {
        return deaths;
    }
    public float getSr() {
        return sr;
    }
    public Kit getKit() {
        if (kit < kits.length && kit != -1) {

            return kits[kit];
        }
        else {
            return null;
        }
    }
    public Player getPlayer() { return Bukkit.getPlayer(uniqueId); }
    public Kit[] getKits() {
        return kits;
    }
    public KitCreator getCreator() {
        return creator;
    }
    public boolean isAllowedToMove() {
        return isAllowedToMove;
    }

    // setters
    public void setUniqueId(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }
    public void setTeam(String team) {
        this.team = team;
    }
    public void setKills(int kills) {
        this.kills = kills;
    }
    public void incrementKills(int amount) {
        this.kills += amount;
    }
    public void decrementKills(int amount) {
        this.kills -= amount;
    }
    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }
    public void incrementDeaths(int amount) {
        this.deaths += amount;
    }
    public void decrementDeaths(int amount) {
        this.deaths -= amount;
    }
    public void setSr(float sr) {
        this.sr = sr;
    }
    public void incrementSr(float amount) {
        this.sr += amount;
    }
    public void decrementSr(float amount) {
        this.sr -= amount;
    }
    public void incrementSr(Fighter killed) {
        // TODO: increment sr based off SR calculation of killer v killed
    }
    public void setKit(int slot) {
        this.kit = slot;
    }
    public void setData(Data data) {
        this.data = data;
    }
    public void setCreator(KitCreator creator) {
        this.creator = creator;
    }
    public void setAllowedToMove(boolean allowedToMove) {
        isAllowedToMove = allowedToMove;
    }

    // methods
    public Kit getDefaultKit() {

        Kit kit = new SiegeKit();
        kit.setDescription("Default siege kit");

        Item boots = new SiegeItem();
        Item leggings = new SiegeItem();
        Item chestplate = new SiegeItem();
        Item helmet = new SiegeItem();
        Item sword = new SiegeItem();
        Item apple = new SiegeItem();
        Item icon;

        boots.setItem(new ItemStack(Material.LEATHER_BOOTS));
        leggings.setItem(new ItemStack(Material.LEATHER_LEGGINGS));
        chestplate.setItem(new ItemStack(Material.LEATHER_CHESTPLATE));
        helmet.setItem(new ItemStack(Material.LEATHER_HELMET));
        sword.setItem(new ItemStack(Material.DIAMOND_SWORD));
        apple.setItem(new ItemStack(Material.APPLE));

        kit.addEffect("speed");
        kit.addPerk("dodge");
        kit.addPerk("bloodlust");

        kit.setBoots(boots);
        kit.setLeggings(leggings);
        kit.setChestplate(chestplate);
        kit.setHelmet(helmet);
        kit.addItem(sword);
        kit.addItem(apple);
        kit.setIcon(kit.createIcon(BukkitUtils.getRandomBlock()));

        return kit;
    }
    public void applyKit() {
        Kit kit = getKit();
        Player player = getPlayer();
        PlayerInventory inventory = player.getInventory();
        inventory.clear();

        player.setGameMode(GameMode.SURVIVAL);

        if (kit == null) {
            kit = getDefaultKit();
        }
        else {
            kit = getKit().clone();
        }

        // add items
        for (int i = kit.getInventory().length-1; i >= 0; i--) {
            Item item = kit.getInventory()[i];
            if (item == null) continue;

            inventory.addItem(item.getItem());
        }

        // set armour
        inventory.setArmorContents(kit.getArmourItemStack());
        // TODO: SET ENCHANTS
        // TODO: SET EFFECTS
    }
    public List<Map<String, Object>> serializeKits() {
        List<Map<String, Object>> list = new ArrayList<>();

        for (Kit kit : kits) {
            if (kit == null) {
                list.add(null);
                continue;
            }

            list.add(kit.serialize());
        }
        return list;
    }
    public Kit[] deserializeKits(List<Map<String, Object>> mapList) {
        Kit[] list = new Kit[Kit.MAX_INV_SLOTS];
        for (int i = 0; i < mapList.size(); i++) {
            Map<String, Object> map = mapList.get(i);
            if (map == null) {
                list[i] = null;
                continue;
            }

            list[i] = SiegeKit.deserialize(map);
        }
        return list;
    }
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("uniqueId", uniqueId.toString());
        if (kills != 0)
            map.put("kills", kills);
        if (deaths != 0)
            map.put("deaths", deaths);
        if (sr != 0)
            map.put("sr", sr);
        if (kits != null);
            map.put("kits", serializeKits());
        if (kit != -1)
            map.put("kit", kit);

            return map;
    }
    public Fighter deserialize(Map<String, Object> map) {
        if (map.containsKey("uniqueId"))
            setUniqueId(UUID.fromString((String) map.get("uniqueId")));
        if (map.containsKey("kills"))
            setKills((Integer) map.get("kills"));
        if (map.containsKey("deaths"))
            setDeaths((Integer) map.get("deaths"));
        if (map.containsKey("sr"))
            setSr((Float) map.get("sr"));
        if (map.containsKey("kits"))
            kits = deserializeKits((List<Map<String, Object>>) map.get("kits"));
        if (map.containsKey("kit"))
            setKit((Integer) map.get("kit"));

        return this;
    }
    public void save() {
        if (data == null) return;
        data.getData().set("data", this.serialize());
        data.save();
    }
    public Fighter load() {
        ConfigurationSection section;

        if (data == null) return null;
        section = data.getData().getConfigurationSection("data");
        if (section == null) return this;

        return this.deserialize(section.getValues(false));
    }
    public void clean() {
        this.uniqueId = null;
        this.team = null;
        this.kills = 0;
        this.deaths = 0;
        this.sr = 0;
        this.kit = -1;
    }

    // static
    public static void registerEvents(Holder holder) {

        holder.getSiege().getServer().getPluginManager().registerEvents(new MovementListener(holder), holder.getSiege());
        holder.getSiege().getServer().getPluginManager().registerEvents(new StaminaListener(holder), holder.getSiege());
        holder.getSiege().getServer().getPluginManager().registerEvents(new DodgeListener(holder), holder.getSiege());
    }
}
