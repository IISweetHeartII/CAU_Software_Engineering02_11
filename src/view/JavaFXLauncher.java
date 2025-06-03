// JavaFXLauncher.java
package view;

import controller.GameController;
import javafx.application.Application;
import javafx.stage.Stage;
import model.GameManager;

public class JavaFXLauncher extends Application {
    @Override
    public void start(Stage stage) {
        GameManager model = new GameManager();
        GameController controller = new GameController(model);
        GameView view = new JavafxUI(controller, model);
        controller.setView(view);

        view.start(stage); // JavaFXUI에서 Stage 시작
    }
}
