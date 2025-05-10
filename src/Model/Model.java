package Model;

import java.util.ArrayDeque;

public interface Model {
    // Game State
    Player getCurrentPlayer();
    /// 현재 턴의 플레이어를 반환

    Player[] getAllPlayers();
    /// 모든 플레이어를 반환
    ///
    /// 필요시 이걸 사용해 MovablePiece를 가져오고 View 에서 보드에 그릴 말을 그리면 됨

    ArrayDeque<MovablePiece> getAllMovablePieces();

    int[] getGameScores();
    /// 각 플레이어 점수 배열을 반환
    ///
    /// index는 PlayerID 숫자의 -1
    ///
    /// 예를 들어 Player1의 점수는 `gameScores[0]`에 저장됨

    // Methods
    boolean initializeGame();
    /// 게임 초기화

    // selectedPiece는 이동할 말, selectedPosition은 이동할 위치
    boolean movePiece(MovablePiece selectedPiece, Position selectedPosition);
    /// selectedPiece가 selectedPosition으로 이동할 수 있는지 확인하고, 이동 가능하면 이동
    ///
    /// Grouping과 Catpure 처리
    ///
    /// selectedPiece가 이동에 성공했을 때 true를 반환, 실패시 false를 반환

    //
    ArrayDeque<Position> getPosableMoves();

    boolean throwAndSaveYut();
    boolean throwAndSaveYut(int n);
    boolean throwAndSaveYut(String input);
    /// extraTurnCount > 0 일 때 윷을 던지고 extraTurnCount를 감소시킴
    ///
    /// extraTurnCount가 생길 때 extraTurnCount++

    boolean isExtraTurn();
    /// extraTurnCount가 0이 아닐 때 true를 반환
    ///
    /// 이 메서드가 false를 반환할 때까지 윷을 다시 던지기
    ///
    /// 윷 던지기 메서드만 extraTurnCount를 감소시킴

    boolean isGameEnd();

    int[] getNotArrivedCount();

    boolean changeTurn();
    /// ExtraTurnCount를 확인하지않고 강제로 턴을 넘김
    ///
    /// 따라서 Controller에서 movePiece 이후 isExtraTurn()을 확인하고 false일 때만 호출해야 함
    ///
    /// 테스트용으로 extraTurnCount를 0이 아닐 때 false를 반환하도록 설정, 반환 타입을 사용하지 않아도 무방함.
}
