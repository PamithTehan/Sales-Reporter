package sales.reporter.input;

public class DefaultRowSplitter implements RowSplitter {

    @Override
    public String[] splitRow(String line) {
        return line.split(",");
    }
}
