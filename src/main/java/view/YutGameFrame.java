package view;

import javax.swing.*;
import java.awt.*;
import model.*;
import java.util.List;

/**
 * 윷놀이 게임의 메인 프레임입니다.
 */
public class YutGameFrame extends JFrame {
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    
    private final YutBoardPanel boardPanel;
    private final YutStickPanel stickPanel;
    private final StatusPanel statusPanel;
    private final JButton throwButton;
    
    private final YutGameEngine gameEngine;
    
    /**
     * 게임 프레임의 생성자입니다.
     */
    public YutGameFrame() {
        setTitle("윷놀이");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(null);
        
        // 게임 엔진 초기화
        gameEngine = new YutGameEngine();
        
        // 패널 생성
        boardPanel = new YutBoardPanel();
        stickPanel = new YutStickPanel();
        statusPanel = new StatusPanel();
        
        // 버튼 생성
        throwButton = new JButton("윷가락 던지기");
        
        // 레이아웃 설정
        setLayout(new BorderLayout());
        
        // 상단 패널 (보드)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(boardPanel, BorderLayout.CENTER);
        
        // 하단 패널 (윷가락과 버튼)
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(stickPanel, BorderLayout.CENTER);
        bottomPanel.add(throwButton, BorderLayout.SOUTH);
        
        // 메인 프레임에 패널 추가
        add(topPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        add(statusPanel, BorderLayout.EAST);
        
        // 이벤트 리스너 설정
        throwButton.addActionListener(e -> throwYutSticks());
    }
    
    /**
     * 윷가락을 던지는 메소드입니다.
     */
    private void throwYutSticks() {
        YutResult result = gameEngine.throwSticks();
        stickPanel.setYutStick(gameEngine.getYutStick());
        statusPanel.updateResult(result);
        statusPanel.updateCurrentPlayer(gameEngine.getCurrentPlayer());
        boardPanel.repaint();
    }
    
    /**
     * 게임을 시작합니다.
     */
    public void startGame() {
        // 플레이어 설정 대화상자 표시
        PlayerSetupDialog dialog = new PlayerSetupDialog(this);
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            List<String> playerNames = dialog.getPlayerNames();
            List<Integer> horseCounts = dialog.getHorseCounts();
            
            // 게임 엔진에 플레이어 추가
            Color[] playerColors = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW};
            for (int i = 0; i < playerNames.size(); i++) {
                gameEngine.addPlayer(playerNames.get(i), playerColors[i], horseCounts.get(i));
            }
            
            // 보드 설정
            boardPanel.setBoard(gameEngine.getBoard());
            statusPanel.updateCurrentPlayer(gameEngine.getCurrentPlayer());
            
            // 프레임 표시
            setVisible(true);
        } else {
            System.exit(0);
        }
    }
} 