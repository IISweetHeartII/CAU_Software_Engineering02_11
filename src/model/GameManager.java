package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class GameManager {
    // ------ manager ------ //
    private BoardManager boardManager = new BoardManager();
    private YutManager yutManager = new YutManager();

    // ------ fields ------ //
    private int numberOfPlayers;
    private int numberOfPieces;

    private int[] countOfPieceAtStart;    // START 위치의 말 개수 저장
    private int[] countOfPieceAtEnd;    // END 위치의 말 개수 저장

    private int currentPlayerIndex = 0; // 현재 플레이어 인덱스
    private int extraTurnCount = 0; // 추가 턴 횟수

    private ArrayDeque<Integer> yutHistory = new ArrayDeque<>();
    private Map<String, Piece> positionPieceMap = new HashMap<>();    // Key: Position, Value: Piece

    // ------ constructor ------ //
    public GameManager() {
        this.numberOfPlayers = loadPlayerCount();
        this.numberOfPieces = loadPieceCount();

        countOfPieceAtStart = new int[numberOfPlayers];
        countOfPieceAtEnd = new int[numberOfPlayers];

        // START 위치에 있는 말 개수 초기화
        for (int i = 0; i < numberOfPlayers; i++) {
            this.countOfPieceAtStart[i] = numberOfPieces;
        }
        // END 위치에 있는 말 개수 초기화
        for (int i = 0; i < numberOfPlayers; i++) {
            this.countOfPieceAtEnd[i] = 0;
        }
    }


    // ------ getter ------ //
    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }
    public int getNumberOfPieces() {
        return numberOfPieces;
    }
    public int getCurrentPlayer() {
        return currentPlayerIndex + 1;
    }
    public int[] getCountOfPieceAtStart() {
        return countOfPieceAtStart;
    }
    public Map<String, String> getPositionPieceMap() {
        Map<String, String> result = new HashMap<>();
        for (Map.Entry<String, Piece> entry : positionPieceMap.entrySet()) {
            result.put(entry.getKey(), entry.getValue().getId());
        }
        return result;
    }

    private String getCurrentPosition(Piece piece) {
        // piece의 현재 위치를 찾기 위해 positionPieceMap을 순회
        for (Map.Entry<String, Piece> entry : positionPieceMap.entrySet()) {
            if (entry.getValue().equals(piece)) {
                return entry.getKey();
            }
        }
        return null; // 위치를 찾지 못한 경우
    }

    // ------ is check ------ //
    public boolean isExtraMove() {
        return !(yutHistory.isEmpty());
    }

    public boolean isExtraTurn() {
        return extraTurnCount > 0;
    }

    public boolean isCurrentPlayersPiecePresent(String positionId) {
        // START일 경우 예외 처리
        if (positionId.equals("START")) {
            return countOfPieceAtStart[currentPlayerIndex] > 0;
        }
        // END일 경우 예외 처리
        if (positionId.equals("END")) {
            testMessage("GameManager: isCurrentPlayerPiecePresent: END 위치는 선택할 수 없습니다.");
            return false;
        }
        // 일반적인 경우
        Piece piece = positionPieceMap.get(positionId);
        if (piece == null) return false;
        return piece.getPlayerId().equals(String.valueOf(currentPlayerIndex + 1));
    }
    
    public boolean isValidMove(String startPosition, String targetPosition) {
        Piece DUMMY_PIECE;
        if (startPosition.equals("START"))
            DUMMY_PIECE = new Piece("DUMMY", boardManager.beforeEND);
        else
            DUMMY_PIECE = new Piece("DUMMY", startPosition);

        for (int move : new int[]{-1, 1, 2, 3, 4, 5}) {
            Position newPosition = boardManager.setPreviousPosition(new Position(startPosition), move, DUMMY_PIECE);
            if (newPosition != null && newPosition.equals(targetPosition) && yutHistory.contains(move)) {
                return true;
            }
        }
        return false;
    }

    // ------ move piece ------ //
    public void controlMovePiece(String startPosition, String targetPosition) {
        // piece 불러오기: 시작 위치가 START인 경우 예외 처리
        Piece piece;
        if (startPosition.equals("START")) {
            piece = new Piece(String.valueOf(currentPlayerIndex + 1), boardManager.beforeEND);
            // test
            positionPieceMap.put(startPosition, piece);
            countOfPieceAtStart[currentPlayerIndex]--;
        } else {
            piece = positionPieceMap.get(startPosition);
        }

        // 이동할 위치가 END인 경우 예외 처리
        if (targetPosition.equals("END")) {
            countOfPieceAtEnd[currentPlayerIndex]++;
            positionPieceMap.remove(startPosition);
            return;
        }

        int moveCount = getMoveCount(startPosition, targetPosition);
        if (moveCount == 0) {
            testMessage("GameManager: controlMovePiece: moveCount가 0입니다.");
            return;
        }

        // target 위치에서 captureOrGrouping 및 이동 처리
        switch (captureOrGrouping(startPosition, targetPosition)) {
            case NONE:
                // target 위치에 말이 없을 경우
                boardManager.setPreviousPosition(new Position(startPosition), moveCount, piece);
                positionPieceMap.remove(startPosition);
                positionPieceMap.put(targetPosition, piece);
                break;

            case CAPTURE:
                // target 위치에 상대 플레이어의 말이 있을 경우
                boardManager.setPreviousPosition(new Position(startPosition), moveCount, piece);
                positionPieceMap.remove(startPosition);
                positionPieceMap.put(targetPosition, piece);
                extraTurnCount++;
                break;

            case GROUPING:
                // target 위치에 현재 플레이어의 말이 있을 경우
                positionPieceMap.remove(startPosition);
                break;

            default:
                testMessage("game manager: controlMovePiece: 잘못된 moveType입니다.");
        }

        // 이동 후 윷 이력에서 이동한 칸 수 제거
        yutHistory.removeFirstOccurrence(moveCount);
    }

    private int getMoveCount(String startPosition, String targetPosition) {
        Piece DUMMY_PIECE;
        if (startPosition.equals("START"))
            DUMMY_PIECE = new Piece("DUMMY", boardManager.beforeEND);
        else
            DUMMY_PIECE = new Piece("DUMMY", startPosition);
        for (int move : new int[]{-1, 1, 2, 3, 4, 5}) {
            Position newPosition = boardManager.setPreviousPosition(new Position(startPosition), move, DUMMY_PIECE);
            if (newPosition != null && newPosition.equals(targetPosition) && yutHistory.contains(move)) {
                return move;
            }
        }
        return 0;
    }

    private static final int NONE = 0;
    private static final int CAPTURE = 1;
    private static final int GROUPING = 2;
    private int captureOrGrouping(String startPosition, String targetPosition) {
        testMessage("GameManager: captureOrGrouping: startPosition -> " + startPosition + ", targetPosition -> " + targetPosition);
        Piece piece;
        if (startPosition.equals("START")) {
            piece = new Piece(String.valueOf(currentPlayerIndex + 1), startPosition);
        } else {
            piece = positionPieceMap.get(startPosition);
        }
        Piece targetPiece = positionPieceMap.get(targetPosition);

        // target 위치에 있는 말이 없을 경우
        if (targetPiece == null) {
            testMessage("GameManager: captureOrGrouping: NONE");
            return NONE;
        }
        // target 위치에 있는 말이 현재 플레이어의 말일 경우
        if (targetPiece.getPlayerId().equals(piece.getPlayerId())) {
            // 그룹화 처리
            piece.group(targetPiece);
            positionPieceMap.remove(startPosition);
            positionPieceMap.replace(targetPosition, piece);
            testMessage("GameManager: captureOrGrouping: GROUPING");
            return GROUPING;
        }
        // target 위치에 있는 말이 상대 플레이어의 말일 경우
        else {
            // 상대 플레이어의 말 제거 처리 = targetPosition의 위치에 있는 말 제거
            positionPieceMap.remove(targetPosition);
            countOfPieceAtStart[Integer.parseInt(targetPiece.getPlayerId()) - 1] += targetPiece.getSize();
            testMessage("GameManager: captureOrGrouping: CAPTURE");
            return CAPTURE;
        }
    }


    // ------ throw yut ------ //
    public YutResult throwYutRandom() {
        if (extraTurnCount > 0) {
            extraTurnCount--;
        }
        YutResult yutResult = yutManager.throwYutRandom();
        yutHistory.add(yutResult.getValue());
        if (yutResult.isExtraTurn()) {
            extraTurnCount++;
        }
        return yutResult;
    }
    public YutResult throwYutManual(int value) {
        if (extraTurnCount > 0) {
            extraTurnCount--;
        }
        YutResult yutResult = yutManager.throwYutCustom(value);
        yutHistory.add(yutResult.getValue());
        if (yutResult.isExtraTurn()) {
            extraTurnCount++;
        }
        return yutResult;
    }

    // ------ turn management ------ //
    public int getCurrentPlayerNumber() {
        return currentPlayerIndex + 1;
    }

    public void nextTurn() {
        if (!yutHistory.isEmpty()) {
            System.out.println("턴을 넘길 수 없습니다. 처리되지않은 남은 윷이 있습니다.");
            return;
        }
        if (extraTurnCount > 0) {
            System.out.println("턴을 넘길 수 없습니다. 추가 윷이 남아있습니다.");
            return;
        }
        currentPlayerIndex = (currentPlayerIndex + 1) % numberOfPlayers;
    }

    public boolean isGameEnd() {
        for (int i = 0; i < numberOfPlayers; i++) {
            if (countOfPieceAtEnd[i] == numberOfPieces) {
                System.out.println("플레이어 " + (i + 1) + "가 승리했습니다.");
                return true;
            }
        }
        return false;
    }


    // ------ print for test ------ //
    public void printYutHistory() {
        testMessage("yutHistory: " + yutHistory);
    }


    // ------ file load ------ //
    private int loadPlayerCount() {
        try (Scanner scanner = new Scanner(new File("src/data/config.txt"))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.startsWith("player:")) {
                    String[] parts = line.split(":");
                    if (parts.length == 2) {
                        return Integer.parseInt(parts[1].trim());
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.err.println("piece 값이 올바르지 않습니다.");
        }
        return 4; // 기본값
    }

    private int loadPieceCount() {
        try (Scanner scanner = new Scanner(new File("src/data/config.txt"))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.startsWith("piece:")) {
                    String[] parts = line.split(":");
                    if (parts.length == 2) {
                        return Integer.parseInt(parts[1].trim());
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.err.println("piece 값이 올바르지 않습니다.");
        }
        return 5; // 기본값
    }

    // ------ print for test ------ //
    boolean isTestMode = true;
    public void testMessage(String message) {
        if (isTestMode) {
            System.out.println(message);
        }
    }
}