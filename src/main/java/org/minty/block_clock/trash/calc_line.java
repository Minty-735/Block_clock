 package org.minty.block_clock.trash;

import java.util.ArrayList;
import java.util.HashMap;

 public class calc_line {
    //xD своровал свой же код
    public static HashMap<Integer, Integer> drawLine(int x1, int y1, int x2, int y2) {
        ArrayList<Integer> result = new ArrayList<>();
        HashMap<Integer, Integer> result1 = new HashMap<>();


        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);

        int sx = (x1 < x2) ? 1 : -1;
        int sy = (y1 < y2) ? 1 : -1;

        int error = dx - dy;

        int x = x1;
        int y = y1;

        while (true) {
            // Добавляем текущую точку в результат
            result1.put(x,y);
//            result.add(x);
//            result.add(y);

            // Проверяем, достигли ли конечной точки
            if (x == x2 && y == y2) {
                break;
            }

            int doubleError = 2 * error;

            // Пересчитываем ошибку и двигаемся по одной из координат
            if (doubleError > -dy) {
                error -= dy;
                x += sx;
            }

            if (doubleError < dx) {
                error += dx;
                y += sy;
            }
        }

        return result1;
    }
}
