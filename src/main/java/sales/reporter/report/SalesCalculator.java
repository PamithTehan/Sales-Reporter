package sales.reporter.report;

import sales.reporter.model.Product;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SalesCalculator implements SalesCalculatorService {

    @Override
    public SalesSummary calculate(List<Product> products) {
        if (products == null || products.isEmpty()) {
            throw new IllegalArgumentException("Cannot calculate a summary from an empty product list");
        }

        Map<Product, BigDecimal> revenuePerProduct = calculateRevenuePerProduct(products);
        Map<String, BigDecimal> revenuePerCategory = calculateRevenuePerCategory(products);
        Product bestSeller = findBestSellingProduct(products);
        Product highestRevenue = findHighestRevenueProduct(products);
        BigDecimal grandTotal = calculateGrandTotal(products);

        return new SalesSummary(products, revenuePerProduct, revenuePerCategory,
                bestSeller, highestRevenue, grandTotal);
    }

    private Map<Product, BigDecimal> calculateRevenuePerProduct(List<Product>products){
        Map<Product, BigDecimal> result = new LinkedHashMap<>();

        for(Product product : products){
            result.put(product,product.getRevenue());
        }

        return result;
    }

    private Map<String, BigDecimal> calculateRevenuePerCategory(List<Product> products){
        Map<String, BigDecimal> results = new LinkedHashMap<>();

        for(Product product: products){
            results.merge(product.getCategory(),product.getRevenue(), BigDecimal::add);
        }

        return results;
    }

    private Product findBestSellingProduct(List<Product> products){
        Product bestSellingProduct = null;

        for(Product product:products){

            if(bestSellingProduct == null || product.getQuantitySold()> bestSellingProduct.getQuantitySold()){
                bestSellingProduct = product;
            }
        }

        return bestSellingProduct;
    }

    private Product findHighestRevenueProduct(List<Product> products){
        Product highestRevenueProduct = null;
        BigDecimal highestRevenue = null;

        for(Product product:products){

            if(highestRevenue == null || product.getRevenue().compareTo(highestRevenue) > 0){
                highestRevenueProduct = product;
                highestRevenue = product.getRevenue();
            }
        }

        return highestRevenueProduct;
    }

    private BigDecimal calculateGrandTotal(List<Product> products ){
        BigDecimal grandTotal = BigDecimal.ZERO;

        for(Product product: products){
            grandTotal = grandTotal.add(product.getRevenue());
        }

        return grandTotal;
    }
}
