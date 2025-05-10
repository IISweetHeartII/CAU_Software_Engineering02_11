package Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.Scanner;

public class GameModel implements Model {
    /// Responsibilities
    /// 1. GameModel은 게임의 상태와 플레이어를 관리합니다.
    /// 2. GameModel은 플레이어의 턴을 관리합니다.
    /// 3. GameModel은 플레이어의 점수를 관리합니다.
    /// 4. GameModel은 게임의 규칙을 적용해 게임의 상태를 변경합니다.
    ///
    /// Sequence
    /// 0. 게임 초기화 및 게임 상태 반환하기
    /// 1<-6. 윷 던지고 결과 얻기
    /// 2. 결과를 이용해 이동할 수 있는 위치 계산하기
    /// 3. 이동할 수 있는 위치를 플레이어에게 보여주기
    /// 4. 플레이어가 선택한 위치로 말 이동하기
    /// 5. 이동한 말의 위치 처리하기
    /// 5-1. 이동한 말의 위치에 따라 그룹화 및 상대편 말 잡기
    /// 5-2. 이동한 말이 END에 도착했을 때 점수 추가하기
    /// 6->1. 추가 턴이 있을 때 1번으로 돌아가기
    /// 7. 추가 턴이 없을 때 다음 플레이어로 턴 넘기기

    /// fields ///
    protected final Board board = new Board();
    protected final Yut yut = new Yut();
    protected final Player[] players;
    protected final int numberOfPlayers;
    protected final int numberOfPieces;
    protected int currentPlayerIndex = 0;
    protected int[] gameScores;
    protected int extraTurnCount = 0; // 추가 턴 수
    protected ArrayDeque<YutResult> yutResultArrayDeque = new ArrayDeque<>();
    protected ArrayDeque<Piece> positionPieceArrayDeque = new ArrayDeque<>();

    /// Constructor ///
    public GameModel(int dummy1, int dummy2) {
        this.numberOfPlayers = loadPlayer();
        this.numberOfPieces = loadNumpiece();
        this.players = new Player[numberOfPlayers];
        this.gameScores = new int[numberOfPlayers];
        for (int i = 0; i < numberOfPlayers; i++) {
            players[i] = new Player("Player" + (i+1), numberOfPieces);
        }
        for (int i = 0; i < numberOfPlayers; i++) {
            gameScores[i] = 0;
        }
    }

