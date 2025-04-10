package com.yut.game;

import com.yut.game.controller.GameController;
import com.yut.game.model.GameEngine;
import com.yut.game.view.MainFrame;

import javax.swing.SwingUtilities;

/**
 * 윷놀이 게임의 메인 클래스입니다.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameEngine gameEngine = new GameEngine();
            MainFrame mainFrame = new MainFrame();
            GameController controller = new GameController(gameEngine, mainFrame);
            
            // 게임 창 표시
            mainFrame.setVisible(true);
        });
    }
} 