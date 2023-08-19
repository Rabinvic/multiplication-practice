package com.example.model;

import java.util.LinkedList;
import java.util.List;

public class GameModel {
    private List<Observer<GameModel,String>> observers = new LinkedList<>();
    private boolean[][] hasBeenAsked = new boolean[12][12];
    private int row, col;

    public enum gameMode{
        ROW,
        RANDOM;
    }

    private gameMode mode;

    public GameModel(){
        resetBoard();
    }

    public void gameSetup(gameMode mode){
        this.mode = mode;
    }

    public void gameSetup(gameMode mode, int row){
        this.mode = mode;
        this.row = row;
    }

    public gameMode getGameMode(){
        return mode;
    }

    public boolean getAnswered(int row, int col){
        return hasBeenAsked[row - 1][col - 1];
    }

    public void getQuestion(){
        if(checkComplete()){
            alertObservers("Done!");
            return;
        }


        if(mode == gameMode.RANDOM){
            row = (int) Math.ceil(Math.random() * 12);
        }
        col = (int) Math.ceil(Math.random() * 12);
        if(hasBeenAsked[row-1][col-1]){
            getQuestion();
        }
        alertObservers(row+" x "+col);
    }

    public void resetBoard(){
        for(int i = 0; i < 12; i++){
            for(int j = 0; j < 12; j++){
                hasBeenAsked[i][j] = false;
            }
        }
        alertObservers("reset");
    }

    public void checkAnswer(String data){
        int answer = Integer.parseInt(data);
        if(answer == row * col){
            hasBeenAsked[row-1][col-1] = true;
            alertObservers("Correct");
        }else{
            alertObservers("Try again");
        }
    }

    public boolean checkComplete(){
        boolean isDone = true;
        if(mode == gameMode.ROW){
            for(int j = 0; j < 12; j++){
                if(!hasBeenAsked[row - 1][j]) isDone = false;
            }
        } else {
            for(int i = 0; i < 12; i++){
                for(int j = 0; j < 12; j++){
                    if(!hasBeenAsked[i][j]) isDone = false;
                }
            }
        }

        return isDone;
    }

    public void addObserver(Observer<GameModel, String> observer){
        this.observers.add(observer);
    }

    public void alertObservers(String data){
        for(Observer<GameModel, String> observer : observers){
            observer.update(this, data);
        }
    }
}
