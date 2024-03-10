package org.minty.block_clock.clocks;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.minty.block_clock.PainterApplication;

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
	/*
	this.all = null;
	*/
//        this.name = null;
//        this.grandClock = null;
//        this.startLocation = null;
//        this.endlocation = null;
        ClockMap.remove(name, this);
        grandClock.remove();

    }

    private String getTime() {
        DateTimeFormatter hourFormatter = DateTimeFormatter.ofPattern(format);
        LocalTime currentTimeUtc = LocalTime.now(utc);
        String f = hourFormatter.format(currentTimeUtc);
        return f;
    }


    public void UpdateTime() {

            String time = this.getTime();
            Location loc1 = this.getStartLocation();
            Location loc2 = this.getEndlocation();

            int x2 = loc2.getBlockX();
            int y2 = loc2.getBlockY();

            int horiz = Math.abs(x2);

            int vert = y2;
            char[][] chats;

            //chats[ВЫСОТА][ШИРИНА]
            chats = PainterApplication.calc(time, horiz, vert);
//todo додеоать перенос всего в 1 класс
            chats = reverseArray(chats);


            Material black = this.getTextBlock();
            Material white = this.getBackgroundBlock();

//        Location[][] matrix =
            this.matrix = createMatrix(loc1, loc2);


            for (int y = 0; y < chats.length; y++) {
                for (int x = 0; x < chats[0].length; x++) {
                    char pixel = chats[y][x];
                    //chats[ВЫСОТА][ШИРИНА]

                    Location loc = matrix[x * 2][y];

                    Block blk = loc.getBlock();
                    if (pixel == '*') {
                        if (blk.getType() != white) {
                            blk.setType(white);
                        }
                    } else {

                        if (blk.getType() != black) {
                            blk.setType(black);
                        }
//                    blk.setType(black);
                    }
                }
            }
    }



}
