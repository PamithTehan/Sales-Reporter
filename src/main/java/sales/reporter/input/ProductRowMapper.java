package sales.reporter.input;

import sales.reporter.model.Product;

public interface ProductRowMapper {
    Product parseLine(String line, int lineNumber);
}
