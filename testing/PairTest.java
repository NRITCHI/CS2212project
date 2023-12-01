import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PairTest {

    @Test
    void testPair() {
        Pair<Integer, String> pair = new Pair<>(1, "Test");
        assertEquals(1, pair.first);
        assertEquals("Test", pair.second);
    }

    @Test
    void testEqualsIdenticalPairs() {
        Pair<Integer, String> pair1 = new Pair<>(1, "Test");
        Pair<Integer, String> pair2 = new Pair<>(1, "Test");
        assertEquals(pair1, pair2);
    }

    @Test
    void testEqualsDifferentValues() {
        Pair<Integer, String> pair1 = new Pair<>(1, "Test");
        Pair<Integer, String> pair2 = new Pair<>(2, "Different");
        assertNotEquals(pair1, pair2);
    }

    @Test
    void testEqualsWithNull() {
        Pair<Integer, String> pair = new Pair<>(1, "Test");
        assertNotEquals(pair, null);
    }

    @Test
    void testEqualsDifferentTypes() {
        Pair<Integer, String> pair1 = new Pair<>(1, "Test");
        Pair<Double, String> pair2 = new Pair<>(1.0, "Test");
        assertNotEquals(pair1, pair2);
    }
}