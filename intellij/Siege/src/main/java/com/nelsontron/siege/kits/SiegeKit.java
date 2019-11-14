package com.nelsontron.siege.kits;

import com.nelsontron.siege.Siege;
import com.nelsontron.siege.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.MemorySection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SiegeKit implements Kit, Cloneable {
    public final int MAX_POINTS = 20;
    public final int MAX_INV_SLOTS = 8;
    public final int MAX_POT_EFFECTS = 3;
    public final int MAX_PERKS = 4;

    private String description;

    private Item helmet;
    private Item chestplate;
    private Item leggings;
    private Item boots;
    private Item icon;

    private Item[] inventory;
    private String[] effects;
    private String[] perks;

    private float weight;
    private int points;

    public SiegeKit() {
        this.description = null;
        this.helmet = null;
        this.chestplate = null;
        this.leggings = null;
        this.boots = null;
        this.icon = null;
        this.inventory = new Item[MAX_INV_SLOTS];
        this.effects = new String[MAX_POT_EFFECTS];
        this.perks = new String[MAX_PERKS];
        this.weight = 0;
        this.points = MAX_POINTS;
    }

    // getters
    public String getDescription() { return description; }
    public Item getHelmet() { return helmet; }
    public Item getChestplate() { return chestplate; }
    public Item getLeggings() { return leggings; }
    public Item getBoots() { return boots; }
    public Item getIcon() { return icon; }

    public Item[] getInventory() { return inventory; }
    public int firstEmptyInventory() {
        int index = -1;
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] == null)
                index = i;
        }
        return index;
    }
    public String[] getEffects() { return effects; }
    public int firstEmptyEffect() {
        int index = -1;
        for (int i = 0; i < effects.length; i++) {
            if (effects[i] == null)
                index = i;
        }
        return index;
    }
    public String[] getPerks() { return perks; }
    public int firstEmptyPerk() {
        int index = -1;
        for (int i = 0; i < perks.length; i++) {
            if (perks[i] == null)
                index = i;
        }
        return index;
    }
    public float getWeight() { return weight; }
    public int getPoints() { return points; }
    public Item[] getArmour() {
        Item[] armour = new SiegeItem[4];
        armour[0] = boots;
        armour[1] = leggings;
        armour[2] = chestplate;
        armour[3] = helmet;
        return armour;
    }
    public ItemStack[] getArmourItemStack() {
        ItemStack[] armour = new ItemStack[4];
        armour[0] = boots.getItem();
        armour[1] = leggings.getItem();
        armour[2] = chestplate.getItem();
        armour[3] = helmet.getItem();
        return armour;
    }
    // setters
    public void setDescription(String description) { this.description = description; }
    public void setHelmet(Item helmet) { this.helmet = helmet; }
    public void setChestplate(Item chestplate) { this.chestplate = chestplate; }
    public void setLeggings(Item leggings) { this.leggings = leggings; }
    public void setBoots(Item boots) { this.boots = boots; }
    public void setIcon(Item icon) {
        this.icon = icon;
    }
    public void setInventory(Item[] inventory) { this.inventory = inventory; }
    public void addItem(Item item) {
        int index = firstEmptyInventory();
        if (index == -1) return;
        inventory[index] = item;
    }
    public void setEffects(String[] effects) { this.effects = effects; }
    public void addEffect(String string) {
        int index = firstEmptyEffect();
        if (index == -1) return;
        effects[index] = string;
    }
    public void setPerks(String[] perks) { this.perks = perks; }
    public void addPerk(String string) {
        int index = firstEmptyPerk();
        if (index == -1) return;
        perks[index] = string;
    }

    public void setWeight(float weight) { this.weight = weight; }
    public void incrementWeight(float amount) { this.weight += amount; }
    public void decrementWeight(float amount) { this.weight -= amount; }

    public void setPoints(int points) { this.points = points; }
    public void incrementPoints(int amount) { this.points += amount; }
    public void decrementPoints(int amount) { this.points -= amount; }
    // methods
    public Item createIcon(Material type) {
        Item icon = new SiegeItem();
        icon.setItem(new ItemStack(type));
        icon.setName(ChatColor.UNDERLINE + "Kit");

        String description = ChatColor.GRAY + "Custom kit.;new";

        Item boots = getBoots();
        Item legs = getLeggings();
        Item chest = getChestplate();
        Item helm = getHelmet();
        List<String> perks = Utils.cleanArray(this.perks);
        List<String> effects = Utils.cleanArray(this.effects);
        List<String> armour = new ArrayList<>();
        int invAmount = getInventory().length;

        String bootName;
        String legName;
        String chestName;
        String helmName;

        for (Item item : getArmour()) {
            if (item != null) armour.add(item.getItem().getType().toString().split("_")[0].toLowerCase());
        }

        if (perks.size() != 0) {
            description += " ;new" +
                    ChatColor.AQUA + "Perks;new";
            for (int i = 0; i < perks.size(); i++) {
                String perk = perks.get(i);
                if (i == 0) {
                    description += ChatColor.DARK_GRAY + "    " + perk;
                }
                else {
                    description += ChatColor.DARK_GRAY + ", " + perk;
                }

                if (i == (perks.size()-1)) {
                    description += ";new";
                }
            }
        }

        if (effects.size() != 0) {
            description += " ;new" +
                    ChatColor.AQUA + "Effects;new";
            for (int i = 0; i < effects.size(); i++) {
                String effect = effects.get(i);
                if (i == 0) {
                    description += ChatColor.DARK_GRAY + "    " + effect;
                }
                else {
                    description += ChatColor.DARK_GRAY + ", " + effect;
                }

                if (i == (effects.size()-1)) {
                    description += ";new";
                }
            }
        }

        if (armour.size() != 0) {
            description += " ;new" +
                    ChatColor.AQUA + "Armour;new";
            for (int i = 0; i < armour.size(); i++) {
                String piece = armour.get(i);
                if (i == 0) {
                    description += ChatColor.DARK_GRAY + "    " + piece;
                }
                else {
                    description += ChatColor.DARK_GRAY + ", " + piece;
                }

                if (i == (armour.size()-1)) {
                    description += ";new";
                }
            }
        }

        icon.setDescription(description);
        icon.updateMeta();

//        if (boots != null) {
//            bootName = boots.getItem().getType().toString().split("_")[0].toLowerCase();
//            description += "";
//        }
//
//        if (boots != null)
//            description
        return icon;
    }

    public Kit clone() {
        Kit kit = null;
        try {
            kit = (Kit) super.clone();
        }
        catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
        }
        return kit;
    }
    private List<Map<String, Object>> serializeInventory() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Item item : inventory) {
            if (item == null) {
                list.add(null);
                continue;
            }

            list.add(item.serialize());
        }
        return list;
    }
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        if (description != null)
            map.put("description", description);
        if (helmet != null)
            map.put("helmet", helmet.serialize());
        if (chestplate != null)
            map.put("chestplate", chestplate.serialize());
        if (leggings != null)
            map.put("leggings", leggings.serialize());
        if (boots != null)
            map.put("boots", boots.serialize());
        if (icon != null)
            map.put("icon", icon.serialize());
        if (inventory != null)
            map.put("inventory", this.serializeInventory());
        if (effects != null)
            map.put("effects", effects);
        if (perks != null)
            map.put("perks", perks);
        if (weight != 0)
            map.put("weight", weight);
        if (points != 0)
            map.put("points", points);
        return map;
    }
    // static
    public static Kit deserialize(Map<String, Object> map) {
        SiegeKit kit = new SiegeKit();

        if (map.containsKey("description"))
            kit.setDescription((String) map.get("description"));
        if (map.containsKey("helmet"))
            kit.setHelmet(SiegeItem.deserialize((Map<String, Object>) map.get("helmet")));
        if (map.containsKey("chestplate"))
            kit.setChestplate(SiegeItem.deserialize((Map<String, Object>) map.get("chestplate")));
        if (map.containsKey("leggings"))
            kit.setLeggings(SiegeItem.deserialize((Map<String, Object>) map.get("leggings")));
        if (map.containsKey("boots"))
            kit.setBoots(SiegeItem.deserialize((Map<String, Object>) map.get("boots")));
        if (map.containsKey("icon"))
            kit.setIcon(SiegeItem.deserialize((Map<String, Object>) map.get("icon")));
        if (map.containsKey("inventory")) {
            List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("inventory");
            for (Map<String, Object> m : list)
                kit.addItem(SiegeItem.deserialize(m));
        }
        if (map.containsKey("effects")) {
            List<String> list = (List<String>) map.get("effects");
            for (int i = 0; i < list.size(); i++)
                kit.getEffects()[i] = list.get(i);
        }
        if (map.containsKey("perks")) {
            List<String> list = (List<String>) map.get("perks");
            for (int i = 0; i < list.size(); i++)
                kit.getPerks()[i] = list.get(i);
        }
        if (map.containsKey("weight"))
            kit.setWeight((Integer) map.get("weight"));
        if (map.containsKey("points"))
            kit.setPoints((Integer) map.get("points"));

        return kit;
    }

    public static Kit deserializeSingle(Map<String, Object> map) {
        SiegeKit kit = new SiegeKit();

        if (map.containsKey("description"))
            kit.setDescription((String) map.get("description"));
        if (map.containsKey("helmet"))
            kit.setHelmet(SiegeItem.deserialize(((MemorySection) map.get("helmet")).getValues(false)));
        if (map.containsKey("chestplate"))
            kit.setChestplate(SiegeItem.deserialize(((MemorySection) map.get("chestplate")).getValues(false)));
        if (map.containsKey("leggings"))
            kit.setLeggings(SiegeItem.deserialize(((MemorySection) map.get("leggings")).getValues(false)));
        if (map.containsKey("boots"))
            kit.setBoots(SiegeItem.deserialize(((MemorySection) map.get("boots")).getValues(false)));
        if (map.containsKey("icon"))
            kit.setIcon(SiegeItem.deserialize(((MemorySection) map.get("icon")).getValues(false)));
        if (map.containsKey("inventory")) {
            List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("inventory");
            for (Map<String, Object> m : list)
                kit.addItem(SiegeItem.deserialize(m));
        }
        if (map.containsKey("effects")) {
            List<String> list = (List<String>) map.get("effects");
            for (int i = 0; i < list.size(); i++)
                kit.getEffects()[i] = list.get(i);
        }
        if (map.containsKey("perks")) {
            List<String> list = (List<String>) map.get("perks");
            for (int i = 0; i < list.size(); i++)
                kit.getPerks()[i] = list.get(i);
        }
        if (map.containsKey("weight"))
            kit.setWeight((Integer) map.get("weight"));
        if (map.containsKey("points"))
            kit.setPoints((Integer) map.get("points"));

        return kit;
    }
}
