import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Cipher {

    private static final String RUSSIAN_ALPHABET = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
    private static final int ALPHABET_LENGTH = RUSSIAN_ALPHABET.length();

    public static String encrypt(String text, int key) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char character = text.charAt(i);

            if (Character.isLetter(character)) {
                boolean isLowerCase = Character.isLowerCase(character);
                character = Character.toUpperCase(character);

                int index = RUSSIAN_ALPHABET.indexOf(character);
                if (index != -1) {
                    int newIndex = (index + key) % ALPHABET_LENGTH;
                    if (newIndex < 0) newIndex += ALPHABET_LENGTH;
                    char newChar = RUSSIAN_ALPHABET.charAt(newIndex);
                    result.append(isLowerCase ? Character.toLowerCase(newChar) : newChar);
                } else {
                    result.append(character);
                }
            } else {
                result.append(character);
            }
        }
        return result.toString();
    }

    public static String decrypt(String text, int key) {
        return encrypt(text, -key);
    }

    public static String readFile(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }

    public static void writeFile(String fileName, String content) throws IOException {
        Files.write(Paths.get(fileName), content.getBytes());
    }

    public static boolean validateFile(String fileName) {
        File file = new File(fileName);
        return file.exists() && file.isFile();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("Введите имя файла для шифрования/расшифровки:");
            String inputFile = scanner.nextLine();

            if (!validateFile(inputFile)) {
                System.out.println("Файл не найден!");
                return;
            }

            String content = readFile(inputFile);

            System.out.println("Выберите режим: 1 - Шифрование, 2 - Расшифровка");
            int mode = scanner.nextInt();
            System.out.println("Введите ключ (целое число):");
            int key = scanner.nextInt();

            String result;
            if (mode == 1) {
                result = encrypt(content, key);
            } else if (mode == 2) {
                result = decrypt(content, key);
            } else {
                System.out.println("Неверный режим работы!");
                return;
            }

            System.out.println("Введите имя выходного файла:");
            String outputFile = scanner.next();

            writeFile(outputFile, result);
            System.out.println("Операция успешно завершена!");

        } catch (IOException e) {
            System.out.println("Ошибка при работе с файлами: " + e.getMessage());
        }
    }
}
