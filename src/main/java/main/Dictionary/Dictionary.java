package main.Dictionary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Dictionary {

    PrefixTree dict; //main prefix tree for dictionary

    public Dictionary() { //constructor
        try {
            createDictionary(); //create dictionary
            addCustomWords(); //add any user define words in it
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //adds user defined words in dictionary
    private void addCustomWords() {

    }

    //create dictionary object in memory using csv file from resources, csv format -> eack line need to have "word","type","meaning" , multiple meaining are seprated by semicolon
    private void createDictionary() throws IOException {
        long start = System.currentTimeMillis();

        dict = new PrefixTree(); //new prefix
        BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/dictionary.csv"))); //read the file

        String line;
        while ((line = reader.readLine()) != null) {
            //System.out.println(line);
            addToRoot(line); //for each line add it in dictionary
        }

        System.out.println("Time took for making Dictionary : " + (System.currentTimeMillis() - start) + " ms");
     }

    //add line of csv in dict
    private void addToRoot(String line) {
        String[] components = line.split("\",\""); //get all components of line, that are [word, type, meaning]
        removeSpaces(components); //remove space and extra symbol from all components

        if (components.length == 3) {  //if the components are valid

            boolean[] status = dict.addWord(components[0], components[1], components[2], 'a'); //try adding in add mode

            if (status[1]) { // if word already exists but have other meaning so need to append the meaning
                dict.addWord(components[0], components[1], components[2], 'u'); //add in update mdoe
            }
        }

        //System.out.println("Added word : " + components[0]);
    }

    //remove unnecessary spaces & symbols from line components
    private void removeSpaces(String[] lines) {
        for (int i = 0; i<lines.length; i++) {
            lines[i] = lines[i].strip();
            lines[i] = lines[i].replaceAll("\"", "");
        }
    }

    //get detail of a word ie type and meaning
    public String[] getWordDetails(String word){
        if (dict.isWordExists(word)) {
            return dict.getWordDetails(word);
        }
        return  null;
    }

    //given a word return a list , containing next words after that word for autocomplete
    public List<String> getAutoComplete(String prefix) {
        List<String> words = dict.getAutoComplete(prefix); //get list of words from prefix
        System.out.println();
        System.out.println("Autocomplete words : ");
        for (String s : words) {
            System.out.println(s);
        }
        return words;
    }
}
