import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class DictionaryTest {

    @Test
    void addWord() throws FileNotFoundException {
        Dictionary.StartDictionary();
        Dictionary.AddWord("helloWorldItsMe");
        assert Dictionary.FindWord("helloWorldItsMe");
        Dictionary.AddWord("deleteMePlease");
        assert Dictionary.FindWord("deleteMePlease");
    }

    @Test
    void removeWord() {
        Dictionary.AddWord("helloWorldItsMe");
        Dictionary.AddWord("deleteMePlease");
        Dictionary.RemoveWord("deleteMePlease");
        assert !Dictionary.FindWord("deleteMePlease");
        assert Dictionary.FindWord("helloWorldItsMe");
    }

    @Test
    void findWord() {
        assert Dictionary.FindWord("text");
    }

    @Test
    void resetDictionary() {
        Dictionary.ResetDictionary();
    }

    @Test
    void startDictionary() throws FileNotFoundException {
        Dictionary.StartDictionary();
    }

    @Test
    void setUserDictionary(){
        String[] words = {"thisIsFirst", "thisIsNotFirst", "thisIsSecond"};
        Dictionary.SetUserDictionary(words);
        assert words.length == 3;
        assert words[0].equals("thisIsFirst");
        assert words[1].equals("thisIsNotFirst");
        assert words[2].equals("thisIsSecond");
    }


}