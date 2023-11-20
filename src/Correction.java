

// Correction class with methods as specified in the diagram

import java.util.ArrayList;
import java.util.List;

public class Correction {
    
    // Method to get corrections for a given text
    public Pair<Integer, Integer[]> GetCorrections(String text) {
        // Return a pair containing the index of the error and an array of integer?
        return null;
    }

    // Method to check spelling in a given string
    public String[] CheckSpelling(String word) {
        //return new String[] { "hello", "world" };

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

    // Method to check capitalization in a given string
    public String CheckCapitalization(String text) {
      
        return "";
    }
    
}
