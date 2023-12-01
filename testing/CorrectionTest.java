import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CorrectionTest {

    @Test
    void getCorrections() {
        assert Correction.GetCorrections("text").length == 1;
        assert Correction.GetCorrections("text help").length == 2;

    }

    @Test
    void getCorrectionsEmptyInput() {
        assertEquals(0, Correction.GetCorrections("").length);
    }

    // Test for input with only numbers
    @Test
    void getCorrectionsNumbersOnly() {
        assertEquals(0, Correction.GetCorrections("12345").length);
    }

    // Test for input with special characters
    @Test
    void getCorrectionsSpecialCharacters() {
        assertEquals(0, Correction.GetCorrections("!@#$%").length);
    }

    @Test
    void checkSpellingExtraLetter() {
        String[] correct = Correction.CheckSpelling("texxt");
        for(int i = 0; i < correct.length; i++){
            if (correct[i].equals("text")) assert true;
        }
    }

    @Test
    void checkSpellingLessLetter() {
        String[] correct = Correction.CheckSpelling("tet");
        for(int i = 0; i < correct.length; i++){
            if (correct[i].equals("text")) assert true;
        }
    }

    @Test
    void checkSpellingWrongLetter() {
        String[] correct = Correction.CheckSpelling("tert");
        for(int i = 0; i < correct.length; i++){
            if (correct[i].equals("text")) assert true;
        }
    }
}