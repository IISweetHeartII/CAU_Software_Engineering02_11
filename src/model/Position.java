package model;

public class Position {
    // 단순히 String 타입을 사용할 수 있지만,
    // Position 클래스를 만들어서 타입을 명확히 하고,
    // 얕은 복사를 제공하기위해 Position 클래스를 사용합니다.
    private final String id;

    public Position(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Position other)) return false;
        return this.id.equals(other.id);
    }

    public boolean equals(String id) {
        return this.id.equals(id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return id;
    }
}
