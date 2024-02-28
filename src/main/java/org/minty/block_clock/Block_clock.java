package org.minty.block_clock;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

import org.bukkit.Bukkit;
import org.minty.block_clock.clocks.Clock;
import org.minty.block_clock.clocks.GrandClock;
import org.minty.block_clock.command.clockSpawner;

public final class Block_clock extends JavaPlugin {
    Thread thread;
    public static Map<String, Clock> ClockMap = new HashMap<>();
    public static FileConfiguration mainConfig;

    public static File DataFolder;
    public static Set<GrandClock> Clocks = new HashSet<>();

    @Override
    public void onEnable() {
        /* todo Надо сделать так чтобы когда плагин выключался то он бы записывал куда-то где расположены все часы в мире и еще надо чтобы он при отключении удалял блоки часов*/
        getCommand("clock").setExecutor(new clockSpawner());

        METADATA.PLUGIN = this;
        DataFolder = getDataFolder();
        mainConfig = getConfig();


        saveDefaultConfig();

        initClock();

        Runnable myRunnable = new CustomRunnable();
        thread = new Thread(myRunnable);
        thread.start();


    }

    @Override
    public void onDisable() {
        thread.interrupt();
    }


    private void initClock() {
        ConfigurationSection clocksSection = mainConfig.getConfigurationSection("clocks");
        if (clocksSection != null) {
            Set<String> clocks = clocksSection.getKeys(false);
            for (String name : clocks) {
                boolean enabled = clocksSection.getBoolean(name);
                if (enabled) {
                    Clock clock = new Clock(name);
                    clock.loadConfig();
                }
            }
        }
    }
}


class CustomRunnable implements java.lang.Runnable {
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            // Выполняем задачу в основном потоке Bukkit
            Bukkit.getScheduler().runTask(METADATA.PLUGIN, () -> {
                for (GrandClock Gclock : Block_clock.Clocks) {
                    Gclock.UpdateTime();
                }
            });

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
