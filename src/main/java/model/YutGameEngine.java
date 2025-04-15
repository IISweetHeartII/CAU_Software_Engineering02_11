package model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * 윷놀이 게임의 핵심 로직을 담당하는 클래스입니다.
 */
public class YutGameEngine {
    private final YutStick yutStick;
    private final Board board;
    private final List<Player> players;
    private int currentPlayerIndex;
    private YutResult lastResult;
    private boolean canThrowAgain;
    
    /**
     * 게임 엔진의 생성자입니다.
     */
    public YutGameEngine() {
        this.yutStick = new YutStick();
        this.board = new Board();
        this.players = new ArrayList<>();
        this.currentPlayerIndex = 0;
        this.canThrowAgain = false;
        
        // 기본 플레이어 추가
        addPlayer("플레이어 1", Color.RED, 4);
        addPlayer("플레이어 2", Color.BLUE, 4);
    }
    
    /**
     * 새로운 플레이어를 게임에 추가합니다.
     * 
     * @param name 플레이어의 이름
     * @param color 플레이어의 색상
     * @param horseCount 말의 개수
     */
    public void addPlayer(String name, Color color, int horseCount) {
        Player player = new Player(name, color, horseCount);
        players.add(player);
        
        // 플레이어의 말을 보드에 추가
        for (Horse horse : player.getHorses()) {
            board.addHorse(horse);
        }
    }
    
    /**
     * 게임 보드를 반환합니다.
     * 
     * @return 게임 보드
     */
    public Board getBoard() {
        return board;
    }
    
    /**
     * 윷가락을 던집니다.
     * 
     * @return 윷가락 던지기 결과
     */
    public YutResult throwSticks() {
        yutStick.throwSticks();
        lastResult = yutStick.getResult();
        canThrowAgain = lastResult.canThrowAgain();
        return lastResult;
    }
    
    /**
     * 현재 윷가락을 반환합니다.
     * 
     * @return 현재 윷가락
     */
    public YutStick getYutStick() {
        return yutStick;
    }
    
    /**
     * 말을 이동시킵니다.
     * 
     * @param horseIndex 말 인덱스
     * @return 이동이 성공했으면 true, 아니면 false
     */
    public boolean moveHorse(int horseIndex) {
        Player player = getCurrentPlayer();
        if (horseIndex < 0 || horseIndex >= player.getHorses().size()) {
            return false;
        }
        
        Horse horse = player.getHorses().get(horseIndex);
        if (horse.isFinished()) {
            return false;
        }
        
        int currentPosition = horse.getPosition();
        int steps = lastResult.getValue();
        int newPosition = currentPosition + steps;
        
        if (newPosition >= board.getSize() * board.getSize()) {
            return false;
        }
        
        // 말 이동
        board.moveHorse(horse, newPosition);
        
        // 완주 확인
        if (horse.isFinished()) {
            player.incrementFinishedHorses();
        }
        
        // 윷/모가 아닌 경우 다음 플레이어로 턴 넘기기
        if (!canThrowAgain) {
            nextPlayer();
        }
        
        return true;
    }
    
    /**
     * 다음 플레이어로 턴을 넘깁니다.
     */
    private void nextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }
    
    /**
     * 현재 플레이어를 반환합니다.
     * 
     * @return 현재 플레이어
     */
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }
    
    /**
     * 게임이 종료되었는지 확인합니다.
     * 
     * @return 게임이 종료되었으면 true, 아니면 false
     */
    public boolean isGameOver() {
        for (Player player : players) {
            if (player.isAllHorsesFinished()) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 마지막 윷가락 던진 결과를 반환합니다.
     * 
     * @return 마지막 결과
     */
    public YutResult getLastResult() {
        return lastResult;
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