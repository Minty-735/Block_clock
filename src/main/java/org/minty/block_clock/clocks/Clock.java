package org.minty.block_clock.clocks;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static org.minty.block_clock.Block_clock.*;

public class Clock {
    public Clock(String name) {
        this.name = name;
        ClockMap.put(name, this);
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public Location getEndlocation() {
        return endlocation;
    }

    private Location startLocation;
    private Location endlocation;
    private String format;
    private ZoneId utc;

    private String name;

    private Material textBlock;

    private Material backgroundBlock;

    public Material getTextBlock() {
        return textBlock;
    }

    public Material getBackgroundBlock() {

        return backgroundBlock;
    }

    private File customConfigFile;
    private GrandClock grandClock;

    public void build(Location startLocation, Location endlocation) {

        getDefaultSettings();
        this.endlocation = endlocation;
        this.startLocation = startLocation;
        try {
            saveCustomConfig();
            addToMainConfig();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        grandClock = new GrandClock(this);

    }

    public void loadConfig() {
        customConfigFile = new File(DataFolder, name + ".yml");
        FileConfiguration customConfig;
        customConfig = YamlConfiguration.loadConfiguration(customConfigFile);
        startLocation = customConfig.getLocation("FirstLoc");
        endlocation = customConfig.getLocation("LastLoc");
        format = customConfig.getString("format");
        utc = ZoneId.of(customConfig.getString("utc"));
        textBlock = Material.valueOf(customConfig.getString("textBlock"));
        backgroundBlock = Material.valueOf(customConfig.getString("backgroundBlock"));
        new GrandClock(this);

    }

    @Override
    public String toString() {
        return "Clock{" + "startLocation=" + startLocation + ", endlocation=" + endlocation + ", format='" + format + '\'' + ", utc=" + utc + ", name='" + name + '\'' + ", textBlock=" + textBlock + ", backgroundBlock=" + backgroundBlock + '}';
    }

    private void getDefaultSettings() {

        File mainCFG = new File(DataFolder, "config.yml");
        FileConfiguration customConfig;
        customConfig = YamlConfiguration.loadConfiguration(mainCFG);
        utc = ZoneId.of(customConfig.getString("settings.utc"));
        format = customConfig.getString("settings.format");
        textBlock = Material.valueOf(customConfig.getString("settings.textBlock"));
        backgroundBlock = Material.valueOf(customConfig.getString("settings.backgroundBlock"));


    }

    private void addToMainConfig() throws IOException {
        File mainCFG = new File(DataFolder, "config.yml");
        FileConfiguration customConfig;
        customConfig = YamlConfiguration.loadConfiguration(mainCFG);
        customConfig.set("clocks." + name, true);
        customConfig.save(mainCFG);

    }

    private void saveCustomConfig() throws IOException {
        customConfigFile = new File(DataFolder, name + ".yml");
        FileConfiguration customConfig;
        customConfig = YamlConfiguration.loadConfiguration(customConfigFile);

        customConfig.set("FirstLoc", startLocation);
        customConfig.set("LastLoc", endlocation);
        customConfig.set("format", format);
        customConfig.set("utc", utc.toString());
        customConfig.set("textBlock", textBlock.toString());
        customConfig.set("backgroundBlock", backgroundBlock.toString());

        customConfig.save(customConfigFile);

    }

    public void remove() {

        ClockMap.remove(name, this);
        grandClock.remove();

    }

    public String updateTime() {

        //        if (this.format.equalsIgnoreCase("24")) {
//        } else if (this.format.equalsIgnoreCase("12")) {
//            hourFormatter = DateTimeFormatter.ofPattern("hh-mm-ss-a");
//        }
//        LocalTime currentTimeUtc = LocalTime.parse("13:24:25");


        DateTimeFormatter hourFormatter = DateTimeFormatter.ofPattern(format);
        LocalTime currentTimeUtc = LocalTime.now(utc);
        String f = hourFormatter.format(currentTimeUtc);

        return f;


    }


}
