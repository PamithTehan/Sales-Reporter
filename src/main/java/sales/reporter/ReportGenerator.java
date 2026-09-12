package sales.reporter;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReportGenerator {

    public static void main() {
        List<Product> products = new ArrayList<>();

        products.add(new Product("P101", "Wireless Mouse", "Electronics", 150, new BigDecimal("25.99")));
        products.add(new Product("P102", "Mechanical Keyboard", "Electronics", 85, new BigDecimal("79.50")));
        products.add(new Product("P103", "Ergonomic Desk Chair", "Furniture", 40, new BigDecimal("199.99")));

        SalesReporter reporter = new SalesReporter();
        SalesSummary summary = reporter.calculate(products);
        ReportGenerator generator = new ReportGenerator();

        String report = generator.format(summary);

        System.out.println(report);

    }


    public String format(SalesSummary summary) {

        String divider = "=====================================================================";
        StringBuilder sb = new StringBuilder();

        sb.append(divider).append(System.lineSeparator());
        sb.append(" PRODUCT SALES SUMMARY REPORT").append(System.lineSeparator());
        sb.append(divider).append(System.lineSeparator());

        sb.append("--- Revenue Per Product ---").append(System.lineSeparator());
        for (Map.Entry<Product, BigDecimal> entry : summary.getRevenuePerProduct().entrySet()) {
            Product product = entry.getKey();
            sb.append(String.format("%s %s %s $%.2f%n",
                    product.getProductId(), product.getProductName(), product.getCategory(), entry.getValue()));
        }

        sb.append("--- Revenue Per Category ---").append(System.lineSeparator());
        for (Map.Entry<String, BigDecimal> entry : summary.getRevenuePerCategory().entrySet()) {
            sb.append(String.format("%s : $%s%n", entry.getKey(), entry.getValue()));
        }

        sb.append("--- Highlights ---").append(System.lineSeparator());
        Product bestSeller = summary.getBestSellingProduct();
        Product highestRevenue = summary.getHighestRevenueProduct();
        sb.append(String.format("Best-Selling Product : %s (%d units)%n",
                bestSeller.getProductName(), bestSeller.getQuantitySold()));
        sb.append(String.format("Highest Revenue : %s ($%s)%n",
                highestRevenue.getProductName(), highestRevenue.getRevenue()));
        sb.append(String.format("Grand Total Revenue : $%s%n", summary.getGrandTotalRevenue()));

        sb.append(divider);

        return sb.toString();
    }
}
