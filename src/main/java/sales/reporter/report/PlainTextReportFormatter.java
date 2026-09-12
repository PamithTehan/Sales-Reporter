package sales.reporter.report;

import sales.reporter.model.Product;

import java.math.BigDecimal;
import java.util.Map;

public class PlainTextReportFormatter implements ReportFormatter {

    private static final String DIVIDER = "=====================================================================";

    @Override
    public String format(SalesSummary summary) {
        if (summary == null) {
            throw new IllegalArgumentException("SalesSummary cannot be null");
        }

        StringBuilder sb = new StringBuilder();

        appendHeader(sb);
        appendProductRevenue(sb, summary.getRevenuePerProduct());
        appendCategoryRevenue(sb, summary.getRevenuePerCategory());
        appendHighlights(sb, summary);
        appendFooter(sb);

        return sb.toString();
    }

    private void appendHeader(StringBuilder sb) {
        sb.append(DIVIDER).append(System.lineSeparator())
                .append(" PRODUCT SALES SUMMARY REPORT").append(System.lineSeparator())
                .append(DIVIDER).append(System.lineSeparator());
    }

    private void appendProductRevenue(StringBuilder sb, Map<Product, BigDecimal> productRevenue) {
        sb.append("--- Revenue Per Product ---").append(System.lineSeparator());
        if (productRevenue != null) {
            for (Map.Entry<Product, BigDecimal> entry : productRevenue.entrySet()) {
                Product product = entry.getKey();
                sb.append(String.format("%s %s %s $%.2f%n",
                        product.getProductId(),
                        product.getProductName(),
                        product.getCategory(),
                        entry.getValue()));
            }
        }
    }

    private void appendCategoryRevenue(StringBuilder sb, Map<String, BigDecimal> categoryRevenue) {
        sb.append("--- Revenue Per Category ---").append(System.lineSeparator());
        if (categoryRevenue != null) {
            for (Map.Entry<String, BigDecimal> entry : categoryRevenue.entrySet()) {
                sb.append(String.format("%s : $%s%n", entry.getKey(), entry.getValue()));
            }
        }
    }

    private void appendHighlights(StringBuilder sb, SalesSummary summary) {
        sb.append("--- Highlights ---").append(System.lineSeparator());

        Product bestSeller = summary.getBestSellingProduct();
        if (bestSeller != null) {
            sb.append(String.format("Best-Selling Product : %s (%d units)%n",
                    bestSeller.getProductName(), bestSeller.getQuantitySold()));
        }

        Product highestRevenue = summary.getHighestRevenueProduct();
        if (highestRevenue != null) {
            sb.append(String.format("Highest Revenue : %s ($%s)%n",
                    highestRevenue.getProductName(), highestRevenue.getRevenue()));
        }

        sb.append(String.format("Grand Total Revenue : $%s%n", summary.getGrandTotalRevenue()));
    }

    private void appendFooter(StringBuilder sb) {
        sb.append(DIVIDER);
    }
}