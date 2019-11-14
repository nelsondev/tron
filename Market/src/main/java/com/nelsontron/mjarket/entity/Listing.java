package com.nelsontron.mjarket.entity;

import com.nelsontron.mjarket.utils.Utils;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class Listing {

    private ItemStack item;
    private Double cost;
    private String ID;

    public Listing() {
        this.item = null;
        this.cost = 0.0;
        this.ID = null;
    }

    public Listing(Government government) {
        this.item = null;
        this.cost = 0.0;
        this.ID = Utils.randomAlphaNumeric(government.getConsumerData(), 4);
    }

    public Listing(String ID) {
        this.item = null;
        this.cost = 0.0;
        this.ID = ID;
    }

    public Listing(String ID, ItemStack item, Double cost) {
        this.item = item;
        this.cost = cost;
        this.ID = ID;
    }

    public Listing(Government government, ItemStack item) {
        this.item = item;
        this.cost = 0.0;
        this.ID = Utils.randomAlphaNumeric(government.getConsumerData(), 4);
    }

    public Listing(Government government, ItemStack item, Double cost) {
        this.item = item;
        this.cost = cost;
        this.ID = Utils.randomAlphaNumeric(government.getConsumerData(), 4);
    }

    public ItemStack getItemStack() {
        return item;
    }

    public void setItemStack(ItemStack item) {
        this.item = item;
    }

    public Double getCost() {
        return cost;
    }
    public Double getRelativeCost() {
        return Utils.roundDecimal(cost/item.getAmount());
    }

    public void setCost(double d) {
        cost = d;
    }

    public String getID() {
        return ID;
    }

    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("item", item);
        map.put("cost", cost);
        map.put("id", ID);
        return  map;
    }

    public Listing deserialize(Map<String, Object> map) {
        if (map.containsKey("item"))
            item = (ItemStack) map.get("item");
        if (map.containsKey("cost"))
            cost = (Double) map.get("cost");
        if (map.containsKey("id"))
            ID = (String) map.get("id");
        return this;
    }
}
