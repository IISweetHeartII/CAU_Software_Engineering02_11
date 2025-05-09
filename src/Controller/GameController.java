//윷 던지기 버튼 선택에 대한 로직 처리

package Controller;

import Model.*;
import View.GameView;

import java.util.ArrayDeque;

public class GameController {
    private final GameModel gameModel;
    private final GameView gameView;
   //private final ArrayDeque<YutResult> currentTurnResult = new ArrayDeque<>();
    //현재 턴에 던진 윷 결과 저장 --> 함수 내부에서 전역으로 꺼냄 --> 모은 이벤트 핸들러에서 접근 가능



    public GameController(GameModel model, GameView gameView) {
        this.gameModel = model;
        this.gameView = gameView;
    }


    // 지정 윷 던지기
    public void handleManualThrow(YutResultType type) {
        do {
            YutResult result = new YutResult(type);
            gameModel.getYutResultDeque().add(result);
            gameView.showYutResult(result);

            ArrayDeque<Position> posableMoves = gameModel.getPosableMoves();
            gameView.showPosableMoves(posableMoves);

            Position selectedPosition = gameView.getUserSelectedPosition(posableMoves);
            if (selectedPosition != null) {
                MovablePiece selectedPiece = gameModel.getCurrentPlayer().getMovablePieceAt(selectedPosition);
                if (selectedPiece != null && gameModel.movePiece(selectedPiece, selectedPosition)) {
                    gameView.BoardRendering();
                    checkGameEnd();
                }
            }
        } while (gameModel.isExtraTurn());

        if (!gameModel.isExtraTurn()) {
            gameModel.changeTurn();
            gameView.updateCurrentPlayer(gameModel.getCurrentPlayer().getPlayerID());
        }
    }

    // 랜덤 윷 던지기
    public void handleRandomThrow() {
        do {
            boolean extra = gameModel.throwAndSaveYut();
            gameView.showYutResult(gameModel.getYutResultDeque().peekLast());

            ArrayDeque<Position> posableMoves = gameModel.getPosableMoves();
            gameView.showPosableMoves(posableMoves);

            Position selectedPosition = gameView.getUserSelectedPosition(posableMoves);
            if (selectedPosition != null) {
                MovablePiece selectedPiece = gameModel.getCurrentPlayer().getMovablePieceAt(selectedPosition);
                if (selectedPiece != null && gameModel.movePiece(selectedPiece, selectedPosition)) {
                    gameView.BoardRendering();
                    checkGameEnd();
                }
            }
        } while (gameModel.isExtraTurn());

        if (!gameModel.isExtraTurn()) {
            gameModel.changeTurn();
            gameView.updateCurrentPlayer(gameModel.getCurrentPlayer().getPlayerID());
        }
    }

    // 사용자 클릭 위치 받아 처리
    public void handleMoveRequest(Position selectedPosition) {
        MovablePiece selectedPiece = gameModel.getCurrentPlayer().getMovablePieceAt(selectedPosition);
        if (selectedPiece == null) return;

        String currentPlayerID = gameModel.getCurrentPlayer().getPlayerID();
        if (!selectedPiece.getPlayerID().equals(currentPlayerID)) return;

        boolean moved = gameModel.movePiece(selectedPiece, selectedPosition);
        if (!moved) return;

        if (selectedPiece.isArrived()) {
            gameModel.addScore(gameModel.getCurrentPlayer());
        }

        gameView.BoardRendering();
        checkGameEnd();

        if (!gameModel.isExtraTurn()) {
            gameModel.changeTurn();
            gameView.updateCurrentPlayer(gameModel.getCurrentPlayer().getPlayerID());
        }
    }

    public void checkGameEnd() {
        if (gameModel.isGameEnd()) {
            gameView.showGameEnd(gameModel.getCurrentPlayer().getPlayerID());
        }
    }
}
