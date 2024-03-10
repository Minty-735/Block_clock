package org.minty.block_clock;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
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

import static org.minty.block_clock.listeners.click.replyMessage;

public final class Block_clock extends JavaPlugin {
    Thread thread;
    public static Map<String, Clock> ClockMap = new HashMap<>();
    public static Map<String, Boolean> clockEnableStatus = new HashMap<>();
    public static FileConfiguration mainConfig;
    public static File DataFolder;
    //    public static Set<GrandClock> grandClocks = new HashSet<>();
    public static IconMenu menu;
    public static Player waitingForReply = null;
//    public static


    @Override
    public void onEnable() {
        getLogger().info("Start enabling plugin");
        getCommand("clock").setExecutor(new clockSpawner());
        getCommand("clock_settings").setExecutor(new openInventory());
        getCommand("clock_reboot").setExecutor(new clockReboot());
        getServer().getPluginManager().registerEvents(new click(), this);


        METADATA.PLUGIN = this;
        DataFolder = getDataFolder();

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
        getLogger().info("Plugin enabled success");

    }

    private IconMenu createClockMenu(IconMenu.OptionClickEvent mainEvent) {
        Clock clock = null;
        boolean enable = false;
        enable = clockEnableStatus.get(mainEvent.getName());

        if (enable) {
            clock = ClockMap.get(mainEvent.getName());
        }

        boolean finalEnable = enable;
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
                        Clock clock1 = new Clock(name);
                        clock1.loadConfig();
                    }
                    customLogger(name);
                    initClock();
                }
            }
        }, this);
        icon.setOption(49, new ItemStack(Material.CLOCK, 1), "MENU", "Back to main menu");

        if (enable) {
            icon.setOption(10, new ItemStack(Material.GREEN_CONCRETE, 1), "ENABLE STATUS", String.valueOf(enable), "Click to change status");
        } else {
            icon.setOption(10, new ItemStack(Material.RED_CONCRETE, 1), "ENABLE STATUS", String.valueOf(enable), "Click to change status");
        }


//        icon.setOption();


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


    private void initClock() {
        clockEnableStatus.clear();
        int i = 10;
        ConfigurationSection clocksSection = getConfig().getConfigurationSection("clocks");
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


    public void reloadPlugin() {
        METADATA.PLUGIN.onDisable();
        METADATA.PLUGIN.onEnable();
    }


    public static void customLogger(String message) {
        METADATA.PLUGIN.getLogger().info("----------------------------->" + message + "<-----------------------------");
    }

}


class CustomRunnable implements Runnable {
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            // Выполняем задачу в основном потоке Bukkit
            Bukkit.getScheduler().runTask(METADATA.PLUGIN, () -> {
//                System.out.println("Block_clock.ClockMap.values() ---------------------------> " + Block_clock.ClockMap.values()+"<-------------------------------");
                for (Clock clock : Block_clock.ClockMap.values()) {
                    clock.UpdateTime();
                }
            });

            try {
                // Задержка 1 сек
                Thread.sleep(1000);
            } catch (InterruptedException e) {
//                e.printStackTrace();
                METADATA.PLUGIN.getLogger().info("stopping thread");
                Thread.currentThread().interrupt(); // Прерывание
                return;
            }
        }
    }
}
