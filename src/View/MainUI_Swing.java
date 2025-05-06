//[메인 프레임, 윷 던지기 버튼 처리]윷 던지기 버튼 UI 초안 + 이벤트 처리 기능 넣음
//GameView 구현중.


package View;

import Controller.GameController;
import Model.YutResultType;
import Model.YutResult;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayDeque;
import java.util.EnumMap;

//JFrame을 상속받아서 윈도우 창 만듦.
public class MainUI_Swing extends JFrame implements GameView {
    private final GameController controller;

    //Controller 받아서 Event 연결
    public MainUI_Swing(GameController controller) {
        this.controller = controller;
        initUI(); //----------> 화면 구성 시작
    }

    //화면 구성 (UI 초기화)
    private void initUI() {
        /*
        UI 메인 구조, 레이아웃 등등
        */


        // 지정 윷 던지기 버튼 생성 + 이벤트 연결
        JPanel manualPanel = new JPanel(new FlowLayout());

        EnumMap<YutResultType, JButton> yutButtons = new EnumMap<>(YutResultType.class);

        for (YutResultType type : YutResultType.values()) {
            JButton button = new JButton(type.name());
            button.addActionListener(e -> {
                controller.handleManualThrow(type);  // enum 그대로 넘김
            });
            yutButtons.put(type, button);
            manualPanel.add(button);
        }

        // 랜덤 윷 던지기 버튼 구성
        JButton randomButton = new JButton("랜덤 윷 던지기");
        randomButton.addActionListener(e -> {
            controller.handleRandomThrow();
        });

        JPanel randomPanel = new JPanel();
        randomPanel.add(randomButton);

        // 결과 출력

        // 프레임에 버튼 패널 추가 (위치 임의로 넣어두었어요)
        add(manualPanel, BorderLayout.NORTH);
        add(randomPanel, BorderLayout.CENTER);

        setVisible(true);
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
}