package sales.reporter.input;
import sales.reporter.Product;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Reader {
    private static final int EXPECTED_COLUMNS = 5;

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

                if (!headerSkipped && looksLikeHeader(line)) {
                    headerSkipped = true;
                    continue;
                }
                headerSkipped = true; // only the very first non-blank line can be a header

                products.add(parseLine(line, lineNumber));
            }
        }

        //handle csv exception

        return products;

    }

    private boolean looksLikeHeader(String line) {
        String[] tokens = splitRow(line);
        if (tokens.length < EXPECTED_COLUMNS) {
            return true; // malformed first line - safest to skip and let real rows be validated
        }
        Integer.parseInt(tokens[3].trim());
        return false; // fourth column parses as a number -> this is a real data row

        //catch number format exception
    }

    private String[] splitRow(String line) {
        return line.split(",");
    }

    private Product parseLine(String line, int lineNumber)  {
        String[] tokens = splitRow(line);

        String productId = tokens[0].trim();
        String productName = tokens[1].trim();
        String category = tokens[2].trim();
        String quantityRaw = tokens[3].trim();
        String priceRaw = tokens[4].trim();


        int quantitySold = Integer.parseInt(quantityRaw);
        BigDecimal unitPrice = new BigDecimal(priceRaw);

        return new Product(productId, productName, category, quantitySold, unitPrice);

    }

}
