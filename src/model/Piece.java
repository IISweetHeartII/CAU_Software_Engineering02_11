package model;

// --- 설계 전략 ---
// id는 플레이어와 말의 그룹 갯수를 포합하는 형태를 가집니다.
// 예를 들어 "1"은 플레이어 1의 말 1개를 의미합니다.
// "111"은 플레이어 1의 말 3개가 모여있는 상태를 의미합니다.
// "2222"는 플레이어 2의 말 4개가 모여있는 상태를 의미합니다.
// "11"의 말과 "111"의 말이 같은 그룹으로 묶이면 "11111"의 형태가 됩니다.

// --- 빽도 전략 ---
// 말은 이전 위치만 기억하고, 현재 위치는 GameManager에서 관리합니다.
// 뒤로 한 칸 이동하는 경우 현재 위치와 이전 위치가 바뀌게 됩니다.

public class Piece {

    private String id;
    private int size;
    private Position previousPosition;

    // ------ constructor ------ //
    public Piece(String id, String previousPosition) {
        this.id = id;
        this.size = id.length();
        this.previousPosition = new Position(previousPosition);
    }

    // ------ getter ------ //
    public String getId() {
        return id;
    }

    public int getSize() {
        return size;
    }

    public String getPlayerId() {
        return id.substring(0, 1);
    }

    public Position getPreviousPosition() {
        return previousPosition;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    // ------ setter ------ //
    public void setPreviousPosition(String previousPosition) {
        this.previousPosition = new Position(previousPosition);
    }

    // ------ method ------ //
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Piece other)) return false;
        return this.id.equals(other.id)
                && this.size == other.size
                && this.previousPosition.equals(other.previousPosition);
    }

    public void group(Piece other) {
        if (this.id.charAt(0) != other.id.charAt(0)) {
            System.out.println("group:그룹화할 수 없습니다. 플레이어가 다릅니다.");
            return;
        }
        this.id += other.id;
        this.size += other.size;
    }

    public boolean isSamePlayers(Piece other) {
        return this.id.charAt(0) == other.id.charAt(0);
    }
}
