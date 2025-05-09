package View;

import Model.YutResult;
import java.util.ArrayDeque;
import Model.Position;

public interface GameView {
    void showYutResult(YutResult yutResult);
    //이후에 다른 요청 메시지들 전달 가능.

    void updateCurrentPlayer(String playerID);

    void showPosableMoves(ArrayDeque<Position> posableMoves);

    //Position getUserSelectedPosition(ArrayDeque<Position> posableMoves);

    void BoardRendering();

    void showGameEnd(String PlayerID); // 게임 종료 메시지 표시
}
