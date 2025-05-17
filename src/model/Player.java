package model;

import java.util.ArrayDeque;

public class Player {
    /// fields ///
    protected final String playerID;
    protected final ArrayDeque<Unit> allUnits; // ArrayDeque로 변경
    protected final ArrayDeque<Piece> pieces; // ArrayDeque로 변경
    protected int notArrivedCount; // 도착하지 않은 말의 개수
    protected int notStartedCount; // 시작하지 않은 말의 개수

    /// Constructor ///
    public Player(String playerID, int numPieces) {
        this.playerID = playerID;
        this.allUnits = new ArrayDeque<>(); // ArrayDeque 초기화
        this.pieces = new ArrayDeque<>();
        this.notArrivedCount = numPieces; // ArrayDeque 초기화

        // initializing //
        for (int i = 0; i < numPieces; i++) {
            Unit unit = new Unit(playerID, "" + (i + 1));
            allUnits.add(unit); // ArrayDeque에 추가
            pieces.add(new Piece(unit)); // MovablePiece 추가
        }
    }

    /// getters ///
    public String getPlayerID() {
        return playerID;
    }

    public ArrayDeque<Unit> getAllUnits() {
        return allUnits;
    }

    public ArrayDeque<Piece> getPieces() {
        return pieces;
    }

    public int getNotArrivedCount() {
        return notArrivedCount;
    }

    /// methods ///
    public boolean hasAllPiecesAtEnd() {
        for (Piece piece : pieces) {
            if (!piece.getCurrentPosition().equals("END")) {
                return false; // 하나라도 END가 아니면 false
            }
        }
        return true; // 모든 말이 END에 도착했음
    }

    public Piece getMovablePieceAt(Position position) {
        for (Piece piece : pieces) {
            if (piece.getCurrentPosition().equals(position)) {
                return piece; // 해당 위치에 있는 말 반환
            }
        }
        return null; // 해당 위치에 말이 없음
    }

    public void updateNotStartedCount() {
        notStartedCount = 0;
        for (Piece piece : pieces) {
            if (piece.getCurrentPosition().equals("START")) {
                notStartedCount += piece.size; // START에 있는 말의 개수
            }
        }
    }
}