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
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import java.util.regex.Matcher;

public class GUI extends Application implements Observer<GameModel, String>{
    private Stage stage;
    private Scene startMenu;
    private Scene rowSelect;
    private Scene gameScreen;
    private Label questionLabel;
    private Label messgaeBox;

    private GameModel model;

    @Override
    public void init() throws Exception{
        model = new GameModel();
        model.addObserver(this);
        questionLabel = new Label();
        messgaeBox = new Label();
    }
    
    @Override
    public void start(Stage stage)throws Exception{
        questionLabel.setFont(Font.font(24));
        messgaeBox.setFont(Font.font(18));
        this.stage = stage;
        stage.setResizable(false);
        makeStartMenu();
        this.stage.setScene(startMenu);
        this.stage.show();
    }


    private void makeStartMenu(){
        BorderPane menu = new BorderPane();
        Label title = new Label("Multiply");
        title.setStyle("-fx-font-size: 24px;-fx-content-display: center;");
        FlowPane labelPane = new FlowPane(title);
        menu.setTop(labelPane);
        labelPane.setAlignment(Pos.TOP_CENTER);
        
        FlowPane fp = new FlowPane();
        Button rowModeButton = new Button("Row");
        rowModeButton.setOnAction(event -> {makeRowSelect();});

        Button randomModeButton = new Button("Random");
        randomModeButton.setOnAction(event -> {model.gameSetup(gameMode.RANDOM); makeGameScene();});

        Label instruct = new Label("Select game mode:\t");
        title.setStyle("-fx-font-size: 24px;-fx-content-display: center;");

        fp.getChildren().addAll(instruct, rowModeButton, randomModeButton);
        menu.setBottom(fp);
        fp.setAlignment(Pos.BOTTOM_CENTER);
        
        startMenu = new Scene(menu);
    }

    private void makeRowSelect(){
        BorderPane bp = new BorderPane();
        Label title = new Label("Select Row");
        title.setStyle("-fx-font-size: 24px;-fx-content-display: center;");
        FlowPane labelPane = new FlowPane(title);
        bp.setTop(labelPane);
        labelPane.setAlignment(Pos.TOP_CENTER);

        FlowPane fp = new FlowPane();
        for(int i = 1; i<=12; i++){
            Button button = new Button(Integer.toString(i));
            int r = i;
            button.setOnAction(event -> {model.gameSetup(gameMode.ROW, r);makeGameScene();});
            fp.getChildren().add(button);
        }

        bp.setCenter(fp);
        fp.setAlignment(Pos.CENTER);

        rowSelect = new Scene(bp);

        stage.setScene(rowSelect);
    }
    private void makeGameScene(){
        BorderPane bp = new BorderPane();
        BorderPane rightPanel = new BorderPane();
        model.getQuestion();
        rightPanel.setTop(this.questionLabel);
        TextField inputTextField = new TextField();
        rightPanel.setCenter(inputTextField);
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(event -> {model.checkAnswer(inputTextField.getText());});
        rightPanel.setBottom(submitButton);
        bp.setRight(rightPanel);
        bp.setTop(messgaeBox);

        GridPane table = new GridPane();
        for(int i = 0; i <= 12; i++){
            for( int j = 0; j <= 12; j++){
                Button b = new Button();
                if(i==0 && j==0){
                    continue;
                }else if(i == 0){
                    b.setText(Integer.toString(j));
                    table.add(b, j, i);
                } else if(j == 0){
                    b.setText(Integer.toString(i));
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
            messgaeBox.setText(msg);
            makeGameScene();
            return;
        } else if(msg.startsWith("Try")){
            messgaeBox.setText(msg);
            return;
        } else if(msg.startsWith("Done")){
            questionLabel.setText(msg);
            return;
        }
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}