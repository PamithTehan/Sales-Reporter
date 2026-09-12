package sales.reporter;

import sales.reporter.input.Reader;
import sales.reporter.model.Product;
import sales.reporter.output.InvalidOutputMethodException;
import sales.reporter.output.OutputHandler;
import sales.reporter.output.OutputWriter;
import sales.reporter.output.OutputWriterFactory;
import sales.reporter.report.SalesCalculator;
import sales.reporter.report.SalesSummary;
import sales.reporter.report.formatter.ConsoleReportFormatter;
import sales.reporter.report.formatter.PlainTextReportFormatter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class ReportGenerator {

//    public static void main() {
//        List<Product> products = new ArrayList<>();
//
//        products.add(new Product("P101", "Wireless Mouse", "Electronics", 150, new BigDecimal("25.99")));
//        products.add(new Product("P102", "Mechanical Keyboard", "Electronics", 85, new BigDecimal("79.50")));
//        products.add(new Product("P103", "Ergonomic Desk Chair", "Furniture", 40, new BigDecimal("199.99")));
//
//        SalesCalculator reporter = new SalesCalculator();
//        SalesSummary summary = reporter.calculate(products);
//        ReportFormatter formatter = new ConsoleReportFormatter();
//
//        String report = formatter.format(summary);
//
//        System.out.println(report);
//
//    }

    public static void main(String[] args) {
        int exitCode = new ReportGenerator().run(args);
        if (exitCode != 0) {
            System.exit(exitCode);
        }
    }

    public int run(String[] args) {
        if (args.length < 2) {
            System.err.println("Error: missing arguments.");
            System.err.println("Usage: java -jar SalesReporter.jar <csv-file-path> <output-method> [output-file-path]");
            return 1;
        }

        String csvFilePath = args[0];
        String outputMethod = args[1];
        String outputFilePath = args.length >= 3 ? args[2] : null;

        try {
            List<Product> products = new Reader().readProducts(csvFilePath);
            SalesSummary summary = new SalesCalculator().calculate(products);
            String reportText;

            if (args.length == 2) {
                reportText = new ConsoleReportFormatter().format(summary);
            } else {
                reportText = new PlainTextReportFormatter().format(summary);
            }

            OutputWriter outputWriter = new OutputWriterFactory().create(outputMethod, outputFilePath);
            new OutputHandler().output(outputWriter, reportText);

            if ("file".equalsIgnoreCase(outputMethod)) {
                System.out.println("Report successfully written to: " + outputFilePath);
            }
            return 0;

        } catch (FileNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
            return 2;
        } catch (InvalidOutputMethodException e) {
            System.err.println("Error: " + e.getMessage());
            return 4;
        } catch (IOException e) {
            System.err.println("Error: an I/O problem occurred - " + e.getMessage());
            return 5;
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            return 6;
        }
    }
}