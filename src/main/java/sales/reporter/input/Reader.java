package sales.reporter.input;
import sales.reporter.Product;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Reader {

    private final RowSplitter rowSplitter = new RowSplitter();
    private final HeaderDetector headerDetector = new HeaderDetector(rowSplitter);
    private final ProductRowMapper rowMapper = new ProductRowMapper(rowSplitter);

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

                products.add(rowMapper.parseLine(line, lineNumber));
            }
        }

        //handle csv exception

        return products;
    }
}
