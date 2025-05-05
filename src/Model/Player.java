package Model;

public class Player {
    private final String playerID;
    private final Piece[] pieces;
    private int score;

    public Player(String playerID, int numPieces) {
        this.playerID = playerID;
        this.pieces = new Piece[numPieces]; // Assuming 4 pieces per player
        this.score = 0;
    }

    public String getPlayerID() {
        return playerID;
    }

    public Piece[] getPieces() {
        return pieces;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int points) {
        this.score += points;
    }

    public boolean hasAllPiecesAtEnd() {
        for (Piece piece : pieces) {
            if (!piece.getCurrentPosition().equals("END")) {
                return false; // 하나라도 END가 아니면 false
            }
        }
        return true; // 모든 말이 END에 도착했음
    }
}
