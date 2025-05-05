package View;

import Model.YutResult;

public interface GameView {
    void showYutResult(YutResult yutResult);
    //이후에 다른 요청 메시지들 전달 가능.

    void updateCurrentPlayer(String playerID);
}
