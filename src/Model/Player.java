package Model;

public class Player {
    /// fields ///
    private final String playerID;
    private final Piece[] allPieces; // 모든 말
    private MovablePiece[] movablePieces;

    /// Constructor ///
    public Player(String playerID, int numPieces) {
        // setting //
        this.playerID = playerID;
        this.allPieces = new Piece[numPieces];
        this.movablePieces = new MovablePiece[numPieces];
        // initializing //
        for (int i = 0; i < numPieces; i++) {
            movablePieces[i] = new MovablePiece(allPieces[i]); // 단일 그룹화로 movablePieces 초기화
        }
        for (int i = 0; i < numPieces; i++) {
            allPieces[i] = new Piece(playerID, playerID + "@Piece" + (i + 1)); // allPieces 초기화
        }
    }

    /// getters ///
    public String getPlayerID() {
        return playerID;
    }

    public Piece[] getAllPieces() {
        return allPieces;
    }

    public MovablePiece[] getMovablePieces() {
        return movablePieces;
    }

    /// methods ///
    public boolean hasAllPiecesAtEnd() {
        for (Piece piece : allPieces) {
            if (!piece.getCurrentPosition().equals("END")) {
                return false; // 하나라도 END가 아니면 false
            }
        }
        return true; // 모든 말이 END에 도착했음
    }

    public MovablePiece getCurrentPlayerPieceAtPosition(Position position) {
        for (MovablePiece piece : movablePieces) {
            if (piece.getCurrentPosition().equals(position)) {
                return piece; // 해당 위치에 있는 말 반환
            }
        }
        return null; // 해당 위치에 말이 없음
    }
}
