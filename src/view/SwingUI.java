package view;

import controller.GameController;
import model.GameModel;
import model.YutResult;

import javax.swing.*;
import java.awt.*;

public class SwingUI {
    /// SwingUI 클래스는 GUI를 구성하는 역할을 합니다.
    /// 이 클래스는 JFrame을 상속받아 GUI의 기본 틀을 제공합니다.
    /// 또한, ActionListener를 구현하여 버튼 클릭 이벤트를 처리합니다.
    ///
    /// JFrame을 상속받아 GUI의 기본 틀을 제공합니다.
    /// ActionListener를 구현하여 버튼 클릭 이벤트를 처리합니다.

    GameController controller;
    GameModel model;
    private JFrame frame;

    public SwingUI(GameController controller, GameModel model) {
        // GUI 초기화 코드
        this.controller = controller;
        this.model = model;
        initUI();
    }



    public void initUI() {
        frame = new JFrame("윷놀이 게임");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 700);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        // 이미지 불러오기
        ImageIcon bgIcon = new ImageIcon(getClass().getResource(
                "/data/UI/board/board_four.png"
        ));
        Image bgImage = bgIcon.getImage();

        // 배경 패널 생성
        BackgroundPanel backgroundPanel = new BackgroundPanel(bgImage);
        backgroundPanel.setPreferredSize(new Dimension(700, 700));

        frame.setContentPane(backgroundPanel); // 프레임에 배경 패널 추가
        frame.pack(); // 패널의 preferredSize에 맞춤
        frame.setVisible(true);
    }

    public void showYutResult(YutResult yutResult) {
    }

    public void updateBoard() {
    }

    public void updatePlayerState() {
    }

    public void showGameEnd(String playerID) {
    }

    public void updateCurrentPlayer(String playerID) {
    }
}
