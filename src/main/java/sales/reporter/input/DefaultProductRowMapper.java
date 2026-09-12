package sales.reporter.input;

import sales.reporter.model.Product;
import java.math.BigDecimal;

public class DefaultProductRowMapper implements ProductRowMapper {

    private final RowSplitter rowSplitter;

    public DefaultProductRowMapper(RowSplitter rowSplitter) {
        this.rowSplitter = rowSplitter;
    }

    @Override
    public Product parseLine(String line, int lineNumber) throws IOException {
        String[] tokens = rowSplitter.splitRow(line);

        try {
            String productId = tokens[0].trim();
            String productName = tokens[1].trim();
            String category = tokens[2].trim();
            String quantityRaw = tokens[3].trim();
            String priceRaw = tokens[4].trim();

            int quantitySold = Integer.parseInt(quantityRaw);
            BigDecimal unitPrice = new BigDecimal(priceRaw);

            return new Product(productId, productName, category, quantitySold, unitPrice);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            throw new IOException("Invalid row at line " + lineNumber + ": '" + line + "'", e);
        }
    }
}
