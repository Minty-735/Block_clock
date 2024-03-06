package org.minty.block_clock;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
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

public final class Block_clock extends JavaPlugin {
    Thread thread;
    public static Map<String, Clock> ClockMap = new HashMap<>();
    public static Map<String, Boolean> clockEnableStatus = new HashMap<>();
    public static FileConfiguration mainConfig;
    public static File DataFolder;
    public static Set<GrandClock> grandClocks = new HashSet<>();
    public static IconMenu menu;
//    public static


    @Override
    public void onEnable() {

        getCommand("clock").setExecutor(new clockSpawner());
        getCommand("clock_settings").setExecutor(new openInventory());


        METADATA.PLUGIN = this;
        DataFolder = getDataFolder();
        mainConfig = getConfig();
        {
            menu = new IconMenu("SETTINGS", 54, new IconMenu.OptionClickEventHandler() {
                @Override
                public void onOptionClick(IconMenu.OptionClickEvent event) {
                    Player player = event.getPlayer();
                    if (event.getName().equalsIgnoreCase("SETTINGS")) {
                    } else {


                        IconMenu clockIcon = createClockMenu(event);

//                        event.setWillClose(true);
                        Bukkit.getScheduler().scheduleSyncDelayedTask(METADATA.PLUGIN, new Runnable() {
                            public void run() {
                                clockIcon.open(player);
                            }
                        }, 2);

//путем жеских вычислений и тестов я выяснил что если здесь не 2 то оно не откроется


                    }
                }
            }, this).setOption(4, new ItemStack(Material.COMMAND_BLOCK, 1), "SETTINGS", "");


        }
        saveDefaultConfig();
        initClock();

        Runnable myRunnable = new CustomRunnable();
        thread = new Thread(myRunnable);
        thread.start();


    }

    private IconMenu createClockMenu(IconMenu.OptionClickEvent mainEvent) {
        Clock clock = ClockMap.get(mainEvent.getName());
        boolean enable = clockEnableStatus.get(mainEvent.getName());

        IconMenu icon = new IconMenu(mainEvent.getName(), 54, new IconMenu.OptionClickEventHandler() {
            @Override
            public void onOptionClick(IconMenu.OptionClickEvent event) {

                String name = event.getName();
                Player player = event.getPlayer();
                //todo не меняется состояние часов
                if (name.equalsIgnoreCase("ENABLE STATUS")) {
                    if (enable) {
                        clock.remove();
                    } else {
                        Clock clock1 = new Clock(name);
                        clock1.loadConfig();
                    }
                    Reload();
                }
            }
        }, this);

        if (enable) {
            icon.setOption(10, new ItemStack(Material.GREEN_CONCRETE, 1), "ENABLE STATUS", String.valueOf(enable), "Click to change status");
        } else {
            icon.setOption(10, new ItemStack(Material.RED_CONCRETE, 1), "ENABLE STATUS", String.valueOf(enable), "Click to change status");
        }
        return icon;

    }


    @Override
    public void onDisable() {
        thread.interrupt();
        for (GrandClock clock : grandClocks) {
            clock.removeBlocks();
        }
    }


    private void initClock() {
        int i = 10;
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
                clockEnableStatus.put(name, enabled);


            }
        }
    }


    public void Reload(){
        onDisable();
        onEnable();
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
