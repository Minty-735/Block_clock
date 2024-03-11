package org.minty.block_clock.command;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.minty.block_clock.clocks.Clock;

public class clockSpawner implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        World world;
        if (sender instanceof Player player) {
            world = player.getWorld();
        }
        else {
            return false;
        }

        Location loc1 = new Location(world, Double.parseDouble(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]));
        Location loc2 = new Location(world, Double.parseDouble(args[3]), Double.parseDouble(args[4]), Double.parseDouble(args[5]));

        String name = args[6];

        Clock clock = Clock.getInstance(name);

        clock.build(loc1, loc2);
        return true;
    }
}
