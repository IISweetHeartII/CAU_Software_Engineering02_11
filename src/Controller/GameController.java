//윷 던지기 버튼 선택에 대한 로직 처리

package Controller;

import Model.YutResult;
import Model.YutResultType;
import Model.GameModel;
import View.GameView;

public class GameController {
    private final GameModel gameModel;
    private final GameView gameView;

    public GameController(GameModel model, GameView gameView) {
        this.gameModel = model;
        this.gameView = gameView;
    }

    public void handleManualThrow(YutResultType type) {
        YutResult result = new YutResult(type);  // 직접 생성
        gameView.showYutResult(result); //View에 결과 전달, UI 업데이트
        // 이후 말 이동 처리
    }

    public void handleRandomThrow(){
        YutResult result = gameModel.throwYutRandom(); //랜덤 생성
        gameView.showYutResult(result); //View에 결과 전달, UI 업데이트
        //이후 말 이동 처리
    }

    public void changeTurn() {
        gameModel.nextTurn(); //다음 턴으로 전환
        String currentPlayerID = gameModel.getCurrentPlayer().getPlayerID();
        gameView.updateCurrentPlayer(currentPlayerID); // UI 업데이트
    }
}
