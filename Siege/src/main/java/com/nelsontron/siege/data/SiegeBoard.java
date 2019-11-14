package com.nelsontron.siege.data;

import com.nelsontron.siege.entity.Fighter;
import com.nelsontron.siege.game.Holder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.*;

public class SiegeBoard {

    private Holder holder;
    private Fighter fighter;
    private ScoreboardManager manager;
    private Scoreboard scoreboard;
    private int time;
    private int schedule;

    public SiegeBoard(Holder holder, Fighter fighter) {
        this.holder = holder;
        this.manager = Bukkit.getScoreboardManager();
        this.scoreboard = this.manager.getNewScoreboard();
        this.fighter = fighter;
        this.time = 0;
    }

    private void setupTeams() {
        Team blue = scoreboard.registerNewTeam("blue");
        Team red = scoreboard.registerNewTeam("red");

        blue.setColor(ChatColor.BLUE);
        red.setColor(ChatColor.RED);

        blue.setPrefix(ChatColor.BLUE + "");
        red.setPrefix(ChatColor.RED + "");

        blue.setAllowFriendlyFire(false);
        red.setAllowFriendlyFire(false);

        for (Fighter fighter : holder.getFighters()) {

            if (fighter.getTeam().equalsIgnoreCase("blue"))
                blue.addEntry(fighter.getPlayer().getName());
            else
                red.addEntry(fighter.getPlayer().getName());
        }
    }

    private void setupObjectives() {
        Objective health = scoreboard.registerNewObjective("showhealth", "health", ChatColor.RED + " ❤");
        Objective stats = scoreboard.registerNewObjective("showstats", "dummy", ChatColor.RED + "" + ChatColor.BOLD + "Siege >");
        health.setDisplaySlot(DisplaySlot.BELOW_NAME);
        stats.setDisplaySlot(DisplaySlot.SIDEBAR);

        Score time = stats.getScore(ChatColor.WHITE + "Time");
        Score kills = stats.getScore(ChatColor.WHITE + "Kills");
        Score deaths = stats.getScore(ChatColor.WHITE + "Deaths");

        time.setScore(holder.getGame().getTime());
        kills.setScore(fighter.getKills());
        deaths.setScore(fighter.getDeaths());
    }

    public void updateScores() {
        Objective stats = scoreboard.getObjective("showstats");
        if (stats == null) return;

        Score time = stats.getScore(ChatColor.WHITE + "Time");
        Score kills = stats.getScore(ChatColor.WHITE + "Kills");
        Score deaths = stats.getScore(ChatColor.WHITE + "Deaths");

        time.setScore(holder.getGame().getTime());
        kills.setScore(fighter.getKills());
        deaths.setScore(fighter.getDeaths());
    }

    public void countDown(int seconds) {

        Server server;
        BukkitScheduler scheduler;
        Runnable runnable;
        int delay = 0;
        int period = 20;

        time = seconds+1;

        server = holder.getSiege().getServer();
        scheduler = server.getScheduler();
        runnable = () -> {
            time -= 1;

            if (time <= 1) scheduler.cancelTask(schedule);

            fighter.getPlayer().sendTitle(ChatColor.RED + "" + time, null, 0, 20, 0);
        };

        // schedule task.
        scheduler.cancelTask(schedule); // cancel old task
        schedule = scheduler.scheduleSyncRepeatingTask(holder.getSiege(), runnable, delay, period);

    }

    public void applyScoreboard() {
        setupTeams();
        setupObjectives();

        for (Player online : Bukkit.getOnlinePlayers()) {
            online.setCustomNameVisible(true);
        }

        fighter.getPlayer().setScoreboard(scoreboard);
    }

    public void removeScoreboard() {
        fighter.getPlayer().setScoreboard(manager.getMainScoreboard());
    }
}
