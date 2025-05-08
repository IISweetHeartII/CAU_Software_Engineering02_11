package Model;

public class Position {
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

    public void print() {
        System.out.println("java.Position: " + id);
    }

    public boolean isStart() {
        return id.equals("START");
    }

    public boolean isEnd() {
        return id.equals("END");
    }
}
