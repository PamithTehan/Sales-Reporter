package sales.reporter;

import java.io.FileWriter;
import java.io.IOException;

public class Outputhandler {

    public void output(String outputMethod, String outputFilePath, String reportContent) {
        if (outputMethod.equals("console")) {
            System.out.println(reportContent);
        } else if (outputMethod.equals("file")) {
            try {
                FileWriter writer = new FileWriter(outputFilePath);
                writer.write(reportContent);
                writer.close();
            } catch (IOException e) {
                System.out.println("Error writing to file: " + e.getMessage());
                System.exit(1);
            }
        } else {
            System.out.println("Invalid output method: " + outputMethod);
            System.exit(1);
        }
    }
}