package test;

import model.BoardManager;
import model.Piece;

import model.Position;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BoardManagerTest {

    @Test
    void boardInitializationTest() {
        BoardManager boardManager = new BoardManager();
        assertNotNull(boardManager);  // 생성 여부 테스트

        BoardManager boardManager4 = new BoardManager(4);
        assertNotNull(boardManager4);  // 사각형 보드 생성 여부 테스트

        BoardManager boardManager5 = new BoardManager(5);
        assertNotNull(boardManager5);  // 오각형 보드 생성 여부 테스트
        assertEquals("P25", boardManager5.getBeforeEND());  // 오각형 보드의 끝 위치 테스트

        BoardManager boardManager6 = new BoardManager(6);
        assertNotNull(boardManager6);  // 육각형 보드 생성 여부 테스트
        assertEquals("P30", boardManager6.getBeforeEND());  // 육각형 보드의 끝 위치 테스트
    }

    @Test
    void getPositionByMoveCountTest() {
        BoardManager boardManager = new BoardManager();

        // 이동 횟수에 따른 위치 테스트
        Piece DUMMY = new Piece("DUMMY", boardManager.getBeforeEND());
        assertEquals(boardManager.getBeforeEND(), boardManager.setPreviousPosition(
                new Position("START"), -1, DUMMY).getId()); // 빽도 테스트 & 시작 위치 빽도 테스트

        DUMMY = new Piece("DUMMY", boardManager.getBeforeEND());
        assertEquals("P1", boardManager.setPreviousPosition(
                new Position("START"), 1, DUMMY).getId()); // DO 테스트 & 시작 위치 DO 테스트

        DUMMY = new Piece("DUMMY", boardManager.getBeforeEND());
        assertEquals("P2", boardManager.setPreviousPosition(
                new Position("START"), 2, DUMMY).getId()); // GAE 테스트 & 시작 위치 GAE 테스트

        DUMMY = new Piece("DUMMY", boardManager.getBeforeEND());
        assertEquals("P3", boardManager.setPreviousPosition(
                new Position("START"), 3, DUMMY).getId()); // GUL 테스트 & 시작 위치 GUL 테스트

        DUMMY = new Piece("DUMMY", boardManager.getBeforeEND());
        assertEquals("P4", boardManager.setPreviousPosition(
                new Position("START"), 4, DUMMY).getId()); // YUT 테스트 & 시작 위치 YUT 테스트

        DUMMY = new Piece("DUMMY", boardManager.getBeforeEND());
        assertEquals("P5", boardManager.setPreviousPosition(
                new Position("START"), 5, DUMMY).getId()); // MO 테스트 & 시작 위치 MO 테스트
    }

    @Test
    void getPositionByMoveCountWithSpecialPositionTest() {
        BoardManager boardManager = new BoardManager();
        Piece DUMMY;

        // 특정 위치에서의 이동 횟수에 따른 위치 테스트 분기 : P5
        DUMMY = new Piece("DUMMY", "P3");
        assertEquals("P6", boardManager.setPreviousPosition(
                new Position("P4"), 2, DUMMY).getId()); // P4에서 2칸 이동
        DUMMY = new Piece("DUMMY", "P4");
        assertEquals("E1", boardManager.setPreviousPosition(
                new Position("P5"), 1, DUMMY).getId()); // P5에서 1칸 이동

        // 특정 위치에서의 이동 횟수에 따른 위치 테스트 분기 : P10
        DUMMY = new Piece("DUMMY", "P8");
        assertEquals("P11", boardManager.setPreviousPosition(
                new Position("P9"), 2, DUMMY).getId()); // P9에서 2칸 이동
        DUMMY = new Piece("DUMMY", "P9");
        assertEquals("E5", boardManager.setPreviousPosition(
                new Position("P10"), 1, DUMMY).getId()); // P10에서 1칸 이동
    }

    @Test
    void getPositionByMoveCountWithCenterTest() {
        BoardManager boardManager = new BoardManager(4);
        Piece DUMMY = new Piece("DUMMY", boardManager.getBeforeEND());

        // 특정 위치에서의 이동 횟수에 따른 위치 테스트 분기 : CENTER
        assertEquals("E7", boardManager.setPreviousPosition(
                new Position("C"), 1, DUMMY).getId()); // C에서 1칸 이동

        assertEquals("E3", boardManager.setPreviousPosition(
                new Position("E2"), 2, DUMMY).getId()); // 분기 직선 테스트

        assertEquals("E7", boardManager.setPreviousPosition(
                new Position("E6"), 2, DUMMY).getId()); // 분기 직선 테스트

        // 오각형 보드에서의 분기 테스트
        BoardManager boardManager5 = new BoardManager(5);

        assertEquals("E7", boardManager5.setPreviousPosition(
                new Position("C"), 1, DUMMY).getId()); // C에서 1칸 이동

        assertEquals("E3", boardManager5.setPreviousPosition(
                new Position("E2"), 2, DUMMY).getId()); // 분기 직선 테스트

        assertEquals("E7", boardManager5.setPreviousPosition(
                new Position("E6"), 2, DUMMY).getId()); // 분기 직선 테스트

        assertEquals("E7", boardManager5.setPreviousPosition(
                new Position("E10"), 2, DUMMY).getId()); // 분기 직선 테스트

        // 육각형 보드에서의 분기 테스트
        BoardManager boardManager6 = new BoardManager(6);

        assertEquals("E7", boardManager6.setPreviousPosition(
                new Position("C"), 1, DUMMY).getId()); // C에서 1칸 이동

        assertEquals("E3", boardManager6.setPreviousPosition(
                new Position("E2"), 2, DUMMY).getId()); // 분기 직선 테스트

        assertEquals("E7", boardManager6.setPreviousPosition(
                new Position("E6"), 2, DUMMY).getId()); // 분기 직선 테스트

        assertEquals("E9", boardManager6.setPreviousPosition(
                new Position("E12"), 2, DUMMY).getId()); // 분기 직선 테스트

    }
}
