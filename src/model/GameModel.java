package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class GameModel {
    // ------ fields ------ //
    protected int numberOfPlayers;
    protected int numberOfPieces;

    protected Player[] players;
    protected int[] gameScores;
    private int[] notStartedCount; // 각 플레이어의 START에 있는 말의 개수

    protected int currentPlayerIndex = 0;

    protected int extraTurnCount = 0; // 추가 턴 수

    protected Yut yut = new Yut();
    protected ArrayDeque<YutResult> yutResultArrayDeque = new ArrayDeque<>();
    protected ArrayDeque<Piece> markers = new ArrayDeque<>();

    private Map<String, String> piecePositionsMap = new HashMap<>(); // key: pieceId, value: positionId
    private Map<Piece, YutResult> markerYutMap = new HashMap<>(); // key: marker, value: yutResult

    // ------ constructor ------ //
    public GameModel() {
        this.numberOfPlayers = loadPlayerCount();
        this.numberOfPieces = loadPieceCount();

        this.players = new Player[numberOfPlayers];
        for (int i = 0; i < numberOfPlayers; i++) {
            players[i] = new Player("" + (i + 1), numberOfPieces);
        }

        this.gameScores = new int[numberOfPlayers];
        for (int i = 0; i < numberOfPlayers; i++) {
            gameScores[i] = 0;
        }

        this.notStartedCount = new int[numberOfPlayers];
        updatePiecePositionsMap();
        updatePlayersScore();
        updateNotStartedCount();
    }
    
    // ------ getters & setters ------ //
    private Player getCurrentPlayer() {
        return players[currentPlayerIndex];
    }

    private static final int PLAYER_INDEX_OFFSET = 1; // 플레이어 번호는 1부터 시작
    public int getCurrentPlayerNumber() {
        return currentPlayerIndex + PLAYER_INDEX_OFFSET;
    }
    
    public Map<String, String> getPiecePositionsMap() {
        return piecePositionsMap;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public int getNumberOfPieces() {
        return numberOfPieces;
    }

    public int[] getNotStartedCount() {
        return notStartedCount;
    }
    // ------------------------------- //


    // ------------------------------- methods ------------------------------- //
    public boolean initGame() {
        try {
            // Reset collections
            yutResultArrayDeque.clear();
            markers.clear();
            piecePositionsMap.clear();
            markerYutMap.clear();

            // Reset game state
            currentPlayerIndex = 0;
            extraTurnCount = 0;

            // Reset players
            for (int i = 0; i < numberOfPlayers; i++) {
                players[i] = new Player("" + (i + 1), numberOfPieces);
                gameScores[i] = 0;
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // ------------ movePiece ------------ //
    // sequence
    // 1. controller -> model.checkCurrentPlayerPieceAt(nodeId) -> [refactor] targetNodeId
    // 2. controller -> model.checkCanMoveTo(nodeId) -> [refactor] targetNodeId
    // 3. controller -> model.handleMovePiece(selectedPieceId, targetNodeId)
    
    public void handleMovePiece(String selectedPieceId, String targetNodeId) { // <--- controller
        // Todo: 
        // marker를 생성
        markers.clear();
        setMarkers();
        
        // 이동할 말 찾기
        Piece selectedPiece = getPieceById(selectedPieceId);
        if (selectedPiece == null) {
            System.out.println("해당 말이 존재하지 않습니다.");
            return;
        }

        // 이동할 위치에 있는 marker 찾기
        Piece targetMarker = getMarkerAt(new Position(targetNodeId));
        if (targetMarker == null) {
            System.out.println("해당 위치에 이동할 수 있는 marker가 없습니다.");
            return;
        }

        // 말 이동하고 사용한 윷 결과 제거
        selectedPiece.moveTo(markerYutMap.get(targetMarker).getValue());
        yutResultArrayDeque.remove(markerYutMap.get(targetMarker));
        markers.remove(targetMarker);
        markerYutMap.remove(targetMarker);

        // 이동한 위치에서 자신의 말을 합칠 수 있는지 확인 : grouping
        // Todo:

        // 이동한 위치에서 상대편 말을 잡을 수 있는지 확인 : catch
        // Todo:

        // 최종 위치 업데이트
        piecePositionsMap.put(selectedPiece.getPieceId(), targetNodeId); // 이동한 말의 위치 업데이트
        updatePiecePositionsMap(); // 말의 위치 업데이트
        updatePlayersScore(); // 플레이어 점수 업데이트
        updateNotStartedCount(); // 플레이어의 START에 있는 말의 개수 업데이트
    }

    private void setMarkers() {
        ArrayDeque<Piece> pieces = new ArrayDeque<>(getCurrentPlayer().getPieces()); // copy

        // 현재 플레이어에 대한 시작 위치를 각 movablePiece의 위치로 하는 PositionPiece 생성
        for (Piece piece : pieces) {
            if (piece.isArrived()) continue; // 이미 도착한 말은 제외

            // 가상 말을 생성하여 이동할 수 있는 위치를 계산
            for (YutResult yutResult : yutResultArrayDeque) {
                Piece marker = new Piece(piece); // 깊은 복사
                marker.moveTo(yutResult.getValue());
                markers.add(marker);
                markerYutMap.put(marker, yutResult); // marker와 yutResult를 매핑
            }
        }

        ArrayDeque<Position> posableMoves = new ArrayDeque<>();
        for (Piece piece : markers) {
            posableMoves.add(piece.getCurrentPosition());
        }
    }


    // grouping //
    // 특정 위치에 있는 말을 그룹화
    public boolean checkGrouping(Piece movedPiece, Position position) {
        // Todo:
        return false;
    }

    // catch opponent's piece //
    public boolean checkCatch(Piece movedPiece, Position position) {
        // Todo:
        return false;
    }

    public void initializeGrouping(Piece piece) {
        // Todo:
    }

    private void updatePiecePositionsMap() {
        // Todo:
    }

    private Piece findPieceAt(Position position) {
        // Todo:
        return null;
    }
    
    // ------------ update ------------ //
    public void updatePlayersScore() {
        int playerIndex = 0;
        for (Player player : players) {
            gameScores[playerIndex++] = calculatePlayerScore(player);
        }
    }

    private int calculatePlayerScore(Player player) {
        return (int) player.getPieces()
                .stream()
                .filter(Piece::isArrived)
                .count();
    }
    
    public void updateNotStartedCount() {
        for (int i = 0; i < numberOfPlayers; i++) {
            notStartedCount[i] = players[i].getNotStartedCount();
        }
    }

    // ------------ throw yut ------------ //
    public YutResult throwAndSaveYut() {
        if (extraTurnCount > 0) extraTurnCount--;
        YutResult yutResult = yut.throwYut();
        yutResultArrayDeque.add(yutResult);
        if (yutResult.isExtraTurn()) {
            extraTurnCount++;
        }
        return yutResult;
    }

    public YutResult throwAndSaveYut(int n) {
        if (extraTurnCount > 0) extraTurnCount--;
        YutResult yutResult = yut.throwYut(n);
        yutResultArrayDeque.add(yutResult);
        if (yutResult.isExtraTurn()) {
            extraTurnCount++;
        }
        return yutResult;
    }

    // ------ change turn ------ //
    public boolean changeTurn() {
        boolean state = true;
        currentPlayerIndex = (currentPlayerIndex + 1) % numberOfPlayers;
        if (extraTurnCount != 0) state = false;
        return state;
    }

    // ----------- is ------------ //  
    public boolean isExtraTurn() {
        return extraTurnCount > 0;
    }
    
    public boolean isALlMoved() {
        return yutResultArrayDeque.isEmpty();
    }

    public boolean isGameEnd() {
        for (int score : gameScores) {
            if (score >= numberOfPieces) {
                return true; // 게임 종료
            }
        }
        return false;
    }

    // ------------ get at ------------ //
    private Piece getPieceAtPosition(Position position) {
        for (Player player : players) {
            for (Piece piece : player.getPieces()) {
                if (piece.getCurrentPosition().equals(position)) {
                    return piece; // 해당 위치에 있는 MovablePiece 반환
                }
            }
        }
        return null;
    }

    public String getPieceIdAtNodeId(String piecePositionId) {
        Position position = new Position(piecePositionId);
        for (Player player : players) {
            for (Piece piece : player.getPieces()) {
                if (piece.getCurrentPosition().equals(position)) {
                    return piece.getPieceId();
                }
            }
        }
        return null;
    }

    public Piece getMarkerAt(Position position) {
        for (Piece piece : markers) {
            if (piece.getCurrentPosition().equals(position)) {
                return piece; // 해당 위치에 있는 Piece 반환
            }
        }
        return null; // 해당 위치에 Piece가 없음
    }
    
    // ------------ get by Id ------------ //
    private Player getPlayerById(String playerID) {
        for (Player player : players) {
            if (player.getPlayerID().equals(playerID)) {
                return player;
            }
        }
        return null;
    }

    private Piece getPieceById(String pieceId) {
        for (Player player : players) {
            for (Piece piece : player.getPieces()) {
                if (piece.getPieceId().equals(pieceId)) {
                    return piece;
                }
            }
        }
        return null;
    }


    // ------------ used controller to check ------------ //
    public boolean isCurrentPlayersPiecePresent(String nodeId) { // <--- controller
        Position targetPosition = new Position(nodeId);
        return findPieceAtPosition(targetPosition).isPresent();
    }

    private Optional<Piece> findPieceAtPosition(Position position) {
        return getCurrentPlayer().getPieces()
                .stream()
                .filter(piece -> piece.getCurrentPosition().equals(position))
                .findFirst();
    }
    
    
    public boolean isPositionMovable(Position targetPosition) {
        markers.clear();
        setMarkers();
        return markers.stream()
                .anyMatch(piece -> piece.getCurrentPosition().equals(targetPosition));
    }

    public boolean isPositionMovable(String nodeId) {
        return isPositionMovable(new Position(nodeId));
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