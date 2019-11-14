package com.nelsontron.siege.kits;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public interface Kit {

    int MAX_POINTS = 20;
    int MAX_INV_SLOTS = 8;
    int MAX_POT_EFFECTS = 3;
    int MAX_PERKS = 4;

    // getters
    String getDescription();
    Item getHelmet();
    Item getChestplate();
    Item getLeggings();
    Item getBoots();
    Item getIcon();
    Item[] getInventory();
    String[] getEffects();
    String[] getPerks();
    float getWeight();
    int getPoints();
    int firstEmptyInventory();
    int firstEmptyEffect();
    int firstEmptyPerk();
    Item[] getArmour();
    ItemStack[] getArmourItemStack();

    // setters
    void setDescription(String description);
    void setHelmet(Item helmet);
    void setChestplate(Item chestplate);
    void setLeggings(Item leggings);
    void setBoots(Item boots);
    void setIcon(Item icon);
    void setInventory(Item[] inventory);
    void addItem(Item item);
    void setEffects(String[] effects);
    void addEffect(String string);
    void setPerks(String[] perks);
    void addPerk(String string);

    void setWeight(float weight);
    void incrementWeight(float amount);
    void decrementWeight(float amount);

    void setPoints(int points);
    void incrementPoints(int amount);
    void decrementPoints(int amount);
    // methods
    Item createIcon(Material type);
    Kit clone();
    Map<String, Object> serialize();
}
