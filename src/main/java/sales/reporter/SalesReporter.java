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
    
}
