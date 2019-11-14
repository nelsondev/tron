package buildtools.utils;

import buildtools.exceptions.ObjectNotPlayerException;
import buildtools.exceptions.SenderNoPermissionException;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Utils {

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

    public static String errorMessage(String message) {

        // preformatting
        message = ChatColor.RED + "> " + message + ".";
        return message;
    }

    public static String errorMessage(String message, String... buzzwords) {

        // preformatting
        message = ChatColor.RED + "> " + message + ".";

        // replace buzzwords with colored version.
        for (String word : buzzwords)
            message = message.replace(word, ChatColor.DARK_PURPLE + "" + ChatColor.UNDERLINE + word + ChatColor.RED);

        return message;
    }

    public static String message(String message) {

        // preformatting
        message = ChatColor.GRAY + message + ".";
        return message;
    }

    public static String message(String message, String... buzzwords) {

        // preformatting
        message = ChatColor.GRAY + message + ".";

        // replace buzzwords with colored version.
        for (String word : buzzwords)
            message = message.replace(word, ChatColor.BLUE + word + ChatColor.GRAY);

        return message;
    }

    public static String arrayToString(Object ob) {

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < Array.getLength(ob); i++) {

            builder.append(Array.get(ob, i));
        }

        return builder.toString();
    }

    public static String arrayToString(Object ob, String regex) {

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < Array.getLength(ob); i++) {

            if (i != 0 || i != Array.getLength(ob))
                builder.append(regex);

            builder.append(Array.get(ob, i));
        }

        return builder.toString();
    }

    public static List<String> stringsToArray(String... str) {
        List<String> list = new ArrayList<>();

        for (String s : str) {
            list.add(s);
        }

        return list;
    }

    // Function to merge multiple arrays in Java 8
    public static String[] mergeArrays(String[]... arrays) {
        return Stream.of(arrays)
                .flatMap(Stream::of)        // or use Arrays::stream
                .toArray(String[]::new);
    }

    // cast a player from an object
    public static Player castPlayer(Object ob) throws ObjectNotPlayerException {

        // try to cast player.

        if (ob instanceof Player)
            return (Player) ob;
        else
            throw new ObjectNotPlayerException();
    }

    public static void checkPermission(String perm, CommandSender sender) throws SenderNoPermissionException {

        if (!sender.hasPermission(perm))
            throw new SenderNoPermissionException();
    }
}
