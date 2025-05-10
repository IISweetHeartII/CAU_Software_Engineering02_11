package main;

import Controller.GameController;
import View.*;
import Model.GameModel;

public class Main {
    public static void main(String[] args) {
        // Initialize the game model and view
        /// GameModel gameModel = new GameModel(numPlayers, numPieces);
        /// GameView gameView = new MainUI_Swing(); // interface GameView를 구현한 MainUI_Swing 사용
        GameController gameController;
        // Initialize the game controller
        /// GameController gameController = new GameController(gameModel, gameView);

        // Start the game loop
        while (true) {
            gameController.playGame();
        }
    }
}