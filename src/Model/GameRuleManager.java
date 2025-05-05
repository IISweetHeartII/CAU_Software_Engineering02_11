package Model;

public class GameRuleManager {

    private final Board board;

    public GameRuleManager(Board board) {
        this.board = board;
    }

    // 말이 이동 가능한지
    public boolean canMove(Piece piece, int steps) {
        Position next = board.getNNextPosition(piece.getCurrentPosition(), steps);
        return next != null;
    }

    // 말이 잡을 수 있는 상황인지
    public boolean canCapture(Piece myPiece, Piece targetPiece) {
        return myPiece.getCurrentPosition().equals(targetPiece.getCurrentPosition());
    }

    // 말이 업힐 수 있는지
    public boolean canStack(Piece myPiece, Piece allyPiece) {
        return myPiece.getCurrentPosition().equals(allyPiece.getCurrentPosition());
    }

    // 말이 도착했는지
    public boolean isArrived(Piece piece) {
        return "END".equals(piece.getCurrentPosition().getId());
    }

    // 빽도 처리 (불가능하면 false 반환)
    public boolean moveBack(Piece piece) {
        return piece.moveBack(); // Piece가 recentPath 갖고 있음
    }

}

