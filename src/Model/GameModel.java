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
}
