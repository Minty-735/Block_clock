package org.minty.block_clock.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.minty.block_clock.Block_clock;
import org.minty.block_clock.METADATA;

import static org.minty.block_clock.Block_clock.initClock;

public class clockReboot implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        initClock();
        return false;
    }
}
