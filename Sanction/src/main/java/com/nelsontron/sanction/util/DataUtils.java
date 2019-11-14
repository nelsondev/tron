package com.nelsontron.sanction.util;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.Map;

public class DataUtils {
    public static void save(Yaml yaml, Object object, String path) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));
        writer.write(yaml.dumpAsMap(object));
        writer.close();
    }
    public static Map<String, Object> load(Yaml yaml, String path) throws FileNotFoundException {
        return yaml.load(new FileInputStream(path));
    }
}
