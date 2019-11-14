package com.nelsontron.siege.listeners.perks;

import com.nelsontron.siege.game.Holder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.util.Vector;
import org.bukkit.Location;

public class DodgeListener implements Listener {

    Holder holder;

    public DodgeListener(Holder holder) {
        this.holder = holder;
    }

    private boolean isPositiveNumber(int num) {
        if (num < 0)
            return false;
        else
            return true;
    }
    private boolean isPositiveNumber(float num) {

        if (num < 0.0)
            return false;
        else
            return true;
    }
    private boolean isPositiveNumber(double num) {

        if (num < 0.0)
            return false;
        else
            return true;
    }

    @EventHandler
    public void playerDodge(PlayerToggleFlightEvent ev) {
        Player player = ev.getPlayer();

        if (!ev.isFlying()) return;
        if (player.getFoodLevel() < 5) {
            player.setAllowFlight(false);
            player.setFlying(false);
            ev.setCancelled(true);
            return;
        }

        player.setAllowFlight(false);
        player.setFlying(false);

        Vector direction = player.getLocation().getDirection();
        Vector vec = player.getVelocity();

        double x = vec.getX() + (direction.getX()*0.35);
        double y = 0.35;
        double z = vec.getZ() + (direction.getZ()*0.35);

        player.setFoodLevel(player.getFoodLevel() - 5);
        player.setVelocity(new Vector(x, y, z));
    }

    @EventHandler
    public void playerLand(PlayerMoveEvent ev) {
        Player player = ev.getPlayer();

        if (!player.isOnGround()) return;
        player.setAllowFlight(true);
        player.setFlying(false);
    }
}
