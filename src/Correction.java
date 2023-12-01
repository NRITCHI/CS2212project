import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Correction is used to find what corrections should be made
 */
public class Correction {

    /**
     * the different types of punctuation a sentence can end in
     */
    private static ArrayList<Character> endingPunctuation;

    /**
     * Breaks up text into words and checks each for possible corrections
     * @param text whole text document
     * @return locations of corrections, used to underline in red, first value is index, second value is size
     */
    public static Pair<Integer, Integer>[] GetCorrections(String text){

        if(endingPunctuation == null){
            endingPunctuation = new ArrayList<>();
            endingPunctuation.add('.');
            endingPunctuation.add('?');
            endingPunctuation.add('!');
        }

        //seperate a string into words by space or punctuation as long as it is not entirely numbers
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

        ArrayList<Pair<Integer, Integer>> errorList = new ArrayList<>();

        for (int i = 0; i < pairsArray.length; i++) {
            int startIndex = pairsArray[i].first;
            int wordLength = pairsArray[i].second;
            String word = text.substring(startIndex, startIndex + wordLength);

            // check spelling
            String[] spellingSuggestions = null;
            if(!(Dictionary.FindWord(word.toLowerCase()) || Dictionary.FindWord(word.toUpperCase()) || Dictionary.FindWord(word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase()))){
                spellingSuggestions = CheckSpelling(word);
            }
            if (spellingSuggestions != null) {

                if(Window.NotIgnoredCorrection(CorrectionType.Misspelling, pairsArray[i], spellingSuggestions)){
                    Window.AddCorrection(CorrectionType.Misspelling, pairsArray[i], spellingSuggestions);
                    errorList.add(pairsArray[i]);
                    continue;
                }
            }

            // check capitalization
            boolean shouldBeUpper = false;
            boolean missingSpace = false;
            boolean allCaps = false;
            boolean capitalized = word.substring(0, 1).toUpperCase().equals(word.substring(0, 1));

            if (Dictionary.FindWord(word.toUpperCase())){
                if(!(Dictionary.FindWord(word.toLowerCase()) || Dictionary.FindWord(word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase()))){
                    shouldBeUpper = true;
                    allCaps = true;
                }
            }

            if (Dictionary.FindWord(word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())){

                if(!(Dictionary.FindWord(word.toLowerCase()) || Dictionary.FindWord(word.toUpperCase()))){
                    shouldBeUpper = true;
                }
            }

            if(word.equalsIgnoreCase("i")){
                shouldBeUpper = true;
            }

            if (i == 0 || text.charAt(startIndex - 1) == '\n'){
                shouldBeUpper = true;
            }
            else if (text.charAt(startIndex - 1) == ' ' && endingPunctuation.contains(text.charAt(startIndex - 2))){
                shouldBeUpper = true;
            }
            else if (endingPunctuation.contains(text.charAt(startIndex - 1))){
                shouldBeUpper = true;
                missingSpace = true;
            }

            if(shouldBeUpper && !capitalized) {
                // lower case
                String correction = word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
                if(missingSpace)
                    correction = " " + correction;
                if(allCaps)
                    correction = correction.toUpperCase();

                if(Window.NotIgnoredCorrection(CorrectionType.Miscapitalization, pairsArray[i], new String[]{correction})){
                    Window.AddCorrection(CorrectionType.Miscapitalization, pairsArray[i], new String[]{correction});
                    errorList.add(pairsArray[i]);
                    continue;
                }
            }
            else if (!shouldBeUpper && capitalized) {

                String correction = word.toLowerCase();
                if(Window.NotIgnoredCorrection(CorrectionType.Miscapitalization, pairsArray[i], new String[]{correction})){
                    Window.AddCorrection(CorrectionType.Miscapitalization, pairsArray[i], new String[]{correction});
                    errorList.add(pairsArray[i]);
                    continue;
                }
            }

            // check double words
            if(i == 0 || startIndex < 2)
                continue;

            if(text.charAt(startIndex - 1) != ' ')
                continue;

            if(text.charAt(pairsArray[i-1].first + pairsArray[i-1].second - 1) != text.charAt(startIndex - 2))
                continue;

            // check if word is equal to prior word
            if(word.equals(text.substring(pairsArray[i-1].first, pairsArray[i-1].first + pairsArray[i-1].second))){

                Pair<Integer, Integer> location = new Pair<>(pairsArray[i].first - 1, pairsArray[i].second + 1);

                if(Window.NotIgnoredCorrection(CorrectionType.DoubleWords, location, new String[]{""})){
                    Window.AddCorrection(CorrectionType.DoubleWords, location, new String[]{""});
                    errorList.add(pairsArray[i]);
                }
            }
        }

        Pair<Integer, Integer>[] errorArray = new Pair[]{};
        return errorList.toArray(errorArray);
    }



     /**
     * Method to find alternative spellings of a given word
     * @param word to check for alternatives to
     * @return alternate words
     */
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

}
