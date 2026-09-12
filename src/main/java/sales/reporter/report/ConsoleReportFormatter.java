package sales.reporter.report;

import sales.reporter.model.Product;

import java.math.BigDecimal;
import java.util.Map;

public class ConsoleReportFormatter implements ReportFormatter {


    private static final String DIVIDER = "=====================================================================";
    private static final String SECTION_PREFIX = "--- ";
    private static final String SECTION_SUFFIX = " ---";

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
                .append("                  PRODUCT SALES SUMMARY REPORT").append(System.lineSeparator())
                .append(DIVIDER).append(System.lineSeparator());
    }

    private void appendProductRevenue(StringBuilder sb, Map<Product, BigDecimal> productRevenue) {
        sb.append(SECTION_PREFIX).append("Revenue Per Product").append(SECTION_SUFFIX).append(System.lineSeparator());

        if (productRevenue == null || productRevenue.isEmpty()) {
            sb.append("No product revenue data available.").append(System.lineSeparator());
            return;
        }

        for (Map.Entry<Product, BigDecimal> entry : productRevenue.entrySet()) {
            Product product = entry.getKey();
            sb.append(String.format("%-10s %-20s %-15s $%.2f%n",
                    product.getProductId(),
                    product.getProductName(),
                    product.getCategory(),
                    entry.getValue()));
        }
    }

    private void appendCategoryRevenue(StringBuilder sb, Map<String, BigDecimal> categoryRevenue) {
        sb.append(SECTION_PREFIX).append("Revenue Per Category").append(SECTION_SUFFIX).append(System.lineSeparator());

        if (categoryRevenue == null || categoryRevenue.isEmpty()) {
            sb.append("No category revenue data available.").append(System.lineSeparator());
            return;
        }

        for (Map.Entry<String, BigDecimal> entry : categoryRevenue.entrySet()) {
            sb.append(String.format("%-20s : $%.2f%n", entry.getKey(), entry.getValue()));
        }
    }

    private void appendHighlights(StringBuilder sb, SalesSummary summary) {
        sb.append(SECTION_PREFIX).append("Highlights").append(SECTION_SUFFIX).append(System.lineSeparator());

        Product bestSeller = summary.getBestSellingProduct();
        if (bestSeller != null) {
            sb.append(String.format("Best-Selling Product : %s (%d units)%n",
                    bestSeller.getProductName(), bestSeller.getQuantitySold()));
        } else {
            sb.append("Best-Selling Product : N/A").append(System.lineSeparator());
        }

        Product highestRevenue = summary.getHighestRevenueProduct();
        if (highestRevenue != null) {
            sb.append(String.format("Highest Revenue      : %s ($%.2f)%n",
                    highestRevenue.getProductName(), highestRevenue.getRevenue()));
        } else {
            sb.append("Highest Revenue      : N/A").append(System.lineSeparator());
        }

        BigDecimal grandTotal = summary.getGrandTotalRevenue() != null
                ? summary.getGrandTotalRevenue()
                : BigDecimal.ZERO;

        sb.append(String.format("Grand Total Revenue  : $%.2f%n", grandTotal));
    }

    private void appendFooter(StringBuilder sb) {
        sb.append(DIVIDER);
    }
}