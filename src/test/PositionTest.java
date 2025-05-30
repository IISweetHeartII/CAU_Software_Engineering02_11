package test;

import model.Position;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PositionTest {
    @Test
    void positionInitializationTest() {
        Position position = new Position("P1");
        assertNotNull(position);  // 생성 여부 테스트
        assertEquals("P1", position.getId());  // ID 테스트
    }

    @Test
    void positionEqualityTest() {
        Position position1 = new Position("P1");
        Position position2 = new Position("P1");
        Position position3 = new Position("P2");

        assertEquals(position1, position2);  // 동일 ID 비교
        assertNotEquals(position1, position3);  // 다른 ID 비교
        assertTrue(position1.equals("P1"));  // 문자열 ID 비교
        assertFalse(position1.equals("P2"));  // 다른 문자열 ID 비교
    }

    @Test
    void positionHashCodeTest() {
        Position position = new Position("P1");
        assertEquals("P1".hashCode(), position.hashCode());  // 해시 코드 테스트
    }

    @Test
    void positionToStringTest() {
        Position position = new Position("P1");
        assertEquals("P1", position.toString());  // 문자열 표현 테스트
    }
}
