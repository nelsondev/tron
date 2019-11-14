package com.nelsontron.lockguy;

import com.nelsontron.lockguy.utils.Sort;
import com.nelsontron.mjarket.Mjarket;
import com.nelsontron.mjarket.entity.Consumer;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SortCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player;
        Consumer consumer;
        Chest chest;

        String result = null;

        try {

            player = (Player) sender;

            player = (Player) sender;
            consumer = Mjarket.getGovernment().getConsumerData().getConsumer(player.getUniqueId());
            Block target = player.getTargetBlock( 16);

            /**
             * MJARKET
             */
            if (consumer.getDoub() < Mjarket.CHEST_SORT_PRICE) {
                float remaining = Mjarket.CHEST_SORT_PRICE-consumer.getDoub();
                result = ChatColor.RED + "> You need " + remaining +" more doubloons.";
                return true;
            }

            if (target.getState() instanceof Chest) {
                chest = (Chest) target.getState();
            }
            else {
                result = ChatColor.RED + "> Player must be looking at a chest.";
                return true;
            }

            List<ItemStack> sorted = new ArrayList<>();

            for (ItemStack item : chest.getInventory().getContents()) {
                if (item == null) continue;
                sorted.add(item);
            }

            Sort sort = new Sort();
            sorted = sort.sortItemStackList(sorted);

            chest.getInventory().clear();

            for (ItemStack item : sorted)
                chest.getInventory().addItem(item);

            /**
             * MJARKET
             */
            consumer.setDoub(consumer.getDoub()-Mjarket.CHEST_SORT_PRICE);
            consumer.getServices().add(Mjarket.CHEST_SORT_PRICE);
            consumer.setActivity(consumer.getActivity() + 0.5f);

            result = ChatColor.GRAY + "Sorted chest.";

        } catch (ClassCastException ex) {
            result = ChatColor.RED + "> Sender must be a player.";
        } finally {
            if (result != null)
                sender.sendMessage(result);
        }


        return false;
    }
}
