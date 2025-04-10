package com.yut.game.view;

import javax.swing.*;
import java.awt.*;

/**
 * 윷놀이 게임의 메인 창을 관리하는 클래스입니다.
 */
public class MainFrame extends JFrame {
    private BoardPanel boardPanel;
    private YutPanel yutPanel;
    private PlayerPanel playerPanel;
    private GameInfoPanel gameInfoPanel;

    public MainFrame() {
        initializeFrame();
        initializeComponents();
        layoutComponents();
    }

    private void initializeFrame() {
        setTitle("윷놀이 게임");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    private void initializeComponents() {
        boardPanel = new BoardPanel();
        yutPanel = new YutPanel();
        playerPanel = new PlayerPanel();
        gameInfoPanel = new GameInfoPanel();
    }

    private void layoutComponents() {
        // 중앙에 게임판 배치
        add(boardPanel, BorderLayout.CENTER);

        // 오른쪽에 컨트롤 패널 배치
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.add(yutPanel);
        controlPanel.add(playerPanel);
        controlPanel.add(gameInfoPanel);
        add(controlPanel, BorderLayout.EAST);
    }

    public void updateBoard() {
        boardPanel.repaint();
    }

    public void updateYutResult(int result) {
        yutPanel.displayResult(result);
    }

    public void updatePlayerInfo(int currentPlayer) {
        playerPanel.updateCurrentPlayer(currentPlayer);
    }

    public void showGameResult(String winner) {
        JOptionPane.showMessageDialog(this,
            winner + " 팀이 승리했습니다!",
            "게임 종료",
            JOptionPane.INFORMATION_MESSAGE);
    }
} 