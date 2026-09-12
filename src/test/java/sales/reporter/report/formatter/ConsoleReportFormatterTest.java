package sales.reporter.report.formatter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sales.reporter.model.Product;
import sales.reporter.report.SalesSummary;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConsoleReportFormatterTest {

    private ConsoleReportFormatter formatter;

    @BeforeEach
    void setUp() {
        formatter = new ConsoleReportFormatter();
    }

    @Test
    void format_NullSummary_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> formatter.format(null));
    }

    @Test
    void format_EmptyMetrics_HandlesGracefullyWithFallbacks() {
        SalesSummary summary = new SalesSummary(
                Collections.emptyList(),
                Collections.emptyMap(),
                Collections.emptyMap(),
                null,
                null,
                null
        );

        String result = formatter.format(summary);

        assertTrue(result.contains("No product revenue data available."));
        assertTrue(result.contains("No category revenue data available."));
        assertTrue(result.contains("Best-Selling Product : N/A"));
        assertTrue(result.contains("Highest Revenue      : N/A"));
        assertTrue(result.contains("Grand Total Revenue  : $0.00"));
    }

    @Test
    void format_PopulatedSummary_ContainsFormattedHighlights() {
        Product p = new Product("P201", "Monitor", "Electronics", 3, new BigDecimal("250.00"));
        SalesSummary summary = new SalesSummary(
                List.of(p),
                Map.of(p, new BigDecimal("750.00")),
                Map.of("Electronics", new BigDecimal("750.00")),
                p,
                p,
                new BigDecimal("750.00")
        );

        String result = formatter.format(summary);

        assertTrue(result.contains("PRODUCT SALES SUMMARY REPORT"));
        assertTrue(result.contains("Best-Selling Product : Monitor (3 units)"));
        assertTrue(result.contains("Grand Total Revenue  : $750.00"));
    }
}