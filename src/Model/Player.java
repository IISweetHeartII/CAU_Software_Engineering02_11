package Model;

public class Player {
    private final String playerID;
    private final Piece[] allPieces; // 모든 말
    private Piece[] movablePieces; /// 더 좋은 변수명 찾기
    private int score;

    public Player(String playerID, int numPieces) {
        this.playerID = playerID;
        this.allPieces = new Piece[numPieces];
        this.movablePieces = new Piece[numPieces];
        this.score = 0;

        for (int i = 0; i < numPieces; i++) {
            allPieces[i] = new Piece(playerID, playerID + " Piece" + (i + 1)); // 각 플레이어의 말 생성
        }
    }

    public String getPlayerID() {
        return playerID;
    }

    public Piece[] getAllPieces() {
        return allPieces;
    }

    public Piece[] getMovablePieces() {
        return movablePieces;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int points) {
        this.score += points;
    }

    public boolean hasAllPiecesAtEnd() {
        for (Piece piece : allPieces) {
            if (!piece.getCurrentPosition().equals("END")) {
                return false; // 하나라도 END가 아니면 false
            }
        }
        return true; // 모든 말이 END에 도착했음
    }

    public Piece getCurrentPlayerPieceAtPosition(Position position) {
        for (Piece piece : movablePieces) {
            if (piece.getCurrentPosition().equals(position)) {
                return piece; // 해당 위치에 있는 말 반환
            }
        }
        return null; // 해당 위치에 말이 없음
    }
}
