

// Correction class with methods as specified in the diagram

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Correction {


    // Method to get corrections for a given text
    public static Pair<Integer, Integer>[] GetCorrections(String text){
       
        //seperate a string into words by space or punctuation as long as it is not entirely numbers
        //word boundry, not all numbers, word boundry, character, word boundry
        Pattern pattern = Pattern.compile("\\b(?![0-9]+\\b)\\w+\\b");
        Matcher matcher = pattern.matcher(text);

        ArrayList<Pair<Integer, Integer>> pairsList = new ArrayList<>();

        while (matcher.find()) {
            // Start index of the word
            int startIndex = matcher.start();
            // Length of the word
            int wordLength = matcher.group().length();

            pairsList.add(new Pair<>(startIndex, wordLength));
        }

        // Convert ArrayList to array
        Pair<Integer, Integer>[] pairsArray = new Pair[pairsList.size()];
        pairsArray = pairsList.toArray(pairsArray);

    
        for (Pair<Integer, Integer> pair : pairsArray) {
            int startIndex = pair.first;
            int wordLength = pair.second;
            String word = text.substring(startIndex, startIndex + wordLength);
    
            String[] spellingSuggestions = CheckSpelling(word);
    
            if (spellingSuggestions.length > 0) {
                
                Window.AddCorrection(CorrectionType.Misspelling, pair, spellingSuggestions);
            }
        }

        // Detect miscapitalization
        CheckCapitalization(text, pairsArray);

        // Detect double words
        CorrectDoubleWords(text, pairsArray);

        return pairsArray;
    }




 

    // Method to check spelling in a given string
    public static String[] CheckSpelling(String word) {
    

        List<String> suggestions = new ArrayList<>();

        // Remove each letter from the word

        for (int i = 0; i < word.length(); i++) {
            String withoutLetter = word.substring(0, i) + word.substring(i + 1);
            if (Dictionary.FindWord(withoutLetter) && !suggestions.contains(withoutLetter)) {
                suggestions.add(withoutLetter);
            }
        }

         // Insert each possible letter into each position
         for (int i = 0; i <= word.length(); i++) {
            for (char c = 'a'; c <= 'z'; c++) {
                String withExtraLetter = word.substring(0, i) + c + word.substring(i);
                if (Dictionary.FindWord(withExtraLetter) && !suggestions.contains(withExtraLetter)) {
                    suggestions.add(withExtraLetter);
                }
            }
        }

         // Swap each pair of consecutive letters
        for (int i = 0; i < word.length() - 1; i++) {
            char[] wordArray = word.toCharArray();
            // Swap letters at i and i+1
            char temp = wordArray[i];
            wordArray[i] = wordArray[i + 1];
            wordArray[i + 1] = temp;

            String swapped = new String(wordArray);
            if (Dictionary.FindWord(swapped) && !suggestions.contains(swapped)) {
                suggestions.add(swapped);
            }
        }

        // Insert a space or hyphen to split the word
        for (int i = 1; i < word.length(); i++) {
            String firstPart = word.substring(0, i);
            String secondPart = word.substring(i);
            if (Dictionary.FindWord(firstPart) && Dictionary.FindWord(secondPart)) {
                String withSpace = firstPart + " " + secondPart;
                String withHyphen = firstPart + "-" + secondPart;
                if (!suggestions.contains(withSpace)) {
                    suggestions.add(withSpace);
                }
                if (!suggestions.contains(withHyphen)) {
                    suggestions.add(withHyphen);
                }
            }
        }

        // Convert suggestions list to an array
        return suggestions.toArray(new String[0]);
    }


    private static void CheckCapitalization(String text, Pair<Integer, Integer>[] pairs) {
        //List<String> capitalizationSuggestions = new ArrayList<>();
        // Regular expression to match a word after a punctuation mark
        Pattern pattern = Pattern.compile("(?<=\\.|!|\\?)\\s+[a-zA-Z]");
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            int startIndex = matcher.start() + 1; // Skipping the space
            int wordLength = matcher.end() - startIndex;
            Pair<Integer, Integer> pair = new Pair<>(startIndex, wordLength);


            String suggestion = text.substring(startIndex, startIndex + 1).toUpperCase() + text.substring(startIndex + 1, startIndex + wordLength);
            //capitalizationSuggestions.add(suggestion);
            Window.AddCorrection(CorrectionType.Miscapitalization, pair, new String[]{suggestion});
        }

        // Regular expression to find words with mixed capitalization
        //uppercase followed by lowercase cannot have uppercase after
        pattern = Pattern.compile("\\b(?=[A-Za-z]*[a-z])(?=[A-Za-z]*[A-Z])[A-Za-z]+\\b");
        matcher = pattern.matcher(text);

        while (matcher.find()) {
            int startIndex = matcher.start();
            int wordLength = matcher.group().length();
            Pair<Integer, Integer> pair = new Pair<>(startIndex, wordLength);

            // AddCorrection for mixed capitalization with empty suggestions
            String suggestion = text.substring(startIndex, startIndex + wordLength).toLowerCase();
            //capitalizationSuggestions.add(suggestion);

            Window.AddCorrection(CorrectionType.Miscapitalization, pair, new String[]{suggestion});
        }

        //for people places and things already capitalized in the dictionary
        //lowercase word
        pattern = Pattern.compile("\\b[a-z]\\w*\\b");
        matcher = pattern.matcher(text);

        while (matcher.find()) {
            String word = matcher.group();
            if (!Dictionary.FindWord(word)) {
                int startIndex = matcher.start();
                int wordLength = word.length();
                Pair<Integer, Integer> pair = new Pair<>(startIndex, wordLength);

                // Suggestion: Capitalize the word
                String suggestion = word.substring(0, 1).toUpperCase() + word.substring(1);
                if(Dictionary.FindWord(suggestion)){
                    Window.AddCorrection(CorrectionType.Miscapitalization, pair, new String[]{suggestion});
                    //capitalizationSuggestions.add(suggestion);
                }
            }

        }
        //AddCorrection(CorrectionType.Miscapitalization, pair, capitalizationSuggestions);
    }

    // New method to detect and correct double words
    private static void CorrectDoubleWords(String text, Pair<Integer, Integer>[] pairs) {
        // Regular expression to match double words
        Pattern pattern = Pattern.compile("\\b(\\w+)\\s+\\1\\b", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            int startIndex = matcher.start();
            int wordLength = matcher.group().length();
            Pair<Integer, Integer> pair = new Pair<>(startIndex, wordLength);

            // AddCorrection for double words with empty suggestions
            String suggestion = matcher.group(1);
            Window.AddCorrection(CorrectionType.DoubleWords, pair, new String[]{suggestion});
        
        }
    }
}
