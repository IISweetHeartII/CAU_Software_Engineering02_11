package main;

import controller.GameController;
import model.GameManager;
import view.JavaFXLauncher;
import view.SwingUI;
import javafx.application.Application;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int uiType = getUITypeFromConfig();
        
        // 게임 모델과 컨트롤러 초기화
        GameManager model = new GameManager();
        GameController controller = new GameController(model);
        
        // UI 타입에 따라 적절한 뷰를 초기화
         // UI 타입: 1 - Swing, 2 - JavaFX
        switch (uiType) {
            case 1:
                SwingUI view = new SwingUI(controller, model);
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

    private static int getUITypeFromConfig() {
        try (Scanner scanner = new Scanner(new File("src/data/config.txt"))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.startsWith("ui:")) {
                    String[] parts = line.split(":");
                    if (parts.length == 2) {
                        return Integer.parseInt(parts[1].trim());
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("config.txt 파일을 찾을 수 없습니다.");
        } catch (NumberFormatException e) {
            System.err.println("ui 타입 설정이 잘못되었습니다.");
        }
        return 1; // 기본값: Swing
    }
}