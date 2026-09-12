package sales.reporter.input;

import sales.reporter.model.Product;
import java.io.IOException;

public interface ProductRowMapper {
    Product parseLine(String line, int lineNumber) throws IOException;
}
