package com.nelsontron.mjarket.commands.sub;

import com.nelsontron.mjarket.entity.Consumer;
import com.nelsontron.mjarket.entity.Government;
import com.nelsontron.mjarket.entity.Listing;
import com.nelsontron.mjarket.exceptions.MjarketCommandException;
import com.nelsontron.mjarket.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

public class RemoveSubCommand {

    private Government government;
    private Consumer consumer;
    private String[] parameters;

    public RemoveSubCommand(Government government, Consumer consumer, String[] parameters) {
        this.government = government;
        this.consumer = consumer;
        this.parameters = parameters;
    }

    /**
     * @return String
     * @throws MjarketCommandException
     */
    public String getMessage() throws MjarketCommandException {
        String result;

        // buy command default help
        if (parameters == null || parameters.length == 0) {
            throw new MjarketCommandException(ChatColor.RED + "> You need to specify a lisitng ID.");
        } // send /market

        String ID = parameters[0].toUpperCase();
        Player player = Utils.consumerToPlayer(consumer);
        Listing listing = consumer.getListing(ID);

        if (listing != null) {

            if (player.getInventory().firstEmpty() == -1)
                throw new MjarketCommandException(ChatColor.RED + "> You have no room in your inventory to deposit the item.");

            player.getInventory().addItem(listing.getItemStack());
            consumer.getListings().remove(listing);

            result = ChatColor.GRAY
                    + "Removed listing "
                    + ChatColor.GOLD
                    + ID
                    + ChatColor.GRAY
                    + " and deposited the item "
                    + ChatColor.UNDERLINE
                    + listing.getItemStack()
                    .getType()
                    .toString()
                    .replace("_", " ")
                    .toLowerCase()
                    + " x "
                    + listing.getItemStack().getAmount()
                    + ChatColor.GRAY
                    + " back to your inventory.";
        }
        else {

            Material type = Material.getMaterial(Utils.arrayToString(parameters, "_").toUpperCase());

            if (type == null)
                throw new MjarketCommandException(ChatColor.RED + "> Unable to find item listing for " + Utils.arrayToString(parameters, " ") + ".");

            List<Listing> listings = consumer.getListings(type);

            if (listings.isEmpty())
                throw new MjarketCommandException(ChatColor.RED + "> Unable to find item listings for " + Utils.arrayToString(parameters, " ") + ".");

            for (Listing l : listings) {
                if (player.getInventory().firstEmpty() == -1)
                    throw new MjarketCommandException(ChatColor.RED + "> Not enough room in your inventory.");

                player.getInventory().addItem(l.getItemStack());
            }

            consumer.getListings().removeAll(listings);

            result = ChatColor.GRAY
                    + "Removed listings for "
                    + ChatColor.GOLD
                    + Utils.arrayToString(parameters, " ")
                    + ChatColor.GRAY
                    + " and deposited them back to your inventory.";
        }

        return result;
    }

}
