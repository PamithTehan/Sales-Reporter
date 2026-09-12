package sales.reporter;

import sales.reporter.input.CsvParseException;
import sales.reporter.input.Reader;
import sales.reporter.model.Product;
import sales.reporter.output.InvalidOutputMethodException;
import sales.reporter.output.OutputHandler;
import sales.reporter.output.OutputWriter;
import sales.reporter.output.OutputWriterFactory;
import sales.reporter.report.SalesCalculator;
import sales.reporter.report.SalesCalculatorService;
import sales.reporter.report.SalesSummary;
import sales.reporter.report.formatter.ReportFormatter;
import sales.reporter.report.formatter.ReportFormatterFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class ReportGenerator {

    private final Reader reader;
    private final SalesCalculatorService calculatorService;
    private final ReportFormatterFactory formatterFactory;
    private final OutputWriterFactory writerFactory;
    private final OutputHandler outputHandler;


    public ReportGenerator(Reader reader,
                           SalesCalculatorService calculatorService,
                           ReportFormatterFactory formatterFactory,
                           OutputWriterFactory writerFactory,
                           OutputHandler outputHandler) {
        this.reader = reader;
        this.calculatorService = calculatorService;
        this.formatterFactory = formatterFactory;
        this.writerFactory = writerFactory;
        this.outputHandler = outputHandler;
    }


    public static void main(String[] args) {

        Reader reader = new Reader();
        SalesCalculatorService calculator = new SalesCalculator();
        ReportFormatterFactory formatterFactory = new ReportFormatterFactory();
        OutputWriterFactory writerFactory = new OutputWriterFactory();
        OutputHandler outputHandler = new OutputHandler();

        ReportGenerator app = new ReportGenerator(
                reader,
                calculator,
                formatterFactory,
                writerFactory,
                outputHandler
        );

        int exitCode = app.run(args);
        if (exitCode != 0) {
            System.exit(exitCode);
        }
    }

    public int run(String[] args) {
        if (args == null || args.length < 2) {
            System.err.println("Error: missing arguments.");
            System.err.println("Usage: java -jar SalesReporter.jar <csv-file-path> <output-method> [output-file-path]");
            return 1;
        }

        String csvFilePath = args[0];
        String outputMethod = args[1];
        String outputFilePath = args.length >= 3 ? args[2] : null;

        if ("file".equalsIgnoreCase(outputMethod) && (outputFilePath == null || outputFilePath.trim().isEmpty())) {
            System.err.println("Error: output file path is required when output method is 'file'.");
            return 1;
        }

        try {
            List<Product> products = reader.readProducts(csvFilePath);
            SalesSummary summary = calculatorService.calculate(products);

            ReportFormatter formatter = formatterFactory.create(outputMethod);
            String reportText = formatter.format(summary);

            OutputWriter outputWriter = writerFactory.create(outputMethod, outputFilePath);
            outputHandler.output(outputWriter, reportText);

            if ("file".equalsIgnoreCase(outputMethod)) {
                System.out.println("Report successfully written to: " + outputFilePath);
            }
            return 0;

        } catch (FileNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
            return 2;
        } catch (CsvParseException e) {
            System.err.println("Error: could not parse the CSV file - " + e.getMessage());
            return 3;
        }catch (InvalidOutputMethodException e) {
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