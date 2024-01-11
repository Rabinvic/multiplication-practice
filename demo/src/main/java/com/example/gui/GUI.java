package com.example.gui;

import com.example.model.GameModel;
import com.example.model.Observer;
import com.example.model.GameModel.gameMode;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class GUI extends Application implements Observer<GameModel, String>{
    private Stage stage;
    private Scene startMenu;
    private Scene rowSelect;
    private Scene gameScreen;
    private Label questionLabel;
    private Label messageBox;
    private TextField inputTextField;

    private GameModel model;

    @Override
    public void init() throws Exception{
        model = new GameModel();
        model.addObserver(this);
        questionLabel = new Label();
        messageBox = new Label();
    }
    
    @Override
    public void start(Stage stage)throws Exception{
        questionLabel.setFont(Font.font(24));
        messageBox.setFont(Font.font(18));
        this.stage = stage;
        stage.setResizable(false);
        makeStartMenu();
        this.stage.setScene(startMenu);
        this.stage.setTitle("Multiply");
        // this.stage.setMinHeight(600);
        // this.stage.setMinWidth(600);
        this.stage.show();
    }


    private void makeStartMenu(){
        BorderPane menu = new BorderPane();
        Label title = new Label("Multiply");
        title.setStyle("-fx-font-size: 24px;-fx-content-display: center;");
        FlowPane labelPane = new FlowPane(title);
        menu.setTop(labelPane);
        labelPane.setAlignment(Pos.TOP_CENTER);
        
        HBox fp = new HBox();
        Button rowModeButton = new Button("Row");
        rowModeButton.setOnAction(event -> {makeRowSelect();});


        Button randomModeButton = new Button("Random");
        randomModeButton.setOnAction(event -> {model.gameSetupRandom(); makeGameScene();});

        Label instruct = new Label("Select game mode:\t");
        title.setStyle("-fx-font-size: 24px;-fx-content-display: center;");

        fp.getChildren().addAll(instruct, rowModeButton, randomModeButton);
        menu.setCenter(fp);
        fp.setAlignment(Pos.CENTER);
        fp.setPrefSize(300, 200);
        fp.prefWidthProperty().bind(menu.widthProperty());
        
        startMenu = new Scene(menu);
    }

    private void makeRowSelect(){
        BorderPane bp = new BorderPane();
        Label title = new Label("Select Row");
        title.setStyle("-fx-font-size: 24px;-fx-content-display: center;");
        HBox labelPane = new HBox(40, title);
        bp.setTop(labelPane);
        labelPane.setAlignment(Pos.TOP_CENTER);

        HBox fp = new HBox(10);
        for(int i = 1; i<=12; i++){
            Button button = new Button(Integer.toString(i));
            button.setMinSize(40, 40);
            int r = i;
            button.setOnAction(event -> {model.gameSetupRow(r);makeGameScene();});
            fp.getChildren().add(button);
        }

        bp.setCenter(fp);
        fp.setAlignment(Pos.CENTER);
        fp.setPrefSize(600, 200);

        rowSelect = new Scene(bp);

        stage.setScene(rowSelect);
    }
    private void makeGameScene(){
        BorderPane bp = new BorderPane();
        VBox rightPanel = new VBox();
        rightPanel.getChildren().add(this.questionLabel);
        inputTextField = new TextField();
        inputTextField.setMaxWidth(questionLabel.getMinWidth());
        inputTextField.setOnAction(event -> {model.checkAnswer(inputTextField.getText());});
        rightPanel.getChildren().add(inputTextField);
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(event -> {model.checkAnswer(inputTextField.getText());});
        rightPanel.getChildren().add(submitButton);

        Button resetButton = new Button("Reset Board");
        resetButton.setOnAction(event -> {model.resetBoard();});
        rightPanel.getChildren().add(resetButton);

        Button rowButton = new Button("Select Row");
        rowButton.setOnAction(event -> {makeRowSelect();});
        rightPanel.getChildren().add(rowButton);

        Button randomButton = new Button("Random");
        randomButton.setOnAction(event -> {model.gameSetupRandom();model.getQuestion();});
        rightPanel.getChildren().add(randomButton);

        rightPanel.setAlignment(Pos.CENTER);
        bp.setRight(rightPanel);
        bp.setTop(messageBox);

        GridPane table = new GridPane();
        for(int i = 0; i <= 12; i++){
            for( int j = 0; j <= 12; j++){
                Button b = new Button();
                b.setMinSize(40,40);//(stage.getWidth()-100)/13, (stage.getWidth()-100)/13);
                if(i==0 && j==0){
                    continue;
                }else if(i == 0){
                    b.setText(Integer.toString(j));
                    b.setFont(Font.font(STYLESHEET_CASPIAN, FontWeight.BOLD, 16));
                    table.add(b, j, i);
                } else if(j == 0){
                    b.setText(Integer.toString(i));
                    b.setFont(Font.font(STYLESHEET_CASPIAN, FontWeight.BOLD, 16));
                    table.add(b, j, i);
                } else {
                    if(model.getAnswered(i, j)){
                        b.setText(Integer.toString(i*j));
                    } else {
                        b.setText("   ");
                    }
                    table.add(b, j, i);
                }
            }
        }
        model.getQuestion();
        bp.setCenter(table);
    //     if(model.getGameMode() == gameMode.RANDOM){

    //     }
        gameScreen = new Scene(bp);
        stage.setScene(gameScreen);
    }

    @Override
    public void update(GameModel model, String msg){
        if(msg.matches("(.*) x (.*)")){
            questionLabel.setText(msg);
            return;
        } else if(msg.startsWith("Correct")){
            messageBox.setText(msg);
            makeGameScene();
            return;
        } else if(msg.startsWith("Try")){
            messageBox.setText(msg);
            return;
        } else if(msg.startsWith("Done")){
            if(model.getGameMode() == gameMode.ROW){
                questionLabel.setText("Row " + Integer.toString(model.getRow()) + " is complete");
            } else {
                questionLabel.setText(msg);
            }
            messageBox.setText("");
            inputTextField.setOnAction(null);
            return;
        } else if(msg.startsWith("reset")){
            makeGameScene();
            return;
        }
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}