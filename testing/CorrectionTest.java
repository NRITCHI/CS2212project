import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CorrectionTest {

    @Test
    void getCorrections() {
        assert Correction.GetCorrections("text").length == 1;
        assert Correction.GetCorrections("text help").length == 2;

    }

    @Test
    void checkSpelling() {
        String[] correct = Correction.CheckSpelling("texxt");
        for(int i = 0; i < correct.length; i++){
            if (correct[i].equals("text")) assert true;
        }
    }
}