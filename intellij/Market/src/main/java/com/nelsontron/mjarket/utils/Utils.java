package com.nelsontron.mjarket.utils;

import com.nelsontron.mjarket.entity.Consumer;
import com.nelsontron.mjarket.entity.ConsumerData;
import com.nelsontron.mjarket.entity.Listing;
import com.nelsontron.mjarket.exceptions.NoPermissionException;
import com.nelsontron.mjarket.exceptions.SenderNotPlayerException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    private static final String ALPHA_NUMERIC_STRING = "FARTEHNUS123456789";
    // define a public list just for use basically in the help command.
    // I dont really know of any other way to do this so.
    public static List<String> commandList = new ArrayList<>();

    public static String randomAlphaNumeric(ConsumerData consumerData, int count) {

        final StringBuilder builder = new StringBuilder();

        while (count-- != 0) {
            final int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());

            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }

        final String str = builder.toString();

        // Check to see if the ID already exists.
        for (final Consumer cons : consumerData.getConsumers()) {

            for (final Listing item : cons.getListings()) {

                if (item.getID().equalsIgnoreCase(str)) {
                    return randomAlphaNumeric(consumerData, count);
                }
            }
        }

        return str;
    }

    // boolean function which registers commands with aliases all in one
    // go. I got sick of typing "this.getCommand("name").setExecutor(new
    // BlahHandler());
    // so this function takes infinite arguments so you can register multiple
    // aliases.
    public static boolean registerCommands(CommandExecutor ex, JavaPlugin plugin,
                                           String... commands) {

        try {

            // loop through the commands function argument and get
            // all strings inputted when function is used.
            for (int i = 0; i < commands.length; i++) {

                // if first index add it to the command list. Others
                // are likely aliases.
                if (i == 0) {
                    commandList.add(commands[i]);
                }

                // register command into bukkit command stack.
                plugin.getCommand(commands[i]).setExecutor(ex);
            }

            // if function has a null within the parameters return false.
        } catch (final NullPointerException nullpointer) {

            return false;
        }

        // function has completed successfully.
        return true;
    }

    // cast a player from an object
    public static Player castPlayer(Object ob) throws SenderNotPlayerException {

        // try to cast player.

        if (ob instanceof Player)
            return (Player) ob;
        else
            throw new SenderNotPlayerException();
    }

    public static String errorMessage(String message) {

        // preformatting
        message = ChatColor.RED + "> " + message;
        return message;
    }

    public static String errorMessage(String message, String... buzzwords) {

        // preformatting
        message = ChatColor.RED + "> " + message;

        // replace buzzwords with colored version.
        for (String word : buzzwords)
            message = message.replace(word, ChatColor.DARK_PURPLE + "" + ChatColor.UNDERLINE + word + ChatColor.RED);

        return message;
    }

    public static String arrayToString(Object ob, String regex) {

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < Array.getLength(ob); i++) {

            if (i != 0)
                builder.append(regex);

            builder.append(Array.get(ob, i));
        }

        return builder.toString();
    }

    public static boolean checkSenderPermission(String perm, CommandSender sender) throws NoPermissionException {

        if (sender.hasPermission(perm + ".use"))
            return true;
        else
            throw new NoPermissionException();
    }

    public static Player consumerToPlayer(Consumer consumer) {
        return Bukkit.getPlayer(consumer.getUUID());
    }

    public static float roundDecimal(float num) {
        DecimalFormat df = new DecimalFormat("#.##");
        return Float.parseFloat(df.format(num));
    }
    public static double roundDecimal(double num) {
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.parseDouble(df.format(num));
    }
}
