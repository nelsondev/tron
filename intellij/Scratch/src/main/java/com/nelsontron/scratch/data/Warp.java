package com.nelsontron.scratch.data;

import com.sun.istack.internal.NotNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.HashMap;
import java.util.Map;

public class Warp {
    private float X;
    private float Y;
    private float Z;
    private float pitch;
    private float yaw;
    private String world;
    private String name;
    private String category;

    public Warp() {
        this.world = null;
        this.name = null;
        this.category = null;
        this.X = 0;
        this.Y = 0;
        this.Z = 0;
        this.pitch = 0;
        this.yaw = 0;
    }
    public Warp(String world, float X, float Y, float Z, String name) {
        this.world = world;
        this.name = name;
        this.category = null;
        this.X = X;
        this.Y = Y;
        this.Z = Z;
        this.pitch = 0;
        this.yaw = 0;
    }
    public Warp(String world, float X, float Y, float Z, float pitch, float yaw, String name) {
        this.world = world;
        this.name = name;
        this.category = null;
        this.X = X;
        this.Y = Y;
        this.Z = Z;
        this.pitch = pitch;
        this.yaw = yaw;
    }
    public Warp(Location location, String name) {
        this.world = location.getWorld().getName();
        this.name = name;
        this.category = null;
        this.X = (float) location.getX();
        this.Y = (float) location.getY();
        this.Z = (float) location.getZ();
        this.pitch = location.getPitch();
        this.yaw = location.getYaw();
    }
    public Warp(String str) {
        this.fromString(str);
    }

    // getters
    public float getX() {
        return X;
    }
    public float getY() {
        return Y;
    }
    public float getZ() {
        return Z;
    }
    public float getPitch() {
        return pitch;
    }
    public float getYaw() {
        return yaw;
    }
    public World getWorld() { return Bukkit.getWorld(world); }
    public String getName() { return name; }
    public Location getLocation() {
        return new Location(Bukkit.getWorld(world), X, Y, Z, yaw, pitch);
    }
    public String getCategory() { return category; }

    // setters
    public void setX(float x) {
        X = x;
    }
    public void setY(float y) {
        Y = y;
    }
    public void setZ(float z) {
        Z = z;
    }
    public void setPitch(float pitch) {
        this.pitch = pitch;
    }
    public void setYaw(float yaw) {
        this.yaw = yaw;
    }
    public void setWorld(World world) { this.world = world.getName(); }
    public void setName(String name) { this.name = name; }
    public void setLocation(float X, float Y, float Z) {
        this.X = X;
        this.Y = Y;
        this.Z = Z;
    }
    public void setLocation(float X, float Y, float Z, float pitch, float yaw) {
        this.X = X;
        this.Y = Y;
        this.Z = Z;
        this.pitch = pitch;
        this.yaw = yaw;
    }
    public void setLocation(Location location) {
        this.world = location.getWorld().getName();
        this.X = (float) location.getX();
        this.Y = (float) location.getY();
        this.Z = (float) location.getZ();
        this.pitch = location.getPitch();
        this.yaw = location.getYaw();
    }
    public void setCategory(String str) { category = str; }

    // methods
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();

        if (name != null)
            map.put("name", name);
        if (world != null)
            map.put("world", world);
        if (category != null)
            map.put("category", category);
        map.put("x", X);
        map.put("y", Y);
        map.put("z", Z);
        map.put("pitch", pitch);
        map.put("yaw", yaw);

        return map;
    }
    public Warp deserialize(Map<String, Object> map) {

        if (map.containsKey("name"))
            name = (String) map.get("name");
        if (map.containsKey("world"))
            world = (String) map.get("world");
        if (map.containsKey("category"))
            category = (String) map.get("category");

        X = ((Double)map.get("x")).floatValue();
        Y = ((Double)map.get("y")).floatValue();
        Z = ((Double)map.get("z")).floatValue();
        pitch = ((Double)map.get("pitch")).floatValue();
        yaw = ((Double)map.get("yaw")).floatValue();

        return this;
    }

    public String toString() {
        String str = "";

        str += "Warp={";
        if (name!=null)
            str += "name=" + name + ",";
        if (world!=null)
            str += "world=" + world + ",";
        if(category!=null)
            str+= "category=" + category + ",";
        str += "x=" + X + ",";
        str += "y=" + Y + ",";
        str += "z=" + Z + ",";
        str += "yaw=" + yaw + ",";
        str += "pitch=" + pitch;
        str += "}";

        return str;
    }
    public Warp fromString(@NotNull String str) {

        if (!str.contains("Warp={"))
            return null;

        // remove the prefix and suffix to make it easier to parse.
        str = str.replace("Warp={", "").replace("}", "");

        // split by commas.
        String[] attributes = str.split(",");

        for (String s : attributes) {
            String[] individual = s.split("=");

            String key = individual[0];
            String val = individual[1];

            if (key.equals("name"))
                name = val;

            if (key.equals("world"))
                world = val;

            if (key.equals("category"))
                category = val;

            if (key.equals("x"))
                X = Float.parseFloat(val);

            if (key.equals("y"))
                Y = Float.parseFloat(val);

            if (key.equals("z"))
                Z = Float.parseFloat(val);

            if (key.equals("yaw"))
                yaw = Float.parseFloat(val);

            if (key.equals("pitch"))
                pitch = Float.parseFloat(val);
        }

        return this;
    }
}
