package sales.reporter.report;

import sales.reporter.model.Product;
import java.util.List;

public interface SalesCalculatorService {
    SalesSummary calculate(List<Product> products);
}
