package com.example;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/* Утилитный класс для загрузки и сохранения BMP-изображений.
 */
public class ImageUtils {

    /* Загружает BMP-изображение из указанного пути.
            *
            * @param path путь к файлу BMP
     * @return загруженное изображение
     * @throws IOException если чтение файла не удалось
     */
    public static BufferedImage loadBMP(String path) throws IOException {
        return ImageIO.read(new File(path));
    }

    /**
     * Сохраняет изображение в BMP-формате по указанному пути.
     *
     * @param image изображение для сохранения
     * @param path путь для сохранения файла
     * @throws IOException если запись файла не удалась
     */
    public static void saveBMP(BufferedImage image, String path) throws IOException {
        ImageIO.write(image, "bmp", new File(path));
    }
}