package sales.reporter.report.formatter;

import sales.reporter.report.SalesSummary;

public interface ReportFormatter {
    String format(SalesSummary summary);
}
