package sales.reporter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SalesReporter {
    //Total revenue per product (quantity_sold × unit_price)
    //Total revenue per category (sum of revenue for all products in that category)
    //Best-selling product (product with the highest quantity_sold)
    //Highest revenue product (product with the highest total revenue)
    //Grand total revenue (sum of all product revenues)

    public Map<Product, BigDecimal> TotalRevenuePerProduct(List<Product>products){
        HashMap<Product, BigDecimal> result = new LinkedHashMap<>();

        for(Product product : products){
            result.put(product,product.getRevenue());
        }

        return result;
    }

    public Map<String, BigDecimal> totalRevenuePerCategory(List<Product> products){
        HashMap<String, BigDecimal> results = new LinkedHashMap<>();

        for(Product product: products){
            results.merge(product.getCategory(),product.getRevenue(), BigDecimal::add);
        }

        return results;
    }

    public Product findBestSellingProduct(List<Product> products){
        Product bestSellingProduct = null;

        for(Product product:products){

            if(bestSellingProduct == null || product.getQuantity_sold()> bestSellingProduct.getQuantity_sold()){
                bestSellingProduct = product;
            }
        }

        return bestSellingProduct;
    }

    public Product findHighestRevenueProduct(List<Product> products){
        Product highestRevenueProduct = null;
        BigDecimal highestRevenue = null;

        for(Product product:products){

            if(highestRevenue == null || highestRevenue.compareTo(product.getRevenue()) > 0){
                highestRevenueProduct = product;
                highestRevenue = product.getRevenue();
            }
        }

        return highestRevenueProduct;
    }

    public BigDecimal calculateGrandTotal(List<Product> products ){
        BigDecimal grandTotal = BigDecimal.ZERO;

        for(Product product: products){
            grandTotal = grandTotal.add(product.getRevenue());
        }

        return grandTotal;
    }

}
