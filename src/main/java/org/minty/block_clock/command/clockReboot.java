package org.minty.block_clock.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.minty.block_clock.Block_clock;
import org.minty.block_clock.METADATA;

public class clockReboot implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("Block_clock");
        Bukkit.getPluginManager().disablePlugin(plugin);
        Bukkit.getPluginManager().enablePlugin(plugin);
        return false;
    }
}
