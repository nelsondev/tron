package com.nelsontron.mjarket.commands.sub;

import com.nelsontron.mjarket.entity.Consumer;
import com.nelsontron.mjarket.entity.Government;
import com.nelsontron.mjarket.entity.Listing;
import com.nelsontron.mjarket.exceptions.MjarketCommandException;
import com.nelsontron.mjarket.utils.Utils;
import com.nelsontron.scratch.utils.Chatils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class ListSubCommand {
    /**
     * START SELL COMMAND > sell command is used by /market sell <price>. It
     * takes the players item global (kijiji like) in hand and uses the <price>
     * argument to put it on the marketplace. All this does is add a new mjarket
     * item to the players items list.
     */

    private Government government;
    private Consumer consumer;
    private String[] parameters;

    public ListSubCommand(Government government, Consumer consumer, String[] parameters) {
        this.government = government;
        this.consumer = consumer;
        this.parameters = parameters;
    }

    /**
     * @return String
     */
    public String getMessage() throws MjarketCommandException {
        String result;

        // sell help page.

        Player player = Utils.consumerToPlayer(consumer);

        if (parameters == null) {
            result = ChatColor.GOLD + "Listings" + ChatColor.GRAY + " sorted alphabetically:\n";

            List<Listing> sortedListings = new ArrayList<>();
            TreeSet<String> sortedListingNames = new TreeSet<>();

            for (Listing l : consumer.getListings()) {
                sortedListingNames.add(l.getItemStack().getType().toString());
            }

            for (String name : sortedListingNames) {
                for (Listing l : consumer.getListings()) {
                    if (l.getItemStack().getType().toString().equalsIgnoreCase(name))
                        sortedListings.add(l);
                }
            }

            for (Listing listing : sortedListings) {
                result += "    "
                        + ChatColor.GRAY
                        + ChatColor.ITALIC
                        + listing.getID()
                        + ChatColor.GRAY
                        + ": "
                        + ChatColor.UNDERLINE
                        + listing.getItemStack()
                        .getType()
                        .toString()
                        .replace("_", " ")
                        .toLowerCase()
                        + " x "
                        + listing.getItemStack().getAmount()
                        + ChatColor.GRAY + " for "
                        + ChatColor.GOLD
                        + listing.getCost()
                        + "d"
                        + ChatColor.GRAY
                        + ".\n";
            }

            return result;
        }
        else {
            String id = parameters[0].toUpperCase();
            Listing listing;

            // get listing by id
            listing = government.getConsumerData().getGlobalListing(id);
            if (listing != null) {

                Consumer seller = null;

                for (Consumer c : government.getConsumerData().getConsumers()) {
                    if (c.getListing(id) != null)
                        seller = c;
                }

                if (seller == null)
                    throw new MjarketCommandException(ChatColor.RED + "> No listings for that id have been found.");

                listing = seller.getListing(id);

                result = ChatColor.GRAY + "" + "Item" + ChatColor.GOLD + " " +  id + ChatColor.GRAY + ". Sold by " + Utils.consumerToPlayer(seller).getName() + ":\n"
                        + "    type: " + ChatColor.ITALIC + listing.getItemStack().getType().toString().replace("_", " ").toLowerCase() + " x " + listing.getItemStack().getAmount() + "\n"
                        + "    cost: " + ChatColor.GOLD + ChatColor.ITALIC + listing.getCost() + "d" + ChatColor.GRAY + "\n";

                if (!listing.getItemStack().getItemMeta().getDisplayName().isEmpty())
                    result += "    name: " + ChatColor.AQUA + ChatColor.ITALIC + listing.getItemStack().getItemMeta().getDisplayName() + ChatColor.GRAY + "\n";

                if (!listing.getItemStack().getEnchantments().isEmpty()) {
                    result += "    enchantments:\n";
                    for (Enchantment ench : listing.getItemStack().getEnchantments().keySet()) {
                        result += "        " + ChatColor.ITALIC + "+ " + ench.getName().toLowerCase().replace("_", " ") + ChatColor.BLUE +  ChatColor.ITALIC + " " + listing.getItemStack().getEnchantments().get(ench) + ChatColor.GRAY + "\n";
                    }
                }

                if (listing.getItemStack().getItemMeta() instanceof EnchantmentStorageMeta) {
                    result += "    enchantments:\n";
                    EnchantmentStorageMeta meta = (EnchantmentStorageMeta) listing.getItemStack().getItemMeta();
                    for (Enchantment ench : meta.getStoredEnchants().keySet()) {
                        result += "      " + ench.getName().toLowerCase().replace("_", " ") + ChatColor.BLUE + " " + meta.getStoredEnchants().get(ench) + ChatColor.GRAY + "\n";
                    }
                }
                return result;
            }

            id = id.toLowerCase();
            Player seller = Bukkit.getPlayer(id);
            if (seller != null) {
                Consumer sellerConsumer = government.getConsumerData().getConsumer(seller.getUniqueId());
                result = ChatColor.GOLD + "Listings" + ChatColor.GRAY + " for " + seller.getName() + ChatColor.GRAY + " sorted alphabetically:\n";

                List<Listing> sortedListings = new ArrayList<>();
                TreeSet<String> sortedListingNames = new TreeSet<>();

                for (Listing l : sellerConsumer.getListings()) {
                    sortedListingNames.add(l.getItemStack().getType().toString());
                }

                for (String name : sortedListingNames) {
                    for (Listing l : sellerConsumer.getListings()) {
                        if (l.getItemStack().getType().toString().equalsIgnoreCase(name))
                            sortedListings.add(l);
                    }
                }

                for (Listing l : sortedListings) {
                    listing = l;
                    result += "    "
                            + ChatColor.GRAY
                            + ChatColor.ITALIC
                            + listing.getID()
                            + ChatColor.GRAY
                            + ": "
                            + ChatColor.UNDERLINE
                            + listing.getItemStack()
                            .getType()
                            .toString()
                            .replace("_", " ")
                            .toLowerCase()
                            + " x "
                            + listing.getItemStack().getAmount()
                            + ChatColor.GRAY + " for "
                            + ChatColor.GOLD
                            + listing.getCost()
                            + "d"
                            + ChatColor.GRAY
                            + ".\n";
                }
                return result;
            }

            id = Utils.arrayToString(parameters, "_").toUpperCase();

            List<Consumer> sellers = new ArrayList<>();
            List<Listing> sortedListings = new ArrayList<>();
            TreeSet<Double> sortedListingsPrice = new TreeSet<>();

            for (Consumer c : government.getConsumerData().getConsumers()) {
                if (!c.getListings(id).isEmpty())
                    sellers.add(c);
            }

            if (sellers.isEmpty())
                throw new MjarketCommandException(ChatColor.RED + "> Unable to find seller for " + id.toLowerCase() + ". Nothing has been listed for that yet.");

            for (Consumer c : sellers) {
                sortedListingsPrice.addAll(c.getPriceList(id));
            }

            System.out.println(sortedListingsPrice);

            for (Double p : sortedListingsPrice) {
                for (Consumer c : sellers) {
                    System.out.println(c.getListings(p, id));
                    sortedListings.addAll(c.getListings(p, id));
                }
            }

            if (sortedListings.isEmpty())
                throw new MjarketCommandException(ChatColor.RED + "> Unable to find listings for " + id.toLowerCase() + ". Nothing has been listed for that yet.");

            result = ChatColor.GOLD + id.replace("_", " ").toLowerCase() + ChatColor.GRAY + " listings sorted by lowest price.\n";

            for (Listing l : sortedListings) {
                if (l == null) continue;
                listing = l;

                result += "    " + ChatColor.GRAY + ChatColor.ITALIC + listing.getID() + ChatColor.GRAY + ": " + ChatColor.UNDERLINE
                        + listing.getItemStack().getType().toString().replace("_", " ").toLowerCase()
                        + " x " + listing.getItemStack().getAmount() + ChatColor.GRAY + " for " + ChatColor.GOLD + listing.getCost()
                        + "d - " + ChatColor.GOLD + listing.getRelativeCost() + ChatColor.GRAY + ChatColor.ITALIC + " each" + ChatColor.GRAY + ".\n";
            }

            return result;
        }
    }
}
