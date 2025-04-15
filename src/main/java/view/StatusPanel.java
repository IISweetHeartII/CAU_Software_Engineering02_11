package view;

import javax.swing.*;
import java.awt.*;
import model.*;

/**
 * 게임 상태를 표시하는 패널입니다.
 */
public class StatusPanel extends JPanel {
    private static final int WIDTH = 200;
    private static final int HEIGHT = 400;
    
    private final JLabel currentPlayerLabel;
    private final JLabel resultLabel;
    
    /**
     * 상태 패널의 생성자입니다.
     */
    public StatusPanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEtchedBorder());
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        // 현재 플레이어 레이블
        currentPlayerLabel = new JLabel("현재 플레이어: ");
        currentPlayerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(Box.createVerticalStrut(20));
        add(currentPlayerLabel);
        
        // 결과 레이블
        resultLabel = new JLabel("결과: ");
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(Box.createVerticalStrut(20));
        add(resultLabel);
    }
    
    /**
     * 현재 플레이어를 업데이트합니다.
     * 
     * @param player 현재 플레이어
     */
    public void updateCurrentPlayer(Player player) {
        currentPlayerLabel.setText("현재 플레이어: " + player.getName());
    }
    
    /**
     * 윷가락 던지기 결과를 업데이트합니다.
     * 
     * @param result 윷가락 던지기 결과
     */
    public void updateResult(YutResult result) {
        resultLabel.setText("결과: " + result.getName());
    }
} 