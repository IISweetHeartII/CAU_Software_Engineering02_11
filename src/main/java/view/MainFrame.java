package view;

import javax.swing.*;
import java.awt.*;

/**
 * 윷놀이 게임의 메인 프레임입니다.
 */
public class MainFrame extends JFrame {
    private final YutBoardPanel boardPanel;
    private final YutStickPanel stickPanel;
    private final StatusPanel statusPanel;
    
    /**
     * 메인 프레임의 생성자입니다.
     */
    public MainFrame() {
        setTitle("윷놀이");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // 패널 생성
        boardPanel = new YutBoardPanel();
        stickPanel = new YutStickPanel();
        statusPanel = new StatusPanel();
        
        // 패널 배치
        add(boardPanel, BorderLayout.CENTER);
        add(stickPanel, BorderLayout.SOUTH);
        add(statusPanel, BorderLayout.EAST);
        
        // 프레임 크기 설정
        pack();
        setLocationRelativeTo(null);
    }
    
    /**
     * 게임 보드 패널을 반환합니다.
     * 
     * @return 게임 보드 패널
     */
    public YutBoardPanel getBoardPanel() {
        return boardPanel;
    }
    
    /**
     * 윷가락 패널을 반환합니다.
     * 
     * @return 윷가락 패널
     */
    public YutStickPanel getStickPanel() {
        return stickPanel;
    }
    
    /**
     * 상태 패널을 반환합니다.
     * 
     * @return 상태 패널
     */
    public StatusPanel getStatusPanel() {
        return statusPanel;
    }
} 