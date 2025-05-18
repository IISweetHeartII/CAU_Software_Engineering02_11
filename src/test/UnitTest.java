package test;

import model.Unit;
import model.Position;

import java.util.Deque;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UnitTest {

    @Test
    void pieceInitializationTest() {
        Unit unit = new Unit("Player1", "Piece1");
        assertNotNull(unit);  // 생성 여부 테스트
        assertEquals("Player1", unit.getPlayerID());  // 플레이어 ID 확인
        assertEquals("Piece1", unit.getPieceID());  // 말 ID 확인
        assertEquals(new Position("START"), unit.getCurrentPosition());  // 초기 위치 확인
    }

    @Test
    void pieceMoveForwardTest() {
        Unit unit = new Unit("Player1", "Piece1", new Position("P2"));
        unit.moveForward(new Position("P4"));
        assertEquals(new Position("P4"), unit.getCurrentPosition());
        assertEquals(new Position("P4"), unit.getPath().peekLast());
        Deque<Position> path = unit.getPath();
        path.removeLast();
        assertEquals(new Position("P3"), path.peekLast());
        assertEquals(new Position("P2"), unit.getPath().peekFirst());

/*
        piece.moveForward(new Position("E6")); // E4 -> E5 -> E6
        assertEquals(new Position("E6"), piece.getCurrentPosition());

        piece.moveForward(new Position("C")); // E6 -> C (이동 불가)
        assertEquals(new Position("E6"), piece.getCurrentPosition()); // 여전히 E6에 있어야 함
*/
    }

    @Test
    void pieceMoveBackwardTest() {
        Unit unit = new Unit("Player1", "Piece1", new Position("P2"));
        unit.moveForward(new Position("P4"));
        assertEquals(new Position("P4"), unit.getCurrentPosition());

        unit.moveBackward();
        assertEquals(new Position("P3"), unit.getCurrentPosition());

        unit.moveBackward(); // E3 -> E2
        assertEquals(new Position("P2"), unit.getCurrentPosition());

/*
        piece.moveBackward(); // E2 -> START (이동 불가)
        assertEquals(new Position("E2"), piece.getCurrentPosition()); // 여전히 E2에 있어야 함
*/
    }
}
