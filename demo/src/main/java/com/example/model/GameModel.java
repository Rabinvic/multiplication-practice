package com.example.model;

import java.util.LinkedList;
import java.util.List;

public class GameModel {
    private List<Observer<GameModel,String>> observers = new LinkedList<>(); //List of observers
    private boolean[][] hasBeenAsked = new boolean[12][12]; //12x12 matrix to store whether or not a question has been answered correctly by user 
    private int row, col; //Current row and coloumn the model is using

    /**
     * Enum declaring the two different types of game mode, by row or,
     * the whole board randomly
     */
    public enum gameMode{
        ROW,
        RANDOM;
    }

    private gameMode mode; //The gaame mode being used

    /**
     * To create a game model object
     */
    public GameModel(){
        resetBoard();
    }

    /**
     * Method to setup a Random whole grid game
     * 
     * @param mode The mode being played on
     */
    public void gameSetupRandom(){
        this.mode = gameMode.RANDOM;
    }

    /**
     * Method to set up the game in row mode
     * 
     * @param row The row the game will be played on
     */
    public void gameSetupRow(int row){
        this.mode = gameMode.ROW;
        this.row = row;
    }

    /**
     * Returns the game mode being played
     * 
     * @return mode The current game mode
     */
    public gameMode getGameMode(){
        return mode;
    }


    /**
     * Check if a question has been answered
     * @param row The row of the cell
     * @param col The col of the cell
     * @return A bool of whether or not the question has been anwered correctly
     */
    public boolean getAnswered(int row, int col){
        return hasBeenAsked[row - 1][col - 1];
    }

    /**
     * Get the row the game is currently on
     * @return integer for the row the game is currently on
     */
    public int getRow(){
        return this.row;
    }

    /*
     * Get a new question to be asked to the user
     */
    public void getQuestion(){
        //Check if the board or row is completed before getting a new question
        if(checkComplete()){
            alertObservers("Done!");
            return;
        }

        //Determines if row needs to randomized or not
        if(mode == gameMode.RANDOM){
            row = (int) Math.ceil(Math.random() * 12);
        }
        col = (int) Math.ceil(Math.random() * 12);
        if(hasBeenAsked[row-1][col-1]){
            getQuestion();
        }
        alertObservers(row+" x "+col);
    }

    /**
     * Reset the board
     */
    public void resetBoard(){
        for(int i = 0; i < 12; i++){
            for(int j = 0; j < 12; j++){
                hasBeenAsked[i][j] = false;
            }
        }
        alertObservers("reset");
    }

    /**
     * Check if the user's answer is correct
     * @param data String of the user's answer
     */
    public void checkAnswer(String data){
        int answer = Integer.parseInt(data);
        if(answer == row * col){
            hasBeenAsked[row-1][col-1] = true;
            hasBeenAsked[col-1][row-1] = true;
            alertObservers("Correct");
        }else{
            alertObservers("Try again");
        }
    }

    /**
     * Checks if the table is full and answered completly
     * @return Bool of whether or not it is complete
     */
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

    /**
     * Add observer for GUI updates
     * @param observer The observer to be added
     */
    public void addObserver(Observer<GameModel, String> observer){
        this.observers.add(observer);
    }

    /**
     * Alert observers for any updates
     * @param data The data to be sent to observers
     */
    public void alertObservers(String data){
        for(Observer<GameModel, String> observer : observers){
            observer.update(this, data);
        }
    }
}
