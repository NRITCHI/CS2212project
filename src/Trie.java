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
         * the character that the node represents
         */
        public char content;

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
            curr = curr.children.computeIfAbsent(c, n -> new TrieNode());
        }
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
            if (node == null)
                return false;
            curr = node;
        }
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
