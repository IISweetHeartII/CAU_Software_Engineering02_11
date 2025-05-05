package Model;

import java.util.Deque;
import java.util.ArrayDeque;

public class Piece {
    private final String id;
    private Position currentPosition; //자신의 현재 위치 기억
    private final Deque<Position> recentPath;
    private static final int MAX_HISTORY = 5;
    //말의 이전 위치 최대 5개만 기억.

    /// 생성자
    public Piece(String id) {
        this(id, new Position("START")); // 기본 위치는 START
    }

    public Piece(String id, Position startPosition) {
        this.id = id;
        this.currentPosition = startPosition;
        this.recentPath = new ArrayDeque<>();
        recentPath.addLast(startPosition); // 초기 위치 저장
    }

    // 앞으로 이동할 때 호출됨
    public void moveTo(Position nextPosition) {
        if (recentPath.size() >= MAX_HISTORY) {
            recentPath.removeFirst(); // 가장 오래된 경로 제거
        }
        recentPath.addLast(nextPosition);
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

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return id + "@" + currentPosition;
    }
}
