package main;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.Dictionary.Dictionary;

public class MainController {

    //Refrences to fxml objects/nodes
    @FXML
    public Label word;  //to show word after search
    @FXML
    public Label wordType; //to show word type after seach
    @FXML
    public VBox meaning; //shows list of Text objects as meanings
    @FXML
    private JFXButton prevWord; //button to show previous word
    @FXML
    private JFXButton nextWord; //button to show next word
    @FXML
    private ImageView deleteWord; //icon for deleting a word
    @FXML
    private ImageView addWord; //icon for adding a word
    @FXML
    private JFXTextField search; //text feild to seach for word / seach bar
    @FXML
    private ImageView searchIcon; // icon to click for seach
    @FXML
    private ImageView bookmarks; //icon to show all bookmarks
    @FXML
    private ImageView about; // icon to show about this app
    @FXML
    private ImageView addToBookmark; //icon for adding a word in bookmarks

    ScaleTransition aboutT, bookmarksT, addToBookmarkT, deleteT, addT, preBuT, nextBuT; //animations to show on icons
    double scale = 1.3; //size of icons for scale animation
    double dur = 200; //time for icon animations
    double stageWidth = 0; //store width of stage in gap of 100 to change size of word line , needs to do for performance issue, works for listener

    Stage stage; //main window
    Dictionary dictionary; //main dictionary for all words

    //on start set animations and initialize the dictionary and custom words
    @FXML
    private void initialize() {

        setAnimations(); //set animations on icons
        new Thread(()-> { //load dictionary in new thread , average will take approx 600-700ms, but in same thread will make ui stuck for that time
            dictionary = new Dictionary();
        }).start();
    }

    //set stage/window for main controller,
    public void setStage(Stage stage) {this.stage = stage;}

    //will take word from seach bar and seach it , if exists add it in user interface
    @FXML
    private void searchWord() {

        cleanInfo(); //clear previous info

        String input = search.getText(); //word to search
        input = input.strip(); //remove spaces
        String[] details; //to store word details

        if ((details = dictionary.getWordDetails(input)) != null) { //if word found in dictionary

            word.setText(input); //set word in ui
            wordType.setText(getType(details[0])) ; //set type in UI after conversion
            meaning.getChildren().addAll(getMeanings(details[1])); //set all the possible meanings in use interface

        } else { //if no such word exists

            wordType.setText("");
            word.setText("No word found as '"+search.getText()+"'"); //show msg for not found
        }

        search.setText(""); //clear the seach bar for new word

    }

    //set animations on all the icons needed
    private void setAnimations() {

        //about
        aboutT = new ScaleTransition(Duration.millis(dur), about);
        about.setOnMouseEntered(e -> {
            aboutT.setToX(scale);
            aboutT.setToY(scale);
            aboutT.playFromStart();
        });
        about.setOnMouseExited(e->{
            aboutT.setToX(1);
            aboutT.setToY(1);
            aboutT.playFromStart();
        });

        //bookmarks
        bookmarksT = new ScaleTransition(Duration.millis(dur), bookmarks);
        bookmarks.setOnMouseEntered(e -> {
            bookmarksT.setToX(scale);
            bookmarksT.setToY(scale);
            bookmarksT.playFromStart();
        });
        bookmarks.setOnMouseExited(e->{
            bookmarksT.setToX(1);
            bookmarksT.setToY(1);
            bookmarksT.playFromStart();
        });

        //add to bookmarks
        addToBookmarkT = new ScaleTransition(Duration.millis(dur), addToBookmark);
        addToBookmark.setOnMouseEntered(e -> {
            addToBookmarkT.setToX(scale);
            addToBookmarkT.setToY(scale);
            addToBookmarkT.playFromStart();
        });
        addToBookmark.setOnMouseExited(e->{
            addToBookmarkT.setToX(1);
            addToBookmarkT.setToY(1);
            addToBookmarkT.playFromStart();
        });

        //delete
        deleteT = new ScaleTransition(Duration.millis(dur), deleteWord);
        deleteWord.setOnMouseEntered(e -> {
            deleteT.setToX(scale);
            deleteT.setToY(scale);
            deleteT.playFromStart();
        });
        deleteWord.setOnMouseExited(e->{
            deleteT.setToX(1);
            deleteT.setToY(1);
            deleteT.playFromStart();
        });

        //add
        addT = new ScaleTransition(Duration.millis(dur), addWord);
        addWord.setOnMouseEntered(e -> {
            addT.setToX(scale);
            addT.setToY(scale);
            addT.playFromStart();
        });
        addWord.setOnMouseExited(e->{
            addT.setToX(1);
            addT.setToY(1);
            addT.playFromStart();
        });

        //pre button
        preBuT = new ScaleTransition(Duration.millis(dur), prevWord);
        prevWord.setOnMouseEntered(e -> {
            preBuT.setToY(scale);
            preBuT.playFromStart();
        });
        prevWord.setOnMouseExited(e->{
            preBuT.setToY(1);
            preBuT.playFromStart();
        });

        //next button
        nextBuT = new ScaleTransition(Duration.millis(dur), nextWord);
        nextWord.setOnMouseEntered(e -> {
            nextBuT.setToY(scale);
            nextBuT.playFromStart();
        });
        nextWord.setOnMouseExited(e->{
            nextBuT.setToY(1);
            nextBuT.playFromStart();
        });
    }

    //set all required listners
    public void setListenersAndEvents() {
        cleanInfo();

        stage.widthProperty().addListener(e->{
            if (stage.getWidth() > stageWidth+100 || stage.getWidth() < stageWidth-100){
                for (Node text : meaning.getChildren()) {
                    Text t = (Text) text;
                    t.setWrappingWidth(stage.getWidth() - 100);
                    stageWidth = stage.getWidth();
                }
            }
        });

        searchIcon.setOnMouseClicked(e->{
            searchWord();
        });

        search.textProperty().addListener(e->{
            if (search.getText().length() > 2) {
                dictionary.getAutoComplete(search.getText());
            }
        });
    }

    //given a line of meanings , return an array of Text object to add in UI
    private Text[] getMeanings(String detail) {
        String[] meanings = detail.split(";"); //get all meanings
        for (int i = 0; i<meanings.length; i++) meanings[i] = meanings[i].strip(); //remove white spaces
        Text[] texts = new Text[meanings.length]; //make an arary for Text objects

        for (int i = 0; i<meanings.length; i++) { //create all text objects for meanings

            Text text = new Text(meanings[i]);
            text.setFont(Font.font(20));
            text.setFill(Color.WHITE);
            text.setWrappingWidth(stageWidth-100);
            texts[i] = text;
        }

        return texts;
    }

    //clean word, type and meaning from ui
    private void cleanInfo(){
        wordType.setText("");
        word.setText("");
        meaning.getChildren().clear();

    }

    //given a code for type will convert it in readable format, types(codes) are -> n. noun, a. adjective,
    private String getType(String type) {

        switch (type) {
            case "n." : return "Noun";
            case "a." : return "Adjective";
            default: return "";
        }
    }
}
