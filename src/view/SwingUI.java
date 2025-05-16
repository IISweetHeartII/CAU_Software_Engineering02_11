package view;

import controller.GameController;
import model.GameModel;
import model.YutResult;
import org.w3c.dom.Node;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

    private JLabel titleLabel;

    private JLabel player1ScoreLabel;
    private JLabel player2ScoreLabel;
    private JLabel player3ScoreLabel;
    private JLabel player4ScoreLabel;

    private JLabel turnLabel;

    private JLabel yutLabel;

    ///  생성자
    public SwingUI(GameController controller, GameModel model) {
        // GUI 초기화 코드
        this.controller = controller;
        this.model = model;
        initUI();
    }


    /// 메서드
    public void initUI() {
        // ------- No Need Modification ------- //
        frame = new JFrame("윷놀이 게임");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 700);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        // ------- 배경 설정 ------- //
        ImageIcon bgIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource(
                "/data/ui/board/board_four.png"
        )));
        Image bgImage = bgIcon.getImage();
        BackgroundPanel backgroundPanel = new BackgroundPanel(bgImage);
        backgroundPanel.setPreferredSize(new Dimension(700, 700));
        backgroundPanel.setLayout(null);  // 절대 위치 지정
        // --------------------- //

        // Todo: [Throw] Button

        // Todo: [Custom choice]

        // Todo: [Restart] Button

        // Todo: [Quit] Button

        // ------- Board 버튼 설정 -------- //
        Map<String, NodeButton> boardButtons = createButtons(boardButtonPositions);
        for (String id : boardButtons.keySet()) { // -> 각 Node 버튼의 ActionListener 설정: 나중에 메서드 책임이 바뀔 수 있음
            NodeButton btn = boardButtons.get(id);
            btn.addActionListener(e -> {
                String clickedId = e.getActionCommand();

                System.out.println("클릭된 노드: " + clickedId); // -> test용, 주석 처리 무관

                controller.handleBoardClick(clickedId);
            });

            backgroundPanel.add(btn);
        }
        // ------------------------------- //

        // ------ Quit 버튼 설정 ------ //
        {
            // 난 버튼을 생성할 것이다 <- 버튼 객체 생성 및 위치 설정
            JButton quitButton = new JButton("Quit");
            quitButton.setBounds(584, 644, 279 / 3, 108 / 3);

            // 이미지 처리
            ImageIcon quitIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource(
                    "/data/ui/button/button_quit_up.png"
            )));
            Image scaled = quitIcon.getImage().getScaledInstance(279 / 3, 108 / 3, Image.SCALE_SMOOTH);
            quitButton.setIcon(new ImageIcon(scaled));

            // 버튼 표시 옵션
            quitButton.setBorderPainted(false);
            quitButton.setContentAreaFilled(false);
            quitButton.setFocusPainted(false);
            quitButton.setOpaque(false);
            quitButton.setText("");

            // Action 설정
            quitButton.addActionListener(e -> {
                System.exit(0);
            });

            // 패널에 버튼 추가
            backgroundPanel.add(quitButton);
        }
        // --------------------------- //

        // ------- 최종 설정 No Need Modification ------- //
        Objects.requireNonNull(frame).setContentPane(backgroundPanel);
        frame.pack();
        frame.setVisible(true);
    }


    // ---------- Board Buttons ---------- //
    Map<String, Point> boardButtonPositions = new HashMap<>();
    // 접어서 사용하세요. 다각형 보드를 제외하고는 더이상 참고할 필요 없습니다.
    {
        boardButtonPositions.put("P1", new Point(432-20, 353-20)); // 각 x, y의 20만큼 위치 보정
        boardButtonPositions.put("P2", new Point(432-20, 275-20));
        boardButtonPositions.put("P3", new Point(432-20, 197-20));
        boardButtonPositions.put("P4", new Point(432-20, 119-20));
        boardButtonPositions.put("P5", new Point(432-20, 42-20));

        boardButtonPositions.put("P6", new Point(355-20, 42-20));
        boardButtonPositions.put("P7", new Point(277-20, 42-20));
        boardButtonPositions.put("P8", new Point(199-20, 42-20));
        boardButtonPositions.put("P9", new Point(121-20, 42-20));
        boardButtonPositions.put("P10", new Point(42-20, 42-20));

        boardButtonPositions.put("P11", new Point(42-20, 119-20));
        boardButtonPositions.put("P12", new Point(42-20, 197-20));
        boardButtonPositions.put("P13", new Point(42-20, 275-20));
        boardButtonPositions.put("P14", new Point(42-20, 353-20));
        boardButtonPositions.put("P15", new Point(42-20, 432-20));

        boardButtonPositions.put("P16", new Point(121-20, 432-20));
        boardButtonPositions.put("P17", new Point(199-20, 432-20));
        boardButtonPositions.put("P18", new Point(277-20, 432-20));
        boardButtonPositions.put("P19", new Point(355-20, 432-20));
        boardButtonPositions.put("P20", new Point(432-20, 432-20));

        // 내부 경로
        boardButtonPositions.put("E1", new Point(364-20, 108-20));
        boardButtonPositions.put("E2", new Point(303-20, 171-20));
        boardButtonPositions.put("E3", new Point(171-20, 303-20));
        boardButtonPositions.put("E4", new Point(108-20, 364-20));

        boardButtonPositions.put("C", new Point(237-20, 237-20));

        boardButtonPositions.put("E5", new Point(108-20, 108-20));
        boardButtonPositions.put("E6", new Point(171-20, 171-20));
        boardButtonPositions.put("E7", new Point(303-20, 303-20));
        boardButtonPositions.put("E8", new Point(364-20, 364-20));
    }

    public static class NodeButton extends JButton {
        private final String nodeId;

        public NodeButton(String nodeId, int x, int y) {
            this.nodeId = nodeId;
            setBounds(x, y, 40, 40);
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);

            setActionCommand(nodeId); // -> 버튼마다 고유 ID 지정 -> ID로 버튼 클릭 조작
        }

        public String getNodeId() {
            return nodeId;
        }
    }

    public static Map<String, NodeButton> createButtons(Map<String, Point> positions) {
        Map<String, NodeButton> buttonMap = new HashMap<>();

        for (String id : positions.keySet()) {  // boardButtonPositions 에 저장된 위치 정보마다 버튼을 생성
            Point p = positions.get(id);        // └ boardButtonPositions Map만 수정하면 다각형으로 확장 가능
            NodeButton button = new NodeButton(id, p.x, p.y);
            buttonMap.put(id, button);
        }
        return buttonMap;
    }
    // ----------------------------------- //

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
