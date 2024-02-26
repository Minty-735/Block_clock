package org.minty.block_clock;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.util.EnumUtils;

import java.io.File;
import java.time.ZoneId;
import java.util.*;

public final class Block_clock extends JavaPlugin {
    Thread thread;
    public static ZoneId utc;

    //    public static List<Clock> ClockList = new ArrayList<>();
    public static Map<String, Clock> ClockMap = new HashMap<>();
    private FileConfiguration config;

    public static File DataFolder;

    public static Clock testClock;

    @Override
    public void onEnable() {
        /* todo Надо сделать так чтобы когда плагин выключался то он бы записывал куда-то где расположены все часы в мире и еще надо чтобы он при отключении удалял блоки часов
         *
         * */
        getCommand("clock").setExecutor(new clockSpawner());
        METADATA.PLUGIN = this;


        DataFolder = getDataFolder();
        saveDefaultConfig();

        config = getConfig();

        Location loc = new Location(null, 2, 2, 2);


        testClock = new Clock("test");
        testClock.loadConfig();
        Runnable myRunnable = new Runnable();


        thread = new Thread(myRunnable);

        thread.start();

    }

    @Override
    public void onDisable() {
        thread.interrupt();
    }

}

@SuppressWarnings("ALL")
class Runnable implements java.lang.Runnable {
    @Override
    public void run() {
        {
            while (!Thread.currentThread().isInterrupted()) {
                Clock clock = Block_clock.testClock;
                String time =
                        clock.updateTime();
                System.out.println("time = " + time);

                try {
                    // Задержка 1 сек
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt(); // Прерывание
                    return;
                }
            }
        }
    }
}