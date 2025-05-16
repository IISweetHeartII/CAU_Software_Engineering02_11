package test;

import model.Piece;
import model.Position;

import java.util.Deque;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PieceTest {

    @Test
    void pieceInitializationTest() {
        Piece piece = new Piece("Player1", "Piece1");
        assertNotNull(piece);  // 생성 여부 테스트
        assertEquals("Player1", piece.getPlayerID());  // 플레이어 ID 확인
        assertEquals("Piece1", piece.getPieceID());  // 말 ID 확인
        assertEquals(new Position("START"), piece.getCurrentPosition());  // 초기 위치 확인
    }

    @Test
    void pieceMoveForwardTest() {
        Piece piece = new Piece("Player1", "Piece1", new Position("P2"));
        piece.moveForward(new Position("P4"));
        assertEquals(new Position("P4"), piece.getCurrentPosition());
        assertEquals(new Position("P4"), piece.getPath().peekLast());
        Deque<Position> path = piece.getPath();
        path.removeLast();
        assertEquals(new Position("P3"), path.peekLast());
        assertEquals(new Position("P2"), piece.getPath().peekFirst());

/*
        piece.moveForward(new Position("E6")); // E4 -> E5 -> E6
        assertEquals(new Position("E6"), piece.getCurrentPosition());

        piece.moveForward(new Position("C")); // E6 -> C (이동 불가)
        assertEquals(new Position("E6"), piece.getCurrentPosition()); // 여전히 E6에 있어야 함
*/
    }

    @Test
    void pieceMoveBackwardTest() {
        Piece piece = new Piece("Player1", "Piece1", new Position("P2"));
        piece.moveForward(new Position("P4"));
        assertEquals(new Position("P4"), piece.getCurrentPosition());

        piece.moveBackward();
        assertEquals(new Position("P3"), piece.getCurrentPosition());

        piece.moveBackward(); // E3 -> E2
        assertEquals(new Position("P2"), piece.getCurrentPosition());

/*
        piece.moveBackward(); // E2 -> START (이동 불가)
        assertEquals(new Position("E2"), piece.getCurrentPosition()); // 여전히 E2에 있어야 함
*/
    }
}
