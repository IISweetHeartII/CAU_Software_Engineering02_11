package Model;

import java.util.Deque;
import java.util.ArrayDeque;

public class Piece {
    private final Board board = new Board();
    private final String playerID; // 플레이어 ID
    private final String pieceID;
    private Position currentPosition; //자신의 현재 위치 기억
    private final Deque<Position> recentPath;
    // private static final int MAX_HISTORY = 5; //말의 이전 위치 최대 5개만 기억.

    /// 생성자
    public Piece(String playerID, String pieceID) {
        this(playerID, pieceID, new Position("START")); // 기본 위치는 START
    }

    public Piece(String playerID, String pieceID, Position startPosition) {
        this.playerID = playerID;
        this.pieceID = pieceID;
        this.currentPosition = startPosition;
        this.recentPath = new ArrayDeque<>();
        recentPath.addLast(startPosition); // 초기 위치 저장
    }

    // 앞으로 이동할 때 호출됨
    public void moveTo(Position nextPosition) {
        // 현재 위치부터 다음 위치까지의 경로를 모두 저장
        // Example: E2 -> E4 => recentPath: E2 -> E3 -> E4
        Position currentPosition = this.currentPosition;
        if (currentPosition.equals(nextPosition)) {
            return; // 이미 같은 위치에 있음
        }
        currentPosition = board.getNNextPosition(currentPosition, 1);
        recentPath.addLast(currentPosition);
        if (currentPosition.equals(nextPosition)) {}
        recentPath.addLast(nextPosition); // E2 -(E3)-> E4
        currentPosition = nextPosition;
    }

    // 빽도: 한 칸 뒤로 이동 (이동 성공 시 true 반환)
    public boolean moveBack() {
        if (recentPath.size() >= 2) {
            recentPath.removeLast(); // 현재 위치 제거
            currentPosition = recentPath.peekLast(); // 바로 이전 위치로
            return true;
        }
        return false; // 되돌아갈 위치가 없음
    }

    public Position getCurrentPosition() {
        return currentPosition;
    }

    public String getPieceID() {
        return pieceID;
    }

    @Override
    public String toString() {
        return pieceID + "@" + currentPosition;
    }

    public String getPlayerID() {
        return playerID;
    }

    // getter recentPath
    public Deque<Position> getRecentPath() {
        return recentPath;
    }
}
