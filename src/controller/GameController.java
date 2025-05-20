//윷 던지기 버튼 선택에 대한 로직 처리

package controller;

import model.*;
import view.SwingUI;

public class GameController {
    public GameManager model;
    public SwingUI view;

    // state flag
    // sequence: yut -> selectPiece -> selectPosition
    public boolean yutState = true;
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
        view.showYutResult(yutResult.getValue());
        System.out.println("Controller: yutResult: " + yutResult.getValue());

        // update state
        yutState = yutResult.isExtraTurn();
        selectPieceState = !yutState;
    }


    // --------- 지정 윷 던지기 ---------
    public void handleManualThrow(int value) { // <------- gameView : ActionListener에서 호출됨
        YutResult yutResult = model.throwYutManual(value);
        view.showYutResult(yutResult.getValue());

        // update state
        yutState = yutResult.isExtraTurn();
        selectPieceState = !yutState;
    }


    // --------- 말 이동 ---------
    private String selectedPiecePositionId = ""; // 말 이동을 완료했을 때 이 String은 초기화 해야함
    private String selectedNodeId = ""; // 말 이동을 완료했을 때 이 String은 초기화 해야함

    public void handleBoardClick(String nodeId) {
        if (yutState) {
            System.out.println("controller: 윷을 던지는 단계입니다.");
            return;
        }

        if (selectPieceState) {
            System.out.println("controller: 말 선택 단계입니다.");
            if (!model.isCurrentPlayersPiecePresent(nodeId)) {
                System.out.println("controller: 현재 플레이어의 말이 아니거나 위치에 현재 플레이어의 말이 없습니다.");
                return;
            }
            selectedPiecePositionId = nodeId;

            selectPieceState = false;
            selectPositionState = true;
        }

        else if (selectPositionState) {
            // 움직인 말 선택을 바꾸고 싶은 경우
            if (selectedPiecePositionId.equals(nodeId)) {
                System.out.println("controller: 선택한 말과 같은 위치입니다. 움직일 말을 다시 선택합니다.");
                selectPieceState = true;
                selectPositionState = false;
                selectedPiecePositionId = "";
                selectedNodeId = "";
                return;
            }

            if (!model.isValidMove(selectedPiecePositionId, nodeId)) {
                System.out.println("controller: 이동할 수 없는 위치입니다.");
                return;
            }
            selectedNodeId = nodeId;
            model.controlMovePiece(selectedPiecePositionId, selectedNodeId);
            view.updateBoard();
            view.updatePlayerScore();

            // update state
            selectPieceState = false;
            selectPositionState = false;
            selectedPiecePositionId = "";
            selectedNodeId = "";
            System.out.println("controller: 이동 완료");

            // 게임 종료 처리
            if (model.isGameEnd()) {
                view.showWinner(model.getCurrentPlayer());
                System.out.println("controller: 게임 종료");
                yutState = false;
                selectPieceState = false;
                selectPositionState = false;
            }

            // Turn 처리
            if (model.isExtraTurn()) {
                System.out.println("controller: 추가 윷을 던질 수 있습니다.");
                yutState = true;
            } else if (model.isExtraMove()) {
                System.out.println("controller: 추가 이동을 할 수 있습니다.");
                model.printYutHistory();
                yutState = false;
                selectPieceState = true;
                selectPositionState = false;
            }
            else {
                System.out.println("controller: 턴을 넘깁니다.");
                model.nextTurn();
                view.updateTurn();
                yutState = true;
            }
        }
        else {
            System.out.println("controller: state error");
            return;
        }
    }

    // --------- 게임 초기화 ---------
    public void resetGame() {
        model = new GameManager();
        view = new SwingUI(this, model);
    }

    // --------- 게임 종료 ---------
    public boolean endGame() {
        endState = true;
        return endState;
    }

    // Todo: Turn 처리

    // Todo: 점수 처리
}
