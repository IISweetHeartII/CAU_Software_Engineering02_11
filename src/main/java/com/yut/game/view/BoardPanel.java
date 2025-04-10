package com.yut.game.view;

import com.yut.game.model.Board;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * 윷놀이 게임판을 그리는 패널 클래스입니다.
 */
public class BoardPanel extends JPanel {
    private static final int CELL_SIZE = 60;
    private static final int PIECE_SIZE = 20;
    private Board board;
    private Color[] playerColors = {Color.RED, Color.BLUE};

    public BoardPanel() {
        setPreferredSize(new Dimension(600, 600));
        setBackground(Color.WHITE);
        addMouseListener(new BoardClickListener());
    }

    public void setBoard(Board board) {
        this.board = board;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                            RenderingHints.VALUE_ANTIALIAS_ON);

        drawBoard(g2d);
        if (board != null) {
            drawPieces(g2d);
        }
    }

    private void drawBoard(Graphics2D g2d) {
        // 기본 경로 그리기
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));

        // 외곽 사각형
        int margin = 50;
        int boardSize = 500;
        g2d.drawRect(margin, margin, boardSize, boardSize);

        // 대각선
        g2d.drawLine(margin, margin, margin + boardSize, margin + boardSize);
        g2d.drawLine(margin + boardSize, margin, margin, margin + boardSize);

        // 점 그리기
        int numPoints = 5;
        int spacing = boardSize / (numPoints - 1);
        
        for (int i = 0; i < numPoints; i++) {
            for (int j = 0; j < numPoints; j++) {
                int x = margin + i * spacing;
                int y = margin + j * spacing;
                g2d.fillOval(x - 5, y - 5, 10, 10);
            }
        }
    }

    private void drawPieces(Graphics2D g2d) {
        int[][] boardState = board.getBoardState();
        // TODO: 말 그리기 로직 구현
    }

    private class BoardClickListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            // TODO: 클릭 위치에 따른 말 선택/이동 로직 구현
        }
    }
} 