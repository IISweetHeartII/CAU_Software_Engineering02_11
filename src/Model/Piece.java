package Model;

import java.util.Deque;
import java.util.ArrayDeque;


public class Piece {
    private final Board board = new Board();
    private final String playerID;
    private final String pieceID;
    private Position currentPosition; //자신의 현재 위치 기억
    private ArrayDeque<Position> recentPath;

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

    public void moveTo(int n) {
        if (n < 0) {
            moveBackward(); // 음수일 경우 뒤로 이동
        } else {
            moveForward(n);
        }
    }

    public void moveForward(int n) {
        Position nextPosition = board.getNNextPosition(currentPosition, n);
        moveForward(nextPosition);
    }

    // 앞으로 이동할 때 호출됨
    public void moveForward(Position nextPosition) {
        // 현재 위치부터 다음 위치까지의 경로를 모두 저장
        while (!currentPosition.equals(nextPosition)) {
            currentPosition = board.getNNextPosition(currentPosition, 1); // 한 칸씩 이동
            recentPath.addLast(currentPosition); // 이동한 위치를 경로에 추가
        }
        this.currentPosition = nextPosition; // 최종 위치 업데이트
    }

    // 빽도: 한 칸 뒤로 이동 (이동 성공 시 true 반환)
    public void moveBackward() {
        if (recentPath.size() > 1) { // 시작 위치를 제외한 경로가 있는 경우
            recentPath.removeLast(); // 현재 위치 제거
            currentPosition = recentPath.peekLast(); // 이전 위치로 이동
        }
    }

    public Position getCurrentPosition() {
        return currentPosition;
    }

    public String getPieceID() {
        return pieceID;
    }

    public ArrayDeque<Position> getPath() {
        return recentPath;
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
