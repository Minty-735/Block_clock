package org.minty.block_clock;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.minty.block_clock.clocks.Clock;
import org.minty.block_clock.command.clockReboot;
import org.minty.block_clock.command.clockSpawner;
import org.minty.block_clock.command.openInventory;
import org.minty.block_clock.listeners.click;
import org.minty.block_clock.utils.IconMenu;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class Block_clock extends JavaPlugin {
    Thread thread;
    public static Map<String, Clock> ClockMap = new HashMap<>();
    public static Map<String, Boolean> clockEnableStatus = new HashMap<>();
    public static FileConfiguration mainConfig;
    public static File DataFolder;
    public static File clocksFolder;
    public static IconMenu menu;
    public static Player waitingForReply = null;
    public static Clock clockForReply = null;

    public static String methodForReply = null;


    @Override
    public void onEnable() {
        getLogger().info("Start enabling plugin");
        getCommand("clock").setExecutor(new clockSpawner());
        getCommand("clock_settings").setExecutor(new openInventory());
//        getCommand("clock_reboot").setExecutor(new clockReboot());
        getServer().getPluginManager().registerEvents(new click(), this);


        METADATA.PLUGIN = this;
        DataFolder = getDataFolder();
        clocksFolder = new File(DataFolder, "clocks");
        if (!clocksFolder.exists()) {
            clocksFolder.mkdirs();
        }

        mainConfig = getConfig();
        {
            menu = new IconMenu("SETTINGS", 54, new IconMenu.OptionClickEventHandler() {
                @Override
                public void onOptionClick(IconMenu.OptionClickEvent event) {
                    Player player = event.getPlayer();
                    if (event.getName().equalsIgnoreCase("SETTINGS")) {
                    } else {

                        IconMenu clockIcon = createClockMenu(event);

                        Bukkit.getScheduler().scheduleSyncDelayedTask(METADATA.PLUGIN, new Runnable() {
                            public void run() {
                                clockIcon.open(player);
                            }
                        }, 2);


                    }
                }
            }, this).setOption(4, new ItemStack(Material.COMMAND_BLOCK, 1), "SETTINGS", "");
        }
        saveDefaultConfig();
        initClock();

        Runnable myRunnable = new CustomRunnable();
        thread = new Thread(myRunnable);
        thread.start();
        getLogger().info("Plugin enabled success");

    }

    private IconMenu createClockMenu(IconMenu.OptionClickEvent mainEvent) {

        Clock clock = Clock.getInstance(mainEvent.getName());

        boolean enable = clock.isEnableStatus();

        boolean finalEnable = enable;
        if (!enable) {
            clock.loadConfig();
            clock.setEnableStatus(false);

        }

        Clock finalClock = clock;

        IconMenu icon = new IconMenu(mainEvent.getName(), 54, new IconMenu.OptionClickEventHandler() {
            @Override
            public void onOptionClick(IconMenu.OptionClickEvent event) {

                String name = event.getName();
                Player player = event.getPlayer();

                if (name.equalsIgnoreCase("MENU")) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(METADATA.PLUGIN, new Runnable() {
                        public void run() {
                            menu.open(player);
                        }
                    }, 2);
                }
                if (name.equalsIgnoreCase("ENABLE STATUS")) {

                    if (finalEnable) {
                        try {
                            finalClock.remove();

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        finalClock.loadConfig();
                    }
//                    customLogger(name);
                    initClock();
                }
                if (name.equalsIgnoreCase("Clock name")) {
                }
                if (name.equalsIgnoreCase("Time Format")) {
                    waitingForReply = player;
                    clockForReply = clock;
                    methodForReply = "setFormat";

                }
                if (name.equalsIgnoreCase("UTC")) {
                    waitingForReply = player;
                    clockForReply = clock;
                    methodForReply = "setUtc";
                }

                event.setWillClose(true);

            }
        }, this);
        icon.setOption(49, new ItemStack(Material.COMMAND_BLOCK, 1), "MENU", "Back to main menu");

        if (enable) {
            icon.setOption(10, new ItemStack(Material.GREEN_CONCRETE, 1), "ENABLE STATUS", String.valueOf(enable), "Click to change status");
        } else {
            icon.setOption(10, new ItemStack(Material.RED_CONCRETE, 1), "ENABLE STATUS", String.valueOf(enable), "Click to change status");
        }

        icon.setOption(4, new ItemStack(Material.CLOCK, 1),  "Clock name",clock.getName());
        icon.setOption(11, new ItemStack(Material.BIRCH_SIGN, 1),  "UTC",clock.getUtc().toString(),"click to change","write in chat");
        icon.setOption(12, new ItemStack(Material.BIRCH_SIGN, 1),  "Time Format",clock.getFormat(),"click to change","write in chat");


        return icon;
    }


    @Override
    public void onDisable() {
        thread.interrupt();
        for (Clock clock : ClockMap.values()) {
            clock.removeBlocks();
        }
        getLogger().info("Disable plugin");

    }


    public static void initClock() {
        for (Clock clock : ClockMap.values()) {
            clock.removeBlocks();
        }
        ClockMap.clear();

        File mainCFG = new File(DataFolder, "clocks.yml");

        FileConfiguration customConfig;
        customConfig = YamlConfiguration.loadConfiguration(mainCFG);

        int i = 10;
        ConfigurationSection clocksSection = customConfig.getConfigurationSection("clocks");
        if (clocksSection != null) {
            Set<String> clocks = clocksSection.getKeys(false);
            for (String name : clocks) {
                Clock clock = Clock.getInstance(name);

                menu.setOption(i, new ItemStack(Material.CLOCK, 1), name, "");
                i++;
                if (i % 9 == 8) {
                    i++;
                    i++;
                }

                boolean enabled = clocksSection.getBoolean(name);

                if (enabled) {
                    clock.loadConfig();
                }

            }
        }
    }


    public static void customLogger(String message) {
        METADATA.PLUGIN.getLogger().info("----------------------------->" + message + "<-----------------------------");
    }
//todo check https://habr.com/ru/articles/649363/
}


class CustomRunnable implements Runnable {
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            Bukkit.getScheduler().runTask(METADATA.PLUGIN, () -> {
                for (Clock clock : Block_clock.ClockMap.values()) {
                    clock.UpdateTime();
                }
            });

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                METADATA.PLUGIN.getLogger().info("stopping thread");
                Thread.currentThread().interrupt(); // Прерывание
                return;
            }
        }
    }
}
