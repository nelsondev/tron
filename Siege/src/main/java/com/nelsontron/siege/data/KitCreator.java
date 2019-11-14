package com.nelsontron.siege.data;

import com.nelsontron.siege.entity.Fighter;
import com.nelsontron.siege.kits.Kit;
import com.nelsontron.siege.kits.SiegeKit;

public class KitCreator extends SiegeKit {

    Fighter fighter;
    int slot;

    public KitCreator(Fighter fighter) {

        this.fighter = fighter;
        this.slot = 0;
    }


    // getters
    public Fighter getFighter() {
        return fighter;
    }
    public int getSlot() {
        return slot;
    }
    // setters
    public void setFighter(Fighter fighter) {
        this.fighter = fighter;
    }
    public void setSlot(int slot) {
        this.slot = slot;
    }
    // methods
    public KitCreator fromKit(Kit kit) {
        KitCreator creator = new KitCreator(fighter);
        creator.setSlot(slot);
        creator.setInventory(kit.getInventory());
        creator.setBoots(kit.getBoots());
        creator.setLeggings(kit.getLeggings());
        creator.setChestplate(kit.getChestplate());
        creator.setHelmet(kit.getHelmet());
        creator.setDescription(kit.getDescription());
        creator.setIcon(kit.getIcon());
        creator.setEffects(kit.getEffects());
        creator.setPerks(kit.getPerks());
        creator.setWeight(kit.getWeight());
        creator.setPoints(kit.getPoints());
        return creator;
    }
    public Kit toKit() {
        Kit kit = new SiegeKit();
        kit.setDescription(this.getDescription());
        kit.setHelmet(this.getHelmet());
        kit.setChestplate(this.getChestplate());
        kit.setLeggings(this.getLeggings());
        kit.setBoots(this.getBoots());
        kit.setIcon(this.getIcon());
        kit.setEffects(this.getEffects());
        kit.setPerks(this.getPerks());
        kit.setInventory(this.getInventory());
        kit.setPoints(this.getPoints());
        kit.setWeight(this.getWeight());
        return kit;
    }
}
