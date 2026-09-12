package sales.reporter.input;

public class DefaultHeaderDetector implements HeaderDetector {

    private static final int EXPECTED_COLUMNS = 5;

    private final RowSplitter rowSplitter;

    public DefaultHeaderDetector(RowSplitter rowSplitter) {
        this.rowSplitter = rowSplitter;
    }

    @Override
    public boolean looksLikeHeader(String line) {
        String[] tokens = rowSplitter.splitRow(line);
        if (tokens.length < EXPECTED_COLUMNS) {
            return true; // malformed first line - safest to skip and let real rows be validated
        }
        try {
            Integer.parseInt(tokens[3].trim());
            return false; // fourth column parses as a number -> this is a real data row
        } catch (NumberFormatException e) {
            return true; // fourth column isn't numeric -> this looks like a header label
        }
    }
}
