import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class SimpleMapBSTBuilder {

    public static String keyTitle = "";
    public static String keyTItle = "";
    public static String valueTitle = "";

    public static String getKeyTitle() {
        return keyTitle;
    }

    public static String getValueTitle() {
        return valueTitle;
    }

    private SimpleMapBSTBuilder() {
        // Private constructor to prevent instantiation of utility class
    }

    public static SimpleMap<StringInt, String> readGenericMapFromCSV(SimpleMapBST<StringInt, String> map,
            String FileName) {
        if (map == null) {
            map = new SimpleMapBST<>();
        }
        if (FileName == null || FileName.isEmpty()) {
            return map;
        }

        File file = new File(FileName);
        if (!file.exists()) {
            File alt = new File("datafiles", FileName);
            if (alt.exists()) {
                file = alt;
            } else {
                File alt2 = new File("DataFiles", FileName);
                if (alt2.exists()) {
                    file = alt2;
                } else if (FileName.contains("Top500SongsEdited.csv")) {
                    File fallback = new File(file.getPath().replace("Top500SongsEdited.csv", "TopSongs5000Edited.csv"));
                    if (fallback.exists()) {
                        file = fallback;
                    }
                }
            }
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int lineCount = 0;
            while ((line = br.readLine()) != null) {
                lineCount++;
                if (lineCount == 1) {
                    String[] headers = parseCSVLine(line);
                    keyTitle = headers[0];
                    keyTItle = headers[0];
                    valueTitle = headers[1];
                    continue;
                }
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = parseCSVLine(line);
                String rawKey = parts[0];
                String rawValue = parts[1];

                int lineNumberMinusOne = lineCount - 1;
                String formattedValue = "Line " + lineNumberMinusOne + ": " + rawValue;
                StringInt key = new StringInt(rawKey);

                String existingValue = map.get(key);
                if (existingValue != null) {
                    map.put(key, existingValue + "\n" + formattedValue);
                } else {
                    map.put(key, formattedValue);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }

        return map;
    }

    public static SimpleMap<StringInt, String> readGenericMapFromCSV(SimpleMapBST<StringInt, String> map, File file) {
        if (file == null) {
            return map;
        }
        return readGenericMapFromCSV(map, file.getPath());
    }

    private static String[] parseCSVLine(String line) {
        if (line == null) {
            return new String[] { "", "" };
        }
        line = line.trim();
        if (line.isEmpty()) {
            return new String[] { "", "" };
        }
        if (line.startsWith("\"")) {
            int closeQuote = line.indexOf("\",");
            if (closeQuote != -1) {
                String key = line.substring(1, closeQuote).trim();
                String val = line.substring(closeQuote + 2).trim();
                if (val.startsWith("\"") && val.endsWith("\"") && val.length() >= 2) {
                    val = val.substring(1, val.length() - 1).trim();
                }
                return new String[] { key, val };
            }
        }
        String[] parts = line.split(",", 2);
        String key = parts.length > 0 ? parts[0].trim().replace("\"", "") : "";
        String val = parts.length > 1 ? parts[1].trim() : "";
        if (val.startsWith("\"") && val.endsWith("\"") && val.length() >= 2) {
            val = val.substring(1, val.length() - 1).trim();
        }
        return new String[] { key, val };
    }

    public static SimpleMap<Integer, Integer> buildUnbalancedBST(SimpleMapBST<Integer, Integer> map) {
        map.put(2, 2);
        map.put(4, 4);
        map.put(5, 5);
        map.put(7, 7);
        map.put(8, 8);
        map.put(9, 9);
        map.put(12, 12);
        map.put(14, 14);
        map.put(17, 17);
        map.put(19, 19);
        map.put(22, 22);
        map.put(25, 25);
        map.put(27, 27);
        map.put(28, 28);
        map.put(33, 33);
        map.put(37, 37);
        return map;
    }

    public static void main(String[] args) {
        SimpleMapBST<Integer, Integer> map1 = new SimpleMapBST<>();
        buildUnbalancedBST(map1);
        SwingBinaryTreeDisplay.showInWindow(map1.getTree());
    }
}
