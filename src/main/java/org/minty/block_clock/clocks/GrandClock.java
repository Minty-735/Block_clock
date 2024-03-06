package org.minty.block_clock.clocks;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.minty.block_clock.PainterApplication;

import java.util.ArrayList;

import static org.minty.block_clock.Block_clock.grandClocks;
import static org.minty.block_clock.trash.calc_line.drawLine;

public class GrandClock {
    private Clock clock;

    public Location[][] getMatrix() {
        return matrix;
    }

    private Location[][] matrix;

    public GrandClock(Clock clock) {
        this.clock = clock;
        grandClocks.add(this);

    }


    public void remove() {

        grandClocks.remove(this);

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

        chats = reverseArray(chats);


        Material black = clock.getTextBlock();
        Material white = clock.getBackgroundBlock();

//        Location[][] matrix =
        this.matrix = createMatrix(loc1, loc2);


        for (int y = 0; y < chats.length; y++) {
            for (int x = 0; x < chats[0].length; x++) {
                char pixel = chats[y][x];
                //chats[ВЫСОТА][ШИРИНА]

                Location loc = matrix[x * 2][y];

                Block blk = loc.getBlock();
                if (pixel == '*') {
                    if (blk.getType() != white) {
                        blk.setType(white);
                    }
                } else {

                    if (blk.getType() != black) {
                        blk.setType(black);
                    }
//                    blk.setType(black);
                }
            }
        }
    }


    public void removeBlocks() {
        for (Location[] loc : matrix) {
            for (Location locBlock : loc) {
                if (locBlock != null) locBlock.getBlock().setType(Material.AIR);
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

        //чекнуть что будет если числа отрицательные (а точнее где ошибка)
        int x2 = (int) loc2.getX();
        int z2 = (int) loc2.getZ();
        int height = (int) loc2.getY();

        World world = loc2.getWorld();
        Location[][] result;
        ArrayList<Integer> test = drawLine(x1, z1, x2 + x1, z2 + z1);

        int len = test.size();
        result = new Location[len][height];

        for (int i = 0; i < result.length; i += 2) {
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

        char[][] reversedArray = new char[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                reversedArray[i][j] = chars[rows - i - 1][cols - j - 1];
            }
        }
        return reversedArray;
    }
}