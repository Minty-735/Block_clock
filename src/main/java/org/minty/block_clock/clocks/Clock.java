package org.minty.block_clock.clocks;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.minty.block_clock.utils.PainterApplication;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static org.minty.block_clock.Block_clock.*;
import static org.minty.block_clock.utils.calc_line.drawLine;

public class Clock {

    private Clock(String name) {
        this.name = name;
        this.enableStatus = false;
    }

    public static Clock getInstance(String name) {
        if (!ClockMap.containsKey(name)) {
            ClockMap.put(name, new Clock(name));
        }
        return ClockMap.get(name);
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void setUtc(String utc) {

        this.utc = ZoneId.of( utc);
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
    private Location[][] matrix;

    public String getName() {
        return name;
    }

    public boolean isEnableStatus() {
        return enableStatus;
    }

    public void setEnableStatus(boolean enableStatus) {
        this.enableStatus = enableStatus;
    }

    private String name;
    private boolean enableStatus;
    private Material textBlock;

    private Material backgroundBlock;

    public Material getTextBlock() {
        return textBlock;
    }

    public Material getBackgroundBlock() {

        return backgroundBlock;
    }

    private File customConfigFile;
//    private GrandClock grandClock;


    public void build(Location startLocation, Location endlocation) {

        getDefaultSettings();
        this.endlocation = endlocation;
        this.startLocation = startLocation;
        try {
            saveCustomConfig();
            addToMainConfig(true);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        enableStatus = true;


    }

    public String getFormat() {
        return format;
    }

    public ZoneId getUtc() {
        return utc;
    }

    public void loadConfig() {
        customConfigFile = new File(clocksFolder, name + ".yml");
        FileConfiguration customConfig;
        customConfig = YamlConfiguration.loadConfiguration(customConfigFile);
        startLocation = customConfig.getLocation("FirstLoc");
        endlocation = customConfig.getLocation("LastLoc");
        format = customConfig.getString("format");
        utc = ZoneId.of(customConfig.getString("utc"));
        textBlock = Material.valueOf(customConfig.getString("textBlock"));
        backgroundBlock = Material.valueOf(customConfig.getString("backgroundBlock"));
        try {
            addToMainConfig(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        enableStatus = true;
    }

    @Override
    public String toString() {
        return "Clock{" +"\n"+ "startLocation=" + startLocation + "\n"+ " endlocation=" + endlocation + "\n"+ " format='" + format + "\n"+" utc=" + utc + " name='" + name + "\n"+ " textBlock=" + textBlock + "\n"+" backgroundBlock=" + backgroundBlock + '}';
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

    public void addToMainConfig(boolean state) throws IOException {
        File mainCFG = new File(DataFolder, "clocks.yml");
        FileConfiguration customConfig;
        customConfig = YamlConfiguration.loadConfiguration(mainCFG);
        customConfig.set("clocks." + name, state);
        customConfig.save(mainCFG);

    }

    public void saveCustomConfig() throws IOException {
        customConfigFile = new File(clocksFolder, name + ".yml");
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

    public void remove() throws IOException {
        removeBlocks();
        addToMainConfig(false);
        ClockMap.remove(name, this);
        enableStatus = false;
    }

    private String getTime() {
        if (format != null) {

            DateTimeFormatter hourFormatter = DateTimeFormatter.ofPattern(format);
            LocalTime currentTimeUtc = LocalTime.now(utc);
            String f = hourFormatter.format(currentTimeUtc);
            return f;
        }
        return null;
    }


    public void UpdateTime() {
        if (enableStatus) {
            String time = this.getTime();
            Location loc1 = this.getStartLocation();
            Location loc2 = this.getEndlocation();

            int x2 = loc2.getBlockX();
            int y2 = loc2.getBlockY();
            int x1 = loc1.getBlockX();
            int y1 = loc1.getBlockY();
            int z1 = loc1.getBlockZ();
            int z2 = loc2.getBlockZ();


            int vert = y2 - y1;
            char[][] chats;

            //chats[ВЫСОТА][ШИРИНА]


            Material black = this.getTextBlock();
            Material white = this.getBackgroundBlock();

            this.matrix = createMatrix(loc1, loc2);
//        System.out.println("matrix = " + matrix[0][0]);

            chats = PainterApplication.calc(time, matrix.length, matrix[0].length);
            chats = reverseArray(chats);


            for (int y = 0; y < chats.length - 1; y++) {
                for (int x = 0; x < (chats[0].length - 1) / 2; x++) {

                    char pixel = chats[y][x * 2];
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

    /**
     * [Координаты блока][высота]
     */
    private Location[][] createMatrix(Location loc1, Location loc2) {
        int x1 = (int) loc1.getX();
        int z1 = (int) loc1.getZ();
        int y1 = (int) loc1.getY();

        int x2 = (int) loc2.getX();
        int z2 = (int) loc2.getZ();
        int height = (int) loc2.getY() - (int) loc1.getY();

        World world = loc2.getWorld();
        Location[][] result;
        ArrayList<Integer> test = drawLine(x1, z1, x2, z2);

        int len = test.size();
        result = new Location[len][height];

        for (int i = 0; i < result.length; i += 2) {
            for (int j = 0; j < result[0].length; j++) {
                int sx = test.get(i);
                int sy = y1 + j;
                int sz = test.get(i + 1);
                result[i][j] = new Location(world, sx, sy, sz);
                //[координаты][высота]
            }
        }
        return result;
    }

    private char[][] reverseArray(char[][] chars) {
        int rows = chars.length;
        int cols = chars[0].length;

        char[][] reversedArray = new char[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                reversedArray[i][j] = chars[rows - i - 1][cols - j - 1];
            }
        }
        return reversedArray;
    }

    public void removeBlocks() {
        if (matrix != null) {
            for (Location[] loc : matrix) {
                for (Location locBlock : loc) {
                    if (locBlock != null) locBlock.getBlock().setType(Material.AIR);
                }
            }
        }
    }

}
