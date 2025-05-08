package Model;

import java.util.ArrayDeque;

public class GameModel {
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

    /// Constructor ///
    public GameModel(int numPlayers, int numPieces) {
        this.numberOfPlayers = numPlayers;
        this.numberOfPieces = numPieces;
        this.players = new Player[numPlayers];
        this.gameScores = new int[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            players[i] = new Player("Player" + i, numPieces);
        }
        for (int i = 0; i < numPlayers; i++) {
            gameScores[i] = 0;
        }
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

    public int[] getGameScores() {
        return gameScores;
    }

    /// methods ///
    public void nextTurn() {
        if (extraTurnCount > 0) {
            extraTurnCount--;
            return; // 추가 턴이 남아있으면 턴을 넘기지 않음
        } else if (extraTurnCount == 0) currentPlayerIndex = (currentPlayerIndex + 1) % numberOfPlayers;
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
    public boolean captureOpponentPiece(MovablePiece movedPiece, Position position, YutResult yutResult) {
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

            // 자신의 말을 해당 위치로 이동
            /// 나중에 책임이 바뀔 수 있음
            movedPiece.moveTo(yutResult.getValue());

            // 추가 턴 증가
            extraTurnCount++;

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

    public YutResult throwYutRandom() {
        return yut.throwYut();
    }

    public YutResult throwYutManual(String input) {
        return yut.throwYut(input);
    }

    public ArrayDeque<Position> getPosableMoves(ArrayDeque<YutResult> YutResultArrayDeque) {
        // Todo:
        return null;
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