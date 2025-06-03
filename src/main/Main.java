package main;

import controller.GameController;
import model.GameManager;
import view.GameView;
import view.JavaFXLauncher;
import view.SwingUI;
import javafx.application.Application;

public class Main {
    public static void main(String[] args) {
        // 게임 모델과 컨트롤러 초기화
        GameManager model = new GameManager();
        GameController controller = new GameController(model);
        
        // UI 타입에 따라 적절한 뷰를 초기화
         // UI 타입: 1 - Swing, 2 - JavaFX
        switch (Config.getUiType()) {
            case 1:
                GameView view = new SwingUI(controller, model);
                controller.setView(view);
                break;
            case 2:
                Application.launch(JavaFXLauncher.class, args);
                break;
            default:
                System.out.println("잘못된 UI 타입입니다.");
                break;
        }
    }
}