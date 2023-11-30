import java.util.HashMap;

/**
 * Trie to store all the words within the dictionary in an efficient to access way.
 * @author Ryan Lambe
 */
public class Trie {

    /**
     * definition of a node in the trie
     */
    private static class TrieNode{
        /**
         * hash map of all the child nodes
         */
        public HashMap<Character, TrieNode> children = new HashMap<>();

        /**
         * if the node is the end of a word
         */
        public boolean word = false;
    }

    /**
     * root node of the trie
     */
    private static final TrieNode root = new TrieNode();

    /**
     * adds specified word to trie
     * @param word to add to trie
     */
    public static void AddWord(String word){
        // for each character in the string, continue down the trie, creating nodes as needed
        // until you reach the end of the word, then mark it as the end of a word.
        TrieNode curr = root;
        for (char c: word.toCharArray()) {
            // System.out.println("Adding node for character: " + c);
            curr = curr.children.computeIfAbsent(c, n -> new TrieNode());
            
        }
        System.out.println("Adding node for character: " + curr);
        curr.word = true;
    }

    /**
     * Checks if word exists within trie
     * @param word to check
     * @return if word exists within trie
     */
    public static boolean FindWord(String word){
        // for each letter in the word continue down the trie until you reach the end,
        // if a node is missing or the final node isn't a word, then return false
        TrieNode curr = root;
        for (char c: word.toCharArray()) {
            TrieNode node = curr.children.get(c);
            if (node == null){
                System.out.println("word not found");
                return false;}
            curr = node;
        }
        System.out.println("word found");
        return curr.word;
    }

    /**
     * removes word from trie
     * @param word to remove from trie
     */
    public static void RemoveWord(String word){
        deleteNodes(word, root, 0);
    }

    /**
     * recursively removes words from tree
     * @param word word to remove
     * @param curr the current subtree to process
     * @param index the index of the character of the word to be checked
     * @return if current subtree has child nodes
     */
    private static boolean deleteNodes(String word, TrieNode curr, int index) {

        // if last node to check
        if (index == word.length()) {
            if (!curr.word)
                return false;

            curr.word = false;
            return curr.children.isEmpty();
        }

        // get next node to check
        char c = word.charAt(index);
        TrieNode node = curr.children.get(c);
        if (node == null)
            return false;

        // search for next node
        boolean shouldDeleteCurrentNode = deleteNodes(word, node, index + 1) && !node.word;

        // delete current node
        if (shouldDeleteCurrentNode) {
            curr.children.remove(c);
            return curr.children.isEmpty();
        }
        return false;
    }

}
// import java.util.HashMap;

// /**
//  * Trie to store all the words within the dictionary in an efficient to access way.
//  * @author Ryan Lambe
//  */
// public class Trie {

//     private static class TrieNode {
//         public HashMap<Character, TrieNode> children = new HashMap<>();
//         public boolean isEndOfWord = false;
//     }

//     private static final TrieNode root = new TrieNode();

//     public static void AddWord(String word) {
//         TrieNode current = root;
//         for (char c : word.toCharArray()) {
//             current.children.putIfAbsent(c, new TrieNode());
//             current = current.children.get(c);
//         }
//         current.isEndOfWord = true;
//     }

//     public static boolean FindWord(String word) {
//         TrieNode current = root;
//         for (char c : word.toCharArray()) {
//             current = current.children.get(c);
//             if (current == null) {
//                 return false;
//             }
//         }
//         return current.isEndOfWord;
//     }

//     public static void RemoveWord(String word) {
//         emoveWord(root, word, 0);
//     }

//     private static boolean RemoveWord(TrieNode current, String word, int index) {
//         if (index == word.length()) {
//             if (!current.isEndOfWord) {
//                 return false; // Word not found
//             }
//             current.isEndOfWord = false;
//             return current.children.isEmpty();
//         }

//         char c = word.charAt(index);
//         TrieNode nextNode = current.children.get(c);

//         if (nextNode == null) {
//             return false; // Word not found
//         }

//         boolean shouldDeleteCurrentNode = removeWord(nextNode, word, index + 1) && !nextNode.isEndOfWord;

//         if (shouldDeleteCurrentNode) {
//             current.children.remove(c);
//             return current.children.isEmpty();
//         }

//         return false;
//     }
// }
