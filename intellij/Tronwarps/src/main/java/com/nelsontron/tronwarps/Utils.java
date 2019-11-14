package com.nelsontron.tronwarps;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Comparator;

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

    public static void sortStringBubble( String  x [ ] )
    {
        int j;
        boolean flag = true;  // will determine when the sort is finished
        String temp;

        while ( flag )
        {
            flag = false;
            for ( j = 0;  j < x.length - 1;  j++ )
            {
                if ( x [ j ].compareToIgnoreCase( x [ j+1 ] ) > 0 )
                {                                             // ascending sort
                    temp = x [ j ];
                    x [ j ] = x [ j+1];     // swapping
                    x [ j+1] = temp;
                    flag = true;
                }
            }
        }
    }

    public static Comparator<String> ALPHABETICAL_ORDER = (String str1, String str2)-> {
        int res = String.CASE_INSENSITIVE_ORDER.compare(str1, str2);
        if (res == 0) {
            res = str1.compareTo(str2);
        }
        return res;
    };
}
