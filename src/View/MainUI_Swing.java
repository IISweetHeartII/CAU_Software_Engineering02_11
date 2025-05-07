//[메인 프레임, 윷 던지기 버튼 처리]윷 던지기 버튼 UI 초안 + 이벤트 처리 기능 넣음
//GameView 구현중.

package View;

import Controller.GameController;
import Model.Position;
import Model.YutResult;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayDeque;
import java.awt.event.ActionEvent;
import java.util.EnumMap;

//JFrame을 상속받아서 윈도우 창 만듦.
public class MainUI_Swing extends JFrame implements GameView {
    /// responsibilities ///
    /// 1. get game state from controller by parameter
    /// 2. show game state to user
    /// 3. UI update
    ///
    /// This class MUST NOT use Controller class !!!
    /// It should only use Model class and GameView interface.

    public MainUI_Swing(GameController controller) {
        initUI(); //----------> 화면 구성시작
    }

    //화면 구성 (UI 초기화)
    private void initUI() {
        // Todo: UI 초기화
    }

    @Override
    public void showYutResult(YutResult yutResult) {
        // Todo: 결과 출력 UI 구현
    }

    @Override
    public void updateCurrentPlayer(String playerID) {
        // Todo: 현재 플레이어 ID 업데이트 UI 구현
    }

    public void showPosableMoves(ArrayDeque<Model.Position> posableMoves) {
        // Todo: Board에 이동 가능한 위치 표시 UI 구현
    }

    public Model.Position getUserSelectedPosition(ArrayDeque<Model.Position> posableMoves) {
        // Todo: 사용자로부터 선택된 위치를 가져오는 UI 구현
        return null; // 임시로 null 반환
    }

    @Override
    public void BoardRendering() {
        // Todo: 보드 렌더링 UI 구현
    }

    @Override
    public void showGameEnd(String playerID) {
        // Todo: 게임 종료 메시지 표시 UI 구현
    }
}