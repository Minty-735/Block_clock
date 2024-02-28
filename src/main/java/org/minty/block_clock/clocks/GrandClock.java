package org.minty.block_clock.clocks;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.minty.block_clock.PainterApplication;

import static org.minty.block_clock.Block_clock.Clocks;

public class GrandClock {
    private Clock clock;

    public GrandClock(Clock clock) {
        this.clock = clock;
        Clocks.add(this);

    }



    public void UpdateTime() {
        String time = clock.updateTime();


        Location loc1 = clock.getStartLocation();
        Location loc2 = clock.getEndlocation();

        int x1 = loc1.getBlockX();
        int y1 = loc1.getBlockY();
        int z1 = loc1.getBlockZ();
        int x2 = loc2.getBlockX();
        int y2 = loc2.getBlockY();
        int z2 = loc2.getBlockZ();
/*

x
y
z
200
0
50


x
y
z
100
50
50








 */

        int horiz = Math.abs(x2 - x1);

        int vert = Math.abs(y1 - y2);
        char[][] chats = new char[vert][horiz];


        chats = PainterApplication.calc(time, horiz, vert);

        chats = reverseArray(chats);
        Material black = clock.getTextBlock();
        Material white = clock.getBackgroundBlock();
        for (int y = 0; y < chats.length; y++) {
            for (int x = 0; x < chats[0].length; x++) {
                char pixel = chats[y][x];

                Location loc = new Location(loc1.getWorld(), x + x1, y + y1, z1);

                Block blk = loc.getBlock();
                if (pixel == '*') {
                    blk.setType(white);
                } else {
                    blk.setType(black);


                }
            }
        }
    }

    private char[][] reverseArray(char[][] chars) {
        int rows = chars.length;
        int cols = chars[0].length;

        // Создание нового массива для перевернутых значений
        char[][] reversedArray = new char[rows][cols];

        // Переворачиваем исходный массив
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                reversedArray[i][j] = chars[rows - i - 1][cols - j - 1];
            }
        }

        // Выводим перевернутый массив

        return reversedArray;
    }


}
