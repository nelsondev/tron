package com.nelsontron.mjarket.entity;

import com.nelsontron.mjarket.Mjarket;
import com.nelsontron.mjarket.utils.Data;
import com.nelsontron.mjarket.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Government {

    private Mjarket mjar;
    private Data data;
    private int doubloons;
    private ConsumerData consumerData;
    private Wellfare wellfare;

    public Government(Mjarket mjar) {
        this.mjar = mjar;
        this.data = new Data(mjar, "data", "government");
        load();
        this.consumerData = new ConsumerData(mjar);
        this.wellfare = new Wellfare();
    }

    public int getDoubloons() { return doubloons; }
    public ConsumerData getConsumerData() { return consumerData; }

    public void setDoubloons(int i) { doubloons = i; }

    // methods
    public void addDoubloons(int i) { doubloons += i; }
    public void removeDoubloons(int i) { doubloons -= i; }
    public boolean hasAmount(int i) {
        if (doubloons >= i)
            return true;
        else
            return false;
    }

    public void save() {
        data.getData().set("doubloons", doubloons);
        data.save();
    }
    public void load() {
        doubloons = data.getData().getInt("doubloons");
    }

    private class Wellfare {

        final int PERIOD = 24000;
        final int MINIMUM_WAGE = 20;
        int schedule;

        public Wellfare() {
            this.schedule = schedulePayments();
        }

        float getSchedule() { return schedule; }

        int schedulePayments() {
            return mjar.getServer().getScheduler().scheduleSyncRepeatingTask(mjar, ()-> {
                int wellfare;
                int taxes;
                Consumer topConsumer = null;
                float topActivity = 0;

                for (Consumer cons : consumerData.getConsumers()) {

                    Player player = Utils.consumerToPlayer(cons);

                    if (cons.isAfk()) {
                        if (!cons.isNotified()) {
                            player.sendMessage(ChatColor.GRAY + "Your productivity has slowed..");
                            cons.setNotified(true);
                        }
                        continue;
                    }

                    wellfare = calculateWellfare(cons);
                    taxes = calculateTaxes(cons);

                    cons.setDoub(cons.getDoub() + wellfare);
                    doubloons += taxes;
                    doubloons -= wellfare;

                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 5, 1);
                    player.sendMessage(ChatColor.GRAY + "It's tax day!");
                    player.sendMessage(ChatColor.GRAY + "This period you've spent " + ChatColor.GOLD + taxes + ChatColor.GRAY + " doubloons and are eligible" +
                            " for a government rebate of " + ChatColor.GOLD + wellfare + ChatColor.GRAY + " doubloons. The funds have been deposited into your account safely and securely." +
                            " Thanks so much for your cooperation.");
                    player.sendMessage(ChatColor.GRAY + "- Government of Poo Poo");

                    if (topActivity < cons.getActivity()) {
                        topActivity = cons.getActivity();
                        topConsumer = cons;
                    }

                    cons.getServices().clear();
                    cons.setActivity(0);
                }

                if (topConsumer != null) {
                    topConsumer.setDoub(topConsumer.getDoub()+150);
                    Utils.consumerToPlayer(topConsumer).sendMessage(ChatColor.GOLD + "!" + ChatColor.GRAY + " We've been " +
                            "impressed with your performance this period. Please enjoy a " + ChatColor.GOLD + "150$ bonus.");
                }

            }, 0, PERIOD);
        }

        int calculateTaxes(Consumer cons) {
            int result = 0;
            for (Integer i : cons.getServices()) {
                result += i;
            }
            return result;
        }

        int calculateWellfare(Consumer cons) {
            int result = 0;
            result += MINIMUM_WAGE;
            result += Math.round(cons.getActivity() - (cons.getActivity()/3));
            return result;
        }
    }
}
