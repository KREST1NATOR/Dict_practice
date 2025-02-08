import java.util.regex.Pattern;

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
