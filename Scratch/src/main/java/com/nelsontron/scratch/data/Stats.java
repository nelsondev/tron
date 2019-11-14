package com.nelsontron.scratch.data;

import com.nelsontron.scratch.Scratch;
import com.nelsontron.scratch.ScratcherHolder;
import com.nelsontron.scratch.listeners.stats.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Stats {
    private int kills;
    private int deaths;
    private int mobKills;
    private int damageTaken;
    private int damageGiven;
    private int blocksPlaced;
    private int blocksDestroyed;
    private int blocksTravelled;
    private int fished;
    private int invOpened;
    private int brokenItems;
    private int consumedItems;
    private int pickedUpItems;
    private int craftedItems;
    private int levels;
    private int exp;

    public Stats() {
        this.kills = 0;
        this.deaths = 0;
        this.mobKills = 0;
        this.damageTaken = 0;
        this.damageGiven = 0;
        this.blocksPlaced = 0;
        this.blocksDestroyed = 0;
        this.blocksTravelled = 0;
        this.fished = 0;
        this.invOpened = 0;
        this.brokenItems = 0;
        this.consumedItems = 0;
        this.pickedUpItems = 0;
        this.craftedItems = 0;
        this.levels = 0;
        this.exp = 0;
    }

    // getters
    public int getKills() {
        return kills;
    }
    public int getDeaths() {
        return deaths;
    }
    public int getMobKills() {
        return mobKills;
    }
    public int getDamageTaken() {
        return damageTaken;
    }
    public int getDamageGiven() {
        return damageGiven;
    }
    public int getBlocksPlaced() {
        return blocksPlaced;
    }
    public int getBlocksDestroyed() {
        return blocksDestroyed;
    }
    public int getBlocksTravelled() {
        return blocksTravelled;
    }
    public int getFished() {
        return fished;
    }
    public int getInvOpened() {
        return invOpened;
    }
    public int getBrokenItems() {
        return brokenItems;
    }
    public int getConsumedItems() {
        return consumedItems;
    }
    public int getPickedUpItems() {
        return pickedUpItems;
    }
    public int getCraftedItems() {
        return craftedItems;
    }
    public int getLevels() {
        return levels;
    }
    public int getExp() {
        return exp;
    }
    public TreeMap<String, Integer> prettyMap() {
        TreeMap<String, Integer> map = new TreeMap<>();
        map.put("kills", this.kills);
        map.put("deaths", this.deaths);
        map.put("mob kills", this.mobKills);
        map.put("damage taken", this.damageTaken);
        map.put("damage given", this.damageGiven);
        map.put("blocks placed", this.blocksPlaced);
        map.put("blocks destroyed", this.blocksDestroyed);
        map.put("blocks travelled", this.blocksTravelled);
        map.put("lures thrown", this.fished);
        map.put("chests opened", this.invOpened);
        map.put("broken items", this.brokenItems);
        map.put("consumed items", this.consumedItems);
        map.put("picked up items", this.pickedUpItems);
        map.put("crafted items", this.craftedItems);
        map.put("total levels", this.levels);
        map.put("total exp", this.exp);
        return map;
    }

    // setters
    public void setKills(int kills) {
        this.kills = kills;
    }
    public void incrementKills() { this.kills += 1; }
    public void incrementKills(int amount) { this.kills += amount; }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }
    public void incrementDeaths() { this.deaths += 1; }
    public void incrementDeaths(int amount) { this.deaths += amount; }

    public void setMobKills(int mobKills) {
        this.mobKills = mobKills;
    }
    public void incrementMobKills() { this.mobKills += 1; }
    public void incrementMobKills(int amount) { this.mobKills += amount; }

    public void setDamageTaken(int damageTaken) {
        this.damageTaken = damageTaken;
    }
    public void incrementDamageTaken() { this.damageTaken += 1; }
    public void incrementDamageTaken(int amount) { this.damageTaken += amount; }

    public void setDamageGiven(int damageGiven) {
        this.damageGiven = damageGiven;
    }
    public void incrementDamageGiven() { this.damageGiven += 1; }
    public void incrementDamageGiven(int amount) { this.damageGiven += amount; }

    public void setBlocksPlaced(int blocksPlaced) {
        this.blocksPlaced = blocksPlaced;
    }
    public void incrementBlocksPlaced() { this.blocksPlaced += 1; }
    public void incrementBlocksPlaced(int amount) { this.blocksPlaced += amount; }

    public void setBlocksDestroyed(int blocksDestroyed) {
        this.blocksDestroyed = blocksDestroyed;
    }
    public void incrementBlocksDestroyed() { this.blocksDestroyed += 1; }
    public void incrementBlocksDestroyed(int amount) { this.blocksDestroyed += amount; }

    public void setBlocksTravelled(int blocksTravelled) {
        this.blocksTravelled = blocksTravelled;
    }
    public void incrementBlocksTravelled() { this.blocksTravelled += 1; }
    public void incrementBlocksTravelled(int amount) { this.blocksTravelled += amount; }

    public void setFished(int fished) {
        this.fished = fished;
    }
    public void incrementFished() { this.fished += 1; }
    public void incrementFished(int amount) { this.fished += amount; }

    public void setInvOpened(int invOpened) {
        this.invOpened = invOpened;
    }
    public void incrementInventoryOpened() { this.invOpened += 1; }
    public void incrementInventoryOpened(int amount) { this.invOpened += amount; }

    public void setBrokenItems(int brokenItems) {
        this.brokenItems = brokenItems;
    }
    public void incrementBrokenItems() { this.brokenItems += 1; }
    public void incrementBrokenItems(int amount) { this.brokenItems += amount; }

    public void setConsumedItems(int consumedItems) {
        this.consumedItems = consumedItems;
    }
    public void incrementConsumedItems() { this.consumedItems += 1; }
    public void incrementConsumedItems(int amount) { this.consumedItems += amount; }

    public void setPickedUpItems(int pickedUpItems) {
        this.pickedUpItems = pickedUpItems;
    }
    public void incrementPickedUpItems() { this.pickedUpItems += 1; }
    public void incrementPickedUpItems(int amount) { this.pickedUpItems += amount; }

    public void setCraftedItems(int craftedItems) {
        this.craftedItems = craftedItems;
    }
    public void incrementCraftedItems() { this.craftedItems += 1; }
    public void incrementCraftedItems(int amount) { this.craftedItems += amount; }

    public void setLevels(int levels) {
        this.levels = levels;
    }
    public void incrementLevels() { this.levels++; }
    public void incrementLevels(int amount) { this.levels += amount; }

    public void setExp(int exp) {
        this.exp = exp;
    }
    public void incrementExp() { this.exp++; }
    public void incrementExp(int amount) { this.exp += amount; }

    // methods
    public Map<String, Object> serialize() {
        Map<String, Object> result = new HashMap<>();
        result.put("kills", this.kills);
        result.put("deaths", this.deaths);
        result.put("mobKills", this.mobKills);
        result.put("damageTaken", this.damageTaken);
        result.put("damageGiven", this.damageGiven);
        result.put("blocksPlaced", this.blocksPlaced);
        result.put("blocksDestroyed", this.blocksDestroyed);
        result.put("blocksTravelled", this.blocksTravelled);
        result.put("fished", this.fished);
        result.put("invOpened", this.invOpened);
        result.put("brokenItems", this.brokenItems);
        result.put("consumedItems", this.consumedItems);
        result.put("pickedUpItems", this.pickedUpItems);
        result.put("craftedItems", this.craftedItems);
        result.put("levels", this.levels);
        result.put("exp", this.exp);
        return result;
    }

    public Stats deserialize(Map<String, Object> args) {

        if (args.containsKey("kills"))
            this.kills = (Integer) args.get("kills");
        if (args.containsKey("deaths"))
            this.deaths = (Integer) args.get("deaths");
        if (args.containsKey("mobKills"))
            this.mobKills = (Integer) args.get("mobKills");
        if (args.containsKey("damageTaken"))
            this.damageTaken = (Integer) args.get("damageTaken");
        if (args.containsKey("damageGiven"))
            this.damageGiven = (Integer) args.get("damageGiven");
        if (args.containsKey("blocksPlaced"))
            this.blocksPlaced = (Integer) args.get("blocksPlaced");
        if (args.containsKey("blocksDestroyed"))
            this.blocksDestroyed = (Integer) args.get("blocksDestroyed");
        if (args.containsKey("blocksTravelled"))
            this.blocksTravelled = (Integer) args.get("blocksTravelled");
        if (args.containsKey("fished"))
            this.fished = (Integer) args.get("fished");
        if (args.containsKey("invOpened"))
            this.invOpened = (Integer) args.get("invOpened");
        if (args.containsKey("brokenItems"))
            this.brokenItems = (Integer) args.get("brokenItems");
        if (args.containsKey("consumedItems"))
            this.consumedItems = (Integer) args.get("consumedItems");
        if (args.containsKey("pickedUpItems"))
            this.pickedUpItems = (Integer) args.get("pickedUpItems");
        if (args.containsKey("craftedItems"))
            this.craftedItems = (Integer) args.get("craftedItems");
        if (args.containsKey("levels"))
            this.levels = (Integer) args.get("levels");
        if (args.containsKey("exp"))
            this.exp = (Integer) args.get("exp");

        return this;
    }

    public Stats fromList(List<Integer> list) {

        this.kills = list.get(0);
        this.deaths = list.get(1);
        this.mobKills = list.get(2);
        this.damageTaken = list.get(3);
        this.damageGiven = list.get(4);
        this.blocksPlaced = list.get(5);
        this.blocksDestroyed = list.get(6);
        this.blocksTravelled = list.get(7);
        this.fished = list.get(8);
        this.invOpened = list.get(9);
        this.brokenItems = list.get(10);
        this.consumedItems = list.get(11);
        this.pickedUpItems = list.get(12);
        this.craftedItems = list.get(13);
        this.levels = list.get(14);
        this.exp = list.get(15);

        return this;
    }

    @Override
    public String toString() {
        return "Stats{" + kills + ","
                + deaths + ","
                + mobKills + ","
                + damageTaken + ","
                + damageGiven + ","
                + blocksPlaced + ","
                + blocksDestroyed + ","
                + blocksTravelled + ","
                + fished + ","
                + invOpened + ","
                + brokenItems + ","
                + consumedItems + ","
                + pickedUpItems + ","
                + craftedItems + ","
                + levels + ","
                + exp + "}";
    }

    public Stats fromString(String string) {

        string = string.substring(4, string.length() - 1);

        String[] args = string.split(",");

        for (int i = 0; i < args.length; i++) {

            if (i == 0)
                kills = Integer.parseInt(args[i]);
            else if (i == 1)
                deaths = Integer.parseInt(args[i]);
            else if (i == 2)
                mobKills = Integer.parseInt(args[i]);
            else if (i == 3)
                damageTaken = Integer.parseInt(args[i]);
            else if (i == 4)
                damageGiven = Integer.parseInt(args[i]);
            else if (i == 5)
                blocksPlaced = Integer.parseInt(args[i]);
            else if (i == 6)
                blocksDestroyed = Integer.parseInt(args[i]);
            else if (i == 7)
                blocksTravelled = Integer.parseInt(args[i]);
            else if (i == 8)
                fished = Integer.parseInt(args[i]);
            else if (i == 9)
                invOpened = Integer.parseInt(args[i]);
            else if (i == 10)
                brokenItems = Integer.parseInt(args[i]);
            else if (i == 11)
                consumedItems = Integer.parseInt(args[i]);
            else if (i == 12)
                pickedUpItems = Integer.parseInt(args[i]);
            else if (i == 13)
                craftedItems = Integer.parseInt(args[i]);
            else if (i == 14)
                levels = Integer.parseInt(args[i]);
            else if (i == 15)
                exp = Integer.parseInt(args[i]);
            else
                System.out.println(args[i] + " DOESN'T MATCH ANY DATA.");

        }

        return this;
    }

    // static methods
    /**
     * REGISTER EVENTS
     *
     * register all the events related to this class through a static function.
     *
     * @param scratch - needed to grab the server instance as well as to register events the certain plugin scratch
     * @param holder - needed as extension of scratch. Holds all player data objects in the scratch instance.
     */
    public static void registerEvents(Scratch scratch, ScratcherHolder holder) {
        if (scratch.getConfig().getBoolean("scratch.track.stats")) {
            scratch.getServer().getPluginManager().registerEvents(new BlockDestroyed(holder), scratch);
            scratch.getServer().getPluginManager().registerEvents(new BlockPlaced(holder), scratch);
            scratch.getServer().getPluginManager().registerEvents(new BlockTravelled(holder), scratch);
            scratch.getServer().getPluginManager().registerEvents(new BrokenItem(holder), scratch);
            scratch.getServer().getPluginManager().registerEvents(new ConsumedItem(holder), scratch);
            scratch.getServer().getPluginManager().registerEvents(new CraftedItem(holder), scratch);
            scratch.getServer().getPluginManager().registerEvents(new DamageGiven(holder), scratch);
            scratch.getServer().getPluginManager().registerEvents(new DamageTaken(holder), scratch);
            scratch.getServer().getPluginManager().registerEvents(new ExpUp(holder), scratch);
            scratch.getServer().getPluginManager().registerEvents(new Fished(holder), scratch);
            scratch.getServer().getPluginManager().registerEvents(new InventoryOpened(holder), scratch);
            scratch.getServer().getPluginManager().registerEvents(new LeveledUp(holder), scratch);
            scratch.getServer().getPluginManager().registerEvents(new MobKill(holder), scratch);
            scratch.getServer().getPluginManager().registerEvents(new PickedUpItem(holder), scratch);
            scratch.getServer().getPluginManager().registerEvents(new PlayerDeath(holder), scratch);
            scratch.getServer().getPluginManager().registerEvents(new PlayerKill(holder), scratch);
        }
    }
}
