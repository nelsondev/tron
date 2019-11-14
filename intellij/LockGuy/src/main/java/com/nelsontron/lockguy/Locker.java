package com.nelsontron.lockguy;

import com.nelsontron.lockguy.utils.Data;
import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Locker {
    LockGuy lockGuy;
    UUID uuid;
    List<Box> boxes;
    Data data;

    public Locker(LockGuy lockGuy, Player player) {
        this.lockGuy = lockGuy;
        this.uuid = player.getUniqueId();
        this.boxes = new ArrayList<>();
        this.data = new Data(this.lockGuy, player);
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public List<Box> getBoxes() {
        return boxes;
    }

    public boolean hasBox(Location location) {
        boolean result = false;
        for (Box b : boxes) {
            if (b.isLocation(location)) {
                result = true;
                break;
            }
        }
        return result;
    }
    public boolean hasBox(Chest chest) {
        boolean result = false;
        for (Box b : boxes) {
            if (b.isLocation(chest.getLocation())) {
                result = true;
                break;
            }
        }
        return result;
    }
    public Box getBox(Location location) {
        Box box = null;
        for (Box b : boxes) {
            if (b.isLocation(location)) {
                box = b;
                break;
            }
        }
        return box;
    }

    public void save() {
        List<String> boxData = new ArrayList<>();
        for (Box box : boxes) {
            boxData.add(box.toString());
        }
        data.getData().set("boxes", boxData);
        data.save();
    }

    public void load() {
        List<String> list = data.getData().getStringList("boxes");

        // load in existing boxes.
        for (String str : list) {
            Box box = new Box();
            box.fromString(str);
            boxes.add(box);
        }
    }

}
