import java.io.IOException;

interface Dictionary {
    void load(String filename) throws IOException;
    void save(String filename) throws IOException;
    void addEntry(String key, String value) throws IllegalArgumentException;
    void removeEntry(String key);
    String searchEntry(String key);
    void displayEntriesPaginated(int page, int pageSize);
    void printAsXml();
}