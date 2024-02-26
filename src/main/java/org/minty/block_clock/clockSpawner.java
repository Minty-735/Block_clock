package org.minty.block_clock;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class clockSpawner implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
// я думаю тут можно просто создавать объекты number или что-то такое
/*
/clock {n/e/w/s} {UTC} {block}
        направление, куда смотрят часы
        понятно как-бы
        тоже подумать как сделать, обязателен ли вообще этот параметр и как парсить все твердые блоки майнкрафта
        формат? надо подумать надо ли? 12/24
        надо ли координаты начала

* */


        return true;
    }
}
