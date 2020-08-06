package main.Dictionary;

import java.util.ArrayList;
import java.util.List;

public class TrieNode {

    private final TrieNode[] chars; //stores trienode object for all characters
    private boolean isWord = false; //defines if word exists till this node
    private String type, meaning;
    private final int NO_OF_CHARACTERS = 42;

    public TrieNode() {
        chars = new TrieNode[NO_OF_CHARACTERS];
        type = "";
        meaning = "";
    } //constructor

    //getters
    public String getType() {
        return type;
    }
    public String getMeaning() {
        return meaning;
    }
    public boolean isWord() {
        return isWord;
    }

    //setters
    public void setType(String type) {
        this.type = type;
    }
    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }
    public void setIsWord(boolean val) {
        isWord = val;
    }

    //return trienode by given character
    public TrieNode getNext(char c) {
        return chars[getIndex(c)];
    }

    //return trienode by given index
    public TrieNode getNext(int index) {
        if (isValidIndex(index)) return chars[index];
        return null;
    }

    //crate trienode for given character
    public void setChar(char c) {
        int index = getIndex(c);
        if (chars[index] == null)
            chars[index] = new TrieNode();
    }

    //crate trie node at given index
    public void setChar(int index) {
        if (isValidIndex(index)) chars[index] = new TrieNode();
    }

    //return trienode for character is null or not
    public boolean isNull(char c) {
        return chars[getIndex(c)] == null;
    }

    //return trienode for index is null or not
    public boolean isNull(int index) {
        if (isValidIndex(index)) return chars[index] == null;
        return true;
    }

    //returns if given index is a valid index for trie
    public boolean isValidIndex(int index) {
        return index >= 0 && index < NO_OF_CHARACTERS;
    }

    //return index of trienode for given character
    public int getIndex(char c) {
        c= Character.toLowerCase(c);

        if (c == ' ') return 36;
        else if (c == '-') return 37;
        else if (c == '_') return 38;
        else if (c == '\'') return 39;
        else if (c == '/') return 40;
        else if (c == '.') return 41;
        else if (c >= '0' && c <= '9') return c - '0';
        else return c - 'a';
    }

    //return character for given index
    public char getChar(int c) {

        if (c >= 0 && c<26) return (char) ('a'+c);
        else if (c > 26 && c < 36) return (char) ('0' + c-26);
        else if (c == 36) return ' ';
        else if (c == 37) return '-';
        else if (c == 38) return '_';
        else if (c == 39) return '\'';
        else if (c == 40) return '/';
        else if (c == 41) return '.';
        else return '#';
    }

    //return count number of words after prefix
    public List<String> getNextWords(String prefix, int count) {
        List<String>  result = new ArrayList<>();
        getAllWords(this, "", result, count, prefix);
        return result;
    }

    //recursive helper funcntion of getNextWords
    private void getAllWords(TrieNode node, String word, List<String> result, int count, String prefix) {
        if (node == null || result.size() >= count) {
            return;
        }
        if (node.isWord()) {
            result.add(prefix + word);
        }
        for (int i = 0; i<chars.length; i++) {
            if (node.chars[i] != null){
                getAllWords(node.chars[i], word + node.getChar(i), result, count, prefix);
            }
        }
    }
}
