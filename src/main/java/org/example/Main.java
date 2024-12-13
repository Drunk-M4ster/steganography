package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/* Главный класс приложения.
 * Запускает графический интерфейс для выбора исходного и результирующего изображения,
        * ввода текста и внедрения его по алгоритму НЗБ (LSB) в BMP-изображение.
 */
public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    /* Точка входа в приложение.
            *
            * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        LoggingConfig.initLogging();
        logger.info("Приложение запущено");

        // Запуск GUI
        java.awt.EventQueue.invokeLater(() -> {
            GuiFrame frame = new GuiFrame();
            frame.setVisible(true);
        });
    }
}