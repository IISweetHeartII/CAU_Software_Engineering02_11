package test.ViewTest;

import View.BoardPanel;
import View.GameView;
import javax.swing.*;
import java.awt.BorderLayout;
import java.util.ArrayDeque;

/**
 * 윷놀이 게임 UI 테스트용 메인 클래스
 */
public class TestMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // TestView 생성 (컨트롤러 없는 테스트용 View)
            TestView testView = new TestView();
            testView.setVisible(true);
            
            // 테스트를 위한 윷 결과 표시
            TestPosition testPos1 = new TestPosition("테스트 위치 1");
            TestPosition testPos2 = new TestPosition("테스트 위치 2");
            TestPosition testPos3 = new TestPosition("테스트 위치 3");
            
            ArrayDeque<TestPosition> testMoves = new ArrayDeque<>();
            testMoves.add(testPos1);
            testMoves.add(testPos2);
            testMoves.add(testPos3);
            
            // 테스트 데이터로 UI 업데이트
            testView.showYutResult(new TestYutResult("도"));
            testView.updateCurrentPlayer("플레이어 1");
            testView.showTestPosableMoves(testMoves);
            
            // 턴 이미지 테스트를 위한 타이머 설정
            final int[] currentPlayer = {1}; // 배열로 변경하여 익명 클래스에서 수정 가능하게 함
            Timer turnChangeTimer = new Timer(2000, e -> {
                // 2초마다 현재 플레이어 변경
                currentPlayer[0] = (currentPlayer[0] % 4) + 1;
                testView.updateCurrentPlayer("player" + currentPlayer[0]);
            });
            turnChangeTimer.start();
        });
    }
    
    // 테스트용 View 클래스 (GameView 구현)
    static class TestView extends JFrame {
        private BoardPanel boardPanel;
        private int currentPlayer = 1;
        
        public TestView() {
            setTitle("윷놀이 게임 테스트");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setResizable(false);
            setLayout(new BorderLayout());
            
            // 보드 패널 생성
            boardPanel = new BoardPanel("board/board_four.png");
            add(boardPanel, BorderLayout.CENTER);
            
            // 게임 종료 리스너 설정
            boardPanel.setGameQuitListener(() -> {
                System.exit(0);
            });
            
            // 게임 재시작 리스너 설정
            boardPanel.setGameRestartListener(() -> {
                boardPanel.restartGame();
            });
            
            // 커스텀 선택 리스너 설정
            boardPanel.setCustomChoiceListener(new BoardPanel.CustomChoiceListener() {
                @Override
                public void onCustomChoice() {
                    JOptionPane.showMessageDialog(TestView.this, "커스텀 선택 기능 테스트");
                }
                
                @Override
                public void onCustomYutSelected(int selection) {
                    JOptionPane.showMessageDialog(TestView.this, "선택된 윷: " + selection);
                }
            });
            
            // 윷 던지기 리스너 설정
            boardPanel.setYutThrowListener(() -> {
                JOptionPane.showMessageDialog(TestView.this, "윷 던지기 기능 테스트");
                return (int)(Math.random() * 6); // 0~5 사이의 랜덤 값 반환
            });
            
            // 플레이어 턴 변경 버튼 추가
            JButton changeTurnButton = new JButton("다음 플레이어 턴");
            changeTurnButton.addActionListener(e -> {
                currentPlayer = (currentPlayer % 4) + 1;
                boardPanel.updateTurnImage(currentPlayer);
                setTitle("윷놀이 게임 테스트 - 현재 차례: 플레이어 " + currentPlayer);
            });
            
            // 플레이어 점수 변경 버튼 추가
            JButton decreasePlayer1Button = new JButton("플레이어1 말 감소");
            decreasePlayer1Button.addActionListener(e -> {
                boardPanel.decreasePlayerPieceCount(1);
            });
            
            JButton decreasePlayer2Button = new JButton("플레이어2 말 감소");
            decreasePlayer2Button.addActionListener(e -> {
                boardPanel.decreasePlayerPieceCount(2);
            });
            
            JButton resetScoresButton = new JButton("점수 초기화");
            resetScoresButton.addActionListener(e -> {
                boardPanel.resetPlayerScores();
            });
            
            JPanel buttonPanel = new JPanel();
            buttonPanel.add(changeTurnButton);
            buttonPanel.add(decreasePlayer1Button);
            buttonPanel.add(decreasePlayer2Button);
            buttonPanel.add(resetScoresButton);
            
            add(buttonPanel, BorderLayout.SOUTH);
            
            pack();
            setLocationRelativeTo(null);
        }

        public void showYutResult(TestYutResult yutResult) {
            JOptionPane.showMessageDialog(this, "윷 결과: " + yutResult.toString());
        }

        public void updateCurrentPlayer(String playerID) {
            setTitle("윷놀이 게임 테스트 - 현재 차례: " + playerID);
            
            // 플레이어 ID에서 숫자 추출하여 턴 이미지 업데이트
            if (playerID != null && playerID.startsWith("player")) {
                try {
                    int playerNum = Integer.parseInt(playerID.substring(6));
                    boardPanel.updateTurnImage(playerNum);
                } catch (NumberFormatException e) {
                    // 숫자 파싱 실패시 기본값 사용
                    boardPanel.updateTurnImage(1);
                }
            }
        }

        public void showTestPosableMoves(ArrayDeque<TestPosition> posableMoves) {
            StringBuilder sb = new StringBuilder("이동 가능한 위치:\n");
            for (TestPosition pos : posableMoves) {
                sb.append("- ").append(pos.toString()).append("\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString());
        }

        public TestPosition getUserSelectedPosition(ArrayDeque<TestPosition> posableMoves) {
            // 간단히 첫 번째 위치 선택
            return posableMoves.isEmpty() ? null : posableMoves.getFirst();
        }

        public void BoardRendering() {
            repaint();
        }

        public void showGameEnd(String playerID) {
            JOptionPane.showMessageDialog(this, playerID + "가 게임에서 승리했습니다!");
            // 승리 이미지 표시
            if (playerID != null && playerID.startsWith("player")) {
                try {
                    int playerNum = Integer.parseInt(playerID.substring(6));
                    boardPanel.showWinnerImage(playerNum);
                } catch (NumberFormatException e) {
                    // 기본값 사용
                    boardPanel.showWinnerImage(1);
                }
            }
        }
    }
    
    // 테스트용 Position 클래스
    static class TestPosition {
        private String name;
        
        public TestPosition(String name) {
            this.name = name;
        }
        
        @Override
        public String toString() {
            return name;
        }
    }
    
    // 테스트용 YutResult 클래스
    static class TestYutResult {
        private String name;
        
        public TestYutResult(String name) {
            this.name = name;
        }
        
        @Override
        public String toString() {
            return name;
        }
    }
} 