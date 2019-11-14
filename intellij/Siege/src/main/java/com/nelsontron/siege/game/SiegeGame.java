package com.nelsontron.siege.game;

import com.nelsontron.siege.Siege;
import com.nelsontron.siege.entity.Fighter;
import com.nelsontron.siege.data.SiegeBoard;
import com.nelsontron.siege.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.List;

public class SiegeGame implements Game {
    private Holder holder;
    private Arena arena;
    private List<Fighter> blue;
    private List<Fighter> red;
    private int startDelay;
    private int time;
    private int maxTime;
    private int schedule;
    private String state;
    private List<SiegeBoard> scoreboardListener;

    private final int JOIN_TIME;

    public SiegeGame(Holder holder, Arena arena, int maxTime) {
        JOIN_TIME = 10;

        this.blue = new ArrayList<>();
        this.red = new ArrayList<>();
        this.holder = holder;
        this.arena = arena;
        this.maxTime = maxTime+JOIN_TIME;
        this.state = "stopped";
        this.scoreboardListener = new ArrayList<>();
    }

    // getters
    public List<Fighter> getBlue() {
        return blue;
    }
    public List<Fighter> getRed() {
        return red;
    }
    public String getState() {
        return state;
    }
    public List<SiegeBoard> getScoreboardListener() {
        return scoreboardListener;
    }
    public int getTime() {
        return time;
    }

    // setters
    public void setBlue(List<Fighter> blue) {
        this.blue = blue;
    }
    public void setRed(List<Fighter> red) {
        this.red = red;
    }
    public void setTime(int time) {
        this.time = time;
    }

    // methods
    private void populateScoreboards() {
        scoreboardListener.clear(); // clear for good measure
        for (Fighter fighter : holder.getFighters()) {
            this.scoreboardListener.add(new SiegeBoard(holder, fighter));
        }
    }
    private void applyScoreboards() {
        for (SiegeBoard fighterBoard : scoreboardListener) {
            fighterBoard.applyScoreboard();
        }
    }
    private void removeScoreboards() {
        for (SiegeBoard fighterBoard : scoreboardListener) {
            fighterBoard.removeScoreboard();
        }
    }
    private void updateScoreboards() {
        for (SiegeBoard fighterBoard : scoreboardListener) {
            fighterBoard.updateScores();
        }
    }

    private void clearMapDestruction() {
        arena.restoreBlocks();
        arena.clearBlocks();
    }
    private void messagePlayers() {
        Player player;
        for (Fighter fighter : blue) {
            player = fighter.getPlayer();
            player.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "Siege > " + ChatColor.GRAY
                    + "has started. You have been placed on " + ChatColor.BLUE + ChatColor.UNDERLINE + "blue.");
        }

