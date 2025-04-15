package model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * 플레이어를 나타내는 클래스입니다.
 */
public class Player {
    private final String name;
    private final List<Horse> horses;
    private int finishedHorses;
    private final Color color;
    
    /**
     * 플레이어의 생성자입니다.
     * 
     * @param name 플레이어의 이름
     * @param color 플레이어의 색상
     * @param horseCount 말의 개수
     */
    public Player(String name, Color color, int horseCount) {
        this.name = name;
        this.color = color;
        this.horses = new ArrayList<>();
        this.finishedHorses = 0;
        
        // 말 생성
        for (int i = 0; i < horseCount; i++) {
            horses.add(new Horse(color, this));
        }
    }
    
    /**
     * 플레이어의 이름을 반환합니다.
     * 
     * @return 플레이어의 이름
     */
    public String getName() {
        return name;
    }
    
    /**
     * 플레이어의 색상을 반환합니다.
     * 
     * @return 플레이어의 색상
     */
    public Color getColor() {
        return color;
    }
    
    /**
     * 모든 말을 반환합니다.
     * 
     * @return 말 리스트
     */
    public List<Horse> getHorses() {
        return horses;
    }
    
    /**
     * 완주한 말의 수를 반환합니다.
     * 
     * @return 완주한 말의 수
     */
    public int getFinishedHorses() {
        return finishedHorses;
    }
    
    /**
     * 완주한 말의 수를 증가시킵니다.
     */
    public void incrementFinishedHorses() {
        finishedHorses++;
    }
    
    /**
     * 모든 말이 완주했는지 여부를 반환합니다.
     * 
     * @return 모든 말이 완주했으면 true, 아니면 false
     */
    public boolean isAllHorsesFinished() {
        return finishedHorses == horses.size();
    }
} 