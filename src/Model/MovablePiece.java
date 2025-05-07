package Model;

public class MovablePiece {
    /// field ///
    private Piece[] pieces;
    private Position currentPosition;
    private boolean isArrived;

    /// Constructor ///
    public MovablePiece(Piece... pieces) { // 가변 인자를 사용해 여러 개의 말을 그룹화
        this.pieces = pieces;
        this.currentPosition = pieces[0].getCurrentPosition();
        this.isArrived = false;
    }

    /// setters ///
    public void setArrived(boolean isArrived) {
        this.isArrived = isArrived;
    }

    /// getters ///
    public Piece[] getPieces() {
        return pieces;
    }

    public Position getCurrentPosition() {
        return currentPosition;
    }

    /// methods ///
    public void moveTo(int n) {
        for (Piece piece : pieces) {
            piece.moveTo(n);
        }
        currentPosition = pieces[0].getCurrentPosition(); // 그룹의 첫 번째 말의 위치로 업데이트
    }
}
