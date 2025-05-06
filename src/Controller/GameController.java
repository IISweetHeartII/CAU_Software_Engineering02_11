//윷 던지기 버튼 선택에 대한 로직 처리

package Controller;

import Model.Position;
import Model.YutResult;
import Model.YutResultType;
import Model.GameModel;
import View.GameView;

import java.util.ArrayDeque;

public class GameController {
    private final GameModel gameModel;
    private final GameView gameView;

    public GameController(GameModel model, GameView gameView) {
        this.gameModel = model;
        this.gameView = gameView;
    }

    public void handleManualThrow(YutResultType type) {
        ArrayDeque<YutResult> yutResultArrayDeque = new ArrayDeque<>();
        YutResult result;
        do {
            result = new YutResult(type);  // 직접 생성
            yutResultArrayDeque.addLast(result); // 결과 저장
            gameView.showYutResult(result); //View에 결과 전달, UI 업데이트
        } while (result.isExtraTurn()); // 추가 턴이 있는 경우 반복

        // Todo: 이후 말 이동 처리
        ArrayDeque<Position> posableMoves = gameModel.getPosableMoves(yutResultArrayDeque);
        gameView.showPosableMoves(posableMoves);

        // 이후 사용자의 입력을 받아 이동 처리
        // 사용자 입력을 받아 이동 처리
        Position selectedPosition = gameView.getUserSelectedPosition(posableMoves); // 사용자로부터 선택된 위치를 가져옴
        if (selectedPosition != null) {
            Model.Piece selectedPiece = gameModel.getCurrentPlayer().getCurrentPlayerPieceAtPosition(selectedPosition); // 선택된 위치의 말을 가져옴
            if (selectedPiece != null) {
                /// gameModel.movePieceForward(selectedPiece, selectedPosition.getStepsFrom(selectedPiece.getCurrentPosition())); // 말 이동
                // Todo: 이동처리
            }
        }
        changeTurn(); // 턴 변경
    }

    public void handleRandomThrow(){
        YutResult result = gameModel.throwYutRandom(); //랜덤 생성
        gameView.showYutResult(result); //View에 결과 전달, UI 업데이트
        // Todo: 이후 말 이동 처리
    }

    public void changeTurn() {
        gameModel.nextTurn(); //다음 턴으로 전환
        String currentPlayerID = gameModel.getCurrentPlayer().getPlayerID();
        gameView.updateCurrentPlayer(currentPlayerID); // UI 업데이트
    }
}
