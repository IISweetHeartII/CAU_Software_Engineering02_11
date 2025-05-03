package com.yut.game.controller;

import com.yut.game.model.GameEngine;
import com.yut.game.view.MainFrame;
import java.awt.event.ActionListener;

/**
 * 게임의 전반적인 흐름을 제어하는 컨트롤러 클래스입니다.
 */
public class GameController {
    private GameEngine gameEngine;
    private MainFrame view;
    private int lastYutResult = -1;
    private boolean isMoving = false;

    public GameController(GameEngine gameEngine, MainFrame view) {
        this.gameEngine = gameEngine;
        this.view = view;
        initializeGame();
    }

    private void initializeGame() {
        // 윷 던지기 버튼에 이벤트 리스너 추가
        view.getYutPanel().addThrowListener(e -> handleYutThrow());
        
        // 게임 시작
        gameEngine.start();
        updateGameState();
    }

    private void handleYutThrow() {
        if (!isMoving) {
            lastYutResult = gameEngine.throwYut();
            view.updateYutResult(lastYutResult);
            view.getGameInfoPanel().addGameLog(String.format(
                "플레이어 %d가 윷을 던져 '%s'가 나왔습니다.",
                gameEngine.getCurrentPlayer() + 1,
                getYutResultText(lastYutResult)
            ));
            isMoving = true;
            view.getYutPanel().setThrowButtonEnabled(false);
        }
    }

    public void handlePieceMove(int pieceIndex) {
        if (isMoving && lastYutResult != -1) {
            boolean success = gameEngine.movePiece(pieceIndex, lastYutResult + 1);
            if (success) {
                view.updateBoard();
                view.getGameInfoPanel().addGameLog(String.format(
                    "플레이어 %d의 말 %d번이 %d칸 이동했습니다.",
                    gameEngine.getCurrentPlayer() + 1,
                    pieceIndex + 1,
                    lastYutResult + 1
                ));
                
                // 승리 조건 체크
                int winner = gameEngine.checkWinCondition();
                if (winner != -1) {
                    view.showGameResult("플레이어 " + (winner + 1));
                    return;
                }
                
                isMoving = false;
                lastYutResult = -1;
                view.getYutPanel().setThrowButtonEnabled(true);
                updateGameState();
            }
        }
    }

    public void handlePieceCapture(int attackerPieceIndex, int targetPieceIndex) {
        boolean success = gameEngine.capturePiece(attackerPieceIndex, targetPieceIndex);
        if (success) {
            view.updateBoard();
            view.getGameInfoPanel().addGameLog(String.format(
                "플레이어 %d의 말이 상대방의 말을 잡았습니다!",
                gameEngine.getCurrentPlayer() + 1
            ));
            view.getGameInfoPanel().updateCaptureCount(
                gameEngine.getPlayerPieces(gameEngine.getCurrentPlayer())
            );
        }
    }

    private void updateGameState() {
        view.updatePlayerInfo(gameEngine.getCurrentPlayer());
        view.updateBoard();
    }

    private String getYutResultText(int result) {
        String[] results = {"도", "개", "걸", "윷", "모"};
        return (result >= 0 && result < results.length) ? results[result] : "알 수 없음";
    }
} 