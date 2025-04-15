package model;

/**
 * 윷가락 던지기 결과를 나타내는 클래스입니다.
 */
public class YutResult {
    private final String name;
    private final int value;
    private final boolean canThrowAgain;
    
    /**
     * 윷가락 던지기 결과의 생성자입니다.
     * 
     * @param name 결과 이름 (도, 개, 걸, 윷, 모)
     * @param value 이동할 칸 수
     * @param canThrowAgain 추가로 던질 수 있는지 여부
     */
    public YutResult(String name, int value, boolean canThrowAgain) {
        this.name = name;
        this.value = value;
        this.canThrowAgain = canThrowAgain;
    }
    
    /**
     * 결과 이름을 반환합니다.
     * 
     * @return 결과 이름
     */
    public String getName() {
        return name;
    }
    
    /**
     * 이동할 칸 수를 반환합니다.
     * 
     * @return 이동할 칸 수
     */
    public int getValue() {
        return value;
    }
    
    /**
     * 추가로 던질 수 있는지 여부를 반환합니다.
     * 
     * @return 추가로 던질 수 있으면 true, 아니면 false
     */
    public boolean canThrowAgain() {
        return canThrowAgain;
    }
} 