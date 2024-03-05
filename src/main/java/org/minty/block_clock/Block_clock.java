package org.minty.block_clock;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

import org.bukkit.Bukkit;
import org.minty.block_clock.clocks.Clock;
import org.minty.block_clock.clocks.GrandClock;
import org.minty.block_clock.command.clockSpawner;
import org.minty.block_clock.command.openInventory;
import org.minty.block_clock.utils.IconMenu;
import org.minty.block_clock.utils.inventGUI;

public final class Block_clock extends JavaPlugin {
    Thread thread;
    public static Map<String, Clock> ClockMap = new HashMap<>();
    public static FileConfiguration mainConfig;

    public static File DataFolder;
    public static Set<GrandClock> grandClocks = new HashSet<>();
    public static IconMenu menu;

    @Override
    public void onEnable() {

        getCommand("clock").setExecutor(new clockSpawner());
        getCommand("clock_settings").setExecutor(new openInventory());


        getServer().getPluginManager().registerEvents(new inventGUI(),
                this);

        METADATA.PLUGIN = this;
        DataFolder = getDataFolder();
        mainConfig = getConfig();
        {
            menu = new IconMenu("Clocks settings", 54, new IconMenu.OptionClickEventHandler() {
                @Override
                public void onOptionClick(IconMenu.OptionClickEvent event) {
                    event.getPlayer().sendMessage(event.getName());
                    event.setWillClose(true);
                }
            }, this).setOption(4, new ItemStack(Material.COMMAND_BLOCK, 1), "SETTINGS", "");


        }
        saveDefaultConfig();
        initClock();

        Runnable myRunnable = new CustomRunnable();
        thread = new Thread(myRunnable);
        thread.start();


    }


    @Override
    public void onDisable() {
        thread.interrupt();

        for (GrandClock clock : grandClocks) {
            clock.removeBlocks();
        }
    }


    private void initClock() {
        int i = 9;
        ConfigurationSection clocksSection = mainConfig.getConfigurationSection("clocks");
        if (clocksSection != null) {
            Set<String> clocks = clocksSection.getKeys(false);
            for (String name : clocks) {

                menu.setOption(i, new ItemStack(Material.CLOCK, 1), name, "");
                i++;
                if (i % 9 == 8) {
                    i++;
                    i++;
                }


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
                for (GrandClock Gclock : Block_clock.grandClocks) {
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
