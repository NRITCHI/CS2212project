import java.io.*;
import java.util.*;

/**
 * Dictionary is used to check if words are spelled correctly, allows for users to add words to dictionary
 * @author Ryan Lambe
 */
public class Dictionary {

    /**
     * list of words in user dictionary
     */
    public static ArrayList<String> userDictionary = new ArrayList<>();

    /**
     * adds word to user dictionary
     * @param word to add to user dictionary
     */
    public static void AddWord(String word){

        if(Trie.FindWord(word))
            return;

        userDictionary.add(word);
        Trie.AddWord(word);

        try{
            BufferedWriter out = new BufferedWriter(new FileWriter("dictionary/userDict.txt", true));
            out.write(word + "\n");
            out.close();
        }
        catch (IOException e){
            System.out.println("error writing to user dictionary");
        }
    }

    /**
     * removes word from user dictionary
     * @param word to remove from dictionary
     */
    public static void RemoveWord(String word){
        if(!userDictionary.contains(word))
            return;
        if(!Trie.FindWord(word))
            return;

        Trie.RemoveWord(word);
        userDictionary.remove(word);

        try{
            BufferedWriter out = new BufferedWriter(new FileWriter("dictionary/userDict.txt"));
            for(String line: userDictionary){
                out.write(line + "\n");
            }
            out.close();
        }
        catch (IOException e){
            System.out.println("error removing from user dictionary");
        }
    }

    /**
     * Checks if input word is a word in either user or global dictionary
     * @param word to check if is word
     * @return if input word is a word
     */
    public static boolean FindWord(String word){
        return Trie.FindWord(word);
    }

    /**
     * Clears all values from user dictionary
     */
    public static void ResetDictionary(){
        for(String line: userDictionary){
            Trie.RemoveWord(line);
        }

        userDictionary.clear();

        try{
            BufferedWriter out = new BufferedWriter(new FileWriter("dictionary/userDict.txt"));
            out.close();
        }
        catch (IOException e){
            System.out.println("error resetting user dictionary");
        }
    }

    /**
     * Loads global and user dictionary, should be called once at start of program
     * @throws FileNotFoundException when the global dictionary file doesn't exist "dictionary/dict.txt"
     */
    public static void StartDictionary() throws FileNotFoundException {

        // global dictionary
        InputStream is = new FileInputStream("dictionary/dict.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        List<String> lines = br.lines().toList();
        for(String line: lines){
            Trie.AddWord(line);
        }

        try{
            br.close();
            is.close();
        }
        catch (IOException e){
            // this will never happen
        }

        // user dictionary
        try{
            is = new FileInputStream("dictionary/userDict.txt");
            br = new BufferedReader(new InputStreamReader(is));

            lines = br.lines().toList();
            for(String line: lines){
                Trie.AddWord(line);
                userDictionary.add(line);
            }
        }
        catch (Exception e){
            // file doesn't exist, will be created when needed
        }
    }

    /**
     * gets the user dictionary as an array of strings
     * @return all words in the users dictionary
     */
    public static String[] GetUserDictionary(){
        return userDictionary.toArray(new String[0]);
    }

    /**
     * clear the user dictionary and fill it with input words
     * @param words all the words that will be contained in the user dictionary
     */
    public static void SetUserDictionary(String[] words){

        for(String line: userDictionary){
            Trie.RemoveWord(line);
        }

        userDictionary = new ArrayList<>(Arrays.asList(words));

        try{
            BufferedWriter out = new BufferedWriter(new FileWriter("dictionary/userDict.txt"));
            for(String word: userDictionary){
                out.write(word + "\n");
                Trie.AddWord(word);
            }
            out.close();
        }
        catch (Exception e) {
            System.out.println("error writing user dictionary");
        }
    }

    /**
     * private assert used for testing class functionality
     * @param val value of the assertion that is expected to be true
     * @throws RuntimeException thrown when value is false
     */
    private static void Assert(boolean val) throws RuntimeException{
        if(!val){
            throw new RuntimeException();
        }
    }

    /**
     * Testing purposes only
     * @throws FileNotFoundException thrown when dictionary/dict.txt doesn't exist
     * @throws RuntimeException thrown when assert is wrong
     */
    public static void main(String[] args) throws FileNotFoundException, RuntimeException {
        StartDictionary();

        Assert(FindWord("thisIsFirst"));
        Assert(FindWord("thisIsNotFirst"));
        Assert(FindWord("thisIsSecond"));

        AddWord("helloWorldItsMe");
        AddWord("deleteMePlease");

        Assert(FindWord("helloWorldItsMe"));
        Assert(FindWord("deleteMePlease"));

        RemoveWord("deleteMePlease");

        Assert(FindWord("helloWorldItsMe"));
        Assert(!FindWord("deleteMePlease"));

        ResetDictionary();

        Assert(!FindWord("thisIsFirst"));
        Assert(!FindWord("thisIsNotFirst"));
        Assert(!FindWord("thisIsSecond"));
        Assert(!FindWord("helloWorldItsMe"));
        Assert(!FindWord("deleteMePlease"));

        String[] words = {"thisIsFirst", "thisIsNotFirst", "thisIsSecond"};
        SetUserDictionary(words);

        words = GetUserDictionary();
        Assert(words.length == 3);
        Assert(words[0].equals("thisIsFirst"));
        Assert(words[1].equals("thisIsNotFirst"));
        Assert(words[2].equals("thisIsSecond"));
    }
}