    private int loadPlayer() {
        try (Scanner scanner = new Scanner(new File("src/data/player.txt"))) {
            if (scanner.hasNextInt()) {
                return scanner.nextInt();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return 4; // 기본값
    }

    private int loadNumpiece() {
        try (Scanner scanner = new Scanner(new File("src/data/numpiece.txt"))) {
            if (scanner.hasNextInt()) {
                return scanner.nextInt();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return 5; // 기본값
    }

    /// setters ///
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


    /// getters ///
    @Override
    public Player getCurrentPlayer() {
        return players[currentPlayerIndex];
    }

    public Player[] getPlayers() {
        return players;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public Player whoseTurn() {
        return getCurrentPlayer();
    }

    @Override
    public int[] getGameScores() {
        return gameScores;
    }

    public ArrayDeque<YutResult> getYutResultDeque() {
        return yutResultArrayDeque;
    }//Controller에서 윷 결과를 가져가기 위한 메서드 추가

    /// methods ///

    // 게임 상태 초기화 //
    @Override
    public boolean initializeGame() { // try catch로 예외 처리를 해야하지만 생략, 항상 true를 반환
        for (int i = 0; i < numberOfPlayers; i++) {
            players[i] = new Player("Player" + (i+1), numberOfPieces);
        }
        for (int i = 0; i < players.length; i++) {
            gameScores[i] = 0;
        }
        yutResultArrayDeque.clear(); // 윷 결과 초기화
        positionPieceArrayDeque.clear(); // 위치 조각 초기화
        currentPlayerIndex = 0; // 현재 플레이어 인덱스 초기화
        extraTurnCount = 0; // 추가 턴 수 초기화
        return true; // 게임 초기화 성공
    }

    /// Controller 또는 View에서 호출하는 메서드 -> game state 변경
    @Override
    public boolean movePiece(MovablePiece selectedPiece, Position selectedPosition) {
        Piece targetPositionPiece = findPositionPieceAt(selectedPosition);
        if (selectedPiece.isArrived()) return false; // 이미 도착한 말은 이동할 수 없음
        if (targetPositionPiece == null) return false; // 해당 위치로 이동할 수 없음

        if (groupPiecesAtPosition(selectedPiece, selectedPosition)) return true; // 경로를 target이 이미 저장하고 있으므로
        if (captureOpponentPiece(selectedPiece, selectedPosition)) extraTurnCount++;

        int targetSize;
        // 빽도라면 실제 말의 위치를 저장하지 않고 있음
        if (!targetPositionPiece.recentPath.contains(selectedPiece.currentPosition)) {
            selectedPiece.moveTo(-1);
            targetSize = -1;
        } else {
            // target의 최근 위치 변화를 통해 윷 결과를 역추적
            targetSize = targetPositionPiece.recentPath.size() - 2; // DO라면, [-1(빽도위치) 0(이동전위치) 1(이동할위치)] -> 3개
            selectedPiece.moveTo(targetSize);
        }

        // 이동한 말이 END에 도착했을 때 점수를 추가하고 그룹화된 말들을 Player -> MovablePieces에서 제거
        Player currentPlayer = getCurrentPlayer();
        if (selectedPiece.isArrived()) {
            for (MovablePiece movablePiece : currentPlayer.getMovablePieces()) {
                if (movablePiece.isArrived()) {
                    int groupSize = movablePiece.size;
                    currentPlayer.getMovablePieces().remove(movablePiece);
                    gameScores[currentPlayerIndex] += groupSize; // 그룹화된 말의 개수만큼 점수 추가
                    currentPlayer.notArrivedCount -= groupSize; // 도착하지 않은 말의 개수 감소
                }
            }
        }

        // PositionPieceArrayDeque 초기화
        positionPieceArrayDeque.clear();
        // 사용한 윷 제거
        YutResult yutResult = yut.throwYut(targetSize);
        yutResultArrayDeque.removeFirstOccurrence(yutResult);

        return true;
    }

    @Override
    public int getNotArrivedCount() {
        return getCurrentPlayer().getNotArrivedCount();
    }

    @Override
    public ArrayDeque<MovablePiece> getAllMovablePieces() {
        ArrayDeque<MovablePiece> allMovablePieces = new ArrayDeque<>();
        for (Player player : players) {
            allMovablePieces.addAll(player.getMovablePieces());
        }
        return allMovablePieces;
    }

    @Override
    public Player[] getAllPlayers() {
        return players;
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

    @Override
    public ArrayDeque<Position> getPosableMoves() {
        ArrayDeque<MovablePiece> movablePieces = getCurrentPlayer().getMovablePieces();
        // 현재 플레이어에 대한 시작 위치를 각 movablePiece의 위치로 하는 PositionPiece 생성
        int caseSize = yutResultArrayDeque.size();
        for (MovablePiece movablePiece : movablePieces) {
            if (movablePiece.isArrived()) continue; // 이미 도착한 말은 제외
            movablePiece.moveTo(-1);
            Position startPosition = movablePiece.getCurrentPosition();
            for (YutResult yutResult : yutResultArrayDeque) {
                Piece positionPiece = new Piece(movablePiece.getPlayerID(), movablePiece.getPieceArrayDeque().peekFirst().pieceID, startPosition);
                positionPiece.moveTo(1); // 빽도 반영하려고... 뒤 한칸을 시작 위치로 갔다가 앞으로 한 칸...
                positionPiece.moveTo(yutResult.getValue());
                positionPieceArrayDeque.add(positionPiece);
            }
        }

        ArrayDeque<Position> posableMoves = new ArrayDeque<>();
        for (Piece piece : positionPieceArrayDeque) {
            posableMoves.add(piece.getCurrentPosition());
        }

        return posableMoves;
    }

    // 윷 던지기 //
    @Override
    public boolean throwAndSaveYut() {
        if (extraTurnCount > 0) extraTurnCount--;
        YutResult yutResult = yut.throwYut();
        yutResultArrayDeque.add(yutResult);
        if (yutResult.isExtraTurn()) {
            extraTurnCount++;
            return true;
        }
        return false;
    }

    @Override
    public boolean throwAndSaveYut(int n) {
        if (extraTurnCount > 0) extraTurnCount--;
        YutResult yutResult = yut.throwYut(n);
        yutResultArrayDeque.add(yutResult);
        if (yutResult.isExtraTurn()) {
            extraTurnCount++;
            return true;
        }
        return false;
    }

    @Override
    public boolean throwAndSaveYut(String input) {
        if (extraTurnCount > 0) extraTurnCount--;
        YutResult yutResult = yut.throwYut(input);
        yutResultArrayDeque.add(yutResult);
        if (yutResult.isExtraTurn()) {
            extraTurnCount++;
            return true;
        }
        return false;
    }

    @Override
    public boolean isExtraTurn() {
        return extraTurnCount > 0;
    }

    /// 내부에서 사용할 메서드 ///
    public Piece findPositionPieceAt(Position position) {
        for (Piece piece : positionPieceArrayDeque) {
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

    // grouping //
    // 특정 위치에 있는 말을 그룹화
    public boolean groupPiecesAtPosition(MovablePiece movedPiece, Position position) {
        // movedPiece의 ID와 현재 플레이어의 ID를 비교
        Player currentPlayer = getCurrentPlayer();
        String currentPlayerID = currentPlayer.getPlayerID();
        String movedPieceID = movedPiece.getPieceArrayDeque().peekFirst() != null ? movedPiece.getPieceArrayDeque().peekFirst().getPlayerID() : null;
        if (movedPieceID != null && !movedPieceID.equals(currentPlayerID)) return false;

        MovablePiece targetPiece = currentPlayer.getMovablePieceAt(position);

        if (targetPiece != null) {
            // 그룹화
            targetPiece.getPieceArrayDeque().addAll(movedPiece.getPieceArrayDeque());
            targetPiece.size += movedPiece.size; // 그룹화된 크기 업데이트
            currentPlayer.movablePieces.removeFirstOccurrence(movedPiece);
            return true; // 그룹화 성공
        }

        return false;
    }

    // Capture opponent's piece //
    public boolean captureOpponentPiece(MovablePiece movedPiece, Position position) {
        // 현재 플레이어 정보 가져오기
        Player currentPlayer = getCurrentPlayer();
        String currentPlayerID = currentPlayer.getPlayerID();

        // 이동한 말의 플레이어 ID 확인
        String movedPieceID = movedPiece.getPieceArrayDeque().peekFirst() != null
                ? movedPiece.getPieceArrayDeque().peekFirst().getPlayerID()
                : null;

        // 이동한 말이 현재 플레이어의 말이 아니면 종료
        if (movedPieceID == null || !movedPieceID.equals(currentPlayerID)) {
            return false;
        }

        // 해당 위치에 있는 MovablePiece 가져오기
        MovablePiece targetPiece = getMovablePieceAt(position);

        // 상대편 말이 존재하고, 현재 플레이어의 말이 아닌 경우
        if (targetPiece != null && !targetPiece.getPlayerID().equals(currentPlayerID)) {

            Player opponentPlayer = getPlayerByID(targetPiece.getPlayerID());
            if (opponentPlayer != null) {
                initializeGrouping(targetPiece);
            }
            return true; // 상대편 말을 잡은 경우
        }

        return false; // 상대편 말을 잡지 못한 경우
    }

    public void initializeGrouping(MovablePiece movablePiece) {
        // 그룹화 초기화
        for (Piece piece : movablePiece.getPieceArrayDeque()) {
            piece.moveTo(0); // 초기 위치로 이동
        }
        // 그룹 해제
        Player owner = getPlayerByID(movablePiece.getPlayerID());
        if (owner != null) {
            owner.getMovablePieces().remove(movablePiece);
            // 그룹 해제된 MovablePiece를 다시 추가
            for (int i = 0; i < movablePiece.size; i++) {
                Piece piece = new Piece(owner.getPlayerID(), movablePiece.getPieceArrayDeque().pop().pieceID);
                MovablePiece newMovablePiece = new MovablePiece(piece);
                owner.getMovablePieces().add(newMovablePiece);
            }
        }
    }


    public MovablePiece getMovablePieceAt(Position position) {
        for (Player player : players) {
            for (MovablePiece movablePiece : player.getMovablePieces()) {
                if (movablePiece.getCurrentPosition().equals(position)) {
                    return movablePiece; // 해당 위치에 있는 MovablePiece 반환
                }
            }
        }
        return null; // 해당 위치에 MovablePiece가 없음
    }

    public Player getPlayerByID(String playerID) {
        for (Player player : players) {
            if (player.getPlayerID().equals(playerID)) {
                return player;
            }
        }
        return null;
    }
}