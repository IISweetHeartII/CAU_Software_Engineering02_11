package View;

import Controller.GameController;
import Model.GameModel;

public class SwingUI {
    // SwingUI 클래스는 GUI를 구성하는 역할을 합니다.
    // 이 클래스는 JFrame을 상속받아 GUI의 기본 틀을 제공합니다.
    // 또한, ActionListener를 구현하여 버튼 클릭 이벤트를 처리합니다.

    // JFrame을 상속받아 GUI의 기본 틀을 제공합니다.
    // ActionListener를 구현하여 버튼 클릭 이벤트를 처리합니다.

    GameController controller;
    GameModel model;

    public SwingUI(GameController controller, GameModel model) {
        // GUI 초기화 코드
        this.controller = controller;
        this.model = model;
        // GameController 에서 this와 this.model을 전달받습니다.
        // 그렇기에 여기서 사용되는 controller는 새로운 controller가 아닌 main에서 생성된 controller입니다.
        // ⚠ GameContoller 내부에서 선언된 모델이 수정되면 이곳의 model도 자동으로 수정됩니다.
        // 그렇기에 View와 Controller의 내부에 포함된 각각의 model 상태 동기화 문제에 관해 고려하실 필요는 없습니다.
        // model 또한 GameController에서 생성된 model이지만, 사용하지 않는 것을 권장드립니다.
        initUI();
    }

    public void initUI() {
        // UI 초기화 코드
        // 게임 보드, 버튼, 레이블 등을 생성하고 배치합니다.
    }
}
