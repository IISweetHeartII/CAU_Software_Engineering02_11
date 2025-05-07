package Model;

        import java.util.ArrayDeque;

        public class GameModel {
            /// fields ///
            protected final Board board = new Board();
            protected final Yut yut = new Yut();
            protected final Player[] players;
            protected final int numberOfPlayers;
            protected final int numberOfPieces;
            protected int currentPlayerIndex = 0;
            protected int[] gameScores;

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
                currentPlayerIndex = (currentPlayerIndex + 1) % numberOfPlayers;
            }

            // 특정 위치에 있는 말을 그룹화
            public void groupPiecesAtPosition(MovablePiece movedPiece, Position position) {
                // movedPiece의 ID와 현재 플레이어의 ID를 비교
                Player currentPlayer = getCurrentPlayer();
                String currentPlayerID = currentPlayer.getPlayerID();
                String movedPieceID = movedPiece.getPieceArrayDeque().peekFirst() != null ? movedPiece.getPieceArrayDeque().peekFirst().getPlayerID() : null;
                if (movedPieceID != null && !movedPieceID.equals(currentPlayerID)) return;

                MovablePiece targetPiece = currentPlayer.getMovablePieceAt(position);

                if (targetPiece != null) {
                    // 그룹화
                    targetPiece.getPieceArrayDeque().addAll(movedPiece.getPieceArrayDeque());
                    currentPlayer.movablePieces.removeFirstOccurrence(movedPiece);
                }
            }

            public YutResult throwYutRandom() {
                return yut.throwYut();
            }

            public YutResult throwYutManual(String input) {
                return yut.throwYut(input);
            }

            public ArrayDeque<Position> getPosableMoves(ArrayDeque<YutResult> YutResultArrayDeque) {
                ArrayDeque<Position> posableMoves = new ArrayDeque<>();
                Player currentPlayer = getCurrentPlayer();

                for (MovablePiece movablePiece : currentPlayer.getMovablePieces()) {
                    if (!movablePiece.isArrived()) {
                        for (YutResult yutResult : YutResultArrayDeque) {
                            Position nextPosition = board.getNNextPosition(movablePiece.getCurrentPosition(), yutResult.getValue());
                            if (nextPosition != null) {
                                posableMoves.add(nextPosition);
                            }
                        }
                    }
                }
                return posableMoves;
            }

            public Piece getPieceAtPosition(Position position) {
                for (Player player : players) {
                    for (Piece piece : player.getAllPieces()) {
                        if (piece.getCurrentPosition().equals(position)) {
                            return piece;
                        }
                    }
                }
                return null;
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