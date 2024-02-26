package org.minty.block_clock;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.swing.text.PlainDocument;

public class clockSpawner implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

//        if (!(sender instanceof Player)) {
//            return false;
//        }
//        Player player = (Player) sender;

/**
 /clock {startloc} {endloc} {format -||- ->} {UTC - по желанию, будет дефолт конфиг} {block - по желанию, будет дефолт конфиг}
 направление, куда смотрят часы
 понятно как-бы
 тоже подумать как сделать, обязателен ли вообще этот параметр и как парсить все твердые блоки майнкрафта
 формат? надо подумать надо ли? 12/24
 надо ли координаты начала
 */























return true;
    }
}
