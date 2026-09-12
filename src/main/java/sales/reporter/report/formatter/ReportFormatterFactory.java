package sales.reporter.report.formatter;

import java.util.HashMap;
import java.util.Map;

public class ReportFormatterFactory {

    private final Map<String, ReportFormatter> formatters = new HashMap<>();

    public ReportFormatterFactory() {
        formatters.put("console", new ConsoleReportFormatter());
        formatters.put("file", new PlainTextReportFormatter());
    }

    public ReportFormatter create(String outputMethod) {
        if (outputMethod == null) {
            return new ConsoleReportFormatter();
        }

        ReportFormatter formatter = formatters.get(outputMethod.trim().toLowerCase());
        if (formatter == null) {
            return new PlainTextReportFormatter();
        }

        return formatter;
    }
}