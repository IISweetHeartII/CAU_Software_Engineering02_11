package Model;

import java.util.ArrayDeque;

public class GameModel {
    /// fields ///
    // Board //
    private final Board board = new Board();
    // YutThrow //
    private final Yut yut = new Yut();
    // Player //
    private final Player[] players;
    // Game State //
    private final int numberOfPlayers;
    private final int numberOfPieces;
    private int currentPlayerIndex = 0;
    private int[] gameScores;

    /// Constructor ///
    public GameModel(int numPlayers, int numPieces) {
        this.numberOfPlayers = numPlayers; // 플레이어 수
        this.numberOfPieces = numPieces; // 말 개수
        this.players = new Player[numPlayers];
        this.gameScores = new int[numPlayers]; // 플레이어 점수 초기화
        for (int i = 0; i < numPlayers; i++) {
            players[i] = new Player("Player" + i, numPieces); // 플레이어 생성
        }
        for (int i = 0; i < numPlayers; i++) {
            gameScores[i] = 0; // 플레이어 점수 초기화
        }
    }

    /// setters ///
    public void addScore(int playerIndex) {
        this.gameScores[playerIndex] += 1; // 플레이어 점수 추가
    }

    /// getters ///
    // 현재 턴의 플레이어 반환
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

    /// methods ///
    // 다음 플레이어로 턴 전환
    public void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % numberOfPlayers;
    }

    // Grouping //

    // Yut 던지기 //
    //랜덤 윷 던지기
    public YutResult throwYutRandom() {
        return yut.throwYut();
    }

    //지정 윷 던지기
    public YutResult throwYutManual(String input) {
        return yut.throwYut(input);
    }

    // Grouping //


    // Capture //


    // currentPlayer의 말 중에서 isArrived가 false인 말들 중에서 모든 말들에 대한 도착 가능한 Position을 반환
    public ArrayDeque<Position> getPosableMoves(ArrayDeque<YutResult> YutResultArrayDeque) {
        ArrayDeque<Position> posableMoves = new ArrayDeque<>();
        Player currentPlayer = getCurrentPlayer();
        Piece[] pieces = currentPlayer.getAllPieces();

        // Todo: How about the case of Grouping?
        for (Piece piece : pieces) {
            if (!piece.getCurrentPosition().equals("END")) { // 도착하지 않은 말만
                for (YutResult yutResult : YutResultArrayDeque) {
                    Position nextPosition = board.getNNextPosition(piece.getCurrentPosition(), yutResult.getValue());
                    if (nextPosition != null) {
                        posableMoves.add(nextPosition);
                    }
                }
            }
        }
        return posableMoves;
    }

    // 특정 Position에 위치하는 Piece를 반환
    public Piece getPieceAtPosition(Position position) {
        for (Player player : players) {
            for (Piece piece : player.getAllPieces()) {
                if (piece.getCurrentPosition().equals(position)) {
                    return piece; // 해당 위치에 있는 말을 반환
                }
            }
        }
        return null; // 해당 위치에 말이 없음
    }

    public Player getPlayerByID(String playerID) {
        for (Player player : players) {
            if (player.getPlayerID().equals(playerID)) {
                return player; // 해당 플레이어 반환
            }
        }
        return null; // 해당 플레이어 없음
    }
}
