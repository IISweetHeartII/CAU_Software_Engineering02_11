package view;

public interface GameView {
    /**
     * UI를 초기화합니다.
     */
    void initUI();

    /**
     * 윷 던지기 결과를 화면에 표시합니다.
     * @param yutResult 윷 던지기 결과값
     */
    void showYutResult(Integer yutResult);

    /**
     * 승자를 화면에 표시합니다.
     * @param winner 승자의 플레이어 번호
     */
    void showWinner(int winner);

    /**
     * 게임 보드 상태를 업데이트합니다.
     */
    void updateBoard();

    /**
     * 플레이어의 점수를 업데이트합니다.
     */
    void updatePlayerScore();

    /**
     * 현재 차례를 업데이트합니다.
     */
    void updateTurn();
}
