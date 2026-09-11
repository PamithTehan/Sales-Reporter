package sales.reporter;

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

    public String getProduct_id() {
        return product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getCategory() {
        return category;
    }

    public int getQuantity_sold() {
        return quantity_sold;
    }

    public BigDecimal getUnit_price() {
        return unit_price;
    }

    public BigDecimal getRevenue(){
        return unit_price.multiply(BigDecimal.valueOf(quantity_sold));
    }
}
