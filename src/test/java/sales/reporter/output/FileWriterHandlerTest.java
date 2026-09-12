package sales.reporter.output;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileWriterHandlerTest {

    @Test
    void writesReportContentToTheGivenFile(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("output.txt");
        FileWriterHandler handler = new FileWriterHandler(file.toString());

        handler.write("PRODUCT SALES SUMMARY REPORT");

        String contents = Files.readString(file);
        assertEquals("PRODUCT SALES SUMMARY REPORT", contents);
    }

    @Test
    void overwritesExistingFileContentOnEachWrite(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("output.txt");
        FileWriterHandler handler = new FileWriterHandler(file.toString());

        handler.write("first report");
        handler.write("second report");

        String contents = Files.readString(file);
        assertEquals("second report", contents);
    }
}