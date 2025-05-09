package View;

import javax.swing.SwingUtilities;
import java.awt.Point;

/**
 * 윷놀이 게임 UI 테스트용 메인 클래스
 */
public class TestMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // MainFrame UI 실행
            MainFrame mainFrame = new MainFrame();
            
            // 보드판 Score 영역 아래에 말 배치 (350, 500에서 4행 4열로 배치)
            BoardPanel boardPanel = mainFrame.getBoardPanel();
            if (boardPanel != null) {
                // 시작 좌표 (500, 200)에서 4행 4열로 말 배치
                int startX = 500;
                int startY = 200;
                int pieceSize = 45; // 말 사이의 간격
                
                int pieceCount = 1;
                for (int player = 1; player <= 4; player++) {
                    for (int piece = 1; piece <= 4; piece++) {
                        // 행과 열 계산 (0부터 시작)
                        int row = (pieceCount - 1) / 4;
                        int col = (pieceCount - 1) % 4;
                        
                        // 말 위치 계산
                        int x = startX + col * pieceSize;
                        int y = startY + row * pieceSize;
                        
                        // 말 추가 (노드는 사용하지 않고 직접 위치 지정)
                        String pieceId = "player" + player + "_" + piece;
                        addPieceAt(boardPanel, pieceId, "horse.png", x, y, player);
                        
                        // 말에 클릭 이벤트 설정
                        mainFrame.addPieceClickEvent(pieceId);
                        
                        pieceCount++;
                    }
                }
            }
            
            // 콘솔에 안내 메시지 출력
            System.out.println("============= 윷놀이 게임 UI 테스트 =============");
            System.out.println("1. 말을 클릭한 후, 이동할 노드를 클릭하면 말이 이동합니다.");
            System.out.println("2. 노드를 클릭하면 콘솔에 노드 ID가 출력됩니다.");
            System.out.println("3. '게임 초기화' 버튼을 클릭하면 모든 말이 원래 위치로 돌아갑니다.");
            System.out.println("==============================================");
        });
    }
    
    // 지정된 위치에 말 추가하는 헬퍼 메서드
    private static void addPieceAt(BoardPanel boardPanel, String pieceId, String imagePath, int x, int y, int playerNumber) {
        try {
            // 말 추가 메서드 호출
            boardPanel.addPieceAtPosition(pieceId, imagePath, x, y, playerNumber);
        } catch (Exception e) {
            System.err.println("말 추가 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 