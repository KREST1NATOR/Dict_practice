import java.io.*;
import java.util.*;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.IOException;

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

    @Override
    public void printAsXml() {
        try {
            XmlMapper xmlMapper = new XmlMapper();
            String xmlOutput = xmlMapper.writerWithDefaultPrettyPrinter().writeValueAsString(entries);
            System.out.println(xmlOutput);
        } catch (IOException e) {
            System.out.println("Ошибка при выводе в XML: " + e.getMessage());
        }
    }
}