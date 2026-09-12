package sales.reporter.report;

import sales.reporter.model.Product;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class SalesSummary {
    private final List<Product> products;
    private final Map<Product, BigDecimal> revenuePerProduct;
    private final Map<String, BigDecimal> revenuePerCategory;
    private final Product bestSellingProduct;
    private final Product highestRevenueProduct;
    private final BigDecimal grandTotalRevenue;

    public SalesSummary(List<Product> products,
                        Map<Product, BigDecimal> revenuePerProduct,
                        Map<String, BigDecimal> revenuePerCategory,
                        Product bestSellingProduct,
                        Product highestRevenueProduct,
                        BigDecimal grandTotalRevenue) {
        this.products = products;
        this.revenuePerProduct = revenuePerProduct;
        this.revenuePerCategory = revenuePerCategory;
        this.bestSellingProduct = bestSellingProduct;
        this.highestRevenueProduct = highestRevenueProduct;
        this.grandTotalRevenue = grandTotalRevenue;
    }

    public List<Product> getProducts() {
        return products;
    }

    public Map<Product, BigDecimal> getRevenuePerProduct() {
        return revenuePerProduct;
    }

    public Map<String, BigDecimal> getRevenuePerCategory() {
        return new LinkedHashMap<>(revenuePerCategory);
    }

    public Product getBestSellingProduct() {
        return bestSellingProduct;
    }

    public Product getHighestRevenueProduct() {
        return highestRevenueProduct;
    }

    public BigDecimal getGrandTotalRevenue() {
        return grandTotalRevenue;
    }
}
