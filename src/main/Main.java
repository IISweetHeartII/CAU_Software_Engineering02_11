package main;

import Controller.GameController;
import Model.GameModel;

public class Main {
    public static void main(String[] args) {
        // GameModel 생성
        GameModel gameModel = new GameModel();

        // GameController 생성 (GameModel과 연결)
        GameController controller = new GameController(gameModel); // -----> View.initUI() 포함한다
        // MainUI_Swing은 GameController 내부에서 초기화됨
        // MainUI_Swing은 GameController를 통해 GameModel과 상호작용

        // 게임 시작
        while (true) {
            if (controller.resetGame()) {
                gameModel = new GameModel();
                controller = new GameController(gameModel); // -----> View.initUI() 포함한다
            }
            if (controller.endGame()) {
                break;
            }
        }
    }
}