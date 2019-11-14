package com.nelsontron.siege.listeners;

import com.nelsontron.siege.Siege;
import com.nelsontron.siege.game.Holder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;

import java.util.HashMap;
import java.util.Map;

public class StaminaListener implements Listener {

    Holder holder;
    private Map<String, Integer> schedule;

    public StaminaListener(Holder holder) {
        this.holder = holder;
        this.schedule = new HashMap<>();
    }

    private void scheduleStaminaRemoval(Player player) {
        Siege siege = holder.getSiege();

        // if schedule contains player
        if (schedule.containsKey(player.getName()))
            // cancel pending task so it doesn't schedule multiple
            siege.getServer().getScheduler().cancelTask(schedule.get(player.getName()));

        // get schedule id for cancellation later
        int id = siege.getServer().getScheduler().scheduleSyncDelayedTask(siege, () -> {

            // if players food level is at its lowest
            if (player.getFoodLevel() <= 0.0)  {
                player.setSprinting(false);
                return;
            }
            // if player isn't sprinting return
            if (!player.isSprinting()) return;

            // minus 1/20 from players food
            player.setFoodLevel(player.getFoodLevel()-1);

            scheduleStaminaRemoval(player); // call function again
        }, 20);
        // put new schedule id into map
        schedule.put(player.getName(), id);
    }

    private void scheduleStaminaRegeneration(Player player) {
        Siege siege = holder.getSiege();

        if (schedule.containsKey(player.getName()))
            siege.getServer().getScheduler().cancelTask(schedule.get(player.getName()));

        int id = siege.getServer().getScheduler().scheduleSyncDelayedTask(siege, () -> {

            if (player.getFoodLevel() >= 20.0) return;
            if (player.isSprinting()) return;

            player.setFoodLevel(player.getFoodLevel() + 1);
            scheduleStaminaRegeneration(player);

        }, (player.isSneaking()) ? 5 : 7);
        schedule.put(player.getName(), id);
    }

    @EventHandler
    public void playerLoseStamina(PlayerToggleSprintEvent ev) {
        Player player = ev.getPlayer();

        if (!ev.isSprinting()) return;
        scheduleStaminaRemoval(player);
    }

    @EventHandler
    public void playerGainStamina(PlayerToggleSprintEvent ev) {
        Player player = ev.getPlayer();

        if (ev.isSprinting()) return;
        Siege siege = holder.getSiege();
        if (schedule.containsKey(player.getName()))
            siege.getServer().getScheduler().cancelTask(schedule.get(player.getName()));

        int id = holder.getSiege().getServer().getScheduler().scheduleSyncDelayedTask(holder.getSiege(), () -> {
            if (ev.isSprinting() ) return;
                scheduleStaminaRegeneration(player);
        }, 30);
        schedule.put(player.getName(), id);
    }

    @EventHandler
    public void playerSprintWhileEmpty(PlayerToggleSprintEvent ev) {
        Player player = ev.getPlayer();
        if (player.getFoodLevel() > 0.0) return;
        holder.getSiege().getServer().getScheduler().scheduleSyncDelayedTask(holder.getSiege(), () -> {
            player.setSprinting(false);
            ev.setCancelled(true);
        }, 1);
    }

    @EventHandler
    public void stopSaturation(FoodLevelChangeEvent ev) {
        if (!(ev.getEntity() instanceof Player)) return;
        Player player = (Player) ev.getEntity();
        player.setSaturation(20.0f);
        ev.setCancelled(true);
    }

    @EventHandler
    public void playerDodge(PlayerToggleFlightEvent ev) {
        Player player = ev.getPlayer();
        Siege siege = holder.getSiege();
        if (player.isSprinting()) return;

        if (schedule.containsKey(player.getName()))
            siege.getServer().getScheduler().cancelTask(schedule.get(player.getName()));

        int id = holder.getSiege().getServer().getScheduler().scheduleSyncDelayedTask(holder.getSiege(), () -> {
            if (player.isSprinting() ) return;
            scheduleStaminaRegeneration(player);
        }, 30);
        schedule.put(player.getName(), id);
    }

    @EventHandler
    public void playerDamaged(EntityDamageEvent ev) {
        Player player;
        if (!(ev.getEntity() instanceof  Player)) return;
        player = (Player) ev.getEntity();

        if (ev.getCause() == EntityDamageEvent.DamageCause.STARVATION)
            ev.setCancelled(true);

        if (player.isSprinting()) {
            player.setSprinting(false);
            return;
        }

        if (schedule.containsKey(player.getName()))
            holder.getSiege().getServer().getScheduler().cancelTask(schedule.get(player.getName()));

        int id = holder.getSiege().getServer().getScheduler().scheduleSyncDelayedTask(holder.getSiege(), () -> {
            if (player.isSprinting()) return;
            scheduleStaminaRegeneration(player);
        }, 40);
        schedule.put(player.getName(), id);

        player.setFoodLevel(player.getFoodLevel() - 2);
    }
}
