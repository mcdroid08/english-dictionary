package main.Dictionary;

import java.io.Serializable;
import java.util.List;

public class PrefixTree implements Serializable {

    TrieNode root;
    private final int MAX_WORDS_FOR_AUTOCOMPLETE = 10;

    public PrefixTree() {
        root = new TrieNode();
    } //constructor

    //add word in prefix tree also return [status if done , or is exists] , mode can be a-> add, u->update
    public boolean[] addWord(String word, String type, String meaning, char mode) {

        //if word or meaning is invalid
        if (word == null || word.length() < 1 || meaning.length() < 1) return new boolean[]{false, false};
        //if word already exists when add mode
        if (mode == 'a' && isWordExists(word)) return new boolean[]{false, true};

        TrieNode wordNode = setLastNodeForWord(word); //set all nodes for that word
        wordNode.setIsWord(true); //set it as word

        if (mode == 'u') { //if mode is update join the new meaning
            wordNode.setMeaning(wordNode.getMeaning() + ";" + meaning);
        } else { //when on add mode
            wordNode.setType(type);
            wordNode.setMeaning(meaning);
        }

        return new boolean[]{true, false};
    }

    //given a word , if it exists, will remove it
    public boolean removeWord(String word) {

        //if word not found or invalid word
        if (word == null || word.length() < 1 || !isWordExists(word)) return false;

        //remove word (we are sure here that word must exists)
        TrieNode wordNode = getLastNodeForWord(word); //get last node for that word
        assert wordNode != null;
        wordNode.setIsWord(false); //reset it
        wordNode.setType("");
        wordNode.setMeaning("");
        return true;
    }

    //tells is given word exists in prefix tree or not
    public boolean isWordExists(String word) {

        //if word is invalid
        if (word == null || word.length() < 1) return false;

        TrieNode curNode = root, preNode = null; //pointers to trienodes
        char c;

        for (int i = 0; i < word.length(); i++) { //for each character of word
            c = word.charAt(i);

            if (curNode.isNull(c)) return false; //if there is null means no word found
            preNode = curNode;
            curNode = curNode.getNext(c); //move to node of next level
        }

        return preNode.isWord(); //if it forms a word
    }

    //given a word return last node for that word/prefix , return null if not exists
    private TrieNode getLastNodeForWord(String word) {

        TrieNode curNode = root, preNode = null; //pointers for trienode
        char c;

        for (int i = 0; i < word.length(); i++) { //for each character of word
            c = word.charAt(i);
            if (curNode.isNull(c)) { return null; } //if there is no such charecter return null
            preNode = curNode;
            curNode = curNode.getNext(c); //move to node of next level
        }

        return preNode;
    }

    //given a word return last node for that word/prefix , will create nodes if not exists
    private TrieNode setLastNodeForWord(String word) {

        TrieNode curNode = root, preNode = null; //pointer for trienodes
        char c;

        for (int i = 0; i < word.length(); i++) {
            c = word.charAt(i);
            curNode.setChar(c); //create new node , will only work when its not there
            preNode = curNode;
            curNode = curNode.getNext(c); //move to node of next level
        }

        return preNode; //return node of last level
    }

    //given a word return a pair , with type and meaning in string array , details = [type, meaning]
    public String[] getWordDetails(String word) {
        TrieNode node = getLastNodeForWord(word); //get last node for that word
        if (node != null && node.isWord()) {  // if that last word exists in prefix tree
            return  new String[] {node.getType(), node.getMeaning()};
        }
        return null; //if word not exists return null
    }

    //given a prefix return a list of nodes that can be possible after that prefix
    public List<String> getAutoComplete(String prefix) {
        TrieNode t = getLastNodeForWord(prefix); //get last node for specific prefix
        if (t != null) { //if that node not null OR that prefix exists
            return t.getNextWords(prefix, MAX_WORDS_FOR_AUTOCOMPLETE); //return list of nodes
        }
        return null; //if that prefix not exists return null
    }
}
