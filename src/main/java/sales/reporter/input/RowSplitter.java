package sales.reporter.input;

public class RowSplitter {

    public String[] splitRow(String line) {
        return line.split(",");
    }
}
