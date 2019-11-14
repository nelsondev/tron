package com.nelsontron.lockguy.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class Sort {

    List<String> categories;
    String regex;

    public Sort() {
        categories = new ArrayList<>();
        categories.add("enchant-book-anvil-lapis");
        categories.add("diamond");
        categories.add("ingot");
        categories.add("coal-charcoal");
        categories.add("pickaxe");
        categories.add("_axe");
        categories.add("spade-shovel");
        categories.add("sword");
        categories.add("helm");
        categories.add("chestplate");
        categories.add("leggin");
        categories.add("boots");
        categories.add("hoe-stick-rod-bucket-bow-armour-flint_-arrow");
        categories.add("redstone-ator-repeater-piston-button-pressu-dispenser-dropper-gate-door");
        categories.add("wheat-sugar-help-bamboo-carrot-potato-pumpk-egg");
        categories.add("seed");
        categories.add("apple-stew-bread-raw-cookie-cake-steak-cooked-pie-tropical");
        categories.add("log-wood");
        categories.add("plank");
        categories.add("_brick-stonebrick");
        categories.add("ore");
        categories.add("stone-andes-dior-dirt-cobble-granite-sand");
        categories.add("nether-");
        categories.add("table-chest-furnace-barrel-cutter-fire-lantern-wool");
        categories.add("misc");

        regex = "-";
    }

    public List<ItemStack> compactItemList(List<List<ItemStack>> list) {
        List<ItemStack> result = new ArrayList<>();

        for (List<ItemStack> itemList : list) {
            for (ItemStack item : itemList)
                if (item != null) result.add(item);
        }

        return result;
    }

    public List<ItemStack> getItemFromItemList(List<ItemStack> items, Material type) {
        List<ItemStack> result = new ArrayList<>();
        for (ItemStack item : items) {
            if (item.getType() == type)
                result.add(item);
        }
        return result;
    }

    public List<ItemStack> getItemFromItemList(List<ItemStack> items, String typeName) {
        List<ItemStack> result = new ArrayList<>();
        for (ItemStack item : items) {
            if (item.getType().toString().equalsIgnoreCase(typeName) || item.getType().toString().toLowerCase().contains(typeName))
                result.add(item);
        }
        return result;
    }

    public List<ItemStack> sortItemListAlphabetically(List<ItemStack> items) {
        List<ItemStack> sorted = new ArrayList<>();
        TreeSet<String> names = new TreeSet<>();

        for (ItemStack item : items) {
            if (item == null)
                continue;

            if (!names.contains(item.getType().toString()))
                names.add(item.getType().toString());
        }

        for (String name : names) {
            Material type = Material.getMaterial(name);
            sorted.addAll(getItemFromItemList(items, type));
        }

        return sorted;
    }

    public List<List<ItemStack>> sortItemListByCategory(List<ItemStack> itemList) {
        List<List<ItemStack>> sorted = new ArrayList<>();

        // fill category data
        for (String str : categories)
            sorted.add(new ArrayList<>());

        // add misc list
        sorted.add(new ArrayList<>());

        // loop through categories and return strings like "ingot-chest-block-diamond"
        for (int i = 0; i < categories.size(); i++) {
            String category = categories.get(i);
            List<ItemStack> items; // list of items to add to this category

            if (category == null) continue;

            // loop through individual sub categories "ingot" "chest" "block"
            for (String subCategory : category.split(regex)) {

                // grab all items matching subCategory from inventory
                items = getItemFromItemList(itemList, subCategory);

                // remove all those items from the itemList
                itemList.removeAll(items);

                if (items.isEmpty()) continue;

                // add items to category. basically the equivalent of
                // List<ItemStack> ingot(chest,or block) = items;
                sorted.get(i).addAll(items);
            }
        }
        // add the remainder as a "misc" category.
        sorted.get(sorted.size()-1).addAll(itemList);

        return sorted;
    }

    public List<ItemStack> sortItemStackList(List<ItemStack> list) {

        List<List<ItemStack>> categorized;

        // sort item list by category
        list = sortItemListAlphabetically(list);
        categorized = sortItemListByCategory(list);

        return compactItemList(categorized);
    }
}
