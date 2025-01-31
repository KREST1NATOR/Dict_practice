import java.io.*;
import java.util.*;
import java.util.regex.*;

// Интерфейс для работы со словарем
interface Dictionary {
    void load(String filename) throws IOException;
    void save(String filename) throws IOException;
    void addEntry(String key, String value) throws IllegalArgumentException;
    void removeEntry(String key);
    String searchEntry(String key);
    void displayEntriesPaginated(int page, int pageSize);
}

// Абстрактный класс для базового функционала
abstract class AbstractDictionary implements Dictionary {
    protected Map<String, String> entries = new HashMap<>();

    @Override
    public void load(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    entries.put(parts[0], parts[1]);
                }
            }
        }
    }

    @Override
    public void save(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Map.Entry<String, String> entry : entries.entrySet()) {
                writer.write(entry.getKey() + "=" + entry.getValue());
                writer.newLine();
            }
        }
    }

    @Override
    public void removeEntry(String key) {
        entries.remove(key);
    }

    @Override
    public String searchEntry(String key) {
        return entries.get(key);
    }

    @Override
    public void displayEntriesPaginated(int page, int pageSize) {
        List<Map.Entry<String, String>> entryList = new ArrayList<>(entries.entrySet());

        int totalPages = (int) Math.ceil((double) entryList.size() / pageSize);
        if (page < 1 || page > totalPages) {
            System.out.println("Страница не найдена.");
            return;
        }

        System.out.println("\nСтраница " + page + " из " + totalPages);
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, entryList.size());

        for (int i = start; i < end; i++) {
            Map.Entry<String, String> entry = entryList.get(i);
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }
    }
}

// Первый словарь
class FirstDictionary extends AbstractDictionary {
    private static final Pattern VALID_KEY = Pattern.compile("^[a-zA-Z]{4}$");

    @Override
    public void addEntry(String key, String value) throws IllegalArgumentException {
        if (!VALID_KEY.matcher(key).matches()) {
            throw new IllegalArgumentException("Ключ должен состоять ровно из 4 латинских букв.");
        }
        entries.put(key, value);
    }
}

// Второй словарь
class SecondDictionary extends AbstractDictionary {
    private static final Pattern VALID_KEY = Pattern.compile("^\\d{5}$");

    @Override
    public void addEntry(String key, String value) throws IllegalArgumentException {
        if (!VALID_KEY.matcher(key).matches()) {
            throw new IllegalArgumentException("Ключ должен состоять ровно из 5 цифр.");
        }
        entries.put(key, value);
    }
}

// Третий словарь
class ThirdDictionary extends AbstractDictionary {
    private static final Pattern VALID_KEY = Pattern.compile("^[a-z#]+$");

    @Override
    public void addEntry(String key, String value) {
        if (!VALID_KEY.matcher(key).matches()) {
            throw new IllegalArgumentException("Ключ должен содержать только строчные латинские буквы и символ #.");
        }
        String processedKey = processKey(key);
        if (entries.keySet().stream().anyMatch(existingKey -> processKey(existingKey).equals(processedKey))) {
            throw new IllegalArgumentException("Ключ уже существует в словаре с учетом #.");
        }
        entries.put(key, value);
    }

    private String processKey(String key) {
        StringBuilder sb = new StringBuilder();
        for (char c : key.toCharArray()) {
            if (c == '#') {
                if (sb.length() > 0) {
                    sb.deleteCharAt(sb.length() - 1);
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}

// Консольное приложение
public class DictionaryApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Dictionary firstDictionary = new FirstDictionary();
        Dictionary secondDictionary = new SecondDictionary();
        Dictionary thirdDictionary = new ThirdDictionary();

        try {
            firstDictionary.load("first_dict.txt");
            System.out.println("Первый словарь загружен.");
        } catch (IOException e) {
            System.out.println("Ошибка загрузки первого словаря.");
        }

        try {
            secondDictionary.load("second_dict.txt");
            System.out.println("Второй словарь загружен.");
        } catch (IOException e) {
            System.out.println("Ошибка загрузки второго словаря.");
        }

        try {
            thirdDictionary.load("third_dict.txt");
            System.out.println("Третий словарь загружен.");
        } catch (IOException e) {
            System.out.println("Ошибка загрузки третьего словаря.");
        }

        int currentPage = 1;
        int pageSize = 5;
        Dictionary selectedDictionary = firstDictionary; // По умолчанию первый словарь

        while (true) {
            System.out.println("\nКонсоль словаря");
            System.out.println("1. Показать страницу словаря");
            System.out.println("2. Следующая страница");
            System.out.println("3. Предыдущая страница");
            System.out.println("4. Добавить запись");
            System.out.println("5. Удалить запись");
            System.out.println("6. Найти запись");
            System.out.println("7. Выбрать словарь");
            System.out.println("8. Выход");
            System.out.print("Выберите нужный вариант: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (choice) {
                    case 1 -> selectedDictionary.displayEntriesPaginated(currentPage, pageSize);
                    case 2 -> {
                        currentPage++;
                        selectedDictionary.displayEntriesPaginated(currentPage, pageSize);
                    }
                    case 3 -> {
                        if (currentPage > 1) currentPage--;
                        selectedDictionary.displayEntriesPaginated(currentPage, pageSize);
                    }
                    case 4 -> {
                        System.out.print("Введите ключ: ");
                        String key = scanner.nextLine();
                        System.out.print("Введите значение: ");
                        String value = scanner.nextLine();
                        try {
                            selectedDictionary.addEntry(key, value);
                            String filename = (selectedDictionary == firstDictionary) ? "first_dict.txt" :
                                    (selectedDictionary == secondDictionary) ? "second_dict.txt" : "third_dict.txt";
                            selectedDictionary.save(filename);
                            System.out.println("Запись добавлена и сохранена.");
                        } catch (IllegalArgumentException e) {
                            System.out.println("Ошибка: " + e.getMessage());
                        }
                    }
                    case 5 -> {
                        System.out.print("Введите ключ: ");
                        String key = scanner.nextLine();
                        selectedDictionary.removeEntry(key);
                        String filename = (selectedDictionary == firstDictionary) ? "first_dict.txt" :
                                (selectedDictionary == secondDictionary) ? "second_dict.txt" : "third_dict.txt";
                        selectedDictionary.save(filename);
                        System.out.println("Запись удалена.");
                    }
                    case 6 -> {
                        System.out.print("Введите ключ: ");
                        String key = scanner.nextLine();
                        String value = selectedDictionary.searchEntry(key);
                        System.out.println(value != null ? "Значение: " + value : "Запись не найдена.");
                    }
                    case 7 -> {
                        System.out.print("Выберите словарь (1 - первый, 2 - второй, 3 - третий): ");
                        int dictChoice = scanner.nextInt();
                        selectedDictionary = (dictChoice == 1) ? firstDictionary : (dictChoice == 2) ? secondDictionary : thirdDictionary;
                        currentPage = 1;
                        System.out.println("Выбран " + dictChoice + " словарь.");
                    }
                    case 8 -> {
                        System.out.println("Выход из программы.");
                        return;
                    }
                    default -> System.out.println("Неверный ввод, попробуйте снова.");
                }
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }
}