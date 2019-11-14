package com.nelsontron.mjarket.commands.sub;

import com.nelsontron.mjarket.entity.Consumer;
import com.nelsontron.mjarket.entity.Government;
import com.nelsontron.mjarket.entity.Listing;
import com.nelsontron.mjarket.exceptions.MjarketCommandException;
import com.nelsontron.scratch.utils.Chatils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;

public class SellSubCommand {

    /**
     * START SELL COMMAND > sell command is used by /market sell <price>. It
     * takes the players item global (kijiji like) in hand and uses the <price>
     * argument to put it on the marketplace. All this does is add a new mjarket
     * item to the players items list.
     */

    private Government government;
    private Consumer consumer;
    private String[] parameters;
    private Chatils chatils;

    public SellSubCommand(Government government, Consumer consumer, String[] parameters) {
        this.government = government;
        this.consumer = consumer;
        this.parameters = parameters;
    }

    /**
     * Try to parse a double from a string. If it succeeds return the new parsed double as usual. If it fails then throw
     * custom exception "SellSubCommand" tp bnring up specific help for price. Handle in a catch statement later.
     *
     * @param price
     * @return double from string
     */
    private double resolvePrice(String price) throws MjarketCommandException {
        try {
            return Double.parseDouble(price); // throws // NumberFormatException

        } catch (final NumberFormatException ex) {

            throw new MjarketCommandException(ChatColor.RED + "Invalid price. Please specify a price.");
        }
    }

    /**
     * Try to get the players item in main hand. If it succeeds return item. If it fails throw custom exception
     * "SellSubCommand" with the index "ITEM" to bring up the specific help for items. Handle in catch statement later.
     *
     * @param itemStack
     * @return item stack from player
     */
    private ItemStack resolveItemStack(ItemStack itemStack) throws MjarketCommandException {
        ItemStack item;

        item = itemStack;

        if (item == null)
            throw new MjarketCommandException(ChatColor.RED + "> Invalid item. You must be holding a sell-able item.");

        return item;
    }

    public boolean isSellable(ItemStack itemStack) throws MjarketCommandException {
        boolean isCobble = itemStack.getType() == Material.COBBLESTONE;
        boolean isStone = itemStack.getType() == Material.ANDESITE;
        boolean isStone1 = itemStack.getType() == Material.GRANITE;
        boolean isStone2 = itemStack.getType() == Material.DIORITE;
        boolean isCraft = itemStack.getType() == Material.CRAFTING_TABLE;
        boolean isHopper = itemStack.getType() == Material.HOPPER;
        boolean isSmoker = itemStack.getType() == Material.SMOKER;
        boolean isChest = itemStack.getType() == Material.CHEST;
        boolean isFurnace = itemStack.getType() == Material.FURNACE;
        boolean isBlast = itemStack.getType() == Material.BLAST_FURNACE;
        boolean isSeeds = itemStack.getType() == Material.WHEAT_SEEDS;
        boolean isDirt = itemStack.getType() == Material.DIRT;
        boolean isCoarse = itemStack.getType() == Material.COARSE_DIRT;
        boolean isGravel = itemStack.getType() == Material.GRAVEL;
        boolean isSand = itemStack.getType() == Material.SAND;
        boolean isCobbleSlab = itemStack.getType() == Material.COBBLESTONE_SLAB;
        boolean isCobbleStair = itemStack.getType() == Material.COBBLESTONE_STAIRS;

        if (isCobble || isStone || isStone1 || isStone2 || isCraft || isHopper || isSmoker || isChest || isFurnace
            || isBlast || isSeeds || isDirt || isCoarse || isGravel || isSand || isCobbleSlab || isCobbleStair)
            throw new MjarketCommandException(ChatColor.RED + "> The item is deemed worthless and can't be sold on the market.");
        else
            return true;
    }

    /**
     * @return String
     */
    public String getMessage() throws MjarketCommandException {
        String result;
        Player player;
        ItemStack item;
        DecimalFormat df = new DecimalFormat("#.##");
        double price;
        Listing listing;

        if (parameters == null || parameters.length == 0) {
            throw new MjarketCommandException(ChatColor.RED + "> Please use this command as follows: /mjar sell <price>.");
        }

        player = consumer.getPlayer();
        item = resolveItemStack(player.getInventory().getItemInMainHand());
        price = Math.abs(Double.parseDouble(df.format(resolvePrice(parameters[0]))));
        listing = new Listing(government, item, price);

        // check if item is sell able
        isSellable(item);

        if (consumer.getListings().size() >= 32)
            throw new MjarketCommandException(ChatColor.RED + "> You have the max amount of listings.");

        // add item to consumers listed items.
        consumer.getListings().add(listing);
        // remove item from players hand.
        player.getInventory().setItemInMainHand(null);

        result = ChatColor.GRAY + "item "
                + ChatColor.GOLD + item.getType().toString().toLowerCase().replace("_", " ")
                + " x " + item.getAmount() + ChatColor.GRAY + " listed for " + ChatColor.GOLD + price + "d" + ChatColor.GRAY + ".";

        return result;
    }
}

