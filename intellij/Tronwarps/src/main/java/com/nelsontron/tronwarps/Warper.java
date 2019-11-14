package com.nelsontron.tronwarps;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Warper {

    private Tronwarps tronwarps;
    private UUID uuid;
    private List<Warp> warps;
    private Data data;

    public Warper(Tronwarps tronwarps, Player player) {
        this.tronwarps = tronwarps;
        this.uuid = player.getUniqueId();
        this.warps = new ArrayList<>();
        this.data = new Data(tronwarps, player);
    }

    // getters
    public UUID getUuid() { return uuid; }
    public List<Warp> getWarps() { return warps; }

    // methods
    public Warp getWarp(String name) {
        Warp warp = null;
        for (Warp w : warps) {
            if (w.getName().equalsIgnoreCase(name))
                warp = w;
        }
        return warp;
    }
    public Warp guessWarp(String name) {
        final int POSSIBLE_RESULT_MAX = 3;

        Warp warp = null;
        String guess = null;
        List<String> guessList = new ArrayList<>();;

        for (Warp w : warps) {

            // warp found, return that.
            if (w.getName().equalsIgnoreCase(name)) {
                return w;
            }

            int probability = 0;

            boolean isSync = false;

            List<Character> typedChars = name.chars().mapToObj(c -> (char) c).collect(Collectors.toList());

            if (w.getName().contains(name))
                probability += 2;

            for (Character c : w.getName().toCharArray()) {
                if (typedChars.contains(c)) {

                    probability += (isSync) ? 2 : 1;
                    isSync = true;
                    typedChars.remove(c);
                }
                else {
                    isSync = false;
                }
            }

            if (typedChars.size() != 0)
                probability -= typedChars.size();

            if (probability < POSSIBLE_RESULT_MAX)
                continue;

            guessList.add(w.getName() + " " + probability);
        }

        System.out.println(guessList + " guesses");

        // local variable maxNum to store what number is the biggest found recently.
        int maxNum = 0;
        // loop through list of warps
        for (String warpData : guessList) {
            // split by space, grab the warp name and probablity
            String[] arr = warpData.split(" ");
            int matchNum; // local variable to grab the probablity number
            try {
                matchNum = Integer.parseInt(arr[1]);
            } catch(NumberFormatException ex) {
                matchNum = 0;
            }
            // if current warps value is over the current max
            if (matchNum > maxNum) {
                guess = arr[0]; // set guess to this warp
                maxNum = matchNum;
            }
        }

        // we found a guess
        if (guess != null) {

            for (Warp w : warps)
                if (w.getName().equalsIgnoreCase(guess))
                    warp = w;
        }
        return warp;
    }

    public void addWarp(Warp warp) {
        warps.add(warp);
    }

    public void removeWarp(Warp warp) {
        warps.remove(warp);
    }

    public List<String> toYamlStringArray() {
        List<String> result = new ArrayList<>();

        for (Warp warp : warps) {
            result.add(warp.toString());
        }

        return result;
    }

    public void sortAlphabeticalAscending() {
        List<String> names = new ArrayList<>();
        for (Warp w : warps) {
            names.add(w.getName());
        }

        names.sort(Comparator.comparing( String::toString ));

        List<Warp> list = new ArrayList<>();
        for (String name : names) {
            for (Warp w : warps) {
                if (w.getName().equalsIgnoreCase(name))
                    list.add(w);
            }
        }

        warps = list;
    }

    public void sortAlphabeticalDecending() {
        List<String> names = new ArrayList<>();
        for (Warp w : warps) {
            names.add(w.getName());
        }

        names.sort(Comparator.comparing( String::toString ).reversed());

        List<Warp> list = new ArrayList<>();
        for (String name : names) {
            for (Warp w : warps) {
                if (w.getName().equalsIgnoreCase(name))
                    list.add(w);
            }
        }

        warps = list;
    }

    public void save() {

        data.getData().set("warps", toYamlStringArray());
        data.save();
    }

    public void load() {
        List<String> list = data.getData().getStringList("warps");

        for (String str : list) {
            Warp warp = new Warp();

            warp.fromString(str);
            warps.add(warp);
        }
    }
}
