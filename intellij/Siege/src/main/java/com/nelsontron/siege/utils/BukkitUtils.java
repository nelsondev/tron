package com.nelsontron.siege.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class BukkitUtils {

    public static Map<String, Object> serializeLocation(Location location) {
        Map<String, Object> map = new HashMap<>();
        map.put("world", location.getWorld().getName());
        map.put("x", location.getBlockX());
        map.put("y", location.getBlockY());
        map.put("z", location.getBlockZ());
        return map;
    }

    public static Location deserializeLocation(Map<String, Object> map) {
        String world = "world";
        int x = 0, y = 0, z = 0;
        float yaw = 0, pitch = 0;

        if (map.containsKey("world"))
            world = (String) map.get("world");
        if (map.containsKey("x"))
            x = (Integer) map.get("x");
        if (map.containsKey("y"))
            y = (Integer) map.get("y");
        if (map.containsKey("z"))
            z = (Integer) map.get("z");
        if (map.containsKey("yaw"))
            yaw = ((Double)map.get("yaw")).floatValue();
        if (map.containsKey("pitch"))
            pitch = ((Double)map.get("pitch")).floatValue();

        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }

    public static ItemStack changeItemName(ItemStack item, String name) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack changeItemLore(ItemStack item, String ...lore) {
        ItemMeta meta = item.getItemMeta();

        List<String> l = new ArrayList<>();
        Collections.addAll(l, lore);

        meta.setLore(l);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack changeItemLore(ItemStack item, String lore) {
        ItemMeta meta = item.getItemMeta();

        List<String> l = new ArrayList<>();
        Collections.addAll(l, lore.split(";new"));

        meta.setLore(l);
        item.setItemMeta(meta);
        return item;
    }

    //
    // getPlayerDirection - Convert Player's Yaw into a human readable direction out of 16 possible.
    //
    public String getPlayerDirection(Player playerSelf){
        String dir = "";
        float y = playerSelf.getLocation().getYaw();
        if( y < 0 ){y += 360;}
        y %= 360;
        int i = (int)((y+8) / 22.5);
        if(i == 0){dir = "west";}
        else if(i == 1){dir = "west northwest";}
        else if(i == 2){dir = "northwest";}
        else if(i == 3){dir = "north northwest";}
        else if(i == 4){dir = "north";}
        else if(i == 5){dir = "north northeast";}
        else if(i == 6){dir = "northeast";}
        else if(i == 7){dir = "east northeast";}
        else if(i == 8){dir = "east";}
        else if(i == 9){dir = "east southeast";}
        else if(i == 10){dir = "southeast";}
        else if(i == 11){dir = "south southeast";}
        else if(i == 12){dir = "south";}
        else if(i == 13){dir = "south southwest";}
        else if(i == 14){dir = "southwest";}
        else if(i == 15){dir = "west southwest";}
        else {dir = "west";}
        return dir;
    }

    public static Material getRandomBlock() {
        List<Material> blocks = new ArrayList<>();
        for (Material block : Material.values()) {
            if (block.isBlock() && block.isSolid() && block.isOccluding()) {
                blocks.add(block);
            }
        }
        return blocks.get(new Random().nextInt(blocks.size()));
    }
}
