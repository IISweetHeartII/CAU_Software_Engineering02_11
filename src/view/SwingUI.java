package view;

import controller.GameController;
import model.GameManager;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SwingUI implements GameView {
    GameController controller;
    GameManager model;
    private JFrame frame;
    private BackgroundPanel backgroundPanel;

    private JLabel titleLabel;
    private JLabel[] playerScoreLabel;
    private JLabel turnLabel;
    private JLabel yutLabel;

    private JButton throwButton;
    private JButton STARTButton;
    private JButton ENDButton;

    private Map<String, JLabel> piecePositions = new HashMap<>();

    private int numberOfPlayers; // 플레이어 수
    private int numberOfPieces; // 플레이어당 말 수

    // ------ 생성자: Constructor ------- //
    public SwingUI(GameController controller, GameManager model) {
        // GUI 초기화 코드
        this.controller = controller;
        this.model = model;

        this.numberOfPlayers = model.getNumberOfPlayers();
        this.numberOfPieces = model.getNumberOfPieces();
        this.playerScoreLabel = new JLabel[numberOfPlayers + 1]; // 0번은 사용하지 않음

        initUI();
    }

    // ------- 메서드: Method ------ //
    public void initUI() {
        // ------- No Need Modification ------- //

        frame = new JFrame("윷놀이 게임");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 700);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);


        // ------- 배경 설정 ------- //
        ImageIcon bgIcon;
        switch (model.getSize()) {
            case 4:
                bgIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource(
                        "/data/ui/board/board_four.png"
                )));
                break;
            case 5:
                bgIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource(
                        "/data/ui/board/board_five.png"
                )));
                break;
            case 6:
                bgIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource(
                        "/data/ui/board/board_six.png"
                )));
                break;
            default:
                System.out.println("Invalid board size");
                bgIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource(
                        "/data/ui/board/board_four.png"
                )));
                break;
        }
        Image bgImage = bgIcon.getImage();
        backgroundPanel = new BackgroundPanel(bgImage);
        backgroundPanel.setPreferredSize(new Dimension(700, 700));
        backgroundPanel.setLayout(null);  // 절대 위치 지정
        // ------------------------ //

        // ------ title 설정 ------ //

        // 이미지 처리
        ImageIcon titleIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource(
                "/data/ui/title.PNG"
        )));
        Image scaledTitle = titleIcon.getImage().getScaledInstance(1242 / 6, 558 / 6, Image.SCALE_SMOOTH);
        titleLabel = new JLabel(new ImageIcon(scaledTitle));

        // 위치 처리 및 보정
        titleLabel.setBounds(473 - (1242/12), 20 - (558/12), 1242 / 3, 558 / 3);

        // 패널에 추가
        backgroundPanel.add(titleLabel);


        // ------- Board 버튼 설정 -------- //
        createBoardButtons(model.getSize());
        Map<String, NodeButton> boardButtons = createBoardButtons(boardButtonPositions);
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

        // ------ Player Score 설정 ------ //
        // 이 프로그램에서 Score는 시작하지않은(START에 위치하는) 말의 개수를 의미합니다.

        for (int i = 1; i < numberOfPlayers + 1; i++) {
            // 이미지 처리
            ImageIcon playerScoreIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource(
                    "/data/ui/score/player" + i + "/player" + i + "_" + numberOfPieces + ".png"
            )));
            Image scaledPlayerScore = playerScoreIcon.getImage().getScaledInstance(
                    playerScoreIcon.getIconWidth() / 3,
                    playerScoreIcon.getIconHeight() / 3,
                    Image.SCALE_SMOOTH);
            playerScoreLabel[i] = new JLabel(new ImageIcon(scaledPlayerScore));

            // 위치 처리 및 보정
            playerScoreLabel[i].setBounds(485, 181 + 47*(i - 1), scaledPlayerScore.getWidth(null), scaledPlayerScore.getHeight(null));

            // 패널에 추가
            backgroundPanel.add(playerScoreLabel[i]);
        }

        // ------------------------------ //

        // ------ START Button 설정 ------ //

        // 버튼 객체 할당
        STARTButton = new JButton("START");

        // 버튼 위치 할당
        STARTButton.setBounds(473+10, 161, 184, 202);

        // 버튼 옵션 설정
        STARTButton.setBorderPainted(false);
        STARTButton.setContentAreaFilled(false);
        STARTButton.setFocusPainted(false);
        STARTButton.setOpaque(false);
        STARTButton.setText("");

        // Action 설정
        STARTButton.addActionListener(e -> {
            controller.handleBoardClick("START");
            System.out.println("START Button Clicked");
        });

        // 패널에 버튼 추가
        backgroundPanel.add(STARTButton);

        // ------ END Button 설정 ------ //
        // 버튼 객체 할당
        ENDButton = new JButton("END");
        // 버튼 위치 할당
        ENDButton.setBounds(20, 472, 1299/3, 621/3);
        // 버튼 옵션 설정
        ENDButton.setBorderPainted(false);
        ENDButton.setContentAreaFilled(false);
        ENDButton.setFocusPainted(false);
        ENDButton.setOpaque(false);
        ENDButton.setText("");
        // Action 설정
        ENDButton.addActionListener(e -> {
            controller.handleBoardClick("END");
            System.out.println("END Button Clicked");
        });

        //------ Turn 설정 ------ //
        // 이미지 처리 및 보정
        ImageIcon turnIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource(
                "/data/ui/turn/turn_1.png"
        )));
        Image scaledTurnIcon = turnIcon.getImage().getScaledInstance(turnIcon.getIconWidth() / 3, turnIcon.getIconHeight() / 3, Image.SCALE_SMOOTH);
        turnLabel = new JLabel(new ImageIcon(scaledTurnIcon));
        turnLabel.setBounds(485, 392, scaledTurnIcon.getWidth(null), scaledTurnIcon.getHeight(null));

        // 패널에 추가
        backgroundPanel.add(turnLabel);
        // --------------------- //

        // ------ [Throw] Button 설정 ------ //
        createThrowButton();

        // ------ [Custom] Button 설정 ------ //
        createPopupButton();

        // ------ [Restart] Button 설정 ------ //
        createRestartButton();

        // ------ Quit 버튼 설정 ------ //
        createQuitButton();

        // ------- 최종 설정 No Need Modification ------- //
        {
            Objects.requireNonNull(frame).setContentPane(backgroundPanel);
            frame.pack();
            frame.setVisible(true);
        }
    }


    // ---------- Board Buttons ---------- //
    Map<String, Point> boardButtonPositions = new HashMap<>();
    private void createBoardButtons(int size) {
        switch (size) {
            case 4:
                createBoardButtons4();
                break;
            case 5:
                createBoardButtons5();
                break;
            case 6:
                createBoardButtons6();
                break;
            default:
                System.out.println("Invalid board size");
                createBoardButtons4();
                break;
        }
    }
    private void createBoardButtons4() {
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

    private void createBoardButtons5() {
        boardButtonPositions.put("P1", new Point(372-20, 375-20)); // 각 x, y의 20만큼 위치 보정
        boardButtonPositions.put("P2", new Point(388-20, 330-20));
        boardButtonPositions.put("P3", new Point(402-20, 284-20));
        boardButtonPositions.put("P4", new Point(417-20, 238-20));
        boardButtonPositions.put("P5", new Point(432-20, 192-20));

        boardButtonPositions.put("P6", new Point(393-20, 164-20));
        boardButtonPositions.put("P7", new Point(354-20, 135-20));
        boardButtonPositions.put("P8", new Point(315-20, 107-20));
        boardButtonPositions.put("P9", new Point(276-20, 78-20));
        boardButtonPositions.put("P10", new Point(236-20, 50-20));

        boardButtonPositions.put("P11", new Point(197-20, 78-20));
        boardButtonPositions.put("P12", new Point(158-20, 107-20));
        boardButtonPositions.put("P13", new Point(119-20, 135-20));
        boardButtonPositions.put("P14", new Point(80-20, 164-20));
        boardButtonPositions.put("P15", new Point(41-20, 192-20));

        boardButtonPositions.put("P16", new Point(56-20, 238-20));
        boardButtonPositions.put("P17", new Point(71-20, 284-20));
        boardButtonPositions.put("P18", new Point(87-20, 330-20));
        boardButtonPositions.put("P19", new Point(101-20, 375-20));
        boardButtonPositions.put("P20", new Point(115-20, 422-20));

        boardButtonPositions.put("P21", new Point(164-20, 422-20));
        boardButtonPositions.put("P22", new Point(212-20, 422-20));
        boardButtonPositions.put("P23", new Point(261-20, 422-20));
        boardButtonPositions.put("P24", new Point(309-20, 422-20));
        boardButtonPositions.put("P25", new Point(358-20, 422-20));

        boardButtonPositions.put("C", new Point(236-20, 255-20));

        boardButtonPositions.put("E1", new Point(354-20, 217-20));
        boardButtonPositions.put("E2", new Point(295-20, 236-20));
        boardButtonPositions.put("E3", new Point(200-20, 305-20));
        boardButtonPositions.put("E4", new Point(164-20, 355-20));
        boardButtonPositions.put("E5", new Point(236-20, 132-20));
        boardButtonPositions.put("E6", new Point(236-20, 195-20));
        boardButtonPositions.put("E7", new Point(274-20, 305-20));
        boardButtonPositions.put("E8", new Point(309-20, 355-20));
        boardButtonPositions.put("E9", new Point(119-20, 217-20));
        boardButtonPositions.put("E10", new Point(178-20, 236-20));
    }

    private void createBoardButtons6() {
        boardButtonPositions.put("P1", new Point(354-20, 370-20)); // 각 x, y의 20만큼 위치 보정
        boardButtonPositions.put("P2", new Point(373-20, 337-20));
        boardButtonPositions.put("P3", new Point(393-20, 303-20));
        boardButtonPositions.put("P4", new Point(409-20, 269-20));
        boardButtonPositions.put("P5", new Point(431-20, 236-20));

        boardButtonPositions.put("P6", new Point(409-20, 203-20));
        boardButtonPositions.put("P7", new Point(393-20, 169-20));
        boardButtonPositions.put("P8", new Point(373-20, 133-20));
        boardButtonPositions.put("P9", new Point(354-20, 101-20));
        boardButtonPositions.put("P10", new Point(335-20, 67-20));

        boardButtonPositions.put("P11", new Point(295-20, 67-20));
        boardButtonPositions.put("P12", new Point(256-20, 67-20));
        boardButtonPositions.put("P13", new Point(217-20, 67-20));
        boardButtonPositions.put("P14", new Point(178-20, 67-20));
        boardButtonPositions.put("P15", new Point(139-20, 67-20));

        boardButtonPositions.put("P16", new Point(118-20, 99-20));
        boardButtonPositions.put("P17", new Point(98-20, 134-20));
        boardButtonPositions.put("P18", new Point(80-20, 168-20));
        boardButtonPositions.put("P19", new Point(60-20, 202-20));
        boardButtonPositions.put("P20", new Point(41-20, 236-20));

        boardButtonPositions.put("P21", new Point(60-20, 269-20));
        boardButtonPositions.put("P22", new Point(80-20, 303-20));
        boardButtonPositions.put("P23", new Point(98-20, 337-20));
        boardButtonPositions.put("P24", new Point(118-20, 370-20));
        boardButtonPositions.put("P25", new Point(139-20, 404-20));

        boardButtonPositions.put("P26", new Point(178-20, 404-20));
        boardButtonPositions.put("P27", new Point(217-20, 404-20));
        boardButtonPositions.put("P28", new Point(256-20, 404-20));
        boardButtonPositions.put("P29", new Point(295-20, 404-20));
        boardButtonPositions.put("P30", new Point(335-20, 404-20));

        boardButtonPositions.put("C", new Point(235-20, 236-20));

        boardButtonPositions.put("E1", new Point(366-20, 236-20));
        boardButtonPositions.put("E2", new Point(301-20, 236-20));
        boardButtonPositions.put("E3", new Point(168-20, 236-20));
        boardButtonPositions.put("E4", new Point(105-20, 236-20));

        boardButtonPositions.put("E5", new Point(170-20, 123-20));
        boardButtonPositions.put("E6", new Point(203-20, 180-20));
        boardButtonPositions.put("E7", new Point(268-20, 292-20));
        boardButtonPositions.put("E8", new Point(298-20, 348-20));

        boardButtonPositions.put("E11", new Point(298-20, 123-20));
        boardButtonPositions.put("E12", new Point(268-20, 180-20));
        boardButtonPositions.put("E9", new Point(203-20, 292-20));
        boardButtonPositions.put("E10", new Point(170-20, 348-20));
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

    public static Map<String, NodeButton> createBoardButtons(Map<String, Point> positions) {
        Map<String, NodeButton> buttonMap = new HashMap<>();

        for (String id : positions.keySet()) {  // boardButtonPositions 에 저장된 위치 정보마다 버튼을 생성
            Point p = positions.get(id);        // └ boardButtonPositions Map만 수정하면 다각형으로 확장 가능
            NodeButton button = new NodeButton(id, p.x, p.y);
            buttonMap.put(id, button);
        }
        return buttonMap;
    }
    // ----------------------------------- //


    // ------ show yut result <---- controller ------ //
    public void showYutResult(Integer yutResult) {
        // 이미지 처리
        ImageIcon yutResultIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource(
                "/data/ui/yut/yut_" + yutResult + ".png"
        )));
        Image scaledYutResult = yutResultIcon.getImage().getScaledInstance(
                yutResultIcon.getIconWidth() / 3,
                yutResultIcon.getIconHeight() / 3,
                Image.SCALE_SMOOTH);

        // 객체 생성
        yutLabel.setIcon(new ImageIcon(scaledYutResult));
    }
    // ---------------------------------------------- //

    // ------ show winner <---- controller ------ //
    public void showWinner(int winner) {
        // 이미지 처리
        ImageIcon winnerIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource(
                "/data/ui/turn/winner_" + winner + ".png"
        )));
        Image scaledWinnerIcon = winnerIcon.getImage().getScaledInstance(
                winnerIcon.getIconWidth() / 3,
                winnerIcon.getIconHeight() / 3,
                Image.SCALE_SMOOTH);
        // turn Label에 winner 이미지 설정
        turnLabel.setIcon(new ImageIcon(scaledWinnerIcon));
    }

    // ------ update board ------ //
    private static final int PIECE_SCALE_FACTOR = 2;
    private static final int POSITION_ADJUSTMENT_FACTOR = 6;
    public void updateBoard() {
        // 기존에 표시된 모든 말 제거
        removePreviousPieces();

        // 새로운 말 위치 정보 가져오기
        Map<String, String> PositionPieceMap = model.getPositionPieceMap();

        // 새로운 말들을 생성하고 표시
        PositionPieceMap.forEach((nodeId, pieceId) -> {
            if (!(nodeId.equals("START") || nodeId.equals("END"))) { // SART, END 제외
                JLabel pieceLabel = createScaledPieceLabel(pieceId);
                adjustPiecePosition(pieceLabel, nodeId);
                piecePositions.put(pieceId, pieceLabel);  // Map에 저장
                backgroundPanel.add(pieceLabel);
            }
        });

        // 화면 갱신
        backgroundPanel.revalidate();
        backgroundPanel.repaint();
    }

    private JLabel createScaledPieceLabel(String pieceId) { // -> updateBoard()
        ImageIcon pieceIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource(
                "/data/ui/player/p" + pieceId.charAt(0) + "_" + pieceId.length() + ".png"
        )));

        // test
        System.out.println("pieceId: " + pieceId);

        Image scaledPiece = pieceIcon.getImage().getScaledInstance(
                pieceIcon.getIconWidth()/PIECE_SCALE_FACTOR,
                pieceIcon.getIconHeight()/PIECE_SCALE_FACTOR,
                Image.SCALE_SMOOTH);

        return new JLabel(new ImageIcon(scaledPiece));
    }

    private void createThrowButton() {
        // 버튼 객체 할당
        throwButton = new JButton("Throw");

        // 이미지 처리 및 보정
        ImageIcon throwIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource(
                "/data/ui/button/button_throw_up.png"
        )));
        Image scaledThrowIcon = throwIcon.getImage().getScaledInstance(
                throwIcon.getIconWidth() / 3,
                throwIcon.getIconHeight() / 3,
                Image.SCALE_SMOOTH);
        throwButton.setIcon(new ImageIcon(scaledThrowIcon));

        // 버튼 위치 할당
        throwButton.setBounds(473,473, scaledThrowIcon.getWidth(null), scaledThrowIcon.getHeight(null));

        // 버튼 옵션 설정
        throwButton.setBorderPainted(false);
        throwButton.setContentAreaFilled(false);
        throwButton.setFocusPainted(false);
        throwButton.setOpaque(false);
        throwButton.setText("");

        // Action 설정
        throwButton.addActionListener(e -> {
            controller.handleRandomThrow();
            System.out.println("Throw Button Clicked");
        });
        // view:throwButton -> controller:handleRandomThrow -> View:showYutResult

        // Hovering 처리
        ImageIcon quitHoverIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource(
                "/data/ui/button/button_throw_down.png"
        )));
        Image hoverScaled = quitHoverIcon.getImage().getScaledInstance(scaledThrowIcon.getWidth(null), scaledThrowIcon.getHeight(null), Image.SCALE_SMOOTH);
        throwButton.setRolloverIcon(new ImageIcon(hoverScaled));

        //패널에 버튼 추가
        Objects.requireNonNull(backgroundPanel).add(throwButton);

        // ------ 기본 윷 표시 ------ //
        ImageIcon yutIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource(
                "/data/ui/yut/yut_5.png"
        )));
        Image scaledYutIcon = yutIcon.getImage().getScaledInstance(yutIcon.getIconWidth() / 3, yutIcon.getIconHeight() / 3, Image.SCALE_SMOOTH);
        yutLabel = new JLabel(new ImageIcon(scaledYutIcon));
        yutLabel.setBounds(20, 472, scaledYutIcon.getWidth(null), scaledYutIcon.getHeight(null));
        backgroundPanel.add(yutLabel);
    }

    private void createQuitButton() {
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

        // Hovering 처리
        ImageIcon quitHoverIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource(
                "/data/ui/button/button_quit_down.png"
        )));
        Image hoverScaled = quitHoverIcon.getImage().getScaledInstance(279 / 3, 108 / 3, Image.SCALE_SMOOTH);
        quitButton.setRolloverIcon(new ImageIcon(hoverScaled));

        // 패널에 버튼 추가
        Objects.requireNonNull(backgroundPanel).add(quitButton);
    }

    private void createRestartButton() {
        // 객체 생성
        JButton restartButton = new JButton("Restart");
        // 버튼 옵션 설정
        restartButton.setBorderPainted(false);
        restartButton.setContentAreaFilled(false);
        restartButton.setFocusPainted(false);
        restartButton.setOpaque(false);
        restartButton.setText("");
        // 이미지 처리
        ImageIcon restartIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource(
                "/data/ui/button/button_restart_up.png"
        )));
        Image scaledRestartIcon = restartIcon.getImage().getScaledInstance(
                restartIcon.getIconWidth() / 3,
                restartIcon.getIconHeight() / 3,
                Image.SCALE_SMOOTH);
        // 위치 설정 및 이미지 삽입
        restartButton.setBounds(473, 644, restartIcon.getIconWidth()/3, restartIcon.getIconHeight()/3);
        restartButton.setIcon(new ImageIcon(scaledRestartIcon));
        // Action 설정
        restartButton.addActionListener(e -> {
            // 새 SwingUI 생성
            SwingUI newView = new SwingUI(controller, controller.model);
            controller.setView(newView); // 새로운 View를 주입

            frame.dispose(); // 이전 창 종료
            System.out.println("Restart Button Clicked");
        });
        // Hovering 처리
        ImageIcon restartHoverIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource(
                "/data/ui/button/button_restart_down.png"
        )));
        Image hoverScaled = restartHoverIcon.getImage().getScaledInstance(scaledRestartIcon.getWidth(null), scaledRestartIcon.getHeight(null), Image.SCALE_SMOOTH);
        restartButton.setRolloverIcon(new ImageIcon(hoverScaled));
        // 패널에 버튼 추가
        backgroundPanel.add(restartButton);
    }

    private void createPopupButton() {
        // 팝업 버튼 생성
        JButton popupButton = new JButton("Popup");
        popupButton.setBounds(473, 587, 621 / 3, 108 / 3);

        // 이미지 처리
        ImageIcon popupIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource(
                "/data/ui/button/button_custom_up.png"
        )));
        Image scaledPopupIcon = popupIcon.getImage().getScaledInstance(
                621 / 3,
                108 / 3,
                Image.SCALE_SMOOTH);
        popupButton.setIcon(new ImageIcon(scaledPopupIcon));

        // Hovering 처리
        ImageIcon popupHoverIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource(
                "/data/ui/button/button_custom_down.png"
        )));
        Image hoverScaled = popupHoverIcon.getImage().getScaledInstance(621 / 3, 108 / 3, Image.SCALE_SMOOTH);
        popupButton.setRolloverIcon(new ImageIcon(hoverScaled));

        // 버튼 옵션 설정
        popupButton.setBorderPainted(false);
        popupButton.setContentAreaFilled(false);
        popupButton.setFocusPainted(false);
        popupButton.setOpaque(false);
        popupButton.setText("");

        // Action 설정
        popupButton.addActionListener(e -> {
            // 팝업 버튼 클릭 시 동작
            System.out.println("Popup Button Clicked");
            // 팝업 창 생성
            // JOptionPane.showMessageDialog(frame, "Custom choice button clicked!", "지정 윷 던지기", JOptionPane.INFORMATION_MESSAGE);
            // 팝업 창에 대한 추가 동작을 여기에 추가할 수 있습니다.
            // 예: 게임 설정 변경, 사용자 입력 받기 등
            int POPUP_WIDTH = 1881 / 3 + 10;
            int POPUP_HEIGHT = 342 / 3 + 35;

            // 팝업의 배경 이미지 설정
            ImageIcon popupBgIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource(
                    "/data/ui/custom/custom_steady.png"
            )));
            // 이미지 처리
            Image scaledPopupBg = popupBgIcon.getImage().getScaledInstance(
                    popupBgIcon.getIconWidth() / 3,
                    popupBgIcon.getIconHeight() / 3,
                    Image.SCALE_SMOOTH);
            JLabel popupBgLabel = new JLabel(new ImageIcon(scaledPopupBg));
            popupBgLabel.setBounds(0, 0, scaledPopupBg.getWidth(null), scaledPopupBg.getHeight(null));
            JDialog popupDialog = new JDialog(frame, "Custom Choice", true);
            popupDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            popupDialog.setSize(POPUP_WIDTH, POPUP_HEIGHT);
            popupDialog.setLocationRelativeTo(frame);
            popupDialog.setLayout(null);
            // 팝업 창에 대한 추가 동작을 여기에 추가할 수 있습니다.

            int BUTTON_WIDTH = POPUP_WIDTH / 6 - 5;
            int BUTTON_Y_ERROR_SIZE = -20;

            JButton popupButton1 = new JButton("Custom 1");
            popupButton1.setBorderPainted(false);
            popupButton1.setContentAreaFilled(false);
            popupButton1.setFocusPainted(false);
            popupButton1.setOpaque(false);
            popupButton1.setText("");
            popupButton1.setBounds(0, BUTTON_Y_ERROR_SIZE, POPUP_WIDTH/6, POPUP_HEIGHT);
            popupButton1.addActionListener(e1 -> {
                // 팝업 버튼 클릭 시 동작
                System.out.println("Custom 1 Button Clicked");
                // 팝업 창에 대한 추가 동작을 여기에 추가할 수 있습니다.
                // 예: 게임 설정 변경, 사용자 입력 받기 등
                controller.handleManualThrow(1);
                // 팝업 창 닫기
                popupDialog.dispose();
            });
            popupBgLabel.add(popupButton1);

            JButton popupButton2 = new JButton("Custom 2");
            popupButton2.setBounds(BUTTON_WIDTH,BUTTON_Y_ERROR_SIZE, BUTTON_WIDTH, POPUP_HEIGHT);
            popupButton2.setBorderPainted(false);
            popupButton2.setContentAreaFilled(false);
            popupButton2.setFocusPainted(false);
            popupButton2.setOpaque(false);
            popupButton2.setText("");
            popupButton2.addActionListener(e1 -> {
                // 팝업 버튼 클릭 시 동작
                System.out.println("Custom 2 Button Clicked");
                // 팝업 창에 대한 추가 동작을 여기에 추가할 수 있습니다.
                // 예: 게임 설정 변경, 사용자 입력 받기 등
                controller.handleManualThrow(2);
                // 팝업 창 닫기
                popupDialog.dispose();
            });
            popupBgLabel.add(popupButton2);

            JButton popupButton3 = new JButton("Custom 3");
            popupButton3.setBounds(BUTTON_WIDTH*2,BUTTON_Y_ERROR_SIZE, BUTTON_WIDTH, POPUP_HEIGHT);
            popupButton3.setBorderPainted(false);
            popupButton3.setContentAreaFilled(false);
            popupButton3.setFocusPainted(false);
            popupButton3.setOpaque(false);
            popupButton3.setText("");
            popupButton3.addActionListener(e1 -> {
                // 팝업 버튼 클릭 시 동작
                System.out.println("Custom 3 Button Clicked");
                // 팝업 창에 대한 추가 동작을 여기에 추가할 수 있습니다.
                // 예: 게임 설정 변경, 사용자 입력 받기 등
                controller.handleManualThrow(3);
                // 팝업 창 닫기
                popupDialog.dispose();
            });
            popupBgLabel.add(popupButton3);

            JButton popupButton4 = new JButton("Custom 4");
            popupButton4.setBounds(BUTTON_WIDTH*3,BUTTON_Y_ERROR_SIZE, BUTTON_WIDTH, POPUP_HEIGHT);
            popupButton4.setBorderPainted(false);
            popupButton4.setContentAreaFilled(false);
            popupButton4.setFocusPainted(false);
            popupButton4.setOpaque(false);
            popupButton4.setText("");
            popupButton4.addActionListener(e1 -> {
                // 팝업 버튼 클릭 시 동작
                System.out.println("Custom 4 Button Clicked");
                // 팝업 창에 대한 추가 동작을 여기에 추가할 수 있습니다.
                // 예: 게임 설정 변경, 사용자 입력 받기 등
                controller.handleManualThrow(4);
                // 팝업 창 닫기
                popupDialog.dispose();
            });
            popupBgLabel.add(popupButton4);

            JButton popupButton5 = new JButton("Custom 5");
            popupButton5.setBounds(BUTTON_WIDTH*4,BUTTON_Y_ERROR_SIZE, BUTTON_WIDTH, POPUP_HEIGHT);
            popupButton5.setBorderPainted(false);
            popupButton5.setContentAreaFilled(false);
            popupButton5.setFocusPainted(false);
            popupButton5.setOpaque(false);
            popupButton5.setText("");
            popupButton5.addActionListener(e1 -> {
                // 팝업 버튼 클릭 시 동작
                System.out.println("Custom 5 Button Clicked");
                // 팝업 창에 대한 추가 동작을 여기에 추가할 수 있습니다.
                // 예: 게임 설정 변경, 사용자 입력 받기 등
                controller.handleManualThrow(5);
                // 팝업 창 닫기
                popupDialog.dispose();
            });
            popupBgLabel.add(popupButton5);

            JButton popupButton6 = new JButton("Custom 6");
            popupButton6.setBounds(BUTTON_WIDTH*5,BUTTON_Y_ERROR_SIZE, BUTTON_WIDTH, POPUP_HEIGHT);
            popupButton6.setBorderPainted(false);
            popupButton6.setContentAreaFilled(false);
            popupButton6.setFocusPainted(false);
            popupButton6.setOpaque(false);
            popupButton6.setText("");
            popupButton6.addActionListener(e1 -> {
                // 팝업 버튼 클릭 시 동작
                System.out.println("Custom 6 Button Clicked");
                // 팝업 창에 대한 추가 동작을 여기에 추가할 수 있습니다.
                // 예: 게임 설정 변경, 사용자 입력 받기 등
                controller.handleManualThrow(-1);
                // 팝업 창 닫기
                popupDialog.dispose();
            });
            popupBgLabel.add(popupButton6);
            popupDialog.add(popupBgLabel);
            popupDialog.setVisible(true);
            popupDialog.setResizable(false);
        });

        // Hovering 처리

        backgroundPanel.add(popupButton);
    }

    private void adjustPiecePosition(JLabel pieceLabel, String nodeId) { // -> updateBoard()
        Icon icon = pieceLabel.getIcon();
        Point position = boardButtonPositions.get(nodeId);

        pieceLabel.setBounds(
                position.x - ((ImageIcon) icon).getIconWidth() / POSITION_ADJUSTMENT_FACTOR,
                position.y - ((ImageIcon) icon).getIconHeight() / POSITION_ADJUSTMENT_FACTOR,
                ((ImageIcon) icon).getIconWidth(),
                ((ImageIcon) icon).getIconHeight());
    }

    private void removePreviousPieces() { // -> updateBoard()
        // 기존 말들을 패널에서 제거
        piecePositions.values().forEach(backgroundPanel::remove);
        piecePositions.clear();
    }
    // ------------------ //

    public void updatePlayerScore() {
        int[] notStartedCount1 = model.getCountOfPieceAtStart();
        for (int i = 0; i < numberOfPlayers; i++) {
            // 플레이어의 점수 가져오기
            int notStartedCount = notStartedCount1[i];

            // 이미지 처리
            ImageIcon playerScoreIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource(
                    "/data/ui/score/player" + (i + 1) + "/player" + (i + 1) + "_" + notStartedCount + ".png"
            )));
            Image scaledPlayerScore = playerScoreIcon.getImage().getScaledInstance(
                    playerScoreIcon.getIconWidth() / 3,
                    playerScoreIcon.getIconHeight() / 3,
                    Image.SCALE_SMOOTH);

            // 사진 교체
            playerScoreLabel[i + 1].setIcon(new ImageIcon(scaledPlayerScore));
        }
    }


    // ------ update turn ------ //
    public void updateTurn() {
        // 이미지 처리 및 보정
        int playerID = model.getCurrentPlayerNumber();
        ImageIcon turnIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource(
                "/data/ui/turn/turn_" + playerID + ".png"
        )));
        Image scaledTurnIcon = turnIcon.getImage().getScaledInstance(
                turnIcon.getIconWidth() / 3,
                turnIcon.getIconHeight() / 3,
                Image.SCALE_SMOOTH);
        // 사진 교체
        turnLabel.setIcon(new ImageIcon(scaledTurnIcon));
    }
}
