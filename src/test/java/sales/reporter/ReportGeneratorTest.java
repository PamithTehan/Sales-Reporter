package sales.reporter;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sales.reporter.input.CsvParseException;
import sales.reporter.input.Reader;
import sales.reporter.model.Product;
import sales.reporter.output.InvalidOutputMethodException;
import sales.reporter.output.OutputHandler;
import sales.reporter.output.OutputWriter;
import sales.reporter.output.OutputWriterFactory;
import sales.reporter.report.SalesCalculatorService;
import sales.reporter.report.SalesSummary;
import sales.reporter.report.formatter.ReportFormatter;
import sales.reporter.report.formatter.ReportFormatterFactory;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReportGeneratorTest {

    // --- Lightweight Stubs for Testing Pipeline Orchestration ---

    private static class StubReader extends Reader {
        private Exception exceptionToThrow;
        private List<Product> productsToReturn = Collections.emptyList();

        @Override
        public List<Product> readProducts(String path) throws IOException, CsvParseException {
            if (exceptionToThrow instanceof IOException ioException) {
                throw ioException;
            }
            if (exceptionToThrow instanceof CsvParseException csvException) {
                throw csvException;
            }
            if (exceptionToThrow instanceof RuntimeException runtimeException) {
                throw runtimeException;
            }
            return productsToReturn;
        }
    }

    private static class StubSalesCalculator implements SalesCalculatorService {
        private RuntimeException exceptionToThrow;
        private SalesSummary summaryToReturn;

        @Override
        public SalesSummary calculate(List<Product> products) {
            if (exceptionToThrow != null) {
                throw exceptionToThrow;
            }
            return summaryToReturn;
        }
    }

    private static class StubReportFormatterFactory extends ReportFormatterFactory {
        @Override
        public ReportFormatter create(String outputMethod) {
            return summary -> "DUMMY_FORMATTED_REPORT";
        }
    }

    private static class StubOutputWriterFactory extends OutputWriterFactory {
        private boolean shouldThrowInvalidMethod = false;

        @Override
        public OutputWriter create(String outputMethod, String destination) throws InvalidOutputMethodException {
            if (shouldThrowInvalidMethod) {
                throw new InvalidOutputMethodException("Invalid output method");
            }
            return content -> { /* no-op */ };
        }
    }

    private static class StubOutputHandler extends OutputHandler {
        private IOException ioExceptionToThrow;

        @Override
        public void output(OutputWriter writer, String content) throws IOException {
            if (ioExceptionToThrow != null) {
                throw ioExceptionToThrow;
            }
        }
    }

    // --- Test Fixtures & Stream Captures ---

    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    private ByteArrayOutputStream outContent;
    private ByteArrayOutputStream errContent;

    private StubReader stubReader;
    private StubSalesCalculator stubCalculator;
    private StubReportFormatterFactory stubFormatterFactory;
    private StubOutputWriterFactory stubWriterFactory;
    private StubOutputHandler stubOutputHandler;
    private ReportGenerator generator;

    @BeforeEach
    void setUp() {
        // Redirect standard streams to suppress noisy error logs during testing
        outContent = new ByteArrayOutputStream();
        errContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));

        stubReader = new StubReader();
        stubCalculator = new StubSalesCalculator();
        stubFormatterFactory = new StubReportFormatterFactory();
        stubWriterFactory = new StubOutputWriterFactory();
        stubOutputHandler = new StubOutputHandler();

        generator = new ReportGenerator(
                stubReader,
                stubCalculator,
                stubFormatterFactory,
                stubWriterFactory,
                stubOutputHandler
        );
    }

    @AfterEach
    void tearDown() {
        // Restore standard output streams after each test finishes
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    // --- Test Cases ---

    @Test
    void run_MissingArguments_ReturnsExitCode1() {
        assertEquals(1, generator.run(new String[]{}));
        assertEquals(1, generator.run(new String[]{"file.csv"}));
        assertEquals(1, generator.run(null));
    }

    @Test
    void run_ValidConsoleOutput_ReturnsExitCode0() {
        Product sampleProduct = new Product("P1", "Keyboard", "Tech", 1, new BigDecimal("50.00"));
        stubReader.productsToReturn = List.of(sampleProduct);
        stubCalculator.summaryToReturn = new SalesSummary(
                List.of(sampleProduct), Collections.emptyMap(), Collections.emptyMap(), sampleProduct, sampleProduct, new BigDecimal("50.00")
        );

        String[] args = {"data.csv", "console"};

        int exitCode = generator.run(args);
        assertEquals(0, exitCode);
    }

    @Test
    void run_ValidFileOutput_ReturnsExitCode0() {
        stubReader.productsToReturn = List.of(new Product("P1", "Item", "Cat", 1, BigDecimal.TEN));
        stubCalculator.summaryToReturn = new SalesSummary(
                Collections.emptyList(), Collections.emptyMap(), Collections.emptyMap(), null, null, BigDecimal.TEN
        );

        String[] args = {"data.csv", "file", "output.txt"};

        int exitCode = generator.run(args);
        assertEquals(0, exitCode);
    }

    @Test
    void run_FileNotFound_ReturnsExitCode2() {
        stubReader.exceptionToThrow = new FileNotFoundException("File not found");
        String[] args = {"missing.csv", "console"};

        int exitCode = generator.run(args);
        assertEquals(2, exitCode);
    }

    @Test
    void run_CsvParseException_ReturnsExitCode3() {
        stubReader.exceptionToThrow = new CsvParseException("Corrupted header format");
        String[] args = {"invalid.csv", "console"};

        int exitCode = generator.run(args);
        assertEquals(3, exitCode);
    }

    @Test
    void run_InvalidOutputMethod_ReturnsExitCode4() {
        stubWriterFactory.shouldThrowInvalidMethod = true;
        String[] args = {"data.csv", "unsupported_method"};

        int exitCode = generator.run(args);
        assertEquals(4, exitCode);
    }

    @Test
    void run_OutputHandlerIOException_ReturnsExitCode5() {
        stubOutputHandler.ioExceptionToThrow = new IOException("Disk write error");
        String[] args = {"data.csv", "file", "report.txt"};

        int exitCode = generator.run(args);
        assertEquals(5, exitCode);
    }

    @Test
    void run_CalculatorThrowsIllegalArgumentException_ReturnsExitCode6() {
        stubCalculator.exceptionToThrow = new IllegalArgumentException("Cannot calculate an empty product list");
        String[] args = {"empty.csv", "console"};

        int exitCode = generator.run(args);
        assertEquals(6, exitCode);
    }
}