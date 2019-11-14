package com.nelsontron.siege.kits;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface Item {

    // getters
    int getWeight();
    int getCost();
    int getDefence();
    int getAttack();
    int getSpeed();
    boolean isSharp();
    boolean isRanged();
    ItemStack getItem();
    String getName();
    List<String> getDescription();
    // setters
    void setWeight(int weight);
    void setCost(int cost);
    void setDefence(int defence);
    void setAttack(int attack);
    void setSpeed(int speed);
    void setSharp(boolean isSharp);
    void setRanged(boolean isRanged);
    void setItem(ItemStack item);
    void setName(String name);
    void setDescription(String description);
    void setDescription(List<String> description);
    // methods
    void updateMeta();
    Item clone();
    Map<String, Object> serialize();
}
