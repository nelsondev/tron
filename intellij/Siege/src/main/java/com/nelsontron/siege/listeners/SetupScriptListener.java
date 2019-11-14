package com.nelsontron.siege.listeners;

import com.nelsontron.siege.game.Holder;
import com.nelsontron.siege.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class SetupScriptListener implements Listener {

    Holder holder;

    public SetupScriptListener(Holder holder) {
        this.holder = holder;
    }

    @EventHandler
    public void playerSend(PlayerCommandPreprocessEvent ev) {
        Player player = ev.getPlayer();

        if (holder.getCreator(player) == null || ev.getMessage().contains("/game")) return;

        player.sendMessage(Utils.formatSiegeMessage("Please finish the siege setup script or type /game quit."));
        ev.setCancelled(true);
    }
}
