import java.util.*;
import java.io.IOException;

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
            System.out.println("8. Вывести словарь в XML формате");
            System.out.println("9. Выход");
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
                        System.out.println("Вывод словаря в формате XML:");
                        selectedDictionary.printAsXml();
                    }
                    case 9 -> {
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