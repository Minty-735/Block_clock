package org.minty.block_clock.clocks;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.minty.block_clock.PainterApplication;

import java.util.ArrayList;
import java.util.List;

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

        int x2 = loc2.getBlockX();
        int y2 = loc2.getBlockY();

        int horiz = Math.abs(x2);

        int vert = y2;
        char[][] chats;

//chats[ВЫСОТА][ШИРИНА]
        chats = PainterApplication.calc(time, horiz, vert);

//        createMatrix(loc1, loc2);
        chats = reverseArray(chats);


        Material black = clock.getTextBlock();
        Material white = clock.getBackgroundBlock();

        Location[][] matrix =
                createMatrix(loc1, loc2);


        for (int y = 0; y < chats.length; y++) {
            for (int x = 0; x < chats[0].length; x++) {
                char pixel = chats[y][x];
                //chats[ВЫСОТА][ШИРИНА]

                Location loc = matrix[x*2][y];

                Block blk = loc.getBlock();
                if (pixel == '*') {
                    blk.setType(white);
                } else {
                    blk.setType(black);
                }
            }
        }
    }

    /**
     * [Координаты блока][высота]
     */
    private Location[][] createMatrix(Location loc1, Location loc2) {
        int x1 = (int) loc1.getX();
        int z1 = (int) loc1.getZ();
        int y1 = (int) loc1.getY();

        //todo
        //чекнуть что будет если числа отрицательные (а точнее где ошибка)
        int x2 = (int) loc2.getX();
        int z2 = (int) loc2.getZ();
        int height = (int) loc2.getY();
        World world = loc2.getWorld();

        Location[][] result;
//        Оказывается оно бы не работало если бы я не указывал при тесте 0 0 0 старт лок

//        ArrayList<Integer> test = drawLine(x1, z1, x2, z2);
        ArrayList<Integer> test = drawLine(x1, z1, x2 + x1, z2 + z1);
//пзщахщзпащхзпазщ
//        в теории то что я прибавил значения должно сделать весь мой код рабочим
//        но не факт,
//        все равно проверить что не так, а то что0то не так



//todo ПРОВЕРИТЬ!!!!!! нижнюю строчку
        int len = test.size();
//        test.size()

        result = new Location[len][height];

        for (int i = 0; i < result.length; i+=2) {
            for (int j = 0; j < result[0].length; j++) {
                int sx = x1 + test.get(i);
                int sy = y1 + j;
                int sz = z1 + test.get(i + 1);

                result[i][j] = new Location(world, sx, sy, sz);
//[координаты][высота]

            }
        }

        return result;
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
//        System.out.println();
        return reversedArray;
    }


}

/*

* */
