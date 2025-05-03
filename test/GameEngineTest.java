import org.junit.Test;
import static org.junit.Assert.*;

/**
 * GameEngine 클래스의 테스트를 위한 클래스입니다.
 */
public class GameEngineTest {
    
    @Test
    public void testGameInitialization() {
        GameEngine game = new GameEngine();
        assertFalse("게임 초기 상태는 실행 중이 아니어야 합니다.", game.isGameRunning());
    }

    @Test
    public void testGameStart() {
        GameEngine game = new GameEngine();
        game.start();
        assertTrue("게임 시작 후에는 실행 중이어야 합니다.", game.isGameRunning());
    }

    @Test
    public void testGameStop() {
        GameEngine game = new GameEngine();
        game.start();
        game.stop();
        assertFalse("게임 종료 후에는 실행 중이 아니어야 합니다.", game.isGameRunning());
    }
} 