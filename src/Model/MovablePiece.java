package Model;

import java.util.ArrayDeque;
import java.util.Collections;

public class MovablePiece {
    /// field ///
    private ArrayDeque<Piece> pieceArrayDeque; // ArrayDeque로 변경
    private Position currentPosition;

    /// Constructor ///
    public MovablePiece(Piece... pieces) { // 가변 인자를 사용해 여러 개의 말을 그룹화
        this.pieceArrayDeque = new ArrayDeque<>(); // ArrayDeque 초기화
        Collections.addAll(this.pieceArrayDeque, pieces);
        this.currentPosition = pieces[0].getCurrentPosition();
    }

    /// setters ///

    /// getters ///
    public ArrayDeque<Piece> getPieceArrayDeque() {
        return pieceArrayDeque;
    }

    public Position getCurrentPosition() {
        return currentPosition;
    }

    /// methods ///
    public void moveTo(int n) {
        for (Piece piece : pieceArrayDeque) {
            piece.moveTo(n);
        }
        if (pieceArrayDeque.peekFirst() != null) {
            currentPosition = pieceArrayDeque.peekFirst().getCurrentPosition(); // 그룹의 첫 번째 말의 위치로 업데이트
        }
    }

    public boolean isArrived() {
        return currentPosition.equals(new Position("END")); // 그룹의 첫 번째 말이 END에 도착했음
    }

    public boolean equals(MovablePiece other) {
        // 다른 MovablePiece
        return this.pieceArrayDeque.equals(other.getPieceArrayDeque()); // 두 MovablePiece가 같은 말을 포함하고 있음
    }
}