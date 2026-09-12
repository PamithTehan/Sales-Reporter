package sales.reporter.input;

import sales.reporter.Product;

public interface ProductRowMapper {
    Product parseLine(String line, int lineNumber);
}
