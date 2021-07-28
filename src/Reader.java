import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.*;

/**
 * This class represents task for simple CSV parser
 */
public class Reader {

    /**
     * This method returns path for storing csv line.
     * @return String path
     */
    private static String getPath() {
        FileInputStream fis;
        Properties property = new Properties();
        try {
            fis = new FileInputStream("resources/config.properties");
            property.load(fis);
            return property.getProperty("path");
        } catch (IOException e) {
            System.err.println("Error: file not found");
        }
        return null;
    }

    /**
     * This method contains logic for printing lines from CSV file.
     * @param resultLine ArrayList<Map<Object, Object>>
     */
    private static void printLines(ArrayList<Map<Object, Object>> resultLine) {
        for (Map<Object, Object> result : resultLine) {
            result.forEach((key, value) -> System.out.print(key.toString() + ": " + value.toString() + "  "));
            System.out.println();
        }
    }

    /**
     * This method contains logic for checking headers and contents lines length.
     * @param arrayList List<Object>
     * @param strings String[]
     * @return true if length is the same, false otherwise.
     */
    private static boolean isLengthSame(List<Object> arrayList, String[] strings) {
        return arrayList.size() == strings.length;
    }

    /**
     * This method contains logic for parsing line from CSV file.
     * @param line String
     * @return String[] of parsed line.
     */
    private static String[] parseLine(String line) {
        return line.contains(",") ? line.split(",") : line.split(";");
    }

    /**
     * This method contains logic for generating headers if they are missing from the file.
     * @param size int
     * @return String[] of generated headers.
     */
    private static String[] generateHeader(int size) {
        String[] arrayOfHeaders = new String[size];
        for (int i = 0; i < arrayOfHeaders.length; i++) {
            arrayOfHeaders[i] = "Header #"  + i;
        }
        return arrayOfHeaders;
    }

    /**
     * This is main method for demonstration
     * @param args String[]
     */
    public static void main(String[] args) {
        String path = getPath();
        String line = "";
        ArrayList<Map<Object, Object>> result = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            List<Object> headers = new ArrayList<>(Arrays.asList(parseLine(br.readLine())));
            String[] content = parseLine(br.readLine());
            if (!isLengthSame(headers, content)) {
                String[] generatedHeaders = generateHeader(Math.abs(headers.size() - content.length));
                headers.addAll(Arrays.asList(generatedHeaders));
            }

            while ((line = br.readLine()) != null) {
                String[] values = parseLine(line);
                Map<Object, Object> mapOfProps = new LinkedHashMap<>();
                for (int i = 0; i < values.length; i++) {
                    mapOfProps.put(headers.get(i), values[i]);
                }
                result.add(mapOfProps);
            }

            //result contains list of parsed data
            printLines(result);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
