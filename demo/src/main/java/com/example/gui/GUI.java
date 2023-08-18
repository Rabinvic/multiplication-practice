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

    private GameModel model;

    @Override
    public void init() throws Exception{
        model = new GameModel();
        model.addObserver(this);
        questionLabel = new Label();
    }
    
    @Override
    public void start(Stage stage)throws Exception{
        questionLabel.setFont(Font.font(24));
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
        bp.setRight(rightPanel);
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
        }
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}