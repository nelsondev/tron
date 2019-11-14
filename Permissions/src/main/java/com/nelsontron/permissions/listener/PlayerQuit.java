package com.nelsontron.permissions.listener;

import com.nelsontron.permissions.Perm;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {

    private Perm permissions;

    public PlayerQuit(Perm permissions) {
        this.permissions = permissions;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent ev) {

    }
}
