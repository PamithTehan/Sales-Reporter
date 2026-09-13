package sales.reporter.report.formatter;

import sales.reporter.output.InvalidOutputMethodException;

import java.util.HashMap;
import java.util.Map;

public class ReportFormatterFactory {

    private final Map<String, ReportFormatter> formatters = new HashMap<>();

    public ReportFormatterFactory() {
        formatters.put("console", new ConsoleReportFormatter());
        formatters.put("file", new PlainTextReportFormatter());
    }

    public ReportFormatter create(String outputMethod) throws InvalidOutputMethodException {
        if (outputMethod == null) {
            throw new InvalidOutputMethodException("Output method cannot be null.");
        }

        ReportFormatter formatter = formatters.get(outputMethod.trim().toLowerCase());
        if (formatter == null) {
            throw new InvalidOutputMethodException("Unsupported output method: " + outputMethod);
        }

        return formatter;
    }
}