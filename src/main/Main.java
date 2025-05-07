package main;

import Controller.GameController;
import View.*;
import Model.GameModel;

public class Main {
    public static void main(String[] args) {
        int numPlayers = 2; // Number of players
        int numPieces = 4; // Number of pieces per player
        // Initialize the game model and view
        /// GameModel gameModel = new GameModel(numPlayers, numPieces);
        /// GameView gameView = new MainUI_Swing(); // interface GameView를 구현한 MainUI_Swing 사용

        // Initialize the game controller
        /// GameController gameController = new GameController(gameModel, gameView);

        // Start the game loop
        while (true) {
            // Handle player input and game logic
            // For example, you can call gameController.handleRandomThrow() or gameController.handleManualThrow(YutResultType.YUT);
            // Add your game loop logic here
        }
    }
}