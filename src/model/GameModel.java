package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class GameModel implements Model {
    // ------ fields ------ //
    protected int numberOfPlayers;
    protected int numberOfPieces;

    protected Player[] players;
    protected int[] gameScores;

    protected int currentPlayerIndex = 0;

    protected int extraTurnCount = 0; // 추가 턴 수

    protected Yut yut = new Yut();
    protected ArrayDeque<YutResult> yutResultArrayDeque = new ArrayDeque<>();
    protected ArrayDeque<Piece> markerArrayDeque = new ArrayDeque<>();

    private Map<String, String> piecePositionsMap = new HashMap<>(); // key: pieceId, value: positionId

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


    // ------ getters & setters ------ //
    @Override
    public Player getCurrentPlayer() {
        return players[currentPlayerIndex];
    }

    public int getPlayerTurn() {
        return currentPlayerIndex + 1;
    }

    @Override
    public int[] getGameScores() {
        return gameScores;
    }

    public ArrayDeque<YutResult> getYutResultDeque() {
        return yutResultArrayDeque;
    }//Controller에서 윷 결과를 가져가기 위한 메서드 추가

    public Map<String, String> getPiecePositionsMap() {
        return piecePositionsMap;
    }
    // ------------------------------- //


    // ------ methods ------ //
    // 게임 상태 초기화 //
    @Override
    public boolean initializeGame() { // try catch로 예외 처리를 해야하지만 생략, 항상 true를 반환
        for (int i = 0; i < numberOfPlayers; i++) {
            players[i] = new Player("" + (i+1), numberOfPieces);
        }
        for (int i = 0; i < players.length; i++) {
            gameScores[i] = 0;
        }

        yutResultArrayDeque.clear(); // 윷 결과 초기화
        markerArrayDeque.clear(); // 위치 조각 초기화
        piecePositionsMap.clear(); // 위치 조각 초기화

        currentPlayerIndex = 0; // 현재 플레이어 인덱스 초기화
        extraTurnCount = 0; // 추가 턴 수 초기화

        return true; // 게임 초기화 성공
    }

    // ------ movePiece ------ //
    private Piece getSelectedPieceAt(String selectedPieceId) { // <--- controller
        Piece selectedPiece = null;
        selectedPiece = getCurrentPlayer().getMovablePieceAt(new Position(selectedPieceId));
        if (selectedPiece == null) {
            System.out.println("이동할 수 없는 말입니다.");
            return null;
        }
        return selectedPiece;
    }

    public boolean handleMovePiece(String selectedPieceId, String selectedNodeId) { // <--- controller
        markerArrayDeque.clear();
        getPosableMoves();

        Piece selectedPiece = getSelectedPieceAt(selectedPieceId);
        Piece marker = null;
        for (Piece piece : markerArrayDeque) {
            if (piece.getCurrentPosition().equals(new Position(selectedNodeId))) {
                marker = piece;
                break;
            }
        }
        if (marker == null) {
            System.out.println("이동할 수 없는 위치입니다.");
            return false;
        }

        selectedPiece = new Piece(marker);

        // grouping check
        checkGrouping(selectedPiece, new Position(selectedNodeId));

        // catch check

        checkCatch(selectedPiece, new Position(selectedNodeId));

        updatePiecePositionsMap();

        Player currentPlayer = getCurrentPlayer();
        currentPlayer.updateNotStartedCount();

        markerArrayDeque.clear();
        yutResultArrayDeque.removeFirstOccurrence(markerMap.get(marker));
        return true;
    }

    @Override
    public boolean movePiece(Piece piece, Position selectedPosition) {

        return false; // 이동 불가능한 경우
    }

    private Map<Piece, YutResult> markerMap = new HashMap<>();

    private ArrayDeque<Position> getPosableMoves() {
        ArrayDeque<Piece> pieces = new ArrayDeque<>(getCurrentPlayer().getPieces()); // copy

        // 현재 플레이어에 대한 시작 위치를 각 movablePiece의 위치로 하는 PositionPiece 생성
        for (Piece piece : pieces) {
            if (piece.isArrived()) continue; // 이미 도착한 말은 제외

            // 가상 말을 생성하여 이동할 수 있는 위치를 계산
            for (YutResult yutResult : yutResultArrayDeque) {
                Piece marker = new Piece(piece); // 깊은 복사
                marker.moveTo(yutResult.getValue());
                markerArrayDeque.add(marker);
                markerMap.put(marker, yutResult); // marker와 yutResult를 매핑
            }
        }

        ArrayDeque<Position> posableMoves = new ArrayDeque<>();
        for (Piece piece : markerArrayDeque) {
            posableMoves.add(piece.getCurrentPosition());
        }

        return posableMoves;
    }

    // grouping //
    // 특정 위치에 있는 말을 그룹화
    public boolean checkGrouping(Piece movedPiece, Position position) {
        // 현재 플레이어 정보 가져오기
        Player currentPlayer = getCurrentPlayer();
        String currentPlayerID = currentPlayer.getPlayerID();

        // 이동할 말의 플레이어 ID 확인
        String movedPieceID = movedPiece.getUnitArrayDeque().peekFirst() != null ? movedPiece.getPlayerID() : null;

        // 이동한 말이 현재 플레이어의 말이 아니면 종료
        if (movedPieceID != null && !movedPieceID.equals(currentPlayerID)) return false;

        // 해당 위치에 있는 Piece 가져오기
        Piece targetPiece = currentPlayer.getMovablePieceAt(position);

        // 상대편 말이 존재하고, 현재 플레이어의 말인 경우
        if (targetPiece != null && targetPiece.getPlayerID().equals(currentPlayerID)) {
            // 그룹화
            targetPiece.getUnitArrayDeque().addAll(movedPiece.getUnitArrayDeque());
            targetPiece.size += movedPiece.size; // 그룹화된 크기 업데이트
            currentPlayer.pieces.removeFirstOccurrence(movedPiece);
            targetPiece.updatePieceId(); // 그룹화된 MovablePiece의 ID 업데이트
            return true; // 그룹화 성공
        }

        return false;
    }

    // catch opponent's piece //
    public boolean checkCatch(Piece movedPiece, Position position) {
        // 현재 플레이어 정보 가져오기
        Player currentPlayer = getCurrentPlayer();
        String currentPlayerID = currentPlayer.getPlayerID();

        // 이동한 말의 플레이어 ID 확인
        String movedPieceID = movedPiece.getUnitArrayDeque().peekFirst() != null ? movedPiece.getPlayerID() : null;

        // 이동한 말이 현재 플레이어의 말이 아니면 종료
        if (movedPieceID == null || !movedPieceID.equals(currentPlayerID)) {
            return false;
        }

        // 해당 위치에 있는 Piece 가져오기
        Piece targetPiece = getPieceAt(position);

        // 상대편 말이 존재하고, 현재 플레이어의 말이 아닌 경우
        if (targetPiece != null && !targetPiece.getPlayerID().equals(currentPlayerID)) {

            Player opponentPlayer = getPlayerByID(targetPiece.getPlayerID());
            if (opponentPlayer != null) {
                initializeGrouping(targetPiece);
            }
            extraTurnCount++;

            return true; // 상대편 말을 잡은 경우
        }

        return false; // 상대편 말을 잡지 못한 경우
    }

    public void initializeGrouping(Piece piece) {
        // 소유자 가져오기
        Player owner = getPlayerByID(piece.getPlayerID());
        // 소유자가 있을 때
        if (owner != null) {
            // 잡힌 말 제거
            owner.getPieces().remove(piece);
            // 그룹 해제된 Piece를 다시 추가
            for (int i = 0; i < piece.size; i++) {
                Unit unit = new Unit(owner.getPlayerID(), owner.getPlayerID());
                Piece newPiece = new Piece(unit);
                owner.getPieces().add(newPiece);
            }
            owner.updateNotStartedCount();
        }
    }

    private void updatePiecePositionsMap() {
        piecePositionsMap.clear();
        for (Player player : players) {
            for (Piece piece : player.getPieces()) {
                String pieceID = piece.getUnitArrayDeque().peekFirst() != null ? piece.getPieceId() : null;
                if (pieceID != null) {
                    piecePositionsMap.put(pieceID, piece.getCurrentPosition().getId());
                }
            }
        }
    }

/*    public boolean movePieces(Position selectedPosition) {
        Unit selectedPositionUnit = findPositionPieceAt(selectedPosition);

        Unit copyOfSelectedPositionUnit = new Unit(selectedPositionUnit);
        copyOfSelectedPositionUnit.recentPath.removeFirst(); // 초기 빽도 값 제거 -> 원래 위치

        Piece selectedPiece = findMovablePieceAt(selectedPosition);

        if (selectedPiece.isArrived()) return false; // 이미 도착한 말은 이동할 수 없음
        if (selectedPositionUnit == null) return false; // 해당 위치로 이동할 수 없음

        if (checkGrouping(selectedPiece, selectedPosition)) return true; // 경로를 target이 이미 저장하고 있으므로
        if (checkCatch(selectedPiece, selectedPosition)) extraTurnCount++;

        int yutSize;
        selectedPiece.moveTo(-1); // 빽도인데 이동할 위치와 같다면 결과도 빽도
        if (selectedPositionUnit.currentPosition.equals(selectedPiece.currentPosition)) {
            yutSize = -1; // 빽도
        } else {
            // target의 최근 위치 변화를 통해 윷 결과를 역추적
            selectedPiece.moveTo(-1);
            yutSize = selectedPositionUnit.recentPath.size() - 3; // DO라면, [0 -1 0 1] -> 3개
            selectedPiece.moveTo(yutSize);
        }

        // 이동한 말이 END에 도착했을 때 점수를 추가하고 그룹화된 말들을 Player -> MovablePieces에서 제거
        Player currentPlayer = getCurrentPlayer();
        if (selectedPiece.isArrived()) {
            for (Piece piece : currentPlayer.getPieces()) {
                if (piece.isArrived()) {
                    int groupSize = piece.size;
                    currentPlayer.getPieces().remove(piece);
                    gameScores[currentPlayerIndex] += groupSize; // 그룹화된 말의 개수만큼 점수 추가
                    currentPlayer.notArrivedCount -= groupSize; // 도착하지 않은 말의 개수 감소
                }
            }
        }

        // PositionPieceArrayDeque 초기화
        markerArrayDeque.clear();
        // 사용한 윷 제거
        YutResult yutResult = yut.throwYut(yutSize);
        yutResultArrayDeque.removeFirstOccurrence(yutResult);

        return true;
    }*/

    private Piece findMovablePieceAt(Position position) {
        for (Player player : players) {
            for (Piece piece : player.getPieces()) {
                if (piece.getCurrentPosition().equals(position)) {
                    return piece; // 해당 위치에 있는 MovablePiece 반환
                }
            }
        }
        return null; // 해당 위치에 MovablePiece가 없음
    }

    @Override
    public int[] getNotStartedCount() {
        // playerIndex의 Piece 중 "START"에 위치한 Piece의 개수를 반환
        int[] notStartedCount = new int[numberOfPlayers];
        for (int i = 0; i < players.length; i++) {
            Player player = players[i];
            int count = 0;
            for (Piece piece : player.getPieces()) {
                if (piece.getCurrentPosition().equals(new Position("START"))) {
                    count++;
                }
            }
            notStartedCount[i] = count;
        }
        return notStartedCount;
    }

    @Override
    public ArrayDeque<Piece> getAllMovablePieces() {
        ArrayDeque<Piece> allPieces = new ArrayDeque<>();
        for (Player player : players) {
            allPieces.addAll(player.getPieces());
        }
        return allPieces;
    }

    // ------ throw yut ------ //
    @Override
    public YutResult throwAndSaveYut() {
        if (extraTurnCount > 0) extraTurnCount--;
        YutResult yutResult = yut.throwYut();
        yutResultArrayDeque.add(yutResult);
        if (yutResult.isExtraTurn()) {
            extraTurnCount++;
        }
        return yutResult;
    }

    @Override
    public YutResult throwAndSaveYut(int n) {
        if (extraTurnCount > 0) extraTurnCount--;
        YutResult yutResult = yut.throwYut(n);
        yutResultArrayDeque.add(yutResult);
        if (yutResult.isExtraTurn()) {
            extraTurnCount++;
        }
        return yutResult;
    }

    @Override
    public YutResult throwAndSaveYut(String input) {
        if (extraTurnCount > 0) extraTurnCount--;
        YutResult yutResult = yut.throwYut(input);
        yutResultArrayDeque.add(yutResult);
        if (yutResult.isExtraTurn()) {
            extraTurnCount++;
        }
        return yutResult;
    }

    @Override
    public boolean isExtraTurn() {
        return extraTurnCount > 0;
    }

    /// 내부에서 사용할 메서드 ///
    public Piece findMarkerAt(Position position) {
        for (Piece piece : markerArrayDeque) {
            if (piece.getCurrentPosition().equals(position)) {
                return piece; // 해당 위치에 있는 Piece 반환
            }
        }
        return null; // 해당 위치에 Piece가 없음
    }

    @Override
    public boolean changeTurn() {
        boolean state = true;
        currentPlayerIndex = (currentPlayerIndex + 1) % numberOfPlayers;
        if (extraTurnCount != 0) state = false;
        return state;
    }

    private Piece getPieceAt(Position position) {
        for (Player player : players) {
            for (Piece piece : player.getPieces()) {
                if (piece.getCurrentPosition().equals(position)) {
                    return piece; // 해당 위치에 있는 MovablePiece 반환
                }
            }
        }
        return null; // 해당 위치에 MovablePiece가 없음
    }

    private Player getPlayerByID(String playerID) {
        for (Player player : players) {
            if (player.getPlayerID().equals(playerID)) {
                return player;
            }
        }
        return null;
    }

    // ------ add score ------ //
    public void addScore(int playerIndex) {
        this.gameScores[playerIndex] += 1;
    }

    public void addScore(Player currentPlayer) {
        for (int i = 0; i < players.length; i++) {
            if (players[i].equals(currentPlayer)) {
                addScore(i);
                break;
            }
        }
    }

    @Override
    public boolean isGameEnd() {
        for (int score : gameScores) {
            if (score >= numberOfPieces) {
                return true; // 게임 종료
            }
        }
        return false;
    }

    public boolean checkCurrentPlayerPieceAt(String nodeId) { // <--- controller
        Position position = new Position(nodeId);
        for (Piece piece : getCurrentPlayer().getPieces()) {
            if (piece.getCurrentPosition().equals(position)) {
                return true; // 현재 플레이어의 말이 해당 위치에 있음
            }
        }
        return false; // 현재 플레이어의 말이 해당 위치에 없음
    }

    public boolean checkPosableMoves(String nodeId) { // <--- controller
        Position position = new Position(nodeId);

        markerArrayDeque.clear();
        getPosableMoves(); // 이동 가능한 위치를 가져옴

        for (Piece piece : markerArrayDeque) {
            if (piece.getCurrentPosition().equals(position)) {
                return true; // 이동 가능한 위치에 있음
            }
        }
        return false; // 이동 가능한 위치에 없음
    }
}