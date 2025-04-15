import view.YutGameFrame;

/**
 * 윷놀이 게임의 메인 클래스입니다.
 */
public class Main {
    public static void main(String[] args) {
        // Swing UI는 Event Dispatch Thread에서 실행
        javax.swing.SwingUtilities.invokeLater(() -> {
            YutGameFrame frame = new YutGameFrame();
            frame.startGame();
        });
    }
} 