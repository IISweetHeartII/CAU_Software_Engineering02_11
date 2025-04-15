package model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 게임 보드를 나타내는 클래스입니다.
 */
public class Board {
    private static final int SIZE = 5;
    private final Map<Integer, Horse> horses;
    private final Map<Integer, Color> colors;
    private final List<Horse> allHorses;
    
    /**
     * 보드의 생성자입니다.
     */
    public Board() {
        this.horses = new HashMap<>();
        this.colors = new HashMap<>();
        this.allHorses = new ArrayList<>();
    }
    
    /**
     * 보드의 크기를 반환합니다.
     * 
     * @return 보드의 크기
     */
    public int getSize() {
        return SIZE;
    }
    
    /**
     * 말을 이동시킵니다.
     * 
     * @param horse 이동할 말
     * @param position 이동할 위치
     */
    public void moveHorse(Horse horse, int position) {
        // 기존 위치에서 말 제거
        horses.remove(horse.getPosition());
        colors.remove(horse.getPosition());
        
        // 새로운 위치에 말 추가
        horses.put(position, horse);
        colors.put(position, horse.getColor());
        
        // 말의 위치 업데이트
        horse.setPosition(position);
    }
    
    /**
     * 말을 보드에 추가합니다.
     * 
     * @param horse 추가할 말
     */
    public void addHorse(Horse horse) {
        allHorses.add(horse);
        horses.put(horse.getPosition(), horse);
        colors.put(horse.getPosition(), horse.getColor());
    }
    
    /**
     * 보드의 모든 말을 반환합니다.
     * 
     * @return 모든 말의 리스트
     */
    public List<Horse> getHorses() {
        return allHorses;
    }
    
    /**
     * 특정 위치에 있는 말을 반환합니다.
     * 
     * @param position 확인할 위치
     * @return 해당 위치의 말, 없으면 null
     */
    public Horse getHorseAt(int position) {
        return horses.get(position);
    }
    
    /**
     * 특정 위치의 색상을 반환합니다.
     * 
     * @param position 확인할 위치
     * @return 해당 위치의 색상, 없으면 null
     */
    public Color getColorAt(int position) {
        return colors.get(position);
    }
    
    /**
     * 보드의 모든 말을 초기화합니다.
     */
    public void clear() {
        horses.clear();
        colors.clear();
        allHorses.clear();
    }
} 