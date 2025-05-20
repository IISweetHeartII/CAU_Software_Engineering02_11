package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class GameManager {
    // ------ fields ------ //
    protected int numberOfPlayers;
    protected int numberOfPieces;
    private int extraTurnCount = 0; // 추가 턴 횟수

    private Board board = new Board();
    private YutManager yutManager = new YutManager();

    private int currentPlayerIndex = 0; // 현재 플레이어 인덱스
    private ArrayDeque<Integer> yutHistory = new ArrayDeque<>();

    // Key: Position, Value: Piece 
    public Map<String, Piece> positionPieceMap = new HashMap<>();

    // START/END 위치의 piece들의 id 저장
    public Map<String, Set<String>> positionPieceIdMap = new HashMap<>();

    // START 위치의 말 개수 저장
    public int[] countOfPieceAtStart;

    // END 위치의 말 개수 저장  
    public int[] countOfPieceAtEnd;

    // ------ constructor ------ //
    public GameManager() {
        this.numberOfPlayers = loadPlayerCount();
        this.numberOfPieces = loadPieceCount();

        countOfPieceAtStart = new int[numberOfPlayers];
        countOfPieceAtEnd = new int[numberOfPlayers];

        positionPieceIdMap.put("START", new HashSet<>());
        positionPieceIdMap.put("END", new HashSet<>());

        // 초기화: 모든 말을 START에 위치
        for (int i = 0; i < numberOfPlayers; i++) {
            String playerId = String.valueOf(i + 1);
            countOfPieceAtStart[i] = numberOfPieces; // START 위치 카운트 초기화

            for (int j = 0; j < numberOfPieces; j++) {
                Piece piece = new Piece(playerId, board.beforeEND);
                positionPieceIdMap.get("START").add(piece.getId());
            }
        }
    }

    // ------ getters ------ //
    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public int getNumberOfPieces() {
        return numberOfPieces;
    }

    public Map<String, String> getPiecePositionsMap() {
        Map<String, String> result = new HashMap<>();
        // 일반 위치의 말들
        for (Map.Entry<String, Piece> entry : positionPieceMap.entrySet()) {
            result.put(entry.getValue().getId(), entry.getKey());
        }
        // START/END 위치의 말들
        for (Map.Entry<String, Set<String>> entry : positionPieceIdMap.entrySet()) {
            for (String pieceId : entry.getValue()) {
                result.put(pieceId, entry.getKey());
            }
        }
        return result;
    }

    public int getExtraTurnCount() {
        return extraTurnCount;
    }

    public int[] getNotStartedPiecesCount() {
        return countOfPieceAtStart;
    }

    // ------ move piece ------ //
    public int INVALID_MOVE_COUNT = 99;

    public void controlMovePiece(String piecePositionId, String targetPositionId) {
        Piece piece = getPieceByPositionId(piecePositionId);
        if (piece == null) {
            System.out.println("해당 위치에 말이 없습니다.");
            return;
        }

        int moveCount = getMoveCount(piece, targetPositionId);
        if (moveCount == INVALID_MOVE_COUNT || !yutHistory.contains(moveCount)) {
            System.out.println("이동할 수 없는 위치입니다.");
            return;
        }

        movePiece(piece, moveCount, piecePositionId, targetPositionId);
        yutHistory.remove(moveCount);
    }

    private int getMoveCount(Piece piece, String targetPositionId) {
        if (targetPositionId.equals("START")) {
            return INVALID_MOVE_COUNT;
        }

    }

    private void movePiece(Piece piece, int moveCount, String currentPositionId, String targetPositionId) {
        // START에서 이동하는 경우
        if (currentPositionId.equals("START")) {
            int playerIndex = Integer.parseInt(piece.getPlayerId()) - 1;
            countOfPieceAtStart[playerIndex]--;
            positionPieceIdMap.get("START").remove(piece.getId());

            if (!targetPositionId.equals("END")) {
                positionPieceMap.put(targetPositionId, piece);
            } else {
                positionPieceIdMap.get("END").add(piece.getId());
                countOfPieceAtEnd[playerIndex] += piece.getSize();
            }
        }
        // 일반 위치에서 이동하는 경우  
        else {
            positionPieceMap.remove(currentPositionId);

            if (targetPositionId.equals("END")) {
                int playerIndex = Integer.parseInt(piece.getPlayerId()) - 1;
                countOfPieceAtEnd[playerIndex] += piece.getSize();
                positionPieceIdMap.get("END").add(piece.getId());
            } else {
                // 이동할 위치에 다른 말이 있는 경우
                Piece otherPiece = positionPieceMap.get(targetPositionId);
                if (otherPiece != null) {
                    captureOrGroup(piece, otherPiece, targetPositionId);
                } else {
                    positionPieceMap.put(targetPositionId, piece);
                }
            }
        }
    }

    private void captureOrGroup(Piece me, Piece other, String position) {
        if (me.isSamePlayers(other)) {
            me.group(other);
            positionPieceMap.put(position, me);
        } else {
            String playerId = other.getPlayerId();
            int playerIndex = Integer.parseInt(playerId) - 1;
            int otherSize = other.getSize();

            // 잡힌 말을 START로 이동
            countOfPieceAtStart[playerIndex] += otherSize;
            positionPieceMap.remove(position);
            positionPieceMap.put(position, me);
            extraTurnCount++;
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


    // ------ get by at ------ //
    private Piece getPieceByPositionId(String position) {
        if (position.equals("START")) {
            String currentPlayerId = String.valueOf(currentPlayerIndex + 1);
            for (String pieceId : positionPieceIdMap.get("START")) {
                if (pieceId.startsWith(currentPlayerId)) {
                    return new Piece(currentPlayerId, board.beforeEND);
                }
            }
            return null;
        }
        return positionPieceMap.get(position);
    }

    public boolean isCurrentPlayersPiecePresent(String position) {
        // Todo:
    }

    public boolean isValidMove(String pieceId, String targetPositionId) {
        // Todo:
        return false;
    }

    public boolean isExtraTurn() {
        return extraTurnCount > 0;
    }

    // ------ test ------ //
    public void printPiecePositionMap() {
        // Todo:
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
}