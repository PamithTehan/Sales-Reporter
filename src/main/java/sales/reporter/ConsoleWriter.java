package sales.reporter;


public class ConsoleWriter implements OutputWriter {

    @Override
    public void write(String reportContent) {
        System.out.println(reportContent);
    }
}