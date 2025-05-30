package test;

import model.Piece;
import model.Position;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class PieceTest {
    @Test
    void pieceInitializationTest() {
        Piece piece = new Piece("111", "P1");
        assertNotNull(piece);  // 생성 여부 테스트
        assertEquals("111", piece.getId());  // ID 테스트
        assertEquals(3, piece.getSize());  // 크기 테스트
        assertEquals("1", piece.getPlayerId());  // 플레이어 ID 테스트
        assertEquals(new Position("P1"), piece.getPreviousPosition());  // 이전 위치 테스트
    }

    @Test
    void pieceGroupingTest() {
        Piece piece1 = new Piece("111", "P2");
        Piece piece2 = new Piece("11", "P1");

        piece1.group(piece2);
        assertEquals("11111", piece1.getId());  // 그룹화 후 ID 테스트
        assertEquals(5, piece1.getSize());  // 그룹화 후 크기 테스트
        assertEquals(new Position("P2"), piece1.getPreviousPosition());  // 이전 위치 테스트
    }
}