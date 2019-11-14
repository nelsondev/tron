package com.nelsontron.siege.listeners;

import com.nelsontron.siege.entity.Fighter;
import com.nelsontron.siege.game.Holder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MovementListener implements Listener {
    Holder holder;

    public MovementListener(Holder holder) {
        this.holder = holder;
    }

    @EventHandler
    public void stopPlayerMovement(PlayerMoveEvent ev) {
        Player player = ev.getPlayer();
        Fighter fighter = holder.getFighter(player);
        if (fighter.isAllowedToMove()) return;
        boolean isMovingToNextBlock = (ev.getTo().getBlockX() != ev.getFrom().getBlockX())
                || (ev.getTo().getBlockZ() != ev.getFrom().getBlockZ());

        if (isMovingToNextBlock)
            ev.setCancelled(true);
    }
}
