package org.minty.block_clock.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.minty.block_clock.utils.inventGUI.Myinventory;

public class openInventory implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;
        player.openInventory(Myinventory);

        return true;
    }
}
