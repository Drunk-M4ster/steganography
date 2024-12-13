package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/* Класс графического интерфейса для взаимодействия с пользователем.
 * Позволяет выбрать исходное изображение, ввести текст, внедрить текст,
 * а также просмотреть визуализацию младших бит.
        */
public class GuiFrame extends JFrame {
    private static final Logger logger = LogManager.getLogger(GuiFrame.class);

    private JTextField originalPathField;
    private JTextField stegoPathField;
    private JTextArea textArea;
    private JLabel originalImageLabel;
    private JLabel stegoImageLabel;
    private JLabel lsbImageLabel;

    private BufferedImage originalImage;
    private BufferedImage stegoImage;
    private BufferedImage lsbImage;
    private SteganographyService service;

    /* Конструктор GUI.
            * Инициализирует компоненты и их расположение.
     */
    public GuiFrame() {
        super("BMP Steganography (НЗБ)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        service = new SteganographyService();
        initUI();
    }

    /* Инициализация пользовательского интерфейса.
     */
    private void initUI() {
        JPanel inputPanel = new JPanel(new GridLayout(3, 3, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Параметры"));

        originalPathField = new JTextField();
        stegoPathField = new JTextField();
        textArea = new JTextArea(3, 20);

        JButton loadOriginalBtn = new JButton("Загрузить исходное");
        loadOriginalBtn.addActionListener(e -> loadOriginalImage());

        JButton embedTextBtn = new JButton("Внедрить текст");
        embedTextBtn.addActionListener(e -> embedText());

        JButton saveStegoBtn = new JButton("Сохранить результат");
        saveStegoBtn.addActionListener(e -> saveStegoImage());

        inputPanel.add(new JLabel("Путь к исходному BMP:"));
        inputPanel.add(originalPathField);
        inputPanel.add(loadOriginalBtn);

        inputPanel.add(new JLabel("Путь к результирующему BMP:"));
        inputPanel.add(stegoPathField);
        inputPanel.add(saveStegoBtn);

        inputPanel.add(new JLabel("Текст для внедрения:"));
        inputPanel.add(new JScrollPane(textArea));
        inputPanel.add(embedTextBtn);

        originalImageLabel = new JLabel();
        stegoImageLabel = new JLabel();
        lsbImageLabel = new JLabel();

        JPanel imagesPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        imagesPanel.setBorder(BorderFactory.createTitledBorder("Изображения"));
        imagesPanel.add(new JScrollPane(originalImageLabel));
        imagesPanel.add(new JScrollPane(stegoImageLabel));
        imagesPanel.add(new JScrollPane(lsbImageLabel));

        getContentPane().add(inputPanel, BorderLayout.NORTH);
        getContentPane().add(imagesPanel, BorderLayout.CENTER);
    }

    /* Загрузка исходного изображения.
     */
    private void loadOriginalImage() {
        try {
            originalImage = ImageUtils.loadBMP(originalPathField.getText().trim());
            originalImageLabel.setIcon(new ImageIcon(originalImage));
            lsbImage = service.visualizeLSBBits(originalImage);
            lsbImageLabel.setIcon(new ImageIcon(lsbImage));
            logger.info("Исходное изображение загружено.");
        } catch (IOException ex) {
            logger.error("Ошибка загрузки изображения: {}", ex.getMessage());
            JOptionPane.showMessageDialog(this, "Ошибка загрузки изображения: " + ex.getMessage(),
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }
    /* Внедрение текста в изображение.
            */
    private void embedText() {
        if (originalImage == null) {
            JOptionPane.showMessageDialog(this, "Сначала загрузите исходное изображение!",
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String text = textArea.getText();
        if (text.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Введите текст для внедрения!",
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        stegoImage = service.embedText(originalImage, text);
        stegoImageLabel.setIcon(new ImageIcon(stegoImage));
        // Обновим визуализацию LSB, теперь для стеганографированного изображения
        lsbImage = service.visualizeLSBBits(stegoImage);
        lsbImageLabel.setIcon(new ImageIcon(lsbImage));
        logger.info("Текст внедрён в изображение");
    }

    /* Сохранение результирующего изображения.
     */
    private void saveStegoImage() {
        if (stegoImage == null) {
            JOptionPane.showMessageDialog(this, "Нет результирующего изображения для сохранения!",
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            ImageUtils.saveBMP(stegoImage, stegoPathField.getText().trim());
            logger.info("Результирующее изображение сохранено.");
            JOptionPane.showMessageDialog(this, "Изображение успешно сохранено!",
                    "Успех", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            logger.error("Ошибка сохранения изображения: {}", ex.getMessage());
            JOptionPane.showMessageDialog(this, "Ошибка сохранения изображения: " + ex.getMessage(),
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }
}