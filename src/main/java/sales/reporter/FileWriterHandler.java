package sales.reporter;

import java.io.FileWriter;
import java.io.IOException;

public class FileWriterHandler {

    public void write(String outputFilePath, String reportContent) throws IOException {
        FileWriter writer = new FileWriter(outputFilePath);
        writer.write(reportContent);
        writer.close();
    }
}