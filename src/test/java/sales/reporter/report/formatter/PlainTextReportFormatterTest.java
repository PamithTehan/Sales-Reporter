package sales.reporter.report.formatter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sales.reporter.model.Product;
import sales.reporter.report.SalesSummary;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlainTextReportFormatterTest {

    private PlainTextReportFormatter formatter;

    @BeforeEach
    void setUp() {
        formatter = new PlainTextReportFormatter();
    }

    @Test
    void format_NullSummary_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> formatter.format(null));
    }

    @Test
    void format_ValidSummary_GeneratesExpectedPlainTextSections() {
        Product p1 = new Product("P101", "Mouse", "Electronics", 5, new BigDecimal("20.00")); // Rev: 100.00
        List<Product> products = List.of(p1);

        Map<Product, BigDecimal> productRevenue = Map.of(p1, new BigDecimal("100.00"));
        Map<String, BigDecimal> categoryRevenue = Map.of("Electronics", new BigDecimal("100.00"));

        SalesSummary summary = new SalesSummary(
                products,
                productRevenue,
                categoryRevenue,
                p1,
                p1,
                new BigDecimal("100.00")
        );

        String result = formatter.format(summary);

        // Verify key structural headers and labels
        assertTrue(result.contains("PRODUCT SALES SUMMARY REPORT"));
        assertTrue(result.contains("--- Revenue Per Product ---"));

        // Match the column-padded format (%-10s %-20s %-15s $%.2f)
        assertTrue(result.contains(String.format("%-10s %-20s %-15s $%.2f", "P101", "Mouse", "Electronics", new BigDecimal("100.00"))));

        assertTrue(result.contains("--- Revenue Per Category ---"));
        assertTrue(result.contains(String.format("%-20s : $%.2f", "Electronics", new BigDecimal("100.00"))));

        assertTrue(result.contains("--- Highlights ---"));
        assertTrue(result.contains("Best-Selling Product : Mouse (5 units)"));
        assertTrue(result.contains("Grand Total Revenue  : $100.00"));
    }
}