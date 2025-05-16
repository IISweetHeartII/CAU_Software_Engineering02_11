package model;

import java.util.ArrayDeque;

public class Player {
    /// fields ///
    protected final String playerID;
    protected final ArrayDeque<Piece> allPieces; // ArrayDeque로 변경
    protected final ArrayDeque<MovablePiece> movablePieces; // ArrayDeque로 변경
    protected int notArrivedCount; // 도착하지 않은 말의 개수

    /// Constructor ///
    public Player(String playerID, int numPieces) {
        this.playerID = playerID;
        this.allPieces = new ArrayDeque<>(); // ArrayDeque 초기화
        this.movablePieces = new ArrayDeque<>();
        this.notArrivedCount = numPieces; // ArrayDeque 초기화

        // initializing //
        for (int i = 0; i < numPieces; i++) {
            Piece piece = new Piece(playerID, playerID + "@Piece" + (i + 1));
            allPieces.add(piece); // ArrayDeque에 추가
            movablePieces.add(new MovablePiece(piece)); // MovablePiece 추가
        }
    }

    /// getters ///
    public String getPlayerID() {
        return playerID;
    }

    public ArrayDeque<Piece> getAllPieces() {
        return allPieces;
    }

    public ArrayDeque<MovablePiece> getMovablePieces() {
        return movablePieces;
    }

    public int getNotArrivedCount() {
        return notArrivedCount;
    }

    /// methods ///
    public boolean hasAllPiecesAtEnd() {
        for (MovablePiece piece : movablePieces) {
            if (!piece.getCurrentPosition().equals("END")) {
                return false; // 하나라도 END가 아니면 false
            }
        }
        return true; // 모든 말이 END에 도착했음
    }

    public MovablePiece getMovablePieceAt(Position position) {
        for (MovablePiece piece : movablePieces) {
            if (piece.getCurrentPosition().equals(position)) {
                return piece; // 해당 위치에 있는 말 반환
            }
        }
        return null; // 해당 위치에 말이 없음
    }
}