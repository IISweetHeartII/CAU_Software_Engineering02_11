package com.yut.game.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * 윷 던지기 UI를 관리하는 패널 클래스입니다.
 */
public class YutPanel extends JPanel {
    private JButton throwButton;
    private JLabel resultLabel;
    private static final String[] YUT_RESULTS = {"도", "개", "걸", "윷", "모"};

    public YutPanel() {
        setPreferredSize(new Dimension(200, 150));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder("윷 던지기"));
        
        initializeComponents();
    }

    private void initializeComponents() {
        throwButton = new JButton("윷 던지기");
        throwButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        throwButton.addActionListener(e -> {
            // TODO: 윷 던지기 이벤트 처리
        });

        resultLabel = new JLabel("결과: ");
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        resultLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));

        add(Box.createVerticalStrut(10));
        add(throwButton);
        add(Box.createVerticalStrut(20));
        add(resultLabel);
        add(Box.createVerticalStrut(10));
    }

    public void displayResult(int result) {
        if (result >= 0 && result < YUT_RESULTS.length) {
            resultLabel.setText("결과: " + YUT_RESULTS[result]);
        }
    }

    public void setThrowButtonEnabled(boolean enabled) {
        throwButton.setEnabled(enabled);
    }

    public void addThrowListener(ActionListener listener) {
        throwButton.addActionListener(listener);
    }
} 