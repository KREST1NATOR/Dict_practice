import java.util.regex.Pattern;

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