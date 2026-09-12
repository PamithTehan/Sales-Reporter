package sales.reporter.report.formatter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sales.reporter.output.InvalidOutputMethodException;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ReportFormatterFactoryTest {

    private ReportFormatterFactory factory;

    @BeforeEach
    void setUp() {
        factory = new ReportFormatterFactory();
    }

    @Test
    void create_NullInput_ThrowsInvalidOutputMethodException() {
        assertThrows(InvalidOutputMethodException.class, () -> factory.create(null));
    }

    @Test
    void create_UnknownMethod_ThrowsInvalidOutputMethodException() {
        assertThrows(InvalidOutputMethodException.class, () -> factory.create("unknown_output_format"));
    }

    @Test
    void create_ConsoleInput_ReturnsConsoleReportFormatter() throws InvalidOutputMethodException {
        ReportFormatter formatter = factory.create("console");
        assertNotNull(formatter);
        assertInstanceOf(ConsoleReportFormatter.class, formatter);
    }

    @Test
    void create_FileInput_ReturnsPlainTextReportFormatter() throws InvalidOutputMethodException {
        ReportFormatter formatter = factory.create("file");
        assertNotNull(formatter);
        assertInstanceOf(PlainTextReportFormatter.class, formatter);
    }

    @Test
    void create_CaseInsensitiveInput_ReturnsCorrectFormatter() throws InvalidOutputMethodException {
        ReportFormatter console = factory.create("  CONSOLE  ");
        ReportFormatter file = factory.create("FILE");

        assertInstanceOf(ConsoleReportFormatter.class, console);
        assertInstanceOf(PlainTextReportFormatter.class, file);
    }
}