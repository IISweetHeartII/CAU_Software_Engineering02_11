package com.yut.game.view;

import javax.swing.*;
import java.awt.*;

/**
 * 플레이어 정보를 표시하는 패널 클래스입니다.
 */
public class PlayerPanel extends JPanel {
    private JLabel player1Label;
    private JLabel player2Label;
    private JLabel currentPlayerLabel;
    private static final Color ACTIVE_COLOR = new Color(144, 238, 144);
    private static final Color INACTIVE_COLOR = new Color(211, 211, 211);

    public PlayerPanel() {
        setPreferredSize(new Dimension(200, 150));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder("플레이어 정보"));
        
        initializeComponents();
    }

    private void initializeComponents() {
        player1Label = createPlayerLabel("플레이어 1 (빨강)");
        player2Label = createPlayerLabel("플레이어 2 (파랑)");
        currentPlayerLabel = new JLabel("현재 차례: 플레이어 1");
        currentPlayerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(Box.createVerticalStrut(10));
        add(player1Label);
        add(Box.createVerticalStrut(10));
        add(player2Label);
        add(Box.createVerticalStrut(20));
        add(currentPlayerLabel);
        add(Box.createVerticalStrut(10));

        updateCurrentPlayer(0); // 초기 상태: 플레이어 1
    }

    private JLabel createPlayerLabel(String text) {
        JLabel label = new JLabel(text);
        label.setOpaque(true);
        label.setBackground(INACTIVE_COLOR);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return label;
    }

    public void updateCurrentPlayer(int playerIndex) {
        player1Label.setBackground(playerIndex == 0 ? ACTIVE_COLOR : INACTIVE_COLOR);
        player2Label.setBackground(playerIndex == 1 ? ACTIVE_COLOR : INACTIVE_COLOR);
        currentPlayerLabel.setText("현재 차례: 플레이어 " + (playerIndex + 1));
    }
} 