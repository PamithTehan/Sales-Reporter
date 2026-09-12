package sales.reporter;

import java.io.IOException;

public class Outputhandler {

    public void output(OutputWriter writer, String reportContent) {
        try {
            writer.write(reportContent);
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
            System.exit(1);
        }
    }
}