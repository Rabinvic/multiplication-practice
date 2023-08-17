package com.example.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class GameModel {
    private List<Observer<GameModel,String>> observers = new LinkedList<>();
    private int[][] userAnswerBoard = new int[12][12];
    private boolean[][] hasBeenAsked = new boolean[12][12];
    private int row, col;
    private Random rand = new Random();

    public enum gameMode{
        ROW,
        RANDOM;
    }

    private gameMode mode;

    public GameModel(){}

    public void gameSetup(gameMode mode){
        this.mode = mode;
    }

    public void gameSetup(gameMode mode, int row){
        this.mode = mode;
        this.row = row;
    }

    public void getCell(){
        if(mode == gameMode.RANDOM){
            row = rand.nextInt(12) + 1;
        }
        col = rand.nextInt(12) + 1;
        if(hasBeenAsked[row-1][col-1]){
            getCell();
        }
        hasBeenAsked[row-1][col-1] = true;
        alertObservers(row+" x "+col);
    }

    public void checkAnswer(String data){
        int answer = Integer.parseInt(data);
        if(answer == row * col){
            userAnswerBoard[row-1][col-1] = answer;
            alertObservers("Correct");
        }else{
            alertObservers("Try again");
        }
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
