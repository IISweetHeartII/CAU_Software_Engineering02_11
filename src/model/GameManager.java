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
    public Map<Piece, String> piecePositionMap = new HashMap<>(); // Key: Piece, Value: Position

    // ------ constructor ------ //
    public GameManager() {
        this.numberOfPlayers = loadPlayerCount();
        this.numberOfPieces = loadPieceCount();

        for (int i = 0; i < numberOfPlayers; i++) {
            String playerId = String.valueOf(i + 1);
            for (int j = 0; j < numberOfPieces; j++) {
                // 처음 생성할 때는 START 위치에 존재하며, playerId와 pieceId가 동일합니다.
                // 예를 들어 "1"은 플레이어 1의 말 1개를 의미합니다.
                piecePositionMap.put(new Piece(playerId, board.beforeEND), "START");
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
        Map<String, String> positionsMap = new HashMap<>();
        for (Map.Entry<Piece, String> entry : piecePositionMap.entrySet()) {
            positionsMap.put(entry.getKey().getId(), entry.getValue());
        }
        return positionsMap;
    }

    // ------- initialize ------ //
    public void initialize() {
        // Todo: 게임 상태를 초기화 합니다.
    }

    // ------ move piece ------ //
    public int INVALID_MOVE_COUNT = 99;
    public void controlMovePiece(String piecePositionId, String targetPositionId) {
        // 움직일 말이 있는지 확인
        Piece piece = getPieceByPositionId(piecePositionId);
        if (piece == null) {
            System.out.println("해당 위치에 말이 없습니다.");
            return;
        }

        // 윷 결과 역추적
        int moveCount = getMoveCount(piece, targetPositionId);
        if (moveCount == INVALID_MOVE_COUNT) {
            return;
        }

        movePiece(piece, moveCount);
        yutHistory.remove(moveCount);

        if (extraTurnCount > 0) {
            extraTurnCount--;
        }
    }

    public int getMoveCount(Piece piece, String targetPositionId) {
        if (piece.getPreviousPosition().equals(targetPositionId)) {
            return -1;
        } else {
            for (int i = 1; i <= 5; i++) {
                Position targetPosition = board.getPositionByMoveCount(new Position(piecePositionMap.get(piece)), i);
                if (targetPosition.equals(targetPositionId)) {
                    return i;
                }
            }
        }
        System.out.println("이동할 수 없는 위치입니다.");
        return INVALID_MOVE_COUNT;
    }

    public void movePiece(Piece piece, int moveCount) {
        Position targetPosition;
        Position currentPosition = piecePositionMap.get(piece) == null ? null : new Position(piecePositionMap.get(piece));

        // 이동할 위치를 계산하고 이동
        if (moveCount == -1) {
            // 빽도인 경우
            targetPosition = piece.getPreviousPosition();
            piecePositionMap.remove(piece);
            piece.setPreviousPosition(piecePositionMap.get(piece));
            piecePositionMap.put(piece, targetPosition.getId());
        }
        else {
            // 일반적인 경우
            targetPosition = board.getPositionByMoveCount(currentPosition, moveCount);
            moveByOneSteps(piece, targetPosition.getId());
        }

        // 이동할 위치에 다른 말이 있는지 확인
        Piece otherPiece = getPieceByPositionId(targetPosition.getId());
        if (otherPiece != null) {
            captureOrGroup(piece, otherPiece);
        }
    }

    private void moveByOneSteps(Piece piece, String targetPositionId) {
        Position previousPosition = piece.getPreviousPosition();
        Position nextPosition;
        Position targetPosition = new Position(targetPositionId);
        for (int i = 0; i < 5; i++) {
            nextPosition = board.getPositionByMoveCount(previousPosition, 1);
            piecePositionMap.remove(piece);
            piece.setPreviousPosition(piecePositionMap.get(piece));
            piecePositionMap.put(piece, nextPosition.getId());
            if (nextPosition.equals(targetPosition)) {
                return;
            }
        }
    }

    private void captureOrGroup(Piece me, Piece other) {
        if (other == null) {
            System.out.println("captureOrGroup: 실행됐지만 다른 말이 없습니다.");
            return;
        }
        if (me.isSamePlayers(other)) {
            // 같은 플레이어의 말이라면 그룹화
            me.group(other);
            piecePositionMap.remove(other);
        } else {
            // 말 잡기 설계 전략
            // 다른 플레이어의 말을 piecePositionMap에서 제거하고 START 위치로 size 만큼 재생성
            // 다른 플레이어의 말 정보 보관
            String playerId = other.getPlayerId();
            int otherSize = other.getSize();

            // 다른 플레이어의 말이라면 잡기
            piecePositionMap.remove(other);
            for (int i = 0; i < otherSize; i++) {
                Piece newPiece = new Piece(playerId, board.beforeEND);
                piecePositionMap.put(newPiece, "START");
            }
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
        for (Map.Entry<Piece, String> entry : piecePositionMap.entrySet()) {
            if (entry.getValue().equals(position)) {
                return entry.getKey();
            }
        }
        return null;
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