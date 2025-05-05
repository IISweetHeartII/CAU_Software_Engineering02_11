package Model;

import java.util.*;

public class GameModel {
    private final Board board = new Board(); //board
    private final YutThrow yutThrower = new YutThrow(); //YutThrow

    //랜덤 윷 던지기
    public YutResult throwYutRandom() {
        return yutThrower.throwRandom();
    }

    //지정 윷 던지기
    public YutResult throwYutManual(String input) {
        return yutThrower.throwManual(input);
    }

    //말 앞으로 이동
    public void movePieceForward(Piece piece, int steps) {
        Position current = piece.getCurrentPosition();
        Position next = board.getNNextPosition(current, steps);

        if (next != null) {
            piece.moveTo(next); // 다음 위치로 이동
        } else {
            System.out.println("이동할 수 없는 위치입니다.");
        }
    }



}
