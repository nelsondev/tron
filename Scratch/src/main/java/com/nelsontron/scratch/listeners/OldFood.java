package com.nelsontron.scratch.listeners;

import com.nelsontron.scratch.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

public class OldFood implements Listener {

    // when item is eaten this event gets the item "item" and
    // gets the nutrition number.

    // note: setting health to OVER 20.0 results in an error,
    // so I have to check if the food amount + player health
    // is NOT over 20.0.
    @SuppressWarnings("deprecation")
    @EventHandler
    public void playerItemConsume(PlayerItemConsumeEvent ev) {

        // foodAmount is the item nutrition.
        int foodAmount = Utils.getFoodInfo(ev.getItem());
        double maxHealth = ev.getPlayer().getMaxHealth();
        double adjustedHealth = ev.getPlayer().getHealth() + foodAmount;

        // if player max health is over or equal to player adjusted health
        if (maxHealth >= adjustedHealth) {

            // you cant set health over 20.0 so this is fine
            ev.getPlayer().setHealth(adjustedHealth);
        } else {

            // this is not. Just set it to max health.
            ev.getPlayer().setHealth(maxHealth);
            ev.getPlayer().setFoodLevel(19);
            ev.getPlayer().setSaturation(20);
        }
    }

    // just a quick function to disable hunger changing eat da poopoo
    @EventHandler
    public void foodLevelChange(FoodLevelChangeEvent ev) {
        if (ev.getEntity() instanceof Player) {
            Player player = (Player) ev.getEntity();

            player.setFoodLevel(19);
            player.setSaturation(20);
            ev.setCancelled(true);
        }
    }

    // BEEEEG if statement
    @EventHandler
    public void entityRegainHealth(EntityRegainHealthEvent ev) {

        if (ev.getRegainReason() == EntityRegainHealthEvent.RegainReason.REGEN || ev.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED || ev.getRegainReason() == EntityRegainHealthEvent.RegainReason.EATING) {

            try {

                Player player = (Player) ev.getEntity();;
                player.setFoodLevel(19);
                player.setSaturation(20);
                ev.setCancelled(true);
            } catch (ClassCastException ex) {
            }
        }
    }
}
