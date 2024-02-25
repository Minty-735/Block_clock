package org.minty.block_clock;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.LocalTime;
import java.time.ZoneId;

public final class Block_clock extends JavaPlugin {
    Thread thread;
    public static ZoneId utc;
    private FileConfiguration config;

    @Override
    public void onEnable() {
        METADATA.PLUGIN = this;

        saveDefaultConfig();

        config = getConfig();

        utc = ZoneId.of(config.getString("settings.utc"));
        /* ZoneId utcZoneId = ZoneId.of("UTC-3"); */


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

                LocalTime currentTimeUtc = LocalTime.now(Block_clock.utc);
                String hours = String.valueOf(currentTimeUtc.getHour());
                String minutes = String.valueOf(currentTimeUtc.getMinute());
                String seconds = String.valueOf(currentTimeUtc.getSecond());
                System.out.println(hours + " " + minutes + " " + seconds);

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