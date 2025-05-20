package test;

import model.BoardManager;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BoardManagerTest {

    @Test
    void boardInitializationTest() {
        BoardManager boardManager = new BoardManager();
        assertNotNull(boardManager);  // 생성 여부 테스트
    }

    @Test
    void getPositionByMoveCountTest() {
    }
}
