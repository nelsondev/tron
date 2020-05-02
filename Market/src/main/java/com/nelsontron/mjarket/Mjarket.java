package com.nelsontron.mjarket;

import com.nelsontron.mjarket.commands.handler.BalanceHandler;
import com.nelsontron.mjarket.commands.handler.MjarketHandler;
import com.nelsontron.mjarket.commands.handler.PayHandler;
import com.nelsontron.mjarket.entity.Consumer;
import com.nelsontron.mjarket.entity.Government;
import com.nelsontron.mjarket.listener.ActivityListener;
import com.nelsontron.mjarket.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Mjarket extends JavaPlugin implements Listener {

    private static Government government;

    public static final int WARP_PRICE = 5;
    public static final int WARP_SET_PRICE = 300;
    public static final int PACK_CUSTOM_PRICE = 3200;
    public static final int PACK_CRAFT_PRICE = 2500;
    public static final int PACK_ENDER_PRICE = 5000;
    public static final int PACK_UPGRADE_SIZE_PRICE = 5000;
    public static final int PACK_UPGRADE_AUTO_SORT_PRICE = 10000;
    public static final int PACK_UPGRADE_AUTO_SMELT_PRICE = 10000;
    public static final int LOCK_PRICE = 250;
    public static final int BACK_PRICE = 10;
    public static final int STONE_CUTTER_PRICE = 2500;
    public static final int ENCHANTMENT_TABLE_PRICE = 4200;
    public static final int CHEST_SORT_PRICE = 10;

    @Override
    public void onEnable() {

        government = new Government(this);
        government.load();

        Utils.registerCommands(new MjarketHandler(government), this, "market", "m", "mjarket", "mjar");
        Utils.registerCommands(new PayHandler(government), this, "pay");
        Utils.registerCommands(new BalanceHandler(government), this, "balance", "bal");

        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new ActivityListener(government), this);

        government.getConsumerData().registerAllPlayers(this);
        government.getConsumerData().loadAllPlayers(this);
    }

    @Override
    public void onDisable() {
        government.save();
        government.getConsumerData().saveAllPlayers(this);
    }

    // getters
    public static Government getGovernment() { return government; }

    /**
     * Listeners
     */
    @EventHandler
    public void playerJoin(PlayerJoinEvent ev) {
        Player player = ev.getPlayer();
        Consumer consumer = new Consumer(government.getConsumerData(), player.getUniqueId());
        consumer.load();
        government.getConsumerData().registerPlayer(this, consumer);
    }
    @EventHandler
    public void playerQuit(PlayerQuitEvent ev) {
        Player player = ev.getPlayer();
        Consumer consumer = government.getConsumerData().getConsumer(player.getUniqueId());
        consumer.save();
        government.getConsumerData().unregisterPlayer(this, consumer);
    }
}
