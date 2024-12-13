package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.image.BufferedImage;

/* Сервис для внедрения и извлечения текстовой информации в BMP-изображение
 * по принципу НЗБ (использование наименее значимых битов).
        */
public class SteganographyService {
    private static final Logger logger = LogManager.getLogger(SteganographyService.class);

    /* Внедряет заданный текст в изображение, модифицируя его младшие биты.
     * Текст кодируется в последовательность бит, которые записываются в синем канале пикселей.
     *
     * @param originalImage исходное изображение
     * @param text текст для внедрения
     * @return новое изображение с внедрённым текстом
     */
    public BufferedImage embedText(BufferedImage originalImage, String text) {
        logger.info("Начало внедрения текста в изображение...");
        // Преобразуем текст в двоичный массив
        byte[] textBytes = text.getBytes();
        // Добавим маркер конца сообщения, например 0x00
        byte[] data = new byte[textBytes.length + 1];
        System.arraycopy(textBytes, 0, data, 0, textBytes.length);
        data[data.length - 1] = 0;

        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        int bitIndex = 0;
        int totalBits = data.length * 8;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = originalImage.getRGB(x, y);

                // Извлечём каналы
                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = rgb & 0xFF;

                if (bitIndex < totalBits) {
                    int byteIndex = bitIndex / 8;
                    int bitPos = bitIndex % 8;
                    int bit = (data[byteIndex] >> bitPos) & 1;

                    // Заменим младший бит синего канала
                    blue = (blue & 0xFE) | bit;
                    bitIndex++;
                }

                int newRgb = (red << 16) | (green << 8) | blue;
                result.setRGB(x, y, newRgb);
            }
        }

        logger.info("Текст успешно внедрён");
        return result;
    }
/* Извлекает текст из переданного изображения, читая биты синего канала.
            *
            * @param stegoImage изображение с внедрённым текстом
     * @return извлечённый текст
     */
    public String extractText(BufferedImage stegoImage) {
        logger.info("Начало извлечения текста из изображения...");
        int width = stegoImage.getWidth();
        int height = stegoImage.getHeight();
        StringBuilder sb = new StringBuilder();

        byte currentByte = 0;
        int bitCount = 0;

        outer:
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = stegoImage.getRGB(x, y);
                int blue = rgb & 0xFF;

                int bit = blue & 1;
                currentByte = (byte) ((currentByte >> 1) | (bit << 7));
                bitCount++;

                if (bitCount == 8) {
                    if (currentByte == 0) {
                        break outer;
                    }
                    sb.append((char)currentByte);
                    bitCount = 0;
                    currentByte = 0;
                }
            }
        }

        String extracted = sb.toString();
        logger.info("Текст извлечён: {}", extracted);
        return extracted;
    }

    /* Генерирует изображение, показывающее только младшие биты синего канала,
            * для наглядной демонстрации внедрения.
            *
            * @param image изображение
     * @return новое изображение с визуализацией младших бит
     */
    public BufferedImage visualizeLSBBits(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage visual = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);
                int blue = rgb & 0xFF;
                int bit = blue & 1;
                // Если бит 1 - делаем пиксель белым, иначе чёрным
                int color = bit == 1 ? 0xFFFFFF : 0x000000;
                visual.setRGB(x, y, color);
            }
        }

        return visual;
    }
}