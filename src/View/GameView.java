package View;

import Model.Position;
import Model.YutResult;
import java.util.ArrayDeque;

public interface GameView {
    void showYutResult(YutResult yutResult);
    //이후에 다른 요청 메시지들 전달 가능.

    void updateCurrentPlayer(String playerID);

    void showPosableMoves(ArrayDeque<Model.Position> posableMoves);

    Model.Position getUserSelectedPosition(ArrayDeque<Model.Position> posableMoves);

    void BoardRendering();
}
