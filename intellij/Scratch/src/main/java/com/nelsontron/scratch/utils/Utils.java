package com.nelsontron.scratch.utils;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Utils {
    // function used to get a player by name. This function
    // is depreciated in Bukkits API and I wanna avoid having
    // to put @Supress('depreciation') annotations everywhere.
    // This function pretty much does the same thing that the
    // bukkit one would do.
    public static Player getPlayer(String str) {

        // predefine player to null so we can check if this
        // function returns null when we use it.
        // player = Util.getPlayer("joe");
        // if (player == null)
        //    // handle exception
        Player result = null;

        // quickly loop through the Bukkit.getOnlinePlayers()
        // function to return a player rather than a number.
        // just makes it easy to manipulate when you dont
        // need the index.
        for (Player online : Bukkit.getOnlinePlayers()) {

            if (!online.getName().equalsIgnoreCase(str))
                continue;

            // define our returned player.
            result = online;
            break;
        }

        return result;
    }
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
    // i ripped this off the internet, recursive function to check if a string contains a char.
    public static boolean containsChar(String s, char search) {
        if (s.length() == 0)
            return false;
        else
            return s.charAt(0) == search || containsChar(s.substring(1), search);
    }
    // grab a random safe enchantment for an itemstack.
    public static ItemStack randomEnchantment(ItemStack item) {
        // Store all possible enchantments for the item
        List<Enchantment> possible = new ArrayList<Enchantment>();

        // Loop through all enchantemnts
        for (Enchantment ench : Enchantment.values()) {
            // Check if the enchantment can be applied to the item, save it if it can
            if (ench.canEnchantItem(item)) {
                possible.add(ench);
            }
        }

        // If we have at least one possible enchantment
        if (possible.size() >= 1) {
            // Randomize the enchantments
            Collections.shuffle(possible);
            // Get the first enchantment in the shuffled list
            Enchantment chosen = possible.get(0);
            // Apply the enchantment with a random level between 1 and the max level
            item.addEnchantment(chosen, 1 + (int) (Math.random() * ((chosen.getMaxLevel() - 1) + 1)));
        }

        // Return the item even if it doesn't have any enchantments
        return item;
    }
    // stringify an array object with a regex.
    public static String stringify(Object ob, String regex) {

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < Array.getLength(ob); i++) {

            if (i != 0)
                builder.append(regex);

            builder.append(Array.get(ob, i));
        }

        return builder.toString();
    }

    /**
     * Saves a serializable object to a given file.
     *
     * @param instance Object to seriaize
     * @param file File to save to
     * @throws IllegalArgumentException If the instance or file is null
     * @throws IOException If the file cannot be written to
     */
    public static void save(ConfigurationSerializable instance, File file) throws IOException
    {
        Validate.notNull(instance, "instance cannot be null!");
        Validate.notNull(file, "file cannot be null!");

        file.delete();
        file.createNewFile();

        YamlConfiguration config = new YamlConfiguration();

        for (Map.Entry<String, Object> entry : instance.serialize().entrySet())
        {
            if (entry.getValue() instanceof ConfigurationSerializable) {
                config.set(entry.getKey(), ((ConfigurationSerializable) entry.getValue()).serialize());
            }
            else {
                config.set(entry.getKey(), entry.getValue());
            }
        }

        config.save(file);
    }

    // get an item stacks food amount
    public static int getFoodInfo(ItemStack item) {
        if (item.getType() == Material.APPLE)
            return 4;
        else if (item.getType() == Material.BREAD)
            return 5;
        else if (item.getType() == Material.BAKED_POTATO)
            return 5;
        else if (item.getType() == Material.BEETROOT)
            return 1;
        else if (item.getType() == Material.BEETROOT_SOUP)
            return 6;
        else if (item.getType() == Material.CAKE)
            return 2;
        else if (item.getType() == Material.CARROT)
            return 3;
        else if (item.getType() == Material.CHORUS_FRUIT)
            return 4;
        else if (item.getType() == Material.COOKED_CHICKEN)
            return 6;
        else if (item.getType() == Material.COOKED_COD)
            return 5;
        else if (item.getType() == Material.COOKED_MUTTON)
            return 6;
        else if (item.getType() == Material.COOKED_PORKCHOP)
            return 8;
        else if (item.getType() == Material.COOKED_RABBIT)
            return 5;
        else if (item.getType() == Material.COOKED_SALMON)
            return 6;
        else if (item.getType() == Material.COOKIE)
            return 2;
        else if (item.getType() == Material.DRIED_KELP)
            return 1;
        else if (item.getType() == Material.GOLDEN_APPLE)
            return 4;
        else if (item.getType() == Material.ENCHANTED_GOLDEN_APPLE)
            return 4;
        else if (item.getType() == Material.GOLDEN_CARROT)
            return 6;
        else if (item.getType() == Material.MELON_SLICE)
            return 2;
        else if (item.getType() == Material.MUSHROOM_STEW)
            return 6;
        else if (item.getType() == Material.POISONOUS_POTATO)
            return 2;
        else if (item.getType() == Material.POTATO)
            return 1;
        else if (item.getType() == Material.PUFFERFISH)
            return 1;
        else if (item.getType() == Material.PUMPKIN_PIE)
            return 8;
        else if (item.getType() == Material.RABBIT_STEW)
            return 10;
        else if (item.getType() == Material.BEEF)
            return 3;
        else if (item.getType() == Material.CHICKEN)
            return 2;
        else if (item.getType() == Material.COD)
            return 2;
        else if (item.getType() == Material.MUTTON)
            return 2;
        else if (item.getType() == Material.PORKCHOP)
            return 3;
        else if (item.getType() == Material.RABBIT)
            return 3;
        else if (item.getType() == Material.SALMON)
            return 2;
        else if (item.getType() == Material.ROTTEN_FLESH)
            return -2;
        else if (item.getType() == Material.SPIDER_EYE)
            return 2;
        else if (item.getType() == Material.COOKED_BEEF)
            return 8;
        else if (item.getType() == Material.SUSPICIOUS_STEW)
            return 6;
        else if (item.getType() == Material.SWEET_BERRIES)
            return 2;
        else if (item.getType() == Material.TROPICAL_FISH)
            return 1;
        else return 0;
    }
}
