package sales.reporter.report;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sales.reporter.model.Product;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SalesCalculatorTest {

    private SalesCalculatorService calculator;

    @BeforeEach
    void setUp() {
        calculator = new SalesCalculator();
    }

    @Test
    void calculate_NullProductList_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> calculator.calculate(null));
    }

    @Test
    void calculate_EmptyProductList_ThrowsIllegalArgumentException() {
        List<Product> emptyList = Collections.emptyList();
        assertThrows(IllegalArgumentException.class, () -> calculator.calculate(emptyList));
    }

    @Test
    void calculate_ValidProducts_CalculatesAggregatesCorrectly() {
        // Arrange: Sample test fixture with multiple products and categories
        Product p1 = new Product("P101", "Wireless Mouse", "Electronics", 10, new BigDecimal("20.00")); // Rev: 200.00
        Product p2 = new Product("P102", "Mechanical Keyboard", "Electronics", 2, new BigDecimal("150.00")); // Rev: 300.00
        Product p3 = new Product("P103", "Ergonomic Chair", "Furniture", 5, new BigDecimal("100.00")); // Rev: 500.00

        List<Product> products = List.of(p1, p2, p3);

        // Act
        SalesSummary summary = calculator.calculate(products);

        // Assert
        assertNotNull(summary);

        // 1. Verify Grand Total: 200 + 300 + 500 = 1000.00
        assertEquals(new BigDecimal("1000.00"), summary.getGrandTotalRevenue());

        // 2. Verify Best Selling Product (by quantity sold: p1 has 10 units)
        assertEquals("P101", summary.getBestSellingProduct().getProductId());
        assertEquals(10, summary.getBestSellingProduct().getQuantitySold());

        // 3. Verify Highest Revenue Product (p3 has 500.00)
        assertEquals("P103", summary.getHighestRevenueProduct().getProductId());
        assertEquals(new BigDecimal("500.00"), summary.getHighestRevenueProduct().getRevenue());

        // 4. Verify Category Revenue: Electronics = 500.00, Furniture = 500.00
        Map<String, BigDecimal> categoryRevenue = summary.getRevenuePerCategory();
        assertEquals(new BigDecimal("500.00"), categoryRevenue.get("Electronics"));
        assertEquals(new BigDecimal("500.00"), categoryRevenue.get("Furniture"));

        // 5. Verify Revenue Per Product
        Map<Product, BigDecimal> productRevenue = summary.getRevenuePerProduct();
        assertEquals(new BigDecimal("200.00"), productRevenue.get(p1));
        assertEquals(new BigDecimal("300.00"), productRevenue.get(p2));
        assertEquals(new BigDecimal("500.00"), productRevenue.get(p3));
    }
}