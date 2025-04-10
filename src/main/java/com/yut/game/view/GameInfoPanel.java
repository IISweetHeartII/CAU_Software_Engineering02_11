package com.yut.game.view;

import javax.swing.*;
import java.awt.*;

/**
 * 게임 정보를 표시하는 패널 클래스입니다.
 */
public class GameInfoPanel extends JPanel {
    private JLabel moveCountLabel;
    private JLabel captureCountLabel;
    private JTextArea gameLogArea;

    public GameInfoPanel() {
        setPreferredSize(new Dimension(200, 200));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder("게임 정보"));
        
        initializeComponents();
    }

    private void initializeComponents() {
        moveCountLabel = new JLabel("이동 횟수: 0");
        moveCountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        captureCountLabel = new JLabel("잡은 말: 0");
        captureCountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        gameLogArea = new JTextArea(5, 20);
        gameLogArea.setEditable(false);
        gameLogArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(gameLogArea);
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(Box.createVerticalStrut(10));
        add(moveCountLabel);
        add(Box.createVerticalStrut(10));
        add(captureCountLabel);
        add(Box.createVerticalStrut(10));
        add(new JLabel("게임 로그:"));
        add(Box.createVerticalStrut(5));
        add(scrollPane);
    }

    public void updateMoveCount(int count) {
        moveCountLabel.setText("이동 횟수: " + count);
    }

    public void updateCaptureCount(int count) {
        captureCountLabel.setText("잡은 말: " + count);
    }

    public void addGameLog(String log) {
        gameLogArea.append(log + "\n");
        gameLogArea.setCaretPosition(gameLogArea.getDocument().getLength());
    }
} 