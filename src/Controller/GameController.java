//윷 던지기 버튼 선택에 대한 로직 처리

package Controller;

import Model.*;
import View.SwingUI;

public class GameController {
    public GameModel model;
    public SwingUI view;

    // state flag
    public boolean yutState = false;
    public boolean moveState = false;
    public boolean resetState = false;
    public boolean endState = false;


    // --------- Constructor ---------
    public GameController(GameModel gameModel) {
        this.model = gameModel;
        this.view = new SwingUI(this, this.model); // <----- View.initUI() 포함한다
    }


    // --------- 랜덤 윷 던지기 ---------
    public void handleRandomThrow() { // <----- gameView : ActionListener에서 호출됨
        YutResult yutResult = model.throwAndSaveYut();
        yutState = yutResult.isExtraTurn();
        view.showYutResult(yutResult);
        moveState = !yutState;
    }


    // --------- 지정 윷 던지기 ---------
    public void handleManualThrow(YutResult yutResult) { // <------- gameView : ActionListener에서 호출됨
        YutResult copy = model.throwAndSaveYut(yutResult.getValue());
        yutState = copy.isExtraTurn();
        view.showYutResult(copy);
        moveState = !yutState;
    }


    public void handleManualThrow(int valueOfYut) { // <------- gameView : ActionListener에서 호출됨
        YutResult yutResult = model.throwAndSaveYut(valueOfYut);
        yutState = yutResult.isExtraTurn();
        view.showYutResult(yutResult);
        moveState = !yutState;
    }


    public void handleManualThrow(String StringOfYut) { // <------- gameView : ActionListener에서 호출됨
        YutResult yutResult = model.throwAndSaveYut(StringOfYut);
        yutState = yutResult.isExtraTurn();
        view.showYutResult(yutResult);
        moveState = !yutState;
    }


    // --------- 말 이동 ---------
    public void movePiece(Position selectedPosition) { // <--------- gameView :
        if (!moveState) return;

        model.getPosableMoves();
        model.movePiece(selectedPosition);

        view.updateBoard();
        view.updatePlayerState();

        if (model.getYutResultDeque().isEmpty()) {
            moveState = false;
            if (model.isGameEnd()) {
                view.showGameEnd(model.getCurrentPlayer().getPlayerID());
                return;
            }
            model.changeTurn();
            view.updateCurrentPlayer(model.getCurrentPlayer().getPlayerID());
        }
    }

    // --------- 게임 초기화 ---------
    public boolean resetGame() {
        resetState = true;
        return resetState;
    }

    // --------- 게임 종료 ---------
    public boolean endGame() {
        endState = true;
        return endState;
    }
}
