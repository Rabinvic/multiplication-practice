package com.example.gui;

import com.example.model.GameModel;
import com.example.model.Observer;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class GUI extends Application implements Observer<GameModel, String>{
    private Stage stage;
    private Scene startMenu;
    private Scene rowSelect;
    private Scene gameScreen;

    private GameModel model;

    @Override
    public void init() throws Exception{
        model = new GameModel();
        model.addObserver(this);
    }
    
    @Override
    public void start(Stage stage)throws Exception{
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

        Label instruct = new Label("Select game mode:\t");
        title.setStyle("-fx-font-size: 24px;-fx-content-display: center;");

        fp.getChildren().addAll(instruct, rowModeButton, randomModeButton);
        menu.setBottom(fp);
        fp.setAlignment(Pos.BOTTOM_CENTER);
        
        startMenu = new Scene(menu);
    }

    private void makeRowSelect(){}
    private void makeGameScene(){}

    @Override
    public void update(GameModel model, String string){}

    public static void main(String[] args) {
        Application.launch(args);
    }
}