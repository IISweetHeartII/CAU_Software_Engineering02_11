package model;

import java.util.Deque;
import java.util.ArrayDeque;


public class Unit {
    protected final Board board = new Board();
    protected String DUMMY = "";
    protected final String pieceID;
    protected Position currentPosition; //자신의 현재 위치 기억
    protected ArrayDeque<Position> recentPath;

    public Unit(String playerID, String pieceID) {
        this(playerID, pieceID, new Position("START")); // 기본 위치는 START
    }

    public Unit(String playerID, String pieceID, Position startPosition) {
        this.DUMMY = playerID;
        this.pieceID = pieceID;
        this.currentPosition = startPosition;
        this.recentPath = new ArrayDeque<>();
        recentPath.addLast(startPosition); // 초기 위치 저장
    }

    public Unit(Unit other) {
        this.pieceID = other.pieceID;
        this.currentPosition = other.currentPosition;
        this.recentPath = new ArrayDeque<>(other.recentPath); // 깊은 복사
    }

    /// n이 음수일 경우 뒤로 한 칸 이동하고, n이 양수일경우 앞으로 n칸 이동
    public void moveTo(int n) {
        if (n < 0) {
            moveBackward(); // 음수일 경우 뒤로 한 칸 이동
        } else if (n == 0) {
            currentPosition = new Position("START"); // 현재 위치를 START로 초기화
            recentPath.clear();
            recentPath.add(currentPosition); // START 위치 추가
        } else {
            moveForward(n);
        }
    }

    public void moveForward(int n) {
        if (n > 0) {
            Position nextPosition = board.getNNextPosition(currentPosition, n);
            moveForward(nextPosition);
        }
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
        /*if (recentPath.size() > 1) { // 시작 위치를 제외한 경로가 있는 경우
            recentPath.removeLast(); // 현재 위치 제거
            currentPosition = recentPath.peekLast(); // 이전 위치로 이동
        }*/
        // P1 빽도일때 예외 상황 추가
        if (recentPath.peekLast() != null) {
            if (recentPath.peekLast().equals("P1")) {
                switch (board.boardFigure) {
                    case 4 -> {
                        currentPosition = new Position("P20");
                        recentPath.addLast(currentPosition); // [P1 P20]
                    }
                    case 5 -> {
                        currentPosition = new Position("P25");
                        recentPath.addLast(currentPosition); // [P1 P25]
                    }
                    case 6 -> {
                        currentPosition = new Position("P30");
                        recentPath.addLast(currentPosition); // [P1 P30]
                    }
                }
            }
        }
        currentPosition = recentPath.peekLast(); // [P1 P2]
        recentPath.pop(); // [P1]
        Position previousPosition = recentPath.peekLast(); // [P1]
        if (previousPosition != null) {
            recentPath.addLast(currentPosition); // [P1 P2]
            recentPath.addLast(previousPosition); // [P1 P2 P1]
            currentPosition = recentPath.peekLast();
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
        return String.valueOf(pieceID.charAt(0)); // 플레이어 ID는 pieceID의 첫 글자
    }

    // getter recentPath
    public Deque<Position> getRecentPath() {
        return recentPath;
    }

    public boolean isEnd() {
        return currentPosition.equals("END"); // 현재 위치가 END인지 확인
    }

    public boolean isStart() {
        return currentPosition.equals("START"); // 현재 위치가 START인지 확인
    }
}
