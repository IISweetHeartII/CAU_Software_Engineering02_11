//윷 던지기 버튼 선택에 대한 로직 처리

package controller;

import model.*;
import view.SwingUI;

public class GameController {
    public GameManager model;
    public SwingUI view;

    // state flag
    public boolean yutState = true;
    public boolean moveState = false;
    public boolean selectPieceState = false;
    public boolean selectPositionState = false;
    public boolean resetState = false;
    public boolean endState = false;

    // --------- Constructor ---------
    public GameController(GameManager gameManager) {
        this.model = gameManager;
    }

    public void setView(SwingUI view) {
        this.view = view;
    }

    // --------- 랜덤 윷 던지기 ---------
    public void handleRandomThrow() { // <----- gameView : ActionListener에서 호출됨
        if (!yutState) return;
        YutResult yutResult = model.throwYutRandom();
        yutState = yutResult.isExtraTurn();
        view.showYutResult(yutResult.getValue());
        System.out.println("yutResult: " + yutResult.getValue());
        moveState = !yutState;
        yutState = false;
        selectPieceState = true;
    }


    // --------- 지정 윷 던지기 ---------
    public void handleManualThrow(YutResult yutResult) { // <------- gameView : ActionListener에서 호출됨
        YutResult copy = model.throwYutManual(yutResult.getValue());
        yutState = copy.isExtraTurn();
        view.showYutResult(copy.getValue());
        moveState = !yutState;
        yutState = false;
    }


    public void handleManualThrow(int value) { // <------- gameView : ActionListener에서 호출됨
        YutResult yutResult = model.throwYutManual(value);
        yutState = yutResult.isExtraTurn();
        view.showYutResult(yutResult.getValue());
        moveState = !yutState;
        yutState = false;
    }


    // --------- 말 이동 ---------
    private String selectedPiecePositionId = "";
    private String selectedNodeId = "";

    public void handleBoardClick(String nodeId) {
        if (selectPieceState && !selectPositionState) { // 1. 움직일 말 선택
            // check if the selected piece is movable
            if (!(model.isCurrentPlayersPiecePresent(nodeId))) {
                System.out.println("해당 위치에 현재 플레이어의 말 없음"); // ---> 테스트용
                return;
            }
            selectedPiecePositionId = nodeId;
            selectPieceState = false;
            selectPositionState = true;
        } else if (!selectPieceState && selectPositionState) { // 2. 이동할 위치 선택
            if (model.isCurrentPlayersPiecePresent(nodeId)) { // 이동할 말을 바꾼다면
                selectedPiecePositionId = nodeId;
                return;
            }
            if (!(model.isPositionMovable(nodeId))) { // 이동할 위치가 유효하지 않다면
                System.out.println("해당 위치로 이동할 수 없음"); // ---> 테스트용
                return;
            }
            selectedNodeId = nodeId;
            String selectedPieceId = model.getPieceIdAtNodeId(selectedPiecePositionId);
            model.handleMovePiece(selectedPieceId, selectedNodeId);
            selectPieceState = true;
            selectPositionState = false;

            view.updateBoard();
            view.updatePlayerScore();

            // test
            System.out.println("selectedPieceId: " + selectedPieceId);
            System.out.println("selectedNodeId: " + selectedNodeId);
            System.out.println("currentPlayer: " + model.getCurrentPlayerNumber());
            System.out.println("notStarted: " + model.getNotStartedCount()[model.getCurrentPlayerNumber() - 1]);

            if (model.isALlMoved()) {
                moveState = false;
                yutState = true;
                if (model.isGameEnd()) {
                    view.showGameEnd(model.getCurrentPlayerNumber());
                    return;
                }
                // Todo: 테스트용 턴 변경
                // model.changeTurn();
                view.updateTurn();
            }
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

    // Todo: Turn 처리

    // Todo: 점수 처리
}
