package sales.reporter;

import java.io.Writer;

public class ConsoleWriter implements OutputWriter {

    @Override
    public void write(String reportContent) {
        System.out.println(reportContent);
    }
}