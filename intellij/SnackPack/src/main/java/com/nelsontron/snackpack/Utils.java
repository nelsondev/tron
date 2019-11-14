package com.nelsontron.snackpack;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import sun.reflect.generics.tree.Tree;

import java.util.*;

public class Utils {
    // boolean function which registers commands with aliases all in one
    // go. I got sick of typing "this.getCommand("name").setExecutor(new BlahHandler());
    // so this function takes infinite arguments so you can register multiple aliases.
    public static boolean registerCommands(CommandExecutor ex, JavaPlugin plugin, String... commands) {

        try {

            // loop through the commands function argument and get
            // all strings inputted when function is used.
            for (int i = 0; i < commands.length; i++) {

                // register command into bukkit command stack.
                plugin.getCommand(commands[i]).setExecutor(ex);
            }

            // if function has a null within the parameters return false.
        } catch (NullPointerException nullpointer) {

            return false;
        }

        // function has completed successfully.
        return true;
    }

    public static Comparator<String> ALPHABETICAL_ORDER = (String str1, String str2)-> {
        int res = String.CASE_INSENSITIVE_ORDER.compare(str1, str2);
        if (res == 0) {
            res = str1.compareTo(str2);
        }
        return res;
    };

    public static void sortArray(List<String> arr) {
        arr.sort(Comparator.comparing( String::toString ));
    }

    public static List<ItemStack> compactItemList(List<List<ItemStack>> list) {
        List<ItemStack> result = new ArrayList<>();

        for (List<ItemStack> itemList : list) {
            for (ItemStack item : itemList)
                if (item != null) result.add(item);
        }

        return result;
    }

    static void removeFirst(List list, Object obj) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) != obj)
                continue;

            list.remove(i);
            break;
        }
    }

    static List<ItemStack> getItemFromItemList(List<ItemStack> items, Material type) {
        List<ItemStack> result = new ArrayList<>();
        for (ItemStack item : items) {
            if (item.getType() == type)
                result.add(item);
        }
        return result;
    }

    static List<ItemStack> getItemFromItemList(List<ItemStack> items, String typeName) {
        List<ItemStack> result = new ArrayList<>();
        for (ItemStack item : items) {
            if (item.getType().toString().equalsIgnoreCase(typeName) || item.getType().toString().toLowerCase().contains(typeName))
                result.add(item);
        }
        return result;
    }

    static List<ItemStack> sortItemListAlphabetically(List<ItemStack> items) {
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

    static List<List<ItemStack>> sortItemListByCategory(List<ItemStack> itemList, List<String> categories, String regex) {
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
//
//        for (String category : categories) {
//
//            if (category == null)
//                continue;
//
//            for (String subCategory : category.split(regex)) {
//
//                if (!type.contains(subCategory))
//                    continue;
//
//                canCategorize = true;
//                categoryResult = i;
//                break outerLoop;
//            }
//        }
//
//        for (ItemStack item : list) {
//
//            int categoryResult = 0;
//            boolean canCategorized = false;
//
//            if (item == null)
//                continue;
//
//
//        }
//
//        for (ItemStack item : list) {
//
//            if (item == null)
//                continue;
//
//            String type = item.getType().toString().toLowerCase();
//            int categoryResult = 0;
//            boolean canCategorize = false;
//
//            // loop through category list
//            outerLoop:
//            for (int i = 0; i < categories.size(); i++) {
//                String category = categories.get(i);
//
//                if (category == null)
//                    continue;
//
//                for (String subCategory : category.split(regex)) {
//
//                    if (!type.contains(subCategory))
//                        continue;
//
//                    canCategorize = true;
//                    categoryResult = i;
//                    break outerLoop;
//                }
//            }
//
//            // if item can categorize add them to the specified category
//            if (canCategorize)
//                sorted.get(categoryResult).add(item);
//                // else add to misc category.
//            else
//                sorted.get(sorted.size()-1).add(item);
//        }
//
//        return sorted;
    }

    static List<ItemStack> sortItemStackList(List<ItemStack> list, List<String> categories, String regex) {

        List<List<ItemStack>> categorized;

        // sort item list by category
        list = sortItemListAlphabetically(list);
        categorized = sortItemListByCategory(list, categories, regex);

        return compactItemList(categorized);
    }
}
