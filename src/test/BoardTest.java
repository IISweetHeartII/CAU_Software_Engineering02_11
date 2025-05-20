package test;

import model.Position;
import model.Board;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    @Test
    void boardInitializationTest() {
        Board board = new Board();
        assertNotNull(board);  // 생성 여부 테스트
    }

    @Test
    void getPositionByMoveCountTest() {
    }
}
