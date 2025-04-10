package view;
/**
 * 윷놀이 게임의 게임판을 관리하는 클래스입니다.
 * 게임판의 상태와 말의 위치를 관리합니다.
 */
public class Board {
    private int[][] board;
    private static final int BOARD_SIZE = 29; // 윷놀이 게임판 크기 (시작점 포함)
    private static final int EMPTY = -1; // 빈 칸
    private static final int PLAYER1 = 0; // 플레이어1
    private static final int PLAYER2 = 1; // 플레이어2
    
    /**
     * Board 생성자
     */
    public Board() {
        board = new int[BOARD_SIZE][2]; // [위치][플레이어]
        initializeBoard();
    }
    
    /**
     * 게임판을 초기화합니다.
     */
    public void initializeBoard() {
        // 모든 칸을 빈 칸으로 초기화
        for (int i = 0; i < BOARD_SIZE; i++) {
            board[i][PLAYER1] = EMPTY;
            board[i][PLAYER2] = EMPTY;
        }
    }
    
    /**
     * 말을 이동합니다.
     * @param player 플레이어 인덱스 (0: 플레이어1, 1: 플레이어2)
     * @param pieceIndex 이동할 말의 인덱스 (0~3)
     * @param steps 이동할 칸 수
     * @return 이동 성공 여부
     */
    public boolean movePiece(int player, int pieceIndex, int steps) {
        if (player < 0 || player > 1 || pieceIndex < 0 || pieceIndex > 3 || steps < 0) {
            return false;
        }
        
        // 현재 말의 위치 찾기
        int currentPosition = findPiecePosition(player, pieceIndex);
        
        // 말이 게임판에 없는 경우 시작점에서 시작
        if (currentPosition == -1) {
            currentPosition = 0;
        }
        
        // 새로운 위치 계산
        int newPosition = calculateNewPosition(currentPosition, steps);
        
        // 게임판 범위를 벗어나는 경우
        if (newPosition >= BOARD_SIZE) {
            return false;
        }
        
        // 말 이동
        board[currentPosition][player] = EMPTY;
        board[newPosition][player] = pieceIndex;
        
        return true;
    }
    
    /**
     * 말들을 그룹화합니다.
     * @param player 플레이어 인덱스 (0: 플레이어1, 1: 플레이어2)
     * @param pieceIndices 그룹화할 말들의 인덱스 배열
     * @return 그룹화 성공 여부
     */
    public boolean groupPieces(int player, int[] pieceIndices) {
        if (player < 0 || player > 1 || pieceIndices == null || pieceIndices.length < 2) {
            return false;
        }
        
        // 모든 말이 같은 위치에 있는지 확인
        int position = findPiecePosition(player, pieceIndices[0]);
        if (position == -1) {
            return false;
        }
        
        for (int i = 1; i < pieceIndices.length; i++) {
            if (findPiecePosition(player, pieceIndices[i]) != position) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * 상대방의 말을 잡습니다.
     * @param player 공격하는 플레이어 인덱스 (0: 플레이어1, 1: 플레이어2)
     * @param attackerPieceIndex 공격하는 말의 인덱스
     * @param targetPieceIndex 잡을 말의 인덱스
     * @return 잡기 성공 여부
     */
    public boolean capturePiece(int player, int attackerPieceIndex, int targetPieceIndex) {
        if (player < 0 || player > 1 || attackerPieceIndex < 0 || attackerPieceIndex > 3 || 
            targetPieceIndex < 0 || targetPieceIndex > 3) {
            return false;
        }
        
        int attackerPosition = findPiecePosition(player, attackerPieceIndex);
        int targetPosition = findPiecePosition(1 - player, targetPieceIndex);
        
        // 공격자와 대상이 같은 위치에 있는지 확인
        if (attackerPosition == -1 || targetPosition == -1 || attackerPosition != targetPosition) {
            return false;
        }
        
        // 말 잡기
        board[targetPosition][1 - player] = EMPTY;
        
        return true;
    }
    
    /**
     * 말의 현재 위치를 찾습니다.
     * @param player 플레이어 인덱스 (0: 플레이어1, 1: 플레이어2)
     * @param pieceIndex 말의 인덱스 (0~3)
     * @return 말의 위치 (-1: 게임판에 없음)
     */
    private int findPiecePosition(int player, int pieceIndex) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (board[i][player] == pieceIndex) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * 새로운 위치를 계산합니다.
     * @param currentPosition 현재 위치
     * @param steps 이동할 칸 수
     * @return 새로운 위치
     */
    private int calculateNewPosition(int currentPosition, int steps) {
        // 윷놀이 게임판의 특수 경로 처리
        // TODO: 윷놀이 게임판의 특수 경로 로직 구현
        return currentPosition + steps;
    }
    
    /**
     * 현재 게임판의 상태를 반환합니다.
     * @return 게임판의 현재 상태
     */
    public int[][] getBoardState() {
        int[][] state = new int[BOARD_SIZE][2];
        for (int i = 0; i < BOARD_SIZE; i++) {
            state[i][0] = board[i][0];
            state[i][1] = board[i][1];
        }
        return state;
    }
} 