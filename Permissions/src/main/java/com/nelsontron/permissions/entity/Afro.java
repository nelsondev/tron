package com.nelsontron.permissions.entity;

import com.nelsontron.permissions.Perm;
import com.nelsontron.permissions.util.YamlData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Afro {

    private Perm permissions;
    private UUID uniqueId;
    private String group;
    private String name;
    private PermissionAttachment attachment;
    private YamlData data;

    public Afro(Perm permissions, Player player) {
        this.permissions = permissions;
        this.uniqueId = player.getUniqueId();
        this.group = null;
        this.name = null;
        this.attachment = player.addAttachment(permissions.getMain());
        this.data = new YamlData(permissions.getMain(), "players", player.getUniqueId().toString());
    }

    // getters
    public UUID getUniqueId() {
        return uniqueId;
    }
    public String getGroup() {
        return group;
    }
    public String getName() {
        return name;
    }
    public PermissionAttachment getAttachment() {
        return attachment;
    }

    // setters
    public void setUniqueId(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }
    public void setGroup(String group) {
        this.group = group;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setAttachment(PermissionAttachment attachment) {
        this.attachment = attachment;
    }

    // methods
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("group", group);
        map.put("uuid", uniqueId.toString());
        return map;
    }

    public static Afro deserialize(Perm permissions, Map<String, Object> map) {
        Afro afro;
        Player player = null;
        String name = null;
        String group = null;
        String uuid = null;
        if (map.containsKey("name"))
            name = (String) map.get("name");
        if (map.containsKey("group"))
            group = (String) map.get("group");
        if (map.containsKey("uuid"))
            uuid = (String) map.get("uuid");

        if (uuid == null) return null;

        player = Bukkit.getPlayer(uuid);
        if (player == null) return null;

        afro = new Afro(permissions, player);
        afro.setName(name);
        afro.setGroup(group);

        return afro;
    }

    public void save() {
        data.getData().set("data", serialize());
        data.save();
    }
}
