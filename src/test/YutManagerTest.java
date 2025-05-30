package test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import model.YutManager;
import model.YutResult;


public class YutManagerTest {
    @Test
    void throwYutRandomTest() {
        YutManager yutManager = new YutManager();
        YutResult result = yutManager.throwYutRandom();

        // 랜덤 던지기 결과 테스트
        assertNotNull(result);
        assertTrue(result.getValue() >= -1 && result.getValue() <= 5);
    }

    @Test
    void throwYutCustomTest() {
        YutManager yutManager = new YutManager();

        // 지정 던지기 결과 테스트
        assertEquals(-1, yutManager.throwYutCustom(-1).getValue());
        assertEquals(1, yutManager.throwYutCustom(1).getValue());
        assertEquals(2, yutManager.throwYutCustom(2).getValue());
        assertEquals(3, yutManager.throwYutCustom(3).getValue());
        assertEquals(4, yutManager.throwYutCustom(4).getValue());
        assertEquals(5, yutManager.throwYutCustom(5).getValue());
    }
}