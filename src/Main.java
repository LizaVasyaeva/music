import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== Загрузчик музыки ===");
        createSampleUrlsFile();
        String musicFolder = "music/";
        createFolder(musicFolder);
        List<String> urls = readUrlsFromFile("urls.txt");
        for (int i = 0; i < urls.size(); i++) {
            String url = urls.get(i);
            System.out.println("Скачивание " + (i + 1) + " из " + urls.size() + ": " + url);

            String fileName = musicFolder + "song_" + (i + 1) + ".mp3";
            downloadMusic(url, fileName);
        }
        System.out.println("=== Загрузка завершена ===");
    }

    private static void createSampleUrlsFile() {
        File urlFile = new File("urls.txt");
        if (!urlFile.exists()) {
            try (PrintWriter writer = new PrintWriter(new FileWriter("urls.txt"))) {
                writer.println("# Тестовые ссылки (могут не работать):");
                writer.println("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3");
                writer.println("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3");
                writer.println("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3");
            } catch (IOException e) {
                System.out.println("Ошибка при создании файла urls.txt: " + e.getMessage());
            }
        }
    }

    private static void createFolder(String folderName) {
        File folder = new File(folderName);
        if (!folder.exists()) {
            folder.mkdirs();
            System.out.println("Создана папка: " + folderName);
        }
    }

    private static List<String> readUrlsFromFile(String filename) {
        List<String> urls = new ArrayList<>();

        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String url = scanner.nextLine().trim();
                if (!url.isEmpty() && !url.startsWith("#")) {
                    urls.add(url);
                }
            }
            scanner.close();

            System.out.println("Найдено URL: " + urls.size());

        } catch (FileNotFoundException e) {
            System.out.println("Файл " + filename + " не найден");
        }

        return urls;
    }

    private static void downloadMusic(String musicUrl, String fileName) {
        try {
            System.out.println("Загрузка: " + fileName);

            URL url = new URL(musicUrl);
            ReadableByteChannel channel = Channels.newChannel(url.openStream());
            FileOutputStream output = new FileOutputStream(fileName);
            output.getChannel().transferFrom(channel, 0, Long.MAX_VALUE);
            output.close();
            channel.close();
            System.out.println("Успешно: " + fileName);

        } catch (Exception e) {
            System.out.println("Ошибка: " + fileName + " - " + e.getMessage());
        }
    }
}