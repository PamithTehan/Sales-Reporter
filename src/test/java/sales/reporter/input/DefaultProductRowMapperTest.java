package sales.reporter.input;

import org.junit.jupiter.api.Test;
import sales.reporter.model.Product;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

// NOTE: getter names (getProductId, getProductName, getCategory,
// getQuantitySold, getUnitPrice) are assumed from Product's constructor
// parameter order. Adjust if your actual Product API differs.
class DefaultProductRowMapperTest {

    private final ProductRowMapper rowMapper = new DefaultProductRowMapper(new DefaultRowSplitter());

    @Test
    void parsesValidRowIntoProduct() throws IOException {
        Product product = rowMapper.parseLine("P1,Widget,Tools,10,9.99", 2);

        assertEquals("P1", product.getProductId());
        assertEquals(10, product.getQuantitySold());
    }

    @Test
    void throwsIOException_whenQuantityIsNotNumeric() {
        // Regression test for the "handle csv exception" fix:
        // a bad numeric field must surface as IOException, not crash.
        assertThrows(IOException.class,
                () -> rowMapper.parseLine("P1,Widget,Tools,NOTANUMBER,9.99", 5));
    }

    @Test
    void throwsIOException_whenRowHasTooFewColumns() {
        assertThrows(IOException.class,
                () -> rowMapper.parseLine("P1,Widget", 3));
    }
}
