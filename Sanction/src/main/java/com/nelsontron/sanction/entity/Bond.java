package com.nelsontron.sanction.entity;

import com.nelsontron.sanction.Sanction;
import com.nelsontron.sanction.util.YamlData;
import org.bukkit.Bukkit;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.yaml.snakeyaml.Yaml;

import java.util.*;

public class Bond {
    private Sanction sanction;
    private UUID uniqueId;
    private String name;
    private String group;
    private YamlData yamlData;
    private PermissionAttachment attachment;

    public Bond(Sanction sanction, Player player) {
        this.sanction = sanction;
        this.yamlData = new YamlData(sanction.getMain(), player);
        this.attachment = player.addAttachment(sanction.getMain());
    }

    // getters
    public Sanction getSanction() {
        return sanction;
    }
    public UUID getUniqueId() {
        return uniqueId;
    }
    public String getName() {
        return name;
    }
    public String getGroup() {
        return group;
    }
    public PermissionAttachment getAttachment() {
        return attachment;
    }
    public List<String> getPermissions() {
        return new ArrayList<>(attachment.getPermissions().keySet());
    }
    public Player getPlayer() {
        return Bukkit.getPlayer(uniqueId);
    }

    // setters
    public void setSanction(Sanction sanction) {
        this.sanction = sanction;
    }
    public void setUniqueId(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setGroup(String group) {
        this.group = group;
    }
    public void setAttachment(PermissionAttachment attachment) {
        this.attachment = attachment;
    }
    // methods
    public void addPermission(String permission) {
        attachment.setPermission(permission, true);
    }
    public void removePermission(String permission) {
        attachment.unsetPermission(permission);
    }
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        return map;
    }
    public static Bond deserialize(Sanction sanction, Map<String, Object> map) {
        Bond bond;
        Player player = null;
        String group = null;

        if (map.containsKey("uniqueId"))
            player = Bukkit.getPlayer((String) map.get("uniqueId"));
        if (map.containsKey("group"))
            group = (String) map.get("group");

        if (player == null) return null;

        bond = sanction.instantiatePlayer(player);
        bond.setGroup(group);
        return bond;
    }
    private void loadGroupPermissions() {
        FileConfiguration config = sanction.getMain().getConfig();
        List<String> permissions = config.getStringList("groups." + group + ".permissions");
        for (String permission : permissions)
            attachment.setPermission(permission, true);
    }
    public void save() {
        yamlData.getData().set("data.name", name);
        yamlData.getData().set("data.group", group);
        yamlData.getData().set("data.uniqueId", uniqueId);
        yamlData.save();
    }
    public void load() {
        Bond bond;
        MemorySection section;
        Map<String, Object> map;

        section = (MemorySection) yamlData.getData().get("data");

        if (section == null) return;

        map = section.getValues(false);

        if (map.isEmpty()) return;

        bond = deserialize(sanction, map);

        if (bond == null) return;

        /*
         * LOAD
         */
        setUniqueId(bond.getUniqueId());
        setName(bond.getName());
        setGroup(bond.getGroup());
        loadGroupPermissions();
    }
}
