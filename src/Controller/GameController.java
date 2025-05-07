//윷 던지기 버튼 선택에 대한 로직 처리

package Controller;

import Model.*;
import View.GameView;

import java.util.ArrayDeque;

public class GameController {
    private final GameModel gameModel;
    private final GameView gameView;
    private final ArrayDeque<YutResult> currentTurnResult = new ArrayDeque<>();
    //현재 턴에 던진 윷 결과 저장 --> 함수 내부에서 전역으로 꺼냄 --> 모은 이벤트 핸들러에서 접근 가능



    public GameController(GameModel model, GameView gameView) {
        this.gameModel = model;
        this.gameView = gameView;

        //이벤트 공유(View와 연결(UI제작 이후 연결용) - UI에서 이벤트 발생 시 호출)
       /* this.gameView.setThrowYutListener(this::handleRandomThrow);
        this.gameView.setManualThrowListener(this::handleManualThrow);*/
    }


    //지정 윷 던지기
    public void handleManualThrow(YutResultType type) {
        currentTurnResult.clear(); //한 턴 결과 초기화

        YutResult result;
        do {
            result = new YutResult(type);  // 직접 생성
            currentTurnResult.add(result); // 결과를 큐에 추가
            gameView.showYutResult(result); //View에 결과 전달, UI 업데이트
        } while (result.isExtraTurn()); // 추가 턴이 있는 경우 반복

        // Todo: 이후 말 이동 처리
        ArrayDeque<Position> posableMoves = gameModel.getPosableMoves(currentTurnResult);
        gameView.showPosableMoves(posableMoves);

        // 이후 사용자의 입력을 받아 이동 처리
        // 사용자 입력을 받아 이동 처리
        Position selectedPosition = gameView.getUserSelectedPosition(posableMoves); // 사용자로부터 선택된 위치를 가져옴
        if (selectedPosition != null) {
            MovablePiece selectedPiece = gameModel.getCurrentPlayer().getMovablePieceAt(selectedPosition); // 선택된 위치의 말을 가져옴
            if (selectedPiece != null) {
                /// gameModel.movePieceForward(selectedPiece, selectedPosition.getStepsFrom(selectedPiece.getCurrentPosition())); // 말 이동
                //  gameView.BoardRendering(); // 보드 렌더링
                //Todo: 이동처리
            }
        }
    }

    //랜덤 윷 던지기
    public void handleRandomThrow(){
        currentTurnResult.clear(); //한 턴 결과 초기화


        YutResult result;
        do {
            result = gameModel.throwYutRandom();
            currentTurnResult.add(result);
            gameView.showYutResult(result);
        } while (result.isExtraTurn());
        // Todo: 이후 말 이동 처리*/
        ArrayDeque<Position> posableMoves = gameModel.getPosableMoves(currentTurnResult);
        gameView.showPosableMoves(posableMoves);
    }




    public void changeTurn() {
        gameModel.nextTurn(); //다음 턴으로 전환
        String currentPlayerID = gameModel.getCurrentPlayer().getPlayerID();
        gameView.updateCurrentPlayer(currentPlayerID); // UI 업데이트
    }

    public void checkGameEnd() {
        if (gameModel.getCurrentPlayer().hasAllPiecesAtEnd()) {
            gameView.showGameEnd(gameModel.getCurrentPlayer().getPlayerID());
        }
    }
}
