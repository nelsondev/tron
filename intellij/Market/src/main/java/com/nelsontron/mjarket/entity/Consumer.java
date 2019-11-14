package com.nelsontron.mjarket.entity;

import com.nelsontron.mjarket.Mjarket;
import com.nelsontron.mjarket.exceptions.MjarketCommandException;
import com.nelsontron.mjarket.utils.Data;
import com.nelsontron.mjarket.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Consumer {

    /**
     * CONSUMER OBJECT >
     * <p>
     * this object holds the data needed for the global and local listing
     * market for this plugin. It defines things such as
     *
     * <data> holds the configuration section where the player is stored. </data>
     * <uuid> holds the player UUID instead of player name because those
     * can be changed in minecraft. </uuid>
     * <doubloons> holds the double value of the current currency the player has </doubloons>
     * <listings> holds the current mjarket items the player has listed. </listings>
     */

    private ConsumerData consumerData;
    private Data data;
    private UUID uuid;
    private float doubloons;
    private List<Listing> listings;
    private List<Integer> services;
    private float activity;
    private float schedule;
    private int afkTimer;
    private final int AFK_TIMER_MAX = 600;
    private boolean isAfk;
    private boolean isNotified;

    // default constructor
    public Consumer(ConsumerData consumerData, UUID uuid) {
        this.uuid = uuid;
        this.doubloons = 0;
        this.listings = new ArrayList<>();
        this.services = new ArrayList<>();
        this.activity = 0;
        this.afkTimer = AFK_TIMER_MAX;
        this.isAfk = false;
        this.data = consumerData.createConsumerData(this);
        consumerData.scheduleAfkTimer(this);
    }

    // return data section the player resides in.
    public Data getData() {
        return data;
    }
    public UUID getUUID() {
        return uuid;
    }
    public Player getPlayer() { return Bukkit.getPlayer(uuid); }
    public float getDoubloons() {
        return doubloons;
    }
    public float getDoub() {
        return doubloons;
    }
    public List<Integer> getServices() { return services; }
    public float getActivity() { return activity; }
    public int getAfkTimer() {
        return afkTimer;
    }
    public float getSchedule() { return schedule; }
    public boolean isAfk() { return isAfk; }
    public boolean isNotified() { return isNotified; }
    public boolean hasDoubloons(float doubloons) throws MjarketCommandException {
        doubloons = Math.abs(doubloons);
        if (this.doubloons >= doubloons)
            return true;
        else
            throw new MjarketCommandException(ChatColor.RED + "> You need " + (doubloons-this.doubloons) + " more doubloons to do that.");
    }

    public List<Listing> getListings() { return listings; }
    public List<Listing> getListings(Material type) {
        List<Listing> listings = new ArrayList<>();
        for (Listing l : this.listings) {
            if (l.getItemStack().getType() == type)
                listings.add(l);
        }
        return listings;
    }
    public List<Listing> getListings(String type) {
        List<Listing> listings = new ArrayList<>();
        for (Listing l : this.listings) {
            if (l.getItemStack().getType().toString().contains(type) || l.getItemStack().getType().toString().equalsIgnoreCase(type))
                listings.add(l);
        }
        return listings;
    }
    public List<Listing> getListings(Double price) {
        List<Listing> result = new ArrayList<>();
        for (Listing l : listings) {
            if (l.getCost() == price)
                result.add(l);
        }
        return result;
    }
    public List<Listing> getListings(Double price, Material type) {
        List<Listing> result = new ArrayList<>();
        for (Listing l : listings) {
            if (l.getRelativeCost().equals(price) && l.getItemStack().getType() == type)
                result.add(l);
        }
        return result;
    }
    public List<Listing> getListings(Double price, String type) {
        List<Listing> result = new ArrayList<>();
        for (Listing l : listings) {
            if (l.getRelativeCost().equals(price) && l.getItemStack().getType().toString().contains(type) || l.getItemStack().getType().toString().equalsIgnoreCase(type))
                result.add(l);
        }
        return result;
    }
    public List<Listing> getEnchantedListings() {
        List<Listing> list = new ArrayList<>();
        for (Listing l : listings) {
            if (!l.getItemStack().getEnchantments().isEmpty())
                list.add(l);
        }
        return list;
    }
    public Listing getListing(String id) {
        Listing listing = null;
        for (Listing l : listings) {
            if (l.getID().equalsIgnoreCase(id))
                listing = l;
        }
        return listing;
    }
    public List<Double> getPriceList() {
        List<Double> list = new ArrayList<>();
        for (Listing l : listings) {
            list.add(l.getRelativeCost());
        }
        return list;
    }
    public List<Double> getPriceList(Material type) {
        List<Double> list = new ArrayList<>();
        for (Listing l : listings) {
            if (l.getItemStack().getType() == type)
                list.add(l.getRelativeCost());
        }
        return list;
    }
    public List<Double> getPriceList(String type) {
        List<Double> list = new ArrayList<>();
        for (Listing l : listings) {
            if (l.getItemStack().getType().toString().contains(type) || l.getItemStack().getType().toString().equalsIgnoreCase(type))
                list.add(l.getRelativeCost());
        }
        return list;
    }

    public void setDoubloons(float doubloons) {
        this.doubloons = doubloons;
    }
    public void setDoub(float doubloons) {
        this.doubloons = doubloons;
    }
    public void addDoubloons(float doubloons) { this.doubloons += doubloons; }
    public void addDoubloons(String str) throws MjarketCommandException {
        float doubloons;
        try {
            doubloons = Float.parseFloat(str);
            doubloons = Utils.roundDecimal(doubloons);
            doubloons = Math.abs(doubloons);
            addDoubloons(doubloons);
        } catch (NumberFormatException ex) {
            throw new MjarketCommandException(ChatColor.RED + "> Please define a integer or decimal price. Got " + str + ".");
        }
    }
    public void subtractDoubloons(float doubloons) { this.doubloons -= doubloons; }
    public void subtractDoubloons(String str) throws MjarketCommandException {
        float doubloons;
        DecimalFormat df = new DecimalFormat("#.##");
        try {
            doubloons = Float.parseFloat(str);
            doubloons = Utils.roundDecimal(doubloons);
            doubloons = -Math.abs(doubloons);
            addDoubloons(doubloons);
        } catch (NumberFormatException ex) {
            throw new MjarketCommandException(ChatColor.RED + "> Please define a integer or decimal price. Got " + str + ".");
        }
    }

    public void setPlayer(UUID uuid) {
        this.uuid = uuid;
    }
    public void setActivity(float activity) {
        this.activity = activity;
    }
    public void addActivity(float activity) { this.activity += activity; }
    public void removeActivity(float activity) { this.activity -= activity; }
    public void setAfkTimer(int afkTimer) {
        this.afkTimer = afkTimer;
    }
    public void setSchedule(float schedule) { this.schedule = schedule; }
    public void setAfk(boolean bool) { this.isAfk = bool; }
    public void setNotified(boolean bool) { this.isNotified = bool; }

    public Listing createListing(ItemStack item, Double price) {
        return new Listing();
    }

    /**
     * Write player-specific data to mjarket-data file for data
     * retention.
     */
    private void saveDoubloons() {
        try {
            // set section in configuration section. If "data" throws
            // a null pointer, setup the player.
            data.getData().set("doubloons", doubloons);
            data.save();

        } catch (final NullPointerException ex) {
            setup(); // if this fails, setup the player.
            saveDoubloons(); // call this function back again.
        }
    }
    /**
     * Access the mjarket-data file and grab data to re-load the
     * doubloons double with it's respectable player-specific
     * value.
     *
     * @return - returns a boolean to check if the load was successful.
     * If it wasn't successful the function returns false so the
     * load() function can handle it.
     */
    private void loadDoubloons() {
        try {
            doubloons = ((Double)data.getData().getDouble("doubloons")).floatValue();
        } catch (final NullPointerException ex) {
            setup();
            loadDoubloons();
        }
    }

    /**
     * Write player-specific data to mjarket-data file for data
     * retention.
     */
    private List<Map<String,Object>> itemsToMap() {
        List<Map<String,Object>> list = new ArrayList<>();
        for (Listing listing : listings) {
            list.add(listing.serialize());
        }
        return list;
    }
    private void saveItems() {
        try {
            // foreach mjarketItem loop to write each mjarket items
            // data into the mjarket-data file for server data retention.
            data.getData().set("listings", itemsToMap());
            data.save(); // save mjarket-data.yml
        } catch (final NullPointerException ex) {
            setup(); // if this fails, setup the player.
            saveItems(); // call this function again.
        }
    }
    /**
     * Access the mjarket-data file and grab data to re-load the
     * listings list with it's respectable player-specific
     * entity.
     *
     * @return - returns a boolean to check if the load was successful.
     * If it wasn't successful the function returns false so the
     * load() function can handle it.
     */
    private void loadItems() {
        try {
            if (data.getData().get("listings") == null) return;
            List<Map<String, Object>> list = (List<Map<String, Object>>) data.getData().get("listings");

            for (Map<String, Object> map : list) {
                Listing listing = new Listing().deserialize(map);
                listings.add(listing);
            }

        } catch (final NullPointerException ex) {
            setup();
            loadItems();
        }
    }

    private void saveServices() {
        data.getData().set("services", services);
        data.save();
    }
    private void loadServices() {
        services = data.getData().getIntegerList("services");
    }

    private void saveActivity() {
        data.getData().set("activity", activity);
        data.save();
    }
    private void loadActivity() {
        activity = data.getData().getInt("activity");
    }

    /**
     * Save the default configuration section, then set the "data" Consumer
     * variable so we're able to save and load the player from a mjarket-data
     * YML file.
     */
    private void setup() {

        // write default consumer data to the mjarket-data file
        data.getData().set("doubloons", 0.0);
        data.getData().set("listings",
                new ArrayList<Listing>().toString());
        data.getData().set("services",
                new ArrayList<Listing>().toString());
        data.getData().set("activity", 0);
        data.save(); // save mjarket-data.yml
    }
    /**
     * Call respective save functions for all data points that need retention.
     * Each function has a built in callback for "setup" if they fail, so
     * they're auto-correcting.
     */
    public void save() {
        saveDoubloons();
        saveItems();
        saveServices();
        saveActivity();
    }
    /**
     * Call respective load functions for all data points that need retention.
     * Each function has a built in callback for "setup" if they fail, so
     * they're auto-correcting.
     */
    public void load() {
        loadDoubloons();
        loadItems();
        loadServices();
        loadActivity();
    }
}