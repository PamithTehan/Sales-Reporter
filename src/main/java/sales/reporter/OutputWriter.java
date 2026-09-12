package sales.reporter;

import java.io.IOException;

public interface OutputWriter {
    void write(String reportContent) throws IOException;
}