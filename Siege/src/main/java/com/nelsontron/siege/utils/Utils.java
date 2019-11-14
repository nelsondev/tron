package com.nelsontron.siege.utils;

import org.bukkit.ChatColor;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static String formatSiegeMessage(String str) {
        return ChatColor.RED + "" + ChatColor.BOLD + "Siege > " + ChatColor.RED + str;
    }
    public static String formatSiegeMessage(String str, String ...buzz) {
        String result;

        result = ChatColor.RED + "" + ChatColor.BOLD + "Siege > " + ChatColor.RED + str;
        for (String word : buzz) {
            result = result.replace(word, ChatColor.UNDERLINE + word + ChatColor.RED);
        }
        return result;
    }
    public static String stringify(Object ob, String regex) {

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < Array.getLength(ob); i++) {

            if (i != 0)
                builder.append(regex);

            builder.append(Array.get(ob, i));
        }

        return builder.toString();
    }

    public static List<String> cleanArray(String[] list) {
        List<String> l = new ArrayList<>();
        for (int i = 0; i < list.length; i++) {
            if (list[i] != null) l.add(list[i]);
        }
        return l;
    }
}
