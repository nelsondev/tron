package com.nelsontron.siege.data;

import com.nelsontron.siege.game.Arena;
import com.nelsontron.siege.game.Game;
import com.nelsontron.siege.game.Holder;
import com.nelsontron.siege.game.SiegeGame;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GameCreator {

    UUID creator;
    Holder holder;
    Arena arena;
    int time;

    public GameCreator(Player player) {
        creator = player.getUniqueId();
        holder = null;
        arena = null;
        time = 0;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(creator);
    }
    public Holder getHolder() {
        return holder;
    }
    public Arena getArena() {
        return arena;
    }
    public int getTime() {
        return time;
    }

    public void setPlayer(Player player) { creator = player.getUniqueId(); }
    public void setHolder(Holder holder) {
        this.holder = holder;
    }
    public void setArena(Arena arena) {
        this.arena = arena;
    }
    public void setTime(int time) {
        this.time = time;
    }

    // methods
    public Game toGame() {

        if (holder == null || arena == null || time == 0) return null;

        return new SiegeGame(holder, arena, time);
    }
}
