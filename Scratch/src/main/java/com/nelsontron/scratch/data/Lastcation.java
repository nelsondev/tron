package com.nelsontron.scratch.data;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class Lastcation {
    int x;
    int y;
    int z;
    float pitch;
    float yaw;

    String world;

    public Lastcation() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.pitch = 0;
        this.yaw = 0;
        this.world = null;
    }

    public Lastcation(String world, int x, int y, int z, int pitch, int yaw) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
        this.world = world;
    }

    // create a lastcation from a player.
    public Lastcation(Player player) {
        this.x = player.getLocation().getBlockX();
        this.y = player.getLocation().getBlockY();
        this.z = player.getLocation().getBlockZ();
        this.pitch = player.getLocation().getPitch();
        this.yaw = player.getLocation().getYaw();
        this.world = player.getWorld().getName();
    }

    // create a lastcation from a player.
    public Lastcation(Location location) {
        this.x = location.getBlockX();
        this.y = location.getBlockY();
        this.z = location.getBlockZ();
        this.pitch = location.getPitch();
        this.yaw = location.getYaw();
        this.world = location.getWorld().getName();
    }

    // getters
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getZ() {
        return z;
    }
    public float getPitch() {
        return pitch;
    }
    public float getYaw() {
        return yaw;
    }
    public String getWorld() {
        return world;
    }
    public Location getLocation() { return new Location(Bukkit.getWorld(world), x+0.5, y+0.2, z+0.5, yaw, pitch); }

    // setters
    public void setLocation(Location location) {
        this.x = location.getBlockX();
        this.y = location.getBlockY();
        this.z = location.getBlockZ();
        this.pitch = location.getPitch();
        this.yaw = location.getYaw();
        this.world = location.getWorld().getName();
    }

    // methods
    public boolean isLocation(Location location) {
        if (location.getBlockX() == x && location.getBlockY() == y && location.getBlockZ() == z && location.getWorld().getName().equalsIgnoreCase(world))
            return true;
        else
            return false;
    }

    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("x", x);
        map.put("y", y);
        map.put("z", z);
        map.put("pitch", pitch);
        map.put("yaw", yaw);
        map.put("world", world);
        return map;
    }

    public Lastcation deserialize(Map<String, Object> map) {
        if (map.containsKey("x"))
            this.x = (Integer) map.get("x");
        if (map.containsKey("y"))
            this.y = (Integer) map.get("y");
        if (map.containsKey("z"))
            this.z = (Integer) map.get("z");
        if (map.containsKey("pitch"))
            this.pitch = ((Double)map.get("pitch")).floatValue();
        if (map.containsKey("yaw"))
            this.yaw = ((Double)map.get("yaw")).floatValue();
        if (map.containsKey("world"))
            this.world = (String) map.get("world");
        return this;
    }

    @Override
    public String toString() {
        return "Lastcation{"
                + "x=" + x + ","
                + "y=" + y + ","
                + "z=" + z + ","
                + "pitch=" + pitch + ","
                + "yaw=" + yaw + ","
                + "world=" + world
                + "}";
    }

    public Lastcation fromString(String string) {

        string = string.substring(9, string.length()-1);

        for (String map : string.split(",")) {
            String[] values = map.split("=");
            String key = values[0];
            String value = values[1];

            if (key.equalsIgnoreCase("x"))
                this.x = Integer.parseInt(value);
            else if (key.equalsIgnoreCase("y"))
                this.y = Integer.parseInt(value);
            else if (key.equalsIgnoreCase("z"))
                this.z = Integer.parseInt(value);
            else if (key.equalsIgnoreCase("pitch"))
                this.pitch = Float.parseFloat(value);
            else if (key.equalsIgnoreCase("yaw"))
                this.yaw = Float.parseFloat(value);
            else if (key.equalsIgnoreCase("world"))
                this.world = value;
        }

        return this;
    }
}
