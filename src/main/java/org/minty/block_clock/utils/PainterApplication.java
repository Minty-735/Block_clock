package org.minty.block_clock.utils;

import org.minty.block_clock.Block_clock;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class PainterApplication {
    /**result[ВЫСОТА][ШИРИНА]
*/
    public static char[][] calc(String text,int horizont, int vertikal) {
//        String text = "12:22:31 AM  ";
        int desiredWidth = horizont;
        int desiredHeight = vertikal;
        // Создаем BufferedImage для измерения текста
        BufferedImage tempImg = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D tempG2d = tempImg.createGraphics();
        char[][] field = new char[desiredHeight][desiredWidth];

        // Заполняем поле пробелами (или другим значением по умолчанию)
        for (int i = 0; i < desiredHeight; i++) {
            Arrays.fill(field[i], ' ');
        }


        // Устанавливаем шрифт
        Font font = new Font(Block_clock.font, Font.PLAIN, 48);
        tempG2d.setFont(font);

        // Получаем метрики шрифта
        FontMetrics fm = tempG2d.getFontMetrics();

        // Вычисляем ширину и высоту изображения
        int width = fm.stringWidth(text);
        int height = fm.getHeight();

        // Создаем изображение с размерами текста
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();

        // Устанавливаем параметры рендеринга
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        // Отрисовываем текст на изображении
        g2d.setFont(font);
        g2d.setColor(Color.BLACK);
        g2d.drawString(text, 0, fm.getAscent());
        g2d.dispose();

        // Растягиваем изображение до желаемых размеров
        BufferedImage resizedImg = new BufferedImage(desiredWidth, desiredHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D resizedG2d = resizedImg.createGraphics();
        resizedG2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        resizedG2d.drawImage(img, 0, 0, desiredWidth, desiredHeight, null);
        resizedG2d.dispose();

//        try {
//            ImageIO.write(resizedImg, "png", new File("Text.png"));
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }

        char[][] result = new char[field.length][field[0].length];
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[0].length; j++) {
                int pixel = resizedImg.getRGB(j, i);
                if ((pixel >> 24) == 0x00) {

                    result[i][j] = '*';

                } else {

                    result[i][j] = ' ';

                }
            }
        }


        return result;

    }

}
