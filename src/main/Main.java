package main;

import controller.GameController;
import model.GameManager;
import view.SwingUI;

public class Main {
    public static void main(String[] args) {
        // 게임 모델과 컨트롤러 초기화
        GameManager model = new GameManager();
        GameController controller = new GameController(model);
        // Todo: UI를 위한 Interface를 구현하여 SwingUI와 GameController를 연결합니다.
        SwingUI view = new SwingUI(controller, model);
        controller.setView(view);

        // 게임 시작
        while (true) {
            if (controller.resetGame()) {
                model = new GameManager();
                controller = new GameController(model); // -----> View.initUI() 포함한다
            }
            if (controller.endGame()) {
                break;
            }
        }
    }
}