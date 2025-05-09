package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BoardPanelTest {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("BoardPanel 테스트");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 800);
            
            // 경로 문제 해결 - "board/board_four.png"에서 "board_four.png"로 변경
            BoardPanel boardPanel = new BoardPanel("board_four.png");
            
            // 리스너 설정
            boardPanel.setGameQuitListener(() -> {
                System.out.println("게임 종료 버튼 클릭됨");
                int option = JOptionPane.showConfirmDialog(frame, 
                    "정말 게임을 종료하시겠습니까?", 
                    "게임 종료", 
                    JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            });
            
            boardPanel.setGameRestartListener(() -> {
                System.out.println("게임 재시작 버튼 클릭됨");
                JOptionPane.showMessageDialog(frame, "게임을 재시작합니다.");
                boardPanel.restartGame();
            });
            
            boardPanel.setCustomChoiceListener(new BoardPanel.CustomChoiceListener() {
                @Override
                public void onCustomChoice() {
                    System.out.println("커스텀 선택 버튼 클릭됨");
                }

                @Override
                public void onCustomYutSelected(int selection) {
                    String[] yutNames = {"도", "개", "걸", "윷", "모", "빽도"};
                    String selectedYut = (selection >= 1 && selection <= 6) ? yutNames[selection-1] : "알 수 없음";
                    System.out.println("사용자가 직접 선택한 윷: " + selectedYut);
                    
                    // 선택된 윷 값에 따라 다른 액션 수행
                    String message = "";
                    switch (selection) {
                        case 1: // 도
                            message = "도: 말을 1칸 이동합니다.";
                            break;
                        case 2: // 개
                            message = "개: 말을 2칸 이동합니다.";
                            break;
                        case 3: // 걸
                            message = "걸: 말을 3칸 이동합니다.";
                            break;
                        case 4: // 윷
                            message = "윷: 말을 4칸 이동하고 한 번 더 던집니다!";
                            break;
                        case 5: // 모
                            message = "모: 말을 5칸 이동하고 한 번 더 던집니다!";
                            break;
                        case 6: // 빽
                            message = "빽: 말을 1칸 뒤로 이동합니다.";
                            break;
                        default:
                            message = "알 수 없는 선택입니다.";
                    }
                    
                    JOptionPane.showMessageDialog(frame, "선택한 윷 결과: " + selectedYut + "\n" + message);
                    
                    // 현재 저장된 선택 값 확인
                    System.out.println("현재 저장된 선택 값: " + boardPanel.getSelectedCustomYut());
                    System.out.println("현재 저장된 선택 이름: " + boardPanel.getSelectedCustomYutName());
                }
            });
            
            // 윷던지기 리스너 설정
            boardPanel.setYutThrowListener(() -> {
                System.out.println("윷 던지기 버튼 클릭됨");
                String[] yutResults = {"도", "개", "걸", "윷", "모", "빽도"};
                // 랜덤하게 윷 결과 선택
                int randomIndex = (int)(Math.random() * 6); // 0(도)부터 5(빽도)까지
                String result = yutResults[randomIndex];
                JOptionPane.showMessageDialog(frame, "윷 결과: " + result);
                return randomIndex; // 윷 결과 값 반환
            });
            
            // 플레이어 점수 변경 버튼 추가
            JPanel controlPanel = new JPanel();
            
            // 플레이어 1 턴 표시 버튼
            JButton player1TurnButton = new JButton("플레이어 1 턴");
            player1TurnButton.addActionListener(e -> {
                boardPanel.updateTurnImage(1);
            });
            controlPanel.add(player1TurnButton);
            
            // 플레이어 2 턴 표시 버튼
            JButton player2TurnButton = new JButton("플레이어 2 턴");
            player2TurnButton.addActionListener(e -> {
                boardPanel.updateTurnImage(2);
            });
            controlPanel.add(player2TurnButton);
            
            // 플레이어 3 턴 표시 버튼
            JButton player3TurnButton = new JButton("플레이어 3 턴");
            player3TurnButton.addActionListener(e -> {
                boardPanel.updateTurnImage(3);
            });
            controlPanel.add(player3TurnButton);
            
            // 플레이어 4 턴 표시 버튼
            JButton player4TurnButton = new JButton("플레이어 4 턴");
            player4TurnButton.addActionListener(e -> {
                boardPanel.updateTurnImage(4);
            });
            controlPanel.add(player4TurnButton);
            
            // 플레이어 1 점수 감소 버튼
            JButton player1ScoreButton = new JButton("플레이어 1 말 감소");
            player1ScoreButton.addActionListener(e -> {
                boardPanel.decreasePlayerPieceCount(1);
            });
            controlPanel.add(player1ScoreButton);
            
            // 플레이어 2 점수 감소 버튼
            JButton player2ScoreButton = new JButton("플레이어 2 말 감소");
            player2ScoreButton.addActionListener(e -> {
                boardPanel.decreasePlayerPieceCount(2);
            });
            controlPanel.add(player2ScoreButton);
            
            // 플레이어 3 점수 감소 버튼
            JButton player3ScoreButton = new JButton("플레이어 3 말 감소");
            player3ScoreButton.addActionListener(e -> {
                boardPanel.decreasePlayerPieceCount(3);
            });
            controlPanel.add(player3ScoreButton);
            
            // 플레이어 4 점수 감소 버튼
            JButton player4ScoreButton = new JButton("플레이어 4 말 감소");
            player4ScoreButton.addActionListener(e -> {
                boardPanel.decreasePlayerPieceCount(4);
            });
            controlPanel.add(player4ScoreButton);
            
            // 점수 초기화 버튼
            JButton resetScoreButton = new JButton("점수 초기화");
            resetScoreButton.addActionListener(e -> {
                boardPanel.resetPlayerScores();
            });
            controlPanel.add(resetScoreButton);
            
            // 승리 이미지 표시 버튼들
            JPanel winnerPanel = new JPanel();
            winnerPanel.setLayout(new FlowLayout());
            
            JButton winner1Button = new JButton("Player 1 승리");
            winner1Button.addActionListener(e -> {
                boardPanel.showWinnerImage(1);
            });
            winnerPanel.add(winner1Button);
            
            JButton winner2Button = new JButton("Player 2 승리");
            winner2Button.addActionListener(e -> {
                boardPanel.showWinnerImage(2);
            });
            winnerPanel.add(winner2Button);
            
            JButton winner3Button = new JButton("Player 3 승리");
            winner3Button.addActionListener(e -> {
                boardPanel.showWinnerImage(3);
            });
            winnerPanel.add(winner3Button);
            
            JButton winner4Button = new JButton("Player 4 승리");
            winner4Button.addActionListener(e -> {
                boardPanel.showWinnerImage(4);
            });
            winnerPanel.add(winner4Button);
            
            frame.add(boardPanel, BorderLayout.CENTER);
            frame.add(controlPanel, BorderLayout.SOUTH);
            frame.add(winnerPanel, BorderLayout.NORTH);
            
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
} 