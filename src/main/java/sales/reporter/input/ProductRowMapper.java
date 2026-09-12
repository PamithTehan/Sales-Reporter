package sales.reporter.input;

import sales.reporter.Product;
import java.math.BigDecimal;

public class ProductRowMapper {

    private final RowSplitter rowSplitter;

    public ProductRowMapper(RowSplitter rowSplitter) {
        this.rowSplitter = rowSplitter;
    }

    public Product parseLine(String line, int lineNumber) {
        String[] tokens = rowSplitter.splitRow(line);

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
