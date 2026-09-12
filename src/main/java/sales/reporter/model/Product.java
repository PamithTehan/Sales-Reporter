package sales.reporter.model;

import java.math.BigDecimal;

public class Product {

    //product_id, product_name, category, quantity_sold, unit_price
    private final String product_id;
    private final String product_name;
    private final String category;
    private final int quantity_sold;
    private final BigDecimal unit_price;

    public Product(String productId, String productName, String category, int quantitySold, BigDecimal unitPrice) {
        product_id = productId;
        product_name = productName;
        this.category = category;
        quantity_sold = quantitySold;
        unit_price = unitPrice;
    }

    public String getProductId() {
        return product_id;
    }

    public String getProductName() {
        return product_name;
    }

    public String getCategory() {
        return category;
    }

    public int getQuantitySold() {
        return quantity_sold;
    }

    public BigDecimal getUnitPrice() {
        return unit_price;
    }

    public BigDecimal getRevenue(){
        return unit_price.multiply(BigDecimal.valueOf(quantity_sold));
    }
}
