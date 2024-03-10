package org.minty.block_clock.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import static org.minty.block_clock.Block_clock.waitingForReply;

public class click implements Listener {

    public static String replyMessage;

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        if (waitingForReply != null && player == waitingForReply) {
            //todo здесь надо потестить
            replyMessage = message;
//            System.out.println("message = " + message);
            player.sendMessage("message = " + message);
            waitingForReply = null;
            event.setCancelled(true);
        }
    }
}
