package sales.reporter.report.formatter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ReportFormatterFactoryTest {

    private ReportFormatterFactory factory;

    @BeforeEach
    void setUp() {
        factory = new ReportFormatterFactory();
    }

    @Test
    void create_ConsoleInput_ReturnsConsoleReportFormatter() {
        ReportFormatter formatter = factory.create("console");
        assertNotNull(formatter);
        assertInstanceOf(ConsoleReportFormatter.class, formatter);
    }

    @Test
    void create_FileInput_ReturnsPlainTextReportFormatter() {
        ReportFormatter formatter = factory.create("file");
        assertNotNull(formatter);
        assertInstanceOf(PlainTextReportFormatter.class, formatter);
    }

    @Test
    void create_CaseInsensitiveInput_ReturnsCorrectFormatter() {
        ReportFormatter console = factory.create("  CONSOLE  ");
        ReportFormatter file = factory.create("FILE");

        assertInstanceOf(ConsoleReportFormatter.class, console);
        assertInstanceOf(PlainTextReportFormatter.class, file);
    }

    @Test
    void create_NullInput_ReturnsDefaultConsoleFormatter() {
        ReportFormatter formatter = factory.create(null);
        assertNotNull(formatter);
        assertInstanceOf(ConsoleReportFormatter.class, formatter);
    }

    @Test
    void create_UnknownMethod_ReturnsDefaultPlainTextFormatter() {
        ReportFormatter formatter = factory.create("unknown_output_format");
        assertNotNull(formatter);
        assertInstanceOf(PlainTextReportFormatter.class, formatter);
    }
}
