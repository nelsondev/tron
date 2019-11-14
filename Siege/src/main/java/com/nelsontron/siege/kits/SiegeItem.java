package com.nelsontron.siege.kits;

import com.nelsontron.siege.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SiegeItem implements Item, Cloneable {
    private int weight;
    private int cost;
    private int defence;
    private int attack;
    private int speed;
    private boolean isSharp;
    private boolean isRanged;
    private ItemStack item;

    private String name;
    private List<String> description;

    public static final int LEATHER_WEIGHT = 1;
    public static final int LEATHER_DEFENCE = 1;
    public static final int IRON_WEIGHT = 3;
    public static final int IRON_DEFENCE = 2;
    public static final int DIA_WEIGHT = 4;
    public static final int DIA_DEFENCE = 3;
    public static final int GOLD_WEIGHT = 5;
    public static final int GOLD_DEFENCE = 4;

    public SiegeItem() {
        this.weight = 0;
        this.cost = 0;
        this.defence = 0;
        this.attack = 0;
        this.speed = 0;
        this.isSharp = false;
        this.isRanged = false;
        this.item = null;
        this.name = null;
        this.description = new ArrayList<>();
    }

    // getters
    public int getWeight() {
        return weight;
    }
    public int getCost() {
        return cost;
    }
    public int getDefence() {
        return defence;
    }
    public int getAttack() {
        return attack;
    }
    public int getSpeed() {
        return speed;
    }
    public boolean isSharp() {
        return isSharp;
    }
    public boolean isRanged() {
        return isRanged;
    }
    public ItemStack getItem() {
        return item;
    }
    public String getName() {
        return name;
    }
    public List<String> getDescription() {
        return description;
    }

    // setters
    public void setWeight(int weight) {
        this.weight = weight;
    }
    public void setCost(int cost) {
        this.cost = cost;
    }
    public void setDefence(int defence) {
        this.defence = defence;
    }
    public void setAttack(int attack) {
        this.attack = attack;
    }
    public void setSpeed(int speed) {
        this.speed = speed;
    }
    public void setSharp(boolean sharp) {
        isSharp = sharp;
    }
    public void setRanged(boolean ranged) {
        isRanged = ranged;
    }
    public void setItem(ItemStack item) {
        this.item = item;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setDescription(String description) {

        this.description.clear();

        for (String str : description.split(";new")) {
            this.description.add(str);
        }
    }
    public void setDescription(List<String> description) {

        this.description = description;
    }
    // methods
    public void updateMeta() {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + name);
        if (!description.isEmpty()) {
            meta.setLore(description);
        }
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        meta.setUnbreakable(true);
        item.setItemMeta(meta);
    }
    public Item clone() {
        SiegeItem item = null;
        try {
            item = (SiegeItem) super.clone();
        }
        catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
        }
        return item;
    }
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        if (weight != 0)
            map.put("weight", weight);
        if (cost != 0)
            map.put("cost", cost);
        if (defence != 0)
            map.put("defence", defence);
        if (attack != 0)
            map.put("attack", attack);
        if (speed != 0)
            map.put("speed", speed);
        map.put("sharp", isSharp);
        map.put("ranged", isRanged);
        if (item != null)
            map.put("item", item);
        if (name != null)
            map.put("name", name);
        if (description != null)
            map.put("description", description);
        return map;
    }
    // static
    public static Item deserialize(Map<String, Object> map) {
        SiegeItem item = new SiegeItem();

        if (map == null) return null;

        if (map.containsKey("weight"))
            item.setWeight((Integer) map.get("weight"));
        if (map.containsKey("cost"))
            item.setCost((Integer) map.get("cost"));
        if (map.containsKey("defence"))
            item.setDefence((Integer) map.get("defence"));
        if (map.containsKey("attack"))
            item.setAttack((Integer) map.get("attack"));
        if (map.containsKey("speed"))
            item.setSpeed((Integer) map.get("speed"));
        if (map.containsKey("sharp"))
            item.setSharp((Boolean) map.get("sharp"));
        if (map.containsKey("ranged"))
            item.setRanged((Boolean) map.get("ranged"));
        if (map.containsKey("item"))
            item.setItem((ItemStack) map.get("item"));
        if (map.containsKey("name"))
            item.setName((String) map.get("name"));
        if (map.containsKey("description"))
            item.setDescription((List<String>) map.get("description"));

        return item;
    }
}
