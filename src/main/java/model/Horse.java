package model;

import java.awt.Color;

/**
 * 말을 나타내는 클래스입니다.
 */
public class Horse {
    private int position; // 현재 위치
    private boolean isFinished; // 완주 여부
    private final Color color;
    private final Player player;
    
    /**
     * 말의 생성자입니다.
     * 
     * @param color 말의 색상
     * @param player 말의 소유자
     */
    public Horse(Color color, Player player) {
        this.position = 0;
        this.isFinished = false;
        this.color = color;
        this.player = player;
    }
    
    /**
     * 말을 이동시킵니다.
     * 
     * @param steps 이동할 칸 수
     * @return 이동 후의 위치
     */
    public int move(int steps) {
        position += steps;
        if (position >= 30) { // 완주점 도달
            position = 30;
            isFinished = true;
        }
        return position;
    }
    
    /**
     * 말의 현재 위치를 반환합니다.
     * 
     * @return 말의 현재 위치
     */
    public int getPosition() {
        return position;
    }
    
    /**
     * 말의 위치를 설정합니다.
     * 
     * @param position 새로운 위치
     */
    public void setPosition(int position) {
        this.position = position;
    }
    
    /**
     * 말의 색상을 반환합니다.
     * 
     * @return 말의 색상
     */
    public Color getColor() {
        return color;
    }
    
    /**
     * 말의 소유자를 반환합니다.
     * 
     * @return 말의 소유자
     */
    public Player getPlayer() {
        return player;
    }
    
    /**
     * 말이 완주했는지 여부를 반환합니다.
     * 
     * @return 완주했으면 true, 아니면 false
     */
    public boolean isFinished() {
        return isFinished;
    }
    
    /**
     * 말을 시작 위치로 되돌립니다.
     */
    public void reset() {
        position = 0;
        isFinished = false;
    }
} 