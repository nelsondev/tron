package com.nelsontron.scratch.commands;

import com.nelsontron.scratch.ScratcherHolder;
import com.nelsontron.scratch.entity.Scratcher;
import com.nelsontron.scratch.utils.Chatils;
import com.nelsontron.scratch.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorldCommand implements CommandExecutor {
    private ScratcherHolder holder;
    private FileConfiguration config;
    private Chatils chatils;

    public static Map<String, GameMode> gamemodes = new HashMap<>();;

    public WorldCommand(FileConfiguration config, ScratcherHolder holder) {
        this.config = config;
        this.holder = holder;
        this.chatils = holder.getChatils();

        loadWorlds();
    }

    public static GameMode getGamemode(String worldName) {

        GameMode mode = gamemodes.get(worldName);

        if (mode == null) return GameMode.SURVIVAL;

        return mode;
    }

    public void loadWorlds() {

        World w;

        for (String worldData : config.getStringList("scratch.multi.worlds")) {
            String[] str = worldData.split(":");
            String worldName = str[0];
            String gamemodeName = null;

            if (str.length == 2)
                gamemodeName = str[1];

            if (gamemodeName != null)
                gamemodes.put(worldName, GameMode.valueOf(gamemodeName.toUpperCase()));
            else
                gamemodes.put(worldName, GameMode.SURVIVAL);

            w = new WorldCreator(worldName).createWorld();

            if (Bukkit.getWorlds().contains(w)) continue;
            Bukkit.getWorlds().add(w);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player;
        Scratcher scratcher;
        World world;
        Scratcher.DomainData domainData;
        String worldName;

        String result = null;

        try {

            player = (Player) sender;
            scratcher = holder.getScratcher(player);
            domainData = scratcher.getDomainData();

            // list current worlds
            if (args.length == 0) {
                result = chatils.formatErrorMessage("Please specify the world you'd like to go to");
            }
            // teleport player to a world
            else {
                worldName = Utils.stringify(args, "_");
                world = Bukkit.getWorld(worldName);

                if (world == null) {
                    result = chatils.formatErrorMessage("That world doesn't exist");
                    return true;
                }

                domainData.updateCurrentDomainData();
                domainData.updatePlayerDomain(world);
                player.setGameMode(getGamemode(worldName));

                result = chatils.formatChatMessage("Teleported to world " + worldName, worldName);
            }

            return true;
        } catch (ClassCastException ex) {
            result = chatils.formatErrorMessage("You must be a player to use this command");
        } finally {
            if (result != null)
                sender.sendMessage(result);
        }

        return false;
    }
}
