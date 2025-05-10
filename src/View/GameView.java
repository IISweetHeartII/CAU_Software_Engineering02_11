package View;

import java.util.ArrayDeque;

import Model.Position;
import Model.YutResult;

// View 패키지에서 사용할 임시 Position 클래스
/*class Position {
    private String name;
    
    public Position(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return name;
    }
}

// View 패키지에서 사용할 임시 YutResult 클래스
class YutResult {
    private String name;
    
    public YutResult(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return name;
    }
}*/

public interface GameView {
    void showYutResult(YutResult yutResult);
    //이후에 다른 요청 메시지들 전달 가능.

    void updateCurrentPlayer(String playerID);

    void showPosableMoves(ArrayDeque<Position> posableMoves);

    //Position getUserSelectedPosition(ArrayDeque<Position> posableMoves);

    void BoardRendering();

    void showGameEnd(String PlayerID); // 게임 종료 메시지 표시
}
