package sales.reporter.input;

public class HeaderDetector {

    private static final int EXPECTED_COLUMNS = 5;

    private final RowSplitter rowSplitter;

    public HeaderDetector(RowSplitter rowSplitter) {
        this.rowSplitter = rowSplitter;
    }

    public boolean looksLikeHeader(String line) {
        String[] tokens = rowSplitter.splitRow(line);
        if (tokens.length < EXPECTED_COLUMNS) {
            return true; // malformed first line - safest to skip and let real rows be validated
        }
        Integer.parseInt(tokens[3].trim());
        return false; // fourth column parses as a number -> this is a real data row

        //catch number format exception
    }
}
