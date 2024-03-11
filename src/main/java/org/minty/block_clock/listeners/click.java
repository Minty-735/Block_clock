package org.minty.block_clock.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.minty.block_clock.Block_clock.*;

public class click implements Listener {

    public static String replyMessage;

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) throws IOException {
        Player player = event.getPlayer();
        String message = event.getMessage();
        if (waitingForReply != null && player == waitingForReply) {
            if (message.equalsIgnoreCase("cansel")) {
            } else {
                //todo здесь надо потестить
                replyMessage = message;
//            System.out.println("message = " + message);


                Class<?> xD = null;

                try {
                    xD = Class.forName("org.minty.block_clock.clocks.Clock");

                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }


                try {
                    Method m = xD.getMethod(methodForReply, String.class);
                    if (m != null && m.canAccess(clockForReply)) {
                        m.invoke(clockForReply, replyMessage);
                    } else {
                        player.sendMessage("Метод setUtc не найден или недоступен для вызова.");
//                    System.out.println();
                    }


                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
                clockForReply.saveCustomConfig();
                initClock();

                waitingForReply = null;
                event.setCancelled(true);

                event.setCancelled(true);
            }
        }
    }
}
