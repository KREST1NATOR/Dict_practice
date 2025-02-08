import java.util.regex.Pattern;

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