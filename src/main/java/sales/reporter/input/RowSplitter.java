package sales.reporter.input;

public interface RowSplitter {
    String[] splitRow(String line);
}
