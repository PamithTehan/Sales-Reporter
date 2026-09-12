package sales.reporter.input;
import sales.reporter.model.Product;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Reader {

    private final RowSplitter rowSplitter;
    private final HeaderDetector headerDetector;
    private final ProductRowMapper rowMapper;

    public Reader(RowSplitter rowSplitter, HeaderDetector headerDetector, ProductRowMapper rowMapper) {
        this.rowSplitter = rowSplitter;
        this.headerDetector = headerDetector;
        this.rowMapper = rowMapper;
    }

    public Reader() {
        this(new DefaultRowSplitter());
    }

    private Reader(RowSplitter rowSplitter) {
        this(rowSplitter, new DefaultHeaderDetector(rowSplitter), new DefaultProductRowMapper(rowSplitter));
    }

    public List<Product> readProducts(String filePath)
            throws IOException {

        Path path = Path.of(filePath);
        if (!Files.exists(path)) {
            throw new FileNotFoundException("CSV file not found: " + filePath);
        }

        List<Product> products = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNumber = 0;
            boolean headerSkipped = false;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                if (line.isBlank()) {
                    continue; // ignore blank lines anywhere in the file
                }

                if (!headerSkipped && headerDetector.looksLikeHeader(line)) {
                    headerSkipped = true;
                    continue;
                }
                headerSkipped = true; // only the very first non-blank line can be a header

                try {
                    products.add(rowMapper.parseLine(line, lineNumber));
                } catch (IOException e) {
                    // handle csv exception: skip the bad row and keep reading rather than
                    // aborting the whole file. Change to `throw e;` if you want fail-fast instead.
                    System.err.println("Skipping malformed row: " + e.getMessage());
                }
            }
        }

        return products;
    }
}
