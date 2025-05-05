package test;

import Model.Position;
import Model.Board;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    @Test
    void boardInitializationTest() {
        Board board = new Board();
        assertNotNull(board);  // 생성 여부 테스트
    }

    @Test
    void getNNextPositionTest() {
        Board board = new Board();
        Position startPosition = new Position("P1");
        Position expectedPosition = new Position("P2");

        // n == 1
        Position resultPosition = board.getNNextPosition(startPosition, 1);
        assertNotNull(resultPosition);  // null이 아닌지 확인
        assertEquals(expectedPosition, resultPosition);  // 예상 위치와 일치하는지 확인

        // n == 2
        expectedPosition = new Position("P3");
        resultPosition = board.getNNextPosition(startPosition, 2);
        assertNotNull(resultPosition);  // null이 아닌지 확인
        assertEquals(expectedPosition, resultPosition);  // 예상 위치와 일치하는지 확인

        // 분기점 테스트
        startPosition = new Position("P4");
        expectedPosition = new Position("P6");
        resultPosition = board.getNNextPosition(startPosition, 2);
        assertNotNull(resultPosition);  // null이 아닌지 확인
        assertEquals(expectedPosition, resultPosition);  // 예상 위치와 일치하는지 확인
        expectedPosition = new Position("E1");
        resultPosition = board.getNNextPosition(startPosition, 1);
        resultPosition = board.getNNextPosition(resultPosition, 1);
        assertNotNull(resultPosition);  // null이 아닌지 확인
        assertEquals(expectedPosition, resultPosition);  // 예상 위치와 일치하는지 확인

        /// center 분기 테스트 ///
        // C -> E7
        startPosition = new Position("C");
        expectedPosition = new Position("E7");
        resultPosition = board.getNNextPosition(startPosition, 1);
        assertNotNull(resultPosition);  // null이 아닌지 확인
        assertEquals(expectedPosition, resultPosition);  // 예상 위치와 일치하는지 확인


        // E2 -> C -> E7
        startPosition = new Position("E2");
        expectedPosition = new Position("E7");
        resultPosition = board.getNNextPosition(startPosition, 1);
        resultPosition = board.getNNextPosition(resultPosition, 1);
        assertNotNull(resultPosition);  // null이 아닌지 확인
        assertEquals(expectedPosition, resultPosition);  // 예상 위치와 일치하는지 확인

        // E2 ->-> E3
        startPosition = new Position("E2");
        expectedPosition = new Position("E3");
        resultPosition = board.getNNextPosition(startPosition, 2);
        assertNotNull(resultPosition);  // null이 아닌지 확인
        assertEquals(expectedPosition, resultPosition);  // 예상 위치와 일치하는지 확인

        // E6 ->-> E7
        startPosition = new Position("E6");
        expectedPosition = new Position("E7");
        resultPosition = board.getNNextPosition(startPosition, 2);
        assertNotNull(resultPosition);  // null이 아닌지 확인
        assertEquals(expectedPosition, resultPosition);  // 예상 위치와 일치하는지 확인
    }
}
