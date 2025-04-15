package model;

import java.util.Random;

/**
 * 윷가락을 나타내는 클래스입니다.
 */
public class YutStick {
    private final boolean[] sticks;
    private final Random random;
    
    /**
     * 윷가락의 생성자입니다.
     */
    public YutStick() {
        this.sticks = new boolean[4];
        this.random = new Random();
    }
    
    /**
     * 윷가락을 던집니다.
     */
    public void throwSticks() {
        for (int i = 0; i < 4; i++) {
            sticks[i] = random.nextBoolean();
        }
    }
    
    /**
     * 윷가락의 상태를 반환합니다.
     * 
     * @return 윷가락의 상태 배열 (true: 앞면, false: 뒷면)
     */
    public boolean[] getSticks() {
        return sticks.clone();
    }
    
    /**
     * 윷가락 던지기 결과를 반환합니다.
     * 
     * @return 윷가락 던지기 결과
     */
    public YutResult getResult() {
        int frontCount = 0;
        for (boolean stick : sticks) {
            if (stick) frontCount++;
        }
        
        switch (frontCount) {
            case 0:
                return new YutResult("모", 5, true);
            case 1:
                return new YutResult("도", 1, false);
            case 2:
                return new YutResult("개", 2, false);
            case 3:
                return new YutResult("걸", 3, false);
            case 4:
                return new YutResult("윷", 4, true);
            default:
                throw new IllegalStateException("Invalid stick count: " + frontCount);
        }
    }
} 