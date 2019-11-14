package com.nelsontron.lockguy;

import org.bukkit.Location;
import org.bukkit.block.Chest;

public class Box {
    int x, y, z;
    String world;

    public Box() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.world = null;
    }

    public Box(String world, int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
    }

    public Box(Chest chest) {
        this.x = chest.getX();
        this.y = chest.getY();
        this.z = chest.getZ();
        this.world = chest.getWorld().getName();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public String getWorld() {
        return world;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public boolean isLocation(Location location) {
        boolean isX = x == location.getBlockX();
        boolean isY = y == location.getBlockY();
        boolean isZ = z == location.getBlockZ();
        boolean isWorld = world.equalsIgnoreCase(location.getWorld().getName());

        return isX && isY && isZ && isWorld;
    }

    public void fromString(String str) {

        str = str.replace("Box={", "").replace("}", "");

        int x = 0, y = 0, z = 0;
        String world = null;
        String[] data;
        String[] keySet;

        data = str.split(",");

        for (String s : data) {

            keySet = s.split("=");

            try {

                if (keySet[0].equalsIgnoreCase("x"))
                    x = Integer.parseInt(keySet[1]);
                if (keySet[0].equalsIgnoreCase("y"))
                    y = Integer.parseInt(keySet[1]);
                if (keySet[0].equalsIgnoreCase("z"))
                    z = Integer.parseInt(keySet[1]);
                if (keySet[0].equalsIgnoreCase("world"))
                    world = keySet[1];

            } catch (NumberFormatException ex) {}
        }

        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
    }

    @Override
    public String toString() {
        String str;

        str = "Box={"
                + "x=" + x + ","
                + "y=" + y + ","
                + "z=" + z + ","
                + "world=" + world
                + "}";

        return str;
    }
}
