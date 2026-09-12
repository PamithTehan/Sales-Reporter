package sales.reporter.output;

import java.io.IOException;

public class OutputHandler {

    public void output(OutputWriter writer, String reportContent) throws IOException {
        writer.write(reportContent);
    }
}