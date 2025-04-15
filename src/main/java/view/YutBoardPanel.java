package view;

import javax.swing.*;
import java.awt.*;
import model.*;

/**
 * 윷놀이 게임의 보드 패널입니다.
 */
public class YutBoardPanel extends JPanel {
    private static final int CELL_SIZE = 60;
    private static final int BOARD_SIZE = 5;
    private static final int MARGIN = 20;
    private static final int HORSE_SIZE = 20;
    
    // 경로 좌표 (시계방향)
    private static final Point[] PATH = {
        new Point(0, 4), // 시작점
        new Point(0, 3),
        new Point(0, 2),
        new Point(0, 1),
        new Point(0, 0), // 왼쪽 위 모서리
        new Point(1, 0),
        new Point(2, 0),
        new Point(3, 0),
        new Point(4, 0), // 오른쪽 위 모서리
        new Point(4, 1),
        new Point(4, 2),
        new Point(4, 3),
        new Point(4, 4), // 오른쪽 아래 모서리
        new Point(3, 4),
        new Point(2, 4),
        new Point(1, 4),
        // 대각선 경로
        new Point(1, 1),
        new Point(2, 2), // 중앙
        new Point(3, 3)
    };
    
    private Board board;
    
    /**
     * 보드 패널의 생성자입니다.
     */
    public YutBoardPanel() {
        setPreferredSize(new Dimension(
            BOARD_SIZE * CELL_SIZE + MARGIN * 2,
            BOARD_SIZE * CELL_SIZE + MARGIN * 2
        ));
        setBackground(Color.WHITE);
    }
    
    /**
     * 게임 보드를 설정합니다.
     * 
     * @param board 게임 보드
     */
    public void setBoard(Board board) {
        this.board = board;
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        if (board == null) return;
        
        // 보드 그리기
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        
        // 외곽선
        for (int i = 0; i < PATH.length - 1; i++) {
            Point p1 = PATH[i];
            Point p2 = PATH[(i + 1) % PATH.length];
            g2d.drawLine(
                MARGIN + p1.x * CELL_SIZE + CELL_SIZE/2,
                MARGIN + p1.y * CELL_SIZE + CELL_SIZE/2,
                MARGIN + p2.x * CELL_SIZE + CELL_SIZE/2,
                MARGIN + p2.y * CELL_SIZE + CELL_SIZE/2
            );
        }
        
        // 대각선
        g2d.drawLine(
            MARGIN + CELL_SIZE/2,
            MARGIN + CELL_SIZE/2,
            MARGIN + CELL_SIZE * 4 + CELL_SIZE/2,
            MARGIN + CELL_SIZE * 4 + CELL_SIZE/2
        );
        g2d.drawLine(
            MARGIN + CELL_SIZE/2,
            MARGIN + CELL_SIZE * 4 + CELL_SIZE/2,
            MARGIN + CELL_SIZE * 4 + CELL_SIZE/2,
            MARGIN + CELL_SIZE/2
        );
        
        // 특별한 지점 표시 (모서리와 중앙)
        int[] specialPoints = {0, 4, 8, 12, 17}; // PATH 배열의 인덱스
        for (int i : specialPoints) {
            Point p = PATH[i];
            g2d.fillOval(
                MARGIN + p.x * CELL_SIZE + CELL_SIZE/2 - 10,
                MARGIN + p.y * CELL_SIZE + CELL_SIZE/2 - 10,
                20,
                20
            );
        }
        
        // 말 그리기
        for (Horse horse : board.getHorses()) {
            if (horse.getPosition() < PATH.length) {
                Point p = PATH[horse.getPosition()];
                g2d.setColor(horse.getColor());
                g2d.fillOval(
                    MARGIN + p.x * CELL_SIZE + CELL_SIZE/2 - HORSE_SIZE/2,
                    MARGIN + p.y * CELL_SIZE + CELL_SIZE/2 - HORSE_SIZE/2,
                    HORSE_SIZE,
                    HORSE_SIZE
                );
            }
        }
    }
} 