package sales.reporter.output;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class OutputWriterFactoryTest {

    private final OutputWriterFactory factory = new OutputWriterFactory();

    @Test
    void createsConsoleWriterForConsoleMethod() throws Exception {
        OutputWriter writer = factory.create("console", null);
        assertInstanceOf(ConsoleWriter.class, writer);
    }

    @Test
    void createsFileWriterHandlerWhenPathProvided(@TempDir Path tempDir) throws Exception {
        String path = tempDir.resolve("report.txt").toString();
        OutputWriter writer = factory.create("file", path);
        assertInstanceOf(FileWriterHandler.class, writer);
    }

    @Test
    void throwsWhenOutputMethodIsNull() {
        assertThrows(InvalidOutputMethodException.class,
                () -> factory.create(null, null));
    }

    @Test
    void throwsWhenOutputMethodIsInvalid() {
        assertThrows(InvalidOutputMethodException.class,
                () -> factory.create("email", null));
    }

    @Test
    void throwsWhenFileMethodHasNoPath() {
        assertThrows(InvalidOutputMethodException.class,
                () -> factory.create("file", null));
    }

    @Test
    void throwsWhenFileMethodHasBlankPath() {
        assertThrows(InvalidOutputMethodException.class,
                () -> factory.create("file", "   "));
    }
}