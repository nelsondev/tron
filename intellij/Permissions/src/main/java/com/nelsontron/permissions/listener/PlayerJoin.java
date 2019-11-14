package com.nelsontron.permissions.listener;

import com.nelsontron.permissions.Perm;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    private Perm permissions;

    public PlayerJoin(Perm permissions) {
        this.permissions = permissions;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent ev) {

    }
}
