package com.nelsontron.scratch.listeners;

import com.nelsontron.scratch.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class HarderMobs implements Listener {
    @EventHandler
    public void skeletonSpawn(EntitySpawnEvent ev) {

        Skeleton mob = null;

        if (ev.getEntity() instanceof Skeleton) {
            mob = (Skeleton) ev.getEntity();
        }
        else {
            return;
        }

        final Random ran = new Random();

        final ItemStack[] armour = new ItemStack[4];
        final ItemStack lhelm = new ItemStack(Material.LEATHER_HELMET);
        final ItemStack lches = new ItemStack(Material.LEATHER_CHESTPLATE);
        final ItemStack llegs = new ItemStack(Material.LEATHER_LEGGINGS);
        final ItemStack lboot = new ItemStack(Material.LEATHER_BOOTS);
        final ItemStack ihelm = new ItemStack(Material.IRON_HELMET);
        final ItemStack iches = new ItemStack(Material.IRON_CHESTPLATE);
        final ItemStack ilegs = new ItemStack(Material.IRON_LEGGINGS);
        final ItemStack iboot = new ItemStack(Material.IRON_BOOTS);

        final boolean canHaveArmour = ran.nextInt(3) == 1;
        final int ranHelm = ran.nextInt(3);
        final int ranChes = ran.nextInt(3);
        final int ranLegs = ran.nextInt(3);
        final int ranBoot = ran.nextInt(3);

        if (!canHaveArmour) { return; }

        if (ranBoot == 0) { armour[0] = lboot; }
        if (ranBoot == 1) { armour[0] = iboot; }

        if (ran.nextInt(100 + 1) <= 1) {
            armour[0] = Utils.randomEnchantment(armour[0]);
        }

        if (ranLegs == 0) { armour[1] = llegs; }
        if (ranLegs == 1) { armour[1] = ilegs; }

        if (ran.nextInt(100 + 1) <= 1) {
            armour[1] = Utils.randomEnchantment(armour[1]);
        }

        if (ranChes == 0) { armour[2] = lches; }
        if (ranChes == 1) { armour[2] = iches; }

        if (ran.nextInt(100 + 1) <= 1) {
            armour[2] = Utils.randomEnchantment(armour[2]);
        }

        if (ranHelm == 0) { armour[3] = lhelm; }
        if (ranHelm == 1) { armour[3] = ihelm; }

        if (ran.nextInt(100 + 1) <= 1) {
            armour[3] = Utils.randomEnchantment(armour[3]);
        }

        mob.getEquipment().setArmorContents(armour);
        mob.setCanPickupItems(true);
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void zombieSpawn(EntitySpawnEvent ev) {

        Zombie mob = null;

        if (ev.getEntity() instanceof Zombie) {
            mob = (Zombie) ev.getEntity();
        }
        else {
            return;
        }

        final Random ran = new Random();

        final ItemStack[] armour = new ItemStack[4];
        ItemStack weapon = new ItemStack(Material.STONE_SWORD);
        final ItemStack lhelm = new ItemStack(Material.LEATHER_HELMET);
        final ItemStack lches = new ItemStack(Material.LEATHER_CHESTPLATE);
        final ItemStack llegs = new ItemStack(Material.LEATHER_LEGGINGS);
        final ItemStack lboot = new ItemStack(Material.LEATHER_BOOTS);
        final ItemStack ihelm = new ItemStack(Material.IRON_HELMET);
        final ItemStack iches = new ItemStack(Material.IRON_CHESTPLATE);
        final ItemStack ilegs = new ItemStack(Material.IRON_LEGGINGS);
        final ItemStack iboot = new ItemStack(Material.IRON_BOOTS);
        final ItemStack dhelm = new ItemStack(Material.DIAMOND_HELMET);
        final ItemStack dches = new ItemStack(Material.DIAMOND_CHESTPLATE);
        final ItemStack dlegs = new ItemStack(Material.DIAMOND_LEGGINGS);
        final ItemStack dboot = new ItemStack(Material.DIAMOND_BOOTS);

        final boolean canHaveArmour = ran.nextInt(3) == 1;
        final int ranHelm = ran.nextInt(4);
        final int ranChes = ran.nextInt(4);
        final int ranLegs = ran.nextInt(4);
        final int ranBoot = ran.nextInt(4);
        final int ranWeapon = ran.nextInt(4);

        if (!canHaveArmour) { return; }

        if (ranBoot == 0) { armour[0] = lboot; }
        if (ranBoot == 1) { armour[0] = iboot; }
        if (ranBoot == 2) { armour[0] = dboot; }

        if (ran.nextInt(100 + 1) <= 1) {
            armour[0] = Utils.randomEnchantment(armour[0]);
        }

        if (ranLegs == 0) { armour[1] = llegs; }
        if (ranLegs == 1) { armour[1] = ilegs; }
        if (ranLegs == 2) { armour[1] = dlegs; }

        if (ran.nextInt(100 + 1) <= 1) {
            armour[1] = Utils.randomEnchantment(armour[1]);
        }

        if (ranChes == 0) { armour[2] = lches; }
        if (ranChes == 1) { armour[2] = iches; }
        if (ranChes == 2) { armour[2] = dches; }

        if (ran.nextInt(100 + 1) <= 1) {
            armour[2] = Utils.randomEnchantment(armour[2]);
        }

        if (ranHelm == 0) { armour[3] = lhelm; }
        if (ranHelm == 1) { armour[3] = ihelm; }
        if (ranHelm == 2) { armour[3] = dhelm; }

        if (ran.nextInt(100 + 1) <= 1) {
            armour[3] = Utils.randomEnchantment(armour[3]);
        }

        if (ranWeapon == 0) { weapon = new ItemStack(Material.WOODEN_SWORD); }
        if (ranWeapon == 1) { weapon = new ItemStack(Material.DIAMOND_SWORD); }
        if (ranWeapon == 2) { weapon = new ItemStack(Material.IRON_SWORD); }
        if (ranWeapon == 3) { weapon = new ItemStack(Material.BOW); }

        if (ran.nextInt(100 + 1) <= 1) {
            weapon = Utils.randomEnchantment(weapon);
        }

        mob.getEquipment().setArmorContents(armour);
        mob.getEquipment().setItemInHand(weapon);
        mob.setCanPickupItems(true);
        mob.setMaxHealth(
                Math.round(mob.getMaxHealth() + (mob.getMaxHealth() / 3)));
    }
}
