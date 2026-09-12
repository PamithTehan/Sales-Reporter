package sales.reporter.input;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DefaultHeaderDetectorTest {

    private final HeaderDetector headerDetector = new DefaultHeaderDetector(new DefaultRowSplitter());

    @Test
    void treatsNonNumericFourthColumnAsHeader() {
        assertTrue(headerDetector.looksLikeHeader("ProductId,Name,Category,Qty,Price"));
    }

    @Test
    void treatsNumericFourthColumnAsDataRow() {
        assertFalse(headerDetector.looksLikeHeader("P1,Widget,Tools,10,9.99"));
    }

    @Test
    void treatsNonNumericFourthColumnAsHeader_notAsCrash() {
        // Regression test for the "catch number format exception" fix:
        // a non-numeric 4th column must return true, not throw.
        assertTrue(headerDetector.looksLikeHeader("P1,Widget,Tools,,9.99"));
    }
}
