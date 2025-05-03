import view.Board;

package model;
/**
 * 윷놀이 게임의 핵심 게임 로직을 관리하는 클래스입니다.
 * MVC 패턴에서 Model 역할을 담당합니다.
 */
public class GameEngine {
    private Board board;
    private boolean isGameRunning;
    private int currentPlayer; // 0: 플레이어1, 1: 플레이어2
    private int[] playerPieces; // 각 플레이어의 말 개수
    private static final int TOTAL_PIECES = 4; // 각 플레이어당 말 개수
    
    /**
     * GameEngine 생성자
     */
    public GameEngine() {
        board = new Board();
        isGameRunning = false;
        currentPlayer = 0;
        playerPieces = new int[2];
        playerPieces[0] = TOTAL_PIECES;
        playerPieces[1] = TOTAL_PIECES;
    }
    
    /**
     * 게임을 시작합니다.
     */
    public void start() {
        isGameRunning = true;
        currentPlayer = 0;
        board.initializeBoard();
    }
    
    /**
     * 게임을 종료합니다.
     */
    public void stop() {
        isGameRunning = false;
    }
    
    /**
     * 윷을 던지고 결과를 반환합니다.
     * @return 윷 던지기 결과 (0: 도, 1: 개, 2: 걸, 3: 윷, 4: 모)
     */
    public int throwYut() {
        if (!isGameRunning) {
            return -1;
        }
        
        // 랜덤 결과 생성 (0~4)
        int result = (int)(Math.random() * 5);
        return result;
    }
    
    /**
     * 현재 플레이어의 말을 이동합니다.
     * @param pieceIndex 이동할 말의 인덱스 (0~3)
     * @param steps 이동할 칸 수
     * @return 이동 성공 여부
     */
    public boolean movePiece(int pieceIndex, int steps) {
        if (!isGameRunning || pieceIndex < 0 || pieceIndex >= TOTAL_PIECES) {
            return false;
        }
        
        // 말 이동 로직 구현
        boolean success = board.movePiece(currentPlayer, pieceIndex, steps);
        
        if (success) {
            // 턴 종료 시 플레이어 변경
            currentPlayer = (currentPlayer + 1) % 2;
        }
        
        return success;
    }
    
    /**
     * 현재 플레이어의 말들을 그룹화합니다.
     * @param pieceIndices 그룹화할 말들의 인덱스 배열
     * @return 그룹화 성공 여부
     */
    public boolean groupPieces(int[] pieceIndices) {
        if (!isGameRunning || pieceIndices == null || pieceIndices.length < 2) {
            return false;
        }
        
        // 그룹화 로직 구현
        return board.groupPieces(currentPlayer, pieceIndices);
    }
    
    /**
     * 상대방의 말을 잡습니다.
     * @param attackerPieceIndex 공격하는 말의 인덱스
     * @param targetPieceIndex 잡을 말의 인덱스
     * @return 잡기 성공 여부
     */
    public boolean capturePiece(int attackerPieceIndex, int targetPieceIndex) {
        if (!isGameRunning || attackerPieceIndex < 0 || attackerPieceIndex >= TOTAL_PIECES ||
            targetPieceIndex < 0 || targetPieceIndex >= TOTAL_PIECES) {
            return false;
        }
        
        // 잡기 로직 구현
        return board.capturePiece(currentPlayer, attackerPieceIndex, targetPieceIndex);
    }
    
    /**
     * 게임이 진행 중인지 확인합니다.
     * @return 게임 진행 상태
     */
    public boolean isGameRunning() {
        return isGameRunning;
    }
    
    /**
     * 현재 플레이어를 반환합니다.
     * @return 현재 플레이어 (0: 플레이어1, 1: 플레이어2)
     */
    public int getCurrentPlayer() {
        return currentPlayer;
    }
    
    /**
     * 게임판 상태를 반환합니다.
     * @return 게임판의 현재 상태
     */
    public int[][] getBoardState() {
        return board.getBoardState();
    }
    
    /**
     * 플레이어의 말 개수를 반환합니다.
     * @param player 플레이어 인덱스 (0: 플레이어1, 1: 플레이어2)
     * @return 해당 플레이어의 말 개수
     */
    public int getPlayerPieces(int player) {
        if (player < 0 || player >= 2) {
            return 0;
        }
        return playerPieces[player];
    }
    
    /**
     * 게임 승리 조건을 확인합니다.
     * @return 승리한 플레이어 인덱스 (-1: 승리 없음)
     */
    public int checkWinCondition() {
        // 승리 조건 확인 로직 구현
        // TODO: 승리 조건 구현
        return -1;
    }
} 