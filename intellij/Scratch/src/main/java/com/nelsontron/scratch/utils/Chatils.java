package com.nelsontron.scratch.utils;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public class Chatils {

    FileConfiguration config;

    private String primary;
    private String secondary;
    private String error;
    private String bold;
    private String italic;
    private String underlined;
    private boolean isPunctuated;

    public Chatils(FileConfiguration config) {
        this.config = config;
        this.primary = "";
        this.secondary = "";
        this.error = "";
        this.bold = "";
        this.italic = "";
        this.underlined = "";
        this.isPunctuated = false;
        this.initialize();
    }

    // getters
    public String getPrimary() {
        return primary;
    }
    public String getSecondary() {
        return secondary;
    }
    public String getError() {
        return error;
    }
    public String getBold() {
        return bold;
    }
    public String getItalic() {
        return italic;
    }
    public String getUnderlined() {
        return underlined;
    }
    public boolean isPunctuated() {
        return isPunctuated;
    }
    public String get(String string) {
        if (string.equalsIgnoreCase("primary"))
            return getPrimary();
        else if (string.equalsIgnoreCase("secondary"))
            return getSecondary();
        else if (string.equalsIgnoreCase("error"))
            return getError();
        else if (string.equalsIgnoreCase("bold"))
            return getBold();
        else if (string.equalsIgnoreCase("italic"))
            return getItalic();
        else if (string.equalsIgnoreCase("underlined"))
            return getUnderlined();
        else if (string.equalsIgnoreCase("punctuate"))
            return ""+isPunctuated();
        else
            return null;
    }

    // setters
    public void setPrimary(String primary) {
        this.primary = primary;
    }
    public void setSecondary(String secondary) {
        this.secondary = secondary;
    }
    public void setError(String error) {
        this.error = error;
    }
    public void setBold(String bold) {
        this.bold = bold;
    }
    public void setItalic(String italic) {
        this.italic = italic;
    }
    public void setUnderlined(String underlined) {
        this.underlined = underlined;
    }
    public void setPunctuated(boolean punctuated) {
        isPunctuated = punctuated;
    }

    // methods
    public Chatils initialize() {

        String primary = config.getString("scratch.colours.primary");
        String secondary = config.getString("scratch.colours.secondary");
        String error = config.getString("scratch.colours.error");
        String bold = config.getString("scratch.colours.bold");
        String italic = config.getString("scratch.colours.italic");
        String underlined = config.getString("scratch.colours.underline");

        if (primary != null) setPrimary(ChatColor.translateAlternateColorCodes('&', primary));
        if (secondary != null) setSecondary(ChatColor.translateAlternateColorCodes('&', secondary));
        if (error != null) setError(ChatColor.translateAlternateColorCodes('&', error));
        if (bold != null) setBold(ChatColor.translateAlternateColorCodes('&', bold));
        if (italic != null) setItalic(ChatColor.translateAlternateColorCodes('&', italic));
        if (underlined != null) setUnderlined(ChatColor.translateAlternateColorCodes('&', underlined));
        setPunctuated(config.getBoolean("scratch.colours.punctuated"));

        return this;
    }

    // api utilities
    public String formatStringVariables(String string) {

        for (String key : config.getConfigurationSection("scratch.colours").getKeys(false)) {
            string = string.replace("${" + key + "}", (this.get(key) != null) ? this.get(key) : "");
        }

        return string;
    }

    public String formatChatMessage(String string) {
        StringBuilder builder = new StringBuilder();

        if (getSecondary() != null)
            builder.append(getSecondary());

        string = formatStringVariables(string);

        builder.append(string);

        if (isPunctuated())
            builder.append(".");

        return builder.toString();
    }
    public String formatChatMessage(String string, String ...buzzwords) {
        StringBuilder builder = new StringBuilder();

        builder.append(getSecondary());

        for (String word : buzzwords) {
            string = string.replace(word, getPrimary() + word + getSecondary());
        }

        string = formatStringVariables(string);

        builder.append(string);

        if (isPunctuated())
            builder.append(".");

        return builder.toString();
    }

    public String formatErrorMessage(String string) {
        StringBuilder builder = new StringBuilder();

        builder.append(getError());

        string = formatStringVariables(string);

        builder.append("> ");
        builder.append(string);

        if (isPunctuated())
            builder.append(".");

        return builder.toString();
    }
    public String formatErrorMessage(String string, String ...buzzwords) {
        StringBuilder builder = new StringBuilder();

        builder.append(getError());

        for (String word : buzzwords) {
            string = string.replace(word, getUnderlined() + word + getError());
        }

        string = formatStringVariables(string);

        builder.append("> ");
        builder.append(string);

        if (isPunctuated())
            builder.append(".");

        return builder.toString();
    }
}
