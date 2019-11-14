package com.nelsontron.permissions;

import com.nelsontron.permissions.util.YamlData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;

import java.util.*;

public class Perm {

    private Main main;
    private YamlData data;
    private Map<UUID, PermissionAttachment> permissions;

    public Perm(Main main) {
        this.main = main;
        this.data = new YamlData(main, "permissions");
        this.permissions = new HashMap<>();
    }

    public Main getMain() {
        return main;
    }
    public YamlData getData() {
        return data;
    }
    public Map<UUID, PermissionAttachment> getPermissions() {
        return permissions;
    }

    public void setData(YamlData data) {
        this.data = data;
    }
    public void setPermissions(Map<UUID, PermissionAttachment> permissions) {
        this.permissions = permissions;
    }

    // methods
    public void attachPlayer(Player player) {
        PermissionAttachment attachment = player.addAttachment(main);
        permissions.put(player.getUniqueId(), attachment);
    }
    public void savePlayer(Player player) {

        data.getData().set("player." + player.getUniqueId() + ".group", main.getConfig().getString("options.default-group"));
        data.getData().set("player." + player.getUniqueId() + ".name", player.getName());
    }
}
