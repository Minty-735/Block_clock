package org.minty.block_clock.clocks;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.minty.block_clock.PainterApplication;

import java.util.ArrayList;

import static org.minty.block_clock.Block_clock.Clocks;
import static org.minty.block_clock.trash.calc_line.drawLine;

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

        int horiz = x2;

        int vert = y2;
        char[][] chats = new char[vert][horiz];


        chats = PainterApplication.calc(time, horiz, vert);
//chats - массив который показывает картинку
        createMatrix(loc1, loc2);
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


    private Location[][] createMatrix(Location loc1, Location loc2) {
        int x1 = (int) loc1.getX();
        int z1 = (int) loc1.getZ();
        int x2 = (int) loc2.getX();
        int z2 = (int) loc2.getZ();

        ArrayList<Integer> test = drawLine(x1,z1,x2,z2);
//todo Доделать!!!!
//        по теореме пифагора узнать длинну и ее как эээээээ
//        char[сюда кароч][а сюда высоту]



        for (int i = 0; i < test.size(); i += 2) {
            System.out.println(test.get(i).toString() + " - " + test.get(i + 1).toString());

        }

        int len = (int) Math.sqrt((double) (3 * 3 + 4 * 4));
        System.out.println(len);
        ArrayList<Integer> test;
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
