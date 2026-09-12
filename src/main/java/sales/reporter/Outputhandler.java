package sales.reporter;

import java.io.IOException;

public class Outputhandler {

    private final ConsoleWriter consoleWriter = new ConsoleWriter();
    private final FileWriterHandler fileWriterHandler = new FileWriterHandler();

    public void output(String outputMethod, String outputFilePath, String reportContent) {
        if (outputMethod.equals("console")) {
            consoleWriter.write(reportContent);
        } else if (outputMethod.equals("file")) {
            try {
                fileWriterHandler.write(outputFilePath, reportContent);
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