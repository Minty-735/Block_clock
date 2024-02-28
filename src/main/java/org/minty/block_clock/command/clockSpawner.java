package org.minty.block_clock.command;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.minty.block_clock.clocks.Clock;

public class clockSpawner implements CommandExecutor {
    @Override
/**
 *
 /clock {startloc} {endloc} {name} {format -||- ->} {UTC - по желанию, будет дефолт конфиг} {block - по желанию, будет дефолт конфиг}
 понятно как-бы
 тоже подумать как сделать, обязателен ли вообще этот параметр и как парсить все твердые блоки майнкрафта
 формат? надо подумать надо ли? 12/24
 надо ли координаты начала
 */

    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        World world = null;
        if (sender instanceof Player) {
            Player player = (Player) sender;
            world = player.getWorld();
        } else {

            for (Player p : Bukkit.getOnlinePlayers()) {
                world = p.getWorld();
                break;
            }
        }


        Location loc1 = new Location(world, Double.parseDouble(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]));
        Location loc2 = new Location(world, Double.parseDouble(args[3]), Double.parseDouble(args[4]), Double.parseDouble(args[5]));


        String name = args[6];

        Clock clock = new Clock(name);


        clock.build(loc1, loc2);




        return true;
    }
}
