package com.nelsontron.tronwarps.listeners;

import com.nelsontron.tronwarps.Tronwarps;
import com.nelsontron.tronwarps.Warper;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class RegisterPlayers implements Listener {

    Tronwarps tronwarps;

    public RegisterPlayers(Tronwarps tronwarps) {
        this.tronwarps = tronwarps;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent ev) {
        Warper warper = tronwarps.registerPlayer(ev.getPlayer());
        warper.load();

        ev.getPlayer().sendMessage(ChatColor.RED + "> PLEASE UPDATE YOUR WARPS.");
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent ev) {
        Warper warper = tronwarps.getWarper(ev.getPlayer());

        if (warper != null) {
            warper.save();
            tronwarps.removePlayer(warper);
        }
    }
}
