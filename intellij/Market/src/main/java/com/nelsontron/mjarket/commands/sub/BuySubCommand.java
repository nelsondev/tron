package com.nelsontron.mjarket.commands.sub;

import com.nelsontron.mjarket.entity.Consumer;
import com.nelsontron.mjarket.entity.Government;
import com.nelsontron.mjarket.entity.Listing;
import com.nelsontron.mjarket.exceptions.MjarketCommandException;
import com.nelsontron.mjarket.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class BuySubCommand {

    private Government government;
    private Consumer consumer;
    private Player player;
    private String[] parameters;
    private String id;
    private Listing listing;
    private String result;
    private Material type;

    public BuySubCommand(Government government, Consumer consumer, String[] parameters) {
        this.government = government;
        this.consumer = consumer;
        this.player = consumer.getPlayer();
        this.parameters = parameters;
    }

    private boolean isPrivateListing() {
        id = parameters[0].toUpperCase();
        listing = government.getConsumerData().getGlobalListing(id);

        if (listing == null)
            return false;
        else
            return true;
    }
    private String getPrivateListing() throws MjarketCommandException {
        Consumer seller = government.getConsumerData().getOwner(listing);

        // check if players inventory has room
        if (player.getInventory().firstEmpty() == -1)
            throw new MjarketCommandException(ChatColor.RED + "> Your inventory is full. Wouldn't be able to add item.");

        // check if player has enough doubloons
        consumer.hasDoubloons(listing.getCost().floatValue());

        player.getInventory().addItem(listing.getItemStack());
        consumer.subtractDoubloons(listing.getCost().floatValue());
        seller.addDoubloons(listing.getCost().floatValue());

        seller.getPlayer().sendMessage(ChatColor.GRAY + player.getName() + " purchased item " + ChatColor.ITALIC + id + "("
                + listing.getItemStack().getType().toString().replace("_", " ").toLowerCase()
                + " x " + listing.getItemStack().getAmount() + ") " + ChatColor.GRAY + "for " + ChatColor.GOLD + listing.getCost() + "d" + ChatColor.GRAY + ".");

        result = ChatColor.GRAY + "Purchased item " + ChatColor.ITALIC + id + "("
                + listing.getItemStack().getType().toString().replace("_", " ").toLowerCase()
                + " x " + listing.getItemStack().getAmount() + ") " + ChatColor.GRAY + "from " + seller.getPlayer().getName()
                + " for " + ChatColor.GOLD + listing.getCost() + "d" + ChatColor.GRAY + ".";

        // remove the listing from the seller
        seller.getListings().remove(listing);

        return result;
    }

    private List<Consumer> getSellers() throws MjarketCommandException {
        List<Consumer> result = new ArrayList<>();
        for (Consumer c : government.getConsumerData().getConsumers()) {
            if (!c.getListings(type).isEmpty())
                result.add(c);
        }

        if (result.isEmpty())
            throw new MjarketCommandException(ChatColor.RED + "> Unable to find seller for " + id.toLowerCase() + ". Nothing has been listed for that yet.");

        return result;
    }
    private List<Listing> getSortedListings(List<Consumer> sellers) throws MjarketCommandException {
        List<Listing> result = new ArrayList<>();
        TreeSet<Double> sortedListingsPrice = new TreeSet<>();

        for (Consumer c : sellers) {
            sortedListingsPrice.addAll(c.getPriceList(type));
        }

        for (Double p : sortedListingsPrice) {
            for (Consumer c : sellers) {
                System.out.println(c.getListings(p, type));
                result.addAll(c.getListings(p, type));
            }
        }

        if (result.isEmpty())
            throw new MjarketCommandException(ChatColor.RED + "> Unable to find listings for " + id.toLowerCase() + ". Nothing has been listed for that yet.");

        return result;
    }
    private String getItemStackListing() throws MjarketCommandException {

        id = Utils.arrayToString(parameters, "_").toUpperCase();
        type = Material.getMaterial(id);

        if (type == null)
            throw new MjarketCommandException(ChatColor.RED + "> Unable to find item listing for " + id.toLowerCase() + ".");

        /**
         * SORT ITEMS BY LOWEST RELATIVE COST.
         */
        // get seller list for item
        List<Consumer> sellers = getSellers();
        // get sorted listings by lowest price
        List<Listing> sortedListings = getSortedListings(sellers);
        // cheapest item is index 0
        listing = sortedListings.get(0);

        Consumer seller = government.getConsumerData().getOwner(listing);

        if (listing == null)
            throw new MjarketCommandException(ChatColor.RED + "> Unable to find item listing for " + id.toLowerCase() + ".");

        // check if players inventory is full
        if (player.getInventory().firstEmpty() == -1)
            throw new MjarketCommandException(ChatColor.RED + "> Your inventory is full. Wouldn't be able to add item.");

        // check if player has enough doubloons
        consumer.hasDoubloons(listing.getCost().floatValue());

        // add items and remove listing
        player.getInventory().addItem(listing.getItemStack());
        seller.getListings().remove(listing);

        consumer.subtractDoubloons(listing.getCost().floatValue());
        seller.addDoubloons(listing.getCost().floatValue());

        Utils.consumerToPlayer(seller).sendMessage(ChatColor.GRAY + player.getName() + " purchased item " + ChatColor.ITALIC + id + "("
                + listing.getItemStack().getType().toString().replace("_", " ").toLowerCase()
                + " x " + listing.getItemStack().getAmount() + ") " + ChatColor.GRAY + "for " + ChatColor.GOLD + listing.getCost() + "d" + ChatColor.GRAY + ".");

        result = ChatColor.GRAY + "Purchased lowest cost item " + ChatColor.ITALIC + id + "("
                + listing.getItemStack().getType().toString().replace("_", " ").toLowerCase()
                + " x " + listing.getItemStack().getAmount() + ") " + ChatColor.GRAY + "from " + Bukkit.getPlayer(seller.getUUID()).getName()
                + " for " + ChatColor.GOLD + listing.getCost() + "d" + ChatColor.GRAY + ".";

        return result;
    }

    /**
     * @return String
     */
    public String getMessage() throws MjarketCommandException {

        // buy command default help
        if (parameters == null || parameters.length == 0) {
            throw new MjarketCommandException(ChatColor.RED + "> You need to specify a listing id or item name.");
        } // send /market

        if (isPrivateListing())
            return getPrivateListing();
        else
            return getItemStackListing();
    }
}
