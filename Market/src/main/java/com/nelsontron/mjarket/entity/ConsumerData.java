package com.nelsontron.mjarket.entity;

import com.nelsontron.mjarket.Mjarket;
import com.nelsontron.mjarket.utils.Data;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class ConsumerData {

    private Mjarket mjar;
    private Calc calc;
    private List<Consumer> consumers;
    private float[] balances;
    private int[] owed;
    private int totalWealth;
    private int totalOwed;

    private final int AFK_TIMER_MAX = 600;

    ConsumerData(Mjarket mjar) {
        this.mjar = mjar;
        this.consumers = new ArrayList<>();
        this.calc = new Calc();
        this.balances = calc.getBalances();
        this.owed = calc.getOwed();
        this.totalWealth = calc.getTotalWealth();
        this.totalOwed = calc.getTotalOwed();
    }

    // getters
    public List<Consumer> getConsumers() {
        return consumers;
    }
    public Consumer getConsumer(UUID uuid) {
        Consumer consumer = null;

        for (Consumer c : consumers)
            if (c.getUUID() == uuid)
                consumer = c;

        return consumer;
    }
    public Consumer getConsumer(int i) {
        return consumers.get(i);
    }
    public Listing getGlobalListing(String id) {
        Listing listing = null;
        for (Consumer consumer : consumers) {
            for (Listing l : consumer.getListings()) {
                if (l.getID().equalsIgnoreCase(id))
                    listing = l;
            }
        }
        return listing;
    }
    public Consumer getOwner(Listing listing) {
        Consumer consumer = null;
        for (Consumer c : consumers) {
            for (Listing l : c.getListings())
                if (l == listing)
                    consumer = c;
        }
        return consumer;
    }
    public float[] getBalances() { return balances; }
    public int[] getOwed() {
        return owed;
    }
    public int getTotalWealth() {
        return totalWealth;
    }
    public int getTotalOwed() {
        return totalOwed;
    }

    // methods
    public Data createConsumerData(Consumer consumer) {
        return new Data(mjar, consumer.getUUID());
    }
    public void scheduleAfkTimer(Consumer consumer) {
        mjar.getServer().getScheduler().scheduleSyncRepeatingTask(mjar, () -> {

            if (consumer.getSchedule() == consumer.getActivity()) {
                consumer.setAfkTimer(consumer.getAfkTimer()-1);
            }
            else {
                consumer.setSchedule(consumer.getActivity());
                consumer.setAfkTimer(AFK_TIMER_MAX);
            }

            if (consumer.getAfkTimer() > 0) {
                if (consumer.isNotified())
                    consumer.setNotified(false);
                consumer.setAfk(false);
            }
            else {
                consumer.setAfk(true);
            }

        }, 20, 20);
    }
    public void calculateTotals() {
        this.balances = calc.getBalances();
        this.owed = calc.getOwed();
        this.totalWealth = calc.getTotalWealth();
        this.totalOwed = calc.getTotalOwed();
    }

    // Function to merge multiple arrays in Java 8
    public static String[] mergeArrays(String[]... arrays) {
        return Stream.of(arrays)
                .flatMap(Stream::of)        // or use Arrays::stream
                .toArray(String[]::new);
    }

    public void registerAllPlayers(Mjarket mjar) {

        consumers.clear();

        for (final Player player : Bukkit.getOnlinePlayers()) {
            final Consumer consumer = new Consumer(this, player.getUniqueId());
            consumers.add(consumer);
        }
    }

    public void registerPlayer(Mjarket mjar, Consumer consumer) {
        consumers.add(consumer);
    }

    public void unregisterPlayer(Mjarket mjar, Consumer consumer) {
        consumers.remove(consumer);
    }

    public void saveAllPlayers(Mjarket mjar) {
        for (final Consumer consumer : consumers) {

            consumer.save();
        }
    }

    public void loadAllPlayers(Mjarket mjar) {
        for (final Consumer consumer : consumers) {

            consumer.load();
            System.out.println(consumer.getListings());
        }
    }

    public class Calc {

        float[] getBalances() {
            float[] arr = new float[getConsumers().size()];
            for (int i = 0; i < getConsumers().size(); i++) {
                arr[i] = getConsumers().get(i).getDoub();
            }
            return arr;
        }

        int[] getOwed() {
            Consumer consumer;
            int debtAmount;

            int[] arr = new int[getConsumers().size()];
            for (int i = 0; i < getConsumers().size(); i++) {
                debtAmount = 0;
                consumer = getConsumers().get(i);
                for (Integer service : consumer.getServices()) {
                    debtAmount += service;
                }
                arr[i] = debtAmount;
            }
            return arr;
        }

        int getTotalWealth() {
            int result = 0;
            for (float i : balances) {
                result += i;
            }
            return result;
        }

        int getTotalOwed() {
            int result = 0;
            for (Integer i : owed) {
                result += i;
            }
            return result;
        }
    }
}
