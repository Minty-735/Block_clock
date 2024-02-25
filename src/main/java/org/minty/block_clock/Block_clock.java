package org.minty.block_clock;

import org.bukkit.plugin.java.JavaPlugin;

import java.time.LocalTime;
import java.time.ZoneId;

public final class Block_clock extends JavaPlugin {
    Thread thread;
    public static ZoneId utc;

    @Override
    public void onEnable() {
        METADATA.PLUGIN = this;

        ZoneId utcZoneId = ZoneId.of("UTC-3");

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
                System.out.println("Текущее время в UTC: " + currentTimeUtc);

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