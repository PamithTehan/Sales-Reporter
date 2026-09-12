package sales.reporter;

import java.io.FileWriter;
import java.io.IOException;

public class FileWriterHandler implements OutputWriter {

    private final String outputFilePath;

    public FileWriterHandler(String outputFilePath) {
        this.outputFilePath = outputFilePath;
    }

    @Override
    public void write(String reportContent) throws IOException {
        FileWriter writer = new FileWriter(outputFilePath);
        writer.write(reportContent);
        writer.close();
    }
}