        for (Fighter fighter : red) {
            player = fighter.getPlayer();
            player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Siege > " + ChatColor.GRAY
                    + "has started. You have been placed on " + ChatColor.RED + ChatColor.UNDERLINE + "red.");
        }

        holder.getSiege().getServer().getScheduler().scheduleSyncDelayedTask(holder.getSiege(), () -> {
            Player player1;

            for (Fighter fighter : blue) {
                player1 = fighter.getPlayer();
                player1.sendMessage(" ");
                player1.sendMessage(ChatColor.GRAY + "The game will begin shortly...");
            }

            for (Fighter fighter : red) {
                player1 = fighter.getPlayer();
                player1.sendMessage(" ");
                player1.sendMessage(ChatColor.GRAY + "The game will begin shortly...");
            }
        }, (startDelay/2));
    }
    private void selectTeams() {
        for (Fighter fighter : holder.getFighters()) {
            if (blue.size() > red.size()) {
                fighter.setTeam("red");
                red.add(fighter);
            }
            else {
                fighter.setTeam("blue");
                blue.add(fighter);
            }
        }
    }
    private void spawnPlayers() {
        for (Fighter fighter : holder.getFighters()) {
            if (fighter.getTeam().equalsIgnoreCase("blue"))
                fighter.getPlayer().teleportAsync(arena.getBlueSpawn(), PlayerTeleportEvent.TeleportCause.PLUGIN);
            else
                fighter.getPlayer().teleportAsync(arena.getRedSpawn(), PlayerTeleportEvent.TeleportCause.PLUGIN);
        }
    }
    private void applyKits() {
        for (Fighter fighter : holder.getFighters()) {
            fighter.applyKit();
        }
    }
    private void lockMovement() {
        for (Fighter fighter : holder.getFighters()) {
            fighter.setAllowedToMove(false);
        }
    }
    private void unlockMovement() {
        for (Fighter fighter : holder.getFighters()) {
            fighter.setAllowedToMove(true);
        }
    }
    private void play() {
        state = "playing";
        unlockMovement();
    }
    private void end() {

        state = "ending";

        Server server;
        BukkitScheduler scheduler;
        Runnable runnable;
        int delay = 20;
        int period = 20;

        server = holder.getSiege().getServer();
        scheduler = server.getScheduler();
        runnable = () -> {
            time -= 1;

            if (time <= 0) stop();

            if (time <= 5) {
                Bukkit.broadcastMessage(Utils.formatSiegeMessage(ChatColor.UNDERLINE + "GAME ENDING IN " + time));
            }
            else {
                Bukkit.broadcastMessage(Utils.formatSiegeMessage("GAME ENDING IN " + time));
            }
        };

        // schedule task
        scheduler.cancelTask(schedule); // cancel old task
        schedule = scheduler.scheduleSyncRepeatingTask(holder.getSiege(), runnable, delay, period);
    }
    private void scheduleCountdown() {
        for (SiegeBoard fighterBoard : scoreboardListener) {
            fighterBoard.countDown(5);
        }
    }
    private void scheduleTimer(int seconds) {

        Server server;
        BukkitScheduler scheduler;
        Runnable runnable;
        int delay = seconds;
        int period = 20;

        server = holder.getSiege().getServer();
        scheduler = server.getScheduler();
        runnable = () -> {
            System.out.println(time -= 1);
            updateScoreboards();
            if (time <= 11) end();
            if (time == (maxTime-JOIN_TIME)) play();
            if (time == (maxTime-(JOIN_TIME-5))) scheduleCountdown();
        };

        // schedule task.
        scheduler.cancelTask(schedule); // cancel old task
        schedule = scheduler.scheduleSyncRepeatingTask(holder.getSiege(), runnable, delay, period);
    }
    private void clearSchedule() {
        Server server = Bukkit.getServer();
        BukkitScheduler scheduler = server.getScheduler();
        scheduler.cancelTask(schedule);
    }
    private void clearScoreboard() {
        removeScoreboards();
    }
    private void clearTeams() {

        for (Fighter fighter : holder.getFighters())
            fighter.setTeam(null);

        red.clear();
        blue.clear();
    }
    private void sendPlayersToWorldSpawn() {
        for (Fighter fighter : blue) {
            fighter.getPlayer().teleportAsync(fighter.getPlayer().getWorld().getSpawnLocation());
        }
        for (Fighter fighter : red) {
            fighter.getPlayer().teleportAsync(fighter.getPlayer().getWorld().getSpawnLocation());
        }
    }

    public void start(int seconds) {
        state = "starting";
        time = maxTime;
        startDelay = seconds*20;

        selectTeams();
        spawnPlayers();
        populateScoreboards();
        applyScoreboards();
        applyKits();
        lockMovement();
        messagePlayers();
        scheduleTimer(seconds);
    }
    public void stop() {
        state = "stopped";
        sendPlayersToWorldSpawn();
        unlockMovement();
        clearSchedule();
        clearMapDestruction();
        clearScoreboard();
        clearTeams();
    }
    public void reset() {
        stop();
        start(startDelay);
    }
}
