package org.minty.block_clock;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static org.minty.block_clock.Block_clock.*;

public class Clock {
    private Location startLocation;
    private Location endlocation;
    private String side;
    private String format;
    private ZoneId utc;
    private String name;

    private File customConfigFile;

    public Clock(String name) {
        this.name = name;
        ClockMap.put(name, this);

    }

    public void build(Location startLocation, Location endlocation, String side, String format, String utc) {
        this.endlocation = endlocation;
        this.startLocation = startLocation;
        this.side = side;
        this.format = format;
        this.utc = ZoneId.of(utc);
        try {
            saveConfig();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "Clock{" +
                "startLocation=" + startLocation +
                ", endlocation=" + endlocation +
                ", side='" + side + '\'' +
                ", format='" + format + '\'' +
                ", utc=" + utc +
                ", name='" + name + '\'' +
                '}';
    }

    private void saveConfig() throws IOException {

        customConfigFile = new File(DataFolder, name + ".yml");
        FileConfiguration customConfig;
        customConfig = YamlConfiguration.loadConfiguration(customConfigFile);

        customConfig.set("FirstLoc", startLocation);
        customConfig.set("LastLoc", endlocation);
        customConfig.set("side", side);
        customConfig.set("format", format);
        customConfig.set("utc", utc.toString());
        customConfig.save(customConfigFile);

    }


    public void loadConfig() {
        customConfigFile = new File(DataFolder, name + ".yml");
        FileConfiguration customConfig;
        customConfig = YamlConfiguration.loadConfiguration(customConfigFile);
        this.name = name;
        startLocation = customConfig.getLocation("FirstLoc");
        endlocation = customConfig.getLocation("LastLoc");
        side = customConfig.getString("side");
        format = customConfig.getString("format");
        utc = ZoneId.of(customConfig.getString("utc"));

    }

    private void updateBlocks() {
        updateTime();
    }

    public String updateTime() {
        DateTimeFormatter hourFormatter = null;
        if (this.format.equalsIgnoreCase("24")) {
            hourFormatter = DateTimeFormatter.ofPattern("HH-mm-ss");
        } else if (this.format.equalsIgnoreCase("12")) {
            hourFormatter = DateTimeFormatter.ofPattern("hh-mm-ss-a");
        }
//        LocalTime currentTimeUtc = LocalTime.parse("13:24:25");
        LocalTime currentTimeUtc = LocalTime.now(utc);
        String f = hourFormatter.format(currentTimeUtc);

        return f;


    }


}
