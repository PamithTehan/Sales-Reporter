package sales.reporter.input;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import sales.reporter.model.Product;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReaderTest {

    @TempDir
    Path tempDir;

    private final Reader reader = new Reader(); // uses the default collaborators end-to-end

    private String writeCsv(String content) throws IOException {
        Path file = tempDir.resolve("products.csv");
        Files.writeString(file, content);
        return file.toString();
    }

    @Test
    void readsAllRows_whenFileHasHeaderAndBlankLines() throws Exception {
        String path = writeCsv(
                "ProductId,Name,Category,Qty,Price\n" +
                "\n" +
                "P1,Widget,Tools,10,9.99\n" +
                "P2,Gadget,Electronics,5,19.99\n"
        );

        List<Product> products = reader.readProducts(path);

        assertEquals(2, products.size());
        assertEquals("P1", products.get(0).getProductId());
    }

    @Test
    void skipsMalformedRows_andStillReturnsGoodOnes() throws Exception {
        String path = writeCsv(
                "ProductId,Name,Category,Qty,Price\n" +
                "P1,Widget,Tools,NOTANUMBER,9.99\n" + // malformed - skipped
                "P2,Gadget,Electronics,5,19.99\n"      // valid - kept
        );

        List<Product> products = reader.readProducts(path);

        assertEquals(1, products.size());
        assertEquals("P2", products.get(0).getProductId());
    }

    @Test
    void throwsFileNotFoundException_whenFileDoesNotExist() {
        String missingPath = tempDir.resolve("does-not-exist.csv").toString();

        assertThrows(FileNotFoundException.class, () -> reader.readProducts(missingPath));
    }

    @Test
    void throwsCsvParseException_whenFileHasNoDataRows() throws Exception {
        String path = writeCsv("ProductId,Name,Category,Qty,Price\n");

        assertThrows(CsvParseException.class, () -> reader.readProducts(path));
    }
